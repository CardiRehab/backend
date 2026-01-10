package com.healthcare.herplatform.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.healthcare.herplatform.entity.AssignedUsers;
import com.healthcare.herplatform.entity.UserJoinUserAssigned;
import com.healthcare.herplatform.repository.AssignedUsersRepository;
import com.healthcare.herplatform.repository.UserJoinUserAssignedRepository;
import com.healthcare.herplatform.repository.UserAssignmentRepository;

@Service
public class AssignedUsersServiceImpl implements AssignedUsersService{
	private  AssignedUsersRepository assURepository;
	private UserJoinUserAssignedRepository ujuaRepo;
	private UserAssignmentRepository uaRepository;
	
	public AssignedUsersServiceImpl(AssignedUsersRepository assURepository, UserJoinUserAssignedRepository ujuaRepo,
			UserAssignmentRepository uaRepository) {
		super();
		this.assURepository = assURepository;
		this.ujuaRepo = ujuaRepo;
		this.uaRepository = uaRepository;
	}

	@Override
	public List<AssignedUsers> getAssignedUsersById(int userId) {
		List<AssignedUsers> usersList =  (List<AssignedUsers>) assURepository.getUsersById(userId) ;
		return usersList; 
	}
	
	@Override
	public List<AssignedUsers> getAssignedCrsplUsersById(int userId) {
		List<AssignedUsers> crsplUsersList =  (List<AssignedUsers>) assURepository.getCrsplUsersById(userId) ;
		return crsplUsersList; 
	}

    @Override
	public AssignedUsers saveNewUserData(AssignedUsers assignNewUserData) throws Exception {
		return uaRepository.save(assignNewUserData);
	}
    
    @Override
	public UserJoinUserAssigned saveUserJoinUserAssignedData(UserJoinUserAssigned userJoinUserAssignedData) throws Exception {
		return ujuaRepo.save(userJoinUserAssignedData);
	}
}



