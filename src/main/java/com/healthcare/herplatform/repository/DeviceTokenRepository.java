package com.healthcare.herplatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.herplatform.entity.DeviceToken;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

	Optional<DeviceToken> findByToken(String token);

	List<DeviceToken> findByUsername(String username);

	@Transactional
	void deleteByToken(String token);

	@Transactional
	void deleteByUsername(String username);
}
