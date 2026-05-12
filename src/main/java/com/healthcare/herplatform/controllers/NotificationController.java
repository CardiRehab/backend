package com.healthcare.herplatform.controllers;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.herplatform.entity.DeviceToken;
import com.healthcare.herplatform.payloads.DeviceTokenRequest;
import com.healthcare.herplatform.payloads.MessageResponse;
import com.healthcare.herplatform.repository.DeviceTokenRepository;
import com.healthcare.herplatform.services.UserDetailsImpl;

/**
 * Lets a signed-in app register/unregister the FCM token for the device it is
 * running on. Called by the mobile app right after login and on logout.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	private DeviceTokenRepository deviceTokenRepository;

	@PostMapping("/register-device")
	public ResponseEntity<?> registerDevice(@RequestBody DeviceTokenRequest body, Authentication authentication) {
		String username = currentUsername(authentication);
		if (username == null) {
			return ResponseEntity.status(401).body(new MessageResponse("Not authenticated"));
		}
		if (body == null || body.getToken() == null || body.getToken().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("token is required"));
		}
		String token = body.getToken().trim();
		String platform = body.getPlatform() == null ? null : body.getPlatform().trim().toLowerCase();

		Optional<DeviceToken> existing = deviceTokenRepository.findByToken(token);
		DeviceToken dt = existing.orElseGet(DeviceToken::new);
		dt.setUsername(username);
		dt.setToken(token);
		if (platform != null && !platform.isEmpty()) {
			dt.setPlatform(platform);
		}
		dt.setUpdatedAt(new Date());
		deviceTokenRepository.save(dt);
		return ResponseEntity.ok(new MessageResponse("Device registered"));
	}

	@PostMapping("/unregister-device")
	public ResponseEntity<?> unregisterDevice(@RequestBody DeviceTokenRequest body, Authentication authentication) {
		if (body == null || body.getToken() == null || body.getToken().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("token is required"));
		}
		deviceTokenRepository.deleteByToken(body.getToken().trim());
		return ResponseEntity.ok(new MessageResponse("Device unregistered"));
	}

	private String currentUsername(Authentication authentication) {
		if (authentication == null || authentication.getPrincipal() == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			return ((UserDetailsImpl) principal).getUsername();
		}
		return authentication.getName();
	}
}
