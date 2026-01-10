package com.healthcare.herplatform.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
//import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "phone")})
public class User{
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;

	@Id
	@Column(name = "id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
	
	@Column(name = "date_time", updatable = true, insertable = true)
	Date dateTime;

	@NotBlank
	@Size(max = 25)
	private String username;

	@NotBlank
	@Size(max = 50)
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	// extended
	@NotBlank
	@Size(max = 15)
	private String dob;

	@NotBlank
	@Size(max = 5)
	private String age;

	@NotBlank
	@Size(max = 100)
	private String fname;

	// @NotBlank
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
	@Size(max = 10)
	private String validityperiod;

	@NotBlank
	@Size(max = 30)
	private String regid;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinTable(name = "user_assmt", 
//	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
//    inverseJoinColumns = {@JoinColumn(name = "assmt_id", referencedColumnName = "id")})
//	private Set<AssignedUsers> user_assignment = new HashSet<>();
	
	public User() {
	}
	
	public User(Date dateTime, @NotBlank @Size(max = 25) String username, @NotBlank @Size(max = 50) String email,
			@NotBlank @Size(max = 120) String password, @NotBlank @Size(max = 15) String dob,
			@NotBlank @Size(max = 5) String age, @NotBlank @Size(max = 100) String fname, @Size(max = 50) String mname,
			@NotBlank @Size(max = 100) String lname, @NotBlank @Size(max = 10) String gender,
			@NotBlank @Size(max = 15) String phone, @NotBlank @Size(max = 100) String country,
			@NotBlank @Size(max = 100) String state, @NotBlank @Size(max = 100) String city,
			@NotBlank @Size(max = 250) String address, @NotBlank @Size(max = 10) String accountstatus,
			@NotBlank @Size(max = 10) String validityperiod, @NotBlank @Size(max = 30) String regid) {
		super();
		this.dateTime = dateTime;
		this.username = username;
		this.email = email;
		this.password = password;
		this.dob = dob;
		this.age = age;
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
		this.gender = gender;
		this.phone = phone;
		this.country = country;
		this.state = state;
		this.city = city;
		this.address = address;
		this.accountstatus = accountstatus;
		this.validityperiod = validityperiod;
		this.regid = regid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getRegid() {
		return regid;
	}

	public void setRegid(String regid) {
		this.regid = regid;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
//	public Set<AssignedUsers> getUser_assignment() {
//		return user_assignment;
//	}
//
//	public void setUser_assignment(Set<AssignedUsers> user_assignment) {
//		this.user_assignment = user_assignment;
//	}

	@Override
	public String toString() {
		return "User [id=" + id + ", dateTime=" + dateTime + ", username=" + username + ", email=" + email
				+ ", password=" + password + ", dob=" + dob + ", age=" + age + ", fname=" + fname + ", mname=" + mname
				+ ", lname=" + lname + ", gender=" + gender + ", phone=" + phone + ", country=" + country + ", state="
				+ state + ", city=" + city + ", address=" + address + ", accountstatus=" + accountstatus
				+ ", validityperiod=" + validityperiod + ", regid=" + regid + ", roles=" + roles + "]";
	}
	
	
}
