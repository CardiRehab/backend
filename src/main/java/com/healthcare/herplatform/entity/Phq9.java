package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name="phq9")
@Entity(name="phq9")
public class Phq9 {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "userid", updatable = true, insertable = true)
    int userId;
	
	@Column(name = "date_time", updatable = true, insertable = true)
	Date dtTime;
	
	@Column(name = "phq1", updatable = true, insertable = true)
	@NotBlank
	String phq1;
	
	@Column(name = "phq2", updatable = true, insertable = true)
	@NotBlank
	String phq2;

	@Column(name = "phq3", updatable = true, insertable = true)
	@NotBlank
	String phq3;

	@Column(name = "phq4", updatable = true, insertable = true)
	@NotBlank
	String phq4;

	@Column(name = "phq5", updatable = true, insertable = true)
	@NotBlank
	String phq5;

	@Column(name = "phq6", updatable = true, insertable = true)
	@NotBlank
	String phq6;

	@Column(name = "phq7", updatable = true, insertable = true)
	@NotBlank
	String phq7;

	@Column(name = "phq8", updatable = true, insertable = true)
	@NotBlank
	String phq8;

	@Column(name = "phq9", updatable = true, insertable = true)
	@NotBlank
	String phq9;
	
	@Column(name = "diffLevel", updatable = true, insertable = true)
	@NotBlank
	String diffLevel;

	@Column(name = "totalScore", updatable = true, insertable = true)
	@NotBlank
	String totScore;

	public Phq9() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Phq9(int userId, Date dtTime, @NotBlank String phq1, @NotBlank String phq2, @NotBlank String phq3,
			@NotBlank String phq4, @NotBlank String phq5, @NotBlank String phq6, @NotBlank String phq7,
			@NotBlank String phq8, @NotBlank String phq9, @NotBlank String diffLevel, @NotBlank String totScore) {
		super();
		this.userId = userId;
		this.dtTime = dtTime;
		this.phq1 = phq1;
		this.phq2 = phq2;
		this.phq3 = phq3;
		this.phq4 = phq4;
		this.phq5 = phq5;
		this.phq6 = phq6;
		this.phq7 = phq7;
		this.phq8 = phq8;
		this.phq9 = phq9;
		this.diffLevel = diffLevel;
		this.totScore = totScore;
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

	public String getPhq1() {
		return phq1;
	}

	public void setPhq1(String phq1) {
		this.phq1 = phq1;
	}

	public String getPhq2() {
		return phq2;
	}

	public void setPhq2(String phq2) {
		this.phq2 = phq2;
	}

	public String getPhq3() {
		return phq3;
	}

	public void setPhq3(String phq3) {
		this.phq3 = phq3;
	}

	public String getPhq4() {
		return phq4;
	}

	public void setPhq4(String phq4) {
		this.phq4 = phq4;
	}

	public String getPhq5() {
		return phq5;
	}

	public void setPhq5(String phq5) {
		this.phq5 = phq5;
	}

	public String getPhq6() {
		return phq6;
	}

	public void setPhq6(String phq6) {
		this.phq6 = phq6;
	}

	public String getPhq7() {
		return phq7;
	}

	public void setPhq7(String phq7) {
		this.phq7 = phq7;
	}

	public String getPhq8() {
		return phq8;
	}

	public void setPhq8(String phq8) {
		this.phq8 = phq8;
	}

	public String getPhq9() {
		return phq9;
	}

	public void setPhq9(String phq9) {
		this.phq9 = phq9;
	}

	public String getDiffLevel() {
		return diffLevel;
	}

	public void setDiffLevel(String diffLevel) {
		this.diffLevel = diffLevel;
	}
	
	public String getTotScore() {
		return totScore;
	}

	public void setTotScore(String totScore) {
		this.totScore = totScore;
	}

	@Override
	public String toString() {
		return "Phq9 [id=" + id + ", userId=" + userId + ", dtTime=" + dtTime + ", phq1=" + phq1 + ", phq2=" + phq2
				+ ", phq3=" + phq3 + ", phq4=" + phq4 + ", phq5=" + phq5 + ", phq6=" + phq6 + ", phq7=" + phq7
				+ ", phq8=" + phq8 + ", phq9=" + phq9 + ", diffLevel=" + diffLevel + ", totScore=" + totScore + "]";
	}
}


