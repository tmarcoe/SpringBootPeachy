package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.Employee;
import com.peachy.entity.UserProfile;
import com.peachy.interfaces.IEmployee;

@Transactional
@Repository
public class EmployeeDao implements IEmployee {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void Create(Employee employee) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(employee);
		tx.commit();
	}

	@Override
	public Employee retrieve(int user_id) {
		Session session = session();
		return (Employee) session.createCriteria(Employee.class).add(Restrictions.idEq(user_id)).uniqueResult();
	}

	@Override
	public void update(Employee employee) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(employee);
		tx.commit();
	}

	@Override
	public void delete(Employee employee) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(employee);
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public List<UserProfile> employeeList() {
		Session session = session();
		return session.createCriteria(Employee.class).add(Restrictions.isNull("endDate")).list();
	}

}
