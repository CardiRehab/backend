package com.healthcare.herplatform.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name="gad7")
@Entity(name="gad7")
public class Gad7 {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    int id;
	
	@Column(name = "userid", updatable = true, insertable = true)
    int userId;
	
	@Column(name = "date_time", updatable = true, insertable = true)
	Date dtTime;
	
	@Column(name = "gad1", updatable = true, insertable = true)
	@NotBlank
	String gad1;
	
	@Column(name = "gad2", updatable = true, insertable = true)
	@NotBlank
	String gad2;

	@Column(name = "gad3", updatable = true, insertable = true)
	@NotBlank
	String gad3;

	@Column(name = "gad4", updatable = true, insertable = true)
	@NotBlank
	String gad4;

	@Column(name = "gad5", updatable = true, insertable = true)
	@NotBlank
	String gad5;

	@Column(name = "gad6", updatable = true, insertable = true)
	@NotBlank
	String gad6;

	@Column(name = "gad7", updatable = true, insertable = true)
	@NotBlank
	String gad7;

	@Column(name = "diffLevel", updatable = true, insertable = true)
	@NotBlank
	String diffLevel;

	@Column(name = "totalScore", updatable = true, insertable = true)
	@NotBlank
	String totScore;

	public Gad7() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Gad7(int userId, Date dtTime, @NotBlank String gad1, @NotBlank String gad2, @NotBlank String gad3,
			@NotBlank String gad4, @NotBlank String gad5, @NotBlank String gad6, @NotBlank String gad7,
			@NotBlank String diffLevel, @NotBlank String totScore) {
		super();
		this.userId = userId;
		this.dtTime = dtTime;
		this.gad1 = gad1;
		this.gad2 = gad2;
		this.gad3 = gad3;
		this.gad4 = gad4;
		this.gad5 = gad5;
		this.gad6 = gad6;
		this.gad7 = gad7;
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

	public String getGad1() {
		return gad1;
	}

	public void setGad1(String gad1) {
		this.gad1 = gad1;
	}

	public String getGad2() {
		return gad2;
	}

	public void setGad2(String gad2) {
		this.gad2 = gad2;
	}

	public String getGad3() {
		return gad3;
	}

	public void setGad3(String gad3) {
		this.gad3 = gad3;
	}

	public String getGad4() {
		return gad4;
	}

	public void setGad4(String gad4) {
		this.gad4 = gad4;
	}

	public String getGad5() {
		return gad5;
	}

	public void setGad5(String gad5) {
		this.gad5 = gad5;
	}

	public String getGad6() {
		return gad6;
	}

	public void setGad6(String gad6) {
		this.gad6 = gad6;
	}

	public String getGad7() {
		return gad7;
	}

	public void setGad7(String gad7) {
		this.gad7 = gad7;
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
		return "Gad7 [id=" + id + ", userId=" + userId + ", dtTime=" + dtTime + ", gad1=" + gad1 + ", gad2=" + gad2
				+ ", gad3=" + gad3 + ", gad4=" + gad4 + ", gad5=" + gad5 + ", gad6=" + gad6 + ", gad7=" + gad7
				+ ", diffLevel=" + diffLevel + ", totScore=" + totScore + "]";
	}
}








