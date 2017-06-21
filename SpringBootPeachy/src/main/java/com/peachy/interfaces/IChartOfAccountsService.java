package com.peachy.interfaces;

import java.util.List;

import org.springframework.beans.support.PagedListHolder;

import com.peachy.entity.ChartOfAccounts;

public interface IChartOfAccountsService {

	public void create(ChartOfAccounts accounts);
	public void update(ChartOfAccounts accounts);
	public ChartOfAccounts retrieve(String account);
	public PagedListHolder<ChartOfAccounts> retrieveList();
	public List<ChartOfAccounts> retrieveRawList();
	public void delete(ChartOfAccounts account);
	public void delete(String account);
}
