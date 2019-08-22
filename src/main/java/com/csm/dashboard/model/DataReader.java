package com.csm.dashboard.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataReader {

	public static HashMap<String, Object> GetConsumptionData(InputStream inputStream) throws IOException {
		Boolean status = true;
		String message = "";
		HashMap<String, Object> consumptionDataReadStatus = new HashMap<String, Object>(); 
		
		Workbook workbook = new XSSFWorkbook(inputStream);
		String updateDate="";
		
//		DATA SHEET
		Sheet datatypeSheet = workbook.getSheetAt(0);
		
//		DATE SHEET
		Sheet dateSheet=workbook.getSheetAt(1);
		
//		GET LATEST DATA 
		Iterator<Row> dateIterator = dateSheet.iterator();
		
//		 GET DATA FROM THE SHEET TWO
		while (dateIterator.hasNext()) {
			Row row=dateIterator.next();
			Cell cell=row.getCell(row.getFirstCellNum());
			updateDate=cell.toString();
			System.out.println("Date: "+updateDate);
		}

		Iterator<Row> iterator = datatypeSheet.iterator();

		List<Consumption> consumptionList = new ArrayList<>();
		//int count = 0;
		while (iterator.hasNext()) {

			Row currentRow = iterator.next();
			if (currentRow.getRowNum() == 0)
				continue;
			System.out.println(" -  - "+currentRow+" -  - ");
			Iterator<Cell> cellIterator = currentRow.iterator();
			Consumption curConsumptionObject = new Consumption();
			curConsumptionObject.setUPDATED_DATE(updateDate);
			while (cellIterator.hasNext()) {
				Cell currentCell = cellIterator.next();
				// getCellTypeEnum shown as deprecated for version 3.15
				// getCellTypeEnum ill be renamed to getCellType starting from version 4.0
				int cellNumber = currentCell.getColumnIndex();
				String celltype = currentCell.getCellTypeEnum().name();
				try{
					switch (cellNumber) {
					case 0:
						curConsumptionObject.setFISCAL_QUATER(currentCell.getStringCellValue());
						break;
					case 1:
						curConsumptionObject.setFISCAL_WEEK_IN_QUATER((int) currentCell.getNumericCellValue());
						break;
					case 2:
						curConsumptionObject.setFISCAL_MONTH_IN_QUATER(currentCell.getStringCellValue());
						break;
					case 3:
						curConsumptionObject.setCONTRACT_TYPE(currentCell.getStringCellValue());
						break;
					case 4:
						curConsumptionObject.setMETEREDLOB(currentCell.getStringCellValue());
						break;
					case 5:
						curConsumptionObject.setCHILD_TIER(currentCell.getStringCellValue());
						break;
					case 6:
						curConsumptionObject.setTRUE_USAGE(currentCell.getNumericCellValue());
						break;
					case 7:
						curConsumptionObject.setCONTRACT_TYPE_TARGET(currentCell.getNumericCellValue());
						break;
					case 8:
						curConsumptionObject.setOVERALL_USAGE_TARGET(currentCell.getNumericCellValue());
						break;
					case 9:
						curConsumptionObject.setLOBTARGET(currentCell.getNumericCellValue());
						break;
					default:
						break;
					}
				}
				catch(Exception e){
					message = "Wrong Data at Row "+(currentRow.getRowNum()+1)+" Cell "+(cellNumber+1);
					consumptionDataReadStatus.put("status", false);
					consumptionDataReadStatus.put("message", message);
					return consumptionDataReadStatus;
				}

			}
			consumptionList.add(curConsumptionObject);
			
		}
		consumptionDataReadStatus.put("consumption", consumptionList);
		consumptionDataReadStatus.put("status", status);
		
		return consumptionDataReadStatus;
	}
}
