package Panels;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Entity.AdditionsTable;
import Entity.WashWaterTable;

public class WashWaterDialog extends JDialog{
	
	JPanel panel;
	String[] columnName = {  "Hoses", "Amount", "Units", "Minutes/Day","total(gal/day)" };   
	Object data[][] = { 	{"Hose 1", "0.00", "gal/minute", "0.00",  "0.00"},
							{"Hose 2", "0.00", "gal/minute", "0.00",  "0.00"}, 
							{"Hose 3", "0.00", "gal/minute", "0.00",  "0.00"},
							{"Hose 4", "0.00", "gal/minute", "0.00",  "0.00"},};
	WashWaterTable myTable;
	JTable databaseTable;
	JScrollPane scrollPane;
	JButton buttonHelp;
	JButton buttonCancel;
	JButton buttonOK;
	
	//result
	String result = null;
	
	public WashWaterDialog(AdditionsTable mt, JTable jTable) {
		
		JDialog dialog = this;		
		panel = new JPanel();
					
		myTable = new WashWaterTable();
    	JTable databaseTable = myTable.buildMyTable(columnName,data);
    	scrollPane = new JScrollPane(databaseTable);	
        scrollPane.setPreferredSize(new Dimension(400,100));				
		panel.add(scrollPane);
		
		buttonHelp = new JButton ("Help");
		buttonCancel = new JButton("Cancel");
		buttonOK = new JButton("OK");
		
		
		
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = myTable.model.data[4][4].toString();
				int row = jTable.getSelectedRow();
				int col = jTable.getSelectedColumn();
				mt.model.data[row][col] = result;
				jTable.updateUI();
				dialog.dispose();
			}
		});
		
		panel.add(buttonHelp);
		panel.add(buttonOK);
		
		
		
		Container container = getContentPane();		
		container.add(panel);   	
    	container.setVisible(true);  
    	setTitle("Wash Water Calculator for ff");
    	setSize(new Dimension(420,440));
    	setVisible(true);
	}

}