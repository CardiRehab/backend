package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name="othersactivities")
@Entity(name="othersactivities")
public class OthersActivities {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "userid", updatable = true, insertable = true)
    int userId;
		
	@Column(name = "date_time", updatable = true, insertable = true)
	Date dtTime;
	
	@Column(name = "activity_name", updatable = true, insertable = true)
	@NotBlank
	String activityName;
	
	@Column(name = "valQuantity", updatable = true, insertable = true)
	@NotBlank
	String valQuantity;
	
	public OthersActivities() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public OthersActivities(int userId, Date dtTime, @NotBlank String activityName,
			@NotBlank String valQuantity) {
		super();
		this.userId = userId;
		this.dtTime = dtTime;
		this.activityName = activityName;
		this.valQuantity = valQuantity;
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
		return "OthersActivities [id=" + id + ", userId=" + userId + ", dtTime=" + dtTime + ", activityName="
				+ activityName + ", valQuantity=" + valQuantity + "]";
	}
}



