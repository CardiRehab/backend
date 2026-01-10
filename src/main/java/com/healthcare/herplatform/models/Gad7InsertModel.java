package com.healthcare.herplatform.models;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Gad7InsertModel {
	// add @NotNull annotation to an integer
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid;

	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dateTime;

	@JsonProperty("gad1")
	@NotBlank(message = "Answer to GAD1 is Required")
	private String gad1;

	@JsonProperty("gad2")
	@NotBlank(message = "Answer to GAD2 is Required")
	private String gad2;

	@JsonProperty("gad3")
	@NotBlank(message = "Answer to GAD3 is Required")
	private String gad3;

	@JsonProperty("gad4")
	@NotBlank(message = "Answer to GAD4 is Required")
	private String gad4;

	@JsonProperty("gad5")
	@NotBlank(message = "Answer to GAD5 is Required")
	private String gad5;

	@JsonProperty("gad6")
	@NotBlank(message = "Answer to GAD6 is Required")
	private String gad6;

	@JsonProperty("gad7")
	@NotBlank(message = "Answer to GAD7 is Required")
	private String gad7;

	@JsonProperty("diffLevel")
	@NotBlank(message = "Answer to the last question (difficulty level) is Required")
	private String diffLevel;

	public Gad7InsertModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Gad7InsertModel(@NotNull(message = "User ID is required") int userid,
			@NotNull(message = "Date/Time is required") Date dateTime,
			@NotBlank(message = "Answer to GAD1 is Required") String gad1,
			@NotBlank(message = "Answer to GAD2 is Required") String gad2,
			@NotBlank(message = "Answer to GAD3 is Required") String gad3,
			@NotBlank(message = "Answer to GAD4 is Required") String gad4,
			@NotBlank(message = "Answer to GAD5 is Required") String gad5,
			@NotBlank(message = "Answer to GAD6 is Required") String gad6,
			@NotBlank(message = "Answer to GAD7 is Required") String gad7,
			@NotBlank(message = "Answer to the last question (difficulty level) is Required") String diffLevel) {
		super();
		this.userid = userid;
		this.dateTime = dateTime;
		this.gad1 = gad1;
		this.gad2 = gad2;
		this.gad3 = gad3;
		this.gad4 = gad4;
		this.gad5 = gad5;
		this.gad6 = gad6;
		this.gad7 = gad7;
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

	public String getGad1() {
		return gad1;
	}

	public void setGad1(String gad1) {
		this.gad1 = gad1;
	}

	public String getGad2() {
		return gad2;
	}

	public void setGad2(String gad2) {
		this.gad2 = gad2;
	}

	public String getGad3() {
		return gad3;
	}

	public void setGad3(String gad3) {
		this.gad3 = gad3;
	}

	public String getGad4() {
		return gad4;
	}

	public void setGad4(String gad4) {
		this.gad4 = gad4;
	}

	public String getGad5() {
		return gad5;
	}

	public void setGad5(String gad5) {
		this.gad5 = gad5;
	}

	public String getGad6() {
		return gad6;
	}

	public void setGad6(String gad6) {
		this.gad6 = gad6;
	}

	public String getGad7() {
		return gad7;
	}

	public void setGad7(String gad7) {
		this.gad7 = gad7;
	}

	public String getDiffLevel() {
		return diffLevel;
	}

	public void setDiffLevel(String diffLevel) {
		this.diffLevel = diffLevel;
	}

	@Override
	public String toString() {
		return "Gad7InsertModel [userid=" + userid + ", dateTime=" + dateTime + ", gad1=" + gad1 + ", gad2=" + gad2
				+ ", gad3=" + gad3 + ", gad4=" + gad4 + ", gad5=" + gad5 + ", gad6=" + gad6 + ", gad7=" + gad7
				+ ", diffLevel=" + diffLevel + "]";
	}

}
