package com.healthcare.herplatform.services;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.healthcare.herplatform.entity.SecondOpinionAttachment;
import com.healthcare.herplatform.entity.SecondOpinionRequest;
import com.healthcare.herplatform.entity.SecondOpinionStatus;

public interface SecondOpinionService {

	SecondOpinionRequest create(String patientUsername, String description);

	SecondOpinionAttachment addAttachment(Long requestId, String patientUsername, MultipartFile file) throws Exception;

	void deleteAttachment(Long requestId, Long attachmentId, String patientUsername) throws Exception;

	List<SecondOpinionRequest> listMine(String patientUsername);

	/** Every request — Board ADMIN only. */
	List<SecondOpinionRequest> listAll();

	/** Requests from the patients assigned to this CRSPL/LHCP (user_assignment.userid). */
	List<SecondOpinionRequest> listForDoctor(long doctorUserId);

	/**
	 * Returns the request only if {@code username} owns it, {@code admin} is true,
	 * or {@code doctorUserId} (nullable) is a clinician the patient is assigned to;
	 * null when absent/forbidden.
	 */
	SecondOpinionRequest getForUser(Long id, String username, boolean admin, Long doctorUserId);

	/** Attachment metadata + its file on disk, access-checked like {@link #getForUser}. */
	SecondOpinionAttachment getAttachmentForUser(Long attachmentId, String username, boolean admin, Long doctorUserId);

	File fileFor(SecondOpinionAttachment attachment);

	/**
	 * Records status/reply. Admins may review any request; a clinician only those of
	 * their assigned patients — otherwise null, indistinguishable from a missing id.
	 */
	SecondOpinionRequest review(Long id, SecondOpinionStatus status, String responseNote, String reviewer,
			boolean admin, Long doctorUserId);

	void delete(Long id);
}
