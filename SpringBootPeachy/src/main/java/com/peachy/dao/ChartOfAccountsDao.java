package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.ChartOfAccounts;
import com.peachy.interfaces.IChartOfAccountsDao;

@Transactional
@Repository
public class ChartOfAccountsDao implements IChartOfAccountsDao {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(ChartOfAccounts accounts) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(accounts);
		tx.commit();
	}

	@Override
	public void update(ChartOfAccounts accounts) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(accounts);
		tx.commit();
	}

	@Override
	public ChartOfAccounts retrieve(String account) {
		Session session = session();
		Criteria crit = session.createCriteria(ChartOfAccounts.class);
		crit.add(Restrictions.idEq(account));
		return (ChartOfAccounts) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChartOfAccounts> retrieveList() {
		Session session = session();
		Criteria crit = session.createCriteria(ChartOfAccounts.class);
		
		return crit.list();
	}

	@Override
	public void delete(ChartOfAccounts account) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(account);
		tx.commit();
	}

	@Override
	public void delete(String account) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(ChartOfAccounts.class);
		crit.add(Restrictions.idEq(account));
		ChartOfAccounts acc = (ChartOfAccounts) crit.uniqueResult();
		session.delete(acc);
		tx.commit();
	}

	public boolean exists(String account) {
		Session session = session();
		String hql = "SELECT COUNT(*) FROM ChartOfAccounts WHERE accountNum = :account";
		long count = (long) session.createQuery(hql).setString("account", account).uniqueResult();
		
		return (count > 0);
	}
	
	public boolean exist() {
		Session session = session();
		String hql = "SELECT COUNT(*) FROM ChartOfAccounts";
		long count = (long) session.createQuery(hql).uniqueResult();
		
		return (count > 0);
	}

}
