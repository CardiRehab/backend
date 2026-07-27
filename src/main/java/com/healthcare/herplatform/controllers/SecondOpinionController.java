package com.healthcare.herplatform.controllers;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.herplatform.entity.SecondOpinionAttachment;
import com.healthcare.herplatform.entity.SecondOpinionRequest;
import com.healthcare.herplatform.entity.SecondOpinionStatus;
import com.healthcare.herplatform.services.SecondOpinionService;
import com.healthcare.herplatform.services.UserDetailsImpl;

/**
 * Second Opinion on Heart Ailments (SOHA): patients submit a request with
 * documents/videos attached; reviewers read them, download the files and record
 * the cardiologist's opinion. Board ADMIN sees every request; a CRSPL/LHCP only
 * the requests of patients assigned to them (user_assignment).
 *
 * Flow is two-step on purpose: create the request first (JSON), then upload
 * attachments ONE file per call — so a large echo video never collides with the
 * multipart request-size cap and each file gets its own upload progress.
 */
@RestController
@RequestMapping("/api/second-opinions")
public class SecondOpinionController {

	private final SecondOpinionService service;

	public SecondOpinionController(SecondOpinionService service) {
		super();
		this.service = service;
	}

	private static boolean isAdmin(Authentication auth) {
		return auth != null && auth.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}

	/** The caller's user id when they hold a clinician role (CRSPL/LHCP), else null. */
	private static Long doctorId(Authentication auth) {
		if (auth == null) {
			return null;
		}
		boolean clinician = auth.getAuthorities().stream().anyMatch(a ->
				a.getAuthority().equals("ROLE_CRSPL") || a.getAuthority().equals("ROLE_LHCP"));
		if (!clinician) {
			return null;
		}
		Object principal = auth.getPrincipal();
		return principal instanceof UserDetailsImpl ? ((UserDetailsImpl) principal).getId() : null;
	}

	private static Map<String, String> error(String message) {
		return Collections.singletonMap("message", message);
	}

	// ---- Patient side ----

	@PreAuthorize("hasRole('PATIENT')")
	@PostMapping("")
	public ResponseEntity<SecondOpinionRequest> create(@RequestBody SecondOpinionRequest body,
			Authentication auth) {
		String description = body != null ? body.getDescription() : null;
		return new ResponseEntity<>(service.create(auth.getName(), description), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('PATIENT')")
	@PostMapping("/{id}/attachments")
	public ResponseEntity<?> addAttachment(@PathVariable Long id,
			@RequestParam("file") MultipartFile file, Authentication auth) {
		try {
			SecondOpinionAttachment attachment = service.addAttachment(id, auth.getName(), file);
			return new ResponseEntity<>(attachment, HttpStatus.CREATED);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(error(e.getMessage()));
		} catch (Exception e) {
			return new ResponseEntity<>(error("Could not store the file. Please try again."),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasRole('PATIENT')")
	@DeleteMapping("/{id}/attachments/{attachmentId}")
	public ResponseEntity<?> deleteAttachment(@PathVariable Long id, @PathVariable Long attachmentId,
			Authentication auth) {
		try {
			service.deleteAttachment(id, attachmentId, auth.getName());
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(error(e.getMessage()));
		} catch (Exception e) {
			return new ResponseEntity<>(error("Could not remove the file."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasRole('PATIENT')")
	@GetMapping("/mine")
	public ResponseEntity<List<SecondOpinionRequest>> mine(Authentication auth) {
		return ResponseEntity.ok(service.listMine(auth.getName()));
	}

	// ---- Reviewer side: Board ADMIN sees all, CRSPL/LHCP their own patients ----

	@PreAuthorize("hasAnyRole('CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("")
	public ResponseEntity<List<SecondOpinionRequest>> listAll(Authentication auth) {
		if (isAdmin(auth)) {
			return ResponseEntity.ok(service.listAll());
		}
		Long doctorUserId = doctorId(auth);
		return ResponseEntity.ok(doctorUserId != null
				? service.listForDoctor(doctorUserId)
				: Collections.<SecondOpinionRequest>emptyList());
	}

	@PreAuthorize("hasAnyRole('CRSPL', 'LHCP', 'ADMIN')")
	@PutMapping("/{id}/review")
	public ResponseEntity<SecondOpinionRequest> review(@PathVariable Long id,
			@RequestBody SecondOpinionRequest body, Authentication auth) {
		SecondOpinionStatus status = body != null ? body.getStatus() : null;
		String note = body != null ? body.getResponseNote() : null;
		SecondOpinionRequest updated = service.review(id, status, note, auth.getName(),
				isAdmin(auth), doctorId(auth));
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// ---- Shared (owner, admin, or the patient's assigned clinician) ----

	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<SecondOpinionRequest> getById(@PathVariable Long id, Authentication auth) {
		SecondOpinionRequest request = service.getForUser(id, auth.getName(), isAdmin(auth), doctorId(auth));
		return request != null ? ResponseEntity.ok(request) : ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("/attachments/{attachmentId}/download")
	public ResponseEntity<Resource> download(@PathVariable Long attachmentId, Authentication auth) {
		SecondOpinionAttachment attachment =
				service.getAttachmentForUser(attachmentId, auth.getName(), isAdmin(auth), doctorId(auth));
		if (attachment == null) {
			return ResponseEntity.notFound().build();
		}
		File file = service.fileFor(attachment);
		if (file == null) {
			return ResponseEntity.notFound().build();
		}
		String contentType = attachment.getFileType() != null
				? attachment.getFileType()
				: MediaType.APPLICATION_OCTET_STREAM_VALUE;
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + attachment.getFileName().replace("\"", "") + "\"")
				.contentType(MediaType.parseMediaType(contentType))
				.contentLength(file.length())
				.body(new FileSystemResource(file));
	}
}
