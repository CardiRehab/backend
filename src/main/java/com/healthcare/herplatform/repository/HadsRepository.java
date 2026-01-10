package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Hads;

@Repository
public interface HadsRepository extends JpaRepository<Hads, Integer> {
	@Query(value="Select un.id, un.userid, un.date_time, un.anx1, un.anx2, un.anx3, un.anx4, un.anx5, un.anx6, un.anx7, un.dep1, un.dep2, un.dep3, un.dep4, un.dep5, un.dep6, un.dep7, un.total_anx_score, un.total_dep_score from hads un where un.userid=?1",nativeQuery=true) // Native query or SQL
	public List<Hads> getHadsFormDataByUserId(int userId);	
}


