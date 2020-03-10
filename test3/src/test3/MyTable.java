package test3;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;



public class MyTable implements TableModelListener{

	
	//implements TableModelListener
	
	
	JTable ntable;
	TableModel model;
	
	String[] columnNamess;
	Object[][] dataa;
	Color cc = Color.lightGray;
	
	
	
	
	public JTable buildMyTable(String[] s, Object[][] o) {

		columnNamess = s;
		dataa = o;
		
		model = new TableModel();
		model.buildModel(columnNamess,dataa);
		
	    model.addTableModelListener(this);
	    model.addRow(model.getEachSum());
				
		ntable = new JTable(model);
				
		int rowcount = ntable.getRowCount();
		int colcount = ntable.getColumnCount();
		//setColor(0,rowcount-3,1,2, Color.yellow);
		
		setColor(rowcount-1,rowcount-1,1,colcount,Color.cyan);	
			
		//model.getColumnClass(0);
		
		ntable.setVisible(true);
		//ntable.setSize(100,100);
		return ntable;
	}

	
	private void setColor(int row_start, int row_end, int col_start, int col_end, Color ncolor){
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(){				
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus,int row,int column){
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if(row == row_start && column == col_start) {
						c.setBackground(ncolor);
					}
					
					
					if(row >= row_start && row <= row_end && column >= col_start && column <= col_end){
						setBackground(ncolor);
						//cc = ncolor;
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
	


	@Override
    public void tableChanged(TableModelEvent e) {
 		
        int col = e.getColumn();      
        model.mySetValueAt(model.getNewSum(col), model.getRowCount()-1, col);       
        ntable.repaint();
    }
}
