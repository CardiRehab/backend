package com.healthcare.herplatform.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.healthcare.herplatform.entity.DeviceToken;
import com.healthcare.herplatform.repository.DeviceTokenRepository;

/**
 * Sends push notifications to a user's registered devices via FCM. Every method
 * is best-effort: if Firebase is not configured, or a send fails, it logs and
 * returns — callers never need to handle exceptions, so this can be safely
 * called from request/message handlers without affecting their outcome.
 */
@Service
public class PushNotificationService {

	private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

	@Autowired
	private DeviceTokenRepository deviceTokenRepository;

	private boolean firebaseReady() {
		return !FirebaseApp.getApps().isEmpty();
	}

	/**
	 * Sends a notification (title + body, plus optional data payload) to every
	 * device registered for {@code username}. Tokens FCM reports as permanently
	 * invalid are removed.
	 */
	public void sendToUser(String username, String title, String body, Map<String, String> data) {
		if (username == null || username.trim().isEmpty()) {
			return;
		}
		if (!firebaseReady()) {
			return;
		}
		List<DeviceToken> tokens;
		try {
			tokens = deviceTokenRepository.findByUsername(username);
		} catch (Exception e) {
			log.warn("Could not load device tokens for '{}': {}", username, e.getMessage());
			return;
		}
		if (tokens == null || tokens.isEmpty()) {
			return;
		}
		for (DeviceToken dt : tokens) {
			sendToToken(dt.getToken(), title, body, data);
		}
	}

	private void sendToToken(String token, String title, String body, Map<String, String> data) {
		try {
			Message.Builder builder = Message.builder()
					.setToken(token)
					.setNotification(Notification.builder()
							.setTitle(title)
							.setBody(body)
							.build())
					.setAndroidConfig(AndroidConfig.builder()
							.setPriority(AndroidConfig.Priority.HIGH)
							.build())
					.setApnsConfig(ApnsConfig.builder()
							.setAps(Aps.builder().setSound("default").build())
							.build());
			if (data != null) {
				for (Map.Entry<String, String> e : data.entrySet()) {
					if (e.getKey() != null && e.getValue() != null) {
						builder.putData(e.getKey(), e.getValue());
					}
				}
			}
			FirebaseMessaging.getInstance().send(builder.build());
		} catch (FirebaseMessagingException e) {
			MessagingErrorCode code = e.getMessagingErrorCode();
			if (code == MessagingErrorCode.UNREGISTERED || code == MessagingErrorCode.INVALID_ARGUMENT) {
				try {
					deviceTokenRepository.deleteByToken(token);
					log.info("Removed stale FCM token ({}).", code);
				} catch (Exception ignored) {
					// best effort
				}
			} else {
				log.warn("FCM send failed ({}): {}", code, e.getMessage());
			}
		} catch (Exception e) {
			log.warn("Unexpected error sending push notification: {}", e.getMessage());
		}
	}
}
