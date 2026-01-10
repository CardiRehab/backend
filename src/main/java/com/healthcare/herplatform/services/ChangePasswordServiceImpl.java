package com.healthcare.herplatform.services;

import com.healthcare.herplatform.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthcare.herplatform.repository.ChangePasswordRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService {
	@Autowired
	private ChangePasswordRepository changePasswordRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public ChangePasswordServiceImpl(ChangePasswordRepository changePasswordRepository) {
		super();
		this.changePasswordRepository = changePasswordRepository;
	}
	
	@Override
	public String changePasswordByUsername(String username, String oldpass, String newpass) throws Exception {
		User existingUser = null;
		try {
			existingUser = changePasswordRepository.findByUsername(username).orElseThrow(() -> new Exception("User not found with username: " + username)); //Get from database by regid
		}catch(Exception e) {
			return "User not found with username: " + username;
		}
		
		//Getting stored encoded password
		String storedEncodedPassword = existingUser.getPassword();
		
		//Update with new values if raw password matches with stored encoded password
		if(checkPassword(oldpass, storedEncodedPassword)) {
		   existingUser.setPassword(newpass);
		}else {
		   return "Old password entered is invalid.Please try again";	
		}
		
		//Save the updated values in Database
		changePasswordRepository.save(existingUser);
		
		return "Password is updated for the username: " + username;
	}
	
	public boolean checkPassword(String rawPassword, String storedEncodedPassword) {
	    return passwordEncoder.matches(rawPassword, storedEncodedPassword);
	}
	
	@Override
	public String changePasswordByRegistrationId(String regid, String newpass) throws Exception {
		User existingUser = null;
		try {
			existingUser = changePasswordRepository.findByRegid(regid).orElseThrow(() -> new Exception("User not found with registration id: " + regid)); //Get from database by registration id
		}catch(Exception e) {
			return "User not found with registration id: " + regid;
		}
		
		//Update the old password with the new values ss
		existingUser.setPassword(newpass);
		
		//Save the updated values in Database
		changePasswordRepository.save(existingUser);
		
		return "Password is Reset for the Registration Id: " + regid;
	}
}
