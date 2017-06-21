package com.peachy.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.Coupons;
import com.peachy.interfaces.ICoupons;

@Transactional
@Repository
public class CouponsDao implements ICoupons {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(Coupons coupons) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(coupons);
		tx.commit();
	}

	@Override
	public Coupons retrieve(String coupon_id) {
		Session session = session();
		
		return (Coupons) session.createCriteria(Coupons.class).add(Restrictions.idEq(coupon_id)).uniqueResult();
	}

	@Override
	public void update(Coupons coupons) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(coupons);
		tx.commit();
		
	}

	@Override
	public void delete(Coupons coupons) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(coupons);
		tx.commit();
		
	}

}
