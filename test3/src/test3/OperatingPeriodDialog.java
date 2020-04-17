package test3;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class OperatingPeriodDialog extends JDialog{
	MainFrame parent;
	JTabbedPane pane;
	LocationPanel locationPanel;
	
    Boolean firstOption = true;
	Boolean secondOption = false;
	String[] month = {"January","February","March","April","May","June","July","August",
			  			"September","October","November","December"};
	String operatingPeriod;
	String firstOperatingPeriod;
	String secondOperatingPeriod;
	
	
	
	public OperatingPeriodDialog() {
		JPanel panel = new JPanel();    	
    	panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();		
		gc.anchor = GridBagConstraints.WEST;
		
		/* separate into five parts: 
		 * 1. a panel for the 1st row
		 * 2. a label for the 2nd row
		 * 3. a panel, for the 3rd and 4throw
		 * 4. a panel, for the 5th and 6th row
		 * 5. a button "OK"
		*/
		JRadioButton r1 = new JRadioButton("1 Operation Period");
		JRadioButton r2 = new JRadioButton("2 Operation Period");
		
		JLabel label = new JLabel(" ");  // the 2nd part
		
		JLabel labelBM = new JLabel("Beginning Month:");
		JLabel labelEM = new JLabel("Ending Month:");
		JComboBox boxBM = new JComboBox(month);
		JComboBox boxEM = new JComboBox(month);
		
		JPanel thirdPanel = new JPanel();
		JLabel labelFirstOP = new JLabel("1st Operating Period:");
		JLabel labelSecondOP = new JLabel("2nd Operating Period:");		
		JTextField textFirstOP = new JTextField();
		textFirstOP.setPreferredSize(new Dimension(130,25));
		JTextField textSecondOP = new JTextField();
		textSecondOP.setPreferredSize(new Dimension(130,25));
		
		JButton buttonOK = new JButton("OK");  // the 5th part;
		
		// The 1st part: a panel
		JPanel firstPanel = new JPanel();
		operatingPeriod = boxEM.getSelectedItem().toString() + "-" + boxEM.getSelectedItem().toString();
		ButtonGroup bg = new ButtonGroup();			
		bg.add(r1);bg.add(r2);
		
		r1.setSelected(true);
		r1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				try {
					r1.setSelected(true);
			    	firstOption = true;
			    	secondOption = false;
					label.setText(" ");	
					boxEM.setSelectedIndex(0);
					boxEM.setSelectedIndex(11);
					operatingPeriod = boxEM.getSelectedItem().toString() + "-" + boxEM.getSelectedItem().toString();
					firstOperatingPeriod = null;
					secondOperatingPeriod = null;
					panel.remove(thirdPanel);
					panel.updateUI();
					
					pane = parent.tabbedPane;
					int index = pane.indexOfTab("location");  	  // it is associate with location panel.				
					if(index >= 0) {
						locationPanel = (LocationPanel) pane.getComponentAt(index);
						locationPanel.update1();
					}
			    	
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		r2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				try {
					r2.setSelected(true);
					firstOption = false;
			    	secondOption = true;
			    	label.setText("Select beginning and ending months for the 1st Operating Period:");
			    	gc.insets = new Insets(5,5,10,5);
			    	gc.gridx = 0;
					gc.gridy = 3;
					panel.add(thirdPanel,gc);							
					boxEM.setSelectedIndex(5);
					operatingPeriod = null;
					firstOperatingPeriod = boxBM.getSelectedItem().toString() + "-" + boxEM.getSelectedItem().toString();
					textFirstOP.setText(firstOperatingPeriod);
					secondOperatingPeriod = boxEM.getItemAt((boxEM.getSelectedIndex()+1)%12).toString()  + "-" + boxBM.getItemAt((boxBM.getSelectedIndex()+11)%12).toString();
					textSecondOP.setText(secondOperatingPeriod);
					panel.updateUI();
					
					pane = parent.tabbedPane;
					int index = pane.indexOfTab("location");    // it is associate with location panel.
					if(index >= 0) {
						locationPanel = (LocationPanel) pane.getComponentAt(index);
						locationPanel.update2();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		firstPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc1 = new GridBagConstraints();		
		gc1.anchor = GridBagConstraints.NORTHWEST;
		gc1.insets = new Insets(0,10,0,10);
		
		gc1.gridx = 0;
		gc1.gridy = 0;
		firstPanel.add(r1,gc1);
		gc1.gridx = 2;
		firstPanel.add(r2,gc1);			
		firstPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Number of Operation Periods"));
		
		// The 3rd part: a panel 			
    	JPanel secondPanel = new JPanel();
    	
		boxBM.setPreferredSize(new Dimension(130,25));
		boxBM.setSelectedIndex(0);
		boxBM.addActionListener(new ActionListener()                
				{
					public void actionPerformed(ActionEvent e){
						try {
							firstOperatingPeriod = boxBM.getSelectedItem().toString() + "-" + boxEM.getSelectedItem().toString();
							textFirstOP.setText(firstOperatingPeriod);
							secondOperatingPeriod = boxEM.getItemAt((boxEM.getSelectedIndex()+1)%12).toString()  + "-" + boxBM.getItemAt((boxBM.getSelectedIndex()+11)%12).toString();
							textSecondOP.setText(secondOperatingPeriod);																					
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
		
		boxEM.setPreferredSize(new Dimension(130,25));
		boxEM.setSelectedIndex(11);		
		boxEM.addActionListener(new ActionListener()                
		{
			public void actionPerformed(ActionEvent e){
				try {
					firstOperatingPeriod = boxBM.getSelectedItem().toString() + "-" + boxEM.getSelectedItem().toString();
					textFirstOP.setText(firstOperatingPeriod);
					secondOperatingPeriod = boxEM.getItemAt((boxEM.getSelectedIndex()+1)%12).toString()  + "-" + boxBM.getItemAt((boxBM.getSelectedIndex()+11)%12).toString();
					textSecondOP.setText(secondOperatingPeriod);																					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
				
		secondPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc2 = new GridBagConstraints();		
		gc2.anchor = GridBagConstraints.NORTHWEST;
		gc2.insets = new Insets(0,10,0,20);
		
		gc2.gridx = 0;
		gc2.gridy = 0;
		secondPanel.add(labelBM,gc2);
		gc2.gridx = 2;
		secondPanel.add(labelEM,gc2);
		
		gc2.gridx = 0;
		gc2.gridy = 1;
		secondPanel.add(boxBM,gc2);
		gc2.gridx = 2;
		secondPanel.add(boxEM,gc2);
				
		//the 4th part: a panel
		thirdPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc3 = new GridBagConstraints();		
		gc3.anchor = GridBagConstraints.NORTHWEST;
		gc3.insets = new Insets(0,10,0,20);
		
		gc3.gridx = 0;
		gc3.gridy = 0;
		thirdPanel.add(labelFirstOP,gc3);
		gc3.gridx = 2;
		thirdPanel.add(labelSecondOP,gc3);
		
		gc3.gridx = 0;
		gc3.gridy = 1;
		thirdPanel.add(textFirstOP,gc3);
		gc3.gridx = 2;
		thirdPanel.add(textSecondOP,gc3);
				
		// the 5th part
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				if(locationPanel != null) {
					if(firstOption)
						locationPanel.update1();
					if(secondOption)
						locationPanel.update2();
				}
				dispose();								
			}							
		}						
		);
		
		
		// set the layout of panel
		gc.insets = new Insets(5,5,5,5);
		gc.gridx = 0;
		gc.gridy = 0;
		panel.add(firstPanel,gc);
		gc.gridy = 1;
		gc.gridwidth = 2;
		panel.add(label,gc);
		gc.gridwidth = 1;
		gc.gridy = 2;
		panel.add(secondPanel,gc);
		//gc.insets = new Insets(5,5,10,5);
		//gc.gridy = 3;
		//panel.add(thirdPanel,gc);
				
		gc.insets = new Insets(20,5,10,5);
		gc.gridx = 1;
		gc.gridy = 3;
		panel.add(buttonOK,gc);
		
		
		
		
		Container container = getContentPane();
		container.add(panel);   	
    	container.setVisible(true);    	
    	setSize(new Dimension(440,240));
    	setVisible(false);
	}
	
	
	
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}

}
