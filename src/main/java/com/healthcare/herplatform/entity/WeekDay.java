package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Table(name="week_day")
@Entity(name="week_day")
public class WeekDay {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "week_name_id", updatable = true, insertable = true)
    int weekNameId;
	
	@Column(name = "week_day", updatable = true, insertable = true)
	@NotBlank
	String weekDay;

	public WeekDay() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeekDay(int weekNameId, @NotBlank String weekDay) {
		super();
		this.weekNameId = weekNameId;
		this.weekDay = weekDay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeekNameId() {
		return weekNameId;
	}

	public void setWeekNameId(int weekNameId) {
		this.weekNameId = weekNameId;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	@Override
	public String toString() {
		return "WeekDay [id=" + id + ", weekNameId=" + weekNameId + ", weekDay=" + weekDay + "]";
	}
}
