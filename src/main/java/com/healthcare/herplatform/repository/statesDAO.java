package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.herplatform.models.States;

public interface statesDAO extends JpaRepository<States, Integer> {
	public List<States> findByCountryid(int countryid);
}
