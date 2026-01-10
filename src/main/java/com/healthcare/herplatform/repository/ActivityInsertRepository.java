package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Activities;

@Repository
public interface ActivityInsertRepository extends JpaRepository<Activities, Integer> {

}

