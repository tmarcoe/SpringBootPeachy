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
		session.disconnect();
	}

	@Override
	public Employee retrieve(int user_id) {
		Session session = session();
		Employee emp = (Employee) session.createCriteria(Employee.class).add(Restrictions.idEq(user_id)).uniqueResult();
		session.disconnect();
		
		return emp;
	}

	@Override
	public void update(Employee employee) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(employee);
		tx.commit();
		session.disconnect();
	}

	@Override
	public void delete(Employee employee) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(employee);
		tx.commit();
		session.disconnect();
	}

	@SuppressWarnings("unchecked")
	public List<UserProfile> employeeList() {
		Session session = session();
		List<UserProfile> empList = session.createCriteria(Employee.class).add(Restrictions.isNull("endDate")).list();
		session.disconnect();
		
		return empList;
	}

}
