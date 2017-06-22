package com.peachy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.peachy.dao.PettyCashRegisterDao;
import com.peachy.entity.PettyCashRegister;
import com.peachy.interfaces.IPettyCashRegister;

@Service
public class PettyCashRegisterService implements IPettyCashRegister {

	@Autowired
	PettyCashRegisterDao pettyCashRegisterDao;
	
	@Override
	public void create(PettyCashRegister pettyCashRegister) {
		pettyCashRegisterDao.create(pettyCashRegister);
	}

	@Override
	public PettyCashRegister retrieve(int registerId) {
		return pettyCashRegisterDao.retrieve(registerId);
	}
	public PagedListHolder<PettyCashRegister> retrieveList() {
		return new PagedListHolder<PettyCashRegister>(pettyCashRegisterDao.retrieveList());
	}

	@Override
	public void update(PettyCashRegister pettyCashRegister) {
		pettyCashRegisterDao.update(pettyCashRegister);
	}

	@Override
	public void delete(PettyCashRegister pettyCashRegister) {
		pettyCashRegisterDao.delete(pettyCashRegister);
	}

}
