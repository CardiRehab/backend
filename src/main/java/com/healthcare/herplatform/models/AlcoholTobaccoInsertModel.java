package com.healthcare.herplatform.models;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlcoholTobaccoInsertModel {
	// add @NotNull annotation to an integer
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid;

	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dateTime;

	@JsonProperty("smk")
	@NotBlank(message = "Answer to Tobacco Screening Question is Required")
	private String smk;

	@JsonProperty("alcScreen1")
	@NotBlank(message = "Answer to all three Alcohol Screening Questions are Required")
	private String alcScreen1;

	@JsonProperty("alcScreen2")
	@NotBlank(message = "Answer to all three Alcohol Screening Questions are Required")
	private String alcScreen2;

	@JsonProperty("alcScreen3")
	@NotBlank(message = "Answer to all three Alcohol Screening Questions are Required")
	private String alcScreen3;

	@JsonProperty("alcFullScreen4")
	//@NotBlank(message = "Answer to all seven Alcohol Full Screening Questions are Required")
	private String alcFullScreen4;

	@JsonProperty("alcFullScreen5")
	//@NotBlank(message = "Answer to all seven Alcohol Full Screening Questions are Required")
	private String alcFullScreen5;

	@JsonProperty("alcFullScreen6")
	//@NotBlank(message = "Answer to all seven Alcohol Full Screening Questions are Required")
	private String alcFullScreen6;

	@JsonProperty("alcFullScreen7")
	//@NotBlank(message = "Answer to all seven Alcohol Full Screening Questions are Required")
	private String alcFullScreen7;

	@JsonProperty("alcFullScreen8")
	//@NotBlank(message = "Answer to all seven Alcohol Full Screening Questions are Required")
	private String alcFullScreen8;

	@JsonProperty("alcFullScreen9")
	//@NotBlank(message = "Answer to all seven Alcohol Full Screening Questions are Required")
	private String alcFullScreen9;

	@JsonProperty("alcFullScreen10")
	//@NotBlank(message = "Answer to all seven Alcohol Full Screening Questions are Required")
	private String alcFullScreen10;

	public AlcoholTobaccoInsertModel() {
		super();
	}

	public AlcoholTobaccoInsertModel(@NotNull(message = "User ID is required") int userid,
			@NotNull(message = "Date/Time is required") Date dateTime,
			@NotBlank(message = "Answer to Tobacco Screening Question is Required") String smk,
			@NotBlank(message = "Answer to all three Alcohol Screening Questions are Required") String alcScreen1,
			@NotBlank(message = "Answer to all three Alcohol Screening Questions are Required") String alcScreen2,
			@NotBlank(message = "Answer to all three Alcohol Screening Questions are Required") String alcScreen3,
			String alcFullScreen4, String alcFullScreen5, String alcFullScreen6, String alcFullScreen7,
			String alcFullScreen8, String alcFullScreen9, String alcFullScreen10) {
		super();
		this.userid = userid;
		this.dateTime = dateTime;
		this.smk = smk;
		this.alcScreen1 = alcScreen1;
		this.alcScreen2 = alcScreen2;
		this.alcScreen3 = alcScreen3;
		this.alcFullScreen4 = alcFullScreen4;
		this.alcFullScreen5 = alcFullScreen5;
		this.alcFullScreen6 = alcFullScreen6;
		this.alcFullScreen7 = alcFullScreen7;
		this.alcFullScreen8 = alcFullScreen8;
		this.alcFullScreen9 = alcFullScreen9;
		this.alcFullScreen10 = alcFullScreen10;
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

	public String getSmk() {
		return smk;
	}

	public void setSmk(String smk) {
		this.smk = smk;
	}

	public String getAlcScreen1() {
		return alcScreen1;
	}

	public void setAlcScreen1(String alcScreen1) {
		this.alcScreen1 = alcScreen1;
	}

	public String getAlcScreen2() {
		return alcScreen2;
	}

	public void setAlcScreen2(String alcScreen2) {
		this.alcScreen2 = alcScreen2;
	}

	public String getAlcScreen3() {
		return alcScreen3;
	}

	public void setAlcScreen3(String alcScreen3) {
		this.alcScreen3 = alcScreen3;
	}

	public String getAlcFullScreen4() {
		return alcFullScreen4;
	}

	public void setAlcFullScreen4(String alcFullScreen4) {
		this.alcFullScreen4 = alcFullScreen4;
	}

	public String getAlcFullScreen5() {
		return alcFullScreen5;
	}

	public void setAlcFullScreen5(String alcFullScreen5) {
		this.alcFullScreen5 = alcFullScreen5;
	}

	public String getAlcFullScreen6() {
		return alcFullScreen6;
	}

	public void setAlcFullScreen6(String alcFullScreen6) {
		this.alcFullScreen6 = alcFullScreen6;
	}

	public String getAlcFullScreen7() {
		return alcFullScreen7;
	}

	public void setAlcFullScreen7(String alcFullScreen7) {
		this.alcFullScreen7 = alcFullScreen7;
	}

	public String getAlcFullScreen8() {
		return alcFullScreen8;
	}

	public void setAlcFullScreen8(String alcFullScreen8) {
		this.alcFullScreen8 = alcFullScreen8;
	}

	public String getAlcFullScreen9() {
		return alcFullScreen9;
	}

	public void setAlcFullScreen9(String alcFullScreen9) {
		this.alcFullScreen9 = alcFullScreen9;
	}

	public String getAlcFullScreen10() {
		return alcFullScreen10;
	}

	public void setAlcFullScreen10(String alcFullScreen10) {
		this.alcFullScreen10 = alcFullScreen10;
	}

	@Override
	public String toString() {
		return "AlcoholTobaccoInsertModel [userid=" + userid + ", dateTime=" + dateTime + ", smk=" + smk
				+ ", alcScreen1=" + alcScreen1 + ", alcScreen2=" + alcScreen2 + ", alcScreen3=" + alcScreen3
				+ ", alcFullScreen4=" + alcFullScreen4 + ", alcFullScreen5=" + alcFullScreen5 + ", alcFullScreen6="
				+ alcFullScreen6 + ", alcFullScreen7=" + alcFullScreen7 + ", alcFullScreen8=" + alcFullScreen8
				+ ", alcFullScreen9=" + alcFullScreen9 + ", alcFullScreen10=" + alcFullScreen10 + "]";
	}
}
