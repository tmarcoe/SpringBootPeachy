package com.peachy.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.peachy.dao.SurveyDao;
import com.peachy.entity.Survey;
import com.peachy.interfaces.ISurvey;

@Service
public class SurveyService implements ISurvey {

	@Autowired
	SurveyDao surveyDao;

	@Override
	public void create(Survey survey) {
		surveyDao.create(survey);
	}

	@Override
	public Survey retrieve(int key) {
		return surveyDao.retrieve(key);
	}

	public PagedListHolder<Survey> retrieveList() {
		return new PagedListHolder<Survey>(surveyDao.retrieveList());
	}
	
	@Override
	public void update(Survey survey) {
		surveyDao.update(survey);
	}

	@Override
	public void delete(Survey survey) {
		surveyDao.delete(survey);
	}


	public List<Double> getSurveyReport() {

		List<Double> averages = new ArrayList<Double>();
		List<BigDecimal> sums = surveyDao.getSurveyReport();

		Iterator<BigDecimal> it = sums.iterator();

		Double satisfied = Double.valueOf(String.valueOf(it.next()));
		Double navigation = Double.valueOf(String.valueOf(it.next()));
		Double prices = Double.valueOf(String.valueOf(it.next()));
		Double numOfPoints = Double.valueOf(String.valueOf(it.next()));

		averages.add((satisfied + numOfPoints) / numOfPoints);
		averages.add((navigation + numOfPoints) / numOfPoints);
		averages.add((prices + numOfPoints) / numOfPoints);

		return averages;

	}

}
