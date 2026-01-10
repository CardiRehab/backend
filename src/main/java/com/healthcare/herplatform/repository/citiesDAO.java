package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.herplatform.models.Cities;

public interface citiesDAO extends JpaRepository<Cities, Integer> {
	public List<Cities> findByStateid(int stateid);
}
