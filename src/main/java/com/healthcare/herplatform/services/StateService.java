package com.healthcare.herplatform.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.herplatform.models.States;
import com.healthcare.herplatform.repository.statesDAO;

//import com.healthcare.herplatform.repository.*;

@Service
public class StateService {
	@Autowired
	private statesDAO statesDAO;
	
	public List<States> getStatesByCountryId(int countryId) {
		List<States> stateList =  (List<States>) statesDAO.findByCountryid(countryId);
		return stateList;
	}
}
