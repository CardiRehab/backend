package com.healthcare.herplatform.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ActivityInsertModel {
	// add @NotNull annotation to an integer
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid;
		
	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dtTime;
	
	@JsonProperty("activityName")
	@NotNull(message="Activity is Required")
	private String activityName;
	
	@JsonProperty("preHR")
	@NotBlank(message="PreHR is Required")
	private String preHR;
	
	@JsonProperty("postHR")
	@NotBlank(message="PostHR is Required")
	private String postHR;
	
	@JsonProperty("results")
	@NotBlank(message="Result is Required")
	private String results;
	
	@JsonProperty("rpeBorg")
	@NotBlank(message="RPE/BORG is Required")
	private String rpeBorg;
	
	@JsonProperty("symptoms")
	@NotBlank(message="Symptoms is Required")
	private String symptoms;
	
	public ActivityInsertModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ActivityInsertModel(int userid, Date dtTime, String activityName, String preHR, String postHR,
			String results, String rpeBorg, String symptoms) {
		super();
		this.userid = userid;
		this.dtTime = dtTime;
		this.activityName = activityName;
		this.preHR = preHR;
		this.postHR = postHR;
		this.results = results;
		this.rpeBorg = rpeBorg;
		this.symptoms = symptoms;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public Date getDtTime() {
		return dtTime;
	}

	public void setDtTime(Date dtTime) {
		this.dtTime = dtTime;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getPreHR() {
		return preHR;
	}

	public void setPreHR(String preHR) {
		this.preHR = preHR;
	}

	public String getPostHR() {
		return postHR;
	}

	public void setPostHR(String postHR) {
		this.postHR = postHR;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getRpeBorg() {
		return rpeBorg;
	}

	public void setRpeBorg(String rpeBorg) {
		this.rpeBorg = rpeBorg;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	@Override
	public String toString() {
		return "ActivityInsertModel [userid=" + userid + ", dtTime=" + dtTime + ", activityName=" + activityName
				+ ", preHR=" + preHR + ", postHR=" + postHR + ", results=" + results + ", rpeBorg=" + rpeBorg
				+ ", symptoms=" + symptoms + "]";
	}
}
