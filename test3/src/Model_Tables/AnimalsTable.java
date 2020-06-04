package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

public class AnimalsTable implements TableModelListener {

	public JTable ntable;
	public TableModelWithTotal model;

	String[] columnNames;
	Object[][] data;
	Color cc = Color.CYAN;

	public JTable buildMyTable(String[] s, Object[][] o) {

		columnNames = s;
		data = o;

		model = new TableModelWithTotal(columnNames, data);		
		model.addTableModelListener(this);
		model.addTotalRow(model.getEachSum());

		ntable = new JTable(model) {
			 public String getToolTipText(MouseEvent e) {   
	                int row=ntable.rowAtPoint(e.getPoint());   
	                int col=ntable.columnAtPoint(e.getPoint());   
	                String tiptextString=null;   
	                if(row>-1 && col == 3){   
	                    double value= Double.parseDouble(ntable.getValueAt(row, col).toString()); 
	                    String type = model.data[row][1].toString();
	                    if(type.equals("Beef") && (value > 1200 || value < 150))   
	                        tiptextString = "The Range for " + type + " weight is 150-1200"; 
	                    else if(type.equals("Horse") )
	                    	tiptextString = "The Range for " + type + " weight is ?";
	                    else if(type.equals("Poultry") && (value > 25 || value < 2 ))
	                    	tiptextString = "The Range for " + type + " weight is 2-25";
	                    else if(type.equals("Dairy") && (value > 1400 || value < 150 ))
	                    	tiptextString = "The Range for " + type + " weight is 150-1400";
	                    else if(type.equals("Sheep") && (value > 500 || value < 25 ))
	                    	tiptextString = "The Range for " + type + " weight is 25-500";
	                    else if(type.equals("Swine") && (value > 600 || value < 8 ))
	                    	tiptextString = "The Range for " + type + " weight is 8-600";
	                    else if(type.equals("Veal") && (value > 200 || value < 70 ))
	                    	tiptextString = "The Range for " + type + " weight is 70-200";	                    
	                }   
	                return tiptextString; 
		}};
		int rowcount = ntable.getRowCount();	
		setColor(0,rowcount-2,2,6,Color.lightGray);

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
						if (column == 3) {
							try {
								double v= Double.parseDouble(model.data[row][3].toString()); 
			                    String type = model.data[row][1].toString();
			                    if( (type.equals("Beef") && (v > 1200 || v < 150))  
			                    		||  (type.equals("Horse")) 
			                    		||  (type.equals("Poultry") && (v > 25 || v < 2 ))
			                    		|| 	(type.equals("Dairy") && (v > 1400 || v < 150 ))
			                    		||	(type.equals("Sheep") && (v > 500 || v < 25 ))
			                    		||	(type.equals("Swine") && (v > 600 || v < 8 ))
			                    		||  (type.equals("Veal") && (v > 200 || v < 70 ))  )			                    	
			                    	setBackground(Color.red);   
							}catch(Exception ev) {
								
							}						
						}
					}													                   																 
					else if (column == 0 || column == 1) 
						setBackground(null);					
					else
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

	@Override
	public void tableChanged(TableModelEvent e) {

		int col = e.getColumn();
		String colname = model.getColumnName(col);

		if (colname.equals("<html>Weight <br> (lbs) </html>")
				|| colname.equals("<html>Manure <br> (cu.ft/day/AU) </html>")
				|| colname.equals("<html> VS <br> (lbs/day/AU) </html>")
				|| colname.equals("<html> TS <br> (lbs/day/AU) </html>"))
			model.mySetValueAt("N/A", model.getRowCount() - 1, col);

		else
			model.mySetValueAt(model.getNewSum(col), model.getRowCount() - 1, col);

		int row = ntable.getSelectedRow();
		Object[] ele = model.data[row];
		DecimalFormat df = new DecimalFormat("0.00");
		Double qDou = Double.parseDouble(ele[2].toString());
		Double wDou = Double.parseDouble(ele[3].toString());
		Double mDou = Double.parseDouble(ele[4].toString());
		Double tsDou = Double.parseDouble(ele[6].toString());
		Double vsDou = Double.parseDouble(ele[5].toString());

		ele[7] = df.format(mDou * qDou * wDou / 1000);
		ele[8] = df.format(vsDou * qDou * wDou / 1000);
		ele[9] = df.format(tsDou * qDou * wDou / 1000);
		ele[10] = df.format(mDou * qDou * wDou * 60 / 1000);

		for (int i = 0; i < 4; i++) {
			model.mySetValueAt(model.getNewSum(7 + i), model.getRowCount() - 1, 7 + i);
		}

		ntable.repaint();
	}
}
