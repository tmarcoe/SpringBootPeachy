package com.peachy.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.PurchaseOrder;
import com.peachy.helper.ProfitDataRecord;
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
	public ProfitDataRecord getCostDataByMonth(int month, int year) {
		Object[] obj;
		ProfitDataRecord totals = new ProfitDataRecord();
		String sql = "SELECT SUM(price), SUM(qty) FROM PurchaseOrder WHERE MONTH(purchaseDate) = :month AND YEAR(purchaseDate) = :year";
		obj = (Object[]) session().createSQLQuery(sql).setInteger("month", month).setInteger("year", year).uniqueResult();
		
		if (obj[0] == null) {
			totals.setCost(BigDecimal.valueOf(0));
			totals.setAmount(BigDecimal.valueOf(0));
		}else{
			totals.setCost((BigDecimal) obj[0]);
			totals.setAmount((BigDecimal) obj[1]);
		}
		
		return totals;
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> getPurchaseOrderList(Date history) {
		Criteria crit = session().createCriteria(PurchaseOrder.class);
		List<PurchaseOrder> poList = crit.add(Restrictions.ge("purchaseDate", history)).list();
		
		session().disconnect();
		
		return poList;
	}
}
