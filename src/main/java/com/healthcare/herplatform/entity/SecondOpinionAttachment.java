package com.healthcare.herplatform.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * One file attached to a {@link SecondOpinionRequest} — a report (PDF/JPG/DOC)
 * or an echo/angio video (AVI/MP4/MOV). Only metadata is stored here; the bytes
 * live on disk under the configured upload root ({@code app.soha.upload-dir}),
 * saved with a random name so the original filename can never be used for
 * path tricks. Downloads go through the access-checked controller endpoint.
 */
@Entity
@Table(name = "second_opinion_attachments")
public class SecondOpinionAttachment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "request_id", nullable = false)
	private SecondOpinionRequest request;

	// Original (sanitized) filename, shown in the UI and used for downloads.
	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "file_type", length = 128)
	private String fileType;

	@Column(name = "file_size")
	private long fileSize;

	// Path relative to the upload root, e.g. "12/3f2a….pdf". Never exposed to
	// clients — it describes the server filesystem.
	@JsonIgnore
	@Column(name = "stored_path", nullable = false, length = 512)
	private String storedPath;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SecondOpinionRequest getRequest() {
		return request;
	}

	public void setRequest(SecondOpinionRequest request) {
		this.request = request;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getStoredPath() {
		return storedPath;
	}

	public void setStoredPath(String storedPath) {
		this.storedPath = storedPath;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
