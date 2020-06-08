package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

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
	// the default color of the table. 
	Color cc = Color.lightGray;

	/**
	 * To create a JTable and set up the color of the table. 
	 * almost the same as the climateTable
	 * @param s the table column name
	 * @param o the table data
	 * @return JTable with color
	 */
	public JTable buildMyTable(String[] s, Object[][] o) {

		columnNames = s;
		data = o;

		model = new TableModelWithTotal(columnNames, data);	
		model.addTableModelListener(this);
		model.addTotalRow(model.getEachSum());
		ntable = new JTable(model);
		int rowcount = ntable.getRowCount();
		int colcount = ntable.getColumnCount();
		setColor(rowcount-1,rowcount-1,1,colcount,Color.cyan);
		ntable.setVisible(true);
		return ntable;
	}

	/**
	 * Sets the background color of a specific rectangular area: between two-row, and between two-column
	 * @param row_start
	 * @param row_end
	 * @param col_start
	 * @param col_end
	 * @param ncolor
	 */
	public void setColor(int row_start, int row_end, int col_start, int col_end, Color ncolor) {
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if (row >= row_start && row <= row_end && column >= col_start && column <= col_end) {
						setBackground(ncolor);
					} else if (column == 0) {
						setBackground(null);
					} else
						setBackground(cc);
					return c;
				}
			};

			for (int i = 0; i < col_end; i++) {
				ntable.setDefaultRenderer(ntable.getColumnClass(i), tcr);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
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
