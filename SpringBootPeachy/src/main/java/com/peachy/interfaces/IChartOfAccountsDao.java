package com.peachy.interfaces;

import java.util.List;

import com.peachy.entity.ChartOfAccounts;

public interface IChartOfAccountsDao {
	public void create(ChartOfAccounts accounts);
	public void update(ChartOfAccounts accounts);
	public ChartOfAccounts retrieve(String account);
	public List<ChartOfAccounts> retrieveList();
	public void delete(ChartOfAccounts account);
	public void delete(String account);
}
