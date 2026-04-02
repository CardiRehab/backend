package com.healthcare.herplatform.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.HashSet;
import java.util.List;
/* For sending email */
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.healthcare.herplatform.entity.AssignedUsers;
import com.healthcare.herplatform.entity.Contactus;
import com.healthcare.herplatform.entity.ERole;
import com.healthcare.herplatform.entity.Role;
import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.entity.UserJoinUserAssigned;
import com.healthcare.herplatform.jwt.JwtUtils;
import com.healthcare.herplatform.models.ChangePasswordModel;
import com.healthcare.herplatform.models.ResetPasswordModel;
import com.healthcare.herplatform.payloads.ContactusRequest;
import com.healthcare.herplatform.payloads.JwtResponse;
import com.healthcare.herplatform.payloads.LoginRequest;
import com.healthcare.herplatform.payloads.MessageResponse;
import com.healthcare.herplatform.payloads.SignupRequest;
import com.healthcare.herplatform.repository.ContactusRepository;
import com.healthcare.herplatform.repository.RoleRepository;
import com.healthcare.herplatform.repository.UserRepository;
import com.healthcare.herplatform.services.AssignedUsersService;
import com.healthcare.herplatform.services.UserDetailsImpl;
import com.healthcare.herplatform.services.ChangePasswordService;
import com.healthcare.herplatform.services.CrsplListService;
import com.healthcare.herplatform.jwt.JwtTokenResponse;
import com.healthcare.herplatform.config.SecureFilesConfig;

//@CrossOrigin(origins = { "https://mbzjku.csb.app", "https://www.cardirehab.com", "https://cardirehab.com",
//		"https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595",
//		"http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000",
//		"http://localhost:3002" }, allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")

public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	ContactusRepository contactusRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	private CrsplListService crsplListService;

	@Autowired
	private ChangePasswordService changePasswordService;

	@Autowired
	private AssignedUsersService assignedUsersService;

	@Autowired
	private SecureFilesConfig secureFilesConfig; // <-- just inject here

	private Path SECURE_BASE_DIR;

	@PostConstruct
	public void init() {
		String basePath = secureFilesConfig.getBasePath();
		this.SECURE_BASE_DIR = Paths.get(basePath).toAbsolutePath().normalize();
		//System.out.println("\nSecure Base Directory Loaded = " + this.SECURE_BASE_DIR);
	}

	@Value("${jwt.secret}")
	private String secureFilesBaseURL;
//	@ResponseBody
//	@RequestMapping("/accessDenied")
//    public String sayHello() {
//    	return "Access error denied!";
//    }

	@ResponseBody
	@RequestMapping("/error")
	public String sayError() {
		return "Error: Access error 404 denied!";
	}

	@GetMapping("/greet/{yourName}")
	public String greet(@PathVariable("yourName") String yourName) {
		return "Good Evening " + yourName;
	}

	@GetMapping("/crspllist")
	public List<User> getCrsplList() {
		List<User> crsplList = crsplListService.getCrsplList(ERole.ROLE_CRSPL);
		return crsplList;
	}

	@PostMapping("/signin") // Login request
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		ResponseEntity<?> resEntity = null;

		// Check for status here and return the ResposeEntity as accordingly
		switch (userDetails.getAccountstatus()) {
		case "Active":
			resEntity = ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
					userDetails.getEmail(), userDetails.getAccountstatus(), userDetails.getValidityperiod(), roles,
					userDetails.getFirstName(), userDetails.getLastName()));
			break;
		case "Pending":
			resEntity = ResponseEntity.badRequest().body(new MessageResponse(
					"Your account is pending for Approval. Please, contact the site administrator if it is more than 24 hours since you created your account."));
			break;
		case "Renewal":
			resEntity = ResponseEntity.badRequest().body(new MessageResponse(
					"You account is expired and is pending for Renewal. Please, renew the account immediately to further avail our services."));
			break;
		case "Disabled":
			resEntity = ResponseEntity.badRequest().body(new MessageResponse(
					"Your account is disabled. Please, contact the administrator using our Contact Us Form."));
			break;
		case "Locked":
			resEntity = ResponseEntity.badRequest().body(new MessageResponse(
					"Your account is temporarily locked. Please, contact the administrator using our Contact Us Form."));
			break;
		default:
			resEntity = ResponseEntity.badRequest().body(new MessageResponse(
					"There is some issue with your account. Please, contact the administrator using our Contact Us Form."));
		}

		return resEntity;
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
		try {
			token = token.replace("Bearer ", ""); // Remove "Bearer " prefix
			if (jwtUtils.validateJwtToken(token)) {
				String username = jwtUtils.getUserNameFromJwtToken(token);
				String newToken = jwtUtils.generateTokenFromUsername(username);
				return ResponseEntity.ok(new JwtTokenResponse(newToken));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token refresh failed");
		}
	}

	/* To sign up or user registration inside the database */
	@PostMapping("/signup") // Registration request
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws Exception {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email Id is already in use!"));
		}
		if (userRepository.existsByPhone(signUpRequest.getPhone())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use!"));
		}

		// Setting the middle name to NA if blank
		if (signUpRequest.getMname() == "" || signUpRequest.getMname() == null) {
			signUpRequest.setMname("NA");
		}

		// Generate registration id
		String registrationId = ((signUpRequest.getRole().toArray()[0].toString()).substring(0, 3).toUpperCase())
				+ ((signUpRequest.getFname().toUpperCase()).charAt(0))
				+ ((signUpRequest.getLname().toUpperCase()).charAt(0)) + signUpRequest.getAge()
				+ signUpRequest.getCountry() + signUpRequest.getState() + "TC" + signUpRequest.getCity();

		User user = new User(signUpRequest.getDateTime(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getDob(), signUpRequest.getAge(),
				signUpRequest.getFname(), signUpRequest.getMname(), signUpRequest.getLname(), signUpRequest.getGender(),
				signUpRequest.getPhone(), signUpRequest.getCountry(), signUpRequest.getState(), signUpRequest.getCity(),
				signUpRequest.getAddress(), signUpRequest.getAccountstatus(), signUpRequest.getValidityperiod(),
				registrationId); // Transferring
									// data from
									// Payload/Model
									// to the
									// entity
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				case "patient":
					Role patientRole = roleRepository.findByName(ERole.ROLE_PATIENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(patientRole);
					break;
				case "crspl":
					// System.out.println("\nEntered the Block, successfully \n");
					Role crsplRole = roleRepository.findByName(ERole.ROLE_CRSPL)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(crsplRole);
					// System.out.println("\nRole is added successfully: " + crsplRole + "\n");
					break;
				case "lhcp":
					Role lhcpRole = roleRepository.findByName(ERole.ROLE_LHCP)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(lhcpRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);

//		System.out.println("\nRegistration Id: " + registrationId + "\n");
//		System.out.println("\n==================================");
//		System.out.println(user);
//		System.out.println("==================================\n");

		/* User registered to the database */
		User responseUser = userRepository.save(user);

		/* User (if in patient role) needs to be assigned to a CRSPL */
		strRoles.forEach(role -> {
			if (role.equals("patient")) {
				/* Assign the user to some CR specialist */
				// Get the data from user or postman
				/*
				 * AssignedUsers assignedUsersdata = new AssignedUsers(8, responseUser.getId(),
				 * "\nRelesha", responseUser.getUsername());
				 */

//				AssignedUsers assignedUsersdata = new AssignedUsers(1, responseUser.getId(), registrationId, "Cardirehab",
//						responseUser.getEmail(), responseUser.getPhone(), responseUser.getGender(),
//						responseUser.getAge(), responseUser.getUsername()); // NOTE: Use Id "1" and CRSPL Name as "\nCardirehab" here

				AssignedUsers assignedUsersdata = new AssignedUsers(Integer.parseInt(signUpRequest.getCrsplid()),
						responseUser.getId(), registrationId, signUpRequest.getCrsplname(), responseUser.getEmail(),
						responseUser.getPhone(), responseUser.getGender(), responseUser.getAge(),
						responseUser.getUsername()); // NOTE: Use Id "1" and CRSPL Name as "\nCardirehab" here

				// System.out.println("\nPayload/Model data: " + signUpRequest + "\n\n");

				try {
					// Save data passed by user by using the service
					AssignedUsers saveData = assignedUsersService.saveNewUserData(assignedUsersdata);

					// Get the data for JOIN table
					UserJoinUserAssigned userJoinUserAssignedData = new UserJoinUserAssigned(responseUser.getId(),
							saveData.getId());

					// Save User Join User Assigned data
					// UserJoinUserAssigned saveJoinData =
					assignedUsersService.saveUserJoinUserAssignedData(userJoinUserAssignedData);

//					System.out.println("\n\n==================================");
//					System.out.println("Join table id generated and stored is: " + saveData.getId());
//					System.out.println("\n\n==================================");
				} catch (Exception e) {
					System.out.println("\nMessage is: " + e.getMessage() + "\n");
					e.printStackTrace();
				}
			}
		});

		return ResponseEntity.ok(new MessageResponse("User registered successfully! Registered user id is: "
				+ registrationId + ". Please, remember and keep it safe with you for further reference."));
	}

	/* To update the assigned password of a particular user based on user name */
	@PutMapping("/changepassword")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordModel cpModel) throws Exception {
		if ((cpModel.getNewPassword().trim()).equals(cpModel.getConfirmPassword().trim())) {
			if ((cpModel.getNewPassword().trim()).equals(cpModel.getOldPassword().trim())) {
				return ResponseEntity.ok(new MessageResponse("New password and Old password are identical!"));
			} else {
				/* Let the control move forward from here */
			}
		} else {
			return ResponseEntity.ok(new MessageResponse("New password and Confirm password does not match!"));
		}

//		System.out.println("\n\n==================================");
//		System.out.println(cpModel);
//		System.out.println("\n\n==================================");

		/*
		 * Encode new password and update the old password with the new encoded password
		 * in db by unique registration id
		 */
		String retMsg = changePasswordService.changePasswordByUsername((cpModel.getUsername().trim()),
				(cpModel.getOldPassword().trim()), (encoder.encode(cpModel.getNewPassword().trim())));

		return ResponseEntity.ok(new MessageResponse(retMsg));

	}

	/*
	 * To reset the assigned password of a particular user based on registration id
	 */
	@PutMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordModel rpModel) throws Exception {
		if ((rpModel.getNewPassword().trim()).equals(rpModel.getConfirmPassword().trim())) {
			/* Let the control move forward from here */
		} else {
			return ResponseEntity.ok(new MessageResponse("New password and Confirm password does not match!"));
		}

//		System.out.println("\n\n==================================");
//		System.out.println(rpModel);
//		System.out.println("\n\n==================================");

		/*
		 * Encode new password and update the old password with the new encoded password
		 * in db by unique registration id
		 */
		String retMsg = changePasswordService.changePasswordByRegistrationId((rpModel.getRegid().trim()),
				(encoder.encode(rpModel.getNewPassword().trim())));

		return ResponseEntity.ok(new MessageResponse(retMsg));

	}

	@PostMapping("/contactus")
	public ResponseEntity<?> contactUser(@Valid @RequestBody ContactusRequest contactusRequest) throws Exception {

		/* Code to get the date in reverse order without any delimiter */
//		SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
//	    String formattedDate = (ft.format(new Date()).toString()); 

		// Generate Token Id
		int randomNo = (int) new java.util.Random().nextInt();
		String tokenId = (String.valueOf((contactusRequest.getName().toUpperCase()).charAt(0))
				+ String.valueOf((contactusRequest.getName().toUpperCase()).charAt(1)) + Integer.toString(randomNo));

		// Create new Contact us request
		Contactus contactus = new Contactus(tokenId, contactusRequest.getDateTime(), contactusRequest.getName(),
				contactusRequest.getEmail(), contactusRequest.getPhone(), contactusRequest.getSubject(),
				contactusRequest.getMessage()); // Transferring
		// data from
		// Payload /
		// Model to
		// the
		// entity
		// object
		// (contactus)

//		System.out.println("\n\nToken Id: " + tokenId + "\n\n");
//		System.out.println("\n\n==================================");
//		System.out.println(contactus);
//		System.out.println("\n\n==================================");

		/* Recording to the database */
		contactusRepository.save(contactus);

		/* Sending an email */
		String status = sendEmail(contactusRequest.getName(), contactusRequest.getEmail(), contactusRequest.getPhone(),
				contactusRequest.getSubject(), contactusRequest.getMessage(), tokenId);

		if (status == "OK") {
			return ResponseEntity.ok(new MessageResponse(
					"Request submitted successfully! Token id is: " + tokenId + ". We will shortly contact you."));
		} else {
			return ResponseEntity.ok(new MessageResponse(
					"Mail is not Received but message is recorded in database successfully! Token id is: " + tokenId
							+ ". We will shortly contact you."));
		}
	}

	public String sendEmail(String name, String email, String phone, String subject, String messageData,
			String tokenId) {
		// Recipient's email ID needs to be mentioned.
		// String to = "anupamchauhan888@gmail.com";

		// Sender's email ID needs to be mentioned
//		String from = "anupamchauhan888@gmail.com";
//		String password = "xjhzecaxnapptryo";
		String from = "sprt.chs@gmail.com";
		String password = "vpnnoqyrnlalkrce";

		// Assuming you are sending email from localhost
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.debug", "true");
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.user", from);
		properties.put("mail.smtp.password", password);
		properties.put("mail.smtp.port", 587);
		properties.put("mail.smtp.ssl.trust", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			try {
				message.setFrom(new InternetAddress(from));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Set To: header field of the header.
			// Address[] toEmailAddr = {(Address)new
			// InternetAddress("ayushi8472@gmail.com"), (Address)new
			// InternetAddress("awdhesh.kumar645@gmail.com"), (Address)new
			// InternetAddress("sniteshin@gmail.com")};
			Address[] toEmailAddr = { (Address) new InternetAddress("jeet.rana@gmail.com"),
					(Address) new InternetAddress("anupamchauhan888@gmail.com"),
					(Address) new InternetAddress("sudhirrathore@hotmail.com"),
					(Address) new InternetAddress("sprt.chs@gmail.com"),
					(Address) new InternetAddress("deepak_rathore@hotmail.com") };
			message.addRecipients(Message.RecipientType.TO, toEmailAddr);

			// Set Subject: header field
			message.setSubject(subject);

			String composeEmailData = "\n Name: " + name + "\n Email Id: " + email + "\n Phone: " + phone
					+ "\n\n Message: " + messageData + "\n Token Id:  " + tokenId;

			// Now set the actual composed message
			message.setText(composeEmailData);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
			return "OK";
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return "NO";
		}
	}

	// Secure base folder (OUTSIDE webroot)
	// private static final Path SECURE_BASE_DIR =
	// Paths.get("/secure_files/files/").toAbsolutePath().normalize();
	// private static final Path SECURE_BASE_DIR =
	// Paths.get("secure_files/files").toAbsolutePath().normalize();
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("/files/{filename}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename, Authentication auth) throws IOException {

		// 1.Check if user is authenticated
		if (auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// 2️.Check role-based access (customize your logic)
		if (!hasAccess(auth, filename)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		// 3️.Prevent directory traversal (e.g. "../../etc/passwd")
		if (filename.contains("..")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		// 4️.Resolve and normalize path
		Path filePath = SECURE_BASE_DIR.resolve(filename).normalize();
		System.out.println("\nSECURE PATH → " + filePath + "\n");
		// Ensure file is inside secure base directory
		if (!filePath.startsWith(SECURE_BASE_DIR)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		// 5️.Load resource
		Resource resource;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		if (!resource.exists()) {
			return ResponseEntity.notFound().build();
		}

		// 6️.Detect MIME type dynamically
		String contentType = detectContentType(filePath);

		// 7️.Build and return response
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/**
	 * Custom access control — adjust per your app logic.
	 */
	private boolean hasAccess(Authentication auth, String filename) {
		// String role = auth.getAuthorities().toString();

		// Allow users with ROLE_PATIENT to access any file
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
			return true;
		}

		// Allow users with ROLE_CRSPL to access any file
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CRSPL"))) {
			return true;
		}

		// Allow users with ROLE_LHCP to access any file
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_LHCP"))) {
			return true;
		}

		// Allow users with ROLE_ADMIN to access any file
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			return true;
		}

		return false; // default allow
	}

	/**
	 * Safely determine the MIME type of a file. Falls back to manual mappings if
	 * Files.probeContentType() fails.
	 */
	private String detectContentType(Path filePath) {
		String contentType = null;

		try {
			contentType = Files.probeContentType(filePath);
		} catch (IOException e) {
			// ignore and fallback below
		}

		if (contentType == null) {
			String fileName = filePath.getFileName().toString().toLowerCase();

			if (fileName.endsWith(".pdf"))
				contentType = "application/pdf";
			else if (fileName.endsWith(".mp4"))
				contentType = "video/mp4";
			else if (fileName.endsWith(".doc"))
				contentType = "application/msword";
			else if (fileName.endsWith(".docx"))
				contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			else if (fileName.endsWith(".xls"))
				contentType = "application/vnd.ms-excel";
			else if (fileName.endsWith(".xlsx"))
				contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			else if (fileName.endsWith(".ppt"))
				contentType = "application/vnd.ms-powerpoint";
			else if (fileName.endsWith(".pptx"))
				contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
			else if (fileName.endsWith(".csv"))
				contentType = "text/csv";
			else if (fileName.endsWith(".json"))
				contentType = "application/json";
			else if (fileName.endsWith(".js"))
				contentType = "application/javascript";
			else if (fileName.endsWith(".txt"))
				contentType = "text/plain";
			else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
				contentType = "image/jpeg";
			else if (fileName.endsWith(".png"))
				contentType = "image/png";
			else if (fileName.endsWith(".gif"))
				contentType = "image/gif";
			else if (fileName.endsWith(".svg"))
				contentType = "image/svg+xml";
			else if (fileName.endsWith(".zip"))
				contentType = "application/zip";
			else if (fileName.endsWith(".rar"))
				contentType = "application/vnd.rar";
			else if (fileName.endsWith(".7z"))
				contentType = "application/x-7z-compressed";
			else if (fileName.endsWith(".xml"))
				contentType = "application/xml";
			else if (fileName.endsWith(".html") || fileName.endsWith(".htm"))
				contentType = "text/html";
			else
				contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // safe fallback
		}

		return contentType;
	}
}
