package Entity;

import java.text.DecimalFormat;
import javax.swing.table.AbstractTableModel;

public class TableModelWithTotal extends AbstractTableModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public String[] columnName;
	public Object[][] data;

	public TableModelWithTotal(String[] s, Object[][] o) {

		columnName = s;
		data = o;
	}


	public void addTotalRow(String[] s) {
		Object[][] np = null;
		int l;
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

	public void addRow(String[] s) {
		int l = data.length + 1;
		int cl = columnName.length;
		Object[][] np = new Object[l][cl];
		for (int i = 0; i < data.length - 1; i++) {
			for (int j = 0; j < columnName.length; j++) {
				np[i][j] = data[i][j];
			}
		}
		for (int i = 0; i < s.length; i++) {
			np[l - 2][i] = s[i];
		}
		for (int i = 0; i < columnName.length; i++) {
			np[l - 1][i] = data[l - 2][i];
		}
		data = np;
	}

	public void deleteRow(int row) {
		int l = data.length - 1;
		int cl = columnName.length;
		Object[][] np = new Object[l][cl];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < columnName.length; j++) {
				np[i][j] = data[i][j];
			}
		}
		for (int i = row - 1; i < data.length - 1; i++) {
			for (int j = 0; j < columnName.length; j++) {
				np[i][j] = data[i + 1][j];
			}
		}
		data = np;
	}

	public void addColumn() {
		int rl = data.length;
		int cl = data[0].length + 1;
		Object[][] np = new Object[rl - 1][cl];
		for (int i = 0; i < rl - 1; i++) {
			for (int j = 0; j < cl - 1; j++) {
				np[i][j] = data[i][j];
			}
			np[i][cl - 1] = "0";
		}
		data = np;
	}
	
	public void deleteColumn(int col) {
		int rl = data.length;
		int cl = data[0].length - 1;
		Object[][] np = new Object[rl-1][cl];
		for(int i = 0; i < rl-1; i++) {
			for(int j = 0; j < col; j++)
				np[i][j] = data[i][j];
			for(int k = col; k < cl; k++) {
				np[i][k] = data[i][k+1];
			}
		 }
		data = np;
	}

	public boolean isContained(String s) {
		for (int i = 0; i < data.length; i++) {
			if (data[i][0] == s)
				return true;
		}
		return false;
	}

	public int rowOfElement(String s) {
		for (int i = 0; i < data.length; i++) {
			if (data[i][0] == s)
				return i + 1;
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

	public double getSum(int column) {
		if (data == null)
			return Double.parseDouble(new DecimalFormat("0.00").format(0.00));
		else {
			double sum1 = 0.00;
			for (int i = 0; i < getRowCount(); i++) {
				String ddd = (String) getValueAt(i, column);
				for (int j = 0; j < ddd.length(); j++) {
					if (!Character.isDigit(ddd.charAt(j)) && ddd.charAt(j) != '.') {
						return 0.00;
					}
				}
				Double val = Double.parseDouble(ddd);
				sum1 += val;
			}
			DecimalFormat df = new DecimalFormat("0.00");
			return Double.parseDouble(df.format(sum1));
		}
	}

	public double getNewSum(int column) {
		if (data == null)
			return 0.00;
		else {

			double sum1 = 0.00;
			for (int i = 0; i < getRowCount() - 1; i++) {
				String ddd = (String) getValueAt(i, column);
				Double val = Double.parseDouble(ddd);
				sum1 += val;
			}
			DecimalFormat df = new DecimalFormat("0.00");
			return Double.parseDouble(df.format(sum1));
		}
	}

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
