package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Activities;

@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Integer>{
	@Query(value="Select un.id, un.userid, un.week_day_id, un.date_time, un.activity_name, un.pre_hr, un.post_hr, un.results, un.rpe_borg, un.symptoms from activities un where un.week_day_id=?1",nativeQuery=true) // Native query or SQL
	public List<Activities> getActivitiesByWeekDayId(int weekDayId);
	
	@Query(value="Select un.id, un.userid, un.date_time, un.activity_name, un.pre_hr, un.post_hr, un.results, un.rpe_borg, un.symptoms from activities un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<Activities> getActivitiesByUserId(int userId);	
}

