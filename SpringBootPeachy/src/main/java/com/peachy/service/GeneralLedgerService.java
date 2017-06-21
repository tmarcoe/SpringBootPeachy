package com.peachy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.peachy.dao.GeneralLedgerDao;
import com.peachy.entity.GeneralLedger;
import com.peachy.helper.DatePicker;
import com.peachy.interfaces.IGeneralLedger;

@Service
public class GeneralLedgerService implements IGeneralLedger {

	@Autowired
	GeneralLedgerDao generalLedgerDao;
	
	@Override
	public void create(GeneralLedger generalLedger) {
		generalLedgerDao.create(generalLedger);
	}

	@Override
	public GeneralLedger retrieve(int entry_num) {
		return generalLedgerDao.retrieve(entry_num);
	}

	@Override
	public void update(GeneralLedger generalLedger) {
		generalLedgerDao.update(generalLedger);
	}

	@Override
	public void delete(GeneralLedger generalLedger) {
		generalLedgerDao.delete(generalLedger);
	}
	
	public PagedListHolder<GeneralLedger> getPagedList(DatePicker datePicker) {
		return new PagedListHolder<GeneralLedger>(generalLedgerDao.getList(datePicker.getStart(), datePicker.getEnd()));
	}

}
