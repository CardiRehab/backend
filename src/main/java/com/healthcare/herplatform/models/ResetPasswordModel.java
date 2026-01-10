package com.healthcare.herplatform.models;
import javax.validation.constraints.NotBlank;

public class ResetPasswordModel {
	@NotBlank
	private String regid;
	
	@NotBlank
    private String newPassword;

	@NotBlank
	private String confirmPassword;

	public String getRegid() {
		return regid;
	}

	public void setRegid(String regid) {
		this.regid = regid;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "ResetPasswordModel [regid=" + regid + ", newPassword=" + newPassword + ", confirmPassword="
				+ confirmPassword + "]";
	}
}




