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


public class MgmtTrainTable_3 implements TableModelListener {

	JTable ntable;
	public TableModel model;

	String[] columnNamess;
	Object[][] dataa;
	// the default color of the table. The cell with this color can be editable.
	Color defaultColor = Color.cyan;
	MyCellRenderer tcr;

	public JTable buildMyTable(String[] s, Object[][] o) {
		
		columnNamess = s;
		dataa = o;
		
		model = new TableModel(columnNamess, dataa);
		model.addTableModelListener(this);
		ntable = new JTable(model);

		setCellRenderer();
		ntable.setBackground(defaultColor);
		
		FitTableColumns(ntable);       
		ntable.setVisible(true);
		
		return ntable;
	}

	/**
	 * Sets up cellRenderers of all cells following the column.
	 */
	public void setCellRenderer() {
		tcr = new MyCellRenderer();		
		for (int i = 0; i < ntable.getColumnCount(); i++) {
			ntable.getColumnModel().getColumn(i).setCellRenderer(tcr);
		}		
	}

	/**
	 * Define special cellRenderer based on the requirement of the table. 
	 * it only set the content of the cell stay in the center, and set font.
	 * @author Kai Zhao
	 *
	 */
	class MyCellRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if(column == 0) {
				super.setHorizontalAlignment(LEFT);
			}
			else {
				super.setHorizontalAlignment(RIGHT);				
			}
			c.setFont(new Font(c.getFont().getName(), Font.BOLD, 12));
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
		//int col = ntable.getSelectedColumn();
		//String colName = ntable.getColumnName(col);

		
		//ntable.repaint();
	}
}
