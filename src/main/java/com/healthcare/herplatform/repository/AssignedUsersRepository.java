package com.healthcare.herplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.AssignedUsers;

@Repository
public interface AssignedUsersRepository extends JpaRepository<AssignedUsers, Integer>{
	//@Query(value="Select un.assignedusers from assigned_chat_users un where un.username=?1") //JPQL or HQL
	//@Query(value="Select un.id, un.userId, un.userName, un.assigneduserid, un.assignedusers from user_assignment un where un.userId=?1",nativeQuery=true) // Native query or SQL
	
	/* Native query or SQL with JOINS */
	@Query(value="Select * from users INNER JOIN user_assmt ON users.id = user_assmt.user_id INNER JOIN user_assignment ON user_assmt.assmt_id = user_assignment.id where user_assignment.userid=?1",nativeQuery=true) 
	public List<AssignedUsers> getUsersById(int userId);
	
	/* Native query or SQL with JOINS */
	//@Query(value="Select * from users INNER JOIN user_assmt ON users.id = user_assmt.user_id INNER JOIN user_assignment ON user_assmt.assmt_id = user_assignment.id where user_assignment.assigneduserid=?1",nativeQuery=true) 
	@Query(value="Select * from user_assignment where user_assignment.assigneduserid=?1",nativeQuery=true)
	public List<AssignedUsers> getCrsplUsersById(int userId);
}

