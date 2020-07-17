package Model_Tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class MgmtTrainTable_1 implements TableModelListener {

	JTable ntable;
	public TableModel model;

	String[] columnNamess;
	Object[][] dataa;
	// the default color of the table. The cell with this color can be editable.
	Color defaultColor = Color.lightGray;

	String[] sepComponents = { "Decanter Centrifuge 16-30 gpm", "Screw Press", "Settling Basin",
			"Static Inclined Screen", "Static Inclined Screen 12 Mesh", "Static Inclined Screen 36 Mesh",
			"Vibrating Screen", "Vibrating Screen 16 Mesh", "Vibrating Screen 18 Mesh", "Vibrating Screen 24 Mesh",
			"Vibrating Screen 30 Mesh" };
	
	public JTable buildMyTable(String[] s, Object[][] o) {
		columnNamess = s;
		dataa = o;

		model = new TableModel(columnNamess, dataa);
		model.addTableModelListener(this);
		ntable = new JTable(model);
		ntable.getTableHeader().setReorderingAllowed(false); // fix the header
		
		ntable.setBackground(defaultColor);
		FitTableColumns(ntable);
		ntable.setVisible(true);
		return ntable;
	}
	
	
	public void updateMyCellRender(String s) {
		MyCellRender mbr = new MyCellRender(s);
		ntable.getColumnModel().getColumn(1).setCellRenderer(mbr);
		
		// set the content of first column stay at center of the cell.
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);
		ntable.setDefaultRenderer(ntable.getColumnClass(0), r);
	}

	class MyCellRender implements TableCellRenderer {

		JPanel panel;
		GridBagConstraints gbc;
		String s;
		MyCellRender(String str){
			 s = str;
		}

		private void initialPanel_1(String str) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			gbc = new GridBagConstraints();

			JLabel label_1;
			JLabel label_2;
			JLabel label_3;
			JLabel label_4;

			label_1 = new JLabel();			
			label_1.setPreferredSize(new Dimension(210,20));
			label_2 = new JLabel();
			label_2.setPreferredSize(new Dimension(210,20));
			label_3 = new JLabel();
			label_3.setPreferredSize(new Dimension(65,20));
			label_4 = new JLabel();
			label_4.setPreferredSize(new Dimension(65,20));
			label_1.setText("Solid-Liquid Separator                ");
			label_2.setText(str);
			label_3.setText("|---Liquids-->");
			label_4.setText("|---Solids-->");

			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.insets = new Insets(2, 0, 2, 0);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			panel.add(label_1, gbc);

			gbc.gridwidth = 1;
			gbc.insets = new Insets(2, 0, 2, 0);
			gbc.gridx = 2;
			panel.add(label_3, gbc);
						
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 2;
			panel.add(label_2, gbc);
			
			gbc.insets = new Insets(2, 0, 2, 0);
			gbc.gridx = 2;
			gbc.gridwidth = 1;
			panel.add(label_4, gbc);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			
			if (column == 1) {
				int rrrr = ntable.getSelectedRow();
				
				if(row == rrrr) {
					if(isContain(sepComponents, s)) {
						initialPanel_1(s);
					}
					else {
						if(value != null) {
							String cur = getName(value.toString());
							if(!isContain(sepComponents, cur )) {
								panel = new JPanel();
								panel.setLayout(new GridBagLayout());
								gbc = new GridBagConstraints();

								JLabel label = new JLabel();
								label.setText(value.toString());
								panel.add(label, gbc);
							}
							else
								initialPanel_1(cur);
						}	
					}					
				}
				else{
					if(value != null) {
						String cur = getName(value.toString());
						
						if(isContain(sepComponents, cur)) {
							initialPanel_1(cur);
						}
						else {
							panel = new JPanel();
							panel.setLayout(new GridBagLayout());
							gbc = new GridBagConstraints();

							JLabel label = new JLabel();
							label.setText(value.toString());
							panel.add(label, gbc);

						}			
					}
				}
				panel.setBackground(defaultColor);
				return panel;
			}
			return null;			
		}
	}
	
	// check whether String[] contains the target String
	private boolean isContain(String[] list, String s) {
		for(int i = 0; i < list.length; i++) {
			if(list[i].equals(s))
				return true;
		}
		return false;			
	}
	
	/**
	 *  gets the pure name, that is, the string without "#" and "number".
	 * @param s	the target string, with the format: "name" + " #" + No.
	 * @return the string, i.g.,"name".
	 */	
	private String getName(String s) {
		if (s == null)
			return " ";
		int index = s.indexOf('#');
		if (index > 0)
			return s.substring(0, index - 1);
		else
			return s;
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

			for (int row = 0; row < rowCount; row++) {

				int preferedWidth = (int) jt.getCellRenderer(row, col)

						.getTableCellRendererComponent(jt,

								jt.getValueAt(row, col), false, false,

								row, col)
						.getPreferredSize().getWidth();

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
