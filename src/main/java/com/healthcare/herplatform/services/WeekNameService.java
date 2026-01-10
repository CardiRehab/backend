package com.healthcare.herplatform.services;

import java.util.List;
import com.healthcare.herplatform.entity.WeekName;

public interface WeekNameService {
	List<WeekName> getWeekNamesByUserId(int userId) throws Exception;
}





