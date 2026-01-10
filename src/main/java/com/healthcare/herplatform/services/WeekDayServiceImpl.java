package com.healthcare.herplatform.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.healthcare.herplatform.entity.WeekDay;
import com.healthcare.herplatform.repository.WeekDayRepository;

@Service
public class WeekDayServiceImpl implements WeekDayService {
	private  WeekDayRepository wDRepository;
	
	public WeekDayServiceImpl(WeekDayRepository wDRepository) {
        this.wDRepository = wDRepository;
    }
	
	@Override
	public List<WeekDay> getWeekDaysByWeekNameId(int weekNameId) throws Exception {
		List<WeekDay> weekDayList =  (List<WeekDay>) wDRepository.getWeekDaysByWeekNameId(weekNameId) ;
		return weekDayList;
	}
}




