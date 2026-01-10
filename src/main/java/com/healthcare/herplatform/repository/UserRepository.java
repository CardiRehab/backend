package com.healthcare.herplatform.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.entity.ERole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	List<User> findByRoles_Name(ERole role); // This returns all users having a specific role
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	Boolean existsByPhone(String phone);
	Boolean existsByRegid(String regid);
	Boolean existsByPassword(String password);
}
