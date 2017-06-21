package com.peachy.interfaces;

import java.util.Date;
import java.util.List;

import com.peachy.entity.TimeSheet;

public interface ITimeSheet {
	public boolean create(TimeSheet timesheet);
	public TimeSheet retrieve(int entry_id);
	public List<TimeSheet> retrieveList(int user_id, Date startPeriod);
	public void update(TimeSheet timesheet);
	public void delete(TimeSheet timesheet);

}
