package com.peachy.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.TimeSheetDao;
import com.peachy.entity.TimeSheet;
import com.peachy.interfaces.ITimeSheet;

@Service
public class TimeSheetService implements ITimeSheet {
	@Autowired
	TimeSheetDao timeSheetDao;

	@Override
	public boolean create(TimeSheet timesheet) {
		boolean success = false;
		if (exists(timesheet.getUserId(), timesheet.getStartPeriod(), timesheet.getAccountNum()) == true) {
			TimeSheet oldTimeSheet = timeSheetDao.retrieve(timesheet.getUserId(), timesheet.getStartPeriod(), timesheet.getAccountNum());
			if (oldTimeSheet.getSubmitted() == null) {
				timesheet.setEntryId(oldTimeSheet.getEntryId());
				timesheet.setSunday(timesheet.getSunday() + oldTimeSheet.getSunday());
				timesheet.setMonday(timesheet.getMonday() + oldTimeSheet.getMonday());
				timesheet.setTuesday(timesheet.getTuesday() + oldTimeSheet.getTuesday());
				timesheet.setWednesday(timesheet.getWednesday() + oldTimeSheet.getWednesday());
				timesheet.setThursday(timesheet.getThursday() + oldTimeSheet.getThursday());
				timesheet.setFriday(timesheet.getFriday() + oldTimeSheet.getFriday());
				timesheet.setSaturday(timesheet.getSaturday() + oldTimeSheet.getSaturday());
				timeSheetDao.update(timesheet);
				success =  true;
			}
		}else{
			timeSheetDao.create(timesheet);
			success = true;
		}
		return success;
	}

	@Override
	public TimeSheet retrieve(int entry_id) {
		
		return timeSheetDao.retrieve(entry_id);
	}

	@Override
	public List<TimeSheet> retrieveList(int user_id, Date startPeriod) {
		return timeSheetDao.retrieveList(user_id, startPeriod);
	}

	@Override
	public void update(TimeSheet timesheet) {
		timeSheetDao.update(timesheet);		
	}

	@Override
	public void delete(TimeSheet timesheet) {
		timeSheetDao.delete(timesheet);		
	}
	
	public boolean exists(int user_id, Date startPeriod, String accountNum) {
		return timeSheetDao.exits(user_id, startPeriod, accountNum);
	}
	
	public List<TimeSheet> retrieveClosed(int userId, Date startPeriod) {
		return timeSheetDao.retrieveClosed(userId, startPeriod);
	}

	public List<TimeSheet> retrieveApproveList(int userID, Date startPeriod) {
		return timeSheetDao.getApprovedTimeSheets(userID, startPeriod);
	}

	public void submit(int user_id, Date startPeriod) {
		timeSheetDao.submit(user_id, startPeriod);
	}

	public void approveTimeSheet(int userId, Date startPeriod) {
		timeSheetDao.approveTimeSheet(userId, startPeriod);
	}

	public List<String> getPayrollPeriods() {
		return timeSheetDao.getPayrollPeriods();
	}
	public double totalHours(int userId, Date startPeriod) {
		return timeSheetDao.totalHours(userId, startPeriod);
	}
	public void closeTimeSheet(int userId, Date startPeriod) {
		timeSheetDao.closeTimeSheet(userId, startPeriod);
	}
}
