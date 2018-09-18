package com.csm.dashboard.dao;

import java.util.List;

import com.csm.dashboard.model.UsageData;

public interface DashboardDAO{
	public List<UsageData> getAllUsageDataStarter();
	public List<Object[]> getContractTypeData();
	public List<Object[]> getLOB();
	public List<Object[]> getProducts();
	public List<Object[]> getQuaterly();
	public List<Object[]> getMonthly();
	public List<Object[]> getWeekly();
}
