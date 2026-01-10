package com.healthcare.herplatform.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthcare.herplatform.entity.Activities;
import com.healthcare.herplatform.entity.ActivitiesType;
import com.healthcare.herplatform.entity.AssignedActivities;
import com.healthcare.herplatform.entity.OthersActivities;
import com.healthcare.herplatform.repository.ActivitiesRepository;
import com.healthcare.herplatform.repository.OtherActivitiesRepository;
import com.healthcare.herplatform.repository.ActivitiesTypeRepository;
import com.healthcare.herplatform.repository.AssignedActivitiesRepository;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {
	@Autowired
	private ActivitiesTypeRepository activitiesTypeRepository;
	@Autowired
	private ActivitiesRepository actRepository;
	@Autowired
	private OtherActivitiesRepository otherActRepository;
	@Autowired
	private AssignedActivitiesRepository assignedActRepository;
	
	public ActivitiesServiceImpl(ActivitiesRepository actRepository) {
        this.actRepository = actRepository;
    }
	
	@Override
	public List<Activities> getActivitiesByWeekDayId(int weekDayId) throws Exception {
		List<Activities> activitiesList =  (List<Activities>) actRepository.getActivitiesByWeekDayId(weekDayId);
		return activitiesList;
	}
	
	@Override
	public List<Activities> getActivitiesByUserId(int userId) throws Exception {
		List<Activities> activitiesList =  (List<Activities>) actRepository.getActivitiesByUserId(userId);
		return activitiesList;
	}
	
	@Override
	public List<OthersActivities> getOtherActivitiesByUserId(int userId) throws Exception {
		List<OthersActivities> otActivitiesList =  (List<OthersActivities>) otherActRepository.getOtherActivitiesByUserId(userId);
		return otActivitiesList;
	}
	
	@Override
	public List<ActivitiesType> getActivitiesType() {
		List<ActivitiesType> activitiesTypeList =  (List<ActivitiesType>) activitiesTypeRepository.findAll();
		return activitiesTypeList;
	}
	
	@Override
	public List<AssignedActivities> saveAssignedActivities(List<AssignedActivities> assignedActivities) {
		return assignedActRepository.saveAll(assignedActivities);
	}
	
	@Override
	public List<AssignedActivities> getAssignedActivitiesByUserId(int userId) throws Exception {
		List<AssignedActivities> assignedActivitiesList =  (List<AssignedActivities>) assignedActRepository.getAssignedActivitiesByUserId(userId);
		return assignedActivitiesList;
	}
	
	@Override
	public List<AssignedActivities> getAssignedActivitiesByUserIdAndWorkStatus(int userId, String wStatus) throws Exception {
		List<AssignedActivities> assignedActivitiesList =  (List<AssignedActivities>) assignedActRepository.getAssignedActivitiesByUserIdAndWorkStatus(userId, wStatus);
		return assignedActivitiesList;
	}
	
	@Override
	public AssignedActivities updateAssignedActivityById(AssignedActivities assignedActivityRequest, int id) throws Exception {
		AssignedActivities existingActivity = null;
		try {
	       existingActivity = assignedActRepository.findById(id).get(); //Get from database by id
		}catch(Exception e) {
		   existingActivity = assignedActivityRequest; //Get from database by id
		}
		//Update with new values
		existingActivity.setActWorkStatus(assignedActivityRequest.getActWorkStatus()); // work status
		existingActivity.setWorkStatusChangedDT(assignedActivityRequest.getWorkStatusChangedDT()); // work status changed Date Time
		//Save the updated values in Database
		AssignedActivities assignedActivity =  (AssignedActivities) assignedActRepository.save(existingActivity);
		return assignedActivity;
	}
}


















