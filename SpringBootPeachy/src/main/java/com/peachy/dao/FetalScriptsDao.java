package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.FetalScripts;
import com.peachy.interfaces.IFetalScripts;

@Transactional
@Repository
public class FetalScriptsDao implements IFetalScripts {
	
	@Autowired
	SessionFactory sessionFactory;
	
	Session session() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void create(FetalScripts fetalScripts) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(fetalScripts);
		tx.commit();
	}

	@Override
	public FetalScripts retrieve(int id) {
		Session session = session();
		return (FetalScripts) session.createCriteria(FetalScripts.class).add(Restrictions.idEq(id)).uniqueResult();
	}

	public FetalScripts retrieve(String file_name) {
		Session session = session();
		
		return (FetalScripts) session.createCriteria(FetalScripts.class).add(Restrictions.eq("file_name", file_name)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<FetalScripts> retrieveList() {
		Session session = session();
		return session.createCriteria(FetalScripts.class).list();
	}
	
	@Override
	public void update(FetalScripts fetalScripts) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(fetalScripts);
		tx.commit();
	}

	@Override
	public void delete(FetalScripts fetalScripts) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(fetalScripts);
		tx.commit();
	}

}
