package Panels;

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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import AWS.PanelManager;
import Entity.AnimalInfo;
import Entity.ClimateTable;
import Entity.LocationsTable;
import Entity.OutputOfAnimalPanel;




public class LocationsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	OperatingPeriodDialog periodDialog;
	AdditionsPanel additionsPanel;
	  
	String[] columnName;
	Object data1[][];   // for table1
	Object data2[][];	// for table2
	ArrayList<AnimalInfo> animalsList;
	int rowIndex;  //  the row index of selected to delete
		
	JPanel panel = this;
	GridBagConstraints gc;
	
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
    
    
 	public LocationsPanel(PanelManager pm) {
 		panelManager = pm;
 		animalsList = panelManager.getDataFromAnimalPanel();
 		
 		// initial this panel
 		initialData();
 		initialElements();
 		initialActionLiseners();
 		setLayout(new GridBagLayout());
 		gc = new GridBagConstraints();
 		initialLayout(gc);

 	}
    
    private void initialData() {
    	// get the column of the table
		int size = animalsList.size() + 1;
		String[] name = new String[size];
		name[0] = "Location";
		for(int i = 1; i < size; i++) {
			name[i] = animalsList.get(i - 1).name;
		}
		columnName = name;
    }
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
      
        
        myTable2 = new LocationsTable();
        databaseTable2 = myTable2.buildMyTable(columnName, data2); 
        scrollPane2 = new JScrollPane(databaseTable2);	
        scrollPane2.setPreferredSize(new Dimension(480,100));
            
        buttonAdd = new JButton("Add Location"); 
        buttonAdd.setPreferredSize(new Dimension(150,25));
        buttonDelete = new JButton("Delete Selected Row");
        buttonHelp = new JButton("Help");
        buttonOK = new JButton("OK");
    }
    private void initialActionLiseners() {
    	buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				if(textLocation != null) {
					String s = textLocation.getText();
					int col = myTable1.model.getColumnCount();
					String[] dataTable1 = new String[col];
					dataTable1[0] = s;
					for(int i = 1 ; i < col; i++) {
						dataTable1[i] = "0";
					}
					
					String[] dataTable2 = new String[col];
					dataTable2[0] = s;
					for(int i = 1 ; i < col; i++) {
						dataTable2[i] = "0";
					}
					
					myTable1.model.addRow(dataTable1);
					data1 = myTable1.model.data;
					//columnNamess = myTable1.model.n;
					databaseTable1.updateUI();
					
					myTable2.model.addRow(dataTable2);
					data2 = myTable2.model.data;				
					databaseTable2.updateUI();
					
					textLocation.setText("");
					
					// update color
					int r = databaseTable1.getRowCount();
					int c = databaseTable1.getColumnCount();					
					myTable1.setColor(r-1,r-1,1,c-1,Color.cyan);
					myTable2.setColor(r-1,r-1,1,c-1,Color.cyan);
					
				}
			}							
		}
        );
        buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				deleteTableRow();
				data1 = myTable1.model.data;
				data2 = myTable2.model.data;															
				
			}							
		}						
		);
        buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				pane = parent.tabbedPane;
				if (additionsPanel == null) {
					getOutput();
					//panelManager.storeAnimalPanelOutput(animalPanelOutput);
					additionsPanel = new AdditionsPanel(panelManager);
					additionsPanel.setParent(parent);
					pane.add("additions", additionsPanel);					

				} else {
					// get the location table and add column.
					
				}
				pane.setSelectedIndex(pane.indexOfTab("additions"));
			
				
			}							
		}						
		);
        
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
    private void initialLayout(GridBagConstraints gc) {
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
 
	
	// Corresponding the first option of periodDialog
	public void update1() {
    	label_3.setText(" ");
    	label_4.setText(" ");
    	panel.remove(scrollPane2);
    	twoPeriod = false;
    	panel.setSize(new Dimension(500,400));
    	panel.updateUI();
    }
	// Corresponding the second option of periodDialog
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
    // change the start and end month of the period
    public void update3() {
    	firstPeriod = periodDialog.firstOperatingPeriod;
    	secondPeriod = periodDialog.secondOperatingPeriod;
    	label_3.setText("1st Operating Period: " + firstPeriod);
    	label_4.setText("2nd Operating Period: " + secondPeriod);
    }
    
    private void deleteTableRow() {	   
		if(rowIndex != data1.length - 1 &&
		   rowIndex != data2.length - 1) {
			myTable1.model.deleteRow(rowIndex+1);
			myTable2.model.deleteRow(rowIndex+1);
			for(int i = 1; i < myTable1.model.getColumnCount(); i++) {
				myTable1.model.mySetValueAt(myTable1.model.getNewSum(i), myTable1.model.getRowCount()-1, i);
				myTable2.model.mySetValueAt(myTable2.model.getNewSum(i), myTable2.model.getRowCount()-1, i);
			}
			
			// update color
			int r = databaseTable1.getRowCount();
			int c = databaseTable1.getColumnCount();			
			myTable1.setColor(r-1,r-1,1,c-1,Color.cyan);
			myTable2.setColor(r-1,r-1,1,c-1,Color.cyan);
			
			databaseTable1.updateUI();
			databaseTable2.updateUI();
		}
    }
	
    private void addTableColumn(String s) {
    	String[] ncolumnNamess = new String[columnName.length+1];
		for(int i = 0; i < columnName.length; i++) {
			ncolumnNamess[i] = columnName[i];
		}
		ncolumnNamess[columnName.length] = s;
		columnName = ncolumnNamess;
		
		myTable1.model.addColumn();
		myTable2.model.addColumn();
		updateTable(myTable1, myTable2);
		/*data1 = myTable1.model.data;						
		myTable1 = new LocationTable();	
		databaseTable1 = myTable1.buildMyTable(columnName, data1);
		scrollPane1.setViewportView(databaseTable1);
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
		

		myTable2.model.addColumn();
		data2 = myTable2.model.data;						
		myTable2 = new LocationTable();						
		databaseTable2 = myTable2.buildMyTable(columnName, data2);	
		scrollPane2.setViewportView(databaseTable2);
		databaseTable2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {              
              int r= databaseTable2.getSelectedRow();              
              rowIndex = r;
              if (databaseTable1.isEditing())
            	  databaseTable1.getCellEditor().stopCellEditing();
              
              databaseTable1.changeSelection(rowIndex, 0,false,false);
              databaseTable1.repaint();
              //databaseTable1.updateUI();
            }
        });*/
	    
    }
    
    private void deleteTableColumn(String s) {
    	int col = 1;
    	for (int i = 0; i < columnName.length; i++) {
    		if (columnName[i] == s)
    			col = i;
    	}  	
    	String[] ncolumnName = new String[columnName.length-1];
    	for(int i = 0; i < col; i++) {
    		ncolumnName[i] = columnName[i];
    	}
    	for(int i = col; i < columnName.length-1; i++) {
    		ncolumnName[i] = columnName[i+1];
    	}   	
    	columnName = ncolumnName;   	
    	
    	myTable1.model.deleteColumn(col);
    	myTable2.model.deleteColumn(col);
    	updateTable(myTable1, myTable2);
    	
    	/*data1 = myTable1.model.data;						
    	myTable1 = new LocationTable();	
    	databaseTable1 = myTable1.buildMyTable(columnName, data1);
    	scrollPane1.setViewportView(databaseTable1);
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
    	
    	
    	myTable2.model.deleteColumn(col);
    	data2 = myTable2.model.data;						
    	myTable2 = new LocationTable();	
    	databaseTable2 = myTable2.buildMyTable(columnName, data1);
    	scrollPane2.setViewportView(databaseTable2);
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
          }); */
 
    }
    
    private void updateTable(LocationsTable t1, LocationsTable t2) {
    	
    	//myTable1.model.deleteColumn(col);
    	data1 = t1.model.data;	
    	data2 = t2.model.data;
    	t1 = new LocationsTable();	
    	t2 = new LocationsTable();
    	databaseTable1 = t1.buildMyTable(columnName, data1);
    	databaseTable2 = t2.buildMyTable(columnName, data2);
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
    
	private void getOutput() {
		/*animalPanelOutput = new ArrayList<>();
		for(int i = 0; i < animalInTable.size(); i++) {
			AnimalInfo ani = animalInTable.get(i);
			String quantity = mTable.model.data[i][2].toString();
			String weight = mTable.model.data[i][3].toString();
			OutputOfAnimalTable ele = new OutputOfAnimalTable(ani,quantity,weight);
			animalPanelOutput.add(ele);
		}*/				
	}
	
	
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
