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

import com.csm.dashboard.dao.LOBDAOImpl;
import com.csm.dashboard.model.PeriodicUsage;
import com.csm.dashboard.model.UsageBean;

@Service
public class LOBServiceImpl {

	@Autowired
	LOBDAOImpl lobDAO;
	
	@Autowired
	ContractTypeServiceImpl contractService;
	
	private String getMonthName(int monthNumber) {
		String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}; 
		return months[monthNumber-1];
		
	}

	public HashMap<String, Object> usageMap() {
		HashMap<String,Object> usageMap = new HashMap<String,Object>();
		TreeSet<String> lobSet = new TreeSet<String>();
		LinkedHashSet<String> lobLinkedHashSet = new LinkedHashSet<String>();

		usageMap.put("quater", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("month", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("week", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("year", contractService.getYearlyDataMap("lob"));
		usageMap.put("dropdown", lobLinkedHashSet);
		
		List<Object[]> results=null;

		// Get the Quater map
		HashMap<String, List<PeriodicUsage>> quatermap=(HashMap<String, List<PeriodicUsage>>) usageMap.get("quater");
		HashMap<String, List<PeriodicUsage>> monthMap=(HashMap<String, List<PeriodicUsage>>)usageMap.get("month");
		HashMap<String, List<PeriodicUsage>> weekMap=(HashMap<String, List<PeriodicUsage>>)usageMap.get("week");

		List<String> quaterList = new ArrayList<String>();
	
		// GET THT QUATER USAGE DATA
		results = lobDAO.getQuaterlyByLOB();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String lobType = (String) arr[0];
			String fiscalQuater = ((String) arr[1]).replace("FY19-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
		    
			String usageData =   weeklyValue+"";
			lobSet.add(lobType);
			if(quatermap.containsKey(lobType)){
				quatermap.get(lobType).add(new PeriodicUsage(fiscalQuater, usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater,usageData));
				quatermap.put(lobType, periodicUsageList);			
			}
		}

		lobLinkedHashSet.add("All");
		lobLinkedHashSet.addAll(lobSet);
		
		// GET THT MONTH USAGE DATA
		results = lobDAO.getMonthlyByLOB();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String lobType = (String) arr[0];
			String fiscalMonth =  (arr[1]+"");
			
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(monthMap.containsKey(lobType)){
				monthMap.get(lobType).add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(lobType, periodicUsageList);			
			}

		}

		
		
		// GET THT WEEK USAGE DATA
		results = lobDAO.getWeeklyByLOB();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String lobType = (String) arr[0];
			BigDecimal fiscalWeek = (BigDecimal) arr[1];
		    
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(weekMap.containsKey(lobType)){
				weekMap.get(lobType).add(new PeriodicUsage(fiscalWeek+"", usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek+"",usageData));
				weekMap.put(lobType, periodicUsageList);			
			}

		}
		
		// GET THT QUATER USAGE DATA All LOB
		results = lobDAO.getQuaterly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String lobType = "All";
			String fiscalQuater = ((String) arr[0]).replace("FY19-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(quatermap.containsKey(lobType)){
				quatermap.get(lobType).add(new PeriodicUsage(fiscalQuater, usageData));
			
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater,usageData));
				quatermap.put(lobType, periodicUsageList);			
			}

		}
		
		// GET THT MONTH USAGE DATA All LOB
		results = lobDAO.getMonthly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String lobType = "All";
			String fiscalMonth = arr[0]+"";

			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(monthMap.containsKey(lobType)){
				monthMap.get(lobType).add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(lobType, periodicUsageList);			
			}

		}

		
		// GET THT WEEK USAGE DATA All LOB
		results = lobDAO.getWeekly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String lobType = "All";
			String fiscalWeek = arr[0]+"";
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(weekMap.containsKey(lobType)){
				weekMap.get(lobType).add(new PeriodicUsage(fiscalWeek, usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek,usageData));
				weekMap.put(lobType, periodicUsageList);			
			}

		}
		
		/*Set quarters values to 0 which are not present--if query is fetching Q3,Q4..we need to send all quaters data
		in this case; Q1 and Q2 will be defined and set to 0 as part of json*/
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

	public HashMap<String, Object> getKPI(String LOB, String periodType, String periodValue) {
			HashMap<String, Object> contractTypeandProductsMap = new HashMap<String,Object>();
			List<UsageBean> lobList = getContractType(LOB, periodType, periodValue);
			List<UsageBean> productsList = getProducts(LOB, periodType, periodValue);
			contractTypeandProductsMap.put("contractType", lobList);
			contractTypeandProductsMap.put("products", productsList);
			return contractTypeandProductsMap;
	}
	
	public List<UsageBean> getContractType(String LOB, String periodType, String periodValue) {
		List<Object[]> results = lobDAO.getContractType(LOB, periodType, periodValue);
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
	
	public List<UsageBean> getProducts(String LOB, String periodType, String periodValue) {
		List<Object[]> results = lobDAO.getProducts(LOB, periodType, periodValue);
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
//			productsMap.put(key, Math.round(productValue*10.0)/10.0);
			if (Double.parseDouble(bean.getActual()) > 0)
				finalProductList.add(bean);
		}
		return finalProductList;
	}
}
