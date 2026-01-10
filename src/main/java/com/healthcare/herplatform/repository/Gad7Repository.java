package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Gad7;

@Repository
public interface Gad7Repository extends JpaRepository<Gad7, Integer> {
	@Query(value="Select un.id, un.userid, un.date_time, un.gad1, un.gad2, un.gad3, un.gad4, un.gad5, un.gad6, un.gad7, un.diff_level, un.total_score from gad7 un where un.userid=?1",nativeQuery=true) // Native query or SQL
    public List<Gad7> getGad7FormDataByUserId(int userId);
}
