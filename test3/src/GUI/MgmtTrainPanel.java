package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import Controller.PanelManager;
import Model_Tables.MgmtTrainTable_1;
import Model_Tables.MgmtTrainTable_2;
import Model_Tables.MgmtTrainTable_3;

public class MgmtTrainPanel extends JPanel {
	
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;

	String[] columnName_1 = { "Waste Stream", "Step 1" }; // jTable1's column names
	String[] columnName_2 = { "Step 2", "Step 3" }; // jTable2's column names
	String[] columnName_3 = { "Component Name", "Manure", "Wash Water", "Flush Water", "Bedding",
			"Total Waste Volume" }; // jtable3's column

	String[] firstColumn = { "a", "b", "c", "Runoff" }; // jTable1's first column's content

	Object[][] tableData_1;
	Object[][] tableData_2;
	Object[][] tableData_3 = { { "", "", "", "", "", "", "" } };

	MgmtTrainTable_1 myTable1;
	JTable jTable1;
	MgmtTrainTable_2 myTable2;
	JTable jTable2;
	MgmtTrainTable_3 myTable3;
	JTable jTable3;

	JScrollPane subScrollPane_1; // contains jTable1.
	JScrollPane subScrollPane_2; // contains jTable2.
	JPanel panel_1; // contains subScrollPanel_1 and subScrollPanel_2
	JPanel panel_2; // contains "help" and "ok" buttons.
	JScrollPane scrollPane_1; // contains panel_1.
	JScrollPane scrollPane_2; // contains jtable3.

	JLabel label;
	JButton buttonHelp;
	JButton buttonOK;

	JMenu separatorMenu; // JMenu with all separator components
	JPopupMenu popupMenu_full; // popupMenu with all components.
	JPopupMenu popupMenu_nosep; // popupMenu with all components except the separatorMenu.
	JPopupMenu popupMenu_term; // popupMenu with some components and new terminal items.
	JPopupMenu popupMenu_liqu; // popupMenu with some components and new liquid items.
	JPopupMenu popupMenu_runoff; // popupMenu with some components, used for "runoff"

	String[] components = { "Storage Pond", "Storage Tank", "Dry Stack(Uncovered)", "Dry Stack(covered)",
			"Anaerobic Lagoon", "Anaerobic Lagoon(Ext.)" };
	String[] sepComponents = { "Decanter Centrifuge 16-30 gpm", "Screw Press", "Settling Basin",
			"Static Inclined Screen", "Static Inclined Screen 12 Mesh", "Static Inclined Screen 36 Mesh",
			"Vibrating Screen", "Vibrating Screen 16 Mesh", "Vibrating Screen 18 Mesh", "Vibrating Screen 24 Mesh",
			"Vibrating Screen 30 Mesh" };

	// records the storage component and its number, such as: <Storage Tank, 1>.
	HashMap<String, Integer> map = new HashMap<>();
	/*
	 * records the storage component with its number and its count, 
	 * e.g., <Storage Tank #1, 1>.
	 */ 
	HashMap<String, Integer> countMap = new HashMap<>();
	/*
	 * records the index of table2 corresponding index of table1,  used for recording 
	 * when merge and split row. e.g., <index of table2, index of table1>.
	 */
	HashMap<Integer, Integer> indexMap = new HashMap<>();

	/*
	 * stores the terminal storage components, used for building jTable3 and update
	 * popupMenus. the format of the string is: components name + " #" + NO.
	 */
	ArrayList<String> resultComponents;

	// stores the liquid storage components in the resultComponents: tank and pond
	ArrayList<String> liquidComponents;
	/*
	 *  stores the terminal storage components in the resultComponents: tank, pond,
	 *  covered dry stack, and anaerobic lagoon.
	 */
	ArrayList<String> terminalComponents;

	// the gridBagConstraints of the main JPanel panel;
	GridBagConstraints gc;
	// the gridBagConstraints of the sub JPanel panel_1;
	GridBagConstraints subgc;

	public MgmtTrainPanel(PanelManager pm) {

		panelManager = pm;
		initialData();
		initialElements();
		initialActionListeners();
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		initialLayout(gc);
	}

	private void initialData() {
		// initial jTable1
		tableData_1 = new Object[firstColumn.length][2];
		for (int i = 0; i < firstColumn.length; i++) {
			tableData_1[i][0] = firstColumn[i];
			tableData_1[i][1] = " ";
			indexMap.put(i, i); // initial indexMap by the way.
		}

		// initial jTable2
		tableData_2 = new Object[firstColumn.length][2];
		for (int i = 0; i < firstColumn.length; i++) {
			tableData_2[i][0] = " ";
			tableData_2[i][1] = " ";
		}

		resultComponents = new ArrayList<>();
		liquidComponents = new ArrayList<>();
		terminalComponents = new ArrayList<>();
	}

	/**
	 * initial elements in this panel. 
	 * there are two JscrollPanes to show tables, the first scrollPane shows a 
	 * child panel, which includes two child Jscrollpanes, show jTable1 and jTable2
	 * respectively. The 2nd scrollPane shows jTable3.
	 * in addition, initial all popupMenus.
	 */
	private void initialElements() {
		label = new JLabel("Component Volumens(cu.ft/day)");
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);

		// initial scrollPane_1
		myTable1 = new MgmtTrainTable_1();
		jTable1 = myTable1.buildMyTable(columnName_1, tableData_1);
		jTable1.getColumnModel().getColumn(0).setWidth(90);
		jTable1.getColumnModel().getColumn(1).setWidth(260);
		jTable1.setRowHeight(50);
		jTable1.setRowSelectionAllowed(false);
		myTable1.updateMyCellRender(" ");
		subScrollPane_1 = new JScrollPane(jTable1);
		subScrollPane_1.setPreferredSize(new Dimension(350, 220));
		subScrollPane_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		subScrollPane_1.setViewportBorder(border);
		subScrollPane_1.setBorder(border);

		myTable2 = new MgmtTrainTable_2();
		jTable2 = myTable2.buildMyTable(columnName_2, tableData_2);
		jTable2.setRowHeight(50);
		jTable2.getColumnModel().getColumn(0).setWidth(160);
		jTable2.getColumnModel().getColumn(1).setWidth(115);
		jTable2.setRowSelectionAllowed(false);
		subScrollPane_2 = new JScrollPane(jTable2);
		subScrollPane_2.setPreferredSize(new Dimension(275, 220));
		subScrollPane_2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		subScrollPane_2.setViewportBorder(border);
		subScrollPane_2.setBorder(border);

		panel_1 = new JPanel();
		panel_1.setLayout(new GridBagLayout());
		subgc = new GridBagConstraints();

		subgc.insets = new Insets(0, 0, 0, 0);
		subgc.gridx = 0;
		subgc.gridy = 0;
		panel_1.add(subScrollPane_1, subgc);

		subgc.insets = new Insets(0, 0, 0, 0);
		subgc.gridx = 1;
		panel_1.add(subScrollPane_2, subgc);

		panel_1.setVisible(true);
		scrollPane_1 = new JScrollPane(panel_1);
		scrollPane_1.setPreferredSize(new Dimension(630, 223));
		//scrollPane_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setViewportBorder(border);
		scrollPane_1.setBorder(border);

		// initial scrollPane_2
		myTable3 = new MgmtTrainTable_3();
		jTable3 = myTable3.buildMyTable(columnName_3, tableData_3);
		jTable3.enable(false);
		jTable3.setRowHeight(25);

		scrollPane_2 = new JScrollPane(jTable3);
		scrollPane_2.setPreferredSize(new Dimension(630, 100));
		scrollPane_2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_2.setViewportBorder(border);
		scrollPane_2.setBorder(border);

		// initial panel_2
		panel_2 = new JPanel();
		panel_2.setLayout(new FlowLayout());
		buttonHelp = new JButton("Help");
		buttonHelp.setPreferredSize(new Dimension(60, 25));
		buttonOK = new JButton("OK");
		buttonOK.setPreferredSize(new Dimension(60, 25));
		panel_2.add(buttonHelp);
		panel_2.add(buttonOK);

		// create and initial all popupMenus
		popupMenu_full = new JPopupMenu();
		popupMenu_nosep = new JPopupMenu();
		popupMenu_term = new JPopupMenu();
		popupMenu_liqu = new JPopupMenu();
		popupMenu_runoff = new JPopupMenu();
		separatorMenu = new JMenu("Solid-Liquid Separator");
		initialPopupMenu();
	}
	
	// initial listeners
	private void initialActionListeners() {
		jTable1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int col = jTable1.getSelectedColumn();
				int row = jTable1.getSelectedRow();
				if (col == 1) {
					if (myTable1.model.data[row][0].toString().equals("Runoff")) {
						popupMenu_runoff.show(jTable1, e.getX(), e.getY());
					} else
						popupMenu_full.show(jTable1, e.getX(), e.getY());
					myTable1.updateMyCellRender(tableData_1[row][col].toString());
				}
				jTable1.repaint();				
			}
		});

		jTable2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int col = jTable2.getSelectedColumn();
				int row = jTable2.getSelectedRow();
				String valueInTable1 = getName(myTable1.model.data[row][1].toString());
				String valueInTable2 = getName(myTable2.model.data[row][0].toString());

				if (col == 0) {
					if (!valueInTable1.equals(" ") && !isTerminalComponent(valueInTable1)) {
						if (valueInTable1.equals("Anaerobic Lagoon(Ext.)")) {
							popupMenu_liqu.show(jTable2, e.getX(), e.getY());
						} else if (valueInTable1.equals("Dry Stack(Uncovered)")) {
							popupMenu_term.show(jTable2, e.getX(), e.getY());
						}
						else {
							popupMenu_nosep.show(jTable2, e.getX(), e.getY());
						}
					}
				}

				if (col == 1 && !valueInTable2.equals(" ") && !isTerminalComponent(valueInTable2))
					popupMenu_term.show(jTable2, e.getX(), e.getY());
				jTable2.repaint();
			}
		});

	}

	// initial layout
	private void initialLayout(GridBagConstraints gbc) {

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(2, 5, 2, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		add(scrollPane_1, gbc);

		gbc.gridy = 2;
		add(label, gbc);

		gbc.gridy = 3;
		add(scrollPane_2, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridheight = 1;
		gbc.gridy = 4;
		add(panel_2, gbc);
	}

	/**
	 * creates menuItem with name "None(Clear)" in the target jTable.
	 * @param jt the target jTable
	 * @return
	 */
	private JMenuItem createNullItem(JTable jt) {
		JMenuItem nullItem = new JMenuItem();
		nullItem.setText("None(Clear)");
		nullItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				// jt == jtable1
				if (jt == jTable1) {
					int row = jTable1.getSelectedRow();
					if (!myTable1.model.data[row][1].toString().equals(" ")) {
						String cur = myTable1.model.data[row][1].toString();

						if (isContain(sepComponents, getName(cur))) {

							int rowInTable2 = firstKey(indexMap, row);
							int extraRowInTable2 = rowInTable2 + 1;
							removeTwoRowsItems(rowInTable2);
							removeTableRow(extraRowInTable2);
						} else if (!isContain(sepComponents, getName(cur))) {
							int rowInTable2 = firstKey(indexMap,row);
							removeOneRowItems(rowInTable2);
							removeOneItem(cur);
						}
					}
					myTable1.updateMyCellRender(" ");
					myTable1.model.mySetValueAt(" ", row, 1);
					jTable1.repaint();
				}
				// jt == jtable2
				else if (jt == jTable2) {
					int row = jTable2.getSelectedRow();
					int col = jTable2.getSelectedColumn();
					if (!myTable2.model.data[row][col].toString().equals(" ")) {
						if(col == 0)						
							removeOneRowItems(row);
						else {
							String cur = myTable2.model.data[row][col].toString();
							removeOneItem(cur);
							myTable2.model.mySetValueAt(" ", row, col);
						}
					}
					jTable1.clearSelection();
					myTable1.updateMyCellRender(" ");					
					jTable2.repaint();
				}

			}
		});
		return nullItem;
	}

	/**
	 * creates special items after the separator line of the popupMenu, under this
	 * format: name + " #" + NO. 
	 * @param n  new value of the cell, under this format: name + " #" + NO.
	 * @param jt the target table
	 * @return
	 */
	private JMenuItem createSpecialItem(String n, JTable jt) {
		JMenuItem item = new JMenuItem();
		item.setText(n);
		item.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				// jt == jtable1
				if (jt == jTable1) {
					int row = jTable1.getSelectedRow();
					int col = jTable1.getSelectedColumn();
					String cur = myTable1.model.data[row][col].toString();

					if (!cur.equals(n)) {
						countMap.put(n, countMap.get(n) + 1);
						String c1 = getName(cur);
						String n1 = getName(n);
						if (cur != " " && (c1.equals(n1))) {
							exchangeNewItems(cur, n);
						} else if (cur != " " && !(c1.equals(n1))) {
							removeOneItem(cur);
						}

					} else {
						System.out.print("==");
					}
					myTable1.model.mySetValueAt(n, row, col);
					jTable1.repaint();
				}

				// jt == jtable2
				else {
					int row = jTable2.getSelectedRow();
					int col = jTable2.getSelectedColumn();
					String cur = myTable2.model.data[row][col].toString();

					if (!cur.equals(n)) {
						countMap.put(n, countMap.get(n) + 1);
						String c1 = getName(cur);
						String n1 = getName(n);
						if (cur != " " && (c1.equals(n1))) {
							exchangeNewItems(cur, n);
						} else if (cur != " " && !(c1.equals(n1))) {
							removeOneItem(cur);
						}
					} else {
						System.out.print("==");
					}
					myTable2.model.mySetValueAt(n, row, col);
					jTable2.repaint();
				}
			}
		});
		return item;
	}

	/**
	 * creates normal item when initializing the popupMenu.
	 * @param s the source of the item name.
	 * @return
	 */
	private JMenuItem createNormalItem(String s, JTable jt) {
		JMenuItem item = new JMenuItem();
		item.setText("New " + s);
		item.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				// jt == jtable1
				if (jt == jTable1) {
					int row = jTable1.getSelectedRow();
					int col = jTable1.getSelectedColumn();
					String cur = myTable1.model.data[row][1].toString();
					String value;
					
					/*
					 * At the beginning, each cell is " ", that is, before we choose the menuItem,
					 * the current value is " ", what to do is add items into the two maps, and add
					 * separator line and the new item into the popupMenu, in addition, store into
					 * the result components list.
					 */

					if (cur.equals(" ")) {

						// add into the 1st map
						if (!map.containsKey(s))
							map.put(s, 1);
						else
							map.put(s, map.get(s) + 1);

						// add into the 2nd map, that is, the count map.
						value = s + " #" + map.get(s).toString();
						if (!countMap.containsKey(value))
							countMap.put(value, 1);
						else
							countMap.put(value, countMap.get(value) + 1);

						if (isContain(sepComponents, s)) {
							myTable1.updateMyCellRender(s);
							myTable1.model.mySetValueAt(s, row, col);
							myTable2.model.mySetValueAt(" ", row, col);
							insertTableRow(firstKey(indexMap, row));
						} else if (!isContain(sepComponents, s)) {
							// add separator line
							if (resultComponents.size() == 0) {
								popupMenu_full.addSeparator();
								popupMenu_nosep.addSeparator();
								popupMenu_term.addSeparator();
								popupMenu_liqu.addSeparator();
								popupMenu_runoff.addSeparator();
							}
							// add into resultComponents and update other components.
							resultComponents.add(value);
							updateLiquidComponents();
							updateTerminalComponents();
							// update all popupMenus
							updatePopupMenus("add", value);
							// update cellRender of jTable1 and value of this cell.
							myTable1.updateMyCellRender(value);
							myTable1.model.mySetValueAt(value, row, col);
						}
					}

					/*
					 * Already have chosen one item, but need to change to another one. that is, the
					 * current value of the cell is not null. 
					 */
					else {
						/*
						 *  check whether the cur item is in sepComponents or not. if yes, need
						 *  to remove a row from table2; if no, check whether both cur and target items
						 *  belong to the same category or not, if no, remove the cur item from jTable1,
						 *  otherwise, do nothing.
						 */						
						if (isContain(sepComponents, getName(cur))) {
							int rowInTable2 = firstKey(indexMap, row);
							int extraRowInTable2 = rowInTable2 + 1;
							removeTwoRowsItems(rowInTable2);					
							removeTableRow(extraRowInTable2);
						} else if (!isContain(sepComponents, getName(cur))) {
							String c1 = getName(cur);
							String s1 = getName(s);
							if (!c1.equals(s1)) {
								int rowInTable2 = firstKey(indexMap,row);
								removeOneRowItems(rowInTable2);
								removeOneItem(cur);							
							}								
						}

						/*
						 *  check whether the target item is in sepComponents or not. if yes, need
						 *  to insert a row into table2; if no, check whether cur item is equal to the
						 *  target item or not, if no, add into 1st map and change value, otherwise, 
						 *  do nothing.
						 */						
						
						if (isContain(sepComponents, s)) {
							myTable1.updateMyCellRender(s);
							myTable1.model.mySetValueAt(s, row, col);
							myTable2.model.mySetValueAt(" ", row, col);
							insertTableRow(firstKey(indexMap, row));
						} else if (!isContain(sepComponents, s)) {
							String c1 = getName(cur);
							String s1 = getName(s);
							if(!c1.equals(s1)) {
								if (!map.containsKey(s))
									map.put(s, 1);
								else
									map.put(s, map.get(s) + 1);
								value = s + " #" + map.get(s).toString();
								addOneItem(value);
								myTable1.updateMyCellRender(value);
								myTable1.model.mySetValueAt(value, row, col);
							}														
						}
					}
				}

				// jt == jTable2, which is similar with jTable1, without checking separatorMenu items.
				else if (jt == jTable2) {
					int row = jTable2.getSelectedRow();
					int col = jTable2.getSelectedColumn();
					// remove old item
					if (!myTable2.model.data[row][col].toString().equals(" ")) {
						if(col == 0)
							removeOneRowItems(row);
						else {
							String cur = myTable2.model.data[row][col].toString();
							removeOneItem(cur);
							myTable2.model.mySetValueAt(" ", row, col);
						}					
					}
					// add separator line
					if (resultComponents.size() == 0) {
						popupMenu_full.addSeparator();
						popupMenu_nosep.addSeparator();
						popupMenu_term.addSeparator();
						popupMenu_liqu.addSeparator();
						popupMenu_runoff.addSeparator();
					}

					// add the target item into the 1st map
					if (!map.containsKey(s))
						map.put(s, 1);
					else
						map.put(s, map.get(s) + 1);
					String value = s + " #" + map.get(s).toString();
					addOneItem(value);
					myTable2.model.mySetValueAt(value, row, col);
				}
			}
		});
		return item;

	}

	/**
	 * Inserts a new row into the target index. It is used for the separator menu.
	 * After insert the row, rebuild the jTable and add listener into the new
	 * jTable.
	 * 
	 * @param name
	 * @param s
	 * @param rowIndex
	 */
	public void insertTableRow(int rowIndex) {

		// update indexMap
		for (int i = jTable2.getRowCount(); i > rowIndex; i--) {
			indexMap.put(i, indexMap.get(i - 1));
		}

		// gets the row data, which will be inserted into jtable2.
		int col = myTable2.model.getColumnCount();
		String[] rowData = new String[col];
		for (int i = 0; i < col; i++) {
			rowData[i] = " ";
		}

		// insert row data into the table2 model.
		myTable2.model.insertRow(rowData, rowIndex);

		updateTable2();
		subScrollPane_2.setViewportView(jTable2);
		subScrollPane_1.setViewportView(jTable1);
		scrollPane_1.setViewportView(panel_1);
	}

	// remove the target row of the jTable2.
	public void removeTableRow(int rowIndex) {
		// update indexMap
		for (int i = rowIndex; i < jTable2.getRowCount() - 1; i++) {
			indexMap.put(i, indexMap.get(i + 1));
		}
		indexMap.remove(jTable2.getRowCount() - 1);

		// update jTable2
		myTable2.model.deleteRow(rowIndex);
		updateTable2();
		subScrollPane_2.setViewportView(jTable2);
		subScrollPane_1.setViewportView(jTable1);
		scrollPane_1.setViewportView(panel_1);
	}

	// update jTable2 through creating a new table with listener.
	private void updateTable2() {

		// rebuild the jTable2
		tableData_2 = myTable2.model.data;
		jTable2 = myTable2.buildMyTable(columnName_2, tableData_2);

		// set each row height of jTable2
		for (int i = 0; i < jTable2.getRowCount(); i++) {
			if (indexMap.get(i) == indexMap.get(i + 1)) {
				jTable2.setRowHeight(i, 25);
				jTable2.setRowHeight(i + 1, 25);
				i++;
			} else
				jTable2.setRowHeight(i, 50);
		}
		// set each column width of jTable2
		jTable2.getColumnModel().getColumn(0).setWidth(160);
		jTable2.getColumnModel().getColumn(1).setWidth(100);
		jTable2.setRowSelectionAllowed(false);

		// add listener
		jTable2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int col = jTable2.getSelectedColumn();
				int row = jTable2.getSelectedRow();

				int rowAtTable1 = indexMap.get(row);
				String valueInTable1 = getName(myTable1.model.data[rowAtTable1][1].toString());
				String valueInTable2 = getName(myTable2.model.data[row][0].toString());

				// update different popupMenus according to the row data in jTable1, through creating new ones.
				if (col == 0) {
					if (!valueInTable1.equals(" ") && !isTerminalComponent(valueInTable1)) {
						if (valueInTable1.equals("Anaerobic Lagoon(Ext.)")) {
							popupMenu_liqu = new JPopupMenu();
							JMenuItem nullItem = createNullItem(jTable2);
							popupMenu_liqu.add(nullItem);
							popupMenu_liqu.add(createNormalItem("Storage Pond", jTable2));
							popupMenu_liqu.add(createNormalItem("Storage Tank", jTable2));
							if (resultComponents.size() != 0)
								popupMenu_liqu.addSeparator();
							for (int i = 0; i < liquidComponents.size(); i++) {
								popupMenu_liqu.add(createSpecialItem(liquidComponents.get(i), jTable2));
							}
							popupMenu_liqu.show(jTable2, e.getX(), e.getY());

						} else if (valueInTable1.equals("Dry Stack(Uncovered)")) {
							popupMenu_term = new JPopupMenu();
							JMenuItem nullItem = createNullItem(jTable2);
							popupMenu_term.add(nullItem);
							popupMenu_term.add(createNormalItem("Storage Pond", jTable2));
							popupMenu_term.add(createNormalItem("Storage Tank", jTable2));
							popupMenu_term.add(createNormalItem("Anaerobic Lagoon", jTable2));
							if (resultComponents.size() != 0)
								popupMenu_term.addSeparator();
							for (int i = 0; i < terminalComponents.size(); i++) {
								popupMenu_term.add(createSpecialItem(terminalComponents.get(i), jTable2));
							}
							popupMenu_term.show(jTable2, e.getX(), e.getY());

						}
						// if the value in jTable1 belongs to the sepComponents.
						else {
							
							popupMenu_nosep = new JPopupMenu();
							JMenuItem nullItem2 = createNullItem(jTable2);
							popupMenu_nosep.add(nullItem2);
							for (int i = 0; i < components.length; i++) {
								popupMenu_nosep.add(createNormalItem(components[i], jTable2));
							}
							if (resultComponents.size() != 0)
								popupMenu_nosep.addSeparator();
							for (int j = 0; j < resultComponents.size(); j++) {
								JMenuItem item = createSpecialItem(resultComponents.get(j), jTable2);
								// the storages in the same separator can't be the same.
								if(indexMap.get(row).equals(indexMap.get(row+1))) {
									String v = myTable2.model.data[row+1][0].toString();
									if(resultComponents.get(j).equals(v))
										item.setEnabled(false);
								}
								if(indexMap.get(row).equals(indexMap.get(row-1))) {
									String v = myTable2.model.data[row-1][0].toString();
									if(resultComponents.get(j).equals(v))
										item.setEnabled(false);
								}							
								popupMenu_nosep.add(item);
							}														
							popupMenu_nosep.show(jTable2, e.getX(), e.getY());
						}
					}
				}

				if (col == 1) {
					if (!valueInTable2.equals(" ") && !isTerminalComponent(valueInTable2)) {
						if (valueInTable2.equals("Anaerobic Lagoon(Ext.)")) {
							popupMenu_liqu = new JPopupMenu();
							JMenuItem nullItem = createNullItem(jTable2);
							popupMenu_liqu.add(nullItem);
							popupMenu_liqu.add(createNormalItem("Storage Pond", jTable2));
							popupMenu_liqu.add(createNormalItem("Storage Tank", jTable2));
							if (resultComponents.size() != 0)
								popupMenu_liqu.addSeparator();
							for (int j = 0; j < liquidComponents.size(); j++) {
								JMenuItem item = createSpecialItem(liquidComponents.get(j), jTable2);
								// the storages in the same separator can't be the same.
								if(indexMap.get(row).equals(indexMap.get(row+1))) {
									String v = myTable2.model.data[row+1][1].toString();
									if(liquidComponents.get(j).equals(v))
										item.setEnabled(false);
								}
								if(indexMap.get(row).equals(indexMap.get(row-1))) {
									String v = myTable2.model.data[row-1][1].toString();
									if(liquidComponents.get(j).equals(v))
										item.setEnabled(false);
								}							
								popupMenu_liqu.add(item);
							}		
							popupMenu_liqu.show(jTable2, e.getX(), e.getY());

						} else if (valueInTable2.equals("Dry Stack(Uncovered)")) {
							popupMenu_term = new JPopupMenu();
							JMenuItem nullItem = createNullItem(jTable2);
							popupMenu_term.add(nullItem);
							popupMenu_term.add(createNormalItem("Storage Pond", jTable2));
							popupMenu_term.add(createNormalItem("Storage Tank", jTable2));
							popupMenu_term.add(createNormalItem("Anaerobic Lagoon", jTable2));
							if (resultComponents.size() != 0)
								popupMenu_term.addSeparator();
							for (int j = 0; j < terminalComponents.size(); j++) {
								JMenuItem item = createSpecialItem(terminalComponents.get(j), jTable2);
								// the storages in the same separator can't be the same.
								if(indexMap.get(row).equals(indexMap.get(row+1))) {
									String v = myTable2.model.data[row+1][1].toString();
									if(terminalComponents.get(j).equals(v))
										item.setEnabled(false);
								}
								if(indexMap.get(row).equals(indexMap.get(row-1))) {
									String v = myTable2.model.data[row-1][1].toString();
									if(terminalComponents.get(j).equals(v))
										item.setEnabled(false);
								}							
								popupMenu_term.add(item);
							}		
							popupMenu_term.show(jTable2, e.getX(), e.getY());
						}
					}
				}
				jTable2.repaint();
			}
		});

	}

	/**
	 *  gets the pure name, i.g., the string without "#" and "number".
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

	// gets the index of the menuItem in the popupMenu
	private int getIndex(String s, Component[] list) {
		if (list == null || s == null)
			return -1;
		for (int i = 0; i < list.length; i++) {
			try {
				JMenuItem item = (JMenuItem) list[i];
				if (item.getText().equals(s))
					return i;
			} catch (Exception e) {

			}
		}
		return -1;
	}

	// remove one item from the jTable
	private void removeOneItem(String s) {
		try {
			if (countMap.get(s) > 0)
				countMap.put(s, countMap.get(s) - 1);
			if (countMap.get(s) == 0) {
				String pureName = getName(s);
				
				updateResultComponents(s);
				updateLiquidComponents();
				updateTerminalComponents();

				updatePopupMenus("delete", s);
				updateOtherItems(s);
				map.put(pureName, map.get(pureName) - 1);
			}
		} catch (Exception e) {

		}
	}
	/**
	 * remove one row item from the jTable2. i.g., one in jTable2 corresponding to one row in jTable1.
	 * @param rowIndex
	 */
	private void removeOneRowItems(int rowIndex) {
		String itemInTable2_1 = myTable2.model.data[rowIndex][0].toString();
		String itemInTable2_2 = myTable2.model.data[rowIndex][1].toString();
		removeOneItem(itemInTable2_2);
		removeOneItem(itemInTable2_1);
		myTable2.model.mySetValueAt(" ", rowIndex, 1);
		myTable2.model.mySetValueAt(" ", rowIndex, 0);
	}
	
	/**
	 *  remove two rows items from the jTable2. i.g., two rows in jTable2 corresponding to one row in jTable1.
	 * @param rowIndex	the row index of jTable2. i.g., the 1st row of the two rows.
	 */
	private void removeTwoRowsItems(int rowIndex) {
		
		int extraRowInTable2 = rowIndex + 1;
		
		// get the value of column "step 3"
		String valueInSecondCol_1 = myTable2.model.data[rowIndex][1].toString();
		String subValueInSecondCol_1 = getName(valueInSecondCol_1);
		int numInSecondCol_1 = Character.getNumericValue(valueInSecondCol_1.charAt(valueInSecondCol_1.length() - 1));
		
		String valueInSecondCol_2 = myTable2.model.data[extraRowInTable2][1].toString();
		String subValueInSecondCol_2 = getName(valueInSecondCol_2);
		int numInSecondCol_2 = Character.getNumericValue(valueInSecondCol_2.charAt(valueInSecondCol_2.length() - 1));
		
		// get the value of column "step 2"
		String valueInFirstCol_1 = myTable2.model.data[rowIndex][0].toString();
		String subValueInFirstCol_1 = getName(valueInFirstCol_1);
		int numInFirstCol_1 = Character.getNumericValue(valueInFirstCol_1.charAt(valueInFirstCol_1.length() - 1));
		
		String valueInFirstCol_2 = myTable2.model.data[extraRowInTable2][0].toString();
		String subValueInFirstCol_2 = getName(valueInFirstCol_2);
		int numInFirstCol_2 = Character.getNumericValue(valueInFirstCol_2.charAt(valueInFirstCol_2.length() - 1));

		// remove the item in the column "step 3"
		if(subValueInSecondCol_1.equals(subValueInSecondCol_2) && numInSecondCol_1 < numInSecondCol_2) {
			removeOneItem(valueInSecondCol_2);
			removeOneItem(valueInSecondCol_1);
		}
		else {
			removeOneItem(valueInSecondCol_1);
			removeOneItem(valueInSecondCol_2);
		}
		// remove the item in the column "step 2"
		if(subValueInFirstCol_1.equals(subValueInFirstCol_2) && numInFirstCol_1 < numInFirstCol_2) {
			removeOneItem(valueInFirstCol_2);
			removeOneItem(valueInFirstCol_1);
		}
		else {
			removeOneItem(valueInFirstCol_1);
			removeOneItem(valueInFirstCol_2);
		}
		
		myTable2.model.mySetValueAt(" ", rowIndex, 1);
		myTable2.model.mySetValueAt(" ", rowIndex, 0);
	}
	
	// add one item into the jTable
	private void addOneItem(String value) {		
		if (!countMap.containsKey(value))
			countMap.put(value, 1);
		else
			countMap.put(value, countMap.get(value) + 1);

		resultComponents.add(value);
		updateLiquidComponents();
		updateTerminalComponents();
		updatePopupMenus("add", value);
	}

	/**
	 * update resultComponents when remove a item from the jTable.
	 * here delete the latest item, e.g., there are A1,A2,A3, when delete A1
	 * from the jTable, in fact, delete A3 from result. Because after delete A1, 
	 * we need to change A2 to A1, change A3 to A2. The easy way is to delete A3
	 * directly.
	 * @param s
	 */
	private void updateResultComponents(String s) {
		int maxNum = map.get(getName(s));
		for (int i = 0; i < resultComponents.size(); i++) {
			String v = resultComponents.get(i);
			int num = Character.getNumericValue(v.charAt(v.length() - 1));
			if (getName(v).equals(getName(s)) && num == maxNum) {
				resultComponents.remove(v);
			}
		}
	}

	/**
	 * update other items of the tables when delete the target item, e.g.,
	 * there are pond#1 and pond#2, when delete the pond#1, all pond#2 need 
	 * to be changed to pand#1.
	 * @param s the target item to delete
	 */
	private void updateOtherItems(String s) {

		String s1 = getName(s);
		int num1 = Character.getNumericValue(s.charAt(s.length() - 1));

		// update countMap
		ArrayList<String> elements = new ArrayList<String>();
		for (Map.Entry<String, Integer> set : countMap.entrySet()) {
			String s2 = getName(set.getKey());
			if (s2.equals(s1)) {
				elements.add(set.getKey());
			}
		}
		elements.sort(null);
		for (int i = 0; i < elements.size() - 1; i++) {
			String k = elements.get(i);
			int v = countMap.get(elements.get(i + 1));
			countMap.put(k, v);
		}
		countMap.remove(elements.get(elements.size() - 1));

		// update jTable1 data, i.g., the 2nd col's data.
		for (int i = 0; i < jTable1.getRowCount(); i++) {
			String v = myTable1.model.data[i][1].toString();
			String sv = getName(v);
			int num = Character.getNumericValue(v.charAt(v.length() - 1));
			if (sv.equals(s1) && num > num1) {
				String value = sv + " #" + (num - 1);
				myTable1.updateMyCellRender(value);
				myTable1.model.mySetValueAt(value, i, 1);
			}
			jTable1.repaint();
		}
		// update jTable2 data.
		for (int i = 0; i < jTable2.getRowCount(); i++) {
			for (int j = 0; j < 2; j++) {
				String v = myTable2.model.data[i][j].toString();
				String sv = getName(v);
				int num = Character.getNumericValue(v.charAt(v.length() - 1));
				if (sv.equals(s1) && num > num1) {
					String value = sv + " #" + (num - 1);
					myTable2.model.mySetValueAt(value, i, j);					
				}
				jTable2.repaint();
			}
		}

	}

	/**
	 * update the items of jTable1 and jTable2, when exchange two new items. 
	 * e.g., there are pond#1 and pond#2, when "pond #1" change to "pond #2", 
	 * pond#2 needs to be changed to pond#1. 
	 * @param s the old item, the form is name + " #" + No.
	 * @param t the target item, the form is name + " #" + No.
	 */
	private void exchangeNewItems(String s, String t) {

		// update countMap.
		int temp = countMap.get(t);
		countMap.put(t, countMap.get(s));
		countMap.put(s, temp);

		// update jTable1 data, i.e., the 2nd col's data.
		for (int i = 0; i < jTable1.getRowCount(); i++) {
			String v = myTable1.model.data[i][1].toString();
			if (v.equals(t)) {
				myTable1.model.mySetValueAt(s, i, 1);
			}
			if (v.equals(s)) {
				myTable1.model.mySetValueAt(t, i, 1);
			}
			jTable1.repaint();
		}
		// update jTable2 data.
		for (int i = 0; i < jTable2.getRowCount(); i++) {
			for (int j = 0; j < 2; j++) {
				String v = myTable2.model.data[i][j].toString();
				if (v.equals(s)) {
					myTable2.model.mySetValueAt(t, i, j);
				}
				if (v.equals(t)) {
					myTable2.model.mySetValueAt(s, i, j);
				}
				jTable2.repaint();
			}
		}
	}

	// check whether String[] contains the target String
	private boolean isContain(String[] list, String s) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(s))
				return true;
		}
		return false;
	}

	/**
	 * get the first key of the target value in a many-to-one hashmap.
	 * Mainly used to locate the index of the row in jTable2.
	 * @param map	the hashmap, e.g., the indexMap
	 * @param value	the value, e.g., the row in jTable1.
	 * @return
	 */
	private int firstKey(HashMap<Integer, Integer> map, int value) {
		Set<Integer> keys = new HashSet<Integer>();
		for (Integer key : map.keySet()) {
			if (map.get(key).equals(value))
				keys.add(key);
		}
		return Collections.min(keys);
	}

	// check whether the components belongs to the terminal components
	private boolean isTerminalComponent(String s) {
		String s1 = getName(s);
		if (s1.equals("Storage Pond") || s1.equals("Storage Tank") || s1.equals("Anaerobic Lagoon")
				|| s1.equals("Dry Stack(covered)"))
			return true;
		return false;
	}

	// check whether the components belongs to the liquid components
	private boolean isLiquidComponent(String s) {
		String s1 = getName(s);
		if (s1.equals("Storage Pond") || s1.equals("Storage Tank"))
			return true;
		return false;
	}

	// update the TerminalComponents
	private ArrayList<String> updateTerminalComponents() {
		terminalComponents.clear();
		for (int i = 0; i < resultComponents.size(); i++) {
			String v = resultComponents.get(i);
			if (isTerminalComponent(getName(v))) {
				terminalComponents.add(v);
			}
		}
		return terminalComponents;
	}

	// update the LiquidComponents
	private ArrayList<String> updateLiquidComponents() {
		liquidComponents.clear();
		for (int i = 0; i < resultComponents.size(); i++) {
			String v = resultComponents.get(i);
			if (getName(v).equals("Storage Pond") || getName(v).equals("Storage Tank")) {
				liquidComponents.add(v);
			}
		}
		return liquidComponents;
	}

	// initial all popupMenus which will be used.
	private void initialPopupMenu() {

		// adds elements into separator menu
		for (int i = 0; i < sepComponents.length; i++) {
			separatorMenu.add(createNormalItem(sepComponents[i], jTable1));
		}

		// adds elements into popupMenu_full
		JMenuItem nullItem1 = createNullItem(jTable1);
		popupMenu_full.add(nullItem1);
		popupMenu_full.add(separatorMenu);
		for (int i = 0; i < components.length; i++) {
			popupMenu_full.add(createNormalItem(components[i], jTable1));
		}

		// adds elements into popupMenu_nosep
		JMenuItem nullItem2 = createNullItem(jTable2);
		popupMenu_nosep.add(nullItem2);
		for (int i = 0; i < components.length; i++) {
			popupMenu_nosep.add(createNormalItem(components[i], jTable2));
		}

		// adds elements into popupMenu_term
		JMenuItem nullItem3 = createNullItem(jTable2);
		popupMenu_term.add(nullItem3);
		popupMenu_term.add(createNormalItem("Storage Pond", jTable2));
		popupMenu_term.add(createNormalItem("Storage Tank", jTable2));
		popupMenu_term.add(createNormalItem("Anaerobic Lagoon", jTable2));

		// adds elements into popupMenu_liquid
		JMenuItem nullItem4 = createNullItem(jTable2);
		popupMenu_liqu.add(nullItem4);
		popupMenu_liqu.add(createNormalItem("Storage Pond", jTable2));
		popupMenu_liqu.add(createNormalItem("Storage Tank", jTable2));

		// adds elements into popupMenu_runoff
		JMenuItem nullItem5 = createNullItem(jTable1);
		popupMenu_runoff.add(nullItem5);
		popupMenu_runoff.add(createNormalItem("Storage Pond", jTable1));
		popupMenu_runoff.add(createNormalItem("Storage Tank", jTable1));
		popupMenu_runoff.add(createNormalItem("Anaerobic Lagoon", jTable1));
		popupMenu_runoff.add(createNormalItem("Anaerobic Lagoon(Ext.)", jTable1));
	}

	// update the status of popupMenus, add item or delete item.
	private void updatePopupMenus(String type, String text) {

		if (type.equals("add")) {
			popupMenu_full.add(createSpecialItem(text, jTable1));
			popupMenu_runoff.add(createSpecialItem(text, jTable1));
			popupMenu_nosep.add(createSpecialItem(text, jTable2));
			if (isTerminalComponent(text))
				popupMenu_term.add(createSpecialItem(text, jTable2));
			if (isLiquidComponent(text))
				popupMenu_liqu.add(createSpecialItem(text, jTable2));
		}
		if (type.equals("delete")) {

			String s = getName(text);
			int num = map.get(s);
			String newText = s + " #" + num;

			int index1 = getIndex(newText, popupMenu_full.getComponents());
			if (index1 > 0) {
				JMenuItem curItem1 = (JMenuItem) popupMenu_full.getComponents()[index1];
				popupMenu_full.remove(curItem1);
			}

			int index2 = getIndex(newText, popupMenu_nosep.getComponents());
			if (index2 > 0) {
				JMenuItem curItem2 = (JMenuItem) popupMenu_nosep.getComponents()[index2];
				popupMenu_nosep.remove(curItem2);
			}

			int index3 = getIndex(newText, popupMenu_term.getComponents());
			if (index3 > 0) {
				JMenuItem curItem3 = (JMenuItem) popupMenu_term.getComponents()[index3];
				popupMenu_term.remove(curItem3);
			}

			int index4 = getIndex(newText, popupMenu_liqu.getComponents());
			if (index4 > 0) {
				JMenuItem curItem4 = (JMenuItem) popupMenu_liqu.getComponents()[index4];
				popupMenu_liqu.remove(curItem4);
			}
			int index5 = getIndex(newText, popupMenu_runoff.getComponents());
			if (index5 > 0) {
				JMenuItem curItem5 = (JMenuItem) popupMenu_runoff.getComponents()[index5];
				popupMenu_runoff.remove(curItem5);
			}
		}
	}



	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
