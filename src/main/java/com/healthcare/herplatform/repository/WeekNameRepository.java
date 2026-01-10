package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.WeekName;

@Repository
public interface WeekNameRepository extends JpaRepository<WeekName, Integer>{
	@Query(value="Select un.id, un.userId, un.week_name from week_name un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<WeekName> getWeekNamesByUserId(int userid);
}



