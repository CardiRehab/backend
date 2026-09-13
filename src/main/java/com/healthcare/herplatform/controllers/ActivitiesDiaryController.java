package com.healthcare.herplatform.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.healthcare.herplatform.entity.WeekName;
import com.healthcare.herplatform.models.ActivityInsertModel;
import com.healthcare.herplatform.models.OthersActivityInsertModel;
import com.healthcare.herplatform.repository.ActivityInsertRepository;
import com.healthcare.herplatform.repository.AssignedActivitiesRepository;
import com.healthcare.herplatform.repository.OthersActivityInsertRepository;
import com.healthcare.herplatform.repository.WeekNameRepository;
import com.healthcare.herplatform.security.PatientAccessGuard;
import com.healthcare.herplatform.entity.WeekDay;
import com.healthcare.herplatform.entity.Activities;
import com.healthcare.herplatform.entity.OthersActivities;
import com.healthcare.herplatform.entity.ActivitiesType;
import com.healthcare.herplatform.entity.AssignedActivities;
import com.healthcare.herplatform.services.PushNotificationService;
import com.healthcare.herplatform.services.WeekNameService;
import com.healthcare.herplatform.services.WeekDayService;
import com.healthcare.herplatform.services.ActivitiesService;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595" , "http://localhost:3000", "http://localhost:3002"}, allowCredentials="true" , maxAge = 3600 )
@RestController
@RequestMapping("/activitiesdiary")
public class ActivitiesDiaryController {
	@Autowired
	ActivityInsertRepository aiREPO;
	
	@Autowired
	OthersActivityInsertRepository otAiREPO;
	
	private WeekNameService weekNameService;
	private WeekDayService weekDayService;
	private ActivitiesService activitiesService;

	@Autowired
	private PushNotificationService pushNotificationService;

	// Indirect ownership lookups: these endpoints name a child record, and the patient who
	// owns it has to be resolved through its parent before the guard can be asked.
	@Autowired
	private WeekNameRepository weekNameRepository;

	@Autowired
	private AssignedActivitiesRepository assignedActivitiesRepository;

	@Autowired
	private PatientAccessGuard patientAccessGuard;

	public ActivitiesDiaryController(WeekNameService weekNameService, WeekDayService weekDayService,
			ActivitiesService activitiesService) {
		super();
		this.weekNameService = weekNameService;
		this.weekDayService = weekDayService;
		this.activitiesService = activitiesService;
	}
	
	/* To get all the weeknames based on a particular userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP') and @patientAccessGuard.canAccessPatient(authentication, #userId)")
	@GetMapping("/getuserweeknames/{userId}")
	public List<WeekName> getSingleUserWeekNamesById(@PathVariable("userId") int userId) throws Exception {
		List<WeekName> weekNamesList =  weekNameService.getWeekNamesByUserId(userId);
		return weekNamesList;
	} 
	
	/* To get all the weekdays based on weeknameid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@GetMapping("/getuserweekdays/{weekNameId}")
	public List<WeekDay> getSingleUserWeekDaysById(@PathVariable("weekNameId") int weekNameId,
			Authentication authentication) throws Exception {
		// Indirect IDOR: the caller names a week, not a patient. Resolve the owning patient
		// through week_name, and fail closed when the week does not exist so that a missing id
		// and someone else's id are indistinguishable.
		Optional<WeekName> weekName = weekNameRepository.findById(weekNameId);
		if (!weekName.isPresent()) {
			patientAccessGuard.denyUnresolved(authentication, weekNameId, "weekNameId does not resolve to a patient");
		}
		patientAccessGuard.assertCanAccessPatient(authentication, weekName.get().getUserId());
		List<WeekDay> weekDaysList =  weekDayService.getWeekDaysByWeekNameId(weekNameId);
		return weekDaysList;
	} 
	
//	/* To get all the activities of a particular user based on weekdayid */
//	@GetMapping("/getuseractivities/{weekDayId}")
//	public List<Activities> getSingleUserActivitiesByWeekDayId(@PathVariable("weekDayId") int weekDayId) throws Exception {
//		System.out.println("\n\nEntered 3\n");
//		List<Activities> activitiesList =  activitiesService.getActivitiesByWeekDayId(weekDayId);
//		return activitiesList;
//	} 
	
	/* To get all the activities of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP') and @patientAccessGuard.canAccessPatient(authentication, #userId)")
	@GetMapping("/getuseractivitiesuid/{userId}")
	public List<Activities> getSingleUserActivitiesByUserId(@PathVariable("userId") int userId) throws Exception {
		List<Activities> activitiesList =  activitiesService.getActivitiesByUserId(userId);
		return activitiesList;
	} 
	
	/* To get all the other activities of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP') and @patientAccessGuard.canAccessPatient(authentication, #userId)")
	@GetMapping("/getuserotheractivitiesuid/{userId}")
	public List<OthersActivities> getSingleUserOtherActivitiesByUserId(@PathVariable("userId") int userId) throws Exception {
		List<OthersActivities> otherActivitiesList =  activitiesService.getOtherActivitiesByUserId(userId);
		return otherActivitiesList;
	} 
	
	/* To get all the available activities type */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
	@GetMapping("/getallactivitiestype")
	public List<ActivitiesType> getAllActivitiesType() throws Exception {
		List<ActivitiesType> activitiesTypeList =  activitiesService.getActivitiesType();
		return activitiesTypeList;
	} 
	
	/* To insert the physical activity in database */
	@PreAuthorize("hasRole('PATIENT') and @patientAccessGuard.canAccessPatient(authentication, #aiModel.userid)")
	@PostMapping("/insertactivity")
    public ResponseEntity<?> insertActivity(@Valid @RequestBody ActivityInsertModel aiModel) {
		Activities activityData = new Activities(
				aiModel.getUserid(),
				aiModel.getDtTime(),
				aiModel.getActivityName(),
				aiModel.getPreHR(),
				aiModel.getPostHR(),
				aiModel.getResults(),
				aiModel.getRpeBorg(),
				aiModel.getSymptoms()
				);
		Activities saveActivity = aiREPO.save(activityData);

		Map<String, String> data = new HashMap<>();
		data.put("type", "activity");
		data.put("activityType", "physical");
		data.put("patientUserId", Integer.toString(aiModel.getUserid()));
		pushNotificationService.sendToAssignedDoctorsOf(
				aiModel.getUserid(),
				"Activity logged",
				"Patient logged " + aiModel.getActivityName() + " (RPE " + aiModel.getRpeBorg() + ")",
				data);

        return new ResponseEntity<Activities>(saveActivity, HttpStatus.OK);
    }

	/* To insert the others activity in database */
	@PreAuthorize("hasRole('PATIENT') and @patientAccessGuard.canAccessPatient(authentication, #otAiModel.userid)")
	@PostMapping("/insertotactivity")
    public ResponseEntity<?> insertOtActivity(@Valid @RequestBody OthersActivityInsertModel otAiModel) {
		OthersActivities othersActivityData = new OthersActivities(
				otAiModel.getUserid(),
				otAiModel.getDtTime(),
				otAiModel.getActivityName(),
				otAiModel.getValQuantity()
				);
		OthersActivities saveOtActivity = otAiREPO.save(othersActivityData);

		Map<String, String> data = new HashMap<>();
		data.put("type", "activity");
		data.put("activityType", "other");
		data.put("patientUserId", Integer.toString(otAiModel.getUserid()));
		pushNotificationService.sendToAssignedDoctorsOf(
				otAiModel.getUserid(),
				"Activity logged",
				"Patient logged " + otAiModel.getActivityName() + " (" + otAiModel.getValQuantity() + ")",
				data);

        return new ResponseEntity<OthersActivities>(saveOtActivity, HttpStatus.OK);
    }
	
	/* To insert all the assigned activities/ activities plan inside the database */
	@PreAuthorize("hasAnyRole('CRSPL', 'LHCP')")
	@PostMapping("/insertassignedactivities")
    public List<AssignedActivities> insertAssignedActivities(@Valid @RequestBody List <AssignedActivities> assignedActivities,
			Authentication authentication) throws Exception{

		// Each element names its own patient, so every one of them is checked. One unassigned
		// patient refuses the whole batch rather than writing part of a plan: a clinician who
		// saw a success for a half-written plan would have no way to tell which half landed.
		patientAccessGuard.assertCanAccessAllPatients(authentication, assignedActivities == null
				? null
				: assignedActivities.stream().map(AssignedActivities::getUserid).collect(Collectors.toList()));

		System.out.println("\n\n==================================");
		//assignedActivities.forEach(System.out::println);
		assignedActivities.forEach((s) -> System.out.println(s));
		System.out.println("\n\n==================================");

		List<AssignedActivities> insertActivitiesList =  activitiesService.saveAssignedActivities(assignedActivities);

		// One push per distinct patient — a plan typically has multiple activity
		// rows for the same patient, and a single notification reads better than
		// N back-to-back alerts.
		Set<Integer> notifiedPatients = new HashSet<>();
		for (AssignedActivities a : insertActivitiesList) {
			int patientUserId = a.getUserid();
			if (!notifiedPatients.add(patientUserId)) {
				continue;
			}
			Map<String, String> data = new HashMap<>();
			data.put("type", "plan");
			data.put("patientUserId", Integer.toString(patientUserId));
			pushNotificationService.sendToUserId(patientUserId,
					"New activity plan",
					"Your doctor has updated your activity plan.",
					data);
		}
        return insertActivitiesList;
    }
	
	/* To get all the assigned activities of a particular user based on userid */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP') and @patientAccessGuard.canAccessPatient(authentication, #userId)")
	@GetMapping("/getassignedactivitiesuid/{userId}")
	public List<AssignedActivities> getSingleUserAssignedActivitiesByUserId(@PathVariable("userId") int userId) throws Exception {
		List<AssignedActivities> assignedActivitiesList =  activitiesService.getAssignedActivitiesByUserId(userId);
		return assignedActivitiesList;
	} 
	
	/* To get all the assigned activities of a particular user based on userid and activity work status */
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP') and @patientAccessGuard.canAccessPatient(authentication, #userId)")
	@GetMapping("/getassignedactivitiesuid/{userId}/{wStatus}")
	public List<AssignedActivities> getSingleUserAssignedActivitiesByUserIdAndWorkStatus(@PathVariable("userId") int userId, @PathVariable("wStatus") String wStatus) throws Exception {
		List<AssignedActivities> assignedActivitiesList =  activitiesService.getAssignedActivitiesByUserIdAndWorkStatus(userId, wStatus);
		return assignedActivitiesList;
	} 
	
	
	/* To update or delete the assigned activities of a particular user based on id(pk) and assign the given activity work status (updated or deleted) */
	@PreAuthorize("hasAnyRole('CRSPL', 'LHCP')")
	@PutMapping("/updAssignedActivity/{id}")
	public AssignedActivities updateAssignedActivityById(@RequestBody AssignedActivities assignedActivity, @PathVariable("id") int id,
			Authentication authentication) throws Exception {

		// Indirect IDOR: the owning patient comes from the stored row, never from the request
		// body, which the caller controls. A missing row is a denial, not an insert.
		Optional<AssignedActivities> existing = assignedActivitiesRepository.findById(id);
		if (!existing.isPresent()) {
			patientAccessGuard.denyUnresolved(authentication, id, "assigned activity id does not resolve to a patient");
		}
		patientAccessGuard.assertCanAccessPatient(authentication, existing.get().getUserid());
		
//		System.out.println("\n\n==================================");
//		System.out.println(assignedActivity);
//		System.out.println("\n\n==================================");
		
		AssignedActivities updateAssignedActivities =  activitiesService.updateAssignedActivityById(assignedActivity, id);
		return updateAssignedActivities;
	} 
}











