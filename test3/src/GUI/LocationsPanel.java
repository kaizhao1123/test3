package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Controller.PanelManager;
import Model_Entity.AnimalInfo;
import Model_Entity.AnimalPanelTableInfo;
import Model_Tables.ClimateTable;
import Model_Tables.LocationsTable;

/**
 * This class is to create the location panel.
 * The purpose of this class is to define where the animals deposit their manure
 * throughout a day for each operating period. It also establishes a manure waste
 * stream from a location to which the waste water, flush water, and bedding are 
 * added to form the total waste stream directed to agricultural waste management 
 * system treatment/storage components such as a waste storage facility or waste 
 * treatment lagoon.
 * 
 * @author Kai Zhao
 *
 */
public class LocationsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	OperatingPeriodDialog periodDialog;
	AdditionsPanel additionsPanel;
	  
	/***********************************************************
	 * declare the data structures used in this panel
	 */
	String[] columnName;
	Object data1[][];   // for table1
	Object data2[][];	// for table2
	// the input data from the output of animalPanel, to generate the column names;
	ArrayList<AnimalInfo> animalsList;	
	ArrayList<String> firstColNames; // the first column's name;
	int rowIndex;  //  the row index of selected to delete
	String deleteName; // the location name to be delete.
    ArrayList<String> locationPanelOutput;	   // to store the output of location panel
	
	/***********************************************************
	 * declare the elements used in this panel
	 */
	JPanel panel = this;

	JLabel label_1;
	JLabel label_2;
    LocationsTable myTable1;
    LocationsTable myTable2;   
    JTable databaseTable1;
    JTable databaseTable2;
    JLabel label_3;
    JLabel label_4;
    JTextField textLocation;
    JScrollPane scrollPane1;
    JScrollPane scrollPane2;
    JButton buttonAdd;
    JButton buttonDelete;
    JButton buttonHelp;
    JButton buttonOK;
    
    Boolean twoPeriod = false;
    String firstPeriod;
    String secondPeriod;
    
	GridBagConstraints gc;
	
   /**
    * The constructor of this class
    * @param pm	 the data "controller" of this project
    */
 	public LocationsPanel(PanelManager pm) {
 		panelManager = pm;
 		initialData();
 		initialElements();
 		initialLiseners();
 		initialLayout();
 	}
    
 	// initials the data structures, mainly to get the input data.
    private void initialData() {
		
    	// get the column of the table		
		columnName = panelManager.getColumnNames();
		// initial the firstColNames;
		firstColNames = new ArrayList<>();
    }
    // initials the elements of this panel
    private void initialElements() {		
		label_1 = new JLabel("Enter Location: ");
		textLocation = new JTextField();
		textLocation.setPreferredSize(new Dimension(130,25));
		label_2 = new JLabel("Enter the Percent of Manure Each Animal Deposits in Each Location: ");

		label_3 = new JLabel(" ");
		label_4 = new JLabel(" ");
        
        myTable1 = new LocationsTable();
        databaseTable1 = myTable1.buildMyTable(columnName, data1); 
        scrollPane1 = new JScrollPane(databaseTable1);	
        scrollPane1.setPreferredSize(new Dimension(480,100));
        Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane1.setViewportBorder(border);
		scrollPane1.setBorder(border);
      
        
        myTable2 = new LocationsTable();
        databaseTable2 = myTable2.buildMyTable(columnName, data2); 
        scrollPane2 = new JScrollPane(databaseTable2);	
        scrollPane2.setPreferredSize(new Dimension(480,100));       
		scrollPane2.setViewportBorder(border);
		scrollPane2.setBorder(border);
            
        buttonAdd = new JButton("Add Location"); 
        buttonAdd.setPreferredSize(new Dimension(150,25));
        buttonDelete = new JButton("Delete Selected Row");
        buttonHelp = new JButton("Help");
        buttonOK = new JButton("OK");

 		gc = new GridBagConstraints();
    }
    // initials the listeners of this panel
    private void initialLiseners() {
    	
    	// associated with additionsPanel. the stream names cannot be duplicated.
    	buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				if (databaseTable1.isEditing())
					databaseTable1.getCellEditor().stopCellEditing(); 
				if (databaseTable2.isEditing())
					databaseTable2.getCellEditor().stopCellEditing(); 
				if(textLocation != null) {
					String s = textLocation.getText();
					if(s.length() > 0) {
						if(additionsPanel != null) {
							if(!additionsPanel.streamNames.contains(s)) {								
								addTableRow(s);
							}
							additionsPanel.addTableRow(s);
						}
						else
							addTableRow(s);						
					}
				
				}							
			}
    	});
    	
        buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				if (databaseTable1.isEditing())
					databaseTable1.getCellEditor().stopCellEditing(); 
				if (databaseTable2.isEditing())
					databaseTable2.getCellEditor().stopCellEditing(); 
				deleteName = myTable1.model.data[rowIndex][0].toString();
				if(additionsPanel != null) {
					additionsPanel.deleteTableRow(deleteName);
				}
				deleteTableRow();
				firstColNames.remove(deleteName);
				data1 = myTable1.model.data;
				data2 = myTable2.model.data;															
				
			}							
		}						
		);
        buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){			
				pane = parent.tabbedPane;
				if (databaseTable1.isEditing())
					databaseTable1.getCellEditor().stopCellEditing(); 
				if (databaseTable2.isEditing())
					databaseTable2.getCellEditor().stopCellEditing(); 
				if (additionsPanel == null) {
					getOutput();
					if(locationPanelOutput.isEmpty()) {
						JOptionPane.showMessageDialog(null,"No location added");
						
					}
					else {
						panelManager.storeLocationPanelOutput(locationPanelOutput);
						additionsPanel = new AdditionsPanel(panelManager);
						additionsPanel.setParent(parent);
						pane.add("additions", additionsPanel);
						pane.setSelectedIndex(pane.indexOfTab("additions"));
					}										
				} else {
					// get the location table and add column.
					
				}
				
						
			}							
		});
        
        /*
         * add mouse listener:
         * 1. to guarantee the selected row of the first table is the same as the second table.
         * 2. to guarantee that the editing data in the second table will be store, when the mouse
         * 	  click outside of the second table after entered the data.
         */             
        databaseTable1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {              
              int r= databaseTable1.getSelectedRow();              
              rowIndex = r;
              if (databaseTable2.isEditing())
            	  databaseTable2.getCellEditor().stopCellEditing();             
              databaseTable2.changeSelection(rowIndex, 0,false,false);
              databaseTable2.repaint();
            }
        }); 
	    
        // similar with the above listener.
	    databaseTable2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {              
              int r= databaseTable2.getSelectedRow();              
              rowIndex = r;
              if (databaseTable1.isEditing())
            	  databaseTable1.getCellEditor().stopCellEditing();              
              databaseTable1.changeSelection(rowIndex, 0,false,false);
              databaseTable1.repaint();
            }
        });
    }
    
    // initials the layout of this panel
    private void initialLayout() {
 		setLayout(new GridBagLayout());
    	gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(0,5,5,0);
		
        gc.gridx = 0;
		gc.gridy = 0;		
		add(label_1,gc);
		gc.gridy = 1;
		add(textLocation,gc);
		gc.gridx = 2;
		add(buttonAdd,gc);
		gc.gridwidth = 3;
		gc.gridx = 4;		
		add(buttonDelete,gc);
		
		gc.gridx = 0;		
		gc.gridy = 2;
		gc.gridwidth = 7;
		add(label_2,gc);			
		gc.gridy = 3;
		add(label_3,gc);
		gc.gridy = 4;		
		add(scrollPane1,gc);
		gc.gridy = 9;
		gc.gridheight = 1;
		add(label_4,gc);

		gc.gridx = 5;
		gc.gridy = 15;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		add(buttonHelp,gc);
		gc.gridx = 6;
		add(buttonOK,gc);		
    }
 
	// get the output of this panel	
	private void getOutput() {
		locationPanelOutput = new ArrayList<>();
		for(int i = 0; i < myTable1.model.data.length-1; i++) {
			locationPanelOutput.add(myTable1.model.data[i][0].toString());
		}
	}
    
	// Corresponding the first option of periodDialog, that is, only table1 will be shown
	public void update1() {
    	label_3.setText(" ");
    	label_4.setText(" ");
    	panel.remove(scrollPane2);
    	twoPeriod = false;
    	panel.setSize(new Dimension(500,400));
    	panel.updateUI();
    }
	
	
	// Corresponding the second option of periodDialog, that is, both table will be shown.
    public void update2() {
    	
    	periodDialog = parent.startPanel.periodDialog;
    	firstPeriod = periodDialog.firstOperatingPeriod;
    	secondPeriod = periodDialog.secondOperatingPeriod;
    	
    	label_3.setText("1st Operating Period: " + firstPeriod);
    	label_4.setText("2nd Operating Period: " + secondPeriod);
		gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(0,5,5,0);
		gc.gridx = 0;
    	gc.gridy = 10;
		gc.gridheight = 3;
		gc.gridwidth = 7;
		panel.add(scrollPane2,gc);
		twoPeriod = true;
		panel.setSize(new Dimension(500,600));
		panel.updateUI();
    }
    
    // after update2, changing of the start and end month of the period will lead to the change of the label
    public void update3() {
    	firstPeriod = periodDialog.firstOperatingPeriod;
    	secondPeriod = periodDialog.secondOperatingPeriod;
    	label_3.setText("1st Operating Period: " + firstPeriod);
    	label_4.setText("2nd Operating Period: " + secondPeriod);
    }
    
    // adds row data into the table
    private void addTableRow(String s) {   					
		if(!firstColNames.contains(s)) {
			int col = myTable1.model.getColumnCount();
			String[] rowData = new String[col];
			rowData[0] = s;
			for(int i = 1 ; i < col; i++) {
				rowData[i] = "0";
			}
			myTable1.model.addRow(rowData);									
			myTable2.model.addRow(rowData);				
			
			databaseTable1.updateUI();
			databaseTable2.updateUI();
			textLocation.setText("");	
			data1 = myTable1.model.data;
			data2 = myTable2.model.data;
			firstColNames.add(s);
		}
		else
			JOptionPane.showMessageDialog(null,"You cannot have two waste streams with"
					+ "the same name!" + s); 			
    }
    
    // delete row data from the table
    private void deleteTableRow() {	   
		if(rowIndex != data1.length - 1 &&
		   rowIndex != data2.length - 1) {
			myTable1.model.deleteRow(rowIndex);
			myTable2.model.deleteRow(rowIndex);
			
			for(int i = 1; i < myTable1.model.getColumnCount(); i++) {
				myTable1.model.mySetValueAt(myTable1.model.getNewSum(i), myTable1.model.getRowCount()-1, i);
				myTable2.model.mySetValueAt(myTable2.model.getNewSum(i), myTable2.model.getRowCount()-1, i);
			}
			
			databaseTable1.updateUI();
			databaseTable2.updateUI();
		}
    }
	
    // add column data into the table
    public void addTableColumn(String s) {
    	String[] ncolumnNamess = new String[columnName.length+1];
		for(int i = 0; i < columnName.length; i++) {
			ncolumnNamess[i] = columnName[i];
		}
		ncolumnNamess[columnName.length] = s;
		columnName = ncolumnNamess;
		
		myTable1.model.addColumn();
		myTable2.model.addColumn();
		updateTable(myTable1, myTable2);		
	    
    }
    
    // delete the column data from the table
    public void deleteTableColumn(String s) {
    	int colIndex = 1;
    	for (int i = 0; i < columnName.length; i++) {
    		if (columnName[i] == s)
    			colIndex = i;
    	}  	
    	String[] ncolumnName = new String[columnName.length-1];
    	for(int i = 0; i < colIndex; i++) {
    		ncolumnName[i] = columnName[i];
    	}
    	for(int i = colIndex; i < columnName.length-1; i++) {
    		ncolumnName[i] = columnName[i+1];
    	}   	
    	columnName = ncolumnName;   	   	
    	myTable1.model.deleteColumn(colIndex);
    	myTable2.model.deleteColumn(colIndex);
    	updateTable(myTable1, myTable2);   	
    }
    
   
    // update table when the column changed  
    private void updateTable(LocationsTable t1, LocationsTable t2) {
    	
    	// gets the new model data, that is, the data after add one column, but delete the last row "total"
    	data1 = t1.model.data;	
    	data2 = t2.model.data;

    	// creates new jtable based on the new model data, and the model's data will have "total" row
    	databaseTable1 = t1.buildMyTable(columnName, data1);
    	databaseTable2 = t2.buildMyTable(columnName, data2);
    	   
    	// update the class variables data1 and data2, which has includes the "total" row
    	data1 = t1.model.data;	
    	data2 = t2.model.data;
    	
    	scrollPane1.setViewportView(databaseTable1);    	
    	scrollPane2.setViewportView(databaseTable2);
    	
    	databaseTable1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {              
                int r= databaseTable1.getSelectedRow();
                rowIndex = r;                           
                if (databaseTable2.isEditing())
              	  databaseTable2.getCellEditor().stopCellEditing();
               
                databaseTable2.changeSelection(rowIndex, 0,false,false);
                databaseTable2.repaint();
                //databaseTable2.updateUI();
                
              }
          }); 
    	databaseTable2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {              
                int r= databaseTable2.getSelectedRow();
                rowIndex = r;                           
                if (databaseTable1.isEditing())
              	  databaseTable1.getCellEditor().stopCellEditing();
               
                databaseTable1.changeSelection(rowIndex, 0,false,false);
                databaseTable1.repaint();
               // databaseTable1.updateUI();                
              }
          }); 
 
 
    }
    
	
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
