package com.csm.dashboard.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csm.dashboard.service.LOBServiceImpl;

@RestController
@CrossOrigin
public class LOBController {
	
	@Autowired
	LOBServiceImpl lobServiceImpl; 
	
	@GetMapping(path="/lob/periodic")
	public HashMap<String, Object> periodicData(){
		return lobServiceImpl.usageMap();
	}	
	
	@GetMapping(path="/lob/kpi/{lobType}")
	public HashMap<String, Object> kpiData(@PathVariable("lobType") String lobType,
			@RequestParam("periodType") String periodType, @RequestParam("periodValue") String periodValue){
		return lobServiceImpl.getKPI(lobType, periodType, periodValue);
	}	

}
