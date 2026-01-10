package com.healthcare.herplatform.services;

import java.util.List;
import com.healthcare.herplatform.entity.WeekDay;

public interface WeekDayService {
	List<WeekDay> getWeekDaysByWeekNameId(int weekNameId) throws Exception;
}
