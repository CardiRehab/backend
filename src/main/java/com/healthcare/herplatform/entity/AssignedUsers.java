package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

//import org.hibernate.annotations.GenericGenerator;

@Table(name="user_assignment")
@Entity(name="user_assignment")
public class AssignedUsers{
	@Id
	
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "userid", updatable = true, insertable = true)
    int userId;
	
	@Column(name = "assigneduserid", updatable = true, insertable = true)
    Long assignedUserId;
	
	@Column(name = "regid", updatable = true, insertable = true)
	@NotBlank
    String regId;
	
	@Column(name = "username", updatable = true, insertable = true)
	@NotBlank
	String userName;
	
	@Column(name = "email", updatable = true, insertable = true)
	@NotBlank
	String emailId;
	
	@Column(name = "phone", updatable = true, insertable = true)
	@NotBlank
	String phone;
	
	@Column(name = "gender", updatable = true, insertable = true)
	@NotBlank
	String gender;
	
	@Column(name = "age", updatable = true, insertable = true)
	@NotBlank
	String age;
	
	@Column(name = "assignedusers", updatable = true, insertable = true)
	@NotBlank
	String assignedUsers;
	
	public AssignedUsers() {
		super();
		// TODO Auto-generated constructor stub
	}
		
//	public AssignedUsers(int userId, Long assignedUserId, @NotBlank String userName, @NotBlank String assignedUsers) {
//		super();
//		this.userId = userId;
//		this.assignedUserId = assignedUserId;
//		this.userName = userName;
//		this.assignedUsers = assignedUsers;
//	}

	public AssignedUsers(int userId, Long assignedUserId, @NotBlank String regId, @NotBlank String userName,
			@NotBlank String emailId, @NotBlank String phone, @NotBlank String gender, @NotBlank String age,
			@NotBlank String assignedUsers) {
		super();
		this.userId = userId;
		this.assignedUserId = assignedUserId;
		this.regId = regId;
		this.userName = userName;
		this.emailId = emailId;
		this.phone = phone;
		this.gender = gender;
		this.age = age;
		this.assignedUsers = assignedUsers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Long getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAssignedUsers() {
		return assignedUsers;
	}

	public void setAssignedUsers(String assignedUsers) {
		this.assignedUsers = assignedUsers;
	}

	@Override
	public String toString() {
		return "AssignedUsers [id=" + id + ", userId=" + userId + ", assignedUserId=" + assignedUserId + ", regId="
				+ regId + ", userName=" + userName + ", emailId=" + emailId + ", phone=" + phone + ", gender=" + gender
				+ ", age=" + age + ", assignedUsers=" + assignedUsers + "]";
	}
}

