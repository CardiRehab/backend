package com.healthcare.herplatform.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="states")
public class States {
	@Id
	@Column(name = "id", updatable = false, insertable = false)
    int id;
	
	@Column(name = "name", updatable = false, insertable = false)
    String name;
	
	@Column(name = "country_id", updatable = false, insertable = false)
    int countryid;
	
	
	public States() {
	}


	public States(int id, String name, int countryid) {
		super();
		this.id = id;
		this.name = name;
		this.countryid = countryid;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getCountryid() {
		return countryid;
	}


	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}


	@Override
	public String toString() {
		return "States [id=" + id + ", name=" + name + ", countryid=" + countryid + "]";
	}
}
