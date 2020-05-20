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

import AWS.PanelManager;
import Entity.AdditionsTable;

public class AdditionsPanel extends JPanel {

	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	RunoffPanel runoffPanel;
	WashWaterDialog washDialog;

	JLabel label_1;
	JLabel label_2;
	JTextField textAdd;
	AdditionsTable myTable;
	JTable databaseTable;
	JScrollPane scrollPane;

	String[] columnName = { "<html> Waste Streams  <br> ---Units --->  </html>", // the header of table
			"<html>Wash Water  <br> (gal/day) </html>", "<html>Flush Water  <br> (gal/day) </html>",
			"<html>Bedding Type  <br>   </html>", "<html>Eff  <br> (Density) </html>",
			"<html> Amout <br> (lbs/day) </html>", "<html> LV Amt <br> (cu.ft/day) </html>",
			"<html> Cv Amt <br> (cu.ft/day) </html>" };
	Object data[][] = { { "dd", "0.00", "0.00", " 0.00", "0.00", "0.00", "0.00", "0.00" },
			{ "ss", "0.00", "0.00", " 0.00", "0.00", "0.00", "0.00", "0.00" } };

	JButton buttonAdd;
	JButton buttonReset;
	JButton buttonHelp;
	JButton buttonOK;
	JPanel panel;
	GridBagConstraints gc;
	JComboBox comboboxType;
	ArrayList<String> beddingType = new ArrayList();

	public AdditionsPanel(PanelManager pm) {

		panelManager = pm;
		initialData();
		initalElements();
		initalActionLiseners();
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		initialLayout(gc);
	}

	private void initialData() {
		panel = this;
		beddingType.add("abc");
		beddingType.add("bcd");
		beddingType.add("cde");
	}

	private void initalElements() {
		label_1 = new JLabel("Aditional Waste Stream");
		label_2 = new JLabel(
				"<html> Note: Do not add recycled water. That has already been accounted for in the system. <br> "
						+ "Only add newly introduced water to the system for wash and flush water. </html>");
		textAdd = new JTextField();
		textAdd.setPreferredSize(new Dimension(130, 25));

		comboboxType = new JComboBox();
		for (int i = 0; i < beddingType.size(); i++) {
			comboboxType.addItem(beddingType.get(i));
		}

		buttonAdd = new JButton("Add");
		buttonReset = new JButton("Reset Effective Densities");
		buttonHelp = new JButton("Help");
		buttonOK = new JButton("OK");
		myTable = new AdditionsTable();
		databaseTable = myTable.buildMyTable(columnName, data);
		scrollPane = new JScrollPane(databaseTable);
		scrollPane.setPreferredSize(new Dimension(600, 100));

		TableColumn sportColumn = databaseTable.getColumnModel().getColumn(3);
		sportColumn.setCellEditor(new DefaultCellEditor(comboboxType));
	}

	private void initalActionLiseners() {

		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
					myTable.setColor(0, databaseTable.getRowCount() - 1, 6, databaseTable.getColumnCount() - 1,
							Color.cyan);
					databaseTable.updateUI();

					textAdd.setText("");
				}
			}
		});

		databaseTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int col = databaseTable.getSelectedColumn();
				if (col == 1) {
					WashWaterDialog modelDialog = new WashWaterDialog(myTable, databaseTable);
				} else if (col == 2) {
					FlushWaterDialog modelDialog = new FlushWaterDialog(myTable, databaseTable);
				}
				databaseTable.repaint();
			}
		});

		buttonOK.addActionListener(new ActionListener() // After selected the data source, open the climate frame with
		// data;
		{
			public void actionPerformed(ActionEvent e) {

				pane = parent.tabbedPane;

				try {
					if (runoffPanel == null) {
						//panelManager.storeClimatePanelOutput(Output);
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

	private void initialLayout(GridBagConstraints gc) {
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

		gc.anchor = GridBagConstraints.EAST;
		gc.gridx = 4;
		gc.gridy = 9;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		add(buttonHelp, gc);
		gc.anchor = GridBagConstraints.EAST;
		gc.gridx = 5;
		gc.gridwidth = 1;
		add(buttonOK, gc);

	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
