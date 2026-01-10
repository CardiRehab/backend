package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
//import javax.validation.constraints.NotBlank;

@Table(name="user_assmt")
@Entity(name="user_assmt")
public class UserJoinUserAssigned {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //GenerationType.AUTO will generate wrong id value here
	//OR
	//@GeneratedValue(generator = "userassign_seq", strategy = GenerationType.SEQUENCE)
	//@SequenceGenerator(name = "userassign_seq", sequenceName = "userassign_sequence", initialValue = 1, allocationSize = 1)
    Long id;
	
	@Column(name = "user_id", updatable = true, insertable = true)
	//@NotBlank
    Long userId;
	
	@Column(name = "assmt_id", updatable = true, insertable = true)
	//@NotBlank
    int assmtId;

	public UserJoinUserAssigned() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserJoinUserAssigned(Long userId, int assmtId) {
		super();
		this.userId = userId;
		this.assmtId = assmtId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getAssmtId() {
		return assmtId;
	}

	public void setAssmtId(int assmtId) {
		this.assmtId = assmtId;
	}

	@Override
	public String toString() {
		return "UserJoinUserAssigned [id=" + id + ", userId=" + userId + ", assmtId=" + assmtId + "]";
	}
}
