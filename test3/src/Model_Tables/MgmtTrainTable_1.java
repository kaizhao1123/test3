package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import Model_Entity.BeddingInfo;



public class MgmtTrainTable_1 implements TableModelListener {

	JTable ntable;
	public TableModel model;

	String[] columnNamess;
	Object[][] dataa;
	// the default color of the table. The cell with this color can be editable. 
	Color defaultColor = Color.lightGray;
	

	public JTable buildMyTable(String[] s, Object[][] o) {
		
		columnNamess = s;
		dataa = o;

		
		model = new TableModel(columnNamess, dataa);
		model.addTableModelListener(this);
		ntable = new JTable(model);
		ntable.getTableHeader().setReorderingAllowed(false);	//fix the header
		setCellRenderer();
		
		FitTableColumns(ntable);
		ntable.getColumnModel().getColumn(1).setWidth(230);
		ntable.getColumnModel().getColumn(2).setWidth(130);
		ntable.getColumnModel().getColumn(3).setWidth(100);
        		
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
				else 
					setBackground(defaultColor);
			return c;
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
		// int col = e.getColumn();
		int col = ntable.getSelectedColumn();
		//String colName = ntable.getColumnName(col);
		int row = ntable.getSelectedRow();
		Object[] ele = model.data[row];
		
		
		ntable.repaint();
	}
}

