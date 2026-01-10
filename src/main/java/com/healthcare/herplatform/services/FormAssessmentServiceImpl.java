package com.healthcare.herplatform.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthcare.herplatform.entity.AlcoholTobacco;
import com.healthcare.herplatform.entity.Gad7;
import com.healthcare.herplatform.entity.Hads;
import com.healthcare.herplatform.entity.Phq9;
import com.healthcare.herplatform.entity.Smwt;
import com.healthcare.herplatform.repository.Gad7Repository;
import com.healthcare.herplatform.repository.Phq9Repository;
import com.healthcare.herplatform.repository.HadsRepository;
import com.healthcare.herplatform.repository.AlcoholTobaccoRepository;
import com.healthcare.herplatform.repository.SmwtRepository;

@Service
public class FormAssessmentServiceImpl implements FormAssessmentService {
	@Autowired
	private Phq9Repository phq9Repo;
	@Autowired
	private Gad7Repository gad7Repo;
	@Autowired
	private HadsRepository hadsRepo;
	@Autowired
	private AlcoholTobaccoRepository alcoholTobaccoRepo;
	@Autowired
	private SmwtRepository smwtRepo;
	
	public FormAssessmentServiceImpl(Phq9Repository phq9Repo, Gad7Repository gad7Repo, HadsRepository hadsRepo,
			AlcoholTobaccoRepository alcoholTobaccoRepo, SmwtRepository smwtRepo) {
		super();
		this.phq9Repo = phq9Repo;
		this.gad7Repo = gad7Repo;
		this.hadsRepo = hadsRepo;
		this.alcoholTobaccoRepo = alcoholTobaccoRepo;
		this.smwtRepo = smwtRepo;
	}

	@Override
	public Phq9 savePhq9Data(Phq9 phq9Data) throws Exception {
		return phq9Repo.save(phq9Data);
	}
	
	@Override
	public List<Phq9> getPhq9FormDataByUserId(int userId) throws Exception {
		List<Phq9> phq9FormDataList =  (List<Phq9>) phq9Repo.getPhq9FormDataByUserId(userId);
		return phq9FormDataList;
	}
	
	@Override
	public Gad7 saveGad7Data(Gad7 gad7Data) throws Exception {
		return gad7Repo.save(gad7Data);
	}
	
	@Override
	public List<Gad7> getGad7FormDataByUserId(int userId) throws Exception {
		List<Gad7> gad7FormDataList =  (List<Gad7>) gad7Repo.getGad7FormDataByUserId(userId);
		return gad7FormDataList;
	}

	@Override
	public Hads saveHadsData(Hads hadsData) throws Exception {
		return hadsRepo.save(hadsData);
	}

	@Override
	public List<Hads> getHadsFormDataByUserId(int userId) throws Exception {
		List<Hads> hadsFormDataList =  (List<Hads>) hadsRepo.getHadsFormDataByUserId(userId);
		return hadsFormDataList;
	}

	@Override
	public AlcoholTobacco saveAlcoholTobaccoData(AlcoholTobacco alcoholTobaccoData) throws Exception {
		return alcoholTobaccoRepo.save(alcoholTobaccoData);
	}

	@Override
	public List<AlcoholTobacco> getAlcoholTobaccoFormDataByUserId(int userId) throws Exception {
		List<AlcoholTobacco> alcoholTobaccoFormDataList =  (List<AlcoholTobacco>) alcoholTobaccoRepo.getAlcoholTobaccoFormDataByUserId(userId);
		return alcoholTobaccoFormDataList;
	}
	
	@Override
	public Smwt saveSmwtData(Smwt smwtData) throws Exception {
		return smwtRepo.save(smwtData);
	}

	@Override
	public List<Smwt> getSmwtFormDataByUserId(int userId) throws Exception {
		List<Smwt> smwtFormDataList = (List<Smwt>) smwtRepo.getSmwtFormDataByUserId(userId);
		return smwtFormDataList;
	}
}


















