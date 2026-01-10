package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name = "hads")
@Entity(name = "hads")
public class Hads {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	@Column(name = "userid", updatable = true, insertable = true)
	int userId;

	@Column(name = "date_time", updatable = true, insertable = true)
	Date dtTime;

	@Column(name = "anx1", updatable = true, insertable = true)
	@NotBlank
	String anx1;

	@Column(name = "anx2", updatable = true, insertable = true)
	@NotBlank
	String anx2;

	@Column(name = "anx3", updatable = true, insertable = true)
	@NotBlank
	String anx3;

	@Column(name = "anx4", updatable = true, insertable = true)
	@NotBlank
	String anx4;

	@Column(name = "anx5", updatable = true, insertable = true)
	@NotBlank
	String anx5;

	@Column(name = "anx6", updatable = true, insertable = true)
	@NotBlank
	String anx6;

	@Column(name = "anx7", updatable = true, insertable = true)
	@NotBlank
	String anx7;

	@Column(name = "dep1", updatable = true, insertable = true)
	@NotBlank
	String dep1;

	@Column(name = "dep2", updatable = true, insertable = true)
	@NotBlank
	String dep2;

	@Column(name = "dep3", updatable = true, insertable = true)
	@NotBlank
	String dep3;

	@Column(name = "dep4", updatable = true, insertable = true)
	@NotBlank
	String dep4;

	@Column(name = "dep5", updatable = true, insertable = true)
	@NotBlank
	String dep5;

	@Column(name = "dep6", updatable = true, insertable = true)
	@NotBlank
	String dep6;

	@Column(name = "dep7", updatable = true, insertable = true)
	@NotBlank
	String dep7;

	@Column(name = "totalAnxScore", updatable = true, insertable = true)
	@NotBlank
	String totAnxScore;

	@Column(name = "totalDepScore", updatable = true, insertable = true)
	@NotBlank
	String totDepScore;

	public Hads() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Hads(int userId, Date dtTime, @NotBlank String anx1, @NotBlank String anx2, @NotBlank String anx3,
			@NotBlank String anx4, @NotBlank String anx5, @NotBlank String anx6, @NotBlank String anx7,
			@NotBlank String dep1, @NotBlank String dep2, @NotBlank String dep3, @NotBlank String dep4,
			@NotBlank String dep5, @NotBlank String dep6, @NotBlank String dep7, @NotBlank String totAnxScore,
			@NotBlank String totDepScore) {
		super();
		this.userId = userId;
		this.dtTime = dtTime;
		this.anx1 = anx1;
		this.anx2 = anx2;
		this.anx3 = anx3;
		this.anx4 = anx4;
		this.anx5 = anx5;
		this.anx6 = anx6;
		this.anx7 = anx7;
		this.dep1 = dep1;
		this.dep2 = dep2;
		this.dep3 = dep3;
		this.dep4 = dep4;
		this.dep5 = dep5;
		this.dep6 = dep6;
		this.dep7 = dep7;
		this.totAnxScore = totAnxScore;
		this.totDepScore = totDepScore;
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

	public String getAnx1() {
		return anx1;
	}

	public void setAnx1(String anx1) {
		this.anx1 = anx1;
	}

	public String getAnx2() {
		return anx2;
	}

	public void setAnx2(String anx2) {
		this.anx2 = anx2;
	}

	public String getAnx3() {
		return anx3;
	}

	public void setAnx3(String anx3) {
		this.anx3 = anx3;
	}

	public String getAnx4() {
		return anx4;
	}

	public void setAnx4(String anx4) {
		this.anx4 = anx4;
	}

	public String getAnx5() {
		return anx5;
	}

	public void setAnx5(String anx5) {
		this.anx5 = anx5;
	}

	public String getAnx6() {
		return anx6;
	}

	public void setAnx6(String anx6) {
		this.anx6 = anx6;
	}

	public String getAnx7() {
		return anx7;
	}

	public void setAnx7(String anx7) {
		this.anx7 = anx7;
	}

	public String getDep1() {
		return dep1;
	}

	public void setDep1(String dep1) {
		this.dep1 = dep1;
	}

	public String getDep2() {
		return dep2;
	}

	public void setDep2(String dep2) {
		this.dep2 = dep2;
	}

	public String getDep3() {
		return dep3;
	}

	public void setDep3(String dep3) {
		this.dep3 = dep3;
	}

	public String getDep4() {
		return dep4;
	}

	public void setDep4(String dep4) {
		this.dep4 = dep4;
	}

	public String getDep5() {
		return dep5;
	}

	public void setDep5(String dep5) {
		this.dep5 = dep5;
	}

	public String getDep6() {
		return dep6;
	}

	public void setDep6(String dep6) {
		this.dep6 = dep6;
	}

	public String getDep7() {
		return dep7;
	}

	public void setDep7(String dep7) {
		this.dep7 = dep7;
	}

	public String getTotAnxScore() {
		return totAnxScore;
	}

	public void setTotAnxScore(String totAnxScore) {
		this.totAnxScore = totAnxScore;
	}

	public String getTotDepScore() {
		return totDepScore;
	}

	public void setTotDepScore(String totDepScore) {
		this.totDepScore = totDepScore;
	}

	@Override
	public String toString() {
		return "Hads [id=" + id + ", userId=" + userId + ", dtTime=" + dtTime + ", anx1=" + anx1 + ", anx2=" + anx2
				+ ", anx3=" + anx3 + ", anx4=" + anx4 + ", anx5=" + anx5 + ", anx6=" + anx6 + ", anx7=" + anx7
				+ ", dep1=" + dep1 + ", dep2=" + dep2 + ", dep3=" + dep3 + ", dep4=" + dep4 + ", dep5=" + dep5
				+ ", dep6=" + dep6 + ", dep7=" + dep7 + ", totAnxScore=" + totAnxScore + ", totDepScore=" + totDepScore
				+ "]";
	}
}
