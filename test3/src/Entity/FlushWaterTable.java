package Entity;

import java.awt.Color;
import java.awt.Component;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class FlushWaterTable implements TableModelListener {

	JTable ntable;
	public TableModelWithTotal model;

	String[] columnNamess;
	Object[][] dataa;
	Color cc = Color.cyan;

	public JTable buildMyTable(String[] s, Object[][] o) {
		
		columnNamess = s;
		dataa = o;
		
		model = new TableModelWithTotal(columnNamess, dataa);
		model.addTableModelListener(this);
		model.addTotalRow(model.getEachSum());

		ntable = new JTable(model);

		int rowcount = ntable.getRowCount();
		//int colcount = ntable.getColumnCount();
		setColor(0,rowcount-1,3,3,Color.lightGray);
		
		//FitTableColumns(ntable);
		//model.mySetValueAt("Wash Water Total", model.getRowCount()-1, 0);
		FitTableColumns(ntable);
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
						// cc = ncolor;
					} else if (column == 0 || column == 1 || column == 2) {
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

	public void FitTableColumns(JTable jt) {              

        JTableHeader header = jt.getTableHeader();

        int rowCount = jt.getRowCount();

        Enumeration columns = jt.getColumnModel().getColumns();

        while (columns.hasMoreElements()) {

            TableColumn column = (TableColumn) columns.nextElement();

            int col = header.getColumnModel().getColumnIndex(

                    column.getIdentifier());

            int width = (int) jt.getTableHeader().getDefaultRenderer()

                    .getTableCellRendererComponent(jt,

                            column.getIdentifier(), false, false, -1, col)

                    .getPreferredSize().getWidth();

            for (int row = 0; row < rowCount; row++){

                int preferedWidth = (int) jt.getCellRenderer(row, col)

                        .getTableCellRendererComponent(jt,

                                jt.getValueAt(row, col), false, false,

                                row, col).getPreferredSize().getWidth();

                width = Math.max(width, preferedWidth);

            }

            header.setResizingColumn(column);

            column.setWidth(width + jt.getIntercellSpacing().width + 10);

        }

    }
	
	
	@Override
	public void tableChanged(TableModelEvent e) {

		int row = ntable.getSelectedRow();
		String amount = model.data[row][1].toString();
		String number = model.data[row][3].toString();
		Double aDou = Double.parseDouble(amount);
		Double nDou = Double.parseDouble(number);		
		String totalPerRow = Double.toString(aDou * nDou);
		model.mySetValueAt(totalPerRow, row, 4);
		model.mySetValueAt(model.getNewSum(4), model.getRowCount() - 1, 4);
		ntable.repaint();
	}
}

