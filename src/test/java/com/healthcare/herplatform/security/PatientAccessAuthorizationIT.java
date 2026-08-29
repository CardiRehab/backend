package com.healthcare.herplatform.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.healthcare.herplatform.entity.AssignedUsers;
import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.entity.UserJoinUserAssigned;
import com.healthcare.herplatform.entity.WeekName;
import com.healthcare.herplatform.repository.AssignedUsersRepository;
import com.healthcare.herplatform.repository.UserAssignmentRepository;
import com.healthcare.herplatform.repository.UserJoinUserAssignedRepository;
import com.healthcare.herplatform.repository.UserRepository;
import com.healthcare.herplatform.repository.WeekNameRepository;
import com.healthcare.herplatform.services.UserDetailsImpl;

/**
 * Evidence for audit finding V-01 (broken object-level authorisation / IDOR).
 *
 * <p>Each test drives a real HTTP request through the full security filter chain and the real
 * {@link PatientAccessGuard}, against real {@code user_assignment} rows. The principal is set
 * directly rather than via a JWT because the defect and its fix are in the authorisation layer;
 * authentication is not what is under test here.
 *
 * <p>Fixture: patients 101 and 202; clinician 501 is assigned patient 101 only; clinician 502 has
 * no patients. Only the tables the guard actually reads are seeded — it resolves ownership through
 * {@code user_assignment} and never loads the user row itself.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientAccessAuthorizationIT {

	private static final long PATIENT_A = 101L;
	private static final long PATIENT_B = 202L;
	private static final long CLINICIAN_WITH_PATIENT_A = 501L;
	private static final long CLINICIAN_WITH_NO_PATIENTS = 502L;
	private static final long BOARD_ADMIN = 900L;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserAssignmentRepository userAssignmentRepository;

	@Autowired
	private AssignedUsersRepository assignedUsersRepository;

	@Autowired
	private WeekNameRepository weekNameRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJoinUserAssignedRepository userJoinUserAssignedRepository;

	private int patientAWeekNameId;
	private int patientBWeekNameId;

	@BeforeEach
	void seed() {
		userAssignmentRepository.deleteAll();
		weekNameRepository.deleteAll();

		// user_assignment.userid = clinician, user_assignment.assigneduserid = patient.
		userAssignmentRepository.save(new AssignedUsers((int) CLINICIAN_WITH_PATIENT_A, PATIENT_A,
				"REG101", "clinician501", "patient101@example.test", "0000000000", "NA", "40", "patient101"));

		patientAWeekNameId = weekNameRepository.save(new WeekName((int) PATIENT_A, "Week 1")).getId();
		patientBWeekNameId = weekNameRepository.save(new WeekName((int) PATIENT_B, "Week 1")).getId();
	}

	private static UserDetailsImpl principal(long id, String username, String role) {
		List<GrantedAuthority> authorities =
				Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority(role));
		return new UserDetailsImpl(id, username, username + "@example.test", "Active", "9999-12-31",
				"irrelevant", authorities, "Test", "User");
	}

	private static UserDetailsImpl patientA() {
		return principal(PATIENT_A, "patient101", "ROLE_PATIENT");
	}

	private static UserDetailsImpl patientB() {
		return principal(PATIENT_B, "patient202", "ROLE_PATIENT");
	}

	private static UserDetailsImpl assignedClinician() {
		return principal(CLINICIAN_WITH_PATIENT_A, "clinician501", "ROLE_CRSPL");
	}

	private static UserDetailsImpl unassignedClinician() {
		return principal(CLINICIAN_WITH_NO_PATIENTS, "clinician502", "ROLE_CRSPL");
	}

	// ---------------------------------------------------------------- direct IDOR: the headline

	@Test
	@DisplayName("V-01: patient A requesting patient B's PHQ-9 is refused")
	void patientCannotReadAnotherPatientsPhq9() throws Exception {
		mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", PATIENT_B)
						.with(user(patientA())))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("V-01: patient A requesting their own PHQ-9 is allowed")
	void patientCanReadOwnPhq9() throws Exception {
		mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", PATIENT_A)
						.with(user(patientA())))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("V-01: a clinician requesting an assigned patient is allowed")
	void clinicianCanReadAssignedPatient() throws Exception {
		mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", PATIENT_A)
						.with(user(assignedClinician())))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("V-01: a clinician requesting an unassigned patient is refused")
	void clinicianCannotReadUnassignedPatient() throws Exception {
		mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", PATIENT_A)
						.with(user(unassignedClinician())))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", PATIENT_B)
						.with(user(assignedClinician())))
				.andExpect(status().isForbidden());
	}

	// ------------------------------------------------------------------------- indirect IDOR

	@Test
	@DisplayName("V-01 indirect: patient A requesting patient B's weekNameId is refused")
	void patientCannotReadAnotherPatientsWeekDays() throws Exception {
		mockMvc.perform(get("/activitiesdiary/getuserweekdays/{weekNameId}", patientBWeekNameId)
						.with(user(patientA())))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("V-01 indirect: patient A requesting their own weekNameId is allowed")
	void patientCanReadOwnWeekDays() throws Exception {
		mockMvc.perform(get("/activitiesdiary/getuserweekdays/{weekNameId}", patientAWeekNameId)
						.with(user(patientA())))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("V-01 indirect: an id that does not exist is refused, not reported as missing")
	void unknownWeekNameIdIsRefusedNotNotFound() throws Exception {
		mockMvc.perform(get("/activitiesdiary/getuserweekdays/{weekNameId}", 999999)
						.with(user(patientA())))
				.andExpect(status().isForbidden());
	}

	// ------------------------------------------------------------------------- no enumeration

	@Test
	@DisplayName("V-01: a refusal returns an empty body, so 'not yours' and 'does not exist' look alike")
	void refusalBodyIsEmpty() throws Exception {
		MvcResult notYours = mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", PATIENT_B)
						.with(user(patientA())))
				.andExpect(status().isForbidden())
				.andReturn();

		MvcResult doesNotExist = mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", 987654)
						.with(user(patientA())))
				.andExpect(status().isForbidden())
				.andReturn();

		assertThat(notYours.getResponse().getContentAsString()).isEmpty();
		assertThat(doesNotExist.getResponse().getContentAsString()).isEmpty();
	}

	// ------------------------------------------------------------------------------ role rules

	@Test
	@DisplayName("V-01: the Board ADMIN account is refused clinical records")
	void adminIsRefusedPatientRecords() throws Exception {
		mockMvc.perform(get("/assessmentforms/getphq9formdatabyuid/{userId}", PATIENT_A)
						.with(user(principal(BOARD_ADMIN, "boardadmin", "ROLE_ADMIN"))))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("V-01: a clinician cannot read another clinician's caseload")
	void clinicianCannotReadAnotherCliniciansCaseload() throws Exception {
		mockMvc.perform(get("/assignedusers/getconalluserrecords/{userId}", CLINICIAN_WITH_PATIENT_A)
						.with(user(unassignedClinician())))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("V-01: a patient cannot read another patient's assigned clinician")
	void patientCannotReadAnotherPatientsClinician() throws Exception {
		mockMvc.perform(get("/assignedusers/getconcrspluserrecords/{userId}", PATIENT_A)
						.with(user(patientB())))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/assignedusers/getconcrspluserrecords/{userId}", PATIENT_B)
						.with(user(patientB())))
				.andExpect(status().isOk());
	}

	// ------------------------------------------------------------------------------ write side

	@Test
	@DisplayName("V-01 write: patient A cannot file a PHQ-9 against patient B")
	void patientCannotWritePhq9ForAnotherPatient() throws Exception {
		mockMvc.perform(post("/assessmentforms/insertphq9")
						.with(user(patientA()))
						.contentType(MediaType.APPLICATION_JSON)
						.content(phq9Json(PATIENT_B)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("V-01 write: a clinician cannot file a PHQ-9 against an unassigned patient")
	void clinicianCannotWritePhq9ForUnassignedPatient() throws Exception {
		mockMvc.perform(post("/assessmentforms/insertphq9")
						.with(user(assignedClinician()))
						.contentType(MediaType.APPLICATION_JSON)
						.content(phq9Json(PATIENT_B)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("V-01 write: the other diary write paths reject a foreign patient id too")
	void activityWritesRejectForeignPatientId() throws Exception {
		// Note the JSON property is dateTime (the field is dtTime); @Valid runs during argument
		// resolution, i.e. before @PreAuthorize, so a malformed body would 400 before the guard
		// is ever consulted and would not prove anything.
		String activity = "{\"userid\":" + PATIENT_B + ",\"dateTime\":\"2026-08-29T10:00:00.000+00:00\","
				+ "\"activityName\":\"Walk\",\"preHR\":\"70\",\"postHR\":\"90\",\"results\":\"ok\","
				+ "\"rpeBorg\":\"11\",\"symptoms\":\"none\"}";
		mockMvc.perform(post("/activitiesdiary/insertactivity")
						.with(user(patientA()))
						.contentType(MediaType.APPLICATION_JSON)
						.content(activity))
				.andExpect(status().isForbidden());
	}

	// ------------------------------------------------------------------------------- the guard

	@Test
	@DisplayName("V-01: the assignment lookup reads userid as the clinician and assigneduserid as the patient")
	void assignmentDirectionIsAsDocumented() {
		assertThat(assignedUsersRepository.existsByUserIdAndAssignedUserId(
				(int) CLINICIAN_WITH_PATIENT_A, PATIENT_A)).isTrue();
		// The reverse pairing must not match: if these columns were read the other way round,
		// clinicians would be locked out of their own patients and patients could read each other.
		assertThat(assignedUsersRepository.existsByUserIdAndAssignedUserId(
				(int) PATIENT_A, CLINICIAN_WITH_PATIENT_A)).isFalse();
		assertThat(assignedUsersRepository.existsByUserIdAndAssignedUserId(
				(int) CLINICIAN_WITH_NO_PATIENTS, PATIENT_A)).isFalse();
	}

	/**
	 * The allowed path through {@code getconalluserrecords}, which is backed by a three-table
	 * native join over {@code users}, {@code user_assmt} and {@code user_assignment}. Seeded with
	 * real rows and real generated ids rather than the fixed ids the rest of the fixture uses,
	 * because the join is on {@code users.id}.
	 */
	@Test
	@DisplayName("V-01: a clinician reading their own caseload is allowed, and the join behind it runs")
	void clinicianCanReadOwnCaseload() throws Exception {
		// users.phone carries a unique constraint, so the two fixtures need distinct numbers.
		User clinician = userRepository.save(newUser("caseloadclinician", "9000000001"));
		User patient = userRepository.save(newUser("caseloadpatient", "9000000002"));

		AssignedUsers assignment = userAssignmentRepository.save(new AssignedUsers(
				clinician.getId().intValue(), patient.getId(), "REGCASE", clinician.getUsername(),
				patient.getEmail(), "0000000000", "NA", "40", patient.getUsername()));
		userJoinUserAssignedRepository.save(new UserJoinUserAssigned(patient.getId(), assignment.getId()));

		mockMvc.perform(get("/assignedusers/getconalluserrecords/{userId}", clinician.getId())
						.with(user(principal(clinician.getId(), clinician.getUsername(), "ROLE_CRSPL"))))
				.andExpect(status().isOk());
	}

	private static User newUser(String username, String phone) {
		return new User(new java.util.Date(), username, username + "@example.test", "irrelevant",
				"1970-01-01", "40", "Test", "NA", "User", "NA", phone, "India", "NA", "NA",
				"NA", "Active", "9999-12-31", "REG" + username.toUpperCase());
	}

	private static String phq9Json(long userid) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"userid\":").append(userid)
				.append(",\"dateTime\":\"2026-08-29T10:00:00.000+00:00\"");
		for (String q : Arrays.asList("phq1", "phq2", "phq3", "phq4", "phq5", "phq6", "phq7", "phq8", "phq9")) {
			sb.append(",\"").append(q).append("\":\"0\"");
		}
		sb.append(",\"diffLevel\":\"Not difficult at all\"}");
		return sb.toString();
	}
}
