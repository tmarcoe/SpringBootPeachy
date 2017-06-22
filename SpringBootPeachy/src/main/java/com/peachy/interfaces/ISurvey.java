package com.peachy.interfaces;

import com.peachy.entity.Survey;

public interface ISurvey {
	public void create(Survey survey);
	public Survey retrieve(int key);
	public void update(Survey survey);
	public void delete(Survey survey);
}
