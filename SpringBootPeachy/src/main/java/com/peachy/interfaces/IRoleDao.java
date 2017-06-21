package com.peachy.interfaces;

import com.peachy.entity.Role;

public interface IRoleDao {
	public void create(Role role);
	public void update(Role role);
	public Role retrieve(int id);
	public Role retrieve(String role);
	public Role delete(Role role);
}
