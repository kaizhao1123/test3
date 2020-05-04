package Panels;

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
import Entity.LocationTable;



public class LocationPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	OperatingPeriodDialog periodDialog;
	  
	String[] columnName;
	Object data1[][];   // for table1
	Object data2[][];	// for table2
	ArrayList<AnimalInfo> animalsList;
	int rowIndex;  //  the row index of selected to delete
		
	JPanel panel = this;
	GridBagConstraints gc;
    LocationTable myTable1;
    LocationTable myTable2;   
    JTable databaseTable1;
    JTable databaseTable2;
    JLabel label_3;
    JLabel label_4;
    JTextField textLocation;
    JScrollPane scrollPane1;
    JScrollPane scrollPane2;
    
    Boolean twoPeriod = false;
    String firstPeriod;
    String secondPeriod;
    
	public LocationPanel(PanelManager pm) {
		// get the column name of the table
		//
		panelManager = pm;
		animalsList = panelManager.getDataFromAnimalPanel();
		
		
		int size = animalsList.size() + 1;
		String[] name = new String[size];
		name[0] = "Location";
		for(int i = 1; i < size; i++) {
			name[i] = animalsList.get(i - 1).name;
		}
		columnName = name;
		
		// initial the component of structure 
		JLabel label_1 = new JLabel("Enter Location: ");
		textLocation = new JTextField();
		textLocation.setPreferredSize(new Dimension(130,25));
		JLabel label_2 = new JLabel("Enter the Percent of Manure Each Animal Deposits in Each Location: ");

		label_3 = new JLabel(" ");
		label_4 = new JLabel(" ");
        
        myTable1 = new LocationTable();
        databaseTable1 = myTable1.buildMyTable(columnName, data1); 
        scrollPane1 = new JScrollPane(databaseTable1);	
        scrollPane1.setPreferredSize(new Dimension(480,100));
      
        
        myTable2 = new LocationTable();
        databaseTable2 = myTable2.buildMyTable(columnName, data2); 
        scrollPane2 = new JScrollPane(databaseTable2);	
        scrollPane2.setPreferredSize(new Dimension(480,100));
            
        JButton buttonAdd = new JButton("Add Location"); 
        buttonAdd.setPreferredSize(new Dimension(150,25));
        JButton buttonDelete = new JButton("Delete Selected Row");
        JButton buttonHelp = new JButton("Help");
        JButton buttonOK = new JButton("OK");
        
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
				String dd= "";
				System.out.print(dd);
			
				
			}							
		}						
		);
        
        databaseTable1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {              
              int r= databaseTable1.getSelectedRow();
              rowIndex = r;
              databaseTable2.isRowSelected(rowIndex);
              databaseTable2.changeSelection(rowIndex, 0,false, false);
            }
        }); 
	    
	    databaseTable2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {              
              int r= databaseTable2.getSelectedRow();              
              rowIndex = r;
              databaseTable1.isRowSelected(rowIndex);
              databaseTable1.changeSelection(rowIndex, 0,false, false);
            }
        });
        
     // setup layout
        panel.setLayout(new GridBagLayout());
		gc = new GridBagConstraints();		
		gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(0,5,5,0);
		
        gc.gridx = 0;
		gc.gridy = 0;
		
		panel.add(label_1,gc);
		gc.gridy = 1;
		panel.add(textLocation,gc);
		gc.gridx = 2;
		panel.add(buttonAdd,gc);
		gc.gridwidth = 3;
		gc.gridx = 4;		
		panel.add(buttonDelete,gc);
		
		gc.gridx = 0;
		
		gc.gridy = 2;
		gc.gridwidth = 7;
		panel.add(label_2,gc);			
		gc.gridy = 3;
		panel.add(label_3,gc);
		gc.gridy = 4;		
		panel.add(scrollPane1,gc);
		gc.gridy = 9;
		gc.gridheight = 1;
		panel.add(label_4,gc);
		//gc.gridy = 10;
		//gc.gridheight = 3;
		//scrollPane2.setVisible(false);
		//panel.add(scrollPane2,gc);
		

		gc.gridx = 5;
		gc.gridy = 15;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		panel.add(buttonHelp,gc);
		gc.gridx = 6;
		panel.add(buttonOK,gc);
		
        
        //add(panel);        
       // panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(500,400));
        //panel.setResizable(false);
        
        
        
        
		
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
			databaseTable1.updateUI();
			databaseTable2.updateUI();
		}
    }
	
	
	
	
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
