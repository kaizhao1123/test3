package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import Controller.PanelManager;
import Model_Entity.ClimateInfo;

/**
 * This is the panel of "Start".
 * It will start a new design.
 * 
 * @author Kai Zhao
 *
 */
public class StartPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	OperatingPeriodDialog periodDialog;
	ClimatePanel climate = null;

	/*********************************************************
	 * declare the data structures used in this panel
	 */
	
	String[] stateNames;	// all input state names
	String[] sourceNames;  // all input source names		
	String sourceForData; // store the source name for output
	String stateForData; // store the state name for output
	String[] output; // output data, will be stored in Manager, be used in the future

	/*********************************************************
	 * declare the elements of this panel
	 */
	
	JLabel jl1, jl2, jl3, jl4, jl5;		// the labels of this panel
	JTextField ownerName;	// Click in the box and then type in the land owner’s name.
	JTextField designerName;	// Click in the box and then type in the designer’s name.
	
	/*
	 *  Click on the drop-down list box to access the various animal data sources available
	 *  for use within AWM. Click on the preferred data source to select it for use within AWM. 
	 */
	JComboBox dataSource; 	
	
	/*
	 * Click on the drop-down list box to select a state. AWM will use state-specific data 
	 * stored in the database for animals, climate, and bedding.  AWM will also generate 
	 * custom reports that are stored in the database for the selected state.
	 */
	JComboBox selectState;
	
	JButton buttonSetup; //Click the Operating Period Setup button to see the Operating Period screen.
	JButton buttonOK;	// store output and open next panel
	JButton buttonCancel;
	JButton buttonHelp;

	GridBagConstraints gbc;
	
	
	/**
	 * The constructor of this class.
	 * @param pm	The "controller" of this project.
	 */
	public StartPanel(PanelManager pm) {
		panelManager = pm;
		initialData();
		initialElements();
		initialListeners();
		initialLayout();
	}

	// initials all data structure, mainly to get the input data.
	private void initialData() {					
		// get all state names
		HashSet<String>allStateNames = panelManager.getAllStateNames(); 
		stateNames = new String[allStateNames.size()];
		allStateNames.toArray(stateNames);
		Arrays.sort(stateNames);
		
		// get all source names
		sourceNames = new String[3];
		sourceNames[0] = " ";
		sourceNames[1] = "MWPS";
		sourceNames[2] = "NRCS-2008";
			
		// initial output 
		output = new String[2];
	}

	// initials all elements in this panel
	private void initialElements() {
		jl1 = new JLabel("Landowner:");
		jl2 = new JLabel("Designer:");
		jl3 = new JLabel("Data Source:");
		jl4 = new JLabel("Select State:");
		jl5 = new JLabel("<html> Click button above to define or <br> modify the operating period(s) </html>");
		ownerName = new JTextField();
		designerName = new JTextField();
	
		dataSource = new JComboBox<>(sourceNames);
		dataSource.setSelectedIndex(0);		
		selectState = new JComboBox<>(stateNames);
		selectState.setSelectedIndex(0);

		buttonSetup = new JButton("Operating Period Setup");
		periodDialog = new OperatingPeriodDialog();
		buttonOK = new JButton("OK");
		buttonCancel = new JButton("Cancel");
		buttonHelp = new JButton("Help");
		
		gbc = new GridBagConstraints();
	}

	// initials the layout of this panel
	private void initialLayout() {
		setLayout(new GridBagLayout());
		gbc.insets = new Insets(10, 10, 0, 0);

		// add *** Landowner ***
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(jl1, gbc);
		gbc.gridx = 3;
		add(ownerName, gbc);

		// add *** Designer ***
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(jl2, gbc);
		gbc.gridx = 3;
		add(designerName, gbc);

		// add *** Data Source ***
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(jl3, gbc);
		gbc.gridx = 3;
		add(dataSource, gbc);

		// add ***Select State ***
		gbc.gridx = 1;
		gbc.gridy = 6;
		add(jl4, gbc);
		gbc.fill = GridBagConstraints.WEST;
		gbc.gridx = 3;
		gbc.ipadx = 20;
		add(selectState, gbc);

		// add ***setup period ***
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 8;
		add(buttonSetup, gbc);
		gbc.gridy = 10;
		add(jl5, gbc);

		// add *** OK ***
		gbc.gridx = 5;
		gbc.gridy = 8;
		add(buttonOK, gbc);

		// add *** Cancel ***
		gbc.gridy = 10;
		add(buttonCancel, gbc);

		// add *** Help ***
		gbc.gridy = 12;
		add(buttonHelp, gbc);

	}

	// initials all listeners of this panel
	private void initialListeners() {

		// source listener: select different source to get corresponding data.
		dataSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataSource.setPrototypeDisplayValue(dataSource.getSelectedItem());
					sourceForData = (String) dataSource.getPrototypeDisplayValue();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		// state listener: select different state name to get corresponding data.
		selectState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					selectState.setPrototypeDisplayValue(selectState.getSelectedItem());
					stateForData = (String) selectState.getPrototypeDisplayValue();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// open the operating period dialog, to setup the period
		buttonSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					periodDialog.setVisible(true);
					periodDialog.setParent(parent);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// After selected the data source, open the climate panel with data;
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dataSource.getSelectedIndex() == 0 || selectState.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null,"Nothing selected");					
				} else if ((dataSource.getSelectedIndex() > 0)
						&& selectState.getSelectedIndex() > 0) { // select the first or second data source

					try {
						pane = parent.tabbedPane;
						output[0] = sourceForData;
						output[1] = stateForData;
						panelManager.storeStartPanelOutput(output);

						if (climate == null) {
							climate = new ClimatePanel(panelManager);
							climate.setParent(parent);
							pane.add("climate", climate);
						}
						/*
						 * else { pane.remove(index); climate = new
						 * ClimatePanel(pane,climateData,sourceForData); climate.setParent(parent);
						 * pane.insertTab("climate", null, climate, null, index); }
						 */
						pane.setSelectedIndex(pane.indexOfTab("climate"));
						dataSource.setEnabled(false);
						selectState.setEnabled(false);
						periodDialog.setParent(parent);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// dispose();
				// frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
	}

	

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}

}
