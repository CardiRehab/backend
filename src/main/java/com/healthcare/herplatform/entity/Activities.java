package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name="activities")
@Entity(name="activities")
public class Activities {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "userid", updatable = true, insertable = true)
    int userId;
	
//	@Column(name = "week_day_id", updatable = true, insertable = true)
//    int weekDayId;
	
	@Column(name = "date_time", updatable = true, insertable = true)
	Date dtTime;
	
	@Column(name = "activity_name", updatable = true, insertable = true)
	@NotBlank
	String activityName;
	
	@Column(name = "pre_hr", updatable = true, insertable = true)
	@NotBlank
	String preHR;
	
	@Column(name = "post_hr", updatable = true, insertable = true)
	@NotBlank
	String postHR;
	
	@Column(name = "results", updatable = true, insertable = true)
	@NotBlank
	String results;
	
	@Column(name = "rpe_borg", updatable = true, insertable = true)
	@NotBlank
	String rpeBorg;
	
	@Column(name = "symptoms", updatable = true, insertable = true)
	@NotBlank
	String symptoms;

	public Activities() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Activities(@NotBlank int userId, @NotBlank Date dtTime, @NotBlank String activityName,
			@NotBlank String preHR, @NotBlank String postHR, @NotBlank String results, @NotBlank String rpeBorg, 
			@NotBlank String symptoms) {
		super();
		this.userId = userId;
		this.dtTime = dtTime;
		this.activityName = activityName;
		this.preHR = preHR;
		this.postHR = postHR;
		this.results = results;
		this.rpeBorg = rpeBorg;
		this.symptoms = symptoms;
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

//	public int getWeekDayId() {
//		return weekDayId;
//	}
//
//	public void setWeekDayId(int weekDayId) {
//		this.weekDayId = weekDayId;
//	}

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
		return "Activities [id=" + id + ", userId=" + userId + ", dtTime=" + dtTime + ", activityName=" + activityName
				+ ", preHR=" + preHR + ", postHR=" + postHR + ", results=" + results + ", rpeBorg=" + rpeBorg
				+ ", symptoms=" + symptoms + "]";
	}
}
