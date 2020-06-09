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

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import Controller.PanelManager;
import Model_Entity.BeddingInfo;
import Model_Tables.AdditionsTable;
/**
 * This class is to create the additions panel.
 * The purpose of this class is to characterize the amount of flush water, 
 * waste water, and bedding added to the manure waste stream for each of the
 * locations identified on the Locations screen.
 * @author Kai Zhao
 *
 */
public class AdditionsPanel extends JPanel {

	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	RunoffPanel runoffPanel;
	WashWaterDialog washDialog;
	
	/***********************************************************
	 * declare the data structures used in this panel
	 */
	
	ArrayList<String> streamName;
	ArrayList<BeddingInfo> dataset;
	ArrayList<String> beddingType;

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

	JComboBox<String> comboboxType;
	JButton buttonAdd;
	JButton buttonReset;
	JButton buttonHelp;
	JButton buttonOK;

	/**
	 * The constructor of this panel
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
		beddingType = new ArrayList<>();
		for (int i = 0; i < dataset.size(); i++) {
			beddingType.add(dataset.get(i).name);
		}

		// get the first column data of table
		streamName = panelManager.locationPanelOutput;

		// initial the other table data
		data = new Object[streamName.size()][8];
		for (int i = 0; i < streamName.size(); i++) {
			data[i][0] = streamName.get(i);
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
		scrollPane = new JScrollPane(databaseTable);
		scrollPane.setPreferredSize(new Dimension(660, 100));
		TableColumn sportColumn = databaseTable.getColumnModel().getColumn(3);
		// set the cell editor of the 3rd column to JComboBox, to show the bedding type.
		sportColumn.setCellEditor(new DefaultCellEditor(comboboxType)); 
		
		gc = new GridBagConstraints();
	}

	// initial listeners of this panel
	private void initalListeners() {

		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (databaseTable.isEditing())
					databaseTable.getCellEditor().stopCellEditing(); 
				if (textAdd != null) {
					String s = textAdd.getText();
					int col = myTable.model.getColumnCount();
					String[] dataTable = new String[col];
					dataTable[0] = s;
					for (int i = 1; i < col; i++) {
						if (i == 3)
							dataTable[i] = "";
						else
							dataTable[i] = "0.00";
					}

					myTable.model.addRow(dataTable);
					// data = myTable.model.data;
					myTable.newElement = s;
					myTable.setColorAndFont(0, databaseTable.getRowCount() - 1, 6, databaseTable.getColumnCount() - 1,
							Color.cyan);

					databaseTable.updateUI();

					textAdd.setText("");
				}
			}
		});

		/*
		 *  when click cell in the "wash water" column, open washWaterDialog, to enter the data;
		 *  when click cell in the "flush water" column, open flushWaterDialog, to enter the data;
		 *  
		 */
		databaseTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (databaseTable.isEditing())
					databaseTable.getCellEditor().stopCellEditing(); 
				int col = databaseTable.getSelectedColumn();
				if (col == 1) {
					WashWaterDialog modelDialog = new WashWaterDialog(myTable, databaseTable);
				} else if (col == 2) {
					FlushWaterDialog modelDialog = new FlushWaterDialog(myTable, databaseTable);
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
						// panelManager.storeClimatePanelOutput(Output);
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

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
