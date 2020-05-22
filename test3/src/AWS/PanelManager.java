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
import Entity.BeddingInfo;
import Entity.ClimateInfo;
import Entity.OutputOfAnimalPanel;


public class PanelManager {
	
	public Workbook workbook = null;
	
	// for input data
	public ArrayList<ClimateInfo> allClimateData = new ArrayList<>();
	public ArrayList<AnimalInfo> allAnimalData = new ArrayList<>();
	public ArrayList<BeddingInfo> allBeddingData = new ArrayList<>();
	
	
	// for output of each panel
	public String[] startPanelOutput = new String[2];
	public ArrayList<String> climatePanelOutout;
	public ArrayList<OutputOfAnimalPanel> animalPanelOutput;
	public ArrayList<String> locationPanelOutput;
	
	
	public PanelManager(String path) throws IOException {
		InputStream fis = new FileInputStream(path);		
		if (path.toLowerCase().endsWith("xlsx")) {
           workbook = new XSSFWorkbook(fis);
        } else if (path.toLowerCase().endsWith("xls")) {
           workbook = new HSSFWorkbook(fis);
        }
		readClimateDataset("Climate");
		readAnimalDataset("Animal");
		readBeddingDataset("Bedding");
	}
	
	// reading data from climate
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
	
	// reading data from animal
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
	
	// reading data from bedding
	public void readBeddingDataset(String sheetName){
		try {    		
    	    Sheet sheet = workbook.getSheet(sheetName);	     
    	    Row row;   	      	     
    	    for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
    	    	row = sheet.getRow(i); 	
    	    	BeddingInfo element;
    	    	String name = row.getCell(0).toString();
	    		String density = row.getCell(1).toString();
	    		String eff_Density = row.getCell(2).toString();
	    		String state = row.getCell(3).toString();
    	    	
    	    	element = new BeddingInfo(name, density, eff_Density, state);
    	    	allBeddingData.add(element);   	    	 
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

	// store output data
	public void storeStartPanelOutput(String[] s) {
		startPanelOutput = s;
	}
	
	
	
	
	/***
	 * manage climate panel
	 ***/
	public ArrayList<ClimateInfo> filterByCounty(String name, ArrayList<ClimateInfo> upperLevelData) {
		ArrayList<ClimateInfo> list = new ArrayList<>();
		for(ClimateInfo ele : upperLevelData) {
			if(ele.county.equals(name)) {
				list.add(ele);
			}
		}	
		return list;
	}
	
	// store output data
	public void storeClimatePanelOutput(ArrayList<String> list) {
		climatePanelOutout = list;
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
	
	// store output data
	public void storeAnimalPanelOutput(ArrayList<OutputOfAnimalPanel> o) {
		animalPanelOutput = o;
	}

	
	/***
	 * manage location panel
	 ***/
	public ArrayList<AnimalInfo> getDataFromAnimalPanel(){
		ArrayList<AnimalInfo> list = new ArrayList<>();
		for(OutputOfAnimalPanel ele : animalPanelOutput) {
			list.add(ele.aniInfo);
		}				
		return list;
	}

	// store output data
	public void storeLocationPanelOutput(ArrayList<String> o) {
		locationPanelOutput = o;
	}
	/***
	 * manage Addition panel
	 ***/
	
	
	
	/***
	 * manage runoff panel
	 ***/
	
	
	/***
	 * manage management panel
	 */
	
	
}
