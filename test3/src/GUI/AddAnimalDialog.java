package GUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import Model_Entity.AnimalInfo;
import Model_Tables.AnimalsTable;
import Model_Tables.ClimateTable;

public class AddAnimalDialog extends JDialog{

	MainFrame parent;
	JTabbedPane pane;
	
	AnimalsTable myTable;
    JTable jTable;
    String source;
    String station;
    
    //AnimalInfo newAnimal;
    
	public AddAnimalDialog(AnimalsTable mt, JTable jt, String sour, String sta){
		 
		myTable = mt;
		jTable = jt;
		source = sour;
		station = sta;
		
		JDialog dialog = this;		
		JPanel panel = new JPanel();
		
		JLabel animalName = new JLabel("Animal Name:");
		JLabel animalType = new JLabel("Animal Type:");
		JLabel manureVolume = new JLabel("Manure Volume:");
		JLabel volatileSolids = new JLabel("Volatile Solids:");
		JLabel totalSolids = new JLabel("Total Solids:");
		JLabel SAR = new JLabel("Sludge Accum. Ratio:");
		JLabel FWV = new JLabel("Flush Water Volume:");
		JLabel nitrogen = new JLabel("Nitrogen:");
		JLabel phosphorous = new JLabel("Phosphorous:");
		JLabel potassium = new JLabel("Potasium:");
		JLabel unit1 = new JLabel("cu.ft/day/AU");
		JLabel unit21 = new JLabel("lbs/day/AU");
		JLabel unit22 = new JLabel("lbs/day/AU");
		JLabel unit3 = new JLabel("gal/day");
		JLabel unit41 = new JLabel("lbs/ton");
		JLabel unit42 = new JLabel("lbs/ton");
		JLabel unit43 = new JLabel("lbs/ton");
			    
	    JTextField valOfAN = new JTextField(" ");
		valOfAN.setPreferredSize(new Dimension(70,20));
		String[] animal = {"Beef","Dairy","Goat","Horse","Poultry", "Sheep","Swine","Veal"};
		JComboBox valOfAT = new JComboBox(animal);
		valOfAT.setSelectedIndex(0);
		valOfAT.setPreferredSize(new Dimension(120,20));
		JTextField valOfMV = new JTextField(" ");
		valOfMV.setPreferredSize(new Dimension(70,20));
		JTextField valOfVS = new JTextField(" ");
		valOfVS.setPreferredSize(new Dimension(70,20));
		JTextField valOfTS = new JTextField(" ");
		valOfTS.setPreferredSize(new Dimension(70,20));
		JTextField valOfSAR = new JTextField(" ");	
		valOfSAR.setPreferredSize(new Dimension(70,20));
		JTextField valOfFWV = new JTextField(" ");
		valOfFWV.setPreferredSize(new Dimension(70,20));
		JTextField valOfNIT = new JTextField(" ");
		valOfNIT.setPreferredSize(new Dimension(70,20));
		JTextField valOfPHO = new JTextField(" ");
		valOfPHO.setPreferredSize(new Dimension(70,20));
		JTextField valOfPOT = new JTextField(" ");
		valOfPOT.setPreferredSize(new Dimension(70,20));
		
		JRadioButton rb = new JRadioButton("Lactating Cow");
		
		JButton cancelB = new JButton("Cancel");
		JButton okB = new JButton("OK");
		
		okB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	

				String n = valOfAN.getText();
				String t = valOfAT.getSelectedItem().toString();
				
				String[] dataSet = new String[11];
				dataSet[0] = valOfMV.getText();
				dataSet[1] = "unknow";
				dataSet[2] = valOfTS.getText();
				dataSet[3] = valOfVS.getText();
				dataSet[4] = valOfSAR.getText();
				if(rb.isSelected())
					dataSet[5] = "1";
				else
					dataSet[5] = "0";
				dataSet[6] = valOfFWV.getText();
				dataSet[7] = valOfNIT.getText();
				dataSet[8] = valOfPHO.getText();
				dataSet[9] = valOfPOT.getText();
				dataSet[10] = station;
				
				if(allNotEmpty(dataSet)) {
					
					AnimalInfo ele = new AnimalInfo(n, t , source, dataSet, 0);
					DecimalFormat df = new DecimalFormat("0.00");	        		
	         		String[] rowData = new String[11];
	    			String quantity = "0";
	    			String weight = "0";
	    			String m = ele.data[0];
	    			String ts = ele.data[2];
	    			String vs = ele.data[3];
	    			double qDou = Double.parseDouble(quantity);
	    			double wDou = Double.parseDouble(weight);
	    			double mDou = Double.parseDouble(m);
	    			double tsDou = Double.parseDouble(ts);
	    			double vsDou = Double.parseDouble(vs);
	    		         		         		 
	    			rowData[0] = ele.name;
	    			rowData[1] = ele.type;
	    			rowData[2] = quantity;
	    			rowData[3] = weight;
	    			rowData[4] = m;
	    			rowData[5] = vs;
	    			rowData[6] = ts;
	    			rowData[7] = df.format(mDou * qDou * wDou / 1000);
	    			rowData[8] = df.format(vsDou * qDou * wDou / 1000);
	    			rowData[9] = df.format(tsDou * qDou * wDou / 1000);
	    			rowData[10] = df.format(mDou * qDou * wDou * 60 / 1000);
	    			//rowData[10] = Double.toString(mDou * qDou * wDou * 60 / 1000);
	    			myTable.model.addRow(rowData);	    					
					for(int i = 2; i < myTable.model.data[0].length; i++) {
						myTable.model.mySetValueAt(myTable.model.getNewSum(i), myTable.model.getRowCount()-1, i);
					}
					pane = parent.tabbedPane;
					int index = pane.indexOfTab("animal");
			    	AnimalsPanel ap = (AnimalsPanel) pane.getComponentAt(index);
			    	ap.newAnimalInfo = ele;
			    	ap.animalInTable.add(ap.newAnimalInfo);					
					jTable.updateUI();
					dialog.dispose();
				}
				else {					
					JDialog d = new JDialog(dialog, "dialog Box"); 					  		             
		            JLabel l = new JLabel("The input data is not enough"); 		  
		            d.add(l); 		 		            
		            d.setSize(100, 100); 
		            d.setVisible(true); 
				}									
			}							
		}						
		);
						
		// build the panel for "Manure Master Only"
		JPanel mmoPanel = new JPanel();
		mmoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc1 = new GridBagConstraints();		
		gc1.anchor = GridBagConstraints.NORTHWEST;
		gc1.insets = new Insets(10,0,0,60);
		
		gc1.gridx = 0;
		gc1.gridy = 0;
		mmoPanel.add(nitrogen,gc1);
		gc1.gridy = 1;
		mmoPanel.add(phosphorous,gc1);
		gc1.gridy = 2;
		mmoPanel.add(potassium,gc1);
		gc1.insets = new Insets(10,0,0,10);
		gc1.gridx = 1;
		gc1.gridy = 0;
		mmoPanel.add(valOfNIT,gc1);
		gc1.gridy = 1;
		mmoPanel.add(valOfPHO,gc1);
		gc1.gridy = 2;
		mmoPanel.add(valOfPOT,gc1);
		
		gc1.insets = new Insets(10,0,0,70);
		gc1.gridx = 3;
		gc1.gridy = 0;
		mmoPanel.add(unit41,gc1);
		gc1.gridy = 1;
		mmoPanel.add(unit42,gc1);
		gc1.gridy = 2;
		mmoPanel.add(unit43,gc1);
		
		mmoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Manure Master Only"));
		mmoPanel.setPreferredSize(new Dimension(350,120));
		

		// set the layout of panel		
    	panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();		
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.insets = new Insets(10,10,5,5);
		gc.gridx = 0;
		gc.gridy = 0;
		panel.add(animalName,gc);
		gc.gridy = 1;		
		panel.add(animalType,gc);
		gc.gridy = 2;
		panel.add(manureVolume,gc);
		gc.gridy = 3;
		panel.add(volatileSolids,gc);
		gc.gridy = 4;
		panel.add(totalSolids,gc);
		gc.gridy = 5;
		panel.add(SAR,gc);
		gc.gridy = 6;
		panel.add(FWV,gc);
		gc.insets = new Insets(10,0,5,0);
		gc.gridy = 7;
		gc.gridwidth = 6;
		gc.gridheight = 3;
		panel.add(mmoPanel,gc);
		gc.insets = new Insets(10,10,5,5);
		gc.gridy = 10;
		gc.gridwidth =1;
		panel.add(rb,gc);
		
		gc.gridx = 2;
		gc.gridy = 0;
		panel.add(valOfAN, gc);
		gc.gridwidth = 3;
		gc.gridy = 1;
		panel.add(valOfAT, gc);
		gc.gridwidth = 1;
		gc.gridy = 2;
		panel.add(valOfMV, gc);
		gc.gridy = 3;
		panel.add(valOfVS, gc);
		gc.gridy = 4;
		panel.add(valOfTS, gc);
		gc.gridy = 5;
		panel.add(valOfSAR, gc);
		gc.gridy = 6;
		panel.add(valOfFWV, gc);
		
		gc.gridx = 4;
		gc.gridy = 2;
		panel.add(unit1, gc);
		gc.gridy = 3;
		panel.add(unit21, gc);
		gc.gridy = 4;
		panel.add(unit22, gc);
		gc.gridy = 6;
		panel.add(unit3, gc);
		
		
		gc.gridx = 4;
		gc.gridy = 10;
		panel.add(cancelB, gc);
		gc.insets = new Insets(10,5,5,0);
		gc.gridx = 5;
		gc.gridy = 10;
		panel.add(okB,gc);
		
		Container container = getContentPane();
		container.add(panel);   	
    	container.setVisible(true);    	
    	setSize(new Dimension(420,440));
    	setVisible(true);
		
	}
	
	private boolean allNotEmpty(String[] s) {
		if(s != null) {
			for(int i = 0; i < s.length; i++) {
				if(s[i].equals(" "))
					return false;
			}
			return true;
		}
		else
			return false;
		
	}
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
