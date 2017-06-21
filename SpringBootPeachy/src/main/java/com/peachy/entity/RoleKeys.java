package com.peachy.entity;

import java.io.Serializable;

public class RoleKeys implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Integer user_id;
	protected Integer role_id;
	
	public RoleKeys() {}
	
	public RoleKeys(Integer user_id, Integer role_id) {
		this.user_id = user_id;
		this.role_id = role_id;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role_id == null) ? 0 : role_id.hashCode());
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleKeys other = (RoleKeys) obj;
		if (role_id == null) {
			if (other.role_id != null)
				return false;
		} else if (!role_id.equals(other.role_id))
			return false;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		return true;
	}


}
