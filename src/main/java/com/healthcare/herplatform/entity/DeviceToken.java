package com.healthcare.herplatform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * An FCM registration token belonging to one of a user's devices. A user may
 * have several (phone, tablet, reinstalls). Tokens are unique; re-registering
 * the same token just updates the owner and timestamp.
 */
@Entity
@Table(name = "device_tokens",
		uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = "token"),
		indexes = { @Index(name = "idx_device_tokens_username", columnList = "username") })
public class DeviceToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false, length = 512)
	private String token;

	@Column(length = 16)
	private String platform; // "android" | "ios"

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public DeviceToken() {
	}

	public DeviceToken(String username, String token, String platform) {
		this.username = username;
		this.token = token;
		this.platform = platform;
		this.updatedAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
