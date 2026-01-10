package com.healthcare.herplatform.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignNewUserModel {
	// add @NotNull annotation to an integer
	@JsonProperty("assigneduserid")
	@NotNull(message = "Assigned User ID is required")
	private int assigneduserid;  //Patient user id
	
	@JsonProperty("assignedusers")
	@NotBlank(message = "Assigned user is Required")
	private String assignedusers;  //Patient username
	
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid; //CRSPL User id to which the patient user is assigned
	
	@JsonProperty("username")
	@NotBlank(message = "Username is Required")
	private String username;    //CRSPL username

	public AssignNewUserModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssignNewUserModel(@NotNull(message = "Assigned User ID is required") int assigneduserid,
			@NotBlank(message = "Assigned user is Required") String assignedusers,
			@NotNull(message = "User ID is required") int userid,
			@NotBlank(message = "Username is Required") String username) {
		super();
		this.assigneduserid = assigneduserid;
		this.assignedusers = assignedusers;
		this.userid = userid;
		this.username = username;
	}

	public int getAssigneduserid() {
		return assigneduserid;
	}

	public void setAssigneduserid(int assigneduserid) {
		this.assigneduserid = assigneduserid;
	}

	public String getAssignedusers() {
		return assignedusers;
	}

	public void setAssignedusers(String assignedusers) {
		this.assignedusers = assignedusers;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "AssignNewUserModel [assigneduserid=" + assigneduserid + ", assignedusers=" + assignedusers + ", userid="
				+ userid + ", username=" + username + "]";
	}
}