package com.csm.dashboard.dao;

import java.util.List;

import com.csm.dashboard.model.Consumption;

public interface DashboardDAO{
	public List<Consumption> getAllUsageDataStarter();
	public List<Object[]> getContractTypeData();
	public List<Object[]> getLOB();
	public List<Object[]> getProducts();
	public List<Object[]> getQuaterly();
	public List<Object[]> getMonthly();
	public List<Object[]> getWeekly();
}
