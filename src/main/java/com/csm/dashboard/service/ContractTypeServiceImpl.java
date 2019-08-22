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

import com.csm.dashboard.dao.ContractTypeDAOImpl;
import com.csm.dashboard.dao.DashboardDAOImpl;
import com.csm.dashboard.model.PeriodicUsage;
import com.csm.dashboard.model.UsageBean;

@Service
public class ContractTypeServiceImpl implements ContractTypeService{
	
	@Autowired
	ContractTypeDAOImpl contractTypeDAO;
	
	@Autowired
	DashboardDAOImpl dashBoardDAO;
	
	private String getMonthName(int monthNumber) {
		String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}; 
		return months[monthNumber-1];
	}
	
	public HashMap<String, Object> usageMap () {
		HashMap<String,Object> usageMap = new HashMap<String,Object>();
		TreeSet<String> contractTypeSet = new TreeSet<String>();
		LinkedHashSet<String> contractTypeLinkedHashSet = new LinkedHashSet<String>();

		usageMap.put("quater", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("month", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("week", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("year", getYearlyDataMap("contract"));
		usageMap.put("dropdown", contractTypeLinkedHashSet);
		
		List<Object[]> results=null;

		// Get the Quater map
		HashMap<String, List<PeriodicUsage>> quatermap=(HashMap<String, List<PeriodicUsage>>) usageMap.get("quater");
		HashMap<String, List<PeriodicUsage>> monthMap=(HashMap<String, List<PeriodicUsage>>)usageMap.get("month");
		HashMap<String, List<PeriodicUsage>> weekMap=(HashMap<String, List<PeriodicUsage>>)usageMap.get("week");

		List<String> quaterList = new ArrayList<String>();
	
		// GET THT QUATER USAGE DATA
		results = contractTypeDAO.getQuaterlyByContractType();
		
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = (String) arr[0];
			String fiscalQuater = ((String) arr[1]).replace("FY20-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
		    
			String usageData =   weeklyValue+"";
			contractTypeSet.add(contractType);
			if(quatermap.containsKey(contractType)){
				quatermap.get(contractType).add(new PeriodicUsage(fiscalQuater, usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater,usageData));
				quatermap.put(contractType, periodicUsageList);			
			}
		}

		contractTypeLinkedHashSet.add("All");
		contractTypeLinkedHashSet.addAll(contractTypeSet);

		// GET THT MONTH USAGE DATA
		results = contractTypeDAO.getMonthlyByContractType();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = (String) arr[0];
			String fiscalMonth =  (arr[1]+"");
			
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(monthMap.containsKey(contractType)) {
				monthMap.get(contractType).add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(contractType, periodicUsageList);			
			}
		}
		
		// GET THT WEEK USAGE DATA
		results = contractTypeDAO.getWeeklyByContractType();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = (String) arr[0];
			BigDecimal fiscalWeek = (BigDecimal) arr[1];
		    
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(weekMap.containsKey(contractType)){
				weekMap.get(contractType).add(new PeriodicUsage(fiscalWeek+"", usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek+"",usageData));
				weekMap.put(contractType, periodicUsageList);			
			}
		}
		
		// GET THT QUATER USAGE DATA All CONTRACT
		results = contractTypeDAO.getQuaterly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = "All";
			String fiscalQuater = ((String) arr[0]).replace("FY20-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(quatermap.containsKey(contractType)){
				quatermap.get(contractType).add(new PeriodicUsage(fiscalQuater, usageData));
				
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater,usageData));
				quatermap.put(contractType, periodicUsageList);			
			}
		}
		
		// GET THT MONTH USAGE DATA All CONTRACT
		results = contractTypeDAO.getMonthly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = "All";
			String fiscalMonth = arr[0]+"";

			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(monthMap.containsKey(contractType)){
				monthMap.get(contractType).add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(contractType, periodicUsageList);			
			}
		}
		
		// GET THT WEEK USAGE DATA All CONTRACT
		results = contractTypeDAO.getWeekly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = "All";
			String fiscalWeek = arr[0]+"";
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(weekMap.containsKey(contractType)){
				weekMap.get(contractType).add(new PeriodicUsage(fiscalWeek, usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek,usageData));
				weekMap.put(contractType, periodicUsageList);			
			}
		}

		//Below code adds quater data which are not present yet , for ex Q3,Q4 as of Sep
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

	@Override
	public HashMap<String, Object> getKPI(String contractType, String periodType, String periodValue) {
			HashMap<String, Object> lobAndProductsMap = new HashMap<String,Object>();
			List<UsageBean> lobList = getLOB(contractType, periodType, periodValue);
			List<UsageBean> productsList = getProducts(contractType, periodType, periodValue);
			lobAndProductsMap.put("lob", lobList);
			lobAndProductsMap.put("products", productsList);
			return lobAndProductsMap;
	}
	
	public List<UsageBean> getLOB(String contractType, String periodType, String periodValue) {
		List<Object[]> results = contractTypeDAO.getLOB(contractType, periodType, periodValue);
		HashMap<String, Double> lobMap = new HashMap<String, Double>();
		List<UsageBean> finalProductList=new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double lobValue = 0.0;
			Double lobTarget = 0.0;
			Object[] arr = results.get(i);
			UsageBean bean=new UsageBean();
			for (int j = 0; j < arr.length; j++) {
				if(j==0){
					key = (String) arr[j];
					bean.setName(key);
				}
				else if (j==1){
					if(arr[j] != null){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    lobValue = valueBigDecimal.doubleValue();
						if(lobValue < 0) {
							lobValue = 0.0;
						}						
					}else{
						lobValue = 0.0;
					}
				    bean.setActual(lobValue+"");
				}
				else if (j==2){
					if(arr[j] != null){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    lobTarget = valueBigDecimal.doubleValue();
					}else{
						lobTarget = 0.0;
					}
				    bean.setTarget((lobTarget)+"");
				}				
			}
			finalProductList.add(bean);
		}
		return finalProductList;
	}
	
	public List<UsageBean> getProducts(String contractType, String periodType, String periodValue) {
		List<Object[]> results = contractTypeDAO.getProducts(contractType, periodType, periodValue);
		HashMap<String, Double> productsMap = new HashMap<String, Double>();
		List<UsageBean> finalProductList=new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double productValue = 0.0;			
			Object[] arr = results.get(i);
			UsageBean bean=new UsageBean();
			for (int j = 0; j < arr.length; j++) {
				if(j==0){
					key = (String) arr[j];
					bean.setName(key);
				}
				
				else if (j==1){
					if(arr[j] != null){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    productValue = valueBigDecimal.doubleValue();
						if(productValue < 0) {
							productValue = 0.0;
						}						
					}else{
						productValue = 0.0;
					}
				    bean.setActual(productValue+"");
				}
			}
			if (Double.parseDouble(bean.getActual()) > 0)
				finalProductList.add(bean);
		}
		return finalProductList;
	}

	public HashMap<String, List<PeriodicUsage>> getYearlyDataMap(String kpiType) {
		HashMap<String, List<PeriodicUsage>> yearMap= new HashMap<String, List<PeriodicUsage>>();
		List<Object[]> results = contractTypeDAO.getYearlyByContractType(kpiType);
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = (String) arr[0];
			double yearlyValue = 0.0;
			if(arr[1]!=null) {
				BigDecimal valueBigDecimal = (BigDecimal) arr[1];
			    yearlyValue = valueBigDecimal.doubleValue();
		    }			
			String usageData =   yearlyValue+"";
			List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
			periodicUsageList.add(new PeriodicUsage("FY-20", usageData));
			yearMap.put(contractType, periodicUsageList);
		}

		// GET THT YEAR USAGE DATA All CONTRACT
		if (!kpiType.equalsIgnoreCase("autonomous")) {
			double yearlyVal = (double) dashBoardDAO.getYearly();
			List <PeriodicUsage> periodicUsageList2=new ArrayList<PeriodicUsage>();
			periodicUsageList2.add(new PeriodicUsage("FY-20", yearlyVal+""));
			yearMap.put("All", periodicUsageList2);
		}
		return yearMap;
	}

}
