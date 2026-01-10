package com.healthcare.herplatform.models;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SmwtInsertModel {
	// add @NotNull annotation to an integer
	@JsonProperty("userid")
	@NotNull(message = "User ID is required")
	private int userid;

	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dateTime;

	@JsonProperty("name")
	@NotBlank(message = "Name is Required")
	private String name;

	@JsonProperty("dob")
	@NotNull(message = "Date of birth is Required")
	private Date dob;

	@JsonProperty("testdate")
	@NotNull(message = "Test date is Required")
	private Date testdate;
	
	@JsonProperty("gender")
	@NotBlank(message = "Gender is Required")
	private String gender;

	@JsonProperty("height")
	@NotBlank(message = "Height is Required")
	private String height;

	@JsonProperty("weight")
	@NotBlank(message = "Weight is Required")
	private String weight;

	@JsonProperty("prebloodpressure")
	@NotBlank(message = "Pre blood pressure is Required")
	private String prebloodpressure;

	@JsonProperty("prepulserate")
	@NotBlank(message = "Pre pulse rate is Required")
	private String prepulserate;

	@JsonProperty("preoxylevel")
	@NotBlank(message = "Pre oxygen level is Required")
	private String preoxylevel;

	@JsonProperty("predyspnea")
	@NotBlank(message = "Pre dyspnea is Required")
	private String predyspnea;

	@JsonProperty("prefatigue")
	@NotBlank(message = "Pre fatigue is Required")
	private String prefatigue;

	@JsonProperty("postbloodpressure")
	@NotBlank(message = "Post blood pressure is Required")
	private String postbloodpressure;

	@JsonProperty("postpulserate")
	@NotBlank(message = "Post pulse rate is Required")
	private String postpulserate;

	@JsonProperty("postoxylevel")
	@NotBlank(message = "Post oxygen level is Required")
	private String postoxylevel;

	@JsonProperty("postdyspnea")
	@NotBlank(message = "Post dyspnea is Required")
	private String postdyspnea;

	@JsonProperty("postfatigue")
	@NotBlank(message = "Post fatigue is Required")
	private String postfatigue;
	
	
	@JsonProperty("stoppause")
	@NotBlank(message = "Stop or Pause is Required")
	private String stoppause;

	@JsonProperty("reason")
	@NotBlank(message = "Reason is Required")
	private String reason;

	@JsonProperty("chestpain")
	@NotBlank(message = "Chest pain is Required")
	private String chestpain;
	
	@JsonProperty("shortofbreath")
	@NotBlank(message = "Shortness of breath is Required")
	private String shortofbreath;

	@JsonProperty("lightheaded")
	@NotBlank(message = "Lightheaded is Required")
	private String lightheaded;
	
	@JsonProperty("paininlegs")
	@NotBlank(message = "Pain in legs is Required")
	private String paininlegs;
	
	@JsonProperty("others")
	@NotBlank(message = "Others is Required")
	private String others;

	@JsonProperty("othersymptoms")
	@NotBlank(message = "Other symptoms is Required")
	private String othersymptoms;
	
	@JsonProperty("numlaps")
	@NotBlank(message = "Number of laps is Required")
	private String numlaps;

	@JsonProperty("fplap")
	@NotBlank(message = "Final partial lap is Required")
	private String fplap;

	@JsonProperty("totdistance")
	@NotBlank(message = "Total distance is Required")
	private String totdistance;
	
	public SmwtInsertModel() {
		super();
	}

	public SmwtInsertModel(@NotNull(message = "User ID is required") int userid,
			@NotNull(message = "Date/Time is required") Date dateTime,
			@NotBlank(message = "Name is Required") String name,
			@NotNull(message = "Date of birth is Required") Date dob,
			@NotNull(message = "Test date is Required") Date testdate,
			@NotBlank(message = "Gender is Required") String gender,
			@NotBlank(message = "Height is Required") String height,
			@NotBlank(message = "Weight is Required") String weight,
			@NotBlank(message = "Pre blood pressure is Required") String prebloodpressure,
			@NotBlank(message = "Pre pulse rate is Required") String prepulserate,
			@NotBlank(message = "Pre oxygen level is Required") String preoxylevel,
			@NotBlank(message = "Pre dyspnea is Required") String predyspnea,
			@NotBlank(message = "Pre fatigue is Required") String prefatigue,
			@NotBlank(message = "Post blood pressure is Required") String postbloodpressure,
			@NotBlank(message = "Post pulse rate is Required") String postpulserate,
			@NotBlank(message = "Post oxygen level is Required") String postoxylevel,
			@NotBlank(message = "Post dyspnea is Required") String postdyspnea,
			@NotBlank(message = "Post fatigue is Required") String postfatigue,
			@NotBlank(message = "Stop or Pause is Required") String stoppause,
			@NotBlank(message = "Reason is Required") String reason,
			@NotBlank(message = "Chest pain is Required") String chestpain,
			@NotBlank(message = "Shortness of breath is Required") String shortofbreath,
			@NotBlank(message = "Lightheaded is Required") String lightheaded,
			@NotBlank(message = "Pain in legs is Required") String paininlegs,
			@NotBlank(message = "Others is Required") String others,
			@NotBlank(message = "Other symptoms is Required") String othersymptoms,
			@NotBlank(message = "Number of laps is Required") String numlaps,
			@NotBlank(message = "Final partial lap is Required") String fplap,
			@NotBlank(message = "Total distance is Required") String totdistance) {
		super();
		this.userid = userid;
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

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
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
		return "SmwtInsertModel [userid=" + userid + ", dateTime=" + dateTime + ", name=" + name + ", dob=" + dob
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