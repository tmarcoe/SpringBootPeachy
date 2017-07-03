package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.TimeSheetAccounts;
import com.peachy.interfaces.ITimeSheetAccounts;

@Transactional
@Repository
public class TimeSheetAccountsDao implements ITimeSheetAccounts {
	
	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void create(TimeSheetAccounts timeSheetAccounts) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(timeSheetAccounts);
		tx.commit();
	}

	@Override
	public TimeSheetAccounts retrieve(int entry_id) {
		Session session = session();
		return (TimeSheetAccounts) session.createCriteria(TimeSheetAccounts.class).add(Restrictions.idEq(entry_id)).uniqueResult();
	}
	
	public TimeSheetAccounts retrieve(String accountNum) {
		Session session = session();
		return (TimeSheetAccounts) session.createCriteria(TimeSheetAccounts.class).add(Restrictions.eq("AccountNum", accountNum)).uniqueResult();
	}
	

	@Override
	public void update(TimeSheetAccounts timeSheetAccounts) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(timeSheetAccounts);
		tx.commit();
	}

	@Override
	public void delete(TimeSheetAccounts timeSheetAccounts) {
		Session session = session();
		String hql = "DELETE FROM TimeSheetAccounts WHERE AccountNum = :AccountNum";
		Transaction tx = session.beginTransaction();
		session.createQuery(hql).setString("AccountNum", timeSheetAccounts.getAccountNum()).executeUpdate();
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeSheetAccounts> retrieveRawList() {
		Session session = session();
		return session.createCriteria(TimeSheetAccounts.class).list();
	}

}
