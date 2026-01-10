package com.healthcare.herplatform.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="countries")
public class Countries {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
    int countryId;
	
	@Column(name = "name", updatable = false, insertable = false)
	String countryName;
	
	public Countries() {
	}
	public Countries(int countryId, String countryName) {
		super();
		this.countryId = countryId;
		this.countryName = countryName;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	@Override
	public String toString() {
		return "Countries [countryId=" + countryId + ", countryName=" + countryName + "]";
	}
}
