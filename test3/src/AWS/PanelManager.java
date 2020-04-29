package AWS;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Entity.AnimalInfo;
import Entity.ClimateInfo;


public class PanelManager {
	

	public Workbook workbook = null;	
	public ArrayList<ClimateInfo> allClimateData = new ArrayList<>();
	public ArrayList<AnimalInfo> allAnimalData = new ArrayList<>();
	
	public String[] outputOfStartPanel = new String[2];
	
	
	public PanelManager(String path) throws IOException {
		InputStream fis = new FileInputStream(path);		
		if (path.toLowerCase().endsWith("xlsx")) {
           workbook = new XSSFWorkbook(fis);
        } else if (path.toLowerCase().endsWith("xls")) {
           workbook = new HSSFWorkbook(fis);
        }
		readClimateDataset("Climate");
		readAnimalDataset("Animal");
	}
	
	
	public void readClimateDataset(String sheetName) {
		try {    		
    	    Sheet sheet = workbook.getSheet(sheetName);	     
    	    Row row;   	      	     
    	    for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
    	    	row = sheet.getRow(i); 	
    	    	ClimateInfo element;
    	    	String state = row.getCell(0).toString();
	    		String county = row.getCell(1).toString();
	    		String name = row.getCell(2).toString();
	    		String[] data = new String[row.getLastCellNum()-3];
    	    	for(int j = 3; j <row.getLastCellNum(); j ++) {
    	    		data[j-3] = row.getCell(j).toString();	    		
    	    	}
    	    	element = new ClimateInfo(state, county, name, data);
    	    	allClimateData.add(element);   	    	 
    	     }	    	       	    
    	}catch(Exception e) {
    		 e.printStackTrace();
    	}  			
	}	
	
	public void readAnimalDataset(String sheetName){
		try {    		
    	    Sheet sheet = workbook.getSheet(sheetName);	     
    	    Row row;   	      	     
    	    for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
    	    	row = sheet.getRow(i); 	
    	    	AnimalInfo element;
    	    	String name = row.getCell(0).toString();
	    		String type = row.getCell(1).toString();
	    		String dataSource = row.getCell(2).toString();
	    		String[] data = new String[row.getLastCellNum()-3];
    	    	for(int j = 3; j <row.getLastCellNum(); j ++) {
    	    		data[j-3] = row.getCell(j).toString();	    		
    	    	}
    	    	element = new AnimalInfo(name, type, dataSource, data,0);
    	    	allAnimalData.add(element);   	    	 
    	     }	    	        	    
    	}catch(Exception e) {
    		 e.printStackTrace();
    	}  		
	}
	
	
	/***
	 * Manage start panel
	 ***/
	// get data filtered by the state 
	public ArrayList<ClimateInfo> filterByState(String name, ArrayList<ClimateInfo> upperLevelData) {
		ArrayList<ClimateInfo> list = new ArrayList<>();
		for(ClimateInfo ele : upperLevelData) {
			if(ele.state.equals(name)) {
				list.add(ele);
			}
		}	
		return list;
	}
	
	/*public HashSet<String> getSet(ArrayList<ClimateInfo> data) {
		HashSet<String> allStateNames = new HashSet();
    	allStateNames.add(" ");  	
    	for(ClimateInfo element : data) {  		
    		allStateNames.add(element.state);   		
    	}
    	return allStateNames;
	}*/
	// store output data
	public void storeStartPanelOutput(String[] s) {
		outputOfStartPanel = s;
	}
	
	
	
	
	/***
	 * manage climate panel
	 ***/
	
	// build a hashmap to store the county and its stations
	/*public HashMap<String, ArrayList<String>> getMap(ArrayList<ClimateInfo> data){
		HashMap<String, ArrayList<String>> countyStationMap = new HashMap<>();  
		for(ClimateInfo element : data) { 			
			String key = element.county;
			if(!countyStationMap.containsKey(key)) {
				ArrayList<String> value = new ArrayList<>();
				value.add(element.name);
	    		countyStationMap.put(key, value);   	
			}
			else
				countyStationMap.get(key).add(element.name);				
		}
		return countyStationMap; 
	}*/
	//
	public ArrayList<ClimateInfo> filterByCounty(String name, ArrayList<ClimateInfo> upperLevelData) {
		ArrayList<ClimateInfo> list = new ArrayList<>();
		for(ClimateInfo ele : upperLevelData) {
			if(ele.county.equals(name)) {
				list.add(ele);
			}
		}	
		return list;
	}
	
	
	/***
	 * manage animal panel
	 ***/
	
	public ArrayList<AnimalInfo> filterByDataSource(String ds, ArrayList<AnimalInfo> data) {
		ArrayList<AnimalInfo> list = new ArrayList<>();
		for(AnimalInfo a : data) {
			if(a.dataSource.equals(ds)) {
				list.add(a);
			}
		}		
		return list;
	}
	
	/*public String[] getTypes(ArrayList<AnimalInfo> data) {
		ArrayList<String> list = new ArrayList<>();
		for(AnimalInfo a : data) {
			if(list.contains(a.type)) {
				continue;
			}
			else
				list.add(a.type);
		}		
		
		String[] sList = new String[list.size()];
		for(int i = 0; i < list.size();i++) {
			sList[i] = list.get(i);
		}
		Arrays.sort(sList);	
		return sList;

	}*/

}
