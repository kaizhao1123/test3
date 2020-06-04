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


public class AdditionsTable implements TableModelListener {

	JTable ntable;
	public TableModel model;
	public String newElement = null;
	ArrayList<String> newElementList = new ArrayList();
	
	String[] columnNamess;
	Object[][] dataa;
	Color cc = Color.lightGray;
	
	
	ArrayList<BeddingInfo> beddingDataset;
	BeddingInfo bed = null;

	public JTable buildMyTable(String[] s, Object[][] o, ArrayList<BeddingInfo> list) {
		
		columnNamess = s;
		dataa = o;
		beddingDataset = list;
		
		
		model = new TableModel(columnNamess, dataa);
		model.addTableModelListener(this);
		ntable = new JTable(model);

		int rowcount = ntable.getRowCount();
		int colcount = ntable.getColumnCount();
		setColorAndFont(0,rowcount-1,6,colcount,Color.cyan);
		FitTableColumns(ntable);
		TableColumn column = ntable.getColumnModel().getColumn(3);
        column.setWidth(160);
		
		ntable.setVisible(true);
	
		return ntable;
	}

	public void setColorAndFont(int row_start, int row_end, int col_start, int col_end, Color ncolor) {
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if (row >= row_start && row <= row_end && column >= col_start && column <= col_end) {
						setBackground(ncolor);
					} else if (column == 0) {					
						newElementList.add(newElement);
						if(model.data[row][0].toString().equals(newElement) 
								|| newElementList.contains(model.data[row][0].toString())) {							
							setFont(new Font(getFont().getFontName(), Font.ITALIC, 12));
						}						
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

		int col = ntable.getSelectedColumn();
		int row = ntable.getSelectedRow();
		Object[] ele = model.data[row];
		String s = ntable.getValueAt(row, col).toString();		
		
		for(int i = 0; i < beddingDataset.size(); i++) {
			if(beddingDataset.get(i).name.equals(s)) {
				bed = beddingDataset.get(i);
				ele[4] = bed.eff_Density;
			}				
		}
	
		DecimalFormat df = new DecimalFormat("0.00");
		Double aDou = Double.parseDouble(ele[5].toString());
		Double dDou = Double.parseDouble(bed.density);
		Double edDou = Double.parseDouble(ele[4].toString());
		
		ele[6] = df.format(aDou / dDou);
		ele[7] = df.format(aDou / edDou);		
		
		ntable.repaint();
	}
}

