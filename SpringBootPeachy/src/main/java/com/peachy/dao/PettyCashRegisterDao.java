package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.PettyCashRegister;
import com.peachy.interfaces.IPettyCashRegister;

@Transactional
@Repository
public class PettyCashRegisterDao implements IPettyCashRegister {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(PettyCashRegister pettyCashRegister) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(pettyCashRegister);
		tx.commit();
		session.disconnect();
	}

	@Override
	public PettyCashRegister retrieve(int registerId) {
		Session session = session();
		PettyCashRegister pcr = (PettyCashRegister) session.createCriteria(PettyCashRegister.class).add(Restrictions.idEq(registerId)).uniqueResult();
		session.disconnect();
		
		return pcr;
	}
	
	@SuppressWarnings("unchecked")
	public List<PettyCashRegister> retrieveList() {
		Session session = session();
		List<PettyCashRegister> pcrList = session.createCriteria(PettyCashRegister.class).list();
		session.disconnect();

		return pcrList;
	}

	@Override
	public void update(PettyCashRegister pettyCashRegister) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(pettyCashRegister);
		tx.commit();
		session.disconnect();
	}

	@Override
	public void delete(PettyCashRegister pettyCashRegister) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(pettyCashRegister);
		tx.commit();
		session.disconnect();
	}

}
