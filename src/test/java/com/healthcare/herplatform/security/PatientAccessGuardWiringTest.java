package com.healthcare.herplatform.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;

import com.healthcare.herplatform.controllers.ActivitiesDiaryController;
import com.healthcare.herplatform.controllers.AssessmentFormsController;
import com.healthcare.herplatform.controllers.AssignedUsersController;
import com.healthcare.herplatform.controllers.ChatAttachmentsController;

/**
 * Makes a forgotten authorisation check a build failure rather than a finding.
 *
 * <p>Every request-handling method on the controllers that serve patient-owned data must do one of
 * three things, and this test fails if a new one does none of them:
 *
 * <ul>
 *   <li>name {@code patientAccessGuard} in its {@code @PreAuthorize} — the usual case, where the
 *       subject is a path variable or a field on the request body;</li>
 *   <li>appear in {@link #GUARDED_IN_METHOD_BODY}, for endpoints whose subject has to be resolved
 *       through a parent record before the guard can be asked;</li>
 *   <li>appear in {@link #NO_PATIENT_SUBJECT}, for endpoints that name no patient at all.</li>
 * </ul>
 *
 * <p>The point is the default: a new endpoint added to these controllers is insecure only if
 * someone deliberately adds it to one of the exemption lists, not if they simply forget.
 */
class PatientAccessGuardWiringTest {

	private static final List<Class<?>> CONTROLLERS = Arrays.asList(
			AssessmentFormsController.class,
			ActivitiesDiaryController.class,
			AssignedUsersController.class,
			ChatAttachmentsController.class);

	/** Endpoints whose owning patient is resolved in the method body, then passed to the guard. */
	private static final Set<String> GUARDED_IN_METHOD_BODY = new HashSet<>(Arrays.asList(
			// owner resolved through week_name
			"ActivitiesDiaryController#getSingleUserWeekDaysById",
			// owner resolved through the stored assigned_activities row
			"ActivitiesDiaryController#updateAssignedActivityById",
			// a batch naming one patient per element; all-or-nothing
			"ActivitiesDiaryController#insertAssignedActivities",
			// owner is the conversation the file was sent in
			"ChatAttachmentsController#downloadFile",
			"ChatAttachmentsController#deleteFile",
			// already correct before V-01: sender-only, inside a recency window
			"ChatAttachmentsController#deleteMessage"));

	/** Endpoints that name no patient: reference data, config, or the caller's own upload. */
	private static final Set<String> NO_PATIENT_SUBJECT = new HashSet<>(Arrays.asList(
			"ActivitiesDiaryController#getAllActivitiesType",
			"ChatAttachmentsController#uploadFile",
			"ChatAttachmentsController#getMessageDeleteWindowHours"));

	@Test
	@DisplayName("every patient-data endpoint asks the guard, or is explicitly declared not to need it")
	void everyEndpointIsAccountedFor() {
		List<String> unguarded = new ArrayList<>();

		for (Class<?> controller : CONTROLLERS) {
			for (Method method : controller.getDeclaredMethods()) {
				if (!AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class)) {
					continue;
				}
				String key = controller.getSimpleName() + "#" + method.getName();

				PreAuthorize preAuthorize = AnnotatedElementUtils.findMergedAnnotation(method, PreAuthorize.class);
				boolean guardedByAnnotation = preAuthorize != null
						&& preAuthorize.value().contains("patientAccessGuard");
				if (guardedByAnnotation || NO_PATIENT_SUBJECT.contains(key)) {
					continue;
				}
				if (GUARDED_IN_METHOD_BODY.contains(key)) {
					// A body-resolved guard needs the principal, so the method must take one.
					assertThat(Arrays.asList(method.getParameterTypes()))
							.as("%s resolves its subject in the method body and must take Authentication", key)
							.contains(Authentication.class);
					continue;
				}
				unguarded.add(key);
			}
		}

		assertThat(unguarded)
				.as("endpoints serving patient data with no authorisation check — guard them, or add "
						+ "them to NO_PATIENT_SUBJECT with a reason")
				.isEmpty();
	}

	@Test
	@DisplayName("the exemption lists name real methods, so a rename cannot quietly widen them")
	void exemptionsReferenceExistingMethods() {
		Set<String> declared = new HashSet<>();
		for (Class<?> controller : CONTROLLERS) {
			for (Method method : controller.getDeclaredMethods()) {
				declared.add(controller.getSimpleName() + "#" + method.getName());
			}
		}
		assertThat(declared).containsAll(GUARDED_IN_METHOD_BODY);
		assertThat(declared).containsAll(NO_PATIENT_SUBJECT);
	}
}
