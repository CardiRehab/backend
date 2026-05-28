package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.UserJoinUserAssigned;

@Repository
public interface UserJoinUserAssignedRepository extends JpaRepository<UserJoinUserAssigned, Integer> {

	@Modifying
	@Query(value="DELETE FROM user_assmt WHERE user_id=?1", nativeQuery=true)
	void deleteByUserId(Long userId);
}


