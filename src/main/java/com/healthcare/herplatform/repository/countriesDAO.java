package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.herplatform.models.Countries;

public interface countriesDAO extends JpaRepository<Countries, Integer> {
	
}
