package com.csm.dashboard.dao;

import java.util.List;

public interface LOBDAO {
	public List<Object[]> getQuaterlyByLOB();

	public List<Object[]> getMonthlyByLOB();

	public List<Object[]> getWeeklyByLOB();

	public List<Object[]> getQuaterly();

	public List<Object[]> getMonthly();

	public List<Object[]> getWeekly();

	public List<Object[]> getContractType(String LOB, String periodType, String periodValue);
	
	public List<Object[]> getProducts(String LOB, String periodType, String periodValue);
}
