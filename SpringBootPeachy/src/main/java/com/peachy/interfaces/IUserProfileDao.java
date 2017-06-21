package com.peachy.interfaces;

import com.peachy.entity.UserProfile;

public interface IUserProfileDao {
	public void create(UserProfile userProfile);
	public UserProfile retrieve(int user_id);
	public UserProfile retrieve(String username);
	public void update(UserProfile userProfile);
	public void delete(UserProfile userProfile);
	public void delete(String username);
}
