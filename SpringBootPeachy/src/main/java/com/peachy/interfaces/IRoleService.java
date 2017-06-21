package com.peachy.interfaces;

import com.peachy.entity.Role;

public interface IRoleService {
	public void create(Role role);
	public Role retrieve(int role_id);
	public Role retrieve(String role);
	public void update(Role role);
	public void delete(Role role);

}
