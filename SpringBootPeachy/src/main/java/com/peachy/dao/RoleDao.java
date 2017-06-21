package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.Role;
import com.peachy.interfaces.IRoleDao;

@Transactional
@Repository
public class RoleDao implements IRoleDao {

	@Autowired
	SessionFactory sessionFactory;
	
	Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(Role role) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(role);
		tx.commit();
	}

	@Override
	public void update(Role role) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(role);
		tx.commit();
	}

	@Override
	public Role retrieve(int id) {
		Session session = session();
		return (Role) session.createCriteria(Role.class).add(Restrictions.idEq(id)).uniqueResult();
	}
	
	@Override
	public Role retrieve(String role) {
		Session session = session();
		return (Role) session.createCriteria(Role.class).add(Restrictions.eq("role", role)).uniqueResult();
	}

	@Override
	public Role delete(Role role) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(role);
		tx.commit();
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Role> retrieveList() {
		Session session = session();
		
		return session.createCriteria(Role.class).list();
	}

}
