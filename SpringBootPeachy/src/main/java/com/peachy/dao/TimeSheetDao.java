package com.peachy.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.TimeSheet;
import com.peachy.interfaces.ITimeSheet;

@Transactional
@Repository
public class TimeSheetDao implements ITimeSheet {

	@Autowired
	SessionFactory sessionFactory;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public boolean create(TimeSheet timesheet) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(timesheet);
		tx.commit();
		session.disconnect();
		
		return true;
	}

	@Override
	public TimeSheet retrieve(int entry_id) {
		Session session = session();
		TimeSheet ts = (TimeSheet) session.createCriteria(TimeSheet.class).add(Restrictions.idEq(entry_id)).uniqueResult();
		
		return ts;
	}
	
	public TimeSheet retrieve(int userId, Date startPeriod, String accountNum) {
		Session session = session();
		String hql = "FROM TimeSheet WHERE user_id = :userId AND startPeriod = :startPeriod AND accountNum = :accountNum";
		TimeSheet timeSheet = (TimeSheet) session.createQuery(hql).setInteger("userId", userId)
												   .setDate("startPeriod", startPeriod)
												   .setString("accountNum", accountNum)
												   .uniqueResult();
		session.disconnect();

		return timeSheet;
	}
	
	public boolean exits(int userId, Date startPeriod, String accountNum) {
		Session session = session();
		String hql = "SELECT COUNT(*) FROM TimeSheet WHERE user_id = :userId AND startPeriod = :startPeriod AND accountNum = :accountNum";
		long count = (long) session.createQuery(hql).setInteger("userId", userId)
													.setDate("startPeriod", startPeriod).setString("accountNum", accountNum).uniqueResult();

		session.disconnect();

		return (count > 0);
	}

	@Override
	public void update(TimeSheet timesheet) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(timesheet);
		tx.commit();
		session.disconnect();
	}

	@Override
	public void delete(TimeSheet timesheet) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(timesheet);
		tx.commit();
		session.disconnect();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeSheet> retrieveList(int userId, Date startPeriod) {
		Session session = session();
		String hql = "FROM TimeSheet WHERE user_id = :userId AND startPeriod = :startPeriod AND submitted = null";
		List<TimeSheet> timeSheets = session.createQuery(hql)
											  .setInteger("userId", userId)
											  .setDate("startPeriod", startPeriod).list();
		session.disconnect();

		return timeSheets;
	}
	
	public List<TimeSheet> retrieveClosed(int userId, Date startPeriod) {
		Session session = session();
		String hql = "FROM TimeSheet WHERE user_id = :userId AND startPeriod = :startPeriod AND closed != null";
		@SuppressWarnings("unchecked")
		List<TimeSheet> timeSheets = session.createQuery(hql).setInteger("userId", userId)
											.setDate("startPeriod", startPeriod).list();
		session.disconnect();

		return timeSheets;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPayrollPeriods() {
		Session session = session();
		String sql = "SELECT distinct(DATE_FORMAT(start_period,'%Y-%m-%d')) FROM time_sheet WHERE approved IS NOT null and closed IS null";
		List<String> periods = session.createSQLQuery(sql).list();
		session.disconnect();

		return periods;
	}

	@SuppressWarnings("unchecked")
	public List<TimeSheet> getApprovedTimeSheets(int userId, Date startPeriod) {
		Session session = session();
		String hql = "FROM TimeSheet WHERE user_id = :userId AND startPeriod = :startPeriod AND approved != null AND closed = null";
		List<TimeSheet> timeSheets = session.createQuery(hql).setInteger("userId", userId)
											.setDate("startPeriod", startPeriod).list();
		session.disconnect();

		return timeSheets;
	}

	public List<TimeSheet> getSubmittedTimeSheet(int userId, Date startPeriod) {
		Session session = session();
		String hql = "FROM TimeSheet WHERE user_id = :userId AND startPeriod = :startPeriod AND submitted != null AND approved = null";
		@SuppressWarnings("unchecked")
		List<TimeSheet> timeSheets = session.createQuery(hql).setInteger("userId", userId)
											.setDate("startPeriod", startPeriod).list();
		session.disconnect();

		return timeSheets;
	}
	
	public void closeTimeSheet(int userId, Date startPeriod) {
		Session session = session();
		String hql = "UPDATE TimeSheet SET closed = CURRENT_DATE() WHERE user_id = :userId AND startPeriod = :startPeriod";
		Transaction tx = session.beginTransaction();
		session.createQuery(hql).setInteger("userId", userId).setDate("startPeriod", startPeriod).executeUpdate();
		tx.commit();
		session.disconnect();
	}

	public void approveTimeSheet(int userId, Date startPeriod) {
		Session session = session();
		String hql = "UPDATE TimeSheet SET approved = CURRENT_DATE() WHERE user_id = :userId AND startPeriod = :startPeriod";
		Transaction tx = session.beginTransaction();
		session().createQuery(hql).setInteger("userId", userId).setDate("startPeriod", startPeriod).executeUpdate();
		tx.commit();
		session.disconnect();
	}

	public void submit(int userId, Date startPeriod) {
		Session session = session();
		String hql = "UPDATE TimeSheet SET submitted = CURRENT_DATE() WHERE user_id = :userId and startPeriod = :startPeriod";
		Transaction tx = session.beginTransaction();
		session().createQuery(hql).setInteger("userId", userId).setDate("startPeriod", startPeriod).executeUpdate();
		tx.commit();
		session.disconnect();
	}

	public double totalHours(int userId, Date startPeriod) {
		Session session = session();
		String hql = "SELECT (SUM(sunday)+SUM(monday)+SUM(tuesday)+SUM(wednesday)+SUM(thursday)+SUM(friday)+SUM(saturday)) " + 
					 "FROM TimeSheet WHERE userId = :userId AND startPeriod = :startPeriod";
		double hours = (double) session.createQuery(hql).setInteger("userId", userId).setDate("startPeriod", startPeriod).uniqueResult();
		session.disconnect();
		
		return hours;
	}

	public boolean exists(int user_id, Date startPeriod) {
		Session session = session();
		String hql = "SELECT COUNT(*) FROM TimeSheet WHERE userId = :userId AND startPeriod = :startPeriod";
		long count = (long) session.createQuery(hql).setInteger("userId", user_id).setDate("startPeriod", startPeriod).uniqueResult();
		session.disconnect();
	
		return (count > 0);
	}

}
