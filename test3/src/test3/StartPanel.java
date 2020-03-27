package test3;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import test3.InputData.stationInfo;

public class StartPanel extends JPanel{

	MainFrame parent;
	JTabbedPane pane;
	
	ClimatePanel climate = null;
	InputData climateDataSet;
	String sourceForData;
	String stationForData;
	ArrayList<InputData.stationInfo> stateList;
	ArrayList<InputData.stationInfo> climateData;
	
	JTextField ownerName = new JTextField();
	JTextField designerName = new JTextField();
	
	
	public StartPanel(InputData data) {
	
		climateDataSet = data;   	
		climateDataSet.readClimateSheet("Climate");
		
    	/***k***
    	 * Get all state names from the excelData, and to show in "select state:"
    	 ***z***/
    	HashSet allStateNames = new HashSet();
    	allStateNames.add(" ");
    	
    	for(InputData.stationInfo element : climateDataSet.allClimateData) {  		
    		allStateNames.add(element.state);   		
    	}

	
		/***k***
		 * Set up the layout and add components into it
		 ***z***/
				
		GridBagLayout grid = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();		
		this.setLayout(grid);
		
		// set the gap of each component
		gbc.insets = new Insets(10,10,0,0); 
		
		// add *** Landowner ***
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(new JLabel("Landowner:"), gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 0;		
		this.add(ownerName,gbc);
		
		// add *** Designer ***		
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(new JLabel("Designer:"), gbc);
				
		gbc.gridx = 3;
		gbc.gridy = 2;		
		this.add(designerName,gbc);
		
		// add *** Data Source ***
		gbc.gridx = 1;
		gbc.gridy = 4;
		this.add(new JLabel("Data Source:"), gbc);
						 
		gbc.gridx = 3;
		gbc.gridy = 4;
		String[] source = {" ","MWPS","NRCS-2008"};
		JComboBox ds = new JComboBox(source);
		ds.setSelectedIndex(0);
		ds.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				try {
					ds.getSelectedIndex();						
					ds.setPrototypeDisplayValue(ds.getSelectedItem());
					sourceForData = (String) ds.getPrototypeDisplayValue();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.add(ds, gbc);
		
		// add  ***Select State ***
		gbc.gridx = 1;
		gbc.gridy = 6;
		this.add(new JLabel("Select State:"), gbc);
				
		gbc.fill = GridBagConstraints.WEST;
		gbc.gridx = 3;
		gbc.gridy = 6;
		gbc.ipadx = 20; 
		
		String[] state = new String[allStateNames.size()];
		allStateNames.toArray(state);
		Arrays.sort(state);		
		JComboBox st = new JComboBox(state);
		st.setSelectedIndex(0);
		st.addActionListener(new ActionListener()                //state listener: select different state name to get corresponding data.
		{
			public void actionPerformed(ActionEvent e){
				try {
					int index = st.getSelectedIndex();
					String stateName = state[index];
					climateData = climateDataSet.filterByState(stateName, climateDataSet.allClimateData);
									
					st.setPrototypeDisplayValue(st.getSelectedItem());
					stationForData = (String) st.getPrototypeDisplayValue();					
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(st, gbc);
		
		// add  *** Operating Period Setup ***
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 8;
		this.add(new JButton("Operating Period Setup"), gbc);
		
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 10;
		this.add(new JLabel("<html> Click button above to define or <br> modify the operating period(s) </html>"), gbc);
	
		// add  *** OK ***
		gbc.gridx = 5;
		gbc.gridy = 8;
		JButton okBt = new JButton("OK");
		okBt.addActionListener(new ActionListener()                   //After selected the data source, open the climate frame with data;
		{
			public void actionPerformed(ActionEvent e){
				if(ds.getSelectedIndex() == 0){
					System.out.print("Nothing selected");            //must select one data source, otherwise, the button doesn't work					
				}
				else if( (ds.getSelectedIndex() == 1 || ds.getSelectedIndex() == 2) && st.getSelectedIndex() != 0){     //select the first or second data source
					
					try {
						pane = parent.tabbedPane;
						int index = pane.indexOfTab("climate");					
						if(climate == null) {
							climate = new ClimatePanel(climateData,sourceForData);
							climate.setParent(parent);
							pane.add("climate", climate);							
						}
						/*else {
							pane.remove(index);
							climate = new ClimatePanel(pane,climateData,sourceForData);
							climate.setParent(parent);	
							pane.insertTab("climate", null, climate, null, index);
						}*/
						pane.setSelectedIndex(pane.indexOfTab("climate"));
						ds.setEnabled(false);
						st.setEnabled(false);
					} catch (Exception e1) {
						e1.printStackTrace();
					}					
				}

			}			
		});
		this.add(okBt, gbc);
		
		// add  *** Cancel ***
		gbc.gridx = 5;
		gbc.gridy = 10;
		JButton cancelBt = new JButton("Cancel");
		
		cancelBt.addActionListener(new ActionListener()                   //After selected the data source, open the climate frame with data;
				{
					public void actionPerformed(ActionEvent e){
						//dispose();
						//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					}			
				});
		this.add(cancelBt, gbc);
		
		// add  *** Help ***
		gbc.gridx = 5;
		gbc.gridy = 12;
		this.add(new JButton("Help"), gbc);			
		
	}
	
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
	
	
	
	
}
