package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Controller.PanelManager;
import Model_Entity.ClimateInfo;
import Model_Tables.ClimateTable;

public class ClimatePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	RunoffPanel runoffPanel;
	AnimalsPanel animalPanel = null;

	// declare the data structure
	ArrayList<ClimateInfo> climateDataByState;
	ArrayList<ClimateInfo> climateDataByCounty;
	ClimateInfo currentElement;
	HashMap<String, ArrayList<String>> countyStationMap; // store the county and its stations
	String[] listOfCounty; // list all county
	String[] listOfStation; // list all station per county
	ArrayList<String> climatePanelOutput;

	// data for table
	String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };
	String[] tableColumnName = { " ", "Prec(in)", "Evap(in)" };
	Object[][] tableData1; // the data from the database
	Object[][] tableData2 = { { "January", "0.00", "0.00" }, { "February", "0.00", "0.00" },
			{ "March", "0.00", "0.00" }, { "April", "0.00", "0.00" }, { "May", "0.00", "0.00" },
			{ "June", "0.00", "0.00" }, { "July", "0.00", "0.00" }, { "August", "0.00", "0.00" },
			{ "September", "0.00", "0.00" }, { "October", "0.00", "0.00" }, { "November", "0.00", "0.00" },
			{ "December", "0.00", "0.00" }, };

	
	// declare the elements in the panel
	JRadioButton r1, r2, r3, r4, r5;
	//ButtonGroup bg1, bg2;
	JLabel labelCounty, labelStation, labelEnterCounty,labelEnterStation, labelInches, labelPrecipitation, labelRate, labelKval, labelOcv, labelLrv,
			labelAlr;
	JLabel jl1, jl2, jl3;
	ClimateTable mt1 = new ClimateTable(); // used for download the existing AWM data
	JTable databaseTable;
	ClimateTable mt2 = new ClimateTable(); // used for input data by the customer
	JTable customTable;
	JScrollPane scrollPane; // used for table
	JPanel secondLeft;
	JPanel databasePlacePanel; // the panel of county and station, used to get the data from the database
	JPanel customerPlacePanel; // the panel of county and station, used to input the data by the customer
	JTextField textPre;
	JTextField textKVAL;
	JTextField textOCV;
	JTextField textLRV;
	JTextField textAna;
	JComboBox bCounty; // to display the county
	JComboBox bStation = new JComboBox(); // to display the station
	JTextField textEnterCounty;
	JTextField textEnterStation;

	JButton buttonHelp, buttonOK;
	
	public ClimatePanel(PanelManager pm) {

		panelManager = pm;
		// get input data
		climateDataByState = panelManager.filterByState(panelManager.startPanelOutput[1],
				panelManager.allClimateData);

		// initial this panel
		initialData();
		initialElements();
		// setLayout(new BorderLayout());
		BorderLayout border = new BorderLayout();
		initialLayout(border);
		initialActionLiseners();
		
		
		// build actionlisterner for button to show whether the action works or not

		/*
		 * ActionListener sliceActionListener = new ActionListener() { public void
		 * actionPerformed(ActionEvent actionEvent) { AbstractButton aButton =
		 * (AbstractButton) actionEvent.getSource(); System.out.println("Selected: " +
		 * aButton.getText()); /// show whether the action works or not } };
		 */



	}
	
	private void getOutput() {
		climatePanelOutput = new ArrayList<>();
		if(r1.isSelected()) {
			if(bCounty.getSelectedIndex() < 0)
			{
				climatePanelOutput.add(bCounty.getItemAt(0).toString());
			}
			else				
				climatePanelOutput.add(bCounty.getSelectedItem().toString());	
			if(bStation.getSelectedIndex() < 0)
			{
				climatePanelOutput.add(bStation.getItemAt(0).toString());
			}
			else				
				climatePanelOutput.add(bStation.getSelectedItem().toString());
		}
		else {
			climatePanelOutput.add(textEnterCounty.getText());
			climatePanelOutput.add(textEnterStation.getText());
		}
		
		climatePanelOutput.add(textPre.getText());
		climatePanelOutput.add(textKVAL.getText());
		climatePanelOutput.add(textOCV.getText());
		climatePanelOutput.add(textLRV.getText());
		climatePanelOutput.add(textAna.getText());
		
		if(r1.isSelected()) {
			if(r3.isSelected()) {
				for(int i = 0; i < 12; i++) {
					double v1 = Double.parseDouble(mt1.model.data[i][1].toString());
					double v2 = Double.parseDouble(mt1.model.data[i][2].toString());
					double sub = v1 - v2;
					if(sub < 0)
						climatePanelOutput.add("0.00");
					else
						climatePanelOutput.add(Double.toString(sub));
				}
			}
			else if(r4.isSelected()) {
				for(int i = 0; i < 12; i++) {
					double v1 = Double.parseDouble(mt1.model.data[i][1].toString());
					double v2 = Double.parseDouble(mt1.model.data[i][2].toString());
					double sub = v1 - v2;
					climatePanelOutput.add(Double.toString(sub));
				}
			}			
			else if(r5.isSelected()) {
				for(int i = 0; i < 12; i++) {				
					climatePanelOutput.add(mt1.model.data[i][1].toString());
				}
			}									
		}
		else {
			if(r3.isSelected()) {
				for(int i = 0; i < 12; i++) {
					double v1 = Double.parseDouble(mt1.model.data[i][1].toString());
					double v2 = Double.parseDouble(mt1.model.data[i][2].toString());
					double sub = v1 - v2;
					if(sub < 0)
						climatePanelOutput.add("0.00");
					else
						climatePanelOutput.add(Double.toString(sub));
				}
			}
			else if(r4.isSelected()) {
				for(int i = 0; i < 12; i++) {
					double v1 = Double.parseDouble(mt1.model.data[i][1].toString());
					double v2 = Double.parseDouble(mt1.model.data[i][2].toString());
					double sub = v1 - v2;
					climatePanelOutput.add(Double.toString(sub));
				}
			}			
			else if(r5.isSelected()) {
				for(int i = 0; i < 12; i++) {				
					climatePanelOutput.add(mt2.model.data[i][1].toString());
				}
			}										
		}

	}
	private void initialData() {
		currentElement = climateDataByState.get(0);
		climateDataByCounty = panelManager.filterByCounty(currentElement.county, climateDataByState);
		countyStationMap = getMap(climateDataByState);
		listOfCounty = new String[countyStationMap.size()];

		Set<String> keySet = countyStationMap.keySet();
		int count = 0;
		for (String s : keySet) {
			listOfCounty[count] = s;
			count++;
		}
		Arrays.sort(listOfCounty);

		ArrayList<String> firstCounty = countyStationMap.get(listOfCounty[0]);
		listOfStation = convertToStringList(firstCounty);

		tableData1 = getTableData(currentElement);

	}

	private void initialElements() {
		r1 = new JRadioButton("Use AWM Database");
		r1.setFont(new Font(r1.getFont().getName(),Font.PLAIN,12));
		r1.setSelected(true);
		r2 = new JRadioButton("Enter custom climate data for this job   ");
		r2.setFont(new Font(r2.getFont().getName(),Font.PLAIN,12));
		//bg1 = new ButtonGroup();
		//bg1.add(r1);
		//bg1.add(r2);

		r3 = new JRadioButton("If prec - evap < 0 then set net value to 0              ");
		r3.setSelected(true);
		r3.setFont(new Font(r3.getFont().getName(),Font.PLAIN,12));
		r4 = new JRadioButton("Always set net value to prec-evap");
		r4.setFont(new Font(r4.getFont().getName(),Font.PLAIN,12));
		r5 = new JRadioButton("Ignore evap value, and use prec. only");
		r5.setFont(new Font(r5.getFont().getName(),Font.PLAIN,12));
		//bg2 = new ButtonGroup();
		//bg2.add(r3);
		//bg2.add(r4);
		//bg2.add(r5);

		labelCounty = new JLabel("Select County:");
		bCounty = new JComboBox<>(listOfCounty);
		bCounty.setPreferredSize(new Dimension(230, 25));

		labelStation = new JLabel("Select Station:");
		DefaultComboBoxModel stationModel = new DefaultComboBoxModel(listOfStation);
		bStation.setModel(stationModel);
		bStation.setPreferredSize(new Dimension(230, 25));
		
		labelEnterCounty = new JLabel("Enter County: ");
		labelEnterStation = new JLabel("Enter Station: ");
		textEnterCounty = new JTextField();
		textEnterCounty.setPreferredSize(new Dimension(230, 25));
		textEnterStation = new JTextField();
		textEnterStation.setPreferredSize(new Dimension(230, 25));

		labelInches = new JLabel("inches");
		labelPrecipitation = new JLabel("25 Yr. -24 Hr. Storm Precipitation:");
		labelRate = new JLabel("Lagoon Loading Rates:");
		labelKval = new JLabel("Barth KVAL:");
		labelOcv = new JLabel("Load Rate for Odor,OCV:");
		labelLrv = new JLabel("LRV Max:");
		labelAlr = new JLabel("Anaerobic Load Rate:");

		textPre = new JTextField(currentElement.data[0]);
		textPre.setPreferredSize(new Dimension(60, 25));
		textKVAL = new JTextField(currentElement.data[25]);
		textKVAL.setPreferredSize(new Dimension(65, 25));
		textOCV = new JTextField(currentElement.data[27]);
		textOCV.setPreferredSize(new Dimension(65, 25));
		textLRV = new JTextField(currentElement.data[28]);
		textLRV.setPreferredSize(new Dimension(65, 25));
		textAna = new JTextField(currentElement.data[26]);
		textAna.setPreferredSize(new Dimension(65, 25));

		jl1 = new JLabel("lbx VS/cu. ft/day");
		jl2 = new JLabel("lbx VS/cu. ft/day");
		jl3 = new JLabel("lbs VS/1000 cu.ft/day");

		buttonHelp = new JButton("Help");
		buttonOK = new JButton("OK");
	}

	

	private void initialLayout(BorderLayout border) {
		add(buildChildPanel_1(), border.NORTH);
		add(buildChildPanel_2(), border.CENTER);
	}
		
	private JPanel buildChildPanel_1() {
		JPanel firstPart = new JPanel();
		
		// the panel for "Select Climate Data Source". Two radio buttons correspond to two placePanels.
		JPanel scds = new JPanel();
		scds.setLayout(new GridLayout(2, 1));
		scds.add(r1);
		scds.add(r2);
		scds.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Select Climate Data Source"));

		// the panel for "Options for Evaluating Monthly Net Prec - Evap"
		JPanel mnpe = new JPanel();
		mnpe.setLayout(new GridLayout(3, 2));
		mnpe.add(r3);
		mnpe.add(r4);
		mnpe.add(r5);
		mnpe.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Options for Evaluating Monthly Net Prec - Evap"));

		// setup the layout of the first part
		GridBagLayout firstLineLayOut = new GridBagLayout();
		GridBagConstraints gbc1 = new GridBagConstraints();
		firstPart.setLayout(firstLineLayOut);
		gbc1.gridx = 0;
		gbc1.gridy = 1;
		gbc1.fill = GridBagConstraints.BOTH;
		firstPart.add(scds, gbc1); // add the panel into the firstpart panel
		gbc1.gridx = 1;
		gbc1.gridwidth = 2;
		firstPart.add(mnpe, gbc1); // add the panel into the firstpart panel

		return firstPart;
	}
	private JPanel buildChildPanel_2() {
		JPanel secondPart = new JPanel();
		secondPart.setLayout(new GridBagLayout());
		GridBagConstraints gbcTwo = new GridBagConstraints();

		gbcTwo.gridx = 0;
		gbcTwo.gridy = 0;
		gbcTwo.fill = GridBagConstraints.BOTH;
		secondPart.add(buildChildPanel_2_Left(), gbcTwo); // add second left into the second part.

		gbcTwo.gridx = 2;
		gbcTwo.gridy = 0;
		gbcTwo.fill = GridBagConstraints.VERTICAL;
		secondPart.add(buildChildPanel_2_right(), gbcTwo); // add the second right part into the second part.

		return secondPart;
	}

	private JPanel buildChildPanel_2_Left() {
		secondLeft = new JPanel();
		secondLeft.setLayout(new GridBagLayout());
		GridBagConstraints gbcTwoLeft = new GridBagConstraints();

		// 1. the panel for "selecting location"
		databasePlacePanel = new JPanel();
		databasePlacePanel.setLayout(new GridBagLayout());
		GridBagConstraints gcl = new GridBagConstraints();
		gcl.anchor = GridBagConstraints.NORTHWEST;
		gcl.insets = new Insets(5, 10, 0, 10);
		gcl.gridx = 0;
		gcl.gridy = 0;
		databasePlacePanel.add(labelCounty, gcl);
		gcl.gridx = 1;
		databasePlacePanel.add(bCounty, gcl);
		gcl.gridx = 0;
		gcl.gridy = 1;
		databasePlacePanel.add(labelStation, gcl);
		gcl.gridx = 1;
		databasePlacePanel.add(bStation, gcl);

		// 2. the panel for "Precipitation"

		JPanel prePanel = new JPanel();
		prePanel.add(labelPrecipitation);
		prePanel.add(textPre);
		prePanel.add(labelInches);

		// 3. labelRate

		// 4. the panel for "rational design method"

		JPanel rationalPanel = new JPanel();
		rationalPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcRa = new GridBagConstraints();
		gbcRa.fill = GridBagConstraints.NORTHWEST;
		gbcRa.insets = new Insets(5, 5, 0, 0);

		gbcRa.gridx = 0;
		gbcRa.gridy = 0;
		gbcRa.gridwidth = 2;
		rationalPanel.add(labelKval, gbcRa);
		gbcRa.gridy = 1;
		gbcRa.gridwidth = 3;
		rationalPanel.add(labelOcv, gbcRa);
		gbcRa.gridwidth = 1;
		gbcRa.gridy = 2;
		rationalPanel.add(labelLrv, gbcRa);

		gbcRa.gridx = 3;
		gbcRa.gridy = 0;
		rationalPanel.add(textKVAL, gbcRa);
		gbcRa.gridy = 1;
		rationalPanel.add(textOCV, gbcRa);
		gbcRa.gridy = 2;
		rationalPanel.add(textLRV, gbcRa);

		gbcRa.gridx = 4;
		gbcRa.gridy = 1;
		rationalPanel.add(jl1, gbcRa);
		gbcRa.gridy = 2;
		rationalPanel.add(jl2, gbcRa);

		rationalPanel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Rational Design Method"));
		rationalPanel.setPreferredSize(new Dimension(355, 120));

		// 5. the panel for for "NRCS design method"
		JPanel nrcsPanel = new JPanel();

		nrcsPanel.add(labelAlr);
		nrcsPanel.add(Box.createHorizontalStrut(10));
		nrcsPanel.add(textAna);
		nrcsPanel.add(jl3);
		nrcsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "NRCS Design Method"));

		// setup for layout of left
		gbcTwoLeft.anchor = GridBagConstraints.NORTHWEST;
		gbcTwoLeft.insets = new Insets(0, 10, 0, 10);

		gbcTwoLeft.gridx = 0;
		gbcTwoLeft.gridy = 0;
		secondLeft.add(databasePlacePanel, gbcTwoLeft);
		gbcTwoLeft.gridy = 1;
		secondLeft.add(prePanel, gbcTwoLeft);
		gbcTwoLeft.gridy = 2;
		secondLeft.add(labelRate, gbcTwoLeft);
		gbcTwoLeft.gridy = 3;
		secondLeft.add(rationalPanel, gbcTwoLeft);
		gbcTwoLeft.gridy = 4;
		secondLeft.add(nrcsPanel, gbcTwoLeft);

		return secondLeft;
	}

	private JPanel buildCustomerPlacePanel() {
		customerPlacePanel = new JPanel();

		customerPlacePanel.setLayout(new GridBagLayout());
		GridBagConstraints gcEnter = new GridBagConstraints();
		gcEnter.anchor = GridBagConstraints.NORTHWEST;
		gcEnter.insets = new Insets(5, 10, 0, 10);
		gcEnter.gridx = 0;
		gcEnter.gridy = 0;
		customerPlacePanel.add(labelEnterCounty, gcEnter);
		gcEnter.gridx = 1;
		customerPlacePanel.add(textEnterCounty, gcEnter);
		gcEnter.gridx = 0;
		gcEnter.gridy = 1;
		customerPlacePanel.add(labelEnterStation, gcEnter);
		gcEnter.gridx = 1;
		customerPlacePanel.add(textEnterStation, gcEnter);

		return customerPlacePanel;
	}
	

	private JPanel buildChildPanel_2_right() {
		// build the second right panel
		JPanel secondRight = new JPanel();
		GridBagLayout secondRightLayOut = new GridBagLayout();
		GridBagConstraints gbcTwoRight = new GridBagConstraints();
		secondRight.setLayout(secondRightLayOut);

		gbcTwoRight.insets = new Insets(0, 0, 0, 10);
		gbcTwoRight.fill = GridBagConstraints.BOTH;
		gbcTwoRight.anchor = GridBagConstraints.EAST;

		// 1: build the panel for "table"
		gbcTwoRight.gridx = 0;
		gbcTwoRight.gridy = 0;
		JPanel jtab = new JPanel();
		jtab.setLayout(new BorderLayout());

		databaseTable = mt1.buildMyTable(tableColumnName, tableData1); // the table with data from the AWS
		int rowcount1 = databaseTable.getRowCount();
		int colcount1 = databaseTable.getColumnCount();
		mt1.setColor(rowcount1 - 1, rowcount1 - 1, 1, colcount1, Color.cyan);

		customTable = mt2.buildMyTable(tableColumnName, tableData2); // the table without data.
		int rowcount2 = customTable.getRowCount();
		int colcount2 = customTable.getColumnCount();
		mt2.setColor(rowcount2 - 1, rowcount2 - 1, 1, colcount2, Color.cyan);

		scrollPane = new JScrollPane(databaseTable);
		scrollPane.setPreferredSize(new Dimension(210, 250));
		jtab.add(scrollPane, BorderLayout.CENTER);
		secondRight.add(jtab, gbcTwoRight); // add the table into the second right.

		gbcTwoRight.insets = new Insets(20, 0, 0, 0);

		// 2. build the panel for "buttons"
		gbcTwoRight.gridx = 0;
		gbcTwoRight.gridy = 1;
		JPanel jbtn = new JPanel();
		FlowLayout flo6 = new FlowLayout(FlowLayout.RIGHT);
		jbtn.setLayout(flo6);

		jbtn.add(buttonHelp);
		jbtn.add(buttonOK);
		secondRight.add(jbtn, gbcTwoRight); // add the buttons into the second right.

		return secondRight;

	}


	private void initialActionLiseners() {
		
		
		
		textPre.getDocument().addDocumentListener(new DocumentListener() {			
			//pane = parent.tabbedPane;
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					int index = pane.indexOfTab("runoff");    // it is associate with location panel.
					if(index >= 0) {
						runoffPanel = (RunoffPanel) pane.getComponentAt(index);
						runoffPanel.pervious25Yr = Double.parseDouble(textPre.getText().toString());
						int num = Integer.parseInt(runoffPanel.textPCN1.getText().toString());
						double a1 = Double.parseDouble(runoffPanel.textPWA.getText().toString());
						double a2 = Double.parseDouble(runoffPanel.textIA.getText().toString());					
						runoffPanel.updateRunoff(num, a1, a2);
					}
				}catch(Exception e1) {
					
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					int index = pane.indexOfTab("runoff");    // it is associate with location panel.
					if(index >= 0) {
						runoffPanel = (RunoffPanel) pane.getComponentAt(index);
						runoffPanel.pervious25Yr = Double.parseDouble(textPre.getText().toString());
						int num = Integer.parseInt(runoffPanel.textPCN1.getText().toString());
						double a1 = Double.parseDouble(runoffPanel.textPWA.getText().toString());
						double a2 = Double.parseDouble(runoffPanel.textIA.getText().toString());					
						runoffPanel.updateRunoff(num, a1, a2);
					}
				}catch(Exception e1) {
					
				}
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		
		r1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r1.setSelected(true);
					textPre.setText(currentElement.data[0]);
					textKVAL.setText(currentElement.data[25]);
					textOCV.setText(currentElement.data[27]);
					textLRV.setText(currentElement.data[28]);
					textAna.setText(currentElement.data[26]);
					scrollPane.setViewportView(databaseTable);
					secondLeft.remove(customerPlacePanel);
					secondLeft.add(databasePlacePanel, 0);
					secondLeft.updateUI();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		r2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r2.setSelected(true);
					textPre.setText("0");
					textKVAL.setText("0");
					textOCV.setText("0");
					textLRV.setText("0");
					textAna.setText("0");
					scrollPane.setViewportView(customTable);
					secondLeft.remove(databasePlacePanel);
					secondLeft.add(buildCustomerPlacePanel(), 0);
					secondLeft.updateUI();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		r3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r3.setSelected(true);
					r4.setSelected(false);
					r5.setSelected(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		r4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r4.setSelected(true);
					r3.setSelected(false);
					r5.setSelected(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		r5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r5.setSelected(true);
					r3.setSelected(false);
					r4.setSelected(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// state listener: select different state name to get corresponding data.
		bCounty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = bCounty.getSelectedIndex();
					String countyName = listOfCounty[index];
					climateDataByCounty = panelManager.filterByCounty(countyName, climateDataByState);
					/*
					 * climateDataByCounty = new ArrayList<>(); for(ClimateInfo ele :
					 * climateDataByState) { if(ele.county.equals(countyName)) {
					 * climateDataByCounty.add(ele); } }
					 */
					ArrayList<String> countyValue = countyStationMap.get(countyName);
					String[] staList = convertToStringList(countyValue);
					DefaultComboBoxModel stationModel = new DefaultComboBoxModel(staList);
					bStation.setModel(stationModel);
					if (climateDataByCounty.size() == 1) {
						refreshData(0);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// state listener: select different state name to getcorresponding data.
		bStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = bStation.getSelectedIndex();
					String stationName = listOfStation[index];
					refreshData(index);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonOK.addActionListener(new ActionListener() // After selected the data source, open the climate frame with
														// data;
		{
			public void actionPerformed(ActionEvent e) {
				// select the first or second data source
				pane = parent.tabbedPane;

				try {
					// int index = pane.indexOfTab("animal");
					if (animalPanel == null) {
						getOutput();
						panelManager.storeClimatePanelOutput(climatePanelOutput);
						animalPanel = new AnimalsPanel(panelManager);
						animalPanel.setParent(parent);
						pane.add("animal", animalPanel);
					}
					/*
					 * else { pane.remove(index);
					 * 
					 * //System.out.print(index);
					 * 
					 * animal = new AnimalPanel(pane,animalData,source); animal.setParent(parent);
					 * pane.insertTab("animal", null, animal, null, index); }
					 */
					pane.setSelectedIndex(pane.indexOfTab("animal"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

	}

	public Object[][] getTableData(ClimateInfo s) {
		Object[][] res = new Object[12][3];
		for (int i = 0; i < res.length; i++) {
			res[i] = new String[] { months[i], s.data[1 + i], s.data[13 + i] };
		}
		return res;
	}

	public String[] convertToStringList(ArrayList<String> list) {
		String[] sList = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			sList[i] = list.get(i);
		}
		Arrays.sort(sList);
		return sList;
	}

	public void refreshData(int index) {
		currentElement = climateDataByCounty.get(index);
		tableData1 = getTableData(currentElement);
		databaseTable = mt1.buildMyTable(tableColumnName, tableData1);
		int rowcount1 = databaseTable.getRowCount();
		int colcount1 = databaseTable.getColumnCount();
		mt1.setColor(rowcount1 - 1, rowcount1 - 1, 1, colcount1, Color.cyan);
		scrollPane.setViewportView(databaseTable);

		textPre.setText(currentElement.data[0]);
		textKVAL.setText(currentElement.data[25]);
		textOCV.setText(currentElement.data[27]);
		textLRV.setText(currentElement.data[28]);
		textAna.setText(currentElement.data[26]);
	}

	public HashMap<String, ArrayList<String>> getMap(ArrayList<ClimateInfo> data) {
		HashMap<String, ArrayList<String>> countyStationMap = new HashMap<>();
		for (ClimateInfo element : data) {
			String key = element.county;
			if (!countyStationMap.containsKey(key)) {
				ArrayList<String> value = new ArrayList<>();
				value.add(element.name);
				countyStationMap.put(key, value);
			} else
				countyStationMap.get(key).add(element.name);
		}
		return countyStationMap;
	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}

}