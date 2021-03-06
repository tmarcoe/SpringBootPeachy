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
		session.disconnect();
	}

	@Override
	public Survey retrieve(int key) {
		Session session = session();
		Survey s = (Survey) session.createCriteria(Survey.class).add(Restrictions.idEq(key)).uniqueResult();
		session.disconnect();
		
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Survey> retrieveList() {
		Session session = session();
		List<Survey> sList = session.createCriteria(Survey.class).list();
		session.disconnect();
		
		return sList;
	}

	@Override
	public void update(Survey survey) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(survey);
		tx.commit();
		session.disconnect();
	}

	@Override
	public void delete(Survey survey) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(survey);
		tx.commit();
		session.disconnect();
	}


	public List<BigDecimal> getSurveyReport() {
		Session session = session();
		BigInteger bigInteger;
		List<BigDecimal> answers = new ArrayList<BigDecimal>();
		String hql1 = "SELECT sum(question3) FROM survey";
		String hql2 = "SELECT sum(question5) FROM survey";
		String hql3 = "SELECT sum(question7) FROM survey";
		String hql4 = "SELECT count(*) FROM survey";

		answers.add((BigDecimal) session.createSQLQuery(hql1).uniqueResult());
		answers.add((BigDecimal) session.createSQLQuery(hql2).uniqueResult());
		answers.add((BigDecimal) session.createSQLQuery(hql3).uniqueResult());
		bigInteger = (BigInteger) session.createSQLQuery(hql4).uniqueResult();
		answers.add((BigDecimal) BigDecimal.valueOf(bigInteger.longValue()));
		session.disconnect();
		
		return answers;
	}

}
