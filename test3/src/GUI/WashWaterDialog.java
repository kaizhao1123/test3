package GUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import Model_Tables.AdditionsTable;
import Model_Tables.WashWaterTable;

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
	
	int row;
	String streamName = null;
	String result = null;
	
	public WashWaterDialog(AdditionsTable mt, JTable jTable) {
		
		JDialog dialog = this;		
		panel = new JPanel();
					
		myTable = new WashWaterTable();
    	JTable databaseTable = myTable.buildMyTable(columnName,data);
    	scrollPane = new JScrollPane(databaseTable);	
        scrollPane.setPreferredSize(new Dimension(400,100));
        Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane.setViewportBorder(border);
		scrollPane.setBorder(border);
		panel.add(scrollPane);
		
		buttonHelp = new JButton ("Help");
		buttonCancel = new JButton("Cancel");
		buttonOK = new JButton("OK");
		
		
		
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (databaseTable.isEditing())
	            	  databaseTable.getCellEditor().stopCellEditing(); 
				result = myTable.model.data[4][4].toString();
				int col = jTable.getSelectedColumn();
				mt.model.data[row][col] = result;
				jTable.updateUI();
				dialog.dispose();
				
			}
		});
		
		panel.add(buttonHelp);
		panel.add(buttonOK);
		
		row = jTable.getSelectedRow();
		streamName = mt.model.data[row][0].toString();
		
		Container container = getContentPane();		
		container.add(panel);   	
    	container.setVisible(true);  
    	setTitle("Wash Water Calculator for " + streamName);
    	setSize(new Dimension(420,440));
    	setVisible(true);
	}

}
