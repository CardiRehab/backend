package com.healthcare.herplatform.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * A patient's request for a second opinion on a heart ailment (SOHA section).
 * The patient describes their situation and attaches medical documents and
 * echo/angio videos; a reviewer (cardiologist via CRSPL/LHCP/Board admin)
 * reads the request, downloads the files and records an opinion.
 *
 * Attachment bytes live on DISK (see {@link SecondOpinionAttachment#getStoredPath()}),
 * never in the database — videos can be hundreds of MB.
 */
@Entity
@Table(name = "second_opinion_requests")
public class SecondOpinionRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Owner: the login username of the patient who submitted the request.
	@Column(name = "patient_username", nullable = false)
	private String patientUsername;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private SecondOpinionStatus status = SecondOpinionStatus.NEW;

	// The reviewer's written opinion, shown back to the patient.
	@Column(name = "response_note", columnDefinition = "TEXT")
	private String responseNote;

	@Column(name = "responded_by")
	private String respondedBy;

	@Column(name = "responded_at")
	private LocalDateTime respondedAt;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@JsonManagedReference
	@OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<SecondOpinionAttachment> attachments = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
		if (this.status == null) {
			this.status = SecondOpinionStatus.NEW;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPatientUsername() {
		return patientUsername;
	}

	public void setPatientUsername(String patientUsername) {
		this.patientUsername = patientUsername;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SecondOpinionStatus getStatus() {
		return status;
	}

	public void setStatus(SecondOpinionStatus status) {
		this.status = status;
	}

	public String getResponseNote() {
		return responseNote;
	}

	public void setResponseNote(String responseNote) {
		this.responseNote = responseNote;
	}

	public String getRespondedBy() {
		return respondedBy;
	}

	public void setRespondedBy(String respondedBy) {
		this.respondedBy = respondedBy;
	}

	public LocalDateTime getRespondedAt() {
		return respondedAt;
	}

	public void setRespondedAt(LocalDateTime respondedAt) {
		this.respondedAt = respondedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<SecondOpinionAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<SecondOpinionAttachment> attachments) {
		this.attachments = attachments;
	}
}
