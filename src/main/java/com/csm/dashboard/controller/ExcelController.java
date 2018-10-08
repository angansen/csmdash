package com.csm.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csm.dashboard.service.ExcelServiceImpl;

@RestController
@CrossOrigin
public class ExcelController {
	
	@Autowired
	private ExcelServiceImpl excelService;

	@PostMapping(path="/excel/upload")
	public boolean uploadExcel(@RequestParam("excelfile") MultipartFile multipartFile){
		return excelService.uploadExcelFile(multipartFile);
	}	
}