package test3;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import test3.InputData.animalInfo;

public class AnimalPanel extends JPanel {
	MainFrame parent = null;
	ArrayList<InputData.animalInfo> animalData;
	
	
	
	Font font = new Font("Arial Narrow", Font.PLAIN, 13);   
    JLabel jl1, jl2, jl3,jl4,jl5,jl6;	
	JScrollPane choicesScrollPane;
	JScrollPane selectedScrollPane;
	JScrollPane tableScrollPane;	
	JComboBox animalType;
	JButton add;
	JButton remove;
	JButton addAll;
	JButton removeAll;
	JButton newAnimal;
	JButton deleteRow;
	JButton ok;
	JButton help;

	JList<String> choicesList;            // to display the choices
	JList<String> selectedList; 		  // to display the selected 	
	DefaultListModel<String> selectedModel;

	HashMap<String, JList<String>> choicesMap = new HashMap<>();          // K is the name of animal type, V is the original JList respond the name
	HashMap<String, JList<String>> selectedMap = new HashMap<>();		 // K is element,  V is the JList where the element come from
	
	String[] animal = {"Beef","Dairy","Goat","Horse","Poultry", "Sheep","Swine","Veal"};
	String[] columnNamess = {"<html> Animal  </html>",
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
	Object[][] dataa;
	MyTable mTable;
	JTable jtable;
	
	public AnimalPanel(JTabbedPane pane, ArrayList<animalInfo> data, String source) {

		animalData = data;	

				
		// build list to store different type of animal
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
	    
	    
	    //initial beef as the default data in choices
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
	    add = new JButton("Add >");
	    add.setFont(font);
	    add.setPreferredSize(new Dimension(100,25));
	    remove = new JButton("< Remove");	
	    remove.setFont(font);
	    remove.setPreferredSize(new Dimension(100,25));
	    addAll = new JButton("Add All >>");
	    addAll.setFont(font);
	    addAll.setPreferredSize(new Dimension(100,25));
	    removeAll =new JButton("<<Remove All");
	    removeAll.setFont(font);
	    removeAll.setPreferredSize(new Dimension(100,25));
		newAnimal = new JButton("New Animal");
		newAnimal.setFont(font);
		newAnimal.setPreferredSize(new Dimension(100,25));	
		deleteRow = new JButton("delete Row");
		deleteRow .setFont(font);
		deleteRow .setPreferredSize(new Dimension(100,25));	
		ok = new JButton("ok");
		ok.setFont(font);
	    ok.setPreferredSize(new Dimension(100,25));
		help = new JButton("help");
		help.setFont(font);
	    help.setPreferredSize(new Dimension(100,25));
		
	    // add listeners of each button
	    add.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){							
						
						addToSelectedList();
					}							
				}						
			);
	    remove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						removeFromSelectedList();
					}							
				}						
			);
	    addAll.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){						
						addAllToSelectedList();
					}							
				}						
			);
	    removeAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						removeAllFromSelectedList();
					}							
				}						
			);
	    newAnimal.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						//removeAllFromSelectedList();
					}							
				}						
			);
	    deleteRow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){						
						int row = jtable.getSelectedRow();
						
						System.out.print("aaaaaaa");
						System.out.print(row);
						
						String item = mTable.model.p[row][0].toString();
						
						System.out.print("bbb");
						System.out.print(item);
						
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
				     		
							System.out.print("ccc");
						}
						else
							deleteTableRow(item);
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
		add(add,gc);
		gc.gridy = 3;
		add(remove,gc);
		gc.gridy = 4;
		add(addAll,gc);
		gc.gridy = 5;
		add(removeAll, gc);
		
		gc.insets = new Insets(30,0,0,0);
		gc.gridx = 4;
		gc.gridy = 6;
		add(newAnimal,gc);
		gc.gridx = 5;
		add(deleteRow,gc);
		
		
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
		add(help,gc);
		gc.gridx = 6;
		add(ok,gc);



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
     		//oldModel.add(ele.index ,item);
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
    				mTable.model.mySetValueAt(mTable.model.getNewSum(i), mTable.model.getRowCount()-1, i);
    			}						
    			jtable.updateUI();
         	 	
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
    
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
