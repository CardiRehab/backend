package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.User;
import java.util.Optional;

@Repository
public interface ChangePasswordRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	Optional<User> findByRegid(String regid);
	Optional<User> findByEmail(String email);
	Optional<User> findByPhone(String phone);
}

