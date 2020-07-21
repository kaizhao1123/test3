package Model_Tables;

import java.text.DecimalFormat;
import javax.swing.table.AbstractTableModel;

/**
 * This class is the base of the Jtable, which used in our panels.
 * It inherits AbstractTableModel, and add some new functions.
 * The case where the last row is the total row is not considered in this model.
 * 
 * @author Kai Zhao
 *
 */
public class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	public String[] columnName;
	public Object[][] data;

	/**
	 * The constructor of this class
	 * @param s	the column names of the table
	 * @param o the data of the table
	 */
	public TableModel(String[] s, Object[][] o) {
		// TODO Auto-generated constructor stub
		columnName = s;
		data = o;
	}
	
	/**
	 * insert one row into the table after the target index. i.g., 
	 * the index of the inserted row is the target index.
	 * @param s	the row data
	 * @param index	the target index.
	 */
	public void insertRow(String[] s, int index) {
		int l;
		int cl = columnName.length;
		if(data == null) {
			l = 1;
			Object[][] np = new Object[l][cl];
			for(int j = 0; j < columnName.length; j++) {
				np[0][j] = s[j];
			}
			data = np;
		}					
		else {
			l = data.length + 1;			
			Object[][] np = new Object[l][cl];
		
			// copy the rows before index into new Object[][]
			for (int i = 0; i < index ; i++) {
				for (int j = 0; j < columnName.length; j++) {
					np[i][j] = data[i][j];
				}
			}
			// add the target row data into the target index
			for (int j = 0; j < s.length; j++) {
				np[index][j] = s[j];
			}
			// copy the origin rows after the index into new Object[][]
			for (int i = index; i < l-1 ; i++) {
				for (int j = 0; j < columnName.length; j++) {
					np[i + 1][j] = data[i][j];
				}
			}	
			data = np;
		}
		
	}
	
	// delete the row data from the target index.
	public void deleteRow(int rowIndex) {
		int l = data.length - 1;
		int cl = columnName.length;
		Object[][] np = new Object[l][cl];

		// copy the rows before index into new Object[][]
		for (int i = 0; i < rowIndex; i++) {
			for (int j = 0; j < cl; j++) {
				np[i][j] = data[i][j];
			}
		}
		// copy the origin rows after the index into new Object[][]
		for (int i = rowIndex; i < l; i++) {
			for (int j = 0; j < cl; j++) {
				np[i][j] = data[i + 1][j];
			}
		}
		data = np;

	}
	
	// Checks the first column of the data whether contains the target string.
	public boolean isContained(String s) {
		for (int i = 0; i < data.length; i++) {
			if (data[i][0] == s)
				return true;
		}
		return false;
	}

	// gets the row location of the target string.
	public int rowIndexOfElement(String s) {
		for (int i = 0; i < data.length; i++) {
			if (data[i][0] == s)
				return i;
		}
		return -1;
	}

	@Override
	public int getRowCount() {
		if (data == null)
			return 0;
		else
			return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnName.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	@Override
	public String getColumnName(int col) {
		return columnName[col];
	}

	/*@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}*/

	@Override
	// set some special columns to be not editable.
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		
		String colName = columnName[columnIndex].toString();
		String s1 = "<html> LV Amt <br> (cu.ft/day) </html>";
		String s2 = "<html> Cv Amt <br> (cu.ft/day) </html>";
		String s3 = "Step 3";
		
		if (columnIndex == 0 || colName == s1 || colName == s2 || colName == s3) {
			return false;
		}
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	// Change the new value to the target location.
	public void mySetValueAt(Object value, int row, int col) {
		data[row][col] = value;
	}

}
