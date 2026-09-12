package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.herplatform.entity.Contactus;
@Repository
public interface ContactusRepository extends JpaRepository<Contactus, Long> {

	// The form's email is typed by hand, so match case-insensitively.
	void deleteByEmailIgnoreCase(String email);
}

