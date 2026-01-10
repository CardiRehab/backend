package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.AlcoholTobacco;

@Repository
public interface AlcoholTobaccoRepository extends JpaRepository<AlcoholTobacco, Integer> {
	@Query(value="Select un.id, un.userid, un.date_time, un.smoke, un.alc_screen1, un.alc_screen2, un.alc_screen3, un.alc_full_screen4, un.alc_full_screen5, un.alc_full_screen6, un.alc_full_screen7, un.alc_full_screen8, un.alc_full_screen9, un.alc_full_screen10, un.total_alcohol_screen, un.total_full_screen, un.grand_total_score from alcohol_tobacco un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<AlcoholTobacco> getAlcoholTobaccoFormDataByUserId(int userId);	
}
