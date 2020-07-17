package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import Model_Tables.ClimateTable.MyCellRenderer;

/**
 * The purpose of this class is to create a JTable based on the table model.
 * At the same time, it implements TableModelListener. In this class, some
 * special properties of the JTable can be set up. Such as: the color or font of the
 * table cell, the change of the cell's content.
 * @author Kai Zhao
 *
 */
public class LocationsTable implements TableModelListener {

	public JTable ntable;
	public TableModelWithTotal model;

	String[] columnNames;
	Object[][] data;
	// the default color of the table. The cell with this color can be editable. 
	Color defaultColor = Color.lightGray;
	// the color of the "total" row. The cell with this color can't be editable.
	Color newColor = Color.cyan;

	/**
	 * To create a JTable and set up the special cellRenderer of the table. 
	 * @param s the table column name
	 * @param o the table data
	 * @return JTable with special cellRenderer
	 */
	public JTable buildMyTable(String[] s, Object[][] o) {

		columnNames = s;
		data = o;
		model = new TableModelWithTotal(columnNames, data);	
		model.addTableModelListener(this);
		model.addTotalRow(model.getEachSum());	// add "total" row
		ntable = new JTable(model);
		ntable.getTableHeader().setReorderingAllowed(false);	//fix the header
		setCellRenderer();
		
		ntable.setVisible(true);
		return ntable;
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
	 * In climate table, it only requires that the color of the "total" row is special.
	 * @author Kai Zhao
	 *
	 */
	class MyCellRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
				int rowCount = ntable.getRowCount();
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column == 0 ) {
					setBackground(null);							
				}				
				else if( (column > 0) && row < rowCount-1 ) {
					setBackground(defaultColor);										
				} 						
				else 
					setBackground(newColor);
			return c;
		}
	}
	
	@Override
	// the data changing of one cell will lead to the change of the "total" row (the last row)
	public void tableChanged(TableModelEvent e) {
		int col = e.getColumn();
		model.mySetValueAt(model.getNewSum(col), model.getRowCount() - 1, col);
		ntable.repaint();
	}
}
