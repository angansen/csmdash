package com.csm.dashboard.dao;

import java.util.List;

public interface ProductDAO {
	
	public List<Object[]> getQuaterlyByProduct();
	
	public List<Object[]> getQuaterly();
	
	public List<Object[]> getMonthlyByProduct();
	
	public List<Object[]> getMonthly();
	
	public List<Object[]> getWeeklyByProduct();
	
	public List<Object[]> getWeekly();
	
	public List<Object[]> getContractType(String LOB, String periodType, String periodValue);
	
	public List<Object[]> getLOB(String LOB, String periodType, String periodValue);
}
