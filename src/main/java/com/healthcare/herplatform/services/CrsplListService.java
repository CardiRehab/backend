package com.healthcare.herplatform.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.repository.UserRepository;
import com.healthcare.herplatform.entity.ERole;

@Service
public class CrsplListService {
	@Autowired
	private UserRepository userRepo;
	
	public List<User> getCrsplList(ERole role) {
		List<User> crsplList =  (List<User>) userRepo.findByRoles_Name(role); // Simple and correct
		return crsplList;
	}
}


