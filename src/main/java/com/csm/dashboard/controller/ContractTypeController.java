package com.csm.dashboard.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csm.dashboard.service.ContractTypeServiceImpl;

@RestController
@CrossOrigin
public class ContractTypeController {
	
	@Autowired
	ContractTypeServiceImpl contractTypeServiceImpl; 
	
	@GetMapping(path="/contract/periodic")
	public HashMap<String, Object> periodicData(){
		return contractTypeServiceImpl.usageMap();
	}	
	@GetMapping(path="/contract/kpi")
	public HashMap<String, Object> kpiData(@RequestParam("contractType") String contractType,
			@RequestParam("periodType") String periodType, @RequestParam("periodValue") String periodValue){
		return contractTypeServiceImpl.getKPI(contractType, periodType, periodValue);
	}	

}
