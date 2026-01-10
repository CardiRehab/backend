package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.herplatform.entity.AssignedActivities;

@Repository
public interface AssignedActivitiesRepository extends JpaRepository<AssignedActivities, Integer> {
	@Query(value="Select un.id, un.userid, un.acttypeid, un.curr_date_time, un.activities_type, un.act_freq, un.peakhr, un.peak_borgrpe, un.min_duration, un.act_status, un.comments, un.act_work_status, un.work_status_changeddt from assigned_activities un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<AssignedActivities> getAssignedActivitiesByUserId(int userId);	
	
	@Query(value="Select un.id, un.userid, un.acttypeid, un.curr_date_time, un.activities_type, un.act_freq, un.peakhr, un.peak_borgrpe, un.min_duration, un.act_status, un.comments, un.act_work_status, un.work_status_changeddt from assigned_activities un where un.userid=?1 and un.act_work_status=?2",nativeQuery=true) // Native query or SQL
	public List<AssignedActivities> getAssignedActivitiesByUserIdAndWorkStatus(int userId, String wStatus);
	
	@Query(value="Select un.id, un.userid, un.acttypeid, un.curr_date_time, un.activities_type, un.act_freq, un.peakhr, un.peak_borgrpe, un.min_duration, un.act_status, un.comments, un.act_work_status, un.work_status_changeddt from assigned_activities un where un.userid=?1 and un.act_work_status=?2",nativeQuery=true) // Native query or SQL
	public AssignedActivities updAssignedActivityByIdAndWorkStatus(int id, String wStatus);
}
