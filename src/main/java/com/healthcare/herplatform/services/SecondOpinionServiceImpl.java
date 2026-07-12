package com.healthcare.herplatform.services;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.herplatform.entity.SecondOpinionAttachment;
import com.healthcare.herplatform.entity.SecondOpinionRequest;
import com.healthcare.herplatform.entity.SecondOpinionStatus;
import com.healthcare.herplatform.repository.SecondOpinionAttachmentRepository;
import com.healthcare.herplatform.repository.SecondOpinionRequestRepository;

/**
 * Second-opinion requests + their attachments. File bytes are streamed to disk
 * under {@code app.soha.upload-dir} (one sub-folder per request, random file
 * names) — never into the database, because echo/angio videos run to hundreds
 * of MB. Uploads are validated against an extension whitelist and a size cap;
 * every read is access-checked (owner or reviewer) before a path is resolved.
 */
@Service
public class SecondOpinionServiceImpl implements SecondOpinionService {

	// Reports + videos a cardiologist actually needs; anything else is refused.
	private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
			"pdf", "jpg", "jpeg", "png", "doc", "docx",
			"avi", "mp4", "mov");

	private static final int MAX_FILES_PER_REQUEST = 10;

	private final SecondOpinionRequestRepository requestRepo;
	private final SecondOpinionAttachmentRepository attachmentRepo;

	@Value("${app.soha.upload-dir:uploads/second-opinion}")
	private String uploadDir;

	@Value("${app.soha.max-file-mb:200}")
	private long maxFileMb;

	public SecondOpinionServiceImpl(SecondOpinionRequestRepository requestRepo,
			SecondOpinionAttachmentRepository attachmentRepo) {
		super();
		this.requestRepo = requestRepo;
		this.attachmentRepo = attachmentRepo;
	}

	private Path uploadRoot() {
		return Paths.get(uploadDir).toAbsolutePath().normalize();
	}

	@Override
	public SecondOpinionRequest create(String patientUsername, String description) {
		SecondOpinionRequest request = new SecondOpinionRequest();
		request.setPatientUsername(patientUsername);
		request.setDescription(description);
		request.setStatus(SecondOpinionStatus.NEW);
		return requestRepo.save(request);
	}

	@Override
	public SecondOpinionAttachment addAttachment(Long requestId, String patientUsername, MultipartFile file)
			throws Exception {
		SecondOpinionRequest request = requestRepo.findById(requestId).orElse(null);
		if (request == null || !request.getPatientUsername().equals(patientUsername)) {
			throw new IllegalArgumentException("Request not found");
		}
		if (request.getStatus() != SecondOpinionStatus.NEW) {
			throw new IllegalStateException("Files can only be added while the request is new");
		}
		if (request.getAttachments().size() >= MAX_FILES_PER_REQUEST) {
			throw new IllegalStateException("A request may have at most " + MAX_FILES_PER_REQUEST + " files");
		}
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("File is empty");
		}
		if (file.getSize() > maxFileMb * 1024L * 1024L) {
			throw new IllegalArgumentException("File exceeds the " + maxFileMb + " MB limit");
		}

		String originalName = StringUtils.cleanPath(
				file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
		if (originalName.contains("..")) {
			throw new IllegalArgumentException("Filename contains invalid path sequence " + originalName);
		}
		String extension = extensionOf(originalName);
		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			throw new IllegalArgumentException("File type ." + extension + " is not allowed");
		}

		// Stream to disk under a random name — the original name is metadata only.
		Path dir = uploadRoot().resolve(String.valueOf(requestId));
		Files.createDirectories(dir);
		String diskName = UUID.randomUUID().toString() + "." + extension;
		Path target = dir.resolve(diskName).normalize();
		if (!target.startsWith(uploadRoot())) {
			throw new IllegalArgumentException("Invalid storage path");
		}
		try (InputStream in = file.getInputStream()) {
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
		}

		try {
			SecondOpinionAttachment attachment = new SecondOpinionAttachment();
			attachment.setRequest(request);
			attachment.setFileName(originalName);
			attachment.setFileType(file.getContentType());
			attachment.setFileSize(file.getSize());
			attachment.setStoredPath(requestId + "/" + diskName);
			return attachmentRepo.save(attachment);
		} catch (Exception e) {
			// Don't strand bytes on disk if the metadata row failed to save.
			Files.deleteIfExists(target);
			throw e;
		}
	}

	@Override
	public void deleteAttachment(Long requestId, Long attachmentId, String patientUsername) throws Exception {
		SecondOpinionAttachment attachment = attachmentRepo.findById(attachmentId).orElse(null);
		if (attachment == null
				|| !attachment.getRequest().getId().equals(requestId)
				|| !attachment.getRequest().getPatientUsername().equals(patientUsername)) {
			throw new IllegalArgumentException("Attachment not found");
		}
		if (attachment.getRequest().getStatus() != SecondOpinionStatus.NEW) {
			throw new IllegalStateException("Files can only be removed while the request is new");
		}
		Files.deleteIfExists(uploadRoot().resolve(attachment.getStoredPath()).normalize());
		SecondOpinionRequest request = attachment.getRequest();
		request.getAttachments().remove(attachment);
		requestRepo.save(request);
	}

	@Override
	public List<SecondOpinionRequest> listMine(String patientUsername) {
		return requestRepo.findByPatientUsernameOrderByCreatedAtDesc(patientUsername);
	}

	@Override
	public List<SecondOpinionRequest> listAll() {
		return requestRepo.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public SecondOpinionRequest getForUser(Long id, String username, boolean reviewer) {
		SecondOpinionRequest request = requestRepo.findById(id).orElse(null);
		if (request == null) {
			return null;
		}
		if (!reviewer && !request.getPatientUsername().equals(username)) {
			return null;
		}
		return request;
	}

	@Override
	@Transactional(readOnly = true)
	public SecondOpinionAttachment getAttachmentForUser(Long attachmentId, String username, boolean reviewer) {
		SecondOpinionAttachment attachment = attachmentRepo.findById(attachmentId).orElse(null);
		if (attachment == null) {
			return null;
		}
		if (!reviewer && !attachment.getRequest().getPatientUsername().equals(username)) {
			return null;
		}
		// Touch lazy fields needed after the session closes.
		attachment.getRequest().getId();
		return attachment;
	}

	@Override
	public File fileFor(SecondOpinionAttachment attachment) {
		Path path = uploadRoot().resolve(attachment.getStoredPath()).normalize();
		if (!path.startsWith(uploadRoot())) {
			return null;
		}
		File file = path.toFile();
		return file.isFile() ? file : null;
	}

	@Override
	public SecondOpinionRequest review(Long id, SecondOpinionStatus status, String responseNote, String reviewer) {
		SecondOpinionRequest request = requestRepo.findById(id).orElse(null);
		if (request == null) {
			return null;
		}
		if (status != null) {
			request.setStatus(status);
		}
		if (responseNote != null && !responseNote.trim().isEmpty()) {
			request.setResponseNote(responseNote.trim());
			request.setRespondedBy(reviewer);
			request.setRespondedAt(LocalDateTime.now());
		}
		return requestRepo.save(request);
	}

	@Override
	public void delete(Long id) {
		SecondOpinionRequest request = requestRepo.findById(id).orElse(null);
		if (request == null) {
			return;
		}
		for (SecondOpinionAttachment attachment : request.getAttachments()) {
			try {
				Files.deleteIfExists(uploadRoot().resolve(attachment.getStoredPath()).normalize());
			} catch (Exception ignored) {
			}
		}
		requestRepo.delete(request);
	}

	private static String extensionOf(String fileName) {
		int dot = fileName.lastIndexOf('.');
		if (dot < 0 || dot == fileName.length() - 1) {
			return "";
		}
		return fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
	}
}
