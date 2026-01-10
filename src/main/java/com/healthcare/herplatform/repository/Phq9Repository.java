package com.healthcare.herplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Phq9;

@Repository
public interface Phq9Repository extends JpaRepository<Phq9, Integer> {
	@Query(value="Select un.id, un.userid, un.date_time, un.phq1, un.phq2, un.phq3, un.phq4, un.phq5, un.phq6, un.phq7, un.phq8, un.phq9, un.diff_level, un.total_score from phq9 un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<Phq9> getPhq9FormDataByUserId(int userId);	
}
