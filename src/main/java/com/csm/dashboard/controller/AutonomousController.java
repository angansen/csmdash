package com.csm.dashboard.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csm.dashboard.service.AutonomousServiceImpl;

@RestController
@CrossOrigin
public class AutonomousController {
	
	@Autowired
	AutonomousServiceImpl autonomousServiceImpl; 
	
	@GetMapping(path="/autonomous/periodic")
	public HashMap<String, Object> periodicData(){
		return autonomousServiceImpl.usageMap();
	}	
	
	@GetMapping(path="/autonomous/kpi/{autonomousType}")
	public HashMap<String, Object> kpiData(@PathVariable("autonomousType") String autonomousType,
			@RequestParam("periodType") String periodType, @RequestParam("periodValue") String periodValue){
		return autonomousServiceImpl.getKPI(autonomousType, periodType, periodValue);
	}	

}
