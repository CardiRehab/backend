package com.healthcare.herplatform.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * Initialises the Firebase Admin SDK from a service-account JSON file so the
 * backend can send push notifications via FCM.
 *
 * <p>Resolution order for the credentials file:
 * <ol>
 *   <li>The path given by {@code firebase.credentials-path} on the filesystem.</li>
 *   <li>The same name on the classpath (e.g. {@code src/main/resources/...}).</li>
 * </ol>
 *
 * <p>If {@code firebase.enabled} is false, or the file cannot be found, push
 * sending is simply disabled — the rest of the application is unaffected and
 * starts normally.
 */
@Configuration
public class FirebaseConfig {

	private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

	@Value("${firebase.enabled:true}")
	private boolean enabled;

	@Value("${firebase.credentials-path:firebase-service-account.json}")
	private String credentialsPath;

	@PostConstruct
	public void init() {
		if (!enabled) {
			log.info("Firebase push notifications disabled (firebase.enabled=false).");
			return;
		}
		if (!FirebaseApp.getApps().isEmpty()) {
			return; // already initialised
		}
		try (InputStream credentials = openCredentials()) {
			if (credentials == null) {
				log.warn("Firebase service-account file not found ('{}'); push notifications disabled.",
						credentialsPath);
				return;
			}
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(credentials))
					.build();
			FirebaseApp.initializeApp(options);
			log.info("Firebase Admin SDK initialised; push notifications enabled.");
		} catch (IOException e) {
			log.error("Failed to initialise Firebase Admin SDK; push notifications disabled.", e);
		}
	}

	private InputStream openCredentials() throws IOException {
		if (credentialsPath != null && !credentialsPath.trim().isEmpty()
				&& Files.exists(Paths.get(credentialsPath))) {
			return new FileInputStream(credentialsPath);
		}
		ClassPathResource cp = new ClassPathResource(
				credentialsPath != null && !credentialsPath.trim().isEmpty()
						? credentialsPath
						: "firebase-service-account.json");
		return cp.exists() ? cp.getInputStream() : null;
	}
}
