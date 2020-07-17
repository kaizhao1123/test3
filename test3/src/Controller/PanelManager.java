package Controller;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model_Entity.AdditionsPanelOutputElement;
import Model_Entity.AnimalInfo;
import Model_Entity.BeddingInfo;
import Model_Entity.ClimateInfo;
import Model_Entity.LocationPanelOutputElement;
import Model_Entity.SeparatorInfo;
import Model_Entity.AnimalPanelOutputElement;


public class PanelManager {
	
	public Workbook workbook = null;
	
	// the input data structure
	public ArrayList<ClimateInfo> allClimateData = new ArrayList<>();
	public ArrayList<AnimalInfo> allAnimalData = new ArrayList<>();
	public ArrayList<BeddingInfo> allBeddingData = new ArrayList<>();
	public ArrayList<SeparatorInfo> allSeparatorData = new ArrayList<>();
		
	// the output data structures of each panel
	public String[] startPanelOutput = new String[2];
	public ArrayList<String> climatePanelOutout;
	public ArrayList<AnimalPanelOutputElement> animalPanelOutput;
	public ArrayList<LocationPanelOutputElement> locationPanelOutput;
	public ArrayList<AdditionsPanelOutputElement> additionsPanelOutput;
	public ArrayList<String> runoffPanelOutput;
	
	/**
	 * This constructor is used for getting the data from the excel file, 
	 * which includes several sheets in it.
	 * And store the data into different data structure categories corresponding 
	 * to the sheets in the excel file :
	 * climate data, animal data, and bedding data.
	 * 
	 * @param path the location of the database
	 * @throws IOException
	 * @see readCLimateDataset()
	 * @see readAnimalDataset()
	 * @see readBeddingDataset()
	 * @see readSeparatorDataset()
	 */
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
		readSeparatorDataset("Separator");
	}
	
	/**
	 * Gets the climate data from the sheet of the excel file.
	 * Read each row of the "climate" sheet of the excel file, and store
	 * into the ArrayList: allCLimateData.
	 * @param sheetName 
	 */
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
	
	/**
	 * gets the animal data from the sheet of the excel file.
	 * Read each row of the "animal" sheet of the excel file, and store
	 * into the ArrayList: allAnimalData.
	 * @param sheetName
	 */
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
	
	/**
	 * gets the bedding data from the sheet of the excel file.
	 * Read each row of the "bedding" sheet of the excel file, and store
	 * into the ArrayList: allBeddingData.
	 * @param sheetName
	 */
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
	
	/**
	 * gets the separator data from the sheet of the excel file.
	 * Read each row of the "separator" sheet of the excel file, and store
	 * into the ArrayList: allSeparatorData.
	 * @param sheetName
	 */
	public void readSeparatorDataset(String sheetName){
		try {    		
    	    Sheet sheet = workbook.getSheet(sheetName);	     
    	    Row row;   	      	     
    	    for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
    	    	row = sheet.getRow(i); 	
    	    	SeparatorInfo element;
    	    	String name = row.getCell(0).toString();
	    		String efficiency = row.getCell(1).toString();
   	    	
    	    	element = new SeparatorInfo(name, efficiency);
    	    	allSeparatorData.add(element);   	    	 
    	     }	    	        	    
    	}catch(Exception e) {
    		 e.printStackTrace();
    	}  		
	}
	
	
	/*************************************************************
	 * 					Manage start panel
	 */

	// gets all state names, used as the input data of start panel
	public HashSet<String> getAllStateNames() {		
		HashSet<String> allStateNames = new HashSet<String>();
		allStateNames.add(" ");
		for (ClimateInfo element : allClimateData) {
			allStateNames.add(element.state);
		}
		return allStateNames;
	}

	// To store the output data
	public void storeStartPanelOutput(String[] s) {
		startPanelOutput = s;
	}		
	
	/**************************************************************
	 *                Manage climate panel
	 */
	
	/**
	 * gets the climate data with the same state, and store into an ArrayList. 
	 * @param name the state name.
	 * @param upperLevelData the origin data which needs to be filtered.
	 * @return ArrayList<ClimateInfo>
	 */
	public ArrayList<ClimateInfo> filterByState(String name, ArrayList<ClimateInfo> upperLevelData) {
		ArrayList<ClimateInfo> list = new ArrayList<>();
		for(ClimateInfo ele : upperLevelData) {
			if(ele.state.equals(name)) {
				list.add(ele);
			}
		}	
		return list;
	}
	/**
	 * gets the climate data with the same county, and store into an ArrayList.
	 * @param name the county name
	 * @param upperLevelData the origin data which needs to be filtered.
	 * @return ArrayList<CLimateInfo>
	 */
	public ArrayList<ClimateInfo> filterByCounty(String name, ArrayList<ClimateInfo> upperLevelData) {
		ArrayList<ClimateInfo> list = new ArrayList<>();
		for(ClimateInfo ele : upperLevelData) {
			if(ele.county.equals(name)) {
				list.add(ele);
			}
		}	
		return list;
	}
	
	// To store the output data
	public void storeClimatePanelOutput(ArrayList<String> list) {
		climatePanelOutout = list;
	}
		
	/*************************************************************
	 * 				Manage animal panel
	 */
	
	/**
	 * gets the data with the same source, and store into an ArrayList. 
	 * @param source the name of the source
	 * @param data the origin data which needs to be filtered.
	 * @return ArrayList<AnimalInfo>
	 */
	public ArrayList<AnimalInfo> filterByDataSource(String source, ArrayList<AnimalInfo> data) {
		ArrayList<AnimalInfo> list = new ArrayList<>();
		for(AnimalInfo a : data) {
			if(a.dataSource.equals(source)) {
				list.add(a);
			}
		}		
		return list;
	}
	
	// To store the output data
	public void storeAnimalPanelOutput(ArrayList<AnimalPanelOutputElement> o) {
		animalPanelOutput = o;
	}
	
	/*************************************************************
	 * 				Manage location panel
	 */
	
	/*
	public ArrayList<AnimalInfo> getDataFromAnimalPanel(){
		ArrayList<AnimalInfo> list = new ArrayList<>();
		for(AnimalPanelTableInfo ele : animalPanelOutput) {
			list.add(ele.aniInfo);
		}				
		return list;
	}*/
	
	/**
	 * Gets the column names from the animalPanelOutput, that is, the valid animal names.
	 * @return
	 */
	public String[] getLocationColumnNames() {

		int size = animalPanelOutput.size() + 1;
		String[] name = new String[size];
		name[0] = "Location";
		int index = 1;
		for(AnimalPanelOutputElement ele : animalPanelOutput) {
			name[index] = ele.name;
			index ++;
		}
		return name;
	}
	
	/**
	 * Gets the column number of the target column name in the list of column names.
	 * @param s	the target column name
	 * @param list	the list of column names
	 * @return	the column number
	 */
	/*public int getColumn(String s, String[] list) {
		for(int i = 0; i < list.length; i++) {
			if(s.equals(list[i]))
				return i+1;
		}
		return -1;
	}*/

	// To store the output data
	public void storeLocationPanelOutput(ArrayList<LocationPanelOutputElement> o) {
		locationPanelOutput = o;
	}
	
	
	
	/***************************************************************
	 * 				Manage Addition panel
	 */
	
	// get the element of the 1st column, from the locationPanelOutput
	public ArrayList<String> getLocationElements(){
		ArrayList<String> res = new ArrayList<>();
		for(int i = 0; i < locationPanelOutput.size(); i++) {
			res.add(locationPanelOutput.get(i).name);
		}
		return res;
	}
	
	
	// To store the output data
	public void storeAdditionsPanelOutput(ArrayList<AdditionsPanelOutputElement> o) {
		additionsPanelOutput = o;
	}
	
	
	/***************************************************************
	 * 				Manage runoff panel
	 */
	
	/**
	 * Gets the value of precipitation of 25Yr from the 
	 * output of the climate panel.
	 * @return double the value of precipitation of 25Yr
	 */
	public double getPrecipitation25Yr() {
		ArrayList<String> list = climatePanelOutout;
		String s = list.get(2);
		double res = Double.parseDouble(s);
		return res;
	}
	
	/**
	 * Gets the precData from the output of the climate panel.
	 * @return String[] the precData
	 */
	public String[] getPrecData() {
		ArrayList<String> list = climatePanelOutout;
		String[] res = new String[12];
		for(int i = 0; i < 12; i++) {
			res[i] = list.get(i + 7);			
		}
		return res;
	}
	
	
	
	/****************************************************************
	 * 				Manage management panel
	 */
	
	// get all stream names of the 1st column, from the additionsPanelOutput
	public ArrayList<String> getAllStreams(){
		ArrayList<String> res = new ArrayList<>();
		for(int i = 0; i < additionsPanelOutput.size(); i++) {
			res.add(additionsPanelOutput.get(i).name);
		}
		return res;
	}
	
	
	// To store the output data
	//public void storeMgmtPanelOutput(ArrayList<AdditionsPanelOutputElement> o) {
	//	additionsPanelOutput = o;
	//}
}
