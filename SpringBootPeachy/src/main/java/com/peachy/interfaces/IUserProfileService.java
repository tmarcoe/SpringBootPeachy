package com.peachy.interfaces;

import com.peachy.entity.UserProfile;

public interface IUserProfileService {
	public void create(UserProfile user);
	public UserProfile retrieve(int user_id);
	public UserProfile retrieve(String username);
	public void update(UserProfile user);
	public void delete(UserProfile user);
	public void delete(String username);

}
