package com.healthcare.herplatform.controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.herplatform.entity.AssignedUsers;
import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.payloads.MessageResponse;
import com.healthcare.herplatform.payloads.UpdateProfileRequest;
import com.healthcare.herplatform.payloads.UserProfileResponse;
import com.healthcare.herplatform.repository.AssignedUsersRepository;
import com.healthcare.herplatform.repository.UserRepository;
import com.healthcare.herplatform.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AssignedUsersRepository assignedUsersRepository;

	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(Authentication authentication) {
		UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepository.findByUsername(principal.getUsername()).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
		}
		return ResponseEntity.ok(UserProfileResponse.fromEntity(user));
	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest req,
			Authentication authentication) {
		UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepository.findByUsername(principal.getUsername()).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
		}

		String fn = req.getFirstName() != null ? req.getFirstName().trim() : "";
		String ln = req.getLastName() != null ? req.getLastName().trim() : "";

		user.setFname(fn);
		user.setLname(ln);
		user.setPhone(req.getPhone() != null ? req.getPhone().trim() : "");
		user.setGender(req.getGender() != null ? req.getGender().trim() : "");
		if (req.getAddress() != null) {
			String addr = req.getAddress().trim();
			if (!addr.isEmpty()) {
				user.setAddress(addr);
			}
		}

		userRepository.save(user);
		syncPatientAssignmentDisplay(user);

		return ResponseEntity.ok(UserProfileResponse.fromEntity(user));
	}

	/**
	 * Initiates account deletion. The account is disabled immediately and a 30-day
	 * clock starts; a scheduled sweep permanently purges it once the window passes.
	 * The user may reactivate within the window via /api/auth/reactivate-account.
	 */
	@PostMapping("/delete-account")
	public ResponseEntity<?> deleteAccount(Authentication authentication) {
		UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepository.findByUsername(principal.getUsername()).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
		}

		user.setAccountstatus("Deleting");
		user.setDeletionRequestedAt(new Date());
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse(
				"Your account has been scheduled for deletion and will be permanently removed in 30 days. "
						+ "Sign in again before then to reactivate it."));
	}

	/**
	 * Keeps doctor-facing patient list in sync when a patient updates their profile.
	 */
	private void syncPatientAssignmentDisplay(User user) {
		List<AssignedUsers> rows = assignedUsersRepository.findByAssignedUserId(user.getId());
		if (rows == null || rows.isEmpty()) {
			return;
		}
		String displayName = (user.getFname() + " " + user.getLname()).trim();
		for (AssignedUsers row : rows) {
			row.setUserName(displayName);
			row.setPhone(user.getPhone());
			row.setGender(user.getGender());
			row.setEmailId(user.getEmail());
		}
		assignedUsersRepository.saveAll(rows);
	}
}
