package com.csm.dashboard.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csm.dashboard.dao.AutonomousDAOImpl;
import com.csm.dashboard.model.PeriodicUsage;
import com.csm.dashboard.model.UsageBean;

@Service
public class AutonomousServiceImpl {

	@Autowired
	AutonomousDAOImpl autonomousDAO;

	@Autowired
	ContractTypeServiceImpl contractService;

	private String getMonthName(int monthNumber) {
		String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		return months[monthNumber - 1];
	}

	public HashMap<String, Object> usageMap() {
		HashMap<String, Object> usageMap = new HashMap<String, Object>();
		//HashSet<String> autonomousSet = new HashSet<String>();
		TreeSet<String> autonomousSet = new TreeSet<String>();
		LinkedHashSet<String> autonomousLinkedHashSet = new LinkedHashSet<String>();
		usageMap.put("quater", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("month", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("week", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("year", contractService.getYearlyDataMap("autonomous"));
		usageMap.put("dropdown", autonomousLinkedHashSet);

		List<Object[]> results = null;

		// Get the Quater map
		HashMap<String, List<PeriodicUsage>> quatermap = (HashMap<String, List<PeriodicUsage>>) usageMap.get("quater");
		HashMap<String, List<PeriodicUsage>> monthMap = (HashMap<String, List<PeriodicUsage>>) usageMap.get("month");
		HashMap<String, List<PeriodicUsage>> weekMap = (HashMap<String, List<PeriodicUsage>>) usageMap.get("week");

		List<String> quaterList = new ArrayList<String>();

		// GET THT QUATER USAGE DATA
		results = autonomousDAO.getQuaterlyByAutonomous();

		// List <PeriodicUsage> periodicUsageList1=initializeQuaters();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String autonomousType = (String) arr[0];
			String fiscalQuater = ((String) arr[1]).replace("FY19-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
			double quaterlyValue = valueBigDecimal.doubleValue();

			String usageData = quaterlyValue + "";
			autonomousSet.add(autonomousType);

			if (quatermap.containsKey(autonomousType)) {
				quatermap.get(autonomousType).add(new PeriodicUsage(fiscalQuater, usageData));

			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater, usageData));
				quatermap.put(autonomousType, periodicUsageList);
			}
		}
		autonomousSet.remove("all");
		autonomousLinkedHashSet.add("All");
		autonomousLinkedHashSet.addAll(autonomousSet);
		
		// GET THT MONTH USAGE DATA
		results = autonomousDAO.getMonthlyByAutonomous();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String autonomousType = (String) arr[0];
			String fiscalMonth = (arr[1] + "");

			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
			double monthlyValue = valueBigDecimal.doubleValue();
			String usageData = null;
			if (monthlyValue > 0) {
				usageData = monthlyValue + "";
			}

			if (monthMap.containsKey(autonomousType)) {
				monthMap.get(autonomousType)
						.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList
						.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(autonomousType, periodicUsageList);
			}
		}

		// GET THT WEEK USAGE DATA
		results = autonomousDAO.getWeeklyByAutonomous();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String autonomousType = (String) arr[0];
			BigDecimal fiscalWeek = (BigDecimal) arr[1];

			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
			double weeklyValue = valueBigDecimal.doubleValue();
			String usageData = null;
			if (weeklyValue > 0) {
				usageData = weeklyValue + "";
			}

			if (weekMap.containsKey(autonomousType)) {
				weekMap.get(autonomousType).add(new PeriodicUsage(fiscalWeek + "", usageData));
			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek + "", usageData));
				weekMap.put(autonomousType, periodicUsageList);
			}

		}
		
		// Get the list of product type
		for (String key : quatermap.keySet()) {

			// get the list of quarters using autonomousType
			List<PeriodicUsage> qList = quatermap.get(key);
			List<PeriodicUsage> dummyList = Arrays.asList(new PeriodicUsage("Q1", "0"), new PeriodicUsage("Q2", "0"),
					new PeriodicUsage("Q3", "0"), new PeriodicUsage("Q4", "0"));
			for(PeriodicUsage dummy:dummyList){
				if(!qList.contains(dummy)){
					qList.add(dummy);
				}
			}
		}

		return usageMap;
	}

	public HashMap<String, Object> getKPI(String autonomousType, String periodType, String periodValue) {
		HashMap<String, Object> contractTypeandLOBMap = new HashMap<String, Object>();
		if(autonomousType.equalsIgnoreCase("all")) {
			autonomousType = "Autonomous";
		}
		List<UsageBean> lobList = getContractType(autonomousType, periodType, periodValue);
		List<UsageBean> productsList = getLOB(autonomousType, periodType, periodValue);
		List<UsageBean> autonomousList = getAutonomous(autonomousType, periodType, periodValue);
		contractTypeandLOBMap.put("contractType", lobList);
		contractTypeandLOBMap.put("lob", productsList);
		contractTypeandLOBMap.put("autonomous", autonomousList);
		return contractTypeandLOBMap;
	}

	private List<UsageBean> getAutonomous(String autonomousType, String periodType, String periodValue) {
		List<Object[]> results = autonomousDAO.getAutonomous(autonomousType, periodType, periodValue);
		HashMap<String, Double> autonomousTypeMap = new HashMap<String, Double>();
		List<UsageBean> finalContractList = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double productValue = 0.0;
			Double productTarget = 0.0;
			Object[] arr = results.get(i);
			UsageBean bean = new UsageBean();
			for (int j = 0; j < arr.length; j++) {
				if (j == 0) {
					key = (String) arr[j];
					bean.setName(key);
				} else if (j == 1) {
					if (arr[j] != null) {
						BigDecimal valueBigDecimal = (BigDecimal) arr[j];
						productValue = valueBigDecimal.doubleValue();
						if(productValue < 0) {
							productValue = 0.0;
						}						
					} else {
						productValue = 0.0;
					}
					bean.setActual(productValue + "");
				} else if (j == 2) {
					if (arr[j] != null) {
						BigDecimal valueBigDecimal = (BigDecimal) arr[j];
						productTarget = valueBigDecimal.doubleValue();
					} else {
						productTarget = 0.0;
					}
					bean.setTarget((productTarget) + "");
				}
			}
			finalContractList.add(bean);
		}
		return finalContractList;
	}

	public List<UsageBean> getContractType(String autonomousType, String periodType, String periodValue) {
		List<Object[]> results = autonomousDAO.getContractType(autonomousType, periodType, periodValue);
		HashMap<String, Double> autonomousTypeMap = new HashMap<String, Double>();
		List<UsageBean> finalContractList = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double productValue = 0.0;
			Double productTarget = 0.0;
			Object[] arr = results.get(i);
			UsageBean bean = new UsageBean();
			for (int j = 0; j < arr.length; j++) {
				if (j == 0) {
					key = (String) arr[j];
					bean.setName(key);
				} else if (j == 1) {
					if (arr[j] != null) {
						BigDecimal valueBigDecimal = (BigDecimal) arr[j];
						productValue = valueBigDecimal.doubleValue();
						if(productValue < 0) {
							productValue = 0.0;
						}						
					} else {
						productValue = 0.0;
					}
					bean.setActual(productValue + "");
				} else if (j == 2) {
					if (arr[j] != null) {
						BigDecimal valueBigDecimal = (BigDecimal) arr[j];
						productTarget = valueBigDecimal.doubleValue();
					} else {
						productTarget = 0.0;
					}
					bean.setTarget((productTarget) + "");
				}
			}
			finalContractList.add(bean);
		}
		return finalContractList;
	}

	public List<UsageBean> getLOB(String autonomousType, String periodType, String periodValue) {
		List<Object[]> results = autonomousDAO.getLOB(autonomousType, periodType, periodValue);
		HashMap<String, Double> autonomousTypeMap = new HashMap<String, Double>();
		List<UsageBean> finalLOBList = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double productValue = 0.0;
			Double productTarget = 0.0;
			Object[] arr = results.get(i);
			UsageBean bean = new UsageBean();
			for (int j = 0; j < arr.length; j++) {
				if (j == 0) {
					key = (String) arr[j];
					bean.setName(key);
				} else if (j == 1) {
					if (arr[j] != null) {
						BigDecimal valueBigDecimal = (BigDecimal) arr[j];
						productValue = valueBigDecimal.doubleValue();
						if(productValue < 0) {
							productValue = 0.0;
						}						
					} else {
						productValue = 0.0;
					}
					bean.setActual(productValue + "");
				} else if (j == 2) {
					if (arr[j] != null) {
						BigDecimal valueBigDecimal = (BigDecimal) arr[j];
						productTarget = valueBigDecimal.doubleValue();
					} else {
						productTarget = 0.0;
					}
					bean.setTarget((productTarget) + "");
				}
			}
			finalLOBList.add(bean);
		}
		return finalLOBList;
	}
}
