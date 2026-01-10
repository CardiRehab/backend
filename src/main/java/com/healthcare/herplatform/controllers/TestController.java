package com.healthcare.herplatform.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"} , allowCredentials="true" , maxAge = 3600)

@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Dashboard.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_PATIENT') or hasRole('ROLE_CRSPL') or hasRole('ROLE_LHCP')")
	public String userAccess() {
		return "User Profile.";
	}
	@GetMapping("/mod")
	@PreAuthorize("hasRole('ROLE_MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Dashboard.";
	}
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String adminAccess() {
		return "Admin Dashboard.";
	}
	@GetMapping("/patient")
	@PreAuthorize("hasRole('ROLE_PATIENT')")
	public String patientAccess() {
		return "Patient Dashboard.";
	}
	@GetMapping("/crspl")
	@PreAuthorize("hasRole('ROLE_CRSPL')")
	public String crspecialistAccess() {
		return "CR Specialist Dash Board.";
	}
	@GetMapping("/lhcp")
	@PreAuthorize("hasRole('ROLE_LHCP')")
	public String lhcpAccess() {
		return "LHCP Dash Board.";
	}
	@GetMapping("/accessDenied")
	public String error() {
		System.out.println("\nAccess is denied for this resource with the current user role!\n");
		return "access-denied";
	} 
}
