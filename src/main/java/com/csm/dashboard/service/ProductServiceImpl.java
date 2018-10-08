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

import com.csm.dashboard.dao.ProductDAOImpl;
import com.csm.dashboard.model.PeriodicUsage;
import com.csm.dashboard.model.UsageBean;

@Service
public class ProductServiceImpl {

	@Autowired
	ProductDAOImpl productDAO;

	@Autowired
	ContractTypeServiceImpl contractService;

	private String getMonthName(int monthNumber) {
		String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		return months[monthNumber - 1];
	}

	public HashMap<String, Object> usageMap() {
		HashMap<String, Object> usageMap = new HashMap<String, Object>();
		TreeSet<String> productSet = new TreeSet<String>();
		LinkedHashSet<String> productLinkedHashSet = new LinkedHashSet<String>();
		usageMap.put("quater", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("month", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("week", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("year", contractService.getYearlyDataMap("product"));
		usageMap.put("dropdown", productLinkedHashSet);

		List<Object[]> results = null;

		// Get the Quater map
		HashMap<String, List<PeriodicUsage>> quatermap = (HashMap<String, List<PeriodicUsage>>) usageMap.get("quater");
		HashMap<String, List<PeriodicUsage>> monthMap = (HashMap<String, List<PeriodicUsage>>) usageMap.get("month");
		HashMap<String, List<PeriodicUsage>> weekMap = (HashMap<String, List<PeriodicUsage>>) usageMap.get("week");

		List<String> quaterList = new ArrayList<String>();

		// GET THT QUATER USAGE DATA
		results = productDAO.getQuaterlyByProduct();

		// List <PeriodicUsage> periodicUsageList1=initializeQuaters();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String productType = (String) arr[0];
			String fiscalQuater = ((String) arr[1]).replace("FY19-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
			double quaterlyValue = valueBigDecimal.doubleValue();

			String usageData = quaterlyValue + "";
			productSet.add(productType);
			List<PeriodicUsage> periodicUsageList1 = new ArrayList<PeriodicUsage>();

			if (quatermap.containsKey(productType)) {
				quatermap.get(productType).add(new PeriodicUsage(fiscalQuater, usageData));

			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater, usageData));
				quatermap.put(productType, periodicUsageList);
			}
		}
		productLinkedHashSet.add("All");
		productLinkedHashSet.addAll(productSet);
		// GET THT MONTH USAGE DATA
		results = productDAO.getMonthlyByProduct();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String productType = (String) arr[0];
			String fiscalMonth = (arr[1] + "");

			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
			double monthlyValue = valueBigDecimal.doubleValue();
			String usageData = null;
			if (monthlyValue > 0) {
				usageData = monthlyValue + "";
			}

			if (monthMap.containsKey(productType)) {
				monthMap.get(productType)
						.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList
						.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(productType, periodicUsageList);
			}
		}

		// GET THT WEEK USAGE DATA
		results = productDAO.getWeeklyByProduct();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String productType = (String) arr[0];
			BigDecimal fiscalWeek = (BigDecimal) arr[1];

			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
			double weeklyValue = valueBigDecimal.doubleValue();
			String usageData = null;
			if (weeklyValue > 0) {
				usageData = weeklyValue + "";
			}

			if (weekMap.containsKey(productType)) {
				weekMap.get(productType).add(new PeriodicUsage(fiscalWeek + "", usageData));
			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek + "", usageData));
				weekMap.put(productType, periodicUsageList);
			}

		}

		// GET THT QUATER USAGE DATA All LOB
		results = productDAO.getQuaterly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String productType = "All";
			String fiscalQuater = ((String) arr[0]).replace("FY19-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
			double quaterlyValue = valueBigDecimal.doubleValue();

			String usageData = null;
			if (quaterlyValue > 0) {
				usageData = quaterlyValue + "";
			}

			if (quatermap.containsKey(productType)) {
				quatermap.get(productType).add(new PeriodicUsage(fiscalQuater, usageData));

			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater, usageData));
				quatermap.put(productType, periodicUsageList);
			}

		}

		// GET THT MONTH USAGE DATA All LOB
		results = productDAO.getMonthly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String productType = "All";
			String fiscalMonth = arr[0] + "";

			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
			double monthlyValue = valueBigDecimal.doubleValue();

			String usageData = null;
			if (monthlyValue > 0) {
				usageData = monthlyValue + "";
			}
			if (monthMap.containsKey(productType)) {
				monthMap.get(productType)
						.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList
						.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(productType, periodicUsageList);
			}

		}

		// GET THT WEEK USAGE DATA All LOB
		results = productDAO.getWeekly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String productType = "All";
			String fiscalWeek = arr[0] + "";
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
			double weeklyValue = valueBigDecimal.doubleValue();

			String usageData = null;
			if (weeklyValue > 0) {
				usageData = weeklyValue + "";
			}
			if (weekMap.containsKey(productType)) {
				weekMap.get(productType).add(new PeriodicUsage(fiscalWeek, usageData));
			} else {
				List<PeriodicUsage> periodicUsageList = new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek, usageData));
				weekMap.put(productType, periodicUsageList);
			}

		}

		// Get the list of product type
		for (String key : quatermap.keySet()) {

			// get the list of quarters using producttype
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

	public HashMap<String, Object> getKPI(String productType, String periodType, String periodValue) {
		HashMap<String, Object> contractTypeandLOBMap = new HashMap<String, Object>();
		if (productType.contains("&")) {
			productType = productType.substring(0, productType.indexOf("&"));
		}
		List<UsageBean> lobList = getContractType(productType, periodType, periodValue);
		List<UsageBean> productsList = getLOB(productType, periodType, periodValue);
		contractTypeandLOBMap.put("contractType", lobList);
		contractTypeandLOBMap.put("lob", productsList);
		return contractTypeandLOBMap;
	}

	public List<UsageBean> getContractType(String productType, String periodType, String periodValue) {
		List<Object[]> results = productDAO.getContractType(productType, periodType, periodValue);
		HashMap<String, Double> productTypeMap = new HashMap<String, Double>();
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

	public List<UsageBean> getLOB(String productType, String periodType, String periodValue) {
		List<Object[]> results = productDAO.getLOB(productType, periodType, periodValue);
		HashMap<String, Double> productTypeMap = new HashMap<String, Double>();
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
