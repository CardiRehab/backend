package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Smwt;

@Repository
public interface SmwtRepository extends JpaRepository<Smwt, Integer> {
	@Query(value="Select un.id, un.userid, un.date_time, un.name, un.dob, un.testdate, un.gender, un.height, un.weight, un.prebloodpressure, un.prepulserate, un.preoxylevel, un.predyspnea, un.prefatigue, un.postbloodpressure, un.postpulserate, un.postoxylevel, un.postdyspnea, un.postfatigue, un.stoppause, un.reason, un.chestpain, un.shortofbreath, un.lightheaded, un.paininlegs, un.others, un.othersymptoms, un.numlaps, un.fplap, un.totdistance from smwt un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<Smwt> getSmwtFormDataByUserId(int userId);	
}