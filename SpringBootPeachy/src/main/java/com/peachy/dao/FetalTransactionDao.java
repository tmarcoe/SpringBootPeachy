package com.peachy.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.component.CurrencyConfigurator;
import com.peachy.entity.ChartOfAccounts;
import com.peachy.entity.GeneralLedger;
import com.peachy.entity.Inventory;
import com.peachy.helper.Currency;

@Transactional
@Repository
public class FetalTransactionDao {

	@Autowired
	SessionFactory sessionFactory;

	private Transaction trans;

	@Autowired
	CurrencyConfigurator cc;

	
	public Transaction getTrans() {
		return trans;
	}

	Session session() {
		return sessionFactory.getCurrentSession();
	}

	public Session beginTrans() {
		Session session = session();
		trans = session.beginTransaction();

		return session;
	}

	public void commitTrans(Session session) {
		trans.commit();
		trans = null;
	}

	public void credit(Double amount, String account, Session session) {
		if (amount != 0) {
			ChartOfAccounts ac = (ChartOfAccounts) session.createCriteria(ChartOfAccounts.class)
					.add(Restrictions.idEq(account)).uniqueResult();
			if (ac.isDebitAccount() == false) {
				ac.setAccountBalance(ac.getAccountBalance() + amount);
			} else {
				ac.setAccountBalance(ac.getAccountBalance() - amount);
			}
			session.update(ac);
		}
	}

	public void debit(Double amount, String account, Session session) {
		if (amount != 0) {
			ChartOfAccounts ac = (ChartOfAccounts) session.createCriteria(ChartOfAccounts.class)
					.add(Restrictions.idEq(account)).uniqueResult();
			if (ac.isDebitAccount() == false) {
				ac.setAccountBalance(ac.getAccountBalance() - amount);
			} else {
				ac.setAccountBalance(ac.getAccountBalance() + amount);
			}
			session.update(ac);
		}
	}

	public void ledger(char type, Double amount, String account, String description, Session session) {
		if (amount != 0) {
			GeneralLedger gl = null;
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (type == 'C') {
				gl = new GeneralLedger(account, 0, cal.getTime(), description, 0, amount);
			} else if (type == 'D') {
				gl = new GeneralLedger(account, 0, cal.getTime(), description, amount, 0);
			}
			session.save(gl);
		}
	}

	public double getBalance(String account, Session session) {
		String hql = "FROM ChartOfAccounts WHERE account_num = :account";
		double retVal = (double) session.createQuery(hql).setString("account", account).uniqueResult();
		return retVal;
	}

	public void addStock(String sku, Long qty, Session session) {
		Session localSession = session();
		Transaction tx = localSession.beginTransaction();
		Inventory inv = retrieveInventory(sku, session);
		inv.setAmt_in_stock(inv.getAmt_in_stock() + Integer.valueOf(String.valueOf(qty)));
		session.update(inv);
		tx.commit();
		localSession.disconnect();
		
	}

	public void depleteStock(String sku, Long qty, Session session) {
		if (sku.startsWith("CPN") == false) {
			Session localSession = session();
			Integer amount = Integer.valueOf(String.valueOf(qty));
			String hql = "Update Inventory SET amt_committed = (amt_committed - :amount) WHERE sku_num = :sku";
			Transaction tx = localSession.beginTransaction();
			localSession.createQuery(hql).setInteger("amount", amount).setString("sku", sku).executeUpdate();
			tx.commit();
			localSession.disconnect();
		}
	}

	public void commitStock(String sku, Long qty, Session session) {
		Session localSession = session();
		Transaction tx = localSession.beginTransaction();
		Integer amount = Integer.valueOf(String.valueOf(qty));
		if (sku.startsWith("CPN") == false) {
			String hql = "UPDATE Inventory SET amt_in_stock = (amt_in_stock - :amount), amt_committed = (amt_committed + :amount) WHERE sku_num = :sku";
			session.createQuery(hql).setInteger("amount", amount).setString("sku", sku).executeUpdate();
		}
		tx.commit();
		localSession.disconnect();
	}

	public double getRate(String Target) {
		double exchangeRate = 0;

		try {
			exchangeRate = new Currency(cc).getRate(Target);
		} catch (IOException | URISyntaxException e) {

		}

		return exchangeRate;
	}


	private Inventory retrieveInventory(String sku, Session session) {
		return (Inventory) session.createCriteria(Inventory.class).add(Restrictions.idEq(sku)).uniqueResult();
	}

	public Object lookup(String sql) {
		Session session = session();
		Object obj = session.createQuery(sql).uniqueResult();
		
		session.disconnect();
		
		return obj;
	}

	public void update(String sqlWithArgs) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.createQuery(sqlWithArgs).executeUpdate();
		tx.commit();
	}

	public void rollback(Session session) {
		session.getTransaction().rollback();
	}

	@SuppressWarnings("unchecked")
	public List<Object> list(String sqlWithArgs) {
		Session session = session();
		List<Object> l = session.createQuery(sqlWithArgs).list();
		session.disconnect();
		
		return l;
	}

}
