package com.healthcare.herplatform.models;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Phq9InsertModel {
	// add @NotNull annotation to an integer
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid;

	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dateTime;

	@JsonProperty("phq1")
	@NotBlank(message = "Answer to PHQ1 is Required")
	private String phq1;

	@JsonProperty("phq2")
	@NotBlank(message = "Answer to PHQ2 is Required")
	private String phq2;

	@JsonProperty("phq3")
	@NotBlank(message = "Answer to PHQ3 is Required")
	private String phq3;

	@JsonProperty("phq4")
	@NotBlank(message = "Answer to PHQ4 is Required")
	private String phq4;

	@JsonProperty("phq5")
	@NotBlank(message = "Answer to PHQ5 is Required")
	private String phq5;

	@JsonProperty("phq6")
	@NotBlank(message = "Answer to PHQ6 is Required")
	private String phq6;

	@JsonProperty("phq7")
	@NotBlank(message = "Answer to PHQ7 is Required")
	private String phq7;

	@JsonProperty("phq8")
	@NotBlank(message = "Answer to PHQ8 is Required")
	private String phq8;

	@JsonProperty("phq9")
	@NotBlank(message = "Answer to PHQ9 is Required")
	private String phq9;

	@JsonProperty("diffLevel")
	@NotBlank(message = "Answer to the last question (difficulty level) is Required")
	private String diffLevel;

	public Phq9InsertModel() {
		super();
	}

	public Phq9InsertModel(@NotNull(message = "User ID is required") int userid,
			@NotNull(message = "Date/Time is required") Date dateTime,
			@NotBlank(message = "Answer to PHQ1 is Required") String phq1,
			@NotBlank(message = "Answer to PHQ2 is Required") String phq2,
			@NotBlank(message = "Answer to PHQ3 is Required") String phq3,
			@NotBlank(message = "Answer to PHQ4 is Required") String phq4,
			@NotBlank(message = "Answer to PHQ5 is Required") String phq5,
			@NotBlank(message = "Answer to PHQ6 is Required") String phq6,
			@NotBlank(message = "Answer to PHQ7 is Required") String phq7,
			@NotBlank(message = "Answer to PHQ8 is Required") String phq8,
			@NotBlank(message = "Answer to PHQ9 is Required") String phq9,
			@NotBlank(message = "Answer to the last question (difficulty level) is Required") String diffLevel) {
		super();
		this.userid = userid;
		this.dateTime = dateTime;
		this.phq1 = phq1;
		this.phq2 = phq2;
		this.phq3 = phq3;
		this.phq4 = phq4;
		this.phq5 = phq5;
		this.phq6 = phq6;
		this.phq7 = phq7;
		this.phq8 = phq8;
		this.phq9 = phq9;
		this.diffLevel = diffLevel;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getPhq1() {
		return phq1;
	}

	public void setPhq1(String phq1) {
		this.phq1 = phq1;
	}

	public String getPhq2() {
		return phq2;
	}

	public void setPhq2(String phq2) {
		this.phq2 = phq2;
	}

	public String getPhq3() {
		return phq3;
	}

	public void setPhq3(String phq3) {
		this.phq3 = phq3;
	}

	public String getPhq4() {
		return phq4;
	}

	public void setPhq4(String phq4) {
		this.phq4 = phq4;
	}

	public String getPhq5() {
		return phq5;
	}

	public void setPhq5(String phq5) {
		this.phq5 = phq5;
	}

	public String getPhq6() {
		return phq6;
	}

	public void setPhq6(String phq6) {
		this.phq6 = phq6;
	}

	public String getPhq7() {
		return phq7;
	}

	public void setPhq7(String phq7) {
		this.phq7 = phq7;
	}

	public String getPhq8() {
		return phq8;
	}

	public void setPhq8(String phq8) {
		this.phq8 = phq8;
	}

	public String getPhq9() {
		return phq9;
	}

	public void setPhq9(String phq9) {
		this.phq9 = phq9;
	}

	public String getDiffLevel() {
		return diffLevel;
	}

	public void setDiffLevel(String diffLevel) {
		this.diffLevel = diffLevel;
	}

	@Override
	public String toString() {
		return "Phq9InsertModel [userid=" + userid + ", dateTime=" + dateTime + ", phq1=" + phq1 + ", phq2=" + phq2
				+ ", phq3=" + phq3 + ", phq4=" + phq4 + ", phq5=" + phq5 + ", phq6=" + phq6 + ", phq7=" + phq7
				+ ", phq8=" + phq8 + ", phq9=" + phq9 + ", diffLevel=" + diffLevel + "]";
	}
}
