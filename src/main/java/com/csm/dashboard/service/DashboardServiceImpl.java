package com.csm.dashboard.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csm.dashboard.dao.DashboardDAOImpl;
import com.csm.dashboard.dao.UserRepository;
import com.csm.dashboard.exception.UnathorizedException;
import com.csm.dashboard.model.CSMUser;
import com.csm.dashboard.model.PeriodicUsage;
import com.csm.dashboard.model.UsageBean;
import com.csm.dashboard.model.UsageData;

@Service
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired
	private DashboardDAOImpl dashboardDAO;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public HashMap<String, Object> getOverallUsageData() {
		/* Fetch FYOverallCloudConsumption Start*/
			double trueUsage = 0.0;
			double totalUsage = 0.0;
			List<UsageData> listUsageData = dashboardDAO.getAllUsageDataStarter();
			for(UsageData object : listUsageData) {
				trueUsage+=object.getTrueUsage();
				if(object.getId()==1)
					totalUsage = (double) object.getOverallUsageTarget();
			}
			List<Double> list = new ArrayList<Double>();
			list.add(trueUsage);
			list.add(totalUsage);
			double usagePercentage = trueUsage / totalUsage * 100;
			String usagePercentageString = new DecimalFormat("##.##").format(usagePercentage)+"";
			String trueUsageString = new DecimalFormat("##.##").format((trueUsage/1000000)) + " M";
			String totalUsageString = new DecimalFormat("##.##").format((totalUsage/1000000)) + " M";
			HashMap<String, String> overAllUsageMap = new HashMap<String,String>();
			HashMap<String, Object> overAllCloudConsumption = new HashMap<String,Object>();
			HashMap<String, Object> usageMap = new HashMap<String,Object>();
			overAllUsageMap.put("percentage", usagePercentageString);
			overAllUsageMap.put("actual", trueUsageString);
			overAllUsageMap.put("total", totalUsageString);
			usageMap.put("usage", overAllUsageMap);
			usageMap.put("type", "OverAll");
			overAllCloudConsumption.put("FYOverallCloudConsumption", usageMap);
			/* Fetch FYOverallCloudConsumption End*/
			
			/* Fetch contractTypeOverallUsage Start */
			//List<UsageData> list = query.getResultList();
			List<Object[]> results = dashboardDAO.getContractTypeData();
			HashMap<String, Object> contractTypeOverAllUsageMap = new HashMap<String,Object>();
			
			List<Object> contractTypeList = new ArrayList<Object>();
			for (int i = 0; i < results.size(); i++) {
				String key = null;
				Double actualContractType = 0.0;
				Double totalContractType = 0.0;			
				Object[] arr = results.get(i);
				for (int j = 0; j < arr.length; j++) {
					if(j==0){
						key = (String) arr[j];
					}
					else if (j==1){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    actualContractType = valueBigDecimal.doubleValue();
					}
					else if (j==2){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    totalContractType = valueBigDecimal.doubleValue();
					}
				}
				HashMap<String,String> contractTypeIndividualData = new HashMap<String,String>();
				HashMap<String, Object> overAllRestMap = new HashMap<String,Object>();
				double CTUsagePercentage = actualContractType / totalContractType * 100;
				String CTPercetageString = new DecimalFormat("##.##").format(CTUsagePercentage)+"";
				String CTTrueUsageString = new DecimalFormat("##.##").format((actualContractType/1000000)) + " M";
				String CTTotalUsageString = new DecimalFormat("##.##").format((totalContractType/1000000)) + " M";
				contractTypeIndividualData.put("actual", CTTrueUsageString);
				contractTypeIndividualData.put("total", CTTotalUsageString);
				contractTypeIndividualData.put("percetage", CTPercetageString);
				overAllRestMap.put("usage", contractTypeIndividualData);
				overAllRestMap.put("type", key);
				contractTypeList.add(overAllRestMap);
				
			}
			contractTypeOverAllUsageMap.put("data", contractTypeList);
			/* Fetch contractTypeOverallUsage End */
			
			overAllCloudConsumption.put("contractTypeOverallUsage", contractTypeOverAllUsageMap);
			return overAllCloudConsumption;
	}

	@Override
	public HashMap<String, Object> getLOBAndProducts() {
			HashMap<String, Object> lobAndProductsMap = new HashMap<String,Object>();
			List<UsageBean> lobList = getLOB();
			List<UsageBean> productsList = getProducts();
			lobAndProductsMap.put("lob", lobList);
			lobAndProductsMap.put("products", productsList);
			return lobAndProductsMap;
	}
	
	public List<UsageBean> getLOB() {
		List<Object[]> results = dashboardDAO.getLOB();
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
				    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
				    lobValue = valueBigDecimal.doubleValue();
//				    bean.setActual(new DecimalFormat("##.##").format(lobValue/1000000)+"M");
				    bean.setActual(lobValue+"");
				}
				else if (j==2){
				    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
				    lobTarget = valueBigDecimal.doubleValue();
//				    bean.setTarget((int)(lobTarget/1000000)+"M");
				    bean.setTarget((lobTarget)+"");
				}				
			}
			finalProductList.add(bean);
//			lobMap.put(key, Math.round(lobValue*10.0)/10.0);
		}
		return finalProductList;
	}
	
	public List<UsageBean> getProducts() {
		List<Object[]> results = dashboardDAO.getProducts();
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
				    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
				    productValue = valueBigDecimal.doubleValue();
//				    bean.setActual(new DecimalFormat("##.##").format(productValue/1000000)+"M");
				    bean.setActual(productValue+"");


				}
			}
//			productsMap.put(key, Math.round(productValue*10.0)/10.0);
			if (Double.parseDouble(bean.getActual()) > 0)
				finalProductList.add(bean);
		}
		return finalProductList;
	}

	@Override
	public HashMap<String, Object> getPeriodicUsage() {
			List<PeriodicUsage> quaterlyList = getQuaterlyData();
			List<PeriodicUsage> monthlyList = getMonthlyData();
			List<PeriodicUsage> weeklyList = getWeeklyData();
			HashMap<String, List> quaterDataMap = new HashMap<String, List>();
			HashMap<String, List> monthDataMap = new HashMap<String, List>();
			HashMap<String, List> weekDataMap = new HashMap<String, List>();
			HashMap<String, Object> periodicDataMap = new HashMap<String, Object>();
			quaterDataMap.put("data", quaterlyList);
			monthDataMap.put("data", monthlyList);
			weekDataMap.put("data", weeklyList);
			periodicDataMap.put("quater", quaterDataMap);
			periodicDataMap.put("week", weekDataMap);
			periodicDataMap.put("month", monthDataMap);
			return periodicDataMap;
	}

	private List<PeriodicUsage> getWeeklyData() {
		List<Object[]> results = dashboardDAO.getWeekly();
		List<PeriodicUsage> finalWeekList=new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double weeklyValue = 0.0;
			Object[] arr = results.get(i);
			PeriodicUsage bean=new PeriodicUsage();
			for (int j = 0; j < arr.length; j++) {
				if(j==0){
					key = arr[j]+"";
					bean.setName(key);
				}
				else if (j==1){
				    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
				    weeklyValue = valueBigDecimal.doubleValue();
//				    bean.setValue((new DecimalFormat("##.##").format(weeklyValue/1000000)+"M"));
				    bean.setValue(weeklyValue+"");
				}
			}
			finalWeekList.add(bean);
		}
		return finalWeekList;
	}

	private List<PeriodicUsage> getMonthlyData() {
		List<Object[]> results = dashboardDAO.getMonthly();
		List<PeriodicUsage> finalMonthList=new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double monthlyValue = 0.0;
			Object[] arr = results.get(i);
			PeriodicUsage bean=new PeriodicUsage();
			for (int j = 0; j < arr.length; j++) {
				if(j==0){
					key = arr[j]+"";
					key = key.substring(key.indexOf("-") + 1);
					key = key.substring(0, key.indexOf("-"));
					int monthNo = Integer.parseInt(key);
					bean.setName(getMonthName(monthNo));
				}
				else if (j==1){
				    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
				    monthlyValue = valueBigDecimal.doubleValue();
//				    bean.setValue((new DecimalFormat("##.##").format(monthlyValue/1000000)+"M"));
				    bean.setValue(monthlyValue+"");
				}
			}
			finalMonthList.add(bean);
		}
		return finalMonthList;
	}
	private String getMonthName(int monthNumber) {
		String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}; 
		return months[monthNumber-1];
		
	}
	private List<PeriodicUsage> getQuaterlyData() {
		List<Object[]> results = dashboardDAO.getQuaterly();
		List<PeriodicUsage> finalQuaterList=new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double quaterlyValue = 0.0;
			Object[] arr = results.get(i);
			PeriodicUsage bean=new PeriodicUsage();
			for (int j = 0; j < arr.length; j++) {
				if(j==0){
					key = arr[j]+"";
					bean.setName(key.replace("FY19-", ""));
				}
				else if (j==1){
				    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
				    quaterlyValue = valueBigDecimal.doubleValue();
//				    bean.setValue((new DecimalFormat("##.##").format(quaterlyValue/1000000)+"M"));
				    bean.setValue(quaterlyValue+"");
				}
			}
			finalQuaterList.add(bean);
		}
		for(int i=3; i<5; i++){
			PeriodicUsage bean=new PeriodicUsage();
			bean.setName("Q"+i);
			bean.setValue("0");
			finalQuaterList.add(bean);
		}
		
		return finalQuaterList;
	}
	
	private boolean isAuthorized (String authKeyValue) {
		System.out.println("authKey "+authKeyValue);
		List<CSMUser> userList = userRepository.findAll();
		System.out.println("userList "+userList.size());
		boolean isAuthorized = false;
		if (userList.size() > 0) {
			for (CSMUser csmuser : userList) {
				System.out.println("csmuser.getKey() "+csmuser.getKey());
				if (csmuser.getKey().equals(authKeyValue)) {
					isAuthorized = true;
					break;
				}
			}
		}
		System.out.println("isAuthorized "+isAuthorized);
		return isAuthorized;
	}
}
