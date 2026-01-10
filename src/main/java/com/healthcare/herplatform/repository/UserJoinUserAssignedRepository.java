package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.UserJoinUserAssigned;

@Repository
public interface UserJoinUserAssignedRepository extends JpaRepository<UserJoinUserAssigned, Integer> {

}


