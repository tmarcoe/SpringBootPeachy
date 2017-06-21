package com.peachy.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.GeneralLedger;
import com.peachy.interfaces.IGeneralLedger;

@Transactional
@Repository
public class GeneralLedgerDao implements IGeneralLedger {

	@Autowired
	SessionFactory sessionFactory;
	
	Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(GeneralLedger generalLedger) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(generalLedger);
		tx.commit();
	}

	@Override
	public GeneralLedger retrieve(int entry_num) {
		Session session = session();
		return (GeneralLedger) session.createCriteria(GeneralLedger.class).add(Restrictions.idEq(entry_num)).uniqueResult();
	}

	@Override
	public void update(GeneralLedger generalLedger) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(generalLedger);
		tx.commit();
	}

	@Override
	public void delete(GeneralLedger generalLedger) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(generalLedger);
		tx.commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<GeneralLedger> getList(Date Begin, Date End) {
		Session session = session();
		
		return session.createCriteria(GeneralLedger.class).add(Restrictions.between("entryDate", Begin, End)).list();
	}

}
