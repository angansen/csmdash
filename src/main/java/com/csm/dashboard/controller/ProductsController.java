package com.csm.dashboard.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csm.dashboard.service.ProductServiceImpl;

@RestController
@CrossOrigin
public class ProductsController {
	
	@Autowired
	ProductServiceImpl productServiceImpl; 
	
	@GetMapping(path="/product/periodic")
	public HashMap<String, Object> periodicData(){
		return productServiceImpl.usageMap();
	}	
	
	@GetMapping(path="/product/kpi/{productType}")
	public HashMap<String, Object> kpiData(@PathVariable("productType") String productType,
			@RequestParam("periodType") String periodType, @RequestParam("periodValue") String periodValue){
		return productServiceImpl.getKPI(productType, periodType, periodValue);
	}	

}
