package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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
import Model_Tables.ClimateTable.MyCellRenderer;

/**
 * The purpose of this class is to create a JTable based on the table model.
 * At the same time, it implements TableModelListener. In this class, some
 * special properties of the JTable can be set up. Such as: the color or font of the
 * table cell, the change of the cell's content.
 * @author Kai Zhao
 *
 */
public class AdditionsTable implements TableModelListener {

	JTable ntable;
	public TableModel model;
	public String newElement = null;	// To record the new streams, the font of new element is ITALIC.
	ArrayList<String> newElementList = new ArrayList(); // To record all new streams
	
	String[] columnNamess;
	Object[][] dataa;
	// the default color of the table. The cell with this color can be editable. 
	Color defaultColor = Color.lightGray;
	// the color of the "total" row. The cell with this color can't be editable.
	Color newColor = Color.cyan;
		
	ArrayList<BeddingInfo> beddingDataset;
	BeddingInfo bed = null;

	/**
	  * To create a JTable and set up the special cellRenderer of the table. 
	 * @param s the table column name
	 * @param o the table data
	 * @return JTable with special cellRenderer
	 */
	public JTable buildMyTable(String[] s, Object[][] o, ArrayList<BeddingInfo> list) {
		
		columnNamess = s;
		dataa = o;
		beddingDataset = list;
				
		model = new TableModel(columnNamess, dataa);
		model.addTableModelListener(this);
		ntable = new JTable(model);
		ntable.getTableHeader().setReorderingAllowed(false);	//fix the header
		setCellRenderer();
		
		FitTableColumns(ntable);
		TableColumn column = ntable.getColumnModel().getColumn(3);
        column.setWidth(160);		// sets the 4th column's width
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
					newElementList.add(newElement);
					if(model.data[row][0].toString().equals(newElement) 
							|| newElementList.contains(model.data[row][0].toString())) {							
						setFont(new Font(getFont().getFontName(), Font.ITALIC, 12));
					}
					setBackground(null);							
				}				
				else if( (column == 6 ||  column == 7)) {
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

	// the data changing of one cell will lead to the change of the last two columns.
	public void tableChanged(TableModelEvent e) {

		int col = ntable.getSelectedColumn();
		if(col > 2) {
			int row = ntable.getSelectedRow();
			Object[] ele = model.data[row];
			String s = ntable.getValueAt(row, col).toString();		
			
			// for loop to get the data of the target beddingtype
			for(int i = 0; i < beddingDataset.size(); i++) {
				if(beddingDataset.get(i).name.equals(s)) {
					bed = beddingDataset.get(i);
					ele[4] = bed.eff_Density;
				}	
				else
					bed = null;
			}
		
			DecimalFormat df = new DecimalFormat("0.00");
			Double aDou = Double.parseDouble(ele[5].toString());
			Double dDou = 0.00;
			if(bed != null)
				dDou = Double.parseDouble(bed.density);
			Double edDou = Double.parseDouble(ele[4].toString());
			
			ele[6] = df.format(aDou / dDou);
			ele[7] = df.format(aDou / edDou);		
			
			ntable.repaint();
		}
		
	}
}

