package Tables;
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


public class RunoffTable implements TableModelListener{
	
	public JTable ntable;
	public TableModelWithTotal model;
	
	String[] columnName;
	Object[][] data;
	Color cc = Color.cyan;

	public JTable buildMyTable(String[] s, Object[][] o) {

		columnName = s;
		data = o;
		
		model = new TableModelWithTotal(columnName,data);				
	    model.addTableModelListener(this);
	    model.addTotalRow(model.getEachSum());				
		ntable = new JTable(model);
	
		setColor(0,11,1,2,Color.lightGray);	
		FitTableColumns(ntable);
		ntable.setVisible(true);
		return ntable;
	}

	
	public void setColor(int row_start, int row_end, int col_start, int col_end, Color ncolor){
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(){				
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus,int row,int column){
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
					if(row >= row_start && row <= row_end && column >= col_start && column <= col_end){
						setBackground(ncolor);
					}
					else if(column == 0) {
						setBackground(null);
					}
					else 
						setBackground(cc);

					
					return c;
				}
			};	
			
			for(int i = 0; i < col_end ; i++) {
				ntable.setDefaultRenderer(ntable.getColumnClass(i), tcr);				
			}	

		}catch(Exception ex){
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
