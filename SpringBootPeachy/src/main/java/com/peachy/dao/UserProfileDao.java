package com.peachy.dao;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.peachy.entity.UserProfile;
import com.peachy.interfaces.IUserProfileDao;

@Transactional
@Repository
public class UserProfileDao implements IUserProfileDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void create(UserProfile userProfile) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.save(userProfile);
		tx.commit();
	}

	@Override
	public UserProfile retrieve(int user_id) {
		Session session = session();
		return (UserProfile) session.createCriteria(UserProfile.class).add(Restrictions.idEq(user_id)).uniqueResult();
	}

	@Override
	public UserProfile retrieve(String username) {
		Session session = session();
		return (UserProfile) session.createCriteria(UserProfile.class).add(Restrictions.eq("username", username)).uniqueResult();
	}
	
	@Override
	public void update(UserProfile userProfile) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(userProfile);
		tx.commit();
	}

	@Override
	public void delete(UserProfile userProfile) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.delete(userProfile);
		tx.commit();
	}

	@Override
	public void delete(String username) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		UserProfile user = (UserProfile) session.createCriteria(UserProfile.class).add(Restrictions.eqOrIsNull("username", username)).uniqueResult();
		session.delete(user);
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public List<UserProfile> retrieveList() {
		Session session = session();
		return session.createCriteria(UserProfile.class).list();
	}


	public boolean exists(String username) {
		Session session = session();
		String hql = "SELECT COUNT(*) FROM UserProfile WHERE username = :username";
		long count = (long) session.createQuery(hql).setString("username", username).uniqueResult();
		return (count > 0);
	}

	public void partialUpdate(UserProfile user) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		updateBilling(user, session);
		updateContactInfo(user, session);
		updateMisc(user, session);
		tx.commit();
		session().disconnect();

	}

	private void updateBilling(UserProfile user, Session session) {
		String hqlUpdate = "update UserProfile as u set address1 = :address1, "
				+ "address2 = :address2, " + "city = :city, "
				+ "region = :region, " + "postalCode = :postalCode, "
				+ "country = :country, " + "currency = :currency "
				+ "where user_id=:user_id";

		session.createQuery(hqlUpdate)
				.setString("address1", user.getAddress1())
				.setString("address2", user.getAddress2())
				.setString("city", user.getCity())
				.setString("region", user.getRegion())
				.setString("postalCode", user.getPostalCode())
				.setString("country", user.getCity())
				.setString("currency", user.getCurrency())
				.setInteger("user_id", user.getUser_id()).executeUpdate();

	}

	private void updateContactInfo(UserProfile user, Session session) {
		String hqlUpdate = "update UserProfile as u set firstname = :firstname, "
				+ "lastname = :lastname, "
				+ "maleFemale = :maleFemale, "
				+ "homePhone = :homePhone, "
				+ "cellPhone = :cellPhone, "
				+ "username = :username " + "where user_id=:user_id";

		session.createQuery(hqlUpdate)
				.setString("firstname", user.getFirstname())
				.setString("lastname", user.getLastname())
				.setString("maleFemale", user.getMaleFemale())
				.setString("homePhone", user.getHomePhone())
				.setString("cellPhone", user.getCellPhone())
				.setString("username", user.getUsername())
				.setInteger("user_id", user.getUser_id()).executeUpdate();
	}

	private void updateMisc(UserProfile user, Session session) {
		String hqlUpdate = "update UserProfile set monthlyMailing = :monthlyMailing, "
				+ "shippingInfo = :shippingInfo,"
				+ "enabled = :enabled, "
				+ "dailySpecials = :dailySpecials "
				+ "where user_id = :user_id";
		
		session.createQuery(hqlUpdate)
				.setBoolean("monthlyMailing", user.isMonthlyMailing())
				.setString("shippingInfo", user.getShippingInfo())
				.setBoolean("enabled", user.isEnabled())
				.setBoolean("dailySpecials", user.isDailySpecials())
				.setInteger("user_id", user.getUser_id()).executeUpdate();
	}

	public void updatePassword(UserProfile user) {
		Session session = session();
		String hqlUpdate = "update UserProfile as u set password = :password where user_id=:user_id";
		Transaction tx = session.beginTransaction();
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		session.createQuery(hqlUpdate)
				.setString("password", user.getPassword())
				.setInteger("user_id", user.getUser_id()).executeUpdate();
		tx.commit();

	}

	@SuppressWarnings("unchecked")
	public List<UserProfile> getDailySpecialUsers() {
		Session session = session();
		
		return session.createCriteria(UserProfile.class).add(Restrictions.eq("dailySpecials",true)).list();
	}
	
	public void merge(UserProfile userProfile) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.merge(userProfile);
		tx.commit();
	}
	@SuppressWarnings("unchecked")
	public List<BigInteger> getGenderBreakdown() {
		String sql = "select count(male_female) from user_profile group by male_female";
		List<BigInteger> counts = session().createSQLQuery(sql).list();
		session().disconnect();
		
		return counts;
	}

	@SuppressWarnings("unchecked")
	public List<UserProfile> selectEmployees() {
		Session session = session();
		String hql = "FROM UserProfile u WHERE EXISTS (SELECT 1 FROM Employee e WHERE startDate IS NOT null AND u.user_id = e.user_id)";
		
		return session.createQuery(hql).list();
	}

}
