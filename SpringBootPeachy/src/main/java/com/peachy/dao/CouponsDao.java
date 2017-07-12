package com.peachy.dao;

import java.util.List;

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
		
		session.disconnect();
	}

	@Override
	public Coupons retrieve(String coupon_id) {
		Session session = session();
		Coupons cp = (Coupons) session.createCriteria(Coupons.class).add(Restrictions.idEq(coupon_id)).uniqueResult();
		session.disconnect();
		
		return cp;
	}
	
	public Coupons retrieveByName(String name) {
		Session session = session();
		Coupons cp = (Coupons) session.createCriteria(Coupons.class).add(Restrictions.eq("name", name)).uniqueResult();
		session.disconnect();
		
		return cp;
	}
	
	@SuppressWarnings("unchecked")
	public List<Coupons> retrieveList() {
		Session session = session();
		List<Coupons> cpList = session.createCriteria(Coupons.class).list();
		session.disconnect();
		
		return cpList;
	}

	@Override
	public void update(Coupons coupons) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(coupons);
		tx.commit();
		session.disconnect();
		
	}

	@Override
	public void delete(Coupons coupons) {
		Session session = session();
		String hql = "DELETE FROM Coupons WHERE coupon_id = :coupon_id";
		Transaction tx = session.beginTransaction();
		session.createQuery(hql).setString("coupon_id", coupons.getCoupon_id()).executeUpdate();
		tx.commit();
		session.disconnect();
		
	}

}
