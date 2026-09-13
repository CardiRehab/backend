package com.healthcare.herplatform.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.healthcare.herplatform.repository.AssignedUsersRepository;
import com.healthcare.herplatform.services.UserDetailsImpl;

/**
 * Single object-level authorisation decision point for patient-owned data (V-01).
 *
 * <p>A role annotation only proves the caller is logged in as <em>someone</em>. Every endpoint
 * that names a patient — directly through a {@code {userId}} path variable, through an id in the
 * request body, or indirectly through a child record whose owner must be resolved — must also ask
 * this guard whether that particular principal may act on that particular patient.
 *
 * <p><b>Ownership model.</b> Table {@code user_assignment} holds one row per patient:
 * {@code userid} is the <em>clinician</em> (CRSPL/LHCP) and {@code assigneduserid} is the
 * <em>patient</em>. Established from {@code AssignNewUserModel}'s field comments, the signup path in
 * {@code AuthController} (clinician id first, new patient's id second), the controller/query pairing
 * in {@code AssignedUsersController}, and the ids the web client passes to each of those two
 * endpoints.
 *
 * <p><b>Rules.</b> PATIENT may act on their own id only. CRSPL/LHCP may act on a patient assigned to
 * them. ADMIN is denied: the only ADMIN account is the Board seed, documented in {@code DataSeeder}
 * as existing to manage the external resource directory, and no client calls these endpoints as
 * ADMIN. Anything else — including a principal this guard cannot identify — is denied.
 *
 * <p>Every denial is logged at WARN with the principal, the attempted subject and the endpoint, and
 * never with patient data. Denials surface as a bare 403 via {@link EmptyBodyAccessDeniedHandler} so
 * that "does not exist" and "is not yours" are indistinguishable to a caller probing for ids.
 */
@Component("patientAccessGuard")
public class PatientAccessGuard {

	private static final Logger log = LoggerFactory.getLogger(PatientAccessGuard.class);

	private static final String ROLE_PATIENT = "ROLE_PATIENT";
	private static final String ROLE_CRSPL = "ROLE_CRSPL";
	private static final String ROLE_LHCP = "ROLE_LHCP";

	private final AssignedUsersRepository assignedUsersRepository;

	public PatientAccessGuard(AssignedUsersRepository assignedUsersRepository) {
		this.assignedUsersRepository = assignedUsersRepository;
	}

	/**
	 * May this principal act on this patient? Called from {@code @PreAuthorize} on endpoints that
	 * name the patient directly, and from {@link #assertCanAccessPatient} at resolved call sites.
	 */
	public boolean canAccessPatient(Authentication authentication, long subjectUserId) {
		Long principalId = principalId(authentication);
		if (principalId == null) {
			deny(authentication, subjectUserId, "unidentified principal");
			return false;
		}

		if (hasRole(authentication, ROLE_PATIENT)) {
			// Compare as long. Narrowing the principal's Long id to int would let a large id
			// truncate onto a different user's id and silently match.
			if (principalId.longValue() == subjectUserId) {
				return true;
			}
			deny(authentication, subjectUserId, "patient may only access their own records");
			return false;
		}

		if (hasRole(authentication, ROLE_CRSPL) || hasRole(authentication, ROLE_LHCP)) {
			if (isAssigned(principalId.longValue(), subjectUserId)) {
				return true;
			}
			deny(authentication, subjectUserId, "patient not assigned to this clinician");
			return false;
		}

		deny(authentication, subjectUserId, "role not permitted on patient records");
		return false;
	}

	/**
	 * May this principal act as the user named in the path? For endpoints where the id is the
	 * caller's own — a clinician asking for their caseload, a patient asking who their clinician
	 * is — not a patient being acted upon.
	 */
	public boolean canActAsSelf(Authentication authentication, long userId) {
		Long principalId = principalId(authentication);
		if (principalId == null) {
			deny(authentication, userId, "unidentified principal");
			return false;
		}
		if (principalId.longValue() == userId) {
			return true;
		}
		deny(authentication, userId, "caller may only act as themselves");
		return false;
	}

	/**
	 * May this principal act as the named user? For the endpoints keyed by username rather than
	 * id — chat history is addressed by {@code {userName}}, not a numeric id.
	 */
	public boolean canActAsUsername(Authentication authentication, String userName) {
		if (authentication == null || authentication.getName() == null) {
			denyResolution(authentication, String.valueOf(userName), "unidentified principal");
			return false;
		}
		if (authentication.getName().equals(userName)) {
			return true;
		}
		denyResolution(authentication, String.valueOf(userName), "caller may only act as themselves");
		return false;
	}

	/** Refuses a request outright: logs the denial, then throws for the bare-403 handler. */
	public void denyAccess(Authentication authentication, Object attemptedSubject, String reason) {
		denyResolution(authentication, String.valueOf(attemptedSubject), reason);
		throw new AccessDeniedException("Forbidden");
	}

	/** Throwing form of {@link #canAccessPatient}, for call sites that resolve the owner first. */
	public void assertCanAccessPatient(Authentication authentication, long subjectUserId) {
		if (!canAccessPatient(authentication, subjectUserId)) {
			throw new AccessDeniedException("Forbidden");
		}
	}

	/**
	 * Asserts access to every patient named in a batch, denying the whole request if any one of
	 * them fails. A partial write on a clinical plan is worse than a clean refusal: the caller
	 * would see success with part of the plan silently missing.
	 */
	public void assertCanAccessAllPatients(Authentication authentication, List<Integer> subjectUserIds) {
		if (subjectUserIds == null || subjectUserIds.isEmpty()) {
			return;
		}
		for (Integer subjectUserId : subjectUserIds) {
			if (subjectUserId == null) {
				denyResolution(authentication, "null", "batch entry named no patient");
				throw new AccessDeniedException("Forbidden");
			}
			assertCanAccessPatient(authentication, subjectUserId.longValue());
		}
	}

	/**
	 * Denies a request whose subject could not be resolved — an indirect endpoint whose child
	 * record does not exist. Fails closed, and returns the same bare 403 as "not yours" so the
	 * pair cannot be used to test whether an id exists.
	 */
	public void denyUnresolved(Authentication authentication, Object attemptedId, String reason) {
		denyAccess(authentication, attemptedId, reason);
	}

	/** True when the patient is assigned to this clinician. */
	private boolean isAssigned(long clinicianUserId, long patientUserId) {
		// user_assignment.userid is an int column; a principal id outside int range cannot
		// match any row, so it is a denial rather than a truncating cast.
		if (clinicianUserId < Integer.MIN_VALUE || clinicianUserId > Integer.MAX_VALUE) {
			return false;
		}
		return assignedUsersRepository.existsByUserIdAndAssignedUserId((int) clinicianUserId,
				Long.valueOf(patientUserId));
	}

	private static Long principalId(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof UserDetailsImpl)) {
			return null;
		}
		return ((UserDetailsImpl) principal).getId();
	}

	private static boolean hasRole(Authentication authentication, String role) {
		if (authentication == null || authentication.getAuthorities() == null) {
			return false;
		}
		return authentication.getAuthorities().stream().anyMatch(a -> role.equals(a.getAuthority()));
	}

	private void deny(Authentication authentication, long subjectUserId, String reason) {
		denyResolution(authentication, Long.toString(subjectUserId), reason);
	}

	/**
	 * The audit line for a refused request. Principal is logged by username because that is what
	 * the rest of the application logs and what an auditor can trace; the subject is logged as a
	 * bare id. No record content is ever logged here — the full audit trail is V-15, not this.
	 */
	private void denyResolution(Authentication authentication, String subject, String reason) {
		log.warn("AUTHZ_DENY principal={} roles={} subject={} endpoint={} reason={}",
				authentication != null ? authentication.getName() : "anonymous",
				authentication != null && authentication.getAuthorities() != null
						? authentication.getAuthorities().toString()
						: "[]",
				subject,
				endpoint(),
				reason);
	}

	private static String endpoint() {
		ServletRequestAttributes attributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return "unknown";
		}
		HttpServletRequest request = attributes.getRequest();
		return request.getMethod() + " " + request.getRequestURI();
	}
}
