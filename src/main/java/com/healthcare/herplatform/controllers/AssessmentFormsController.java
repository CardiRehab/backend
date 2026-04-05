package com.healthcare.herplatform.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.healthcare.herplatform.entity.Phq9;
import com.healthcare.herplatform.entity.Gad7;
import com.healthcare.herplatform.entity.Hads;
import com.healthcare.herplatform.entity.AlcoholTobacco;
import com.healthcare.herplatform.entity.Smwt;
import com.healthcare.herplatform.models.Phq9InsertModel;
import com.healthcare.herplatform.models.Gad7InsertModel;
import com.healthcare.herplatform.models.HadsInsertModel;
import com.healthcare.herplatform.models.AlcoholTobaccoInsertModel;
import com.healthcare.herplatform.models.SmwtInsertModel;
import com.healthcare.herplatform.services.FormAssessmentService;
import com.healthcare.herplatform.services.UserDetailsImpl;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"}, allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/assessmentforms")
public class AssessmentFormsController {
	
	private FormAssessmentService formAssessmentService;

	public AssessmentFormsController(FormAssessmentService formAssessmentService) {
		super();
		this.formAssessmentService = formAssessmentService;
	}

	/**
	 * CRSPL/LHCP may read any patient's forms. PATIENT may only read their own row
	 * ({@code userId} must match the authenticated user id).
	 */
	private void assertFormReadAccess(Authentication authentication, int userId) {
		boolean isPatient = authentication.getAuthorities().stream()
				.anyMatch(a -> "ROLE_PATIENT".equals(a.getAuthority()));
		if (!isPatient) {
			return;
		}
		UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
		if (user.getId().intValue() != userId) {
			throw new AccessDeniedException("Patients may only access their own assessment form data");
		}
	}
	
	/*
	 * =================================== PHQ9 Write ==========================================
	 * 
	 */
	/* To insert the PHQ9 form data in database */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@PostMapping("/insertphq9")
	public ResponseEntity<?> insertPHQ9(@Valid @RequestBody Phq9InsertModel phq9Model) throws Exception {

		// Calculate the total Score
		int totScore = (Integer.parseInt(phq9Model.getPhq1()) + Integer.parseInt(phq9Model.getPhq2())
				+ Integer.parseInt(phq9Model.getPhq3()) + Integer.parseInt(phq9Model.getPhq4())
				+ Integer.parseInt(phq9Model.getPhq5()) + Integer.parseInt(phq9Model.getPhq6())
				+ Integer.parseInt(phq9Model.getPhq7()) + Integer.parseInt(phq9Model.getPhq8())
				+ Integer.parseInt(phq9Model.getPhq9()));

		// Get the data from user or postman
		Phq9 phq9Data = new Phq9(phq9Model.getUserid(), phq9Model.getDateTime(), phq9Model.getPhq1(),
				phq9Model.getPhq2(), phq9Model.getPhq3(), phq9Model.getPhq4(), phq9Model.getPhq5(), phq9Model.getPhq6(),
				phq9Model.getPhq7(), phq9Model.getPhq8(), phq9Model.getPhq9(), phq9Model.getDiffLevel(),
				Integer.toString(totScore) /* Setting the total score to an Entity */
		);

//		System.out.println("\n\n==================================");
//		System.out.println(phq9Data);
//		System.out.println("\n\n==================================");

		// Save data passed by user by using the service
		Phq9 saveData = formAssessmentService.savePhq9Data(phq9Data);

		return new ResponseEntity<Phq9>(saveData, HttpStatus.OK);
	}
	/*
	 * =================================== PHQ9 Read ==========================================
	 * 
	 */

	/* To get all the PHQ9 form data of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@GetMapping("/getphq9formdatabyuid/{userId}")
	public List<Phq9> getPhq9FormDataByUserId(@PathVariable("userId") int userId, Authentication authentication)
			throws Exception {
		assertFormReadAccess(authentication, userId);
		List<Phq9> phq9FormDataList = formAssessmentService.getPhq9FormDataByUserId(userId);
		return phq9FormDataList;
	}
	/*
	 * ==================================== GAD7 Write =========================================
	 * 
	 */

	/* To insert the GAD7 form data in database */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@PostMapping("/insertgad7")
	public ResponseEntity<?> insertGAD7(@Valid @RequestBody Gad7InsertModel gad7Model) throws Exception {

		// Calculate the total Score
		int totScore = (Integer.parseInt(gad7Model.getGad1()) + Integer.parseInt(gad7Model.getGad2())
				+ Integer.parseInt(gad7Model.getGad3()) + Integer.parseInt(gad7Model.getGad4())
				+ Integer.parseInt(gad7Model.getGad5()) + Integer.parseInt(gad7Model.getGad6())
				+ Integer.parseInt(gad7Model.getGad7()));

		// Get the data from user or postman
		Gad7 gad7Data = new Gad7(gad7Model.getUserid(), gad7Model.getDateTime(), gad7Model.getGad1(),
				gad7Model.getGad2(), gad7Model.getGad3(), gad7Model.getGad4(), gad7Model.getGad5(), gad7Model.getGad6(),
				gad7Model.getGad7(), gad7Model.getDiffLevel(),
				Integer.toString(totScore) /* Setting the total score to an Entity */
		);

//		System.out.println("\n\n==================================");
//		System.out.println(gad7Data);
//		System.out.println("\n\n==================================");

		// Save data passed by user by using the service
		Gad7 saveData = formAssessmentService.saveGad7Data(gad7Data);

		return new ResponseEntity<Gad7>(saveData, HttpStatus.OK);
	}
	/*
	 * ====================================== GAD7 Read =======================================
	 * 
	 */

	/* To get all the Gad7 form data of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@GetMapping("/getgad7formdatabyuid/{userId}")
	public List<Gad7> getGad7FormDataByUserId(@PathVariable("userId") int userId, Authentication authentication)
			throws Exception {
		assertFormReadAccess(authentication, userId);
		List<Gad7> gad7FormDataList = formAssessmentService.getGad7FormDataByUserId(userId);
		return gad7FormDataList;
	}
	/*
	 * ====================================== HADS Write =======================================
	 * 
	 */
	/* To insert the HADS form data in database */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@PostMapping("/inserthads")
	public ResponseEntity<?> insertHADS(@Valid @RequestBody HadsInsertModel hadsModel) throws Exception {

		// Calculate the total Anxiety Score
		int totAnxScore = (Integer.parseInt(hadsModel.getAnx1()) + Integer.parseInt(hadsModel.getAnx2())
				+ Integer.parseInt(hadsModel.getAnx3()) + Integer.parseInt(hadsModel.getAnx4())
				+ Integer.parseInt(hadsModel.getAnx5()) + Integer.parseInt(hadsModel.getAnx6())
				+ Integer.parseInt(hadsModel.getAnx7()));
		
		// Calculate the total Depression Score
		int totDepScore = (Integer.parseInt(hadsModel.getDep1()) + Integer.parseInt(hadsModel.getDep2())
				+ Integer.parseInt(hadsModel.getDep3()) + Integer.parseInt(hadsModel.getDep4())
				+ Integer.parseInt(hadsModel.getDep5()) + Integer.parseInt(hadsModel.getDep6())
				+ Integer.parseInt(hadsModel.getDep7()));

		// Get the data from user or postman
		Hads hadsData = new Hads(hadsModel.getUserid(), hadsModel.getDateTime(), hadsModel.getAnx1(),
				hadsModel.getAnx2(), hadsModel.getAnx3(), hadsModel.getAnx4(), hadsModel.getAnx5(), hadsModel.getAnx6(),
				hadsModel.getAnx7(), hadsModel.getDep1(), hadsModel.getDep2(), hadsModel.getDep3(), hadsModel.getDep4(),
				hadsModel.getDep5(), hadsModel.getDep6(), hadsModel.getDep7(), 
				Integer.toString(totAnxScore), /* Setting the total anxiety score to an Entity */
				Integer.toString(totDepScore) /* Setting the total depression score to an Entity */
		);

//		System.out.println("\n\n==================================");
//		System.out.println(hadsData);
//		System.out.println("\n\n==================================");

		// Save data passed by user by using the service
		Hads saveData = formAssessmentService.saveHadsData(hadsData);

		return new ResponseEntity<Hads>(saveData, HttpStatus.OK);
	}
	/*
	 * ====================================== HADS Read =======================================
	 * 
	 */

	/* To get all the Hads form data of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@GetMapping("/gethadsformdatabyuid/{userId}")
	public List<Hads> getHadsFormDataByUserId(@PathVariable("userId") int userId, Authentication authentication)
			throws Exception {
		assertFormReadAccess(authentication, userId);
		List<Hads> hadsFormDataList = formAssessmentService.getHadsFormDataByUserId(userId);
		return hadsFormDataList;
	}
	/*
	 * ====================================== AlcoholTobacco Write =======================================
	 * 
	 */
	/* To insert the AlcoholTobacco form data in database */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@PostMapping("/insertalcoholtobacco")
	public ResponseEntity<?> insertAlcoholTobacco(@Valid @RequestBody AlcoholTobaccoInsertModel alcoholTobaccoModel) throws Exception {

		// Calculate the total alcohol screening Score
		int totalAlcoholScreenScore = (Integer.parseInt(alcoholTobaccoModel.getAlcScreen1()) + Integer.parseInt(alcoholTobaccoModel.getAlcScreen2())
				+ Integer.parseInt(alcoholTobaccoModel.getAlcScreen3()));
		
		// Calculate the total alcohol full screening Score
		int totalAlcoholFullScreenScore = (Integer.parseInt(alcoholTobaccoModel.getAlcFullScreen4()) + Integer.parseInt(alcoholTobaccoModel.getAlcFullScreen5())
				+ Integer.parseInt(alcoholTobaccoModel.getAlcFullScreen6()) + Integer.parseInt(alcoholTobaccoModel.getAlcFullScreen7())
				+ Integer.parseInt(alcoholTobaccoModel.getAlcFullScreen8()) + Integer.parseInt(alcoholTobaccoModel.getAlcFullScreen9())
				+ Integer.parseInt(alcoholTobaccoModel.getAlcFullScreen10()));
		
		// Calculate the grand total Score
		int grandTotalScore = totalAlcoholScreenScore + totalAlcoholFullScreenScore;

		// Get the data from user or postman
		AlcoholTobacco alcoholTobaccoData = new AlcoholTobacco(alcoholTobaccoModel.getUserid(), alcoholTobaccoModel.getDateTime(), alcoholTobaccoModel.getSmk(),
				alcoholTobaccoModel.getAlcScreen1(), alcoholTobaccoModel.getAlcScreen2(), alcoholTobaccoModel.getAlcScreen3(), alcoholTobaccoModel.getAlcFullScreen4(), 
				alcoholTobaccoModel.getAlcFullScreen5(), alcoholTobaccoModel.getAlcFullScreen6(), alcoholTobaccoModel.getAlcFullScreen7(), alcoholTobaccoModel.getAlcFullScreen8(),
				alcoholTobaccoModel.getAlcFullScreen9(), alcoholTobaccoModel.getAlcFullScreen10(), 
				Integer.toString(totalAlcoholScreenScore), /* Setting the total alcohol screening score to an Entity */
				Integer.toString(totalAlcoholFullScreenScore), /* Setting the total alcohol full screening score to an Entity */
				Integer.toString(grandTotalScore) /* Setting the grand total screening score to an Entity */
		);

//		System.out.println("\n\n==================================");
//		System.out.println(alcoholTobaccoData);
//		System.out.println("\n\n==================================");

		// Save data passed by user by using the service
		AlcoholTobacco saveData = formAssessmentService.saveAlcoholTobaccoData(alcoholTobaccoData);

		return new ResponseEntity<AlcoholTobacco>(saveData, HttpStatus.OK);
	}
	/*
	 * ====================================== AlcoholTobacco Read =======================================
	 * 
	 */

	/* To get all the AlcoholTobacco form data of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@GetMapping("/getalcoholtobaccoformdatabyuid/{userId}")
	public List<AlcoholTobacco> getAlcoholTobaccoFormDataByUserId(@PathVariable("userId") int userId,
			Authentication authentication) throws Exception {
		assertFormReadAccess(authentication, userId);
		List<AlcoholTobacco> alcoholTobaccoFormDataList = formAssessmentService
				.getAlcoholTobaccoFormDataByUserId(userId);
		return alcoholTobaccoFormDataList;
	}
	
	/*
	 * ====================================== SMWT(Six Minute Wslk Test) Write =======================================
	 * 
	 */
	/* To insert the SMWT form data in database */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@PostMapping("/insertsmwt")
	public ResponseEntity<?> insertSMWT(@Valid @RequestBody SmwtInsertModel smwtModel) throws Exception {

		// Calculate the total Distance covered here (if required)
		//int totDistanceCovered = (Integer.parseInt(smwtModel.getNumlaps()) + Integer.parseInt(smwtModel.getFplap()));
		
		// Get the data from user or postman
		Smwt smwtData = new Smwt(smwtModel.getUserid(), smwtModel.getDateTime(), smwtModel.getName(),
				smwtModel.getDob(), smwtModel.getTestdate(), smwtModel.getGender(), smwtModel.getHeight(), smwtModel.getWeight(),
				smwtModel.getPrebloodpressure(), smwtModel.getPrepulserate(), smwtModel.getPreoxylevel(), smwtModel.getPredyspnea(), smwtModel.getPrefatigue(),
				smwtModel.getPostbloodpressure(), smwtModel.getPostpulserate(), smwtModel.getPostoxylevel(),
				smwtModel.getPostdyspnea(), smwtModel.getPostfatigue(), smwtModel.getStoppause(),
				smwtModel.getReason(), smwtModel.getChestpain(), smwtModel.getShortofbreath(),
				smwtModel.getLightheaded(), smwtModel.getPaininlegs(), smwtModel.getOthers(),
				smwtModel.getOthersymptoms(), smwtModel.getNumlaps(), smwtModel.getFplap(), smwtModel.getTotdistance()
				//Integer.toString(totDistanceCovered) /* Setting the total distance covered to an Entity */
		);
		
		System.out.println("\n\n==================================");
		System.out.println(smwtData);
		System.out.println("\n\n==================================");

		// Save data passed by user by using the service
		Smwt saveData = formAssessmentService.saveSmwtData(smwtData);

		return new ResponseEntity<Smwt>(saveData, HttpStatus.OK);
	}
	
	/*
	 * ====================================== SMWT(Six Minute Wslk Test) Read =======================================
	 * 
	 */
	/* To get all the Smwt form data of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@GetMapping("/getsmwtformdatabyuid/{userId}")
	public List<Smwt> getSmwtFormDataByUserId(@PathVariable("userId") int userId, Authentication authentication)
			throws Exception {
		assertFormReadAccess(authentication, userId);
		List<Smwt> smwtFormDataList = formAssessmentService.getSmwtFormDataByUserId(userId);
		return smwtFormDataList;
	}
	
	/*
	 * ============================================== End ================================================
	 * 
	 */
}
