package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import GUI.AnimalsPanel;
import GUI.LocationsPanel;
import GUI.MgmtTrainPanel;
import GUI.RunoffPanel;
import Model_Tables.ClimateTable.MyCellRenderer;

/**
 * The purpose of this class is to create a JTable based on the table model.
 * At the same time, it implements TableModelListener. In this class, some
 * special properties of the JTable can be set up. Such as: the color or font of the
 * table cell, the change of the cell's content.
 * @author Kai Zhao
 *
 */
public class AnimalsTable implements TableModelListener {

	public JTabbedPane pane;
	public JTable ntable;
	public TableModelWithTotal model;
	public AnimalsPanel animalsPanel;
	public LocationsPanel locationsPanel;
	public MgmtTrainPanel mgmtTrainPanel;

	String[] columnNames;
	Object[][] data;
	// the default color of the table. The cell with this color can be editable. 
	Color defaultColor = Color.lightGray;
	// the color of the "total" row. The cell with this color can't be editable.
	Color newColor = Color.cyan;

	/**
	 * To create a JTable with tool tip and set up the color of the table. 
	 * @param s the table column name
	 * @param o the table data
	 * @param tbp the tabbedPane
	 * @return JTable with color
	 */
	public JTable buildMyTable(String[] s, Object[][] o) {

		columnNames = s;
		data = o;
				
		model = new TableModelWithTotal(columnNames, data);		
		model.addTableModelListener(this);
		model.addTotalRow(model.getEachSum());	// add the "total" row.
		
		ntable = new JTable(model) {
			/**
			 * creates the tool tip, a kind of floating window.
			 * it used for offer the tip of the limitation of the value in the cell.
			 * it will show the tip, when the mouse stops at the target cell. 
			 */
			 public String getToolTipText(MouseEvent e) {   
	                int row=ntable.rowAtPoint(e.getPoint());   
	                int col=ntable.columnAtPoint(e.getPoint());   
	                String tiptextString=null;   
	                if(row >= 0 && row < ntable.getRowCount()-1 && col == 3){   
	                    double value= Double.parseDouble(ntable.getValueAt(row, col).toString()); 
	                    String type = model.data[row][1].toString();
	                    if(type.equals("Beef") && (value > 1200 || value < 150))   
	                        tiptextString = "The Range for " + type + " weight is 150-1200"; 
	                    else if(type.equals("Horse") )
	                    	tiptextString = "The Range for " + type + " weight is ?";
	                    else if(type.equals("Poultry") && (value > 25 || value < 2 ))
	                    	tiptextString = "The Range for " + type + " weight is 2-25";
	                    else if(type.equals("Dairy") && (value > 1400 || value < 150 ))
	                    	tiptextString = "The Range for " + type + " weight is 150-1400";
	                    else if(type.equals("Sheep") && (value > 500 || value < 25 ))
	                    	tiptextString = "The Range for " + type + " weight is 25-500";
	                    else if(type.equals("Swine") && (value > 600 || value < 8 ))
	                    	tiptextString = "The Range for " + type + " weight is 8-600";
	                    else if(type.equals("Veal") && (value > 200 || value < 70 ))
	                    	tiptextString = "The Range for " + type + " weight is 70-200";	                    
	                }   
	                return tiptextString; 
		}};
		ntable.getTableHeader().setReorderingAllowed(false);	// fix the header
		setCellRenderer();
		ntable.setVisible(true);		
		return ntable;
	}

	public void getTabbedPane(JTabbedPane jtp) {
		pane = jtp;
	}
	
	/**
	 * Sets up cellRenderers of all cells following the column.
	 */
	public void setCellRenderer() {
		MyCellRenderer tcr = new MyCellRenderer();
		for(int i = 0; i < ntable.getColumnCount(); i++) {
			ntable.getColumnModel().getColumn(i).setCellRenderer(tcr);
		}
	}
	
	/**
	 * Define special cellRenderer based on the requirement of the table.
	 * In climate table, except the "total" row, the color of the cell in the columns from 3 to 7 
	 * is default color, and these cells can be editable; columns from 8 to 11 is new color, and 
	 * can't be editable.
	 * @author Kai Zhao
	 *
	 */
	class MyCellRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				int rowCount = ntable.getRowCount();
				if (column == 0 || column == 1) {
					setBackground(null);							
				}				
				else if(column == 3 && row < rowCount-1) {
					try {
						double v= Double.parseDouble(model.data[row][3].toString()); 
	                    String type = model.data[row][1].toString();
	                    
	                    //sets up the color of the cell with the limitation value
	                    if( (type.equals("Beef") && (v > 1200 || v < 150))  
	                    		||  (type.equals("Horse")) 
	                    		||  (type.equals("Poultry") && (v > 25 || v < 2 ))
	                    		|| 	(type.equals("Dairy") && (v > 1400 || v < 150 ))
	                    		||	(type.equals("Sheep") && (v > 500 || v < 25 ))
	                    		||	(type.equals("Swine") && (v > 600 || v < 8 ))
	                    		||  (type.equals("Veal") && (v > 200 || v < 70 ))  )			                    	
	                    	setBackground(Color.red);
	                    else
	                    	setBackground(defaultColor);
					}catch(Exception ev) {
						
					}											
				}
				else if( (column == 2 || column == 4 || column == 5 || column == 6) && row < rowCount-1) {
					setBackground(defaultColor);
				}
				else if( (column == 7 || column == 8 || column == 9 || column == 10) && row < rowCount-1) {
					setBackground(newColor);
				}
				else 
					setBackground(newColor);
			return c;
		}
	}
	// check whether String[] contains the target String
	private boolean isContain(String[] list, String s) {
		for(int i = 0; i < list.length; i++) {
			if(list[i].equals(s))
				return true;
		}
		return false;			
	}
	

	@Override
	/**
	 * the data changing of some cell will lead to the change of "other" cells of the table.
	 * the "other" cells include the "total" row (the last row) and several special columns.
	 * the data in the columns (from the 3rd to the 7th) affect the data in the 
	 * columns (from 8th to the last)
	 */
	public void tableChanged(TableModelEvent e) {

		int row = ntable.getSelectedRow();
		Object[] ele = model.data[row];
		DecimalFormat df = new DecimalFormat("0.00");
		Double qDou = Double.parseDouble(ele[2].toString());
		Double wDou = Double.parseDouble(ele[3].toString());
		Double mDou = Double.parseDouble(ele[4].toString());
		Double tsDou = Double.parseDouble(ele[6].toString());
		Double vsDou = Double.parseDouble(ele[5].toString());

		ele[2] = df.format(Math.round(qDou));
		ele[7] = df.format(mDou * qDou * wDou / 1000);
		ele[8] = df.format(vsDou * qDou * wDou / 1000);
		ele[9] = df.format(tsDou * qDou * wDou / 1000);
		ele[10] = df.format(mDou * qDou * wDou * 60 / 1000);

		for (int i = 2; i < 11; i++) {
			if(i == 2)
				model.mySetValueAt(df.format(model.getNewSum(i)), model.getRowCount() - 1, i);
			else if(i >= 3 && i <= 6)
				model.mySetValueAt("N/A", model.getRowCount() - 1, i);
			else
				model.mySetValueAt(model.getNewSum(i), model.getRowCount() - 1, i);
		}
		ntable.repaint();
		
		// update the output of animalsPanel
		int aniIndex = pane.indexOfTab("animals");
		animalsPanel = (AnimalsPanel) pane.getComponentAt(aniIndex);
		if(animalsPanel.animalPanelOutput.size() > 0) {
			animalsPanel.updateOutput();
		}
		
		/*
		 *  The value of the "quantity" associate with the table in the location panel and mgmttrain panel.
		 *  If the value > 0, then it will appear in the table in the location panel,
		 *  otherwise, not.
		 */
		if(Double.parseDouble(ele[2].toString()) > 0.00) {			
			try {
				int locIndex = pane.indexOfTab("locations"); 				
				if(locIndex >= 0) {
					locationsPanel = (LocationsPanel) pane.getComponentAt(locIndex);
					if(!isContain(locationsPanel.columnName, ele[0].toString())) {
						locationsPanel.addTableColumn(ele[0].toString());
						int mgmtIndex = pane.indexOfTab("Mgmt Train");
						if(mgmtIndex >= 0) {
							mgmtTrainPanel = (MgmtTrainPanel) pane.getComponentAt(mgmtIndex);
							mgmtTrainPanel.updateBottomJtables("jTable3");
							mgmtTrainPanel.updateBottomJtables("jTable4");
						}
					}
						
				}
			}catch(Exception ef) {
				
			}
					
		}
		if(Double.parseDouble(ele[2].toString()) <= 0.00) {
			try {
				int locIndex = pane.indexOfTab("locations"); 				
				if(locIndex >= 0) {
					locationsPanel = (LocationsPanel) pane.getComponentAt(locIndex);																	
					locationsPanel.deleteTableColumn(ele[0].toString());
					int mgmtIndex = pane.indexOfTab("Mgmt Train");
					if(mgmtIndex >= 0) {
						mgmtTrainPanel = (MgmtTrainPanel) pane.getComponentAt(mgmtIndex);
						mgmtTrainPanel.updateBottomJtables("jTable3");
						mgmtTrainPanel.updateBottomJtables("jTable4");
					}
				}
			}catch(Exception ef) {
				
			}
		}
		
		
	}
}
