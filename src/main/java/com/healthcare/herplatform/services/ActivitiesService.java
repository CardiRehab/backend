package com.healthcare.herplatform.services;

import java.util.List;
import com.healthcare.herplatform.entity.Activities;
import com.healthcare.herplatform.entity.ActivitiesType;
import com.healthcare.herplatform.entity.AssignedActivities;
import com.healthcare.herplatform.entity.OthersActivities;

public interface ActivitiesService {
	List<Activities> getActivitiesByWeekDayId(int weekDayId) throws Exception;
	List<Activities> getActivitiesByUserId(int userId) throws Exception;
	List<OthersActivities> getOtherActivitiesByUserId(int userId) throws Exception;
	List<ActivitiesType> getActivitiesType() throws Exception;
	List<AssignedActivities> saveAssignedActivities(List<AssignedActivities> assActivities) throws Exception;
	List<AssignedActivities> getAssignedActivitiesByUserId(int userId) throws Exception;
	List<AssignedActivities> getAssignedActivitiesByUserIdAndWorkStatus(int userId, String wStatus) throws Exception;
	AssignedActivities updateAssignedActivityById(AssignedActivities assignedActivityRequest,int id) throws Exception;
}

