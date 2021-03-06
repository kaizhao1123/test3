package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Controller.PanelManager;
import Model_Tables.RunoffTable;
/**
 * This class is used to create the runoff panel.
 * The Runoff screen estimates the contaminated runoff that must be managed by the 
 * waste management system.  Runoff volumes estimated by AWM are conservative 
 * overestimates.  Because of this, the user is encouraged to use a method outside 
 * the program to determine the monthly and the 25-year, 24-hour runoff volumes, 
 * especially when larger watersheds are involved.
 * <p>
 * AWM computes runoff for two types of "watersheds": "impervious" watersheds such 
 * as roofs and frequently scraped concrete slabs; and "pervious" watersheds including 
 * feedlots with a manure package.
 * 
 * @author Kai Zhao
 *
 */
public class RunoffPanel extends JPanel {

	MainFrame parent;
	JTabbedPane pane;
	MgmtTrainPanel mgmtTrainPanel;
	PanelManager panelManager;
	
	/****************************************************************
	 * declare the data structures used in this panel
	 */
	String[] perData;
	int curveNum_30;
	double pervious25Yr;
	double valuePWA, valuePCN1, valuePCN2, valueIA, valueT1, valueT2;
	DecimalFormat df;

	// the column of the table
	String[] columnName = { " ", "Pervious", "Impervious", "Monthly Totals" };
	// the data from the database
	Object[][] tableData1 = { { "January", "0.00", "0.00", "0.00" }, { "February", "0.00", "0.00", "0.00" },
			{ "March", "0.00", "0.00", "0.00" }, { "April", "0.00", "0.00", "0.00" }, { "May", "0.00", "0.00", "0.00" },
			{ "June", "0.00", "0.00", "0.00" }, { "July", "0.00", "0.00", "0.00" },
			{ "August", "0.00", "0.00", "0.00" }, { "September", "0.00", "0.00", "0.00" },
			{ "October", "0.00", "0.00", "0.00" }, { "November", "0.00", "0.00", "0.00" },
			{ "December", "0.00", "0.00", "0.00" } };
	// the data for customer
	Object[][] tableData2 = { { "January", "0.00", "0.00", "0.00" }, { "February", "0.00", "0.00", "0.00" },
			{ "March", "0.00", "0.00", "0.00" }, { "April", "0.00", "0.00", "0.00" }, { "May", "0.00", "0.00", "0.00" },
			{ "June", "0.00", "0.00", "0.00" }, { "July", "0.00", "0.00", "0.00" },
			{ "August", "0.00", "0.00", "0.00" }, { "September", "0.00", "0.00", "0.00" },
			{ "October", "0.00", "0.00", "0.00" }, { "November", "0.00", "0.00", "0.00" },
			{ "December", "0.00", "0.00", "0.00" } };
	
	/****************************************************************
	 * declare the elements of this panel
	 */
	
	JPanel childPanel_1, childPanel_2;
	JLabel labelMethods;
	JLabel label_1, label_2;
	JRadioButton r1, r2, r3, r4;
	ButtonGroup bg;
	JLabel labelPWA, labelPCN1, labelPCN2, labelIA;
	JLabel label_3;
	JLabel label_4;
	JLabel labelRV;
	JLabel label_5;
	JLabel labelWarning;

	JTextField textPWA, textPCN1, textPCN2, textIA;
	JTextField text_1, text_2, text_3;

	JButton buttonHelp;
	JButton buttonOK;
	
	RunoffTable myTable1;
	JTable databaseTable;
	RunoffTable myTable2;
	JTable customerTable;
	JScrollPane scrollPane;

	GridBagConstraints gbc;

	/** 
	 * The constructor of this panel
	 * @param pm
	 */
	public RunoffPanel(PanelManager pm) {
		panelManager = pm;
		initialData();
		initialElements();
		initialListeners();
		initialLayout();
	}

	// initial data structures, mainly to get the input data.
	private void initialData() {
		df = new DecimalFormat("0.00");
		perData = panelManager.getPrecData();
		curveNum_30 = 77;
		pervious25Yr = panelManager.getPrecipitation25Yr();
		valuePWA = 0;
		valuePCN1 = 0;
		valuePCN2 = 0;
		valueIA = 0;
		valueT1 = 0;
		valueT2 = 0;
	}

	// initial the elements of this panel
	private void initialElements() {
		labelMethods = new JLabel("Methods for determining monthly runoff volumes:");
		label_1 = new JLabel("1.) Calculate volumes from climate and watershed data.");
		label_1.setFont(new Font(label_1.getFont().getName(), Font.PLAIN, 12));
		label_2 = new JLabel("2.) Enter runoff volumes directly in the table on the right.");
		label_2.setFont(new Font(label_2.getFont().getName(), Font.PLAIN, 12));
		initialPanel_1();
		initialPanel_2();
		labelRV = new JLabel("Runoff Volumes (1000 cu.ft)");
		labelRV.setFont(new Font(labelRV.getFont().getName(), Font.BOLD, 14));

		myTable1 = new RunoffTable();
		databaseTable = myTable1.buildMyTable(columnName, tableData1);
		databaseTable.setRowHeight(20);
		databaseTable.enable(false);
		myTable2 = new RunoffTable();
		customerTable = myTable2.buildMyTable(columnName, tableData2);
		customerTable.setRowHeight(20);
		scrollPane = new JScrollPane(databaseTable);
		scrollPane.setPreferredSize(new Dimension(310, 283));
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane.setViewportBorder(border);
		scrollPane.setBorder(border);

		label_5 = new JLabel("25-Yr 24-Hr Storm Runoff:");
		text_1 = new JTextField("0.00");
		text_1.setName("text_1");
		text_1.setPreferredSize(new Dimension(63, 25));
		text_1.setBackground(Color.lightGray);
		text_1.setEditable(false);
		text_2 = new JTextField("0.00");
		text_2.setName("text_2");
		text_2.setPreferredSize(new Dimension(73, 25));
		text_2.setBackground(Color.lightGray);
		text_2.setEditable(false);
		text_3 = new JTextField("0.00");
		text_3.setPreferredSize(new Dimension(102, 25));
		text_3.setBackground(Color.cyan);
		text_3.setEditable(false);

		labelWarning = new JLabel(
				"<html> WARNING: The volumes computed by the program are conservative over-estimates. <br>"
						+ "The user is encouraged to use a method outside of the program to compute runoff <br>"
						+ "volumes for larger watersheds and where precision is vital. Methods for computing <br>"
						+ "monthly runoff volumes include the NEH-4 stream gauge procedure and SPAW. </html>");
		labelWarning.setForeground(Color.red);
		buttonHelp = new JButton("Help");
		buttonHelp.setPreferredSize(new Dimension(60, 25));
		buttonOK = new JButton("OK");
		buttonOK.setPreferredSize(new Dimension(60, 25));

		gbc = new GridBagConstraints();
	}

	// initial the listeners of this panel
	private void initialListeners() {

		r1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r1.setSelected(true);
					r2.setSelected(false);
					if (customerTable.isEditing())
		            	  customerTable.getCellEditor().stopCellEditing(); 
					updateTablePerv(curveNum_30, valuePWA, perData);
					updateTableImperv(curveNum_30, valueIA, perData);
					updateRunoff25Yr(valuePCN1, valuePWA, valueIA);
					databaseTable.setEnabled(false);
					text_1.setEditable(false);
					text_2.setEditable(false);
					scrollPane.setViewportView(databaseTable);
					gbc.gridx = 0;
					gbc.gridy = 4;
					gbc.gridheight = 1;
					add(childPanel_2, gbc);
					updateUI();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		r2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r2.setSelected(true);
					r1.setSelected(false);
					text_1.setEditable(true);
					text_2.setEditable(true);
					customerTable = databaseTable;
					customerTable.enable(true);
					scrollPane.setViewportView(customerTable);
					remove(childPanel_2);
					updateUI();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		r3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				r3.setSelected(true);
				r4.setSelected(false);
				updateTextField(textPCN2);
			}
		});

		r4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				r4.setSelected(true);
				r3.setSelected(false);								
				updateTextField(textPCN2);
			}
		});

		textPWA.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTextField(textPWA);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTextField(textPWA);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});

		textPCN1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTextField(textPCN1);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTextField(textPCN1);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		textPCN2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTextField(textPCN2);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTextField(textPCN2);

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		textIA.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTextField(textIA);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTextField(textIA);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});

		text_1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTextField(text_1);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTextField(text_1);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});

		text_2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTextField(text_2);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTextField(text_2);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				pane = parent.tabbedPane;
				if (customerTable.isEditing())
					customerTable.getCellEditor().stopCellEditing(); 
				try {
					if (mgmtTrainPanel == null) {
						// panelManager.storeClimatePanelOutput(Output);
						mgmtTrainPanel = new MgmtTrainPanel(panelManager);
						mgmtTrainPanel.setParent(parent);
						if (parent.startPanel.periodDialog.secondOption == true) {
							mgmtTrainPanel.update2();
						}
						pane.add("Mgmt Train", mgmtTrainPanel);
					}
					/*
					 * else { pane.remove(index);
					 * 
					 * //System.out.print(index);
					 * 
					 * animal = new AnimalPanel(pane,animalData,source); animal.setParent(parent);
					 * pane.insertTab("animal", null, animal, null, index); }
					 */
					pane.setSelectedIndex(pane.indexOfTab("Mgmt Train"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		
	}

	// initial the layout of the panel
	private void initialLayout() {
		setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(5, 10, 5, 5);

		// first column
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		add(labelMethods, gbc);
		gbc.gridy = 1;
		add(label_1, gbc);
		gbc.gridy = 2;
		add(label_2, gbc);
		gbc.gridy = 3;
		gbc.gridheight = 1;
		add(childPanel_1, gbc);
		gbc.gridy = 4;
		gbc.gridheight = 1;
		add(childPanel_2, gbc);
		gbc.gridy = 6;
		gbc.gridheight = 2;
		gbc.gridwidth = 5;
		add(labelWarning, gbc);

		// second column
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 4;
		add(labelRV, gbc);
		gbc.gridy = 1;
		gbc.gridheight = 4;
		gbc.gridwidth = 5;
		add(scrollPane, gbc);

		// gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridheight = 1;
		gbc.gridwidth = 6;
		add(label_5, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(5, 82, 5, 0);
		gbc.gridx = 3;
		gbc.gridwidth = 1;
		add(text_1, gbc);
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.gridx = 4;
		add(text_2, gbc);
		gbc.gridx = 5;
		add(text_3, gbc);

		// button
		gbc.insets = new Insets(5, 10, 5, 5);
		gbc.gridx = 5;
		gbc.gridy = 6;
		add(buttonHelp, gbc);
		gbc.gridy = 7;
		add(buttonOK, gbc);
	}
	
	// initial the 1st child panel: the group of the radio buttons
	private void initialPanel_1() {
		childPanel_1 = new JPanel();
		r1 = new JRadioButton("Calculate Monthly Runoff Volumes");
		r1.setSelected(true);
		r2 = new JRadioButton("Enter Monthly Runoff Volumes");

		childPanel_1.setLayout(new GridLayout(2, 1));
		childPanel_1.add(r1);
		childPanel_1.add(r2);
		childPanel_1.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Runoff Volume Method"));
		childPanel_1.setPreferredSize(new Dimension(310, 80));
	}

	// initial the 2ed child panel: the four rows below the 1st panel.
	private void initialPanel_2() {
		childPanel_2 = new JPanel();
		labelPWA = new JLabel("Pervious Watershed Area: ");
		labelPCN1 = new JLabel("<html> Pervious Curve Number (1-day) <br>" + "for 25-Yr 24-Hr Storm Runoff: </html>");
		labelPCN2 = new JLabel("<html> Pervious Curve Number  <br>" + "for Monthly Runoff: </html>");
		labelIA = new JLabel("Impervious Area (roofs, slabs, etc): ");
		label_3 = new JLabel("acres");
		label_4 = new JLabel("sq.ft.");

		r3 = new JRadioButton("(1-day)");
		r3.setFont(new Font(r3.getFont().getName(), Font.PLAIN, 11));
		r3.setPreferredSize(new Dimension(70, 16));
		r3.setSelected(true);
		r4 = new JRadioButton("(30-day)");
		r4.setFont(new Font(r4.getFont().getName(), Font.PLAIN, 11));
		r4.setPreferredSize(new Dimension(70, 16));

		textPWA = new JTextField("0");
		textPWA.setName("textPWA");
		textPWA.setPreferredSize(new Dimension(50, 25));
		textPCN1 = new JTextField("90");
		textPCN1.setName("textPCN1");
		textPCN1.setPreferredSize(new Dimension(50, 25));
		textPCN2 = new JTextField("90");
		textPCN2.setName("textPCN2");
		textPCN2.setPreferredSize(new Dimension(50, 25));
		textIA = new JTextField("0");
		textIA.setName("textIA");
		textIA.setPreferredSize(new Dimension(50, 25));

		try {
			valuePWA = Double.parseDouble(textPWA.getText().toString());
		} catch (Exception e) {
			//valuePWA = 0;
		}
		try {
			valuePCN1 = Double.parseDouble(textPCN1.getText().toString());
		} catch (Exception e) {
			//valuePCN1 = 0;
		}
		try {
			valuePCN2 = Double.parseDouble(textPCN2.getText().toString());
		} catch (Exception e) {
			//valuePCN2 = 0;
		}
		try {
			valueIA = Double.parseDouble(textIA.getText().toString());
		} catch (Exception e) {
			//valueIA = 0;
		}

		childPanel_2.setLayout(new GridBagLayout());
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.anchor = GridBagConstraints.NORTHWEST;
		gc1.insets = new Insets(3, 2, 2, 2);

		// first row
		gc1.gridx = 0;
		gc1.gridy = 0;
		gc1.gridwidth = 2;
		childPanel_2.add(labelPWA, gc1);
		gc1.gridwidth = 1;
		gc1.gridx = 2;
		childPanel_2.add(textPWA, gc1);
		gc1.gridx = 4;
		childPanel_2.add(label_3, gc1);

		// second row
		gc1.gridx = 0;
		gc1.gridy = 1;
		gc1.gridwidth = 2;
		childPanel_2.add(labelPCN1, gc1);
		gc1.anchor = GridBagConstraints.CENTER;
		gc1.gridx = 2;
		gc1.gridwidth = 1;
		childPanel_2.add(textPCN1, gc1);

		// third row
		// gc1.anchor = GridBagConstraints.CENTER;
		gc1.gridx = 0;
		gc1.gridy = 2;
		gc1.gridheight = 2;
		childPanel_2.add(labelPCN2, gc1);
		gc1.anchor = GridBagConstraints.NORTHWEST;
		gc1.gridx = 1;
		gc1.gridheight = 1;
		childPanel_2.add(r3, gc1);
		gc1.insets = new Insets(0, 2, 0, 2);
		gc1.gridy = 3;
		childPanel_2.add(r4, gc1);
		gc1.anchor = GridBagConstraints.CENTER;
		gc1.insets = new Insets(3, 2, 2, 2);
		gc1.gridx = 2;
		gc1.gridy = 2;
		gc1.gridheight = 2;
		childPanel_2.add(textPCN2, gc1);

		// fourth row
		gc1.anchor = GridBagConstraints.NORTHWEST;
		gc1.gridx = 0;
		gc1.gridy = 4;
		gc1.gridwidth = 2;
		childPanel_2.add(labelIA, gc1);
		gc1.gridx = 2;
		gc1.gridwidth = 1;
		childPanel_2.add(textIA, gc1);
		gc1.gridx = 4;
		childPanel_2.add(label_4, gc1);

		childPanel_2.setPreferredSize(new Dimension(310, 140));
	}

	// calculate the curve number form 1 day to 30 days.
	private void getCurveNum_30(int num) {
		double res = Math.pow(num, 2.365) / 631.79;
		res = (num - res - 15) * Math.log10(30);
		res = num - res;
		curveNum_30 = (int) res;
	}

	// update perv data of the table, when curve number and area changed.
	public void updateTablePerv(int num, double area, String[] data) {

		double s = 1000.00 / num - 10;
		s = Double.parseDouble(df.format(s));

		for (int i = 0; i < 12; i++) {
			double ele = Double.parseDouble(data[i].toString());
			double p = Double.parseDouble(df.format(ele));
			double q = (p - 0.2 * s) * (p - 0.2 * s) / (p + 0.8 * s);
			q = Double.parseDouble(df.format(q));
			double perv = Double.parseDouble(df.format(q * area * 3.63));
			myTable1.model.data[i][1] = Double.toString(perv);
			double imperv = Double.parseDouble(myTable1.model.data[i][2].toString());
			double total = Double.parseDouble(df.format(perv + imperv));
			myTable1.model.data[i][3] = Double.toString(total);
		}

		myTable1.model.mySetValueAt(myTable1.model.getNewSum(1), 12, 1);
		myTable1.model.mySetValueAt(myTable1.model.getNewSum(3), 12, 3);
		databaseTable.updateUI();

	}

	// update imperv data of the table, when curve number and area changed.
	public void updateTableImperv(int num, double area, String[] data) {
		
		double s = 1000.00 / num - 10;
		s = Double.parseDouble(df.format(s));

		for (int i = 0; i < 12; i++) {
			double ele = Double.parseDouble(data[i].toString());
			double p = Double.parseDouble(df.format(ele));
			double q = (p - 0.2 * s) * (p - 0.2 * s) / (p + 0.8 * s);
			q = Double.parseDouble(df.format(q));
			double imperv = Double.parseDouble(df.format(q * area * 3.63 / 43559));
			myTable1.model.data[i][2] = Double.toString(imperv);
			double perv = Double.parseDouble(myTable1.model.data[i][1].toString());
			double total = Double.parseDouble(df.format(perv + imperv));
			myTable1.model.data[i][3] = Double.toString(total);
		}

		myTable1.model.mySetValueAt(myTable1.model.getNewSum(2), 12, 2);
		myTable1.model.mySetValueAt(myTable1.model.getNewSum(3), 12, 3);
		databaseTable.updateUI();
	}

	// update 25yr run off data of the table, when curve number and areas changed.
	public void updateRunoff25Yr(double numCN, double areaPWA, double areaIPWA) {

		double s = 1000.00 / numCN - 10;
		s = Double.parseDouble(df.format(s));
		double q = (pervious25Yr - 0.2 * s) * (pervious25Yr - 0.2 * s) / (pervious25Yr + 0.8 * s);
		q = Double.parseDouble(df.format(q));

		double s1 = 1000.00 / 98 - 10;
		s1 = Double.parseDouble(df.format(s1));
		double q1 = (pervious25Yr - 0.2 * s1) * (pervious25Yr - 0.2 * s1) / (pervious25Yr + 0.8 * s1);
		q1 = Double.parseDouble(df.format(q1));

		double perv = Double.parseDouble(df.format(q * areaPWA * 3.63));
		double imperv = Double.parseDouble(df.format(q1 * areaIPWA * 3.63 / 43559));

		text_1.setText(Double.toString(perv));
		text_2.setText(Double.toString(imperv));
		text_3.setText(Double.toString(Double.parseDouble(df.format(perv + imperv))));
		
	}

	// update the textfield value and set up the limetation
	public void updateTextField(JTextField textField) {

		switch (textField.getName()) {
		case "textPWA":
			try {
				String s = textPWA.getText().toString();
				if (onlyContainDigit(s))
					valuePWA = Double.parseDouble(s);
				else
					valuePWA = 0;
			} catch (Exception f) {
				valuePWA = 0;
			}

			if (curveNum_30 > 0 && valuePCN1 >= 60 && valuePCN1 <= 92 && valuePCN2 >= 90 && valuePCN2 <= 97) {
				updateTablePerv(curveNum_30, valuePWA, perData);
				updateRunoff25Yr(valuePCN1, valuePWA, valueIA);
			}

			break;
		case "textPCN1":
			try {
				String s = textPCN1.getText().toString();
				if (onlyContainDigit(s))
					valuePCN1 = Double.parseDouble(s);
				else
					valuePCN1 = 0;
			} catch (Exception f) {
				valuePCN1 = 0;
			}
			int val = (int) valuePCN1;
			if (val >= 60 && val <= 92) {
				updateRunoff25Yr(val, valuePWA, valueIA);
				textPCN1.setBackground(null);
			} else {
				updateRunoff25Yr(-1, 0, valueIA);
				textPCN1.setBackground(Color.red);
				textPCN1.setToolTipText("Range: 60-92");				
			}

			break;
		case "textPCN2":
			int v;
			try {
				String s = textPCN2.getText().toString();
				if (onlyContainDigit(s)) {
					valuePCN2 = Double.parseDouble(s);
				} else {
					valuePCN2 = 0;
				}
			} catch (Exception f) {
				valuePCN2 = 0;
			}
			v = (int) valuePCN2;
			if (r3.isSelected())
				getCurveNum_30(v);
			else if (r4.isSelected())
				curveNum_30 = v;

			// set the bounder of the valuePCN2
			if (v >= 90 && v <= 97) {
				updateTablePerv(curveNum_30, valuePWA, perData);
				updateTableImperv(95, valueIA, perData);
				textPCN2.setBackground(null);
			} else {
				updateTablePerv(-1, 0, perData);
				updateTableImperv(95, 0, perData);
				textPCN2.setBackground(Color.red);
				textPCN2.setToolTipText("Range: 90-97");
			}
			r3.setToolTipText("=" + curveNum_30 + "(30 day)");
			break;
		case "textIA":
			try {
				String s = textIA.getText().toString();
				if (onlyContainDigit(s))
					valueIA = Double.parseDouble(s);
				else
					valueIA = 0;
			} catch (Exception f) {
				valueIA = 0;
			}
			if(curveNum_30 > 0 && valuePCN1 >= 60 && valuePCN1 <= 92 && valuePCN2 >= 90 && valuePCN2 <= 97) {
				updateTableImperv(95, valueIA, perData);
				updateRunoff25Yr(valuePCN1, valuePWA, valueIA);
			}
			break;
		case "text_1":
			try {
				String s = text_1.getText().toString();
				if (onlyContainDigit(s))
					valueT1 = Double.parseDouble(s);
				//else
					//valueT1 = 0;
			} catch (Exception f) {
				//valueT1 = 0;
			}
			try {
				String s = text_2.getText().toString();
				if (onlyContainDigit(s))
					valueT2 = Double.parseDouble(s);
				//else
					//valueT2 = 0;
			} catch (Exception f) {
				//valueT2 = 0;
			}
			double total = Double.parseDouble(df.format(valueT1 + valueT2));
			String s = Double.toString(total);
			text_3.setText(s);
			break;
		case "text_2":
			try {
				String ns = text_1.getText().toString();
				if (onlyContainDigit(ns))
					valueT1 = Double.parseDouble(ns);
				//else
					//valueT1 = 0;
			} catch (Exception f) {
				//valueT1 = 0;
			}
			try {
				String ns = text_2.getText().toString();
				if (onlyContainDigit(ns))
					valueT2 = Double.parseDouble(ns);
				//else
					//valueT2 = 0;
			} catch (Exception f) {
				//valueT2 = 0;
			}
			double valueT3 = Double.parseDouble(df.format(valueT1 + valueT2));
			String ns = Double.toString(valueT3);
			text_3.setText(ns);
			break;
		}

	}

	// check the input of the textfield is only digit.
	private boolean onlyContainDigit(String s) {

		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i)) && (s.charAt(i) != '.'))
				return false;
		}
		return true;
	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
