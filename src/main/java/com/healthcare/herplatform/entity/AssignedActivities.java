package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name="assigned_activities")
@Entity(name="assigned_activities")
public class AssignedActivities {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "userid", updatable = true, insertable = true)
	//@NotBlank
    int userid;
	
	@Column(name = "acttypeid", updatable = true, insertable = true)
	//@NotBlank
    int acttypeid;
	
	@Column(name = "curr_date_time", updatable = true, insertable = true)
	//@NotBlank
	Date currDateTime;
	
	@Column(name = "activities_type", updatable = true, insertable = true)
	//@NotBlank
	String activitiesType;
	
	@Column(name = "act_freq", updatable = true, insertable = true)
	String actFreq;
	
	@Column(name = "peakhr", updatable = true, insertable = true)
	String peakHR;
	
	@Column(name = "peak_borgrpe", updatable = true, insertable = true)
	String peakBorgRPE;
	
	@Column(name = "min_duration", updatable = true, insertable = true)
	String minDuration;
	
	@Column(name = "act_status", updatable = true, insertable = true)
	String actStatus;
	
	@Column(name = "comments", updatable = true, insertable = true)
	String comments;
	
	@Column(name = "act_work_status", updatable = true, insertable = true)
	String actWorkStatus;

	@Column(name = "work_status_changeddt", updatable = true, insertable = true)
	Date workStatusChangedDT;
	
	public AssignedActivities() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssignedActivities(@NotBlank int userid, @NotBlank int acttypeid, @NotBlank Date currDateTime,
			@NotBlank String activitiesType, String actFreq, String peakHR, String peakBorgRPE, String minDuration,
			String actStatus, String comments, String actWorkStatus, Date workStatusChangedDT) {
		super();
		this.userid = userid;
		this.acttypeid = acttypeid;
		this.currDateTime = currDateTime;
		this.activitiesType = activitiesType;
		this.actFreq = actFreq;
		this.peakHR = peakHR;
		this.peakBorgRPE = peakBorgRPE;
		this.minDuration = minDuration;
		this.actStatus = actStatus;
		this.comments = comments;
		this.actWorkStatus = actWorkStatus;
		this.workStatusChangedDT = workStatusChangedDT;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getActtypeid() {
		return acttypeid;
	}

	public void setActtypeid(int acttypeid) {
		this.acttypeid = acttypeid;
	}

	public Date getCurrDateTime() {
		return currDateTime;
	}

	public void setCurrDateTime(Date currDateTime) {
		this.currDateTime = currDateTime;
	}

	public String getActivitiesType() {
		return activitiesType;
	}

	public void setActivitiesType(String activitiesType) {
		this.activitiesType = activitiesType;
	}

	public String getActFreq() {
		return actFreq;
	}

	public void setActFreq(String actFreq) {
		this.actFreq = actFreq;
	}

	public String getPeakHR() {
		return peakHR;
	}

	public void setPeakHR(String peakHR) {
		this.peakHR = peakHR;
	}

	public String getPeakBorgRPE() {
		return peakBorgRPE;
	}

	public void setPeakBorgRPE(String peakBorgRPE) {
		this.peakBorgRPE = peakBorgRPE;
	}

	public String getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(String minDuration) {
		this.minDuration = minDuration;
	}

	public String getActStatus() {
		return actStatus;
	}

	public void setActStatus(String actStatus) {
		this.actStatus = actStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getActWorkStatus() {
		return actWorkStatus;
	}

	public void setActWorkStatus(String actWorkStatus) {
		this.actWorkStatus = actWorkStatus;
	}

	public Date getWorkStatusChangedDT() {
		return workStatusChangedDT;
	}

	public void setWorkStatusChangedDT(Date workStatusChangedDT) {
		this.workStatusChangedDT = workStatusChangedDT;
	}

	@Override
	public String toString() {
		return "AssignedActivities [id=" + id + ", userid=" + userid + ", acttypeid=" + acttypeid + ", currDateTime="
				+ currDateTime + ", activitiesType=" + activitiesType + ", actFreq=" + actFreq + ", peakHR=" + peakHR
				+ ", peakBorgRPE=" + peakBorgRPE + ", minDuration=" + minDuration + ", actStatus=" + actStatus
				+ ", comments=" + comments + ", actWorkStatus=" + actWorkStatus + ", workStatusChangedDT="
				+ workStatusChangedDT + "]";
	}
}
