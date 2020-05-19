package Entity;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

public class AnimalsTable implements TableModelListener {

	public JTable ntable;
	public TableModelWithTotal model;

	String[] columnNames;
	Object[][] data;
	Color cc = Color.CYAN;

	public JTable buildMyTable(String[] s, Object[][] o) {

		columnNames = s;
		data = o;

		model = new TableModelWithTotal(columnNames, data);		
		model.addTableModelListener(this);
		model.addTotalRow(model.getEachSum());

		ntable = new JTable(model);
		int rowcount = ntable.getRowCount();
		int colcount = ntable.getColumnCount();
		setColor(0,rowcount-2,2,6,Color.lightGray);

		ntable.setVisible(true);		
		return ntable;
	}

	public void setColor(int row_start, int row_end, int col_start, int col_end, Color ncolor) {
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

					if (row >= row_start && row <= row_end && column >= col_start && column <= col_end) {
						setBackground(ncolor);
					} else if (column == 0 || column == 1) {
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
	public void tableChanged(TableModelEvent e) {

		int col = e.getColumn();
		String colname = model.getColumnName(col);

		if (colname.equals("<html>Weight <br> (lbs) </html>")
				|| colname.equals("<html>Manure <br> (cu.ft/day/AU) </html>")
				|| colname.equals("<html> VS <br> (lbs/day/AU) </html>")
				|| colname.equals("<html> TS <br> (lbs/day/AU) </html>"))
			model.mySetValueAt("N/A", model.getRowCount() - 1, col);

		else
			model.mySetValueAt(model.getNewSum(col), model.getRowCount() - 1, col);

		int row = ntable.getSelectedRow();
		Object[] ele = model.data[row];
		DecimalFormat df = new DecimalFormat("0.00");
		double qDou = Double.parseDouble(ele[2].toString());
		double wDou = Double.parseDouble(ele[3].toString());
		double mDou = Double.parseDouble(ele[4].toString());
		double tsDou = Double.parseDouble(ele[6].toString());
		double vsDou = Double.parseDouble(ele[5].toString());

		ele[7] = df.format(mDou * qDou * wDou / 1000);
		ele[8] = df.format(vsDou * qDou * wDou / 1000);
		ele[9] = df.format(tsDou * qDou * wDou / 1000);
		ele[10] = df.format(mDou * qDou * wDou * 60 / 1000);

		for (int i = 0; i < 4; i++) {
			model.mySetValueAt(model.getNewSum(7 + i), model.getRowCount() - 1, 7 + i);
		}

		ntable.repaint();
	}
}
