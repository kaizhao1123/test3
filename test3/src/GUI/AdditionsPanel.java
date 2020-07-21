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
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;

import Controller.PanelManager;
import Model_Entity.AdditionsPanelOutputElement;
import Model_Entity.AnimalPanelOutputElement;
import Model_Entity.BeddingInfo;
import Model_Entity.LocationPanelOutputElement;
import Model_Tables.AdditionsTable;


/**
 * This class is to create the additions panel. The purpose of this class is to
 * characterize the amount of flush water, waste water, and bedding added to the
 * manure waste stream for each of the locations identified on the Locations
 * screen.
 * 
 * @author Kai Zhao
 *
 */
public class AdditionsPanel extends JPanel {

	MainFrame parent;
	JTabbedPane pane;
	OperatingPeriodDialog periodDialog;
	PanelManager panelManager;
	RunoffPanel runoffPanel;
	WashWaterDialog washDialog;
	FlushWaterDialog flushDialog;
	MgmtTrainPanel mgmtTrainPanel;

	/***********************************************************
	 * declare the data structures used in this panel
	 */

	ArrayList<String> streamNames;	// the first column's data
	ArrayList<BeddingInfo> beddingDataset;	// all beddingInfos
	ArrayList<String> beddingType;	// used for showing in the JCombobox
	
	ArrayList<AdditionsPanelOutputElement> outputOfTable_1; 
	ArrayList<AdditionsPanelOutputElement> outputOfTable_2; 
	ArrayList<ArrayList<AdditionsPanelOutputElement>> additionsPanelOutput;

	/************************************************************
	 * declare the elements of this panel
	 */

	JPanel panel;
	GridBagConstraints gc;
	JLabel label_1;
	JLabel label_2;
	JLabel label_3;
	JLabel label_4;
	JTextField textAdd;
	AdditionsTable myTable1;
	AdditionsTable myTable2;
	JTable databaseTable1;
	JTable databaseTable2;
	JScrollPane scrollPane1;
	JScrollPane scrollPane2;

	// the header of table
	String[] columnName = { "<html> Waste Streams  <br> ---Units --->  </html>",
			"<html>Wash Water  <br> (gal/day) </html>", "<html>Flush Water  <br> (gal/day) </html>",
			"<html>Bedding Type  <br>   </html>", "<html>Eff  <br> (Density) </html>",
			"<html> Amout <br> (lbs/day) </html>", "<html> LV Amt <br> (cu.ft/day) </html>",
			"<html> Cv Amt <br> (cu.ft/day) </html>" };
	Object data1[][];	// for table1
	Object data2[][];	// for table2
	DecimalFormat df = new DecimalFormat("0.00");
	
	JComboBox<String> comboboxType;
	JButton buttonAdd;
	JButton buttonReset;
	JButton buttonHelp;
	JButton buttonOK;

	String firstPeriod;
    String secondPeriod;
	
	/**
	 * The constructor of this panel
	 * 
	 * @param pm
	 */
	public AdditionsPanel(PanelManager pm) {

		panelManager = pm;
		initialData();
		initalElements();
		initalListeners();
		initialLayout();
	}

	// initials the data structures in this panel, mainly to get the input data.
	private void initialData() {
		panel = this;
		// get the bedding data
		beddingDataset = panelManager.allBeddingData;
		
		// get the beddingType data
		beddingType = new ArrayList<>();
		for (int i = 0; i < beddingDataset.size(); i++) {
			beddingType.add(beddingDataset.get(i).name);
		}

		// get the initialized elements in the first column of table
		streamNames = panelManager.getLocationElements();

		// initial the other table data
		data1 = new Object[streamNames.size()][8];
		for (int i = 0; i < streamNames.size(); i++) {
			data1[i][0] = streamNames.get(i);
			for (int j = 1; j < 8; j++) {
				if (j == 3)
					data1[i][j] = "";
				else
					data1[i][j] = "0.00";
			}
		}
		data2 = new Object[streamNames.size()][8];
		for (int i = 0; i < streamNames.size(); i++) {
			data2[i][0] = streamNames.get(i);
			for (int j = 1; j < 8; j++) {
				if (j == 3)
					data2[i][j] = "";
				else
					data2[i][j] = "0.00";
			}
		}

	}

	// initials the elements of this panel
	private void initalElements() {
		label_1 = new JLabel("Aditional Waste Stream");
		label_2 = new JLabel(
				"<html> Note: Do not add recycled water. That has already been accounted for in the system. <br> "
						+ "Only add newly introduced water to the system for wash and flush water. </html>");
		label_2.setForeground(Color.RED);
		
		label_3 = new JLabel(" ");
		label_4 = new JLabel(" ");
		
		textAdd = new JTextField();
		textAdd.setPreferredSize(new Dimension(130, 25));

		comboboxType = new JComboBox<String>();
		comboboxType.addItem(" ");
		for (int i = 0; i < beddingType.size(); i++) {
			comboboxType.addItem(beddingType.get(i));
		}

		buttonAdd = new JButton("Add");
		buttonReset = new JButton("Reset Effective Densities");
		buttonHelp = new JButton("Help");
		buttonOK = new JButton("OK");
		
		myTable1 = new AdditionsTable();
		databaseTable1 = myTable1.buildMyTable(columnName, data1, beddingDataset);
		databaseTable1.enable(); // to get different mouse click count, or the mouse click count always be 1.
		scrollPane1 = new JScrollPane(databaseTable1);
		scrollPane1.setPreferredSize(new Dimension(670, 100));
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane1.setViewportBorder(border);
		scrollPane1.setBorder(border);
		TableColumn sportColumnInTable1 = databaseTable1.getColumnModel().getColumn(3);
		// set the cell editor of the 3rd column to JComboBox, to show the bedding type.
		sportColumnInTable1.setCellEditor(new DefaultCellEditor(comboboxType));
		
		myTable2 = new AdditionsTable();
		databaseTable2 = myTable2.buildMyTable(columnName, data2, beddingDataset);
		databaseTable2.enable(); // to get different mouse click count, or the mouse click count always be 1.
		scrollPane2 = new JScrollPane(databaseTable2);
		scrollPane2.setPreferredSize(new Dimension(670, 100));
		//Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane2.setViewportBorder(border);
		scrollPane2.setBorder(border);
		TableColumn sportColumnInTable2 = databaseTable2.getColumnModel().getColumn(3);
		// set the cell editor of the 3rd column to JComboBox, to show the bedding type.
		sportColumnInTable2.setCellEditor(new DefaultCellEditor(comboboxType));

		gc = new GridBagConstraints();
	}


	
	// initial listeners of this panel
	private void initalListeners() {

		// adds new waste stream, the stream names cannot be duplicated.
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (databaseTable1.isEditing())
					databaseTable1.getCellEditor().stopCellEditing();
				if (databaseTable2.isEditing())
					databaseTable2.getCellEditor().stopCellEditing();
				if (textAdd != null) {
					String s = textAdd.getText();
					String preItem = myTable1.model.data[databaseTable1.getRowCount()-1][0].toString();
					if(s.length() > 0) {
						addTableRow(s, preItem);
						myTable1.updateElementList(s);
					}
					if(pane != null) {
						try {
							int index = pane.indexOfTab("Mgmt Train"); 				
							if(index >= 0) {
								mgmtTrainPanel = (MgmtTrainPanel) pane.getComponentAt(index);
								int r = 0;
								for(int i = 0; i < mgmtTrainPanel.jTable1.getRowCount(); i++) {
									if(mgmtTrainPanel.myTable1.model.data[i][0].toString().equals(preItem))
										r = i;									
								}
								mgmtTrainPanel.addRow(s, r+1);
							}
						}catch(Exception ef) {
							
						}
					}
				}				
			}
		});
		
		// Resets back the original effective densities if some EFF changed.
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (databaseTable1 != null) {
					if (databaseTable1.isEditing())
						databaseTable1.getCellEditor().stopCellEditing();
					if (databaseTable2.isEditing())
						databaseTable2.getCellEditor().stopCellEditing();
					for(int i = 0; i < databaseTable1.getRowCount(); i++) {
						Object[] ele = myTable1.model.data[i];
						resetEffectiveDensities(ele);
					}					
					databaseTable1.updateUI();
					for(int i = 0; i < databaseTable2.getRowCount(); i++) {
						Object[] ele = myTable2.model.data[i];
						resetEffectiveDensities(ele);
					}					
					databaseTable2.updateUI();				
				}
			}
		});
		
		/*
		 * when click cell in the "wash water" column, open washWaterDialog, or double click 
		 * the cell to enter the data directly;
		 * when click cell in the "flush water" column, open flushWaterDialog, or double click
		 * the cell to enter the data directly;
		 * 
		 */	
		databaseTable1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {	
				int row = databaseTable1.rowAtPoint(e.getPoint()); 
	            int col = databaseTable1.columnAtPoint(e.getPoint()); 
	            if (databaseTable2.getCellEditor() != null)
					databaseTable2.getCellEditor().stopCellEditing();
	            if(col == 1) {
	            	if(e.getClickCount() == 2) {
	            		
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}	            		

						databaseTable1.enable(true);
						databaseTable1.editCellAt(row, col);
	            	}
	            	else if(e.getClickCount() == 1) {
	            		//if (databaseTable2.getCellEditor() != null)
						//	databaseTable2.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}
	            		washDialog = new WashWaterDialog(myTable1, databaseTable1);
	            		databaseTable1.enable(false);
	            	}	            	
	            }
	            else if(col == 2) {
	            	if(e.getClickCount() == 2) {
	            	//	if (databaseTable2.getCellEditor() != null)
					//		databaseTable2.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}	            		
						databaseTable1.enable(true);
						databaseTable1.editCellAt(row, col);
	            	}
	            	else if(e.getClickCount() == 1) {
	            	//	if (databaseTable2.getCellEditor() != null)
					//		databaseTable2.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}
	            		
	            		flushDialog = new FlushWaterDialog(myTable1, databaseTable1);
	            		databaseTable1.enable(false);
	            	}	            	
	            }
	            else {
	            	if(washDialog != null) {
            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
						washDialog.setVisible(false);
            		}	            			
            		if(flushDialog != null) {
            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
            			flushDialog.setVisible(false);
            		}
            		databaseTable1.enable(true);
	            }
	            	            				
				databaseTable1.repaint();
			}
		});
		
		databaseTable2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {	
				int row = databaseTable2.rowAtPoint(e.getPoint()); 
	            int col = databaseTable2.columnAtPoint(e.getPoint()); 
	            if (databaseTable1.getCellEditor() != null)
					databaseTable1.getCellEditor().stopCellEditing();
	            if(col == 1) {
	            	if(e.getClickCount() == 2) {
	            		//if (databaseTable1.getCellEditor() != null)
						//	databaseTable1.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}	            		

						databaseTable2.enable(true);
						databaseTable2.editCellAt(row, col);
	            	}
	            	else if(e.getClickCount() == 1) {
	            	//	if (databaseTable1.getCellEditor() != null)
					//		databaseTable1.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}
	            		washDialog = new WashWaterDialog(myTable2, databaseTable2);
	            		databaseTable2.enable(false);
	            	}	            	
	            }
	            else if(col == 2) {
	            	if(e.getClickCount() == 2) {
	            	//	if (databaseTable1.getCellEditor() != null)
					//		databaseTable1.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}	            		
						databaseTable2.enable(true);
						databaseTable2.editCellAt(row, col);
	            	}
	            	else if(e.getClickCount() == 1) {
	            	//	if (databaseTable1.getCellEditor() != null)
					//		databaseTable1.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}
	            		
	            		flushDialog = new FlushWaterDialog(myTable2, databaseTable2);
	            		databaseTable2.enable(false);
	            	}	            	
	            }
	            else {
	            	if(washDialog != null) {
            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
						washDialog.setVisible(false);
            		}	            			
            		if(flushDialog != null) {
            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
            			flushDialog.setVisible(false);
            		}
            		databaseTable2.enable(true);
	            }
	            	            				
				databaseTable2.repaint();
			}
		});
		

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				pane = parent.tabbedPane;
				if (databaseTable1.isEditing())
					databaseTable1.getCellEditor().stopCellEditing();
				if (databaseTable2.isEditing())
					databaseTable2.getCellEditor().stopCellEditing();
				try {
					if (runoffPanel == null) {
						getOutput();
						panelManager.storeAdditionsPanelOutput(additionsPanelOutput);
						runoffPanel = new RunoffPanel(panelManager);
						runoffPanel.setParent(parent);
						pane.add("runoff", runoffPanel);
					}					
					else {
						if(additionsPanelOutput != null)
							updateOutput();
					}
					/*
					 * else { pane.remove(index);
					 * 
					 * //System.out.print(index);
					 * 
					 * animal = new AnimalPanel(pane,animalData,source); animal.setParent(parent);
					 * pane.insertTab("animal", null, animal, null, index); }
					 */
					pane.setSelectedIndex(pane.indexOfTab("runoff"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}

	// initials the layout of this panel
	private void initialLayout() {
		setLayout(new GridBagLayout());
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.insets = new Insets(5, 5, 5, 5);

		gc.gridx = 0;
		gc.gridy = 0;
		add(label_1, gc);

		// gc.gridx = 0;
		gc.gridy = 1;
		add(textAdd, gc);
		gc.gridx = 2;
		// gc.gridwidth = 3;
		add(buttonAdd, gc);
		gc.anchor = GridBagConstraints.CENTER;

		gc.gridx = 4;
		gc.gridwidth = 3;
		add(buttonReset, gc);

		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 6;
		add(label_2, gc);
		
		gc.gridy = 3;
		add(label_3, gc);
		
		gc.gridy = 4;
		gc.gridheight = 6;
		add(scrollPane1, gc);
		
		gc.gridy = 10;
		gc.gridheight = 1;
		add(label_4, gc);

		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 4;
		gc.gridy = 17;
		gc.gridwidth = 3;
		gc.gridheight = 1;
		add(buttonHelp, gc);
		gc.anchor = GridBagConstraints.EAST;
		// gc.gridx = 4;
		gc.gridwidth = 2;
		add(buttonOK, gc);

	}
	
	// gets the output of this panel
	private void getOutput() {
		outputOfTable_1 = new ArrayList<>();
		outputOfTable_2 = new ArrayList<>();
		additionsPanelOutput = new ArrayList<>();
		for(int i = 0; i < databaseTable1.getRowCount(); i++) {
			String name = myTable1.model.data[i][0].toString();
			String[] data = new String[3];
			data[0] = myTable1.model.data[i][1].toString(); // wash water
			data[1] = myTable1.model.data[i][2].toString(); // flush water
			data[2] = myTable1.model.data[i][7].toString(); // CV
			AdditionsPanelOutputElement additionsElement = new AdditionsPanelOutputElement(name, data);
			outputOfTable_1.add(additionsElement);							
		}
		for(int i = 0; i < databaseTable2.getRowCount(); i++) {
			String name = myTable2.model.data[i][0].toString();
			String[] data = new String[3];
			data[0] = myTable2.model.data[i][1].toString(); // wash water
			data[1] = myTable2.model.data[i][2].toString(); // flush water
			data[2] = myTable2.model.data[i][7].toString(); // CV
			AdditionsPanelOutputElement additionsElement = new AdditionsPanelOutputElement(name, data);
			outputOfTable_2.add(additionsElement);							
		}
		additionsPanelOutput.add(outputOfTable_1);
		additionsPanelOutput.add(outputOfTable_1);
	}
	// update the output when values of this panel change
		private void updateOutput() {
			getOutput();
			panelManager.storeAdditionsPanelOutput(additionsPanelOutput);
		}
	
	// Corresponding the first option of periodDialog, that is, only table1 will be shown
	public void update1() {
    	label_3.setText(" ");
    	label_4.setText(" ");
    	panel.remove(scrollPane2);
    	//twoPeriod = false;
    	//panel.setSize(new Dimension(700,400));
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
        gc.insets = new Insets(5,5,5,5);
		gc.gridx = 0;
    	gc.gridy = 11;
		gc.gridheight = 6;
		gc.gridwidth = 6;
		panel.add(scrollPane2,gc);
		//twoPeriod = true;
		//panel.setSize(new Dimension(700,700));
		panel.updateUI();
    }
    
	// Resets back the original effective densities
	private void resetEffectiveDensities(Object[] ele) {		
		String s = ele[3].toString();
		BeddingInfo bed = null;
		for(int j = 0; j < beddingDataset.size(); j++) {
			if(beddingDataset.get(j).name.equals(s)){
				bed = beddingDataset.get(j);
				ele[4] = bed.eff_Density;
			}
		}					
		Double aDou = Double.parseDouble(ele[5].toString());
		Double dDou = 0.00;
		if(bed != null)
			dDou = Double.parseDouble(bed.density);			
		Double edDou = Double.parseDouble(ele[4].toString());
		
		ele[6] = df.format(aDou / dDou);
		ele[7] = df.format(aDou / edDou);
	}
	
	/**
	 * adds the row data, with the limitation that the stream name can't be duplicated.
	 * it is also associated with locationsPanel: the change of row count of the locationsPanel affect this table.
	 * @param s
	 */
	public void addTableRow(String s, String preItem) {
		if(!streamNames.contains(s)) {
			int col = myTable1.model.getColumnCount();
			String[] dataTable = new String[col];
			dataTable[0] = s;
			for (int i = 1; i < col; i++) {
				if (i == 3)
					dataTable[i] = "";
				else
					dataTable[i] = "0.00";
			}
			int rowIndex = -1;
			for(int i = 0; i < myTable1.model.getRowCount(); i++) {
				if(myTable1.model.data[i][0].toString().equals(preItem))
					rowIndex = i;
			}
			myTable1.model.insertRow(dataTable, rowIndex+1);
			myTable2.model.insertRow(dataTable, rowIndex+1);
			databaseTable1.updateUI();
			databaseTable2.updateUI();
			
			textAdd.setText("");
			streamNames.add(s);
		}
		else
			JOptionPane.showMessageDialog(null,"You cannot have two waste streams with"
					+ "the same name!" + s);		
	}
	
	// associated with locationsPanel: the change of row count of the locationsPanel affect this table.
	public void deleteTableRow(String s) {
		int rowIndex = 0;
		for(int i = 0; i < myTable1.model.data.length; i++) {
			if(myTable1.model.data[i][0].toString().equals(s))
				rowIndex = i;
		}
		
		myTable1.model.deleteRow(rowIndex);
		myTable2.model.deleteRow(rowIndex);
		databaseTable1.updateUI();
		databaseTable2.updateUI();
		streamNames.remove(s);	

		if(myTable1.model.data.length == 0) {
			myTable1.model.data = null;
			myTable2.model.data = null;
		}		
	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
