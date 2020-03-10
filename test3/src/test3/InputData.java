package test3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;



//import test3.Test3.stationInfo;

public class InputData {

	Workbook workbook = null;

	String filePath;
	ArrayList<stationInfo> allClimateData = new ArrayList<>();
	ArrayList<animalInfo> allAnimalData = new ArrayList<>();
	
	class stationInfo{
		String state;
		String county;
		String name; 
		String[] data;
		
		stationInfo(String s, String c, String n, String[] d ){
			state = s;
			county = c;
			name = n;
			data = d;
		}
	}
	
	class animalInfo{
		String name;
		String type;
		String dataSource; 
		String[] data;
		int index;
		
		
		animalInfo(String n, String t, String s, String[] d, int i ){
			name = n;
			type = t;
			dataSource = s;
			data = d;
			index = 0;
		}
	}
	
	public InputData(String path) throws IOException {
		filePath = path;
		InputStream fis = new FileInputStream(filePath);
		
		if (filePath.toLowerCase().endsWith("xlsx")) {
           workbook = new XSSFWorkbook(fis);
        } else if (filePath.toLowerCase().endsWith("xls")) {
           workbook = new HSSFWorkbook(fis);
        }
	}
	
	public void readClimateSheet(String sheetName) {
		
    	try {    		
    	    Sheet sheet = workbook.getSheet(sheetName);	     
    	    Row row;   	      	     
    	    for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
    	    	row = sheet.getRow(i); 	
    	    	stationInfo element;
    	    	String state = row.getCell(0).toString();
	    		String county = row.getCell(1).toString();
	    		String name = row.getCell(2).toString();
	    		String[] data = new String[row.getLastCellNum()-3];
    	    	for(int j = 3; j <row.getLastCellNum(); j ++) {
    	    		data[j-3] = row.getCell(j).toString();	    		
    	    	}
    	    	element = new stationInfo(state, county, name, data);
    	    	allClimateData.add(element);   	    	 
    	     }	
    	    
    	    
    	}catch(Exception e) {
    		 e.printStackTrace();
    	}  	
	}
	
	public ArrayList<stationInfo> filterByState(String stateName, ArrayList<stationInfo> upperLevelData) {
		ArrayList<stationInfo> list = new ArrayList<>();
		for(stationInfo ele : upperLevelData) {
			if(ele.state.equals(stateName)) {
				list.add(ele);
			}
		}	
		return list;
	}
	
	public void readAnimalSheet(String sheetName) {
		
    	try {    		
    	    Sheet sheet = workbook.getSheet(sheetName);	     
    	    Row row;   	      	     
    	    for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
    	    	row = sheet.getRow(i); 	
    	    	animalInfo element;
    	    	String name = row.getCell(0).toString();
	    		String type = row.getCell(1).toString();
	    		String dataSource = row.getCell(2).toString();
	    		String[] data = new String[row.getLastCellNum()-3];
    	    	for(int j = 3; j <row.getLastCellNum(); j ++) {
    	    		data[j-3] = row.getCell(j).toString();	    		
    	    	}
    	    	element = new animalInfo(name, type, dataSource, data,0);
    	    	allAnimalData.add(element);   	    	 
    	     }	
    	    
    	    
    	}catch(Exception e) {
    		 e.printStackTrace();
    	}  	
	}
	public ArrayList<animalInfo> filterByDataSource(String ds, ArrayList<animalInfo> data) {
		ArrayList<animalInfo> list = new ArrayList<>();
		for(animalInfo a : data) {
			if(a.dataSource.equals(ds)) {
				list.add(a);
			}
		}		
		return list;
	}
	



	
}