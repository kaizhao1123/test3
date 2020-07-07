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

public class FlushWaterDialog extends JDialog{
	
	JPanel panel;
	String[] columnName = { "<html> Animal  <br> ---Units --->  </html>", 
							"<html> Quantity  <br>   </html>", 
							"<html>Sug. Flush Volume  <br> (gal/head) </html>", 
							"<html>Flush Volume  <br>  (gal/head) </html>",
							"<html>Daily Flush  <br> (gallons) </html>"};    
	Object data[][] = { {"Cow", "10", "500", "0.00",  "0.00"},
						{"Feeder Calf", "20", "0.00", "0.00",  "0.00"}};
	WashWaterTable myTable;
	JTable databaseTable;
	JScrollPane scrollPane;
	JButton buttonHelp;
	JButton buttonCancel;
	JButton buttonOK;
	
	int row;
	String streamName = null;
	String result = null;
	
	public FlushWaterDialog(AdditionsTable mt, JTable jTable) {
		
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
				int rowCount = myTable.model.data.length;
				result = myTable.model.data[rowCount-1][4].toString();
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
    	setTitle("Flush Water Calculator for " + streamName);
    	setSize(new Dimension(420,440));
    	setVisible(true);
	}

}
