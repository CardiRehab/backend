package com.healthcare.herplatform.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Editable profile fields only. Username, password, roles, registration id, and
 * account status are not accepted here.
 */
public class UpdateProfileRequest {

	@NotBlank(message = "First name is required")
	@Size(max = 100)
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(max = 100)
	private String lastName;

	@NotBlank(message = "Phone is required")
	@Size(max = 15)
	private String phone;

	@NotBlank(message = "Gender is required")
	@Size(max = 10)
	private String gender;

	@Size(max = 250)
	private String address;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
