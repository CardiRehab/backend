package com.healthcare.herplatform.models;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HadsInsertModel {
	// add @NotNull annotation to an integer
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid;

	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dateTime;

	@JsonProperty("anx1")
	@NotBlank(message = "Answer to Anxiety Q1 is Required")
	private String anx1;

	@JsonProperty("anx2")
	@NotBlank(message = "Answer to Anxiety Q2 is Required")
	private String anx2;

	@JsonProperty("anx3")
	@NotBlank(message = "Answer to Anxiety Q3 is Required")
	private String anx3;

	@JsonProperty("anx4")
	@NotBlank(message = "Answer to Anxiety Q4 is Required")
	private String anx4;

	@JsonProperty("anx5")
	@NotBlank(message = "Answer to Anxiety Q5 is Required")
	private String anx5;

	@JsonProperty("anx6")
	@NotBlank(message = "Answer to Anxiety Q6 is Required")
	private String anx6;

	@JsonProperty("anx7")
	@NotBlank(message = "Answer to Anxiety Q7 is Required")
	private String anx7;

	@JsonProperty("dep1")
	@NotBlank(message = "Answer to Depression Q8 is Required")
	private String dep1;

	@JsonProperty("dep2")
	@NotBlank(message = "Answer to Depression Q9 is Required")
	private String dep2;

	@JsonProperty("dep3")
	@NotBlank(message = "Answer to Depression Q10 is Required")
	private String dep3;

	@JsonProperty("dep4")
	@NotBlank(message = "Answer to Depression Q11 is Required")
	private String dep4;

	@JsonProperty("dep5")
	@NotBlank(message = "Answer to Depression Q12 is Required")
	private String dep5;

	@JsonProperty("dep6")
	@NotBlank(message = "Answer to Depression Q13 is Required")
	private String dep6;

	@JsonProperty("dep7")
	@NotBlank(message = "Answer to Depression Q14 is Required")
	private String dep7;

	public HadsInsertModel() {
		super();
	}

	public HadsInsertModel(@NotNull(message = "User ID is required") int userid,
			@NotNull(message = "Date/Time is required") Date dateTime,
			@NotBlank(message = "Answer to Anxiety Q1 is Required") String anx1,
			@NotBlank(message = "Answer to Anxiety Q2 is Required") String anx2,
			@NotBlank(message = "Answer to Anxiety Q3 is Required") String anx3,
			@NotBlank(message = "Answer to Anxiety Q4 is Required") String anx4,
			@NotBlank(message = "Answer to Anxiety Q5 is Required") String anx5,
			@NotBlank(message = "Answer to Anxiety Q6 is Required") String anx6,
			@NotBlank(message = "Answer to Anxiety Q7 is Required") String anx7,
			@NotBlank(message = "Answer to Depression Q8 is Required") String dep1,
			@NotBlank(message = "Answer to Depression Q9 is Required") String dep2,
			@NotBlank(message = "Answer to Depression Q10 is Required") String dep3,
			@NotBlank(message = "Answer to Depression Q11 is Required") String dep4,
			@NotBlank(message = "Answer to Depression Q12 is Required") String dep5,
			@NotBlank(message = "Answer to Depression Q13 is Required") String dep6,
			@NotBlank(message = "Answer to Depression Q14 is Required") String dep7) {
		super();
		this.userid = userid;
		this.dateTime = dateTime;
		this.anx1 = anx1;
		this.anx2 = anx2;
		this.anx3 = anx3;
		this.anx4 = anx4;
		this.anx5 = anx5;
		this.anx6 = anx6;
		this.anx7 = anx7;
		this.dep1 = dep1;
		this.dep2 = dep2;
		this.dep3 = dep3;
		this.dep4 = dep4;
		this.dep5 = dep5;
		this.dep6 = dep6;
		this.dep7 = dep7;
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

	public String getAnx1() {
		return anx1;
	}

	public void setAnx1(String anx1) {
		this.anx1 = anx1;
	}

	public String getAnx2() {
		return anx2;
	}

	public void setAnx2(String anx2) {
		this.anx2 = anx2;
	}

	public String getAnx3() {
		return anx3;
	}

	public void setAnx3(String anx3) {
		this.anx3 = anx3;
	}

	public String getAnx4() {
		return anx4;
	}

	public void setAnx4(String anx4) {
		this.anx4 = anx4;
	}

	public String getAnx5() {
		return anx5;
	}

	public void setAnx5(String anx5) {
		this.anx5 = anx5;
	}

	public String getAnx6() {
		return anx6;
	}

	public void setAnx6(String anx6) {
		this.anx6 = anx6;
	}

	public String getAnx7() {
		return anx7;
	}

	public void setAnx7(String anx7) {
		this.anx7 = anx7;
	}

	public String getDep1() {
		return dep1;
	}

	public void setDep1(String dep1) {
		this.dep1 = dep1;
	}

	public String getDep2() {
		return dep2;
	}

	public void setDep2(String dep2) {
		this.dep2 = dep2;
	}

	public String getDep3() {
		return dep3;
	}

	public void setDep3(String dep3) {
		this.dep3 = dep3;
	}

	public String getDep4() {
		return dep4;
	}

	public void setDep4(String dep4) {
		this.dep4 = dep4;
	}

	public String getDep5() {
		return dep5;
	}

	public void setDep5(String dep5) {
		this.dep5 = dep5;
	}

	public String getDep6() {
		return dep6;
	}

	public void setDep6(String dep6) {
		this.dep6 = dep6;
	}

	public String getDep7() {
		return dep7;
	}

	public void setDep7(String dep7) {
		this.dep7 = dep7;
	}

	@Override
	public String toString() {
		return "HadsInsertModel [userid=" + userid + ", dateTime=" + dateTime + ", anx1=" + anx1 + ", anx2=" + anx2
				+ ", anx3=" + anx3 + ", anx4=" + anx4 + ", anx5=" + anx5 + ", anx6=" + anx6 + ", anx7=" + anx7
				+ ", dep1=" + dep1 + ", dep2=" + dep2 + ", dep3=" + dep3 + ", dep4=" + dep4 + ", dep5=" + dep5
				+ ", dep6=" + dep6 + ", dep7=" + dep7 + "]";
	}
}
