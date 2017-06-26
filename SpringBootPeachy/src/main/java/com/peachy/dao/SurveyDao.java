package com.peachy.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.Survey;
import com.peachy.interfaces.ISurvey;

@Transactional
@Repository
public class SurveyDao implements ISurvey {
	
	@Autowired
	SessionFactory sessionFactory;
	
	Session session() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void create(Survey survey) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(survey);
		tx.commit();
	}

	@Override
	public Survey retrieve(int key) {
		Session session = session();
		return (Survey) session.createCriteria(Survey.class).add(Restrictions.idEq(key)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Survey> retrieveList() {
		
		Session session = session();
		return session.createCriteria(Survey.class).list();
	}

	@Override
	public void update(Survey survey) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(survey);
		tx.commit();
	}

	@Override
	public void delete(Survey survey) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(survey);
		tx.commit();
	}


	public List<BigDecimal> getSurveyReport() {
		Session session = session();
		BigInteger bigInteger;
		List<BigDecimal> answers = new ArrayList<BigDecimal>();
		String hql1 = "SELECT sum(question3) FROM Survey";
		String hql2 = "SELECT sum(question5) FROM Survey";
		String hql3 = "SELECT sum(question7) FROM Survey";
		String hql4 = "SELECT count(*) FROM Survey";

		answers.add((BigDecimal) session.createSQLQuery(hql1).uniqueResult());
		answers.add((BigDecimal) session.createSQLQuery(hql2).uniqueResult());
		answers.add((BigDecimal) session.createSQLQuery(hql3).uniqueResult());
		bigInteger = (BigInteger) session.createSQLQuery(hql4).uniqueResult();
		answers.add((BigDecimal) BigDecimal.valueOf(bigInteger.longValue()));
		
		return answers;
	}

}
