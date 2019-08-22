package com.csm.dashboard.dao;

import java.util.List;

public interface ContractTypeDAO {

	List<Object[]> getQuaterly();

	List<Object[]> getMonthly();

	List<Object[]> getWeekly();

	List<Object[]> getWeeklyByContractType();

	List<Object[]> getMonthlyByContractType();

	List<Object[]> getQuaterlyByContractType();

}
