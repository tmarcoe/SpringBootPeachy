package com.peachy.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.PurchaseOrder;
import com.peachy.interfaces.IPurchaseOrder;

@Transactional
@Repository
public class PurchaseOrderDao implements IPurchaseOrder {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(PurchaseOrder purchaseOrder) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(purchaseOrder);
		tx.commit();
	}

	@Override
	public PurchaseOrder retrieve(int orderId) {
		Session session = session();
		
		return (PurchaseOrder) session.createCriteria(PurchaseOrder.class).add(Restrictions.idEq(orderId)).uniqueResult();
	}

	@Override
	public void update(PurchaseOrder purchaseOrder) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(purchaseOrder);
		tx.commit();

	}

	@Override
	public void delete(PurchaseOrder purchaseOrder) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(purchaseOrder);
		tx.commit();

	}

}
