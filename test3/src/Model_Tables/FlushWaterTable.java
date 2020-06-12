package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import Model_Tables.AdditionsTable.MyCellRenderer;

/**
 * The purpose of this class is to create a JTable based on the table model.
 * At the same time, it implements TableModelListener. In this class, some
 * special properties of the JTable can be set up. Such as: the color or font of the
 * table cell, the change of the cell's content.
 * @author Kai Zhao
 *
 */
public class FlushWaterTable implements TableModelListener {

	JTable ntable;
	public TableModelWithTotal model;

	String[] columnNamess;
	Object[][] dataa;
	// the default color of the table. The cell with this color can be editable. 
	Color defaultColor = Color.lightGray;
	// the color of the "total" row. The cell with this color can't be editable.
	Color newColor = Color.cyan;

	/**
	 * To create a JTable with fixed column count.
	 * set up the color of the table
	 * @param s the table column name
	 * @param o the table data
	 * @return
	 */
	public JTable buildMyTable(String[] s, Object[][] o) {
		
		columnNamess = s;
		dataa = o;
		
		model = new TableModelWithTotal(columnNamess, dataa);
		model.addTableModelListener(this);
		model.addTotalRow(model.getEachSum());
		ntable = new JTable(model);
		ntable.getTableHeader().setReorderingAllowed(false);	//fix the header
		setCellRenderer();
		FitTableColumns(ntable);
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
				
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column == 0 ) {					
					setBackground(null);							
				}				
				else if(column == 4) {
					setBackground(newColor);										
				} 						
				else 
					setBackground(defaultColor);
			return c;
		}
	}
	

	/**
	 * set the column's width is the same as the content of the cell, 
	 * rather than the fixed same as each other.
	 * @param jt the table needs to be resize the column's width
	 */
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
	//the data changing of one cell will lead to the change of the last row
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

