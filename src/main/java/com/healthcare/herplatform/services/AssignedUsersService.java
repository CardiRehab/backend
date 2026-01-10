package com.healthcare.herplatform.services;

import java.util.List;
import com.healthcare.herplatform.entity.AssignedUsers;
import com.healthcare.herplatform.entity.UserJoinUserAssigned;

public interface AssignedUsersService {
	List<AssignedUsers> getAssignedUsersById(int userId) throws Exception;
	List<AssignedUsers> getAssignedCrsplUsersById(int userId) throws Exception;
	AssignedUsers saveNewUserData(AssignedUsers assignNewUserData) throws Exception;
	UserJoinUserAssigned saveUserJoinUserAssignedData(UserJoinUserAssigned userJoinUserAssignedData) throws Exception;
}



