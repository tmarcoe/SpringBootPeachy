package com.peachy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.PasswordRecoveryDao;
import com.peachy.entity.PasswordRecovery;
import com.peachy.interfaces.IPasswordRecovery;

@Service
public class PasswordRecoveryService implements IPasswordRecovery {

	@Autowired
	PasswordRecoveryDao passwordRecovery;
	
	@Override
	public void create(PasswordRecovery pr) {
		passwordRecovery.create(pr);
	}

	@Override
	public PasswordRecovery retrieve(String token) {
		return passwordRecovery.retrieve(token);
	}

	@Override
	public void update(PasswordRecovery pr) {
		passwordRecovery.update(pr);
	}

	@Override
	public void delete(PasswordRecovery pr) {
		passwordRecovery.delete(pr);
	}

}
