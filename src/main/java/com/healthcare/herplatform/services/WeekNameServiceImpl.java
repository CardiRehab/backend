package com.healthcare.herplatform.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.healthcare.herplatform.entity.WeekName;
import com.healthcare.herplatform.repository.WeekNameRepository;

@Service
public class WeekNameServiceImpl implements WeekNameService {
	private  WeekNameRepository wNRepository;
	
	public WeekNameServiceImpl(WeekNameRepository wNRepository) {
        this.wNRepository = wNRepository;
    }
	
	@Override
	public List<WeekName> getWeekNamesByUserId(int userId) throws Exception {
		List<WeekName> weekNameList =  (List<WeekName>) wNRepository.getWeekNamesByUserId(userId) ;
		return weekNameList;
	}
}





