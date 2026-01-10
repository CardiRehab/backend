package com.healthcare.herplatform.payloads;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignupRequest {
	@JsonProperty("dateTime")
	@NotNull(message = "Date/Time is required")
	private Date dateTime;
	
	@NotBlank
	@Size(min = 3, max = 25)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(min = 3, max = 120)
	private String password;

	@NotBlank
	@Size(max = 20)
	private String crsplid;
	
	@NotBlank
	@Size(max = 25)
	private String crsplname;
	
	@NotBlank
	@Size(max = 15)
	private String dob;

	@NotBlank
	@Size(max = 5)
	private String age;

	@NotBlank
	@Size(max = 100)
	private String fname;

    //@NotBlank
	@Size(max = 50)
	private String mname;

	@NotBlank
	@Size(max = 100)
	private String lname;

	@NotBlank
	@Size(max = 10)
	private String gender;

	@NotBlank
	@Size(max = 15)
	private String phone;

	@NotBlank
	@Size(max = 100)
	private String country;

	@NotBlank
	@Size(max = 100)
	private String state;

	@NotBlank
	@Size(max = 100)
	private String city;

	@NotBlank
	@Size(max = 250)
	private String address;
	
	@NotBlank
	@Size(max = 10)
	private String accountstatus;
	
	@NotBlank
	@Size(max = 5)
	private String validityperiod;
	
	private Set<String> role;

	
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCrsplid() {
		return crsplid;
	}

	public void setCrsplid(String crsplid) {
		this.crsplid = crsplid;
	}

	public String getCrsplname() {
		return crsplname;
	}

	public void setCrsplname(String crsplname) {
		this.crsplname = crsplname;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getValidityperiod() {
		return validityperiod;
	}

	public void setValidityperiod(String validityperiod) {
		this.validityperiod = validityperiod;
	}

	public Set<String> getRole() {
		return this.role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "SignupRequest [dateTime=" + dateTime + ", username=" + username + ", email=" + email + ", password="
				+ password + ", crsplid=" + crsplid + ", crsplname=" + crsplname + ", dob=" + dob + ", age=" + age
				+ ", fname=" + fname + ", mname=" + mname + ", lname=" + lname + ", gender=" + gender + ", phone="
				+ phone + ", country=" + country + ", state=" + state + ", city=" + city + ", address=" + address
				+ ", accountstatus=" + accountstatus + ", validityperiod=" + validityperiod + ", role=" + role + "]";
	}
}
