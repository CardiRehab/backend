package com.healthcare.herplatform.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name="contactus")
@Entity(name="contactus")
public class Contactus {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "token_id", updatable = true, insertable = true)
	@NotBlank
    String tokenId;
	
	@Column(name = "date_time", updatable = true, insertable = true)
	Date dtTime;
	
	@Column(name = "name", updatable = true, insertable = true)
	@NotBlank
	String name;
	
	@Column(name = "email", updatable = true, insertable = true)
	@NotBlank
	String email;
	
	@Column(name = "phone", updatable = true, insertable = true)
	@NotBlank
	String phone;

	@Column(name = "subject", updatable = true, insertable = true)
	@NotBlank
	String subject;
	
	@Column(name = "message", updatable = true, insertable = true)
	@NotBlank
	String message;

	public Contactus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contactus(@NotBlank String tokenId, Date dtTime, @NotBlank String name, @NotBlank String email,
			@NotBlank String phone, @NotBlank String subject, @NotBlank String message) {
		super();
		this.tokenId = tokenId;
		this.dtTime = dtTime;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.subject = subject;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Date getDtTime() {
		return dtTime;
	}

	public void setDtTime(Date dtTime) {
		this.dtTime = dtTime;
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
		return "Contactus [id=" + id + ", tokenId=" + tokenId + ", dtTime=" + dtTime + ", name=" + name + ", email="
				+ email + ", phone=" + phone + ", subject=" + subject + ", message=" + message + "]";
	}
}







