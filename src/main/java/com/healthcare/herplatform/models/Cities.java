package com.healthcare.herplatform.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="cities")
public class Cities {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
    int cityid;
	
	@Column(name = "name", updatable = false, insertable = false)
    String cityname;
	
	@Column(name = "state_id", updatable = false, insertable = false)
    int stateid;
	
	public Cities() {
	}

	public Cities(int cityid, String cityname, int stateid) {
		super();
		this.cityid = cityid;
		this.cityname = cityname;
		this.stateid = stateid;
	}

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public int getStateid() {
		return stateid;
	}

	public void setStateid(int stateid) {
		this.stateid = stateid;
	}

	@Override
	public String toString() {
		return "Cities [cityid=" + cityid + ", cityname=" + cityname + ", stateid=" + stateid + "]";
	}
}
