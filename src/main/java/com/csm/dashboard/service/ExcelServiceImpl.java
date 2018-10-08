package com.csm.dashboard.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
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
	
	public boolean uploadExcelFile(MultipartFile multipartFile) {
		
		InputStream inputStream;
		try {
			inputStream = multipartFile.getInputStream();
			List<Consumption>  consumptions= DataReader.GetConsumptionData(inputStream);
			System.out.println(consumptions.size()+" records found");
			//purgeTable();
			return saveAllData(consumptions);
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		} catch(Throwable e) {
			System.out.println(e.getMessage());
			return false;
		}

		//return false;
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
