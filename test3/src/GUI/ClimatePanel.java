package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Controller.PanelManager;
import Model_Entity.ClimateInfo;
import Model_Tables.ClimateTable;

/**
 * This is the panel of "Climate".
 * The Climate screen allows the user to define the monthly precipitation 
 * and evaporation, the 25 year – 24 hour precipitation, and the anaerobic
 * lagoon volatile solids loading rates.
 *  
 * @author Kai Zhao
 *
 */
public class ClimatePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	RunoffPanel runoffPanel;
	AnimalsPanel animalPanel = null;

	/********************************************************
	 *  declare the data structures uesd in this panel
	 */
	
	ArrayList<ClimateInfo> climateDataByState;
	ArrayList<ClimateInfo> climateDataByCounty;
	ClimateInfo currentElement;		// getting data to show in the table, also used for creating output
	HashMap<String, ArrayList<String>> countyStationMap; 	// store the county and its stations
	String[] listOfCounty; 		// list all county, to show in comboBox county
	String[] listOfStation; 	// list all station per county, to show in comboBox station
	double valuePre, valueKVAL, valueOCV, valueLRV, valueAna;
	ArrayList<String> climatePanelOutput;
	
	
	/********************************************************
	 *  declare the elements in the panel
	 */
	
	JRadioButton r1, r2, r3, r4, r5;
	JLabel labelCounty, labelStation, labelEnterCounty,labelEnterStation, labelInches, labelPrecipitation, labelRate, labelKval, labelOcv, labelLrv,
			labelAlr;
	JLabel jl1, jl2, jl3;
	JScrollPane scrollPane;
	JPanel secondLeft;
	JPanel databasePlacePanel; 		// the panel of county and station, used for getting the data from the database
	JPanel customerPlacePanel; 		// the panel of county and station, used for input the data by the customer
	JTextField textPre;
	JTextField textKVAL;
	JTextField textOCV;
	JTextField textLRV;
	JTextField textAna;
	JComboBox comboBoxCounty; 	// to display the county names
	JComboBox comboBoxStation; 	// to display the station names
	JTextField textEnterCounty;
	JTextField textEnterStation;
	JButton buttonHelp, buttonOK;
	
	ClimateTable climateTable1 = new ClimateTable(); // used for download the existing AWM data
	JTable jTable_database;
	ClimateTable climateTable2 = new ClimateTable(); // used for input data by the customer
	JTable jTable_customer;
	
	
	String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };
	
	// data for table
	String[] tableColumnName = { " ", "Prec(in)", "Evap(in)" };
	Object[][] data_database; 	// the data from the database
	Object[][] data_customer = { { "January", "0.00", "0.00" }, { "February", "0.00", "0.00" },
			{ "March", "0.00", "0.00" }, { "April", "0.00", "0.00" }, { "May", "0.00", "0.00" },
			{ "June", "0.00", "0.00" }, { "July", "0.00", "0.00" }, { "August", "0.00", "0.00" },
			{ "September", "0.00", "0.00" }, { "October", "0.00", "0.00" }, { "November", "0.00", "0.00" },
			{ "December", "0.00", "0.00" }, };
	
	BorderLayout border;
	
	/**
	 *  The constructor of this class.
	 * @param pm	The "controller" of this project.
	 */
	public ClimatePanel(PanelManager pm) {
		panelManager = pm;
		initialData();
		initialElements();
		initialListeners();
		initialLayout();		
	}
	
	// initials all data structure, mainly to get the input data.
	private void initialData() {
		climateDataByState = panelManager.filterByState(panelManager.startPanelOutput[1],
				panelManager.allClimateData);
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
		
		data_database = getTableData(currentElement);		
		valuePre = 0;
		valueKVAL = 0;
		valueOCV = 0;
		valueLRV = 0;
		valueAna = 0;
	}

	// initials all elements in this panel
	private void initialElements() {
		r1 = new JRadioButton("Use AWM Database");
		r1.setFont(new Font(r1.getFont().getName(),Font.PLAIN,12));
		r1.setSelected(true);
		r2 = new JRadioButton("Enter custom climate data for this job   ");
		r2.setFont(new Font(r2.getFont().getName(),Font.PLAIN,12));

		r3 = new JRadioButton("If prec - evap < 0 then set net value to 0              ");
		r3.setSelected(true);
		r3.setFont(new Font(r3.getFont().getName(),Font.PLAIN,12));
		r4 = new JRadioButton("Always set net value to prec-evap");
		r4.setFont(new Font(r4.getFont().getName(),Font.PLAIN,12));
		r5 = new JRadioButton("Ignore evap value, and use prec. only");
		r5.setFont(new Font(r5.getFont().getName(),Font.PLAIN,12));

		labelCounty = new JLabel("Select County:");
		comboBoxCounty = new JComboBox<>(listOfCounty);
		comboBoxCounty.setPreferredSize(new Dimension(230, 25));

		labelStation = new JLabel("Select Station:");
		DefaultComboBoxModel stationModel = new DefaultComboBoxModel(listOfStation);
		comboBoxStation = new JComboBox();
		comboBoxStation.setModel(stationModel);
		comboBoxStation.setPreferredSize(new Dimension(230, 25));
		
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
		textPre.setName("textPre");
		textPre.setPreferredSize(new Dimension(60, 25));
		textKVAL = new JTextField(currentElement.data[25]);
		textKVAL.setName("textKVAL");
		textKVAL.setPreferredSize(new Dimension(65, 25));
		textOCV = new JTextField(currentElement.data[27]);
		textOCV.setName("textOCV");
		textOCV.setPreferredSize(new Dimension(65, 25));
		textLRV = new JTextField(currentElement.data[28]);
		textLRV.setName("textLRV");
		textLRV.setPreferredSize(new Dimension(65, 25));
		textAna = new JTextField(currentElement.data[26]);
		textAna.setName("textAna");
		textAna.setPreferredSize(new Dimension(65, 25));

		updateTextField(textPre);
		updateTextField(textKVAL);
		updateTextField(textOCV);
		updateTextField(textLRV);
		updateTextField(textAna);
			
		jl1 = new JLabel("lbx VS/cu. ft/day");
		jl2 = new JLabel("lbx VS/cu. ft/day");
		jl3 = new JLabel("lbs VS/1000 cu.ft/day");

		buttonHelp = new JButton("Help");
		buttonOK = new JButton("OK");
		
		border = new BorderLayout();
	}

	// initials all listeners of this panel
	private void initialListeners() {
	
		// it is associate with runoff panel: the valuePre affect some value of runoffPanel
		textPre.getDocument().addDocumentListener(new DocumentListener() {						
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					updateTextField(textPre);
					int index = pane.indexOfTab("runoff");    
					if(index >= 0) {
						runoffPanel = (RunoffPanel) pane.getComponentAt(index);																	
						runoffPanel.pervious25Yr = valuePre;
						int num = (int)(runoffPanel.valuePCN1);
						double a1 = runoffPanel.valuePWA;
						double a2 = runoffPanel.valueIA;					
						runoffPanel.updateRunoff25Yr(num, a1, a2);
						
					}										
				}catch(Exception e1) {
					
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					updateTextField(textPre);
					int index = pane.indexOfTab("runoff");    
					if(index >= 0) {
						runoffPanel = (RunoffPanel) pane.getComponentAt(index);						
						if(valuePre <= 12 && valuePre >= 1.5) {
							textPre.setBackground(null);
						}
						else {
							textPre.setBackground(Color.red);
							textPre.setToolTipText("Range: 1.5-12");
						}					
						runoffPanel.pervious25Yr = valuePre;
						int num = (int)(runoffPanel.valuePCN1);
						double a1 = runoffPanel.valuePWA;
						double a2 = runoffPanel.valueIA;					
						runoffPanel.updateRunoff25Yr(num, a1, a2);
						
					}	
				}catch(Exception e1) {
					
				}			
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
					
			}
		});
				
		textKVAL.getDocument().addDocumentListener(new DocumentListener() {						
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {					
					updateTextField(textKVAL);															
				}catch(Exception e1) {
					
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					updateTextField(textKVAL);						
				}catch(Exception e1) {
					
				}				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		
		textOCV.getDocument().addDocumentListener(new DocumentListener() {						
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {					
					updateTextField(textOCV);															
				}catch(Exception e1) {
					
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					updateTextField(textOCV);						
				}catch(Exception e1) {
					
				}				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		
		textLRV.getDocument().addDocumentListener(new DocumentListener() {						
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {					
					updateTextField(textLRV);									
				}catch(Exception e1) {
					
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					updateTextField(textLRV);	
				}catch(Exception e1) {
					
				}				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		
		textAna.getDocument().addDocumentListener(new DocumentListener() {						
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {					
					updateTextField(textAna);											
				}catch(Exception e1) {
					
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					updateTextField(textAna);			
				}catch(Exception e1) {
					
				}				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		
		//  the radioButton for "Use AWM Database"
		r1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent f) {
				try {
					r1.setSelected(true);
					r2.setSelected(false);
					textPre.setText(currentElement.data[0]);
					textKVAL.setText(currentElement.data[25]);
					textOCV.setText(currentElement.data[27]);
					textLRV.setText(currentElement.data[28]);
					textAna.setText(currentElement.data[26]);
					
					updateTextField(textPre);
					updateTextField(textKVAL);
					updateTextField(textOCV);
					updateTextField(textLRV);
					updateTextField(textAna);
					
					scrollPane.setViewportView(jTable_database);
					secondLeft.remove(customerPlacePanel);
					secondLeft.add(databasePlacePanel, 0);
					secondLeft.updateUI();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// the radioButton for " Enter custom ...", require to enter county name and station name
		r2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r2.setSelected(true);
					r1.setSelected(false);
					textPre.setText("0.0");
					textKVAL.setText("0.0");
					textOCV.setText("0.0");
					textLRV.setText("0.0");
					textAna.setText("0.0");
					
					updateTextField(textPre);
					updateTextField(textKVAL);
					updateTextField(textOCV);
					updateTextField(textLRV);
					updateTextField(textAna);
					
					scrollPane.setViewportView(jTable_customer);
					secondLeft.remove(databasePlacePanel);
					secondLeft.add(buildCustomerPlacePanel(), 0);
					secondLeft.updateUI();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// the radioButton for the 1st option for evaluating monthly net pre - evap
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
		
		// the radioButton for the 2nd option for evaluating monthly net pre - evap
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
		
		// the radioButton for the 3rd option for evaluating monthly net pre - evap
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

		// state listener: select different county name to get corresponding data (station names).
		comboBoxCounty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = comboBoxCounty.getSelectedIndex();
					String countyName = listOfCounty[index];
					climateDataByCounty = panelManager.filterByCounty(countyName, climateDataByState);
					ArrayList<String> countyValue = countyStationMap.get(countyName);
					String[] staList = convertToStringList(countyValue);
					DefaultComboBoxModel stationModel = new DefaultComboBoxModel(staList);
					comboBoxStation.setModel(stationModel);
					if (climateDataByCounty.size() == 1) {
						refreshData(0);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// state listener: select different station name to get corresponding data(table data).
		comboBoxStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = comboBoxStation.getSelectedIndex();
					//String stationName = listOfStation[index];
					refreshData(index);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
				
		buttonOK.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				pane = parent.tabbedPane;
				if (jTable_database.isEditing())
	            	  jTable_database.getCellEditor().stopCellEditing(); 
				try {					
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

	// initials the layout of this panel
	private void initialLayout() {
		add(buildChildPanel_1(), border.NORTH);
		add(buildChildPanel_2(), border.CENTER);
	}
		
	// creates the 1st child panel, includes two groups of ridioButtons
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
	
	// creates the 2nd child panel, divided by two small parts: left and right.
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

	// creates the left of the 2nd child panel
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
		databasePlacePanel.add(comboBoxCounty, gcl);
		gcl.gridx = 0;
		gcl.gridy = 1;
		databasePlacePanel.add(labelStation, gcl);
		gcl.gridx = 1;
		databasePlacePanel.add(comboBoxStation, gcl);

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

	// creates the customer panel to enter the county name and the station name
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
	
	// creates the right of the 2nd child panel
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

		jTable_database = climateTable1.buildMyTable(tableColumnName, data_database); // the table with data from the AWS
		jTable_customer = climateTable2.buildMyTable(tableColumnName, data_customer); // the table without data.

		scrollPane = new JScrollPane(jTable_database);
		scrollPane.setPreferredSize(new Dimension(210, 250));
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane.setViewportBorder(border);
		scrollPane.setBorder(border);
		
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

	// There are some limitation of each value of JtextField, this function is used for set limitation.
	private void updateTextField(JTextField text) {
		switch (text.getName()) {
		case "textPre":
			try {
				valuePre = Double.parseDouble(textPre.getText().toString());				
			} catch (Exception e) {
				valuePre = 0;
			}
			if(valuePre <= 12 && valuePre >= 1.5) {
				textPre.setBackground(null);
			}
			else {
				textPre.setBackground(Color.red);
				textPre.setToolTipText("Range: 1.5-12");
			}
			break;
		case "textKVAL":
			try {
				valueKVAL = Double.parseDouble(textKVAL.getText().toString());			
			} catch (Exception e) {
				valueKVAL = 0;
			}
			if(valueKVAL <= 1.2 && valueKVAL >= 0.4) {
				textKVAL.setBackground(null);
			}
			else {
				textKVAL.setBackground(Color.red);
				textKVAL.setToolTipText("Range: 0.4-1.2");
			}
			break;
		case "textOCV":
			try {
				valueOCV = Double.parseDouble(textOCV.getText().toString());
			} catch (Exception e) {
				valueOCV = 0;
			}
			if(valueOCV <= 0.00381 && valueOCV >= 0.00256) {
				textOCV.setBackground(null);
			}
			else {
				textOCV.setBackground(Color.red);
				textOCV.setToolTipText("Range: 0.00256-0.00381");
			}
			break;
		case "textLRV":
			try {
				valueLRV = Double.parseDouble(textLRV.getText().toString());
			} catch (Exception e) {
				valueLRV = 0;
			}
			if(valueLRV <= 0.0106 && valueLRV >= 0.00625) {
				textLRV.setBackground(null);
			}
			else {
				textLRV.setBackground(Color.red);
				textLRV.setToolTipText("Range: 0.00625-0.0106");
			}
			break;
		case "textAna":
			try {
				valueAna = Double.parseDouble(textAna.getText().toString());	
			} catch (Exception e) {
				valueAna = 0;
			}
			if(valueAna <= 12 && valueAna >= 3) {
				textAna.setBackground(null);
			}
			else {
				textAna.setBackground(Color.red);
				textAna.setToolTipText("Range: 3-12");
			}			
			break;
			
		}
	}
	
	
	/*
	 *  To store the data from this panel, include all values of textField and the result data (pre - evap) in the table.
	 *  The data maybe come from the database, maybe come from the customer entering.
	 *  And, each case includes three options of calculate the result (pre - evap)	   
	 */	 
	private void getOutput() {
		climatePanelOutput = new ArrayList<>();
		if(r1.isSelected()) {
			if(comboBoxCounty.getSelectedIndex() < 0)
			{
				climatePanelOutput.add(comboBoxCounty.getItemAt(0).toString());
			}
			else				
				climatePanelOutput.add(comboBoxCounty.getSelectedItem().toString());	
			if(comboBoxStation.getSelectedIndex() < 0)
			{
				climatePanelOutput.add(comboBoxStation.getItemAt(0).toString());
			}
			else				
				climatePanelOutput.add(comboBoxStation.getSelectedItem().toString());
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
					double v1 = Double.parseDouble(climateTable1.model.data[i][1].toString());
					double v2 = Double.parseDouble(climateTable1.model.data[i][2].toString());
					double sub = v1 - v2;
					if(sub < 0)
						climatePanelOutput.add("0.00");
					else
						climatePanelOutput.add(Double.toString(sub));
				}
			}
			else if(r4.isSelected()) {
				for(int i = 0; i < 12; i++) {
					double v1 = Double.parseDouble(climateTable1.model.data[i][1].toString());
					double v2 = Double.parseDouble(climateTable1.model.data[i][2].toString());
					double sub = v1 - v2;
					climatePanelOutput.add(Double.toString(sub));
				}
			}			
			else if(r5.isSelected()) {
				for(int i = 0; i < 12; i++) {				
					climatePanelOutput.add(climateTable1.model.data[i][1].toString());
				}
			}									
		}
		else {
			if(r3.isSelected()) {
				for(int i = 0; i < 12; i++) {
					double v1 = Double.parseDouble(climateTable1.model.data[i][1].toString());
					double v2 = Double.parseDouble(climateTable1.model.data[i][2].toString());
					double sub = v1 - v2;
					if(sub < 0)
						climatePanelOutput.add("0.00");
					else
						climatePanelOutput.add(Double.toString(sub));
				}
			}
			else if(r4.isSelected()) {
				for(int i = 0; i < 12; i++) {
					double v1 = Double.parseDouble(climateTable1.model.data[i][1].toString());
					double v2 = Double.parseDouble(climateTable1.model.data[i][2].toString());
					double sub = v1 - v2;
					climatePanelOutput.add(Double.toString(sub));
				}
			}			
			else if(r5.isSelected()) {
				for(int i = 0; i < 12; i++) {				
					climatePanelOutput.add(climateTable2.model.data[i][1].toString());
				}
			}										
		}

	}

	// gets the table data based on the input data
	public Object[][] getTableData(ClimateInfo s) {
		Object[][] res = new Object[12][3];
		for (int i = 0; i < res.length; i++) {
			res[i] = new String[] { months[i], s.data[1 + i], s.data[13 + i] };
		}
		return res;
	}

	// converts the arrayList to String[]
	public String[] convertToStringList(ArrayList<String> list) {
		String[] sList = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			sList[i] = list.get(i);
		}
		Arrays.sort(sList);
		return sList;
	}

	/**
	 * updates the values of all textFields and the table, based on the the selection
	 * of the county and the station 
	 * @param index the selected station location at the list of the selected county.
	 */
	public void refreshData(int index) {
		currentElement = climateDataByCounty.get(index);
		data_database = getTableData(currentElement);
		jTable_database = climateTable1.buildMyTable(tableColumnName, data_database);
		scrollPane.setViewportView(jTable_database);

		textPre.setText(currentElement.data[0]);
		textKVAL.setText(currentElement.data[25]);
		textOCV.setText(currentElement.data[27]);
		textLRV.setText(currentElement.data[28]);
		textAna.setText(currentElement.data[26]);
	}

	/**
	 * gets the map from the data. 
	 * Key is the county name, Value is all station names in this county.
	 * @param data the input data from the database
	 * @return
	 */
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