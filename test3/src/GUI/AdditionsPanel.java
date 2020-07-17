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
	PanelManager panelManager;
	RunoffPanel runoffPanel;
	WashWaterDialog washDialog;
	FlushWaterDialog flushDialog;

	/***********************************************************
	 * declare the data structures used in this panel
	 */

	ArrayList<String> streamNames;	// the first column's data
	ArrayList<BeddingInfo> dataset;	// all beddingInfos
	ArrayList<String> beddingType;	// used for showing in the JCombobox
	// store the output of panel: the name, and part of data of each animal(manure and ts).
	ArrayList<AdditionsPanelOutputElement> additionsPanelOutput; 

	/************************************************************
	 * declare the elements of this panel
	 */

	JPanel panel;
	GridBagConstraints gc;
	JLabel label_1;
	JLabel label_2;
	JTextField textAdd;
	AdditionsTable myTable;
	JTable databaseTable;
	JScrollPane scrollPane;

	// the header of table
	String[] columnName = { "<html> Waste Streams  <br> ---Units --->  </html>",
			"<html>Wash Water  <br> (gal/day) </html>", "<html>Flush Water  <br> (gal/day) </html>",
			"<html>Bedding Type  <br>   </html>", "<html>Eff  <br> (Density) </html>",
			"<html> Amout <br> (lbs/day) </html>", "<html> LV Amt <br> (cu.ft/day) </html>",
			"<html> Cv Amt <br> (cu.ft/day) </html>" };
	Object data[][];
	DecimalFormat df = new DecimalFormat("0.00");
	
	JComboBox<String> comboboxType;
	JButton buttonAdd;
	JButton buttonReset;
	JButton buttonHelp;
	JButton buttonOK;

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
		dataset = panelManager.allBeddingData;
		
		// get the beddingType data
		beddingType = new ArrayList<>();
		for (int i = 0; i < dataset.size(); i++) {
			beddingType.add(dataset.get(i).name);
		}

		// get the initialized elements in the first column of table
		streamNames = panelManager.getLocationElements();

		// initial the other table data
		data = new Object[streamNames.size()][8];
		for (int i = 0; i < streamNames.size(); i++) {
			data[i][0] = streamNames.get(i);
			for (int j = 1; j < 8; j++) {
				if (j == 3)
					data[i][j] = "";
				else
					data[i][j] = "0.00";
			}
		}

	}

	// initials the elements of this panel
	private void initalElements() {
		label_1 = new JLabel("Aditional Waste Stream");
		label_2 = new JLabel(
				"<html> Note: Do not add recycled water. That has already been accounted for in the system. <br> "
						+ "Only add newly introduced water to the system for wash and flush water. </html>");
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
		myTable = new AdditionsTable();
		databaseTable = myTable.buildMyTable(columnName, data, dataset);
		databaseTable.enable(); // to get different mouse click count, or the mouse click count always be 1.
		scrollPane = new JScrollPane(databaseTable);
		scrollPane.setPreferredSize(new Dimension(670, 100));
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane.setViewportBorder(border);
		scrollPane.setBorder(border);
		TableColumn sportColumn = databaseTable.getColumnModel().getColumn(3);
		// set the cell editor of the 3rd column to JComboBox, to show the bedding type.
		sportColumn.setCellEditor(new DefaultCellEditor(comboboxType));

		gc = new GridBagConstraints();
	}


	
	// initial listeners of this panel
	private void initalListeners() {

		// adds new waste stream, the stream names cannot be duplicated.
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (databaseTable.isEditing())
					databaseTable.getCellEditor().stopCellEditing();
				if (textAdd != null) {
					String s = textAdd.getText();
					if(s.length() > 0) {
						addTableRow(s);
						myTable.updateElementList(s);
					}
										
				}
			}
		});
		
		// Resets back the original effective densities if some EFF changed.
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (databaseTable != null) {
					if (databaseTable.isEditing())
						databaseTable.getCellEditor().stopCellEditing();
					for(int i = 0; i < databaseTable.getRowCount(); i++) {
						Object[] ele = myTable.model.data[i];
						String s = ele[3].toString();
						BeddingInfo bed = null;
						for(int j = 0; j < dataset.size(); j++) {
							if(dataset.get(j).name.equals(s)){
								bed = dataset.get(j);
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
					databaseTable.updateUI();
					
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
		databaseTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {	
				int row = databaseTable.rowAtPoint(e.getPoint()); 
	            int col = databaseTable.columnAtPoint(e.getPoint()); 
	            if(col == 1) {
	            	if(e.getClickCount() == 2) {
	            		if (databaseTable.getCellEditor() != null)
							databaseTable.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}	            		

						databaseTable.enable(true);
						databaseTable.editCellAt(row, col);
	            	}
	            	else if(e.getClickCount() == 1) {
	            		if (databaseTable.getCellEditor() != null)
							databaseTable.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}
	            		washDialog = new WashWaterDialog(myTable, databaseTable);
	            		databaseTable.enable(false);
	            	}	            	
	            }
	            else if(col == 2) {
	            	if(e.getClickCount() == 2) {
	            		if (databaseTable.getCellEditor() != null)
							databaseTable.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}	            		
						databaseTable.enable(true);
						databaseTable.editCellAt(row, col);
	            	}
	            	else if(e.getClickCount() == 1) {
	            		if (databaseTable.getCellEditor() != null)
							databaseTable.getCellEditor().stopCellEditing();
	            		if(washDialog != null) {
	            			washDialog.setDefaultCloseOperation(washDialog.DISPOSE_ON_CLOSE);
							washDialog.setVisible(false);
	            		}	            			
	            		if(flushDialog != null) {
	            			flushDialog.setDefaultCloseOperation(flushDialog.DISPOSE_ON_CLOSE);
	            			flushDialog.setVisible(false);
	            		}
	            		
	            		flushDialog = new FlushWaterDialog(myTable, databaseTable);
	            		databaseTable.enable(false);
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
            		databaseTable.enable(true);
	            }
	            	            				
				databaseTable.repaint();
			}
		});
		
		

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				pane = parent.tabbedPane;
				if (databaseTable.isEditing())
					databaseTable.getCellEditor().stopCellEditing();
				try {
					if (runoffPanel == null) {
						getOutput();
						panelManager.storeAdditionsPanelOutput(additionsPanelOutput);
						runoffPanel = new RunoffPanel(panelManager);
						runoffPanel.setParent(parent);
						pane.add("runoff", runoffPanel);
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
		gc.gridheight = 6;
		add(scrollPane, gc);

		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 4;
		gc.gridy = 9;
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
		additionsPanelOutput = new ArrayList<>();
		for(int i = 0; i < databaseTable.getRowCount(); i++) {
			String name = myTable.model.data[i][0].toString();
			String[] data = new String[3];
			data[0] = myTable.model.data[i][1].toString(); // wash water
			data[1] = myTable.model.data[i][2].toString(); // flush water
			data[2] = myTable.model.data[i][7].toString(); // CV
			AdditionsPanelOutputElement additionsElement = new AdditionsPanelOutputElement(name, data);
			additionsPanelOutput.add(additionsElement);
							
		}				
	}
	
	/**
	 * adds the row data, with the limitation that the stream name can't be duplicated.
	 * it is also associated with locationsPanel: the change of row count of the locationsPanel affect this table.
	 * @param s
	 */
	public void addTableRow(String s) {
		if(!streamNames.contains(s)) {
			int col = myTable.model.getColumnCount();
			String[] dataTable = new String[col];
			dataTable[0] = s;
			for (int i = 1; i < col; i++) {
				if (i == 3)
					dataTable[i] = "";
				else
					dataTable[i] = "0.00";
			}

			myTable.model.insertRow(dataTable, myTable.model.getRowCount()-1);

			databaseTable.updateUI();
			
			//System.out.print(myTable.model.data.length);
			//System.out.print("---");
			//System.out.print(databaseTable.getColumnCount());
			
			
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
		for(int i = 0; i < myTable.model.data.length; i++) {
			if(myTable.model.data[i][0].toString().equals(s))
				rowIndex = i;
		}
		myTable.model.deleteRow(rowIndex);
		streamNames.remove(s);
		databaseTable.updateUI();
	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
