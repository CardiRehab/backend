package com.healthcare.herplatform.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.herplatform.models.Cities;
import com.healthcare.herplatform.repository.citiesDAO;

//import com.healthcare.herplatform.repository.*;

@Service
public class CityService {
	@Autowired
	private citiesDAO citiesDAO;
	
	public List<Cities> getCitiesByStateId(int stateId) {
		List<Cities> cityList =  (List<Cities>) citiesDAO.findByStateid(stateId);
		return cityList;
	}


}
