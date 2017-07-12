package com.peachy.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.PasswordRecovery;
import com.peachy.interfaces.IPasswordRecovery;

@Transactional
@Repository
public class PasswordRecoveryDao implements IPasswordRecovery {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(PasswordRecovery pr) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(pr);
		tx.commit();
		session.disconnect();
	}

	@Override
	public PasswordRecovery retrieve(String token) {
		Session session = session();
		PasswordRecovery pr = (PasswordRecovery) session.createCriteria(PasswordRecovery.class).add(Restrictions.idEq(token)).uniqueResult();
		session.disconnect();
		
		return pr;
	}

	@Override
	public void update(PasswordRecovery pr) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(pr);
		tx.commit();
		session.disconnect();
	}

	@Override
	public void delete(PasswordRecovery pr) {
		Session session = session();
		String hql = "DELETE FROM PasswordRecovery WHERE token = :token";
		Transaction tx = session.beginTransaction();
		session.createQuery(hql).setString("token", pr.getToken()).executeUpdate();
		tx.commit();
		session.disconnect();
	}

}
