package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.WeekDay;

@Repository
public interface WeekDayRepository extends JpaRepository<WeekDay, Integer> {
	@Query(value="Select un.id, un.week_name_id, un.week_day from week_day un where un.week_name_id=?1",nativeQuery=true) // Native query or SQL
	public List<WeekDay> getWeekDaysByWeekNameId(int weekNameId);
}


