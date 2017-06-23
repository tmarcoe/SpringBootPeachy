package com.peachy.dao;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.InvoiceItem;
import com.peachy.interfaces.IInvoiceItem;

@Transactional
@Repository
public class InvoiceItemDao implements IInvoiceItem {

	@Autowired
	SessionFactory sessionFactory;
	
	Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(InvoiceItem item) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(item);
		tx.commit();
	}

	@Override
	public InvoiceItem retrieve(int item_id) {
		Session session = session();
		Criteria crit = session.createCriteria(InvoiceItem.class).add(Restrictions.idEq(item_id));
		return (InvoiceItem) crit.uniqueResult();
	}

	@Override
	public InvoiceItem retrieve(int invoice_num, String sku_num) {
		Session session = session();
		String hql = "FROM InvoiceItem WHERE invoice_num = :invoice_num AND sku_num = :sku_num";
		
		return (InvoiceItem) session.createQuery(hql)
									.setInteger("invoice_num", invoice_num)
									.setString("sku_num", sku_num)
									.uniqueResult();
	}

	@Override
	public boolean exists(int invoice_num, String sku_num) {
		Session session = session();
		String hql = "SELECT COUNT(*) FROM InvoiceItem WHERE invoice_num = :invoice_num AND sku_num = :sku_num";
		long count = (long) session.createQuery(hql)
				.setInteger("invoice_num",invoice_num)
				.setString("sku_num", sku_num)
				.uniqueResult();
		return (count > 0);
	}

	@Override
	public void update(InvoiceItem item) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(item);
		tx.commit();

	}

	@Override
	public void delete(InvoiceItem item) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(item);
		tx.commit();

	}

	public void delete(int invoice_num, String sku_num) {
		String hql = "DELETE FROM InvoiceItem WHERE invoice_num = :invoice_num AND sku_num = :sku_num";
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.createQuery(hql).setInteger("invoice_num", invoice_num).setString("sku_num", sku_num).executeUpdate();
		tx.commit();
	}

	public void merge(InvoiceItem item) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.merge(item);
		tx.commit();
	}

	public boolean exists(int invoice_num) {
		Session session = session();
		long count;
		
		String hql = "SELECT COUNT(*) FROM InvoiceItem WHERE invoice_num = :invoice_num";
		count = (long) session.createQuery(hql).setInteger("invoice_num", invoice_num).uniqueResult();
		
		return (count > 0);
	}
	 
	public long countCoupons(int user_id, String coupon) {
		Session session = session();
		long count = 0;
		String sql = "SELECT COUNT(*) FROM Invoice a, InvoiceItem b WHERE a.user_id = :user_id " +
					 "AND a.invoice_num = b.invoice_num AND sku_num = :coupon";
		
		count = (long) session.createSQLQuery(sql).setInteger("user_id", user_id).setString("coupon", coupon).uniqueResult();
		
		return count;
	}
	
	public boolean hasCoupons(int invoice_num) {
		Session session = session();
		String hql = "SELECT COUNT(*) FROM InvoiceItem WHERE invoice_num = :invoice_num AND sku_num LIKE CPN%";
		long count = (long) session.createQuery(hql).setInteger("invoice_num", invoice_num).uniqueResult();
		
		return (count > 0);
	}

}