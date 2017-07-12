package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.Schemes;
import com.peachy.interfaces.ISchemes;

@Transactional
@Repository
public class SchemesDao implements ISchemes {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(Schemes schemes) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(schemes);
		tx.commit();
		session.disconnect();
	}

	@Override
	public Schemes retrieve(int entry_id) {
		Session session = session();
		Schemes s = (Schemes) session.createCriteria(Schemes.class).add(Restrictions.idEq(entry_id)).uniqueResult();
		session.disconnect();

		return s;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Schemes> retrieveScheme(String scheme) {
		Session session = session();
		List<Schemes> sList = session.createCriteria(Schemes.class).add(Restrictions.eq("scheme", scheme)).list();
		session.disconnect();

		return sList;
	}

	@Override
	public void update(Schemes schemes) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(schemes);
		tx.commit();
		session.disconnect();

	}

	@Override
	public void delete(Schemes schemes) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(schemes);
		tx.commit();
		session.disconnect();

	}

	@SuppressWarnings("unchecked")
	public List<String> retrieveAllSchemes() {
		Session session = session();
		String hql = "SELECT DISTINCT scheme FROM Schemes";
		List<String> sList = session.createQuery(hql).list();
		session.disconnect();	
		
		return sList;
	}

	public void delete(String scheme) {
		Session session = session();
		String hql = "DELETE FROM Schemes WHERE scheme = :scheme";
		Transaction tx = session.beginTransaction();
		session.createQuery(hql).setString("scheme", scheme).executeUpdate();
		tx.commit();
		session.disconnect();
	}

	public boolean idExists(String scheme, String id) {
		Session session = session();
		long count;
		String hql = "SELECT COUNT(*) FROM Schemes WHERE scheme = :scheme AND id = :id";
		
		count = (long) session.createQuery(hql).setString("scheme", scheme).setString("id", id).uniqueResult();
		session.disconnect();
		
		return (count > 0);
	}

}
