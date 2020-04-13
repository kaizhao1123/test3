package test3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import test3.InputData.animalInfo;

public class AnimalPanel extends JPanel {
	MainFrame parent;
	JTabbedPane pane;
	LocationPanel locationPanel;
	AddAnimalDialog modelDialog;
	
	ArrayList<animalInfo> animalData;     // the animal data from the database
	//ArrayList<InputData.animalInfo> newAnimalDataset = new ArrayList<InputData.animalInfo>();  // the animal data created by the customer;
	animalInfo newAnimalInfo;
	ArrayList<animalInfo> reportedAnimal = new ArrayList<animalInfo>(); // the animals showed in the table, including all selected and new build animals, used to generate report
	
	String testword = "animal.....";
	
	Font font = new Font("Arial Narrow", Font.PLAIN, 13);   
    JLabel jl1, jl2, jl3,jl4,jl5,jl6;	
	JScrollPane choicesScrollPane;
	JScrollPane selectedScrollPane;
	JScrollPane tableScrollPane;
	MyTable mTable;
	JTable jtable;
	JComboBox animalType;
	JButton buttonAdd;
	JButton buttonRemove;
	JButton buttonAddAll;
	JButton buttonRemoveAll;
	JButton buttonNewAnimal;
	JButton buttonDeleteRow;
	JButton buttonOK;
	JButton buttonHelp;

	JList<String> choicesList;            // to display the choices
	JList<String> selectedList; 		  // to display the selected 	
	DefaultListModel<String> selectedModel;

	HashMap<String, JList<String>> choicesMap = new HashMap<>();          // K is the name of animal type, V is the original JList respond the name
	HashMap<String, JList<String>> selectedMap = new HashMap<>();		 // K is element,  V is the JList where the element come from.
	
	String[] animal = {"Beef","Dairy","Goat","Horse","Poultry", "Sheep","Swine","Veal"};
	String[] columnNamess = {"<html> Animal  </html>",           // the header of table
			"<html>Animal <br> (type) </html>",
			"<html>Quantity </html>",
			"<html>Weight <br> (lbs) </html>",
			"<html>Manure <br> (cu.ft/day/AU) </html>",
			"<html> VS <br> (lbs/day/AU) </html>",
			"<html> TS <br> (lbs/day/AU) </html>",
			"<html> Manure <br> (cu.ft/day) </html>",
			"<html> VS <br> (lbs/day) </html>",
			"<html> TS <br> (lbs/day) </html>",
			"<html>Manure <br> (lbs/day) </html>"};
	/*Object[][] dataa = {{"Feeder Calf","Beef","0","0","0","0","0","0","0","0","0","0"},
						{"Feeder Beef","Beef","0","1","2","3","4","5","0","0","0","0"},
	{"Feeder Beef","Beef","0","1","2","3","4","5","0","0","0","0"},
	{"Feeder Beef","Beef","0","1","2","3","4","5","0","0","0","0"}}; */
	Object[][] dataa;											// the data of table

	
	public AnimalPanel(ArrayList<animalInfo> data, String source) {

		animalData = data;	

				
		// build several lists to store different type of animal
		ArrayList<animalInfo> beef = filterByType("Beef",animalData);
		ArrayList<animalInfo> dairy = filterByType("Dairy",animalData);
		ArrayList<animalInfo> goat = filterByType("Goat",animalData);
		ArrayList<animalInfo> horse = filterByType("Horse",animalData);
		ArrayList<animalInfo> poultry = filterByType("Poultry",animalData);
		ArrayList<animalInfo> sheep = filterByType("Sheep",animalData);
		ArrayList<animalInfo> swine = filterByType("Swine",animalData);
		ArrayList<animalInfo> veal = filterByType("Veal",animalData);
						
		// build each Jlist for each type of animal, and store in the map
        DefaultListModel<String> beefModel = new DefaultListModel<>();  
        InputElementIntoModel(beef,beefModel);
	    JList<String> beefList = new JList<String>(beefModel); 	      	      	      	    
	    beefList.setFont(font);
	    choicesMap.put("beef", beefList);
	    
        DefaultListModel<String> dairyModel = new DefaultListModel<>();  
        InputElementIntoModel(dairy,dairyModel);
	    JList<String> dairyList = new JList<String>(dairyModel); 	      	      	      	    
	    dairyList.setFont(font); 
	    choicesMap.put("dairy", dairyList);
	    
	    DefaultListModel<String> goatModel = new DefaultListModel<>();  
        InputElementIntoModel(goat,goatModel);
	    JList<String> goatList = new JList<String>(goatModel); 	      	      	      	    
	    goatList.setFont(font);
	    choicesMap.put("goat",goatList);
	    
	    DefaultListModel<String> horseModel = new DefaultListModel<>();  
        InputElementIntoModel(horse,horseModel);
	    JList<String> horseList = new JList<String>(horseModel); 	      	      	      	    
	    horseList.setFont(font);     
	    choicesMap.put("horse",horseList);
	    
	    DefaultListModel<String> poultryModel = new DefaultListModel<>();  
        InputElementIntoModel(poultry,poultryModel);
	    JList<String> poultryList = new JList<String>(poultryModel); 	      	      	      	    
	    poultryList.setFont(font); 
	    choicesMap.put("poultry",poultryList);
	    
	    DefaultListModel<String> sheepModel = new DefaultListModel<>();  
        InputElementIntoModel(sheep,sheepModel);
	    JList<String> sheepList = new JList<String>(sheepModel); 	      	      	      	    
	    sheepList.setFont(font);  
	    choicesMap.put("sheep",sheepList);
	    
	    DefaultListModel<String> swineModel = new DefaultListModel<>();  
        InputElementIntoModel(swine,swineModel);
	    JList<String> swineList = new JList<String>(swineModel); 	      	      	      	    
	    swineList.setFont(font);
	    choicesMap.put("swine",swineList);
	    
        DefaultListModel<String> vealModel = new DefaultListModel<>();  
        InputElementIntoModel(veal,vealModel);
	    JList<String> vealList = new JList<String>(vealModel); 	      	      	      	    
	    vealList.setFont(font);
	    choicesMap.put("veal",vealList);
	    
	    
	    //initial beef as the first display data in choices
	    choicesList = beefList;
	    choicesScrollPane = new JScrollPane(choicesList);
	    choicesScrollPane.setPreferredSize(new Dimension(120,160));
	
	    //initial empty in selected
	    selectedModel = new DefaultListModel<>();	  
	    selectedList = new JList<String>(selectedModel);           
	    selectedList.setFont(font);		
		selectedScrollPane = new JScrollPane(selectedList);
	    selectedScrollPane.setPreferredSize(new Dimension(120,160));
	    
	    // initial animalType
	    JComboBox animalType = new JComboBox(animal);
	    animalType.setFont(font);
	    animalType.setPreferredSize(new Dimension(100,20));
	    animalType.setSelectedIndex(0);      
	    animalType.addActionListener(new ActionListener() {  
        		public void actionPerformed(ActionEvent e) {       
        			String data = animalType.getItemAt(animalType.getSelectedIndex()).toString().toLowerCase();
        		    choicesList = choicesMap.get(data);
        		    choicesScrollPane.setViewportView(choicesList);
        			
        		}  
    	});

	    // initial table
		mTable = new MyTable();	
		jtable = mTable.buildMyTable(columnNamess, dataa);
		jtable.getTableHeader().setPreferredSize(new Dimension(10,35));
	    tableScrollPane = new JScrollPane(jtable); 
		tableScrollPane.setPreferredSize(new Dimension(800,120));
			
		// initial the labels and buttons
	    jl1 = new JLabel("Choices");
	    jl2 = new JLabel("Animal Type:");
	    jl3 = new JLabel("Selected");
	    jl4 = new JLabel("AU = Animal Unit");
	    jl5 = new JLabel("VS = Volatile Solids");
	    jl6 = new JLabel("TS = Total Solids");	    
	    jl1.setFont(font);
	    jl2.setFont(font);
	    jl3.setFont(font);
	    jl4.setFont(font);
	    jl5.setFont(font);
	    jl6.setFont(font);	    
	    buttonAdd = new JButton("Add >");
	    buttonAdd.setFont(font);
	    buttonAdd.setPreferredSize(new Dimension(100,25));
	    buttonRemove = new JButton("< Remove");	
	    buttonRemove.setFont(font);
	    buttonRemove.setPreferredSize(new Dimension(100,25));
	    buttonAddAll = new JButton("Add All >>");
	    buttonAddAll.setFont(font);
	    buttonAddAll.setPreferredSize(new Dimension(100,25));
	    buttonRemoveAll =new JButton("<<Remove All");
	    buttonRemoveAll.setFont(font);
	    buttonRemoveAll.setPreferredSize(new Dimension(100,25));
	    buttonNewAnimal = new JButton("New Animal");
	    buttonNewAnimal.setFont(font);
	    buttonNewAnimal.setPreferredSize(new Dimension(100,25));	
	    buttonDeleteRow = new JButton("delete Row");
	    buttonDeleteRow .setFont(font);
	    buttonDeleteRow .setPreferredSize(new Dimension(100,25));	
	    buttonOK = new JButton("ok");
	    buttonOK.setFont(font);
	    buttonOK.setPreferredSize(new Dimension(100,25));
	    buttonHelp = new JButton("help");
	    buttonHelp.setFont(font);
	    buttonHelp.setPreferredSize(new Dimension(100,25));
		
	    // add listeners of each button
	    buttonAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){													
						addToSelectedList();
					}							
				}						
			);
	    buttonRemove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						removeFromSelectedList();
					}							
				}						
			);
	    buttonAddAll.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){						
						addAllToSelectedList();
					}							
				}						
			);
	    buttonRemoveAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						removeAllFromSelectedList();
					}							
				}						
			);
	    buttonNewAnimal.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						String source = parent.startPanel.sourceForData;
						String station = parent.startPanel.stationForData;						
				    	modelDialog = new AddAnimalDialog(mTable,jtable,source,station);
				    	modelDialog.setParent(parent);
					}							
				}						
			);
	    buttonDeleteRow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						int row = jtable.getSelectedRow();					
						String item = mTable.model.p[row][0].toString();					
						DefaultListModel<String> model = (DefaultListModel<String>) selectedList.getModel(); 
						if(model.contains(item)) {
							model.remove(model.indexOf(item));							
				     		JList oldList =  selectedMap.get(item);							
				     		DefaultListModel<String> oldModel = (DefaultListModel<String>) oldList.getModel();
				     		animalInfo ele = getInfoByName(item, animalData);
				     		//oldModel.add(ele.index ,item);
				     		addToModel(ele,oldModel,animalData);
				     		selectedMap.remove(item, oldList); 
				     		deleteTableRow(item);
						}
						else {
							if(!item.equals("Total"))
								deleteTableRow(item);
						}
							
					}							
				}						
			);
	    buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				pane = parent.tabbedPane;
				if(locationPanel == null) {
					
					locationPanel = new LocationPanel(reportedAnimal);
					locationPanel.setParent(parent);								
					pane.add("location",locationPanel);
					
					if(parent.startPanel.periodDialog.secondOption == true) {
						locationPanel.update2();
					}
					
				}
				else {
					//get the location table and add column.
				}
				pane.setSelectedIndex(pane.indexOfTab("location"));
			}							
		}						
	);
	    
	    /**
	     * set the layout
	     */
	   	    
	    setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();		
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.insets = new Insets(0,10,0,10);
		
		gc.gridx = 0;
		gc.gridy = 0;
		add(jl1, gc);
		gc.gridx = 2;
		add(jl2, gc);
		gc.gridx = 4;
		add(jl3, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridheight = 5;
		add(choicesScrollPane,gc);
		gc.gridx = 4;
		add(selectedScrollPane, gc);
		gc.gridx = 2;
		gc.gridheight = 1;
		add(animalType, gc);
		
		gc.insets = new Insets(2,10,2,10);
		gc.gridx = 2;
		gc.gridy = 2;
		add(buttonAdd,gc);
		gc.gridy = 3;
		add(buttonRemove,gc);
		gc.gridy = 4;
		add(buttonAddAll,gc);
		gc.gridy = 5;
		add(buttonRemoveAll, gc);
		
		gc.insets = new Insets(30,0,0,0);
		gc.gridx = 4;
		gc.gridy = 6;
		add(buttonNewAnimal,gc);
		gc.gridx = 5;
		add(buttonDeleteRow,gc);
		
		
		gc.insets = new Insets(5,0,0,0);
		gc.gridx = 0;
		gc.gridy = 7;
		gc.gridwidth = 8;
		gc.gridheight = 2;
		add(tableScrollPane, gc);
		
		
		gc.gridy = 9;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.insets = new Insets(0,10,0,10);
		add(jl4,gc);
		gc.gridy = 10;
		add(jl5,gc);
		gc.gridy = 11;
		add(jl6,gc);
		gc.gridx = 5;
		add(buttonHelp,gc);
		gc.gridx = 6;
		add(buttonOK,gc);



	}
	private void addToSelectedList() {
     	int index = choicesList.getSelectedIndex();		
     	if(index >= 0) {
     		DefaultListModel<String> model = (DefaultListModel<String>) choicesList.getModel();
     		String item = model.getElementAt(index);
     		model.remove(index);
     		selectedModel.addElement(item);	
     		selectedMap.put(item, choicesList); 
     		addTableRow(item);
     	}		 	
    }
	
    private void removeFromSelectedList() {

 		int index = selectedList.getSelectedIndex();
 		if(index >= 0) {
     		DefaultListModel<String> model = (DefaultListModel<String>) selectedList.getModel();
     		String item = model.getElementAt(index);							
     		model.remove(index);
     		JList oldList =  selectedMap.get(item);							
     		DefaultListModel<String> oldModel = (DefaultListModel<String>) oldList.getModel();
     		animalInfo ele = getInfoByName(item, animalData);    		
     		addToModel(ele,oldModel,animalData);
     		selectedMap.remove(item, oldList); 
     		deleteTableRow(item);
 		}	   	 						
    }
	
    private void addAllToSelectedList() {
	   	DefaultListModel<String> model = (DefaultListModel<String>) choicesList.getModel();
	   	if(model !=null) {
	   		int size = model.getSize();  	
		   	while(size > 0) {     		
		    		String item = model.getElementAt(0);
		    		model.remove(0);
		    		selectedModel.addElement(item);	
		    		selectedMap.put(item, choicesList);
		    		addTableRow(item);
		    		size --;
		   	} 	   	
	   	}
	   	
    }
    
    private void removeAllFromSelectedList() {  	 
      	DefaultListModel<String> model = (DefaultListModel<String>) selectedList.getModel();     	
     	int size = model.getSize();  
     	if(model != null) {
     		while(size > 0) {  
         		String item = model.getElementAt(0);							
         		model.remove(0);
         		JList oldList =  selectedMap.get(item);	
         		if(oldList != null) {
         			DefaultListModel<String> oldModel = (DefaultListModel<String>) oldList.getModel();
             		animalInfo ele = getInfoByName(item, animalData);
             		//oldModel.add(ele.index ,item);	
             		addToModel(ele,oldModel,animalData);
             		selectedMap.remove(item, oldList); 
             		deleteTableRow(item);
         		}
         		size--;          		
         	} 
     	}     	
    }
       
    private void deleteTableRow(String item) {
    	
    	if(mTable.model.isContained(item)) {
 			int row = mTable.model.rowOfElement(item); 			
 			mTable.model.deleteRow(row);
 			for(int i = 2; i < mTable.model.getColumnCount(); i++) {
				mTable.model.mySetValueAt(mTable.model.getNewSum(i), mTable.model.getRowCount()-1, i);
			}						
			jtable.updateUI();
			animalInfo a = getByName(reportedAnimal, item);
			reportedAnimal.remove(a);
 		}
    }
    
    private void addTableRow(String item) {
     		animalInfo ele = getInfoByName(item, animalData);
     		if(ele != null) {
     			DecimalFormat df = new DecimalFormat("0.00");
        		
         		String[] rowData = new String[11];
    			String quantity = "0";
    			String weight = "0";
    			String m = ele.data[0];
    			String ts = ele.data[2];
    			String vs = ele.data[3];
    			double qDou = Double.parseDouble(quantity);
    			double wDou = Double.parseDouble(weight);
    			double mDou = Double.parseDouble(m);
    			double tsDou = Double.parseDouble(ts);
    			double vsDou = Double.parseDouble(vs);
    		         		         		 
    			rowData[0] = item;
    			rowData[1] = ele.type;
    			rowData[2] = quantity;
    			rowData[3] = weight;
    			rowData[4] = m;
    			rowData[5] = vs;
    			rowData[6] = ts;
    			rowData[7] = df.format(mDou * qDou * wDou / 1000);
    			rowData[8] = df.format(vsDou * qDou * wDou / 1000);
    			rowData[9] = df.format(tsDou * qDou * wDou / 1000);
    			rowData[10] = df.format(mDou * qDou * wDou * 60 / 1000);
    			//rowData[10] = Double.toString(mDou * qDou * wDou * 60 / 1000);
    			mTable.model.addRow(rowData);
               
    			for(int i = 2; i < rowData.length; i++) {
    				if(i == 3 || i == 4 || i == 5 || i == 6) {
    					mTable.model.mySetValueAt("N/A", mTable.model.getRowCount()-1, i);
    					
    				}    					
    				else
    					mTable.model.mySetValueAt(mTable.model.getNewSum(i), mTable.model.getRowCount()-1, i);
    			}						
    			jtable.updateUI();
         	 	reportedAnimal.add(ele);
     		}
     		
    }
    
    private animalInfo getInfoByName(String name, ArrayList<animalInfo> data) {
    	animalInfo ele = null;
    	for(animalInfo a : data) {
    		if(a.name == name) {
    			ele = a;
    		}
    	}
    	return ele;
    }
    
	private ArrayList<animalInfo> filterByType(String ds, ArrayList<animalInfo> data) {
		ArrayList<animalInfo> list = new ArrayList<>();
		for(animalInfo a : data) {
			if(a.type.equals(ds)) {
				list.add(a);
			}
		}		
		return list;
	}
    private void InputElementIntoModel(ArrayList<animalInfo> l, DefaultListModel<String> model) {   	
    	for(int i  = 0; i < l.size(); i++) {
    		animalInfo ele = l.get(i);
    		ele.index = i;
    		model.add(i, ele.name);   		
    	} 	
    }
    private void addToModel(animalInfo a, DefaultListModel<String> model, ArrayList<animalInfo> data) {
    	if(model == null)
    		model.add(0, a.name);
    	if(a != null) {
    		int size = model.getSize();
        	int index = a.index;       	
        	if(index == 0) {
        		model.add(0, a.name);
        	}
        	else {
        		for (int i = 0; i < model.size(); i ++) {
            		String name = model.get(i);        		       		
            		animalInfo aa = getInfoByName(name, data);     
            		        		
            		if(aa != null) {
            			if(aa.index > index) {
                			model.add(i, a.name);
                    		break;
                		}
                		else
                			continue;
            		}           		      			
            	}
        		if(model.size() == size) {
        			model.setSize(size + 1);
        			model.add(model.size() -1, a.name);
        		}       		
        	}    	  	    	    		
    	}    	
    }
    
    public boolean inclued(JList<String> list, String s) {
    	DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();     	
     	int size = model.getSize();
    	for(int i = 0; i < size; i++) {
    		if(model.elementAt(i) == s)    			
    			return true;
    	}
    	return false;
    }
    
    public animalInfo getByName(ArrayList<animalInfo> list, String n) {
    	animalInfo res = null;
    	for(int i = 0; i < list.size(); i++) {
    		if(list.get(i).name.equals(n)) {
    			res = list.get(i);
    		}
    	}
    	return res;
    }
    
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
