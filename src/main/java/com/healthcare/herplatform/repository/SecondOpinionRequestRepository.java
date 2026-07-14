package com.healthcare.herplatform.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.herplatform.entity.SecondOpinionRequest;

@Repository
public interface SecondOpinionRequestRepository extends JpaRepository<SecondOpinionRequest, Long> {

	List<SecondOpinionRequest> findByPatientUsernameOrderByCreatedAtDesc(String patientUsername);

	List<SecondOpinionRequest> findByPatientUsernameInOrderByCreatedAtDesc(Collection<String> patientUsernames);

	List<SecondOpinionRequest> findAllByOrderByCreatedAtDesc();
}
