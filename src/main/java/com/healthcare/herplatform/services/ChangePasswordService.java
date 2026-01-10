package com.healthcare.herplatform.services;

public interface ChangePasswordService {
	String changePasswordByUsername(String username, String oldpass, String newpass) throws Exception;
	String changePasswordByRegistrationId(String regid, String newpass) throws Exception;
}

