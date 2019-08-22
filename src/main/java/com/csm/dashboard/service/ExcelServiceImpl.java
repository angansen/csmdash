package com.csm.dashboard.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.csm.dashboard.dao.ConsumptionDao;
import com.csm.dashboard.model.Consumption;
import com.csm.dashboard.model.DataReader;

@Service
@Transactional
public class ExcelServiceImpl {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	ConsumptionDao dao;
	
	public HashMap<String, String> uploadExcelFile(MultipartFile multipartFile) throws IOException {
		HashMap<String, String> uploadStatus = new HashMap<String, String>(); 
		InputStream inputStream;
		HashMap<String, Object> consumptionDataMap = null;
		try{
			inputStream = multipartFile.getInputStream();
			//List<Consumption>  consumptions= DataReader.GetConsumptionData(inputStream);
			consumptionDataMap = DataReader.GetConsumptionData(inputStream);
		}catch(Exception e){
			System.out.println("Exception : "+e);
			uploadStatus.put("status", "false");
			uploadStatus.put("message", "Only xslx format is accepted");
			return uploadStatus;
		}
		if(!(Boolean) consumptionDataMap.get("status")){
			uploadStatus.put("status", "false");
			uploadStatus.put("message", (String) consumptionDataMap.get("message"));
			return uploadStatus;
		}
		boolean saveStatus = saveAllData((List<Consumption>) consumptionDataMap.get("consumption"));
		if(saveStatus){
			uploadStatus.put("status", "true");
			uploadStatus.put("message", "Successfully inserted data");
		}else {
			uploadStatus.put("status", "true");
			uploadStatus.put("message", "Error while inserting data");
		}
		return uploadStatus;
			
	}

	@Modifying(clearAutomatically = true)
	private void purgeTable() {
		String sql="truncate table USAGE_DATA";
		
		int deleteCount=entityManager.createNativeQuery(sql).executeUpdate();
		
		System.out.println("Deleted records : "+deleteCount);
	}

	public boolean saveAllData(List<Consumption> usage) {
		purgeTable();
		String started=Calendar.getInstance().getTime().toString();
		int flag = 1; 
		for (Consumption consumption : usage) {
			try {
			Consumption savedObj=dao.save(consumption);
			System.out.println(savedObj);
			
			}catch (Throwable e) {
				System.out.println("Record insert error: "+e.getMessage());
				flag = 0;
			}
		}
		
		System.out.println("#########################################################");
		System.out.println("Job Started       @ "+started);
		System.out.println("Job Completed @ "+Calendar.getInstance().getTime().toString());
		System.out.println("#########################################################");
		if (flag ==1){
			return true;
		}
		return false;
	}
}
