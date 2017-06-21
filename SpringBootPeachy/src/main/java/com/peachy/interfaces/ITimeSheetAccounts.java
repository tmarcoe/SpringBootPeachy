package com.peachy.interfaces;

import java.util.List;


import com.peachy.entity.TimeSheetAccounts;

public interface ITimeSheetAccounts {
	public void create(TimeSheetAccounts timeSheetAccounts);
	public TimeSheetAccounts retrieve(int entry_id);
	public void update(TimeSheetAccounts timeSheetAccounts);
	public void delete(TimeSheetAccounts timeSheetAccounts);
	public List<TimeSheetAccounts> retrieveRawList();

}
