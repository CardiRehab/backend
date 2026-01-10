package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Table(name="activities_type")
@Entity(name="activities_type")
public class ActivitiesType {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "activities_type", updatable = true, insertable = true)
	@NotBlank
	String activitiesType;

	public ActivitiesType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ActivitiesType(@NotBlank String activitiesType) {
		super();
		this.activitiesType = activitiesType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActivitiesType() {
		return activitiesType;
	}

	public void setActivitiesType(String activitiesType) {
		this.activitiesType = activitiesType;
	}

	@Override
	public String toString() {
		return "ActivitiesType [id=" + id + ", activitiesType=" + activitiesType + "]";
	}
}


