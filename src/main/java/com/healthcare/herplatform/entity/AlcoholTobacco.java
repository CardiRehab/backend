package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name = "alcohol_tobacco")
@Entity(name = "alcoholtobacco")
public class AlcoholTobacco {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	@Column(name = "userid", updatable = true, insertable = true)
	int userId;

	@Column(name = "date_time", updatable = true, insertable = true)
	Date dateTime;

	@Column(name = "smoke", updatable = true, insertable = true)
	@NotBlank
	String smk;

	@Column(name = "alcScreen1", updatable = true, insertable = true)
	@NotBlank
	String alcScreen1;

	@Column(name = "alcScreen2", updatable = true, insertable = true)
	@NotBlank
	String alcScreen2;

	@Column(name = "alcScreen3", updatable = true, insertable = true)
	@NotBlank
	String alcScreen3;

	@Column(name = "alcFullScreen4", updatable = true, insertable = true)
	@NotBlank
	String alcFullScreen4;

	@Column(name = "alcFullScreen5", updatable = true, insertable = true)
	@NotBlank
	String alcFullScreen5;

	@Column(name = "alcFullScreen6", updatable = true, insertable = true)
	@NotBlank
	String alcFullScreen6;

	@Column(name = "alcFullScreen7", updatable = true, insertable = true)
	@NotBlank
	String alcFullScreen7;

	@Column(name = "alcFullScreen8", updatable = true, insertable = true)
	@NotBlank
	String alcFullScreen8;

	@Column(name = "alcFullScreen9", updatable = true, insertable = true)
	@NotBlank
	String alcFullScreen9;

	@Column(name = "alcFullScreen10", updatable = true, insertable = true)
	@NotBlank
	String alcFullScreen10;

	@Column(name = "totalAlcoholScreen", updatable = true, insertable = true)
	@NotBlank
	String totalAlcoholScreen;

	@Column(name = "totalFullScreen", updatable = true, insertable = true)
	@NotBlank
	String totalFullScreen;

	@Column(name = "grandTotalScore", updatable = true, insertable = true)
	@NotBlank
	String grandTotalScore;

	public AlcoholTobacco() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AlcoholTobacco(int userId, Date dateTime, @NotBlank String smk, @NotBlank String alcScreen1,
			@NotBlank String alcScreen2, @NotBlank String alcScreen3, @NotBlank String alcFullScreen4,
			@NotBlank String alcFullScreen5, @NotBlank String alcFullScreen6, @NotBlank String alcFullScreen7,
			@NotBlank String alcFullScreen8, @NotBlank String alcFullScreen9, @NotBlank String alcFullScreen10,
			@NotBlank String totalAlcoholScreen, @NotBlank String totalFullScreen, @NotBlank String grandTotalScore) {
		super();
		this.userId = userId;
		this.dateTime = dateTime;
		this.smk = smk;
		this.alcScreen1 = alcScreen1;
		this.alcScreen2 = alcScreen2;
		this.alcScreen3 = alcScreen3;
		this.alcFullScreen4 = alcFullScreen4;
		this.alcFullScreen5 = alcFullScreen5;
		this.alcFullScreen6 = alcFullScreen6;
		this.alcFullScreen7 = alcFullScreen7;
		this.alcFullScreen8 = alcFullScreen8;
		this.alcFullScreen9 = alcFullScreen9;
		this.alcFullScreen10 = alcFullScreen10;
		this.totalAlcoholScreen = totalAlcoholScreen;
		this.totalFullScreen = totalFullScreen;
		this.grandTotalScore = grandTotalScore;
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

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getSmk() {
		return smk;
	}

	public void setSmk(String smk) {
		this.smk = smk;
	}

	public String getAlcScreen1() {
		return alcScreen1;
	}

	public void setAlcScreen1(String alcScreen1) {
		this.alcScreen1 = alcScreen1;
	}

	public String getAlcScreen2() {
		return alcScreen2;
	}

	public void setAlcScreen2(String alcScreen2) {
		this.alcScreen2 = alcScreen2;
	}

	public String getAlcScreen3() {
		return alcScreen3;
	}

	public void setAlcScreen3(String alcScreen3) {
		this.alcScreen3 = alcScreen3;
	}

	public String getAlcFullScreen4() {
		return alcFullScreen4;
	}

	public void setAlcFullScreen4(String alcFullScreen4) {
		this.alcFullScreen4 = alcFullScreen4;
	}

	public String getAlcFullScreen5() {
		return alcFullScreen5;
	}

	public void setAlcFullScreen5(String alcFullScreen5) {
		this.alcFullScreen5 = alcFullScreen5;
	}

	public String getAlcFullScreen6() {
		return alcFullScreen6;
	}

	public void setAlcFullScreen6(String alcFullScreen6) {
		this.alcFullScreen6 = alcFullScreen6;
	}

	public String getAlcFullScreen7() {
		return alcFullScreen7;
	}

	public void setAlcFullScreen7(String alcFullScreen7) {
		this.alcFullScreen7 = alcFullScreen7;
	}

	public String getAlcFullScreen8() {
		return alcFullScreen8;
	}

	public void setAlcFullScreen8(String alcFullScreen8) {
		this.alcFullScreen8 = alcFullScreen8;
	}

	public String getAlcFullScreen9() {
		return alcFullScreen9;
	}

	public void setAlcFullScreen9(String alcFullScreen9) {
		this.alcFullScreen9 = alcFullScreen9;
	}

	public String getAlcFullScreen10() {
		return alcFullScreen10;
	}

	public void setAlcFullScreen10(String alcFullScreen10) {
		this.alcFullScreen10 = alcFullScreen10;
	}

	public String getTotalAlcoholScreen() {
		return totalAlcoholScreen;
	}

	public void setTotalAlcoholScreen(String totalAlcoholScreen) {
		this.totalAlcoholScreen = totalAlcoholScreen;
	}

	public String getTotalFullScreen() {
		return totalFullScreen;
	}

	public void setTotalFullScreen(String totalFullScreen) {
		this.totalFullScreen = totalFullScreen;
	}

	public String getGrandTotalScore() {
		return grandTotalScore;
	}

	public void setGrandTotalScore(String grandTotalScore) {
		this.grandTotalScore = grandTotalScore;
	}

	@Override
	public String toString() {
		return "AlcoholTobacco [id=" + id + ", userId=" + userId + ", dateTime=" + dateTime + ", smk=" + smk
				+ ", alcScreen1=" + alcScreen1 + ", alcScreen2=" + alcScreen2 + ", alcScreen3=" + alcScreen3
				+ ", alcFullScreen4=" + alcFullScreen4 + ", alcFullScreen5=" + alcFullScreen5 + ", alcFullScreen6="
				+ alcFullScreen6 + ", alcFullScreen7=" + alcFullScreen7 + ", alcFullScreen8=" + alcFullScreen8
				+ ", alcFullScreen9=" + alcFullScreen9 + ", alcFullScreen10=" + alcFullScreen10
				+ ", totalAlcoholScreen=" + totalAlcoholScreen + ", totalFullScreen=" + totalFullScreen
				+ ", grandTotalScore=" + grandTotalScore + "]";
	}
}
