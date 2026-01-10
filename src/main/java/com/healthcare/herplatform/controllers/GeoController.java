package com.healthcare.herplatform.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.healthcare.herplatform.models.Cities;
import com.healthcare.herplatform.models.Countries;
import com.healthcare.herplatform.models.States;
import com.healthcare.herplatform.services.CityService;
import com.healthcare.herplatform.services.CountryService;
import com.healthcare.herplatform.services.StateService;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"} , allowCredentials="true" , maxAge = 3600)

@RestController
@RequestMapping("/api/geo")
public class GeoController {
	@Autowired
	private CountryService countryService;
	@Autowired
	private StateService stateService;
	@Autowired
	private CityService cityService;
	
	@GetMapping("/country")
	public List<Countries> getCountry() {
		List<Countries> countryList =  countryService.getCountries();
		return countryList;
	}

	@GetMapping("/state/{countryId}")
	public List<States> getState(@PathVariable("countryId") int countryId) {
		List<States> stateList =  stateService.getStatesByCountryId(countryId);
		return stateList;
	}
	
	@GetMapping("/city/{stateId}")
	public List<Cities> getCity(@PathVariable("stateId") int stateId) {
		List<Cities> cityList =  cityService.getCitiesByStateId(stateId);
		return cityList;
	}
}
