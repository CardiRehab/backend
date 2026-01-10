package com.healthcare.herplatform.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt" }, allowGetters = true)
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String senderName;
	
	@NotBlank
	private String receiverName;
	
	private String message;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	//@NotBlank
	private Status status; //Enum (JOIN, MESSAGE, MSG_ATTACH, LEAVE, JOIN_CRSPL)
	
	@Column(nullable = true)
	private String fileId;
	
	@Column(nullable = true)
	private String fileName;

	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message(@NotBlank String senderName, @NotBlank String receiverName, String message, Date createdAt,
			Status status, String fileId, String fileName) {
		super();
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.message = message;
		this.createdAt = createdAt;
		this.status = status;
		this.fileId = fileId;
		this.fileName = fileName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", senderName=" + senderName + ", receiverName=" + receiverName + ", message="
				+ message + ", createdAt=" + createdAt + ", status=" + status + ", fileId=" + fileId + ", fileName="
				+ fileName + "]";
	}
}
