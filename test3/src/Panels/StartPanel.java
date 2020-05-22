package Panels;

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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import AWS.PanelManager;
import Entity.ClimateInfo;

public class StartPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	OperatingPeriodDialog periodDialog;
	ClimatePanel climate = null;

	// declare the data structure used in the panel
	ArrayList<ClimateInfo> climateDataSet; // store input data
	HashSet<String> allStateNames; // store all state names
	String sourceForData; // store the source name
	String stateForData; // store the state name
	String[] output; // output data, will be stored in Manager, be used in the future

	// declare the elements shown in the panel
	JLabel jl1, jl2, jl3, jl4, jl5;
	JTextField ownerName;
	JTextField designerName;
	JComboBox dataSource;
	JComboBox selectState;
	JButton buttonSetup;
	JButton buttonOK;
	JButton buttonCancel;
	JButton buttonHelp;

	public StartPanel(PanelManager pm) {
		panelManager = pm;

		initialData();
		initialElements();
		initialActionLiseners();
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		initialLayout(gbc);
	}

	private void initialData() {
		
		// get climate dataset		
		climateDataSet = panelManager.allClimateData;
		
		// get all state names
		allStateNames = getSet(climateDataSet); 
		output = new String[2];
	}

	private void initialElements() {
		jl1 = new JLabel("Landowner:");
		jl2 = new JLabel("Designer:");
		jl3 = new JLabel("Data Source:");
		jl4 = new JLabel("Select State:");
		jl5 = new JLabel("<html> Click button above to define or <br> modify the operating period(s) </html>");
		ownerName = new JTextField();
		designerName = new JTextField();

		String[] source = { " ", "MWPS", "NRCS-2008" };
		dataSource = new JComboBox<>(source);
		dataSource.setSelectedIndex(0);

		String[] state = new String[allStateNames.size()];
		allStateNames.toArray(state);
		Arrays.sort(state);
		selectState = new JComboBox<>(state);
		selectState.setSelectedIndex(0);

		buttonSetup = new JButton("Operating Period Setup");
		periodDialog = new OperatingPeriodDialog();
		buttonOK = new JButton("OK");
		buttonCancel = new JButton("Cancel");
		buttonHelp = new JButton("Help");
	}

	private void initialLayout(GridBagConstraints gbc) {
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

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 8;
		add(buttonSetup, gbc);

		// gbc.gridx = 3;
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

	private void initialActionLiseners() {

		// source listener: select different source to get corresponding data.
		dataSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// dataSource.getSelectedIndex();
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

		// setup the period
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

		// After selected the data source, open the climate frame with data;
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dataSource.getSelectedIndex() == 0) {
					// must select one data source, otherwise, the button doesn't work
					System.out.print("Nothing selected");
				} else if ((dataSource.getSelectedIndex() == 1 || dataSource.getSelectedIndex() == 2)
						&& selectState.getSelectedIndex() != 0) { // select the first or second data source

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

	private HashSet<String> getSet(ArrayList<ClimateInfo> data) {
		HashSet<String> allStateNames = new HashSet<String>();
		allStateNames.add(" ");
		for (ClimateInfo element : data) {
			allStateNames.add(element.state);
		}
		return allStateNames;
	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}

}
