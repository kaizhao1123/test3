package Model_Tables;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import Model_Tables.ClimateTable.MyCellRenderer;

/**
 * The purpose of this class is to create a JTable based on the table model.
 * At the same time, it implements TableModelListener. In this class, some
 * special properties of the JTable can be set up. Such as: the color or font of the
 * table cell, the change of the cell's content.
 * @author Kai Zhao
 *
 */
public class RunoffTable implements TableModelListener{
	
	public JTable ntable;
	public TableModelWithTotal model;
	
	String[] columnName;
	Object[][] data;
	// the default color of the table. The cell with this color can be editable. 
	Color defaultColor = Color.lightGray;
	// the color of the "total" row. The cell with this color can't be editable.
	Color newColor = Color.cyan;
	
	/**
	 * To create a JTable with fixed row count and column count 
	 * set up the color of the table
	 * @param s the table column name
	 * @param o the table data
	 * @return the JTable 
	 */
	public JTable buildMyTable(String[] s, Object[][] o) {

		columnName = s;
		data = o;
		
		model = new TableModelWithTotal(columnName,data);				
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
				else if( (column == 1 ||  column == 2) && row < 12) {
					setBackground(defaultColor);										
				} 						
				else 
					setBackground(newColor);
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
	//  the data changing of one cell will lead to the change of the last column and last row.
    public void tableChanged(TableModelEvent e) { 
		int col = ntable.getSelectedColumn();
		int row = ntable.getSelectedRow();

		String pervious = model.data[row][1].toString();
		String impervious = model.data[row][2].toString();
		Double pDou = Double.parseDouble(pervious);
		Double iDou = Double.parseDouble(impervious);
		String totalPerRow = Double.toString(pDou +  iDou);
		
		model.mySetValueAt(totalPerRow, row, 3);		// total of row
		model.mySetValueAt(model.getNewSum(col), 12, col);	// total of column
		model.mySetValueAt(model.getNewSum(3), 12, 3);	// total of total
        ntable.repaint();
    }
}
