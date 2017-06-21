package com.peachy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.peachy.dao.TimeSheetAccountsDao;
import com.peachy.entity.TimeSheetAccounts;
import com.peachy.interfaces.ITimeSheetAccounts;

@Service
public class TimeSheetAccountsService implements ITimeSheetAccounts {
	
	@Autowired
	TimeSheetAccountsDao timeSheetAccountsDao;
	
	
	@Override
	public void create(TimeSheetAccounts timeSheetAccounts) {
		timeSheetAccountsDao.create(timeSheetAccounts);
	}

	@Override
	public TimeSheetAccounts retrieve(int entry_id) {
		return timeSheetAccountsDao.retrieve(entry_id);
	}
	
	public TimeSheetAccounts retrieve(String accounts) {
		return timeSheetAccountsDao.retrieve(accounts);
	}

	@Override
	public void update(TimeSheetAccounts timeSheetAccounts) {
		timeSheetAccountsDao.update(timeSheetAccounts);
	}

	@Override
	public void delete(TimeSheetAccounts timeSheetAccounts) {
		timeSheetAccountsDao.delete(timeSheetAccounts);
	}

	@Override
	public List<TimeSheetAccounts> retrieveRawList() {
		return timeSheetAccountsDao.retrieveRawList();
	}

	public PagedListHolder<TimeSheetAccounts> retrieveList() {
		return new PagedListHolder<TimeSheetAccounts>(timeSheetAccountsDao.retrieveRawList());
	}

}
