package com.peachy.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.Invoice;
import com.peachy.interfaces.IInvoice;

@Transactional
@Repository
public class InvoiceDao implements IInvoice {

	@Autowired
	SessionFactory sessionFactory;
	
	Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(Invoice header) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(header);
		tx.commit();
	}

	@Override
	public Invoice retrieve(int invoice_num) {
		Session session = session();
		String hql = "FROM Invoice WHERE invoice_num = :invoice_num";
		
		return (Invoice) session.createQuery(hql).setInteger("invoice_num", invoice_num).uniqueResult();
	}

	@Override
	public void update(Invoice header) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(header);
		tx.commit();
	}

	@Override
	public void delete(Invoice header) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(header);
		tx.commit();
	}

	@Override
	public void merge(Invoice header) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.merge(header);
		tx.commit();
	}

	public Invoice getOpenOrder(int userID) {
		String hql = "from Invoice where user_id = :userID and processed = null";

		Invoice invHdr = (Invoice) session().createQuery(hql)
				.setInteger("userID", userID).uniqueResult();
		session().disconnect();

		return invHdr;
	}
	
	public void deleteInvoice(int invoice_num) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		String itms = "DELETE FROM InvoiceItem WHERE invoice_num = :invoice_num";
		String hdr = "DELETE FROM Invoice WHERE invoice_num = :invoice_num"; 
		
		session.createQuery(itms).setInteger("invoice_num", invoice_num).executeUpdate();
		session.createQuery(hdr).setInteger("invoice_num", invoice_num).executeUpdate();
		tx.commit();
	}
	
	public long purgeCoupons(int invoice_num) {
		Session session = session();
		String hql = "DELETE FROM InvoiceItem WHERE invoice_num = :invoice_num AND sku_num LIKE 'CPN%'";
		Transaction tx = session.beginTransaction();
		session.createQuery(hql).setInteger("invoice_num", invoice_num).executeUpdate();
		tx.commit();
		hql = "SELECT COUNT(*) FROM InvoiceItem WHERE invoice_num = :invoice_num";
		long count = (long) session.createQuery(hql).setInteger("invoice_num", invoice_num).uniqueResult();
		
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Invoice> getHistory(int user_id) {
		Session session = session();
		String hql = "From Invoice WHERE user_id = :user_id";
		session.createQuery(hql).setInteger("user_id", user_id).list();
		
		return session.createQuery(hql).setInteger("user_id", user_id).list();
	}

	@SuppressWarnings("unchecked")
	public List<Invoice> getProcessedInvoices() {
		Session session = session();
		
		return session.createCriteria(Invoice.class).add(Restrictions.isNotNull("processed")).add(Restrictions.isNull("shipped")).list();
	}
	
	public double getCountByMonth(int month, int year) {
		Object obj = null;
		double total = 0.0;
		String sql = "select sum(total) from Invoice  where month(modified) = :month and year(modified) = :year";
		obj = session().createSQLQuery(sql).setInteger("month", month).setInteger("year", year).uniqueResult();
		
		if (obj != null) {
			total = (double) obj;
		}else{
			total = 0;
		}
		session().disconnect();
		
		return total;
	}
	
	public BigInteger getCustomerCounts(int month, int year) {
		Session session = session();
		Object obj = null;
		BigInteger total = BigInteger.valueOf(0);
		String sql = "SELECT count(DISTINCT user_id) FROM Invoice  WHERE month(modified) = :month and year(modified) = :year";
		obj = session.createSQLQuery(sql).setInteger("month", month).setInteger("year", year).uniqueResult();
		
		if (obj != null) {
			total = (BigInteger) obj;
		}else{
			total = BigInteger.valueOf(0);
		}
		
		return total;
	}
	
	public BigDecimal getMonthlyTotal(int month, int year) {
		BigDecimal total;
		String sql = "SELECT SUM(amount) FROM invoice_item i, invoice h " +
					 "WHERE i.invoice_num = h.invoice_num AND MONTH(h.processed) = " + 
					 ":month AND YEAR(h.processed) = :year AND sku_num NOT LIKE 'CPN%'";
		total = (BigDecimal) session().createSQLQuery(sql).setInteger("month", month).setInteger("year", year).uniqueResult();
		
		return total;
	}


}
