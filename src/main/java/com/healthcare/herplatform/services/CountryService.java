package com.healthcare.herplatform.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.herplatform.models.Countries;
import com.healthcare.herplatform.repository.countriesDAO;

//import com.healthcare.herplatform.repository.*;

@Service
public class CountryService {
	@Autowired
	private countriesDAO countriesDAO;
	
	public List<Countries> getCountries() {
		List<Countries> countryList =  (List<Countries>) countriesDAO.findAll();
		return countryList;
	}
}
