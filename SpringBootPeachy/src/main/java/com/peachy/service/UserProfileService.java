package com.peachy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.peachy.dao.UserProfileDao;
import com.peachy.entity.Role;
import com.peachy.entity.UserProfile;
import com.peachy.interfaces.IUserProfileService;

@Service
public class UserProfileService implements IUserProfileService {
	
	@Autowired
	UserProfileDao userProfileDao;
	@Autowired
	RoleService roleService;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public void create(UserProfile user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		List<Role> role = new ArrayList<Role>();
		user.setEnabled(true);
		user.setDateAdded(new Date());
		role.add(roleService.retrieve("USER"));
		user.setRoles(role);

		userProfileDao.create(user);
	}

	@Override
	public UserProfile retrieve(int user_id) {
		return userProfileDao.retrieve(user_id);
	}

	@Override
	public UserProfile retrieve(String username) {
		return userProfileDao.retrieve(username);
	}

	@Override
	public void update(UserProfile user) {
		userProfileDao.update(user);
	}

	@Override
	public void delete(UserProfile user) {
		userProfileDao.delete(user);
	}

	@Override
	public void delete(String username) {
		userProfileDao.delete(username);
	}

	public PagedListHolder<UserProfile> retrieveList() {
		return new PagedListHolder<UserProfile>(userProfileDao.retrieveList());
	}

	public boolean exists(String username) {
		return userProfileDao.exists(username);
	}
	
	public void partialUpdate(UserProfile user) {
		userProfileDao.partialUpdate(user);
	}
	
	public List<UserProfile> getDailySpecialUsers() {
		return userProfileDao.getDailySpecialUsers();
	}
	public void merge(UserProfile userProfile) {
		userProfileDao.merge(userProfile);
	}
}
