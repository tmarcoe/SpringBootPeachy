package com.peachy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.peachy.dao.ChartOfAccountsDao;
import com.peachy.entity.ChartOfAccounts;
import com.peachy.interfaces.IChartOfAccountsService;

@Service
public class ChartOfAccountsService implements IChartOfAccountsService {

	@Autowired
	ChartOfAccountsDao chartOfAccountsDao;
	
	@Override
	public void create(ChartOfAccounts accounts) {
		chartOfAccountsDao.create(accounts);
	}

	@Override
	public void update(ChartOfAccounts accounts) {
		chartOfAccountsDao.update(accounts);
	}

	@Override
	public ChartOfAccounts retrieve(String account) {
		return chartOfAccountsDao.retrieve(account);
	}

	@Override
	public PagedListHolder<ChartOfAccounts> retrieveList() {
		return new PagedListHolder<ChartOfAccounts>(chartOfAccountsDao.retrieveList());
	}
	
	public List<ChartOfAccounts> getRawList() {
		return chartOfAccountsDao.retrieveList();
	}

	@Override
	public List<ChartOfAccounts> retrieveRawList() {
		return chartOfAccountsDao.retrieveList();
	}

	@Override
	public void delete(ChartOfAccounts account) {
		chartOfAccountsDao.delete(account);
	}

	@Override
	public void delete(String account) {
		chartOfAccountsDao.delete(account);
	}
	
	public boolean exists(String account) {
		return chartOfAccountsDao.exists(account);
	}
	public boolean exists() {
		return chartOfAccountsDao.exist();
	}


}
