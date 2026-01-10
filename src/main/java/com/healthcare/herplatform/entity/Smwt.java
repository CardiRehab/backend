package com.healthcare.herplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Table(name = "smwt")
@Entity(name = "smwt")
public class Smwt {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	@Column(name = "userid", updatable = true, insertable = true)
	int userId;

	@Column(name = "date_time", updatable = true, insertable = true)
	Date dateTime;
	
	@Column(name = "name", updatable = true, insertable = true)
	@NotBlank
	private String name;

	@Column(name = "dob", updatable = true, insertable = true)
	@NotNull
	private Date dob;

	@Column(name = "testdate", updatable = true, insertable = true)
	@NotNull
	private Date testdate;
	
	@Column(name = "gender", updatable = true, insertable = true)
	@NotBlank
	private String gender;

	@Column(name = "height", updatable = true, insertable = true)
	@NotBlank
	private String height;

	@Column(name = "weight", updatable = true, insertable = true)
	@NotBlank
	private String weight;

	@Column(name = "prebloodpressure", updatable = true, insertable = true)
	@NotBlank
	private String prebloodpressure;

	@Column(name = "prepulserate", updatable = true, insertable = true)
	@NotBlank
	private String prepulserate;

	@Column(name = "preoxylevel", updatable = true, insertable = true)
	@NotBlank
	private String preoxylevel;

	@Column(name = "predyspnea", updatable = true, insertable = true)
	@NotBlank
	private String predyspnea;

	@Column(name = "prefatigue", updatable = true, insertable = true)
	@NotBlank
	private String prefatigue;

	@Column(name = "postbloodpressure", updatable = true, insertable = true)
	@NotBlank
	private String postbloodpressure;

	@Column(name = "postpulserate", updatable = true, insertable = true)
	@NotBlank
	private String postpulserate;

	@Column(name = "postoxylevel", updatable = true, insertable = true)
	@NotBlank
	private String postoxylevel;

	@Column(name = "postdyspnea", updatable = true, insertable = true)
	@NotBlank
	private String postdyspnea;

	@Column(name = "postfatigue", updatable = true, insertable = true)
	@NotBlank
	private String postfatigue;
	
	@Column(name = "stoppause", updatable = true, insertable = true)
	@NotBlank
	private String stoppause;

	@Column(name = "reason", updatable = true, insertable = true)
	@NotBlank
	private String reason;

	@Column(name = "chestpain", updatable = true, insertable = true)
	@NotBlank
	private String chestpain;
	
	@Column(name = "shortofbreath", updatable = true, insertable = true)
	@NotBlank
	private String shortofbreath;

	@Column(name = "lightheaded", updatable = true, insertable = true)
	@NotBlank
	private String lightheaded;
	
	@Column(name = "paininlegs", updatable = true, insertable = true)
	@NotBlank
	private String paininlegs;
	
	@Column(name = "others", updatable = true, insertable = true)
	@NotBlank
	private String others;

	@Column(name = "othersymptoms", updatable = true, insertable = true)
	@NotBlank
	private String othersymptoms;
	
	@Column(name = "numlaps", updatable = true, insertable = true)
	@NotBlank
	private String numlaps;

	@Column(name = "fplap", updatable = true, insertable = true)
	@NotBlank
	private String fplap;

	@Column(name = "totdistance", updatable = true, insertable = true)
	@NotBlank
	private String totdistance;

	public Smwt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Smwt(int userId, Date dateTime, @NotBlank String name, @NotNull Date dob, @NotNull Date testdate,
			@NotBlank String gender, @NotBlank String height, @NotBlank String weight,
			@NotBlank String prebloodpressure, @NotBlank String prepulserate, @NotBlank String preoxylevel,
			@NotBlank String predyspnea, @NotBlank String prefatigue, @NotBlank String postbloodpressure,
			@NotBlank String postpulserate, @NotBlank String postoxylevel, @NotBlank String postdyspnea,
			@NotBlank String postfatigue, @NotBlank String stoppause, @NotBlank String reason,
			@NotBlank String chestpain, @NotBlank String shortofbreath, @NotBlank String lightheaded,
			@NotBlank String paininlegs, @NotBlank String others, @NotBlank String othersymptoms,
			@NotBlank String numlaps, @NotBlank String fplap, @NotBlank String totdistance) {
		super();
		this.userId = userId;
		this.dateTime = dateTime;
		this.name = name;
		this.dob = dob;
		this.testdate = testdate;
		this.gender = gender;
		this.height = height;
		this.weight = weight;
		this.prebloodpressure = prebloodpressure;
		this.prepulserate = prepulserate;
		this.preoxylevel = preoxylevel;
		this.predyspnea = predyspnea;
		this.prefatigue = prefatigue;
		this.postbloodpressure = postbloodpressure;
		this.postpulserate = postpulserate;
		this.postoxylevel = postoxylevel;
		this.postdyspnea = postdyspnea;
		this.postfatigue = postfatigue;
		this.stoppause = stoppause;
		this.reason = reason;
		this.chestpain = chestpain;
		this.shortofbreath = shortofbreath;
		this.lightheaded = lightheaded;
		this.paininlegs = paininlegs;
		this.others = others;
		this.othersymptoms = othersymptoms;
		this.numlaps = numlaps;
		this.fplap = fplap;
		this.totdistance = totdistance;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getTestdate() {
		return testdate;
	}

	public void setTestdate(Date testdate) {
		this.testdate = testdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPrebloodpressure() {
		return prebloodpressure;
	}

	public void setPrebloodpressure(String prebloodpressure) {
		this.prebloodpressure = prebloodpressure;
	}

	public String getPrepulserate() {
		return prepulserate;
	}

	public void setPrepulserate(String prepulserate) {
		this.prepulserate = prepulserate;
	}

	public String getPreoxylevel() {
		return preoxylevel;
	}

	public void setPreoxylevel(String preoxylevel) {
		this.preoxylevel = preoxylevel;
	}

	public String getPredyspnea() {
		return predyspnea;
	}

	public void setPredyspnea(String predyspnea) {
		this.predyspnea = predyspnea;
	}

	public String getPrefatigue() {
		return prefatigue;
	}

	public void setPrefatigue(String prefatigue) {
		this.prefatigue = prefatigue;
	}

	public String getPostbloodpressure() {
		return postbloodpressure;
	}

	public void setPostbloodpressure(String postbloodpressure) {
		this.postbloodpressure = postbloodpressure;
	}

	public String getPostpulserate() {
		return postpulserate;
	}

	public void setPostpulserate(String postpulserate) {
		this.postpulserate = postpulserate;
	}

	public String getPostoxylevel() {
		return postoxylevel;
	}

	public void setPostoxylevel(String postoxylevel) {
		this.postoxylevel = postoxylevel;
	}

	public String getPostdyspnea() {
		return postdyspnea;
	}

	public void setPostdyspnea(String postdyspnea) {
		this.postdyspnea = postdyspnea;
	}

	public String getPostfatigue() {
		return postfatigue;
	}

	public void setPostfatigue(String postfatigue) {
		this.postfatigue = postfatigue;
	}

	public String getStoppause() {
		return stoppause;
	}

	public void setStoppause(String stoppause) {
		this.stoppause = stoppause;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getChestpain() {
		return chestpain;
	}

	public void setChestpain(String chestpain) {
		this.chestpain = chestpain;
	}

	public String getShortofbreath() {
		return shortofbreath;
	}

	public void setShortofbreath(String shortofbreath) {
		this.shortofbreath = shortofbreath;
	}

	public String getLightheaded() {
		return lightheaded;
	}

	public void setLightheaded(String lightheaded) {
		this.lightheaded = lightheaded;
	}

	public String getPaininlegs() {
		return paininlegs;
	}

	public void setPaininlegs(String paininlegs) {
		this.paininlegs = paininlegs;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getOthersymptoms() {
		return othersymptoms;
	}

	public void setOthersymptoms(String othersymptoms) {
		this.othersymptoms = othersymptoms;
	}

	public String getNumlaps() {
		return numlaps;
	}

	public void setNumlaps(String numlaps) {
		this.numlaps = numlaps;
	}

	public String getFplap() {
		return fplap;
	}

	public void setFplap(String fplap) {
		this.fplap = fplap;
	}

	public String getTotdistance() {
		return totdistance;
	}

	public void setTotdistance(String totdistance) {
		this.totdistance = totdistance;
	}

	@Override
	public String toString() {
		return "Smwt [id=" + id + ", userId=" + userId + ", dateTime=" + dateTime + ", name=" + name + ", dob=" + dob
				+ ", testdate=" + testdate + ", gender=" + gender + ", height=" + height + ", weight=" + weight
				+ ", prebloodpressure=" + prebloodpressure + ", prepulserate=" + prepulserate + ", preoxylevel="
				+ preoxylevel + ", predyspnea=" + predyspnea + ", prefatigue=" + prefatigue + ", postbloodpressure="
				+ postbloodpressure + ", postpulserate=" + postpulserate + ", postoxylevel=" + postoxylevel
				+ ", postdyspnea=" + postdyspnea + ", postfatigue=" + postfatigue + ", stoppause=" + stoppause
				+ ", reason=" + reason + ", chestpain=" + chestpain + ", shortofbreath=" + shortofbreath
				+ ", lightheaded=" + lightheaded + ", paininlegs=" + paininlegs + ", others=" + others
				+ ", othersymptoms=" + othersymptoms + ", numlaps=" + numlaps + ", fplap=" + fplap + ", totdistance="
				+ totdistance + "]";
	}
}