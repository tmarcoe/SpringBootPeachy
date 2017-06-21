package com.peachy.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.PaymentRegister;
import com.peachy.interfaces.IPaymentRegister;

@Transactional
@Repository
public class PaymentRegisterDao implements IPaymentRegister {
	
	@Autowired
	SessionFactory sessionFactory;
	
	Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	
	@Override
	public void create(PaymentRegister paymentRegister) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(paymentRegister);
		tx.commit();
	}

	@Override
	public PaymentRegister retrieve(int entryId) {
		Session session = session();
		return (PaymentRegister) session.createCriteria(PaymentRegister.class).add(Restrictions.idEq(entryId)).uniqueResult();
	}

	@Override
	public void update(PaymentRegister paymentRegister) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(paymentRegister);
		tx.commit();
	}

	@Override
	public void delete(PaymentRegister paymentRegister) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(paymentRegister);
		tx.commit();
	}

}
