package com.peachy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="password_recovery")
public class PasswordRecovery implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String token;
	private int user_id;
	private Date expires;
	
	public PasswordRecovery() {

	}

	public PasswordRecovery(String token, int user_id, Date expires) {
		this.token = token;
		this.user_id = user_id;
		this.expires = expires;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}
}
