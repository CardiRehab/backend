package com.healthcare.herplatform.models;


import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OthersActivityInsertModel {
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid;
		
	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dtTime;
	
	@JsonProperty("activityName")
	@NotNull(message="Activity is Required")
	private String activityName;
	
	@JsonProperty("valQuantity")
	@NotBlank(message="Value or Quantity is Required")
	private String valQuantity;

	public OthersActivityInsertModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OthersActivityInsertModel(int userid, Date dtTime, String activityName, String valQuantity) {
		super();
		this.userid = userid;
		this.dtTime = dtTime;
		this.activityName = activityName;
		this.valQuantity = valQuantity;
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

	public String getValQuantity() {
		return valQuantity;
	}

	public void setValQuantity(String valQuantity) {
		this.valQuantity = valQuantity;
	}

	@Override
	public String toString() {
		return "OthersActivityInsertModel [userid=" + userid + ", dtTime=" + dtTime + ", activityName=" + activityName
				+ ", valQuantity=" + valQuantity + "]";
	}
}
