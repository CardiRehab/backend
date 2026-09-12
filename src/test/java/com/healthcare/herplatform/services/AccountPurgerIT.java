package com.healthcare.herplatform.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.FileSystemUtils;

import com.healthcare.herplatform.entity.Contactus;
import com.healthcare.herplatform.entity.PasswordResetToken;
import com.healthcare.herplatform.entity.SecondOpinionAttachment;
import com.healthcare.herplatform.entity.SecondOpinionRequest;
import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.repository.ContactusRepository;
import com.healthcare.herplatform.repository.PasswordResetTokenRepository;
import com.healthcare.herplatform.repository.SecondOpinionRequestRepository;
import com.healthcare.herplatform.repository.UserRepository;

/**
 * Permanent account deletion must take the second-opinion uploads with it: the
 * rows, and the reports and videos on disk. Disk deletes can't be rolled back,
 * so they must wait for the purge transaction to commit.
 *
 * <p>Fixture: patient "purge-me" (being deleted) and patient "keep-me" (not),
 * each with one second-opinion request carrying one file, one contact-form
 * message and one password reset token.
 */
@SpringBootTest
@ActiveProfiles("test")
class AccountPurgerIT {

	private static final String DOOMED = "purge-me";
	private static final String DOOMED_EMAIL = "purge.me@example.com";
	private static final String KEPT = "keep-me";
	private static final String KEPT_EMAIL = "keep.me@example.com";

	@Autowired private AccountPurger accountPurger;
	@Autowired private UserRepository userRepository;
	@Autowired private SecondOpinionRequestRepository requestRepo;
	@Autowired private ContactusRepository contactusRepository;
	@Autowired private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired private PlatformTransactionManager transactionManager;

	@Value("${app.soha.upload-dir}")
	private String uploadDir;

	private User doomed;
	private Path doomedFile;
	private Path keptFile;

	@BeforeEach
	void seed() throws Exception {
		doomed = userRepository.save(patient(DOOMED, DOOMED_EMAIL, "9000000001"));
		userRepository.save(patient(KEPT, KEPT_EMAIL, "9000000002"));

		doomedFile = secondOpinionWithFile(DOOMED);
		keptFile = secondOpinionWithFile(KEPT);

		// The contact form's email is typed by hand; the purge must still match it.
		contactusRepository.save(contact("PURGE.Me@Example.com"));
		contactusRepository.save(contact(KEPT_EMAIL));

		passwordResetTokenRepository.save(new PasswordResetToken("doomed-token", DOOMED_EMAIL));
		passwordResetTokenRepository.save(new PasswordResetToken("kept-token", KEPT_EMAIL));
	}

	@AfterEach
	void cleanUp() {
		requestRepo.deleteAll(requestRepo.findByPatientUsernameOrderByCreatedAtDesc(DOOMED));
		requestRepo.deleteAll(requestRepo.findByPatientUsernameOrderByCreatedAtDesc(KEPT));
		contactusRepository.deleteAll();
		passwordResetTokenRepository.deleteAll();
		userRepository.findByUsername(DOOMED).ifPresent(userRepository::delete);
		userRepository.findByUsername(KEPT).ifPresent(userRepository::delete);
		FileSystemUtils.deleteRecursively(Paths.get(uploadDir).toFile());
	}

	@Test
	@DisplayName("purge removes the patient's second-opinion rows and files, contact messages and reset tokens")
	void purgeRemovesEverythingOwnedByTheAccount() {
		accountPurger.purge(doomed.getId(), DOOMED, DOOMED_EMAIL);

		assertThat(userRepository.findByUsername(DOOMED)).isEmpty();
		assertThat(requestRepo.findByPatientUsernameOrderByCreatedAtDesc(DOOMED)).isEmpty();
		assertThat(doomedFile).doesNotExist();
		assertThat(doomedFile.getParent()).doesNotExist();
		assertThat(passwordResetTokenRepository.findByToken("doomed-token")).isEmpty();
		assertThat(contactEmails()).containsExactly(KEPT_EMAIL);

		// Nobody else's data is touched.
		assertThat(requestRepo.findByPatientUsernameOrderByCreatedAtDesc(KEPT)).hasSize(1);
		assertThat(keptFile).exists();
		assertThat(passwordResetTokenRepository.findByToken("kept-token")).isPresent();
	}

	@Test
	@DisplayName("a purge that rolls back leaves the files on disk")
	void rolledBackPurgeKeepsFiles() {
		new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
			accountPurger.purge(doomed.getId(), DOOMED, DOOMED_EMAIL);
			status.setRollbackOnly();
		});

		assertThat(userRepository.findByUsername(DOOMED)).isPresent();
		assertThat(requestRepo.findByPatientUsernameOrderByCreatedAtDesc(DOOMED)).hasSize(1);
		assertThat(doomedFile).exists();
	}

	private Path secondOpinionWithFile(String username) throws Exception {
		SecondOpinionRequest request = new SecondOpinionRequest();
		request.setPatientUsername(username);
		request.setDescription("Echo report");
		request = requestRepo.save(request);

		String storedPath = request.getId() + "/report.pdf";
		Path file = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(storedPath);
		Files.createDirectories(file.getParent());
		Files.write(file, new byte[] { 1, 2, 3 });

		SecondOpinionAttachment attachment = new SecondOpinionAttachment();
		attachment.setRequest(request);
		attachment.setFileName("report.pdf");
		attachment.setFileType("application/pdf");
		attachment.setFileSize(3);
		attachment.setStoredPath(storedPath);
		request.getAttachments().add(attachment);
		requestRepo.save(request);
		return file;
	}

	private java.util.List<String> contactEmails() {
		return contactusRepository.findAll().stream().map(Contactus::getEmail).collect(Collectors.toList());
	}

	private static Contactus contact(String email) {
		return new Contactus("t-" + email, new Date(), "Test", email, "0000000000", "Hello", "Message");
	}

	// users.phone is unique, so each fixture patient needs its own.
	private static User patient(String username, String email, String phone) {
		return new User(new Date(), username, email, "not-a-real-hash", "1970-01-01", "NA", "Test", "NA",
				"Patient", "NA", phone, "India", "NA", "NA", "NA", "Deleting", "9999-12-31", "REG" + username);
	}
}
