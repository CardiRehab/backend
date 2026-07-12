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

	List<SecondOpinionRequest> listAll();

	/** Returns the request only if {@code username} owns it or {@code reviewer} is true; null when absent/forbidden. */
	SecondOpinionRequest getForUser(Long id, String username, boolean reviewer);

	/** Attachment metadata + its file on disk, access-checked like {@link #getForUser}. */
	SecondOpinionAttachment getAttachmentForUser(Long attachmentId, String username, boolean reviewer);

	File fileFor(SecondOpinionAttachment attachment);

	SecondOpinionRequest review(Long id, SecondOpinionStatus status, String responseNote, String reviewer);

	void delete(Long id);
}
