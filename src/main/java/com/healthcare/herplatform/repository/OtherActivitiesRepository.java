package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.OthersActivities;

@Repository
public interface OtherActivitiesRepository extends JpaRepository<OthersActivities, Integer> {
	@Query(value="Select un.id, un.userid, un.date_time, un.activity_name, un.val_quantity from othersactivities un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<OthersActivities> getOtherActivitiesByUserId(int userId);	
}
