package com.healthcare.herplatform.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.herplatform.entity.AssignedUsers;
import com.healthcare.herplatform.services.AssignedUsersService;
       
//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"}, allowCredentials="true" , maxAge = 3600 )
@RestController
@RequestMapping("/assignedusers")
public class AssignedUsersController {
    private AssignedUsersService assignedUsersService;
   
    public AssignedUsersController(AssignedUsersService assignedUsersService) {
        this.assignedUsersService = assignedUsersService;
    }
    
    @PreAuthorize("hasAnyRole('CRSPL', 'LHCP')")
    @GetMapping("/getconalluserrecords/{userId}")
	public List<AssignedUsers> getUsersById(@PathVariable("userId") int userId) throws Exception {
		List<AssignedUsers> assignedUsersList =  assignedUsersService.getAssignedUsersById(userId)  ;
		return assignedUsersList;
	}  
    
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/getconcrspluserrecords/{userId}")
	public List<AssignedUsers> getCrsplUsersById(@PathVariable("userId") int userId) throws Exception {
		List<AssignedUsers> assignedCrsplUsersList =  assignedUsersService.getAssignedCrsplUsersById(userId)  ;
		return assignedCrsplUsersList;
	} 
}
