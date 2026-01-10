package com.healthcare.herplatform.services;

import java.util.List;
import com.healthcare.herplatform.entity.Phq9;
import com.healthcare.herplatform.entity.Gad7;
import com.healthcare.herplatform.entity.Hads;
import com.healthcare.herplatform.entity.AlcoholTobacco;
import com.healthcare.herplatform.entity.Smwt;

public interface FormAssessmentService {
	/* PHQ9 Form - Read and Write */
	Phq9 savePhq9Data(Phq9 phq9Data) throws Exception;
	List<Phq9> getPhq9FormDataByUserId(int userId) throws Exception;
	
	/* GAD7 Form - Read and Write */
	Gad7 saveGad7Data(Gad7 gad7Data) throws Exception;	
	List<Gad7> getGad7FormDataByUserId(int userId) throws Exception;
	
	/* HADS Form - Read and Write */
	Hads saveHadsData(Hads hadsData) throws Exception;
	List<Hads> getHadsFormDataByUserId(int userId) throws Exception;
	
	/* Alcohol and Tobacco Screening Form - Read and Write */
	AlcoholTobacco saveAlcoholTobaccoData(AlcoholTobacco alcoholTobaccoData) throws Exception;
	List<AlcoholTobacco> getAlcoholTobaccoFormDataByUserId(int userId) throws Exception;
	
	/* SMWT Form - Read and Write */
	Smwt saveSmwtData(Smwt smwtData) throws Exception;
	List<Smwt> getSmwtFormDataByUserId(int userId) throws Exception;
}
