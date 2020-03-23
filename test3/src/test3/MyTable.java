package test3;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;


public class MyTable implements TableModelListener{
	
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
		
		//int rowcount = ntable.getRowCount();
		//int colcount = ntable.getColumnCount();		
		//setColor(rowcount-1,rowcount-1,1,colcount,Color.cyan);			
		ntable.setVisible(true);;
		return ntable;
	}

	
	public void setColor(int row_start, int row_end, int col_start, int col_end, Color ncolor){
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
        //int col = e.getColumn();  		
        int col = ntable.getSelectedColumn();        
        String colName = ntable.getColumnName(col);

        if(colName != "<html>Quantity </html>" && colName != "<html>Weight <br> (lbs) </html>")
        	model.mySetValueAt(model.getNewSum(col), model.getRowCount()-1, col); 
        else {
        	int raw = ntable.getSelectedRow();
        	Object[][] data = model.p;
        	
        	String quantity = data[raw][2].toString();
			String weight = data[raw][3].toString();
			String m = data[raw][4].toString();
			String ts = data[raw][6].toString();
			String vs = data[raw][5].toString();
			double qDou = Double.parseDouble(quantity);
			double wDou = Double.parseDouble(weight);
			double mDou = Double.parseDouble(m);
			double tsDou = Double.parseDouble(ts);
			double vsDou = Double.parseDouble(vs);

			String mValue = Double.toString(mDou * qDou * wDou / 1000);                     
			String vsValue = Double.toString(vsDou * qDou * wDou / 1000);
			String tsValue = Double.toString(tsDou * qDou * wDou / 1000);
			String mmValue = Double.toString(mDou * qDou * wDou * 60 / 1000);
        	
			model.mySetValueAt(model.getNewSum(2), model.getRowCount()-1, 2); 
			model.mySetValueAt(model.getNewSum(3), model.getRowCount()-1, 3); 
			model.mySetValueAt(mValue, raw, 7);
			model.mySetValueAt(model.getNewSum(7), model.getRowCount()-1, 7); 
			model.mySetValueAt(vsValue, raw, 8); 
			model.mySetValueAt(model.getNewSum(8), model.getRowCount()-1, 8); 
			model.mySetValueAt(tsValue, raw, 9); 
			model.mySetValueAt(model.getNewSum(9), model.getRowCount()-1, 9); 
			model.mySetValueAt(mmValue, raw, 10); 
			model.mySetValueAt(model.getNewSum(10), model.getRowCount()-1, 10); 
			
			
        }
        
              
        ntable.repaint();
    }
}
