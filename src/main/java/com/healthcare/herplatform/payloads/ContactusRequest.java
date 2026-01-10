package com.healthcare.herplatform.payloads;

import java.util.Date;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactusRequest {
	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dateTime;
	
	@NotBlank
	@Size(min = 3, max = 50)
	private String name;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 20)
	private String phone;
	
	@NotBlank
	@Size(max = 120)
	private String subject;

	@NotBlank
	@Size(max = 1000)
	private String message;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ContactusRequest [dateTime=" + dateTime + ", name=" + name + ", email=" + email + ", phone=" + phone
				+ ", subject=" + subject + ", message=" + message + "]";
	}
}
