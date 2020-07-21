package Model_Tables;

import java.text.DecimalFormat;

import javax.swing.table.AbstractTableModel;
/**
 * This class is the base of the Jtable, which used in our panels.
 * It inherits AbstractTableModel, and add some new functions.
 * The case where the last row is the total row is considered in this model.
 * 
 * @author Kai Zhao

 */
public class TableModelWithTotal extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	public String[] columnName;
	public Object[][] data;

	/**
	 * The constructor of this class
	 * @param s	the column names of the table
	 * @param o the data of the table
	 */
	public TableModelWithTotal(String[] s, Object[][] o) {
		columnName = s;
		data = o;
	}

	// add the "total" row into the model, and locates at the last row
	public void addTotalRow(String[] s) {
		Object[][] np = null;
		int l; // the data length
		int cl = columnName.length;
		;
		if (data == null) {
			l = 1;
			np = new Object[l][cl];
		} else {
			l = data.length + 1;
			np = new Object[l][cl];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < columnName.length; j++) {
					np[i][j] = data[i][j];
				}
			}

		}
		for (int i = 0; i < s.length; i++) {
			np[l - 1][i] = s[i];
		}

		data = np;

	}

	/**
	 *  inserts a row data into the model's data, locates at the row after the rowIndex. 
	 *  that is, above the "total" row. 
	 * @param s the row data will be added into the model
	 * @param rowIndex the target index
	 */
	public void insertRow(String[] s, int rowIndex) {
		int l = data.length + 1;
		int cl = columnName.length;
		
		//1. creates a new "data" with 1 more row than original "data".
		Object[][] np = new Object[l][cl];
		
		//2. copy the original "data", whose row index is less and equal the target rowIndex.
		for (int i = 0; i < rowIndex+1; i++) {
			for (int j = 0; j < columnName.length; j++) {
				np[i][j] = data[i][j];
			}
		}
		
		//3. add the target row data into the target index + 1
		for (int j = 0; j < s.length; j++) {
			np[rowIndex+1][j] = s[j];
		}
		//4. copy the origin rows after the index except "total" row into new Object[][]
		for (int i = rowIndex+1; i < l-2 ; i++) {
			for (int j = 0; j < columnName.length; j++) {
				np[i + 1][j] = data[i][j];
			}
		}
		
		//4. change the value of the last row: "total"
		np[l-1][0] = "Total";		
		for (int i = 1; i < columnName.length; i++) {
			np[l-1][i] = Double.toString(getNewSum(i));
		}
		data = np;
	}

	/**
	 * deletes a row data from the model's data, except the "total" row.
	 * @param s the row data will be added into the model
	 */
	public void deleteRow(int rowIndex) {
		int l = data.length - 1;
		int cl = columnName.length;
		
		//1. creates a new "data" with 1 less row than the original "data"
		Object[][] np = new Object[l][cl];
		
		//2. copy the rows before the target row of the original "data" into the new "data"
		for (int i = 0; i < rowIndex; i++) {
			for (int j = 0; j < columnName.length; j++) {
				np[i][j] = data[i][j];
			}
		}
		/*
		 * 3. copy the rows after the target row, include the "total" row, of the original "data" into the new "data".
		 *  Because "getNewSum" is based on the old data, so we have to need the step4 to change the value of the
		 * "total" row after copying the old "data" to the new "data"
		 */
		for (int i = rowIndex; i < l; i++) {
			for (int j = 0; j < columnName.length; j++) {
				np[i][j] = data[i + 1][j];
			}
		}		
		data = np;	
		
		//4. change the value of the last row: "total".		 		 		
		for (int i = 1; i < columnName.length; i++) {
			data[l-1][i] = Double.toString(getNewSum(i));
		}
	}

	// adds new column after the model's last column, and delete the last row "total"
	public void addColumn() {
		int rl = data.length;
		int cl = data[0].length + 1;
		Object[][] np = new Object[rl-1][cl];
		for (int i = 0; i < rl-1; i++) {
			for (int j = 0; j < cl - 1; j++) {
				np[i][j] = data[i][j];
			}
			np[i][cl - 1] = "0";
		}
		data = np;
		
		
	}
	
	// delete the target column from the model, and delete the last row "total"
	public void deleteColumn(int colIndex) {
		int rl = data.length;
		int cl = data[0].length - 1;
		Object[][] np = new Object[rl-1][cl];
		for(int i = 0; i < rl-1; i++) {
			for(int j = 0; j < colIndex; j++)
				np[i][j] = data[i][j];
			for(int k = colIndex; k < cl; k++) {
				np[i][k] = data[i][k+1];
			}
		 }
		data = np;
	}

	// Checks whether the data in the first column contains the target string
	public boolean isContained(String s) {
		for (int i = 0; i < data.length; i++) {
			if (data[i][0] == s)
				return true;
		}
		return false;
	}

	// Gets the row index of the target string
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

	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	// set some special columns to be not editable.
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		String colName = columnName[columnIndex].toString();
		
		// set uneditable column of animalsTable
		String s1 = "<html>Animal <br> (type) </html>";
		String s2 = "<html> Manure <br> (cu.ft/day) </html>";
		String s3 = "<html> VS <br> (lbs/day) </html>";
		String s4 = "<html> TS <br> (lbs/day) </html>";
		String s5 = "<html>Manure <br> (lbs/day) </html>";
		
		// set uneditable column of runoffTable
		String s6 = "Monthly Totals";
		
		if (columnIndex == 0 || rowIndex == data.length -1 
				|| colName == s1
				|| colName == s2
				|| colName == s3
				|| colName == s4
				|| colName == s5
				|| colName == s6)
			return false;
		else
			return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	public void mySetValueAt(Object value, int row, int col) {
		data[row][col] = value;
	}

	// Gets the sum of one column before adding the total row, that is, this function only used when the table initializing.
	public double getSum(int column) {
		if (data == null)
			return 0.00;
		else {
			double sum1 = 0.00;
			for (int i = 0; i < getRowCount(); i++) {
				String ddd = getValueAt(i, column).toString();
				for (int j = 0; j < ddd.length(); j++) {
					if (!Character.isDigit(ddd.charAt(j)) && ddd.charAt(j) != '.') {
						return 0.00;
					}
				}
				double val = Double.parseDouble(ddd);
				sum1 += val;
			}
			DecimalFormat df = new DecimalFormat("0.00");
			return Double.parseDouble(df.format(sum1));
		}
	}

	// Gets the sum of one column after adding the total row, that is, it used in the changing of the table.
	public double getNewSum(int column) {
		if (data == null)
			return 0.00;
		else {

			double sum1 = 0.00;
			for (int i = 0; i < getRowCount() - 1; i++) {
				String ddd = getValueAt(i, column).toString();
				for (int j = 0; j < ddd.length(); j++) {
					if (!Character.isDigit(ddd.charAt(j)) && ddd.charAt(j) != '.') {
						return 0.00;
					}
				}
				double val =  Double.parseDouble(ddd);
				sum1 += val;
			}
			DecimalFormat df = new DecimalFormat("0.00");
			return Double.parseDouble(df.format(sum1));
		}
	}

	// Gets each column's sum of a row, used for the function "addTotalRow"
	public String[] getEachSum() {
		String[] eachSum = new String[getColumnCount()];
		eachSum[0] = "Total";
		for (int i = 1; i < getColumnCount(); i++) {
			double num = getSum(i);
			if (num == 0.00) {
				eachSum[i] = "N/A";
			} else
				eachSum[i] = Double.toString(getSum(i));
		}
		return eachSum;
	}

}
