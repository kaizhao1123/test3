package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import Model_Entity.AdditionsPanelOutputElement;
import Model_Tables.MgmtTrainTable_1;
import Model_Tables.MgmtTrainTable_2;
import Model_Tables.MgmtTrainTable_3;

import Model_Entity.AnimalPanelOutputElement;
import Model_Entity.LocationPanelOutputElement;
import Model_Entity.SeparatorInfo;

public class MgmtTrainPanel extends JPanel {

	MainFrame parent;
	JTabbedPane pane;
	OperatingPeriodDialog periodDialog;
	JPanel panel;
	PanelManager panelManager;

	DecimalFormat df = new DecimalFormat("0.00");
	String[] columnName_1 = { "Waste Stream", "Step 1" }; // jTable1's column names
	String[] columnName_2 = { "Step 2", "Step 3" }; // jTable2's column names
	String[] columnName_3 = { "Component Name", "Manure", "Wash Water", "Flush Water", "Bedding",
			"Total Waste Volume" }; // jtable3's column

	ArrayList<String> elementsInFirstColumn; // the elements of the first column of jTable1
	Object[][] tableData_1;			//to show the first 2 columns in above screen
	Object[][] tableData_2;			//to show the last 2 columns in above screen
	Object[][] tableData_3;			//to show in bottom screen
	Object[][] tableData_4;			//to show in bottom screen alternatively.

	Boolean twoPeriod = false;
	
	MgmtTrainTable_1 myTable1;
	JTable jTable1;
	MgmtTrainTable_2 myTable2;
	JTable jTable2;
	MgmtTrainTable_3 myTable3;
	JTable jTable3;
	MgmtTrainTable_3 myTable4;
	JTable jTable4;

	JScrollPane subScrollPane_1; // contains jTable1.
	JScrollPane subScrollPane_2; // contains jTable2.
	JPanel panel_1; // contains subScrollPanel_1 and subScrollPanel_2
	JPanel panel_2; // contains "help" and "ok" buttons.
	JScrollPane scrollPane_1; // contains panel_1.
	JScrollPane scrollPane_2; // contains jtable3.

	JLabel label;
	String firstPeriod;
	String secondPeriod;
	String[] periods;
	JComboBox operationPeriod;
	JButton buttonHelp;
	JButton buttonOK;

	JMenu separatorMenu; // JMenu with all separator components
	JPopupMenu popupMenu_full; // popupMenu with all components.
	JPopupMenu popupMenu_nosep; // popupMenu with all components except the separatorMenu.
	JPopupMenu popupMenu_term; // popupMenu with some components and new terminal items.
	JPopupMenu popupMenu_liqu; // popupMenu with some components and new liquid items.
	JPopupMenu popupMenu_runoff; // popupMenu with some components, used for "runoff"

	String[] components = { "Storage Pond", "Storage Tank", "Dry Stack(Uncovered)", "Dry Stack(covered)",
			"Anaerobic Lagoon", "Anaerobic Lagoon(Ext)" };
	String[] sepComponents = { "Decanter Centrifuge 16-30 gpm", "Screw Press", "Settling Basin",
			"Static Inclined Screen", "Static Inclined Screen 12 Mesh", "Static Inclined Screen 36 Mesh",
			"Vibrating Screen", "Vibrating Screen 16 Mesh", "Vibrating Screen 18 Mesh", "Vibrating Screen 24 Mesh",
			"Vibrating Screen 30 Mesh" };

	// records the storage component and its number, such as: <Storage Tank, 1>.
	HashMap<String, Integer> map = new HashMap<>();
	/*
	 * records the storage component with its number and its count, e.g., <Storage
	 * Tank #1, 1>.
	 */
	HashMap<String, Integer> countMap = new HashMap<>();
	/*
	 * records the index of table2 corresponding index of table1, used for recording
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
	 * stores the terminal storage components in the resultComponents: tank, pond,
	 * covered dry stack, and anaerobic lagoon.
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
		elementsInFirstColumn = panelManager.getAllStreams();
		tableData_1 = new Object[elementsInFirstColumn.size()][2];
		for (int i = 0; i < elementsInFirstColumn.size(); i++) {
			tableData_1[i][0] = elementsInFirstColumn.get(i);
			tableData_1[i][1] = " ";
			indexMap.put(i, i); // initial indexMap by the way.
		}

		// initial jTable2
		tableData_2 = new Object[elementsInFirstColumn.size()][2];
		for (int i = 0; i < elementsInFirstColumn.size(); i++) {
			tableData_2[i][0] = " ";
			tableData_2[i][1] = " ";
		}

		// initial jTable3, jTable4.
		tableData_3 = null;
		tableData_4 = null;

		
		periods = new String[2];
		resultComponents = new ArrayList<>();
		liquidComponents = new ArrayList<>();
		terminalComponents = new ArrayList<>();
	}

	/**
	 * initial elements in this panel. there are two JscrollPanes to show tables,
	 * the first scrollPane shows a child panel, which includes two child
	 * Jscrollpanes, show jTable1 and jTable2 respectively. The 2nd scrollPane shows
	 * jTable3. in addition, initial all popupMenus.
	 */
	private void initialElements() {
		panel = this;
		label = new JLabel("<HTML> <U>Component Volumens(cu.ft/day)</U></HTML>");
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, 13));
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
		subScrollPane_1.setPreferredSize(new Dimension(350, 223));
		subScrollPane_1.setViewportBorder(border);
		subScrollPane_1.setBorder(border);

		myTable2 = new MgmtTrainTable_2();
		jTable2 = myTable2.buildMyTable(columnName_2, tableData_2);
		jTable2.setRowHeight(50);
		jTable2.getColumnModel().getColumn(0).setWidth(160);
		jTable2.getColumnModel().getColumn(1).setWidth(115);
		jTable2.setRowSelectionAllowed(false);
		subScrollPane_2 = new JScrollPane(jTable2);
		subScrollPane_2.setPreferredSize(new Dimension(275, 223));
		subScrollPane_2.setViewportBorder(border);
		subScrollPane_2.setBorder(border);
		subScrollPane_2.setVerticalScrollBar(subScrollPane_1.getVerticalScrollBar());

		panel_1 = new JPanel();
		panel_1.setLayout(new GridBagLayout());
		panel_1.setPreferredSize(new Dimension(630, 226));
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
		scrollPane_1.setPreferredSize(new Dimension(630, 226));
		scrollPane_1.setViewportBorder(border);
		scrollPane_1.setBorder(border);
				   	
		// initial scrollPane_2
		myTable3 = new MgmtTrainTable_3();
		jTable3 = myTable3.buildMyTable(columnName_3, tableData_3);
		jTable3.getColumnModel().getColumn(0).setWidth(160);
		jTable3.getColumnModel().getColumn(1).setWidth(85);
		jTable3.getColumnModel().getColumn(2).setWidth(85);
		jTable3.getColumnModel().getColumn(3).setWidth(85);
		jTable3.getColumnModel().getColumn(4).setWidth(85);
		jTable3.getColumnModel().getColumn(5).setWidth(130);
		jTable3.enable(false);
		jTable3.setRowHeight(25);
		
		myTable4 = new MgmtTrainTable_3();
		jTable4 = myTable4.buildMyTable(columnName_3, tableData_4);
		jTable4.getColumnModel().getColumn(0).setWidth(160);
		jTable4.getColumnModel().getColumn(1).setWidth(85);
		jTable4.getColumnModel().getColumn(2).setWidth(85);
		jTable4.getColumnModel().getColumn(3).setWidth(85);
		jTable4.getColumnModel().getColumn(4).setWidth(85);
		jTable4.getColumnModel().getColumn(5).setWidth(130);
		jTable4.enable(false);
		jTable4.setRowHeight(25);
		

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
				}
				myTable1.updateMyCellRender(" ");
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
						if (valueInTable1.equals("Anaerobic Lagoon(Ext)")) {
							popupMenu_liqu.show(jTable2, e.getX(), e.getY());
						} else if (valueInTable1.equals("Dry Stack(Uncovered)")) {
							popupMenu_term.show(jTable2, e.getX(), e.getY());
						} else {
							popupMenu_nosep.show(jTable2, e.getX(), e.getY());
						}
					}
				}

				if (col == 1 && !valueInTable2.equals(" ") && !isTerminalComponent(valueInTable2))
					popupMenu_term.show(jTable2, e.getX(), e.getY());
				jTable2.repaint();
			}
		});
		
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
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
		gbc.gridwidth = 2;
		add(scrollPane_1, gbc);

		gbc.gridy = 2;
		gbc.gridwidth = 1;
		add(label, gbc);

		gbc.gridy = 3;
		gbc.gridwidth = 2;
		add(scrollPane_2, gbc);

		gbc.anchor = GridBagConstraints.EAST;		
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		add(panel_2, gbc);
	}

	// Corresponding the first option of periodDialog, that is, only table1 will be shown
	public void update1() {
		twoPeriod = false;
    	label.setText("<HTML> <U>Component Volumens(cu.ft/day)</U></HTML>");    	
    	panel.remove(operationPeriod);
    	scrollPane_2.setViewportView(jTable3);
    	panel.updateUI();
    }
	// Corresponding the second option of periodDialog, that is, both table will be shown.
    public void update2() {
    	twoPeriod = true;
    	periodDialog = parent.startPanel.periodDialog;
    	firstPeriod = periodDialog.firstOperatingPeriod;
    	secondPeriod = periodDialog.secondOperatingPeriod;
    	
    	periods[0] = firstPeriod;
    	periods[1] = secondPeriod;
    	label.setText("<HTML> <U>Component Volumens(cu.ft/day) for Operating Period: </U></HTML>");
    	operationPeriod = new JComboBox<>(periods);
    	operationPeriod.setPreferredSize(new Dimension(160,25));
    	operationPeriod.setSelectedIndex(0);
    	scrollPane_2.setViewportView(jTable3);
    	/*operationPeriod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					operationPeriod.setPrototypeDisplayValue(operationPeriod.getSelectedItem());
			    	if(operationPeriod.getSelectedItem().toString().equals(firstPeriod)) {
						System.out.print("t3");
						scrollPane_2.setViewportView(jTable3);
					}			
					else {
						System.out.print("t4");
						scrollPane_2.setViewportView(jTable4);
					} 
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});*/
  	
    	
    	
		gc.anchor = GridBagConstraints.NORTHWEST;
        //gc.insets = new Insets(5,5,5,5);
		gc.gridx = 1;
    	gc.gridy = 2;
    	gc.gridwidth = 1;
		panel.add(operationPeriod,gc);
		panel.updateUI();
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
							removeRowInTable2(extraRowInTable2);
						} else if (!isContain(sepComponents, getName(cur))) {
							int rowInTable2 = firstKey(indexMap, row);
							removeOneRowItems(rowInTable2);
							removeOneItem(cur);
						}
					}
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
					//myTable1.updateMyCellRender(" ");
					jTable2.repaint();
				}
				updateBottomJtables("jTable3");
				updateBottomJtables("jTable4");
				if(twoPeriod.equals(false))
					scrollPane_2.setViewportView(jTable3);
				else
					scrollPane_2.setViewportView(jTable4);
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
				updateBottomJtables("jTable3");
				updateBottomJtables("jTable4");
				if(twoPeriod.equals(false))
					scrollPane_2.setViewportView(jTable3);
				else
					scrollPane_2.setViewportView(jTable4);
			}
		});
		return item;
	}

	/**
	 * creates normal item when initializing the popupMenu.
	 * 
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
							myTable1.model.mySetValueAt(s, row, col);
							myTable2.model.mySetValueAt(" ", row, col);
							insertRowInTable2(firstKey(indexMap, row));
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
							// update value of this cell.
							myTable1.model.mySetValueAt(value, row, col);
						}
					}

					/*
					 * Already have chosen one item, but need to change to another one. that is, the
					 * current value of the cell is not null.
					 */
					else {
						/*
						 * check whether the cur item is in sepComponents or not. if yes, need to remove
						 * a row from table2; if no, check whether both cur and target items belong to
						 * the same category or not, if no, remove the cur item from jTable1, otherwise,
						 * do nothing.
						 */
						if (isContain(sepComponents, getName(cur))) {
							int rowInTable2 = firstKey(indexMap, row);
							int extraRowInTable2 = rowInTable2 + 1;
							removeTwoRowsItems(rowInTable2);
							removeRowInTable2(extraRowInTable2);
						} else if (!isContain(sepComponents, getName(cur))) {
							String c1 = getName(cur);
							String s1 = getName(s);
							if (!c1.equals(s1)) {
								int rowInTable2 = firstKey(indexMap, row);
								removeOneRowItems(rowInTable2);
								removeOneItem(cur);
							}
						}

						/*
						 * check whether the target item is in sepComponents or not. if yes, need to
						 * insert a row into table2; if no, check whether cur item is equal to the
						 * target item or not, if no, add into 1st map and change value, otherwise, do
						 * nothing.
						 */

						if (isContain(sepComponents, s)) {
							myTable1.model.mySetValueAt(s, row, col);
							myTable2.model.mySetValueAt(" ", row, col);
							insertRowInTable2(firstKey(indexMap, row));
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
								myTable1.model.mySetValueAt(value, row, col);
							}
						}
					}
				}

				// jt == jTable2, which is similar with jTable1, without checking separatorMenu
				// items.
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
				updateBottomJtables("jTable3");
				updateBottomJtables("jTable4");
				if(twoPeriod.equals(false))
					scrollPane_2.setViewportView(jTable3);
				else
					scrollPane_2.setViewportView(jTable4);
			}
		});
		return item;
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
						if (valueInTable1.equals("Anaerobic Lagoon(Ext)")) {
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
								if (indexMap.get(row).equals(indexMap.get(row + 1))) {
									String v = myTable2.model.data[row + 1][0].toString();
									if (resultComponents.get(j).equals(v))
										item.setEnabled(false);
								}
								if (indexMap.get(row).equals(indexMap.get(row - 1))) {
									String v = myTable2.model.data[row - 1][0].toString();
									if (resultComponents.get(j).equals(v))
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
						if (valueInTable2.equals("Anaerobic Lagoon(Ext)")) {
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
								if(indexMap.get(row).equals(indexMap.get(row + 1))) {
									String v1 = myTable2.model.data[row + 1][1].toString();
									String v0 = myTable2.model.data[row + 1][0].toString();
									if(liquidComponents.get(j).equals(v0) || liquidComponents.get(j).equals(v1))
										item.setEnabled(false);
								}
								if(indexMap.get(row).equals(indexMap.get(row - 1))) {
									String v0 = myTable2.model.data[row - 1][0].toString();
									String v1 = myTable2.model.data[row - 1][1].toString();
									if(liquidComponents.get(j).equals(v0) || liquidComponents.get(j).equals(v1))
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
								if (indexMap.get(row).equals(indexMap.get(row + 1))) {
									String v0 = myTable2.model.data[row + 1][0].toString();
									String v1 = myTable2.model.data[row + 1][1].toString();
									if (terminalComponents.get(j).equals(v0) || terminalComponents.get(j).equals(v1))
										item.setEnabled(false);
								}
								if (indexMap.get(row).equals(indexMap.get(row - 1))) {
									String v0 = myTable2.model.data[row - 1][0].toString();
									String v1 = myTable2.model.data[row - 1][1].toString();
									if (terminalComponents.get(j).equals(v0) || terminalComponents.get(j).equals(v1))
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
	
	// update jTable3 or Jtable4
	public void updateBottomJtables(String name) {
		
		if(name.equals("jTable3")) {
			if(tableData_3 != null) {
				tableData_3 = null;
				myTable3 = new MgmtTrainTable_3();
				jTable3 = myTable3.buildMyTable(columnName_3, tableData_3);
				jTable3.getColumnModel().getColumn(0).setWidth(160);
				jTable3.getColumnModel().getColumn(1).setWidth(85);
				jTable3.getColumnModel().getColumn(2).setWidth(85);
				jTable3.getColumnModel().getColumn(3).setWidth(85);
				jTable3.getColumnModel().getColumn(4).setWidth(85);
				jTable3.getColumnModel().getColumn(5).setWidth(130);
				jTable3.enable(false);
				jTable3.setRowHeight(25);
			}		
		}
		
		if(name.equals("jTable4")) {
			if(tableData_4 != null) {
				tableData_4 = null;
				myTable4 = new MgmtTrainTable_3();
				jTable4 = myTable4.buildMyTable(columnName_3, tableData_4);
				jTable4.getColumnModel().getColumn(0).setWidth(160);
				jTable4.getColumnModel().getColumn(1).setWidth(85);
				jTable4.getColumnModel().getColumn(2).setWidth(85);
				jTable4.getColumnModel().getColumn(3).setWidth(85);
				jTable4.getColumnModel().getColumn(4).setWidth(85);
				jTable4.getColumnModel().getColumn(5).setWidth(130);
				jTable4.enable(false);
				jTable4.setRowHeight(25);
			}		
		}

		// go through the whole resultComponents
		for(int i = 0; i < resultComponents.size(); i++) {
			String ele = resultComponents.get(i);
			String[] rowData = new String[6];
			rowData[0] = ele;
			double v1 = 0.0;
			double v2 = 0.0;
			double v3 = 0.0;
			double v4 = 0.0;
			double v5 = 0.0;
			// for special components: "Anaerobic Lagoon(Ext)", which will show "N/A".
			if(getName(ele).equals("Anaerobic Lagoon(Ext)")) {
				for(int j = 1; j < 6; j++) {
					rowData[j] = "N/A";
				}
			}
			/*
			 * for other components, needs to calculate the data, to be precise, accumulate
			 * data. go through column of jTable1 and columns of Jtable2 to get the target
			 * component.
			 */
			else {
				// jTable1
				for(int j = 0; j < jTable1.getRowCount(); j++) {
					if(myTable1.model.data[j][1].toString().equals(ele)) {
						String streamName = myTable1.model.data[j][0].toString();
						String tempV2, tempV3, tempV4;
						if(name.equals("jTable3")) {
							tempV2 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[0];
							tempV3 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[1];
							tempV4 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[2];
						}
						else {
							tempV2 = getAdditionElement(panelManager.additionsPanelOutput.get(1), streamName).data[0];
							tempV3 = getAdditionElement(panelManager.additionsPanelOutput.get(1), streamName).data[1];
							tempV4 = getAdditionElement(panelManager.additionsPanelOutput.get(1), streamName).data[2];
						}
						
						if(getLocationElement(panelManager.locationsPanelOutput.get(0), streamName) == null) {
							v1 += 0.0;
						} 
						else {
							/*
							 * go through each column of location panel with the target row. the data in the
							 * 1st column is the stream name, i.g., name the length of animalpaneloutput is
							 * the data length of locationpaneloutput element.
							 */
							double val = 0.0;
							for(int subj = 0; subj < panelManager.animalsPanelOutput.size(); subj++) {
								AnimalPanelOutputElement animalElement = panelManager.animalsPanelOutput.get(subj);
								String manure = animalElement.data[0];
								String locationRatio;
								if(name.equals("jTable3"))
									locationRatio = getLocationElement(panelManager.locationsPanelOutput.get(0),
										streamName).data[subj];
								else
									locationRatio = getLocationElement(panelManager.locationsPanelOutput.get(1),
											streamName).data[subj];
								double dManure = Double.parseDouble(manure);
								double dFactor = Double.parseDouble(locationRatio);
								val += (dManure * dFactor / 100);
							}
							v1 += val;
						}
						v2 += (Double.parseDouble(tempV2) / 7.48);
						v3 += (Double.parseDouble(tempV3) / 7.48);
						v4 += Double.parseDouble(tempV4);
						v5 = (v1 + v2 + v3 + v4);

					}
				}
				// jTable2
				for(int k = 0; k < jTable2.getRowCount(); k++) {

					int rowInTable1 = indexMap.get(k);
					int rowInTable2 = firstKey(indexMap, rowInTable1);
					String streamName = myTable1.model.data[rowInTable1][0].toString();
					String separatorName = myTable1.model.data[rowInTable1][1].toString();
					String separatorRatio = "1";
					String valueInCol_1 = myTable2.model.data[k][0].toString(); // step 2
					String valueInCol_2 = myTable2.model.data[k][1].toString(); // step 3
					
					//String tempV2 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[0]; // wash water
					//String tempV3 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[1]; // flush water
					//String tempV4 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[2]; // bedding value					
					String tempV2, tempV3, tempV4;
					if(name.equals("jTable3")) {
						tempV2 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[0];	// wash water
						tempV3 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[1];	// flush water
						tempV4 = getAdditionElement(panelManager.additionsPanelOutput.get(0), streamName).data[2];	// bedding value
					}
					else {
						tempV2 = getAdditionElement(panelManager.additionsPanelOutput.get(1), streamName).data[0];
						tempV3 = getAdditionElement(panelManager.additionsPanelOutput.get(1), streamName).data[1];
						tempV4 = getAdditionElement(panelManager.additionsPanelOutput.get(1), streamName).data[2];
					}
					if (isContain(sepComponents, separatorName))
						separatorRatio = getSeparatorElement(panelManager.allSeparatorData, separatorName).efficiency;
					/*
					 * the 1st column of jTable2, the prior condition is one of following: 1. choose
					 * separator in jTable1 leads to the exist of data in jtable2; 2. choose
					 * "uncovered" or "(ext)".
					 */
					if (valueInCol_1.equals(ele)
							|| (valueInCol_2.equals(ele) && getName(valueInCol_1).equals("Anaerobic Lagoon(Ext)"))) {
						/*
						 * the 1st row of the separator(i.g.,liquid) or noSeparator( "uncovered" or
						 * "(ext)" ). 1. it is the same as jTable1 except v4, which based on the type of
						 * separator. 2. it is the same as jTable1.
						 */
						if (k == rowInTable2) {
							if (getLocationElement(panelManager.locationsPanelOutput.get(0), streamName) == null) {
								v1 += 0.0;
							} else {
								/*
								 * go through each column of location panel with the target row. the data in the
								 * 1st column is the stream name, i.g., name the length of animalpaneloutput is
								 * the data length of locationpaneloutput element.
								 */
								double val = 0.0;
								for (int subj = 0; subj < panelManager.animalsPanelOutput.size(); subj++) {
									AnimalPanelOutputElement animalElement = panelManager.animalsPanelOutput.get(subj);
									String manure = animalElement.data[0];
									String locationRatio;
									if(name.equals("jTable3"))
										locationRatio = getLocationElement(panelManager.locationsPanelOutput.get(0),
											streamName).data[subj];
									else
										locationRatio = getLocationElement(panelManager.locationsPanelOutput.get(1),
												streamName).data[subj];
									double dManure = Double.parseDouble(manure);
									double dFactor = Double.parseDouble(locationRatio);
									val += (dManure * dFactor / 100);
								}
								v1 += val;
							}
							v2 += (Double.parseDouble(tempV2) / 7.48);
							v3 += (Double.parseDouble(tempV3) / 7.48);
							if (!isContain(sepComponents, separatorName)) // the 2nd condition.
								v4 += Double.parseDouble(tempV4);
							else
								v4 += (Double.parseDouble(tempV4) * (1 - Double.parseDouble(separatorRatio)));
							v5 = (v1 + v2 + v3 + v4);
						}
						/*
						 * the 2nd row of the separator(i.g., valid). it is the same as jTable1 except
						 * v1 and v4, which based on the type of separator.
						 */
						else {
							if (getLocationElement(panelManager.locationsPanelOutput.get(0), streamName) == null) {
								v1 += 0.0;
							} else {
								/*
								 * go through each column of location panel with the target row. the data in the
								 * 1st column is the stream name, i.g., name the length of animalpaneloutput is
								 * the data length of locationpaneloutput element.
								 */
								double val = 0.0;
								for (int subj = 0; subj < panelManager.animalsPanelOutput.size(); subj++) {
									AnimalPanelOutputElement animalElement = panelManager.animalsPanelOutput.get(subj);
									String manure = animalElement.data[1]; // solid(TS)
									String locationRatio;
									if(name.equals("jTable3"))
										locationRatio = getLocationElement(panelManager.locationsPanelOutput.get(0),
											streamName).data[subj];
									else
										locationRatio = getLocationElement(panelManager.locationsPanelOutput.get(1),
												streamName).data[subj];
									double dManure = Double.parseDouble(manure);
									double dFactor = Double.parseDouble(locationRatio);
									val += (dManure * dFactor / 100);
								}
								v1 += (val * Double.parseDouble(separatorRatio) / 55);
							}
							v2 += 0.0;
							v3 += 0.0;
							v4 += (Double.parseDouble(tempV4) * Double.parseDouble(separatorRatio));
							v5 = (v1 + v2 + v3 + v4);
						}
					} else if (valueInCol_2.equals(ele) && getName(valueInCol_1).equals("Dry Stack(Uncovered)")) {
						v1 += 0.0;
						v2 += 0.0;
						v3 += 0.0;
						v4 += 0.0;
						v5 = (v1 + v2 + v3 + v4);
					}
				}

				rowData[1] = df.format(v1);
				rowData[2] = df.format(v2);
				rowData[3] = df.format(v3);
				rowData[4] = df.format(v4);
				rowData[5] = df.format(v5);
			}
			if(name.equals("jTable3")) {
				if (jTable3.getRowCount() == 0)
					myTable3.model.insertRow(rowData, 0);
				else
					myTable3.model.insertRow(rowData, jTable3.getRowCount() - 1);
			}
			else {
				if (jTable4.getRowCount() == 0)
					myTable4.model.insertRow(rowData, 0);
				else
					myTable4.model.insertRow(rowData, jTable4.getRowCount() - 1);
			}
					
			
		}
		// jTable3.updateUI();
		if(name.equals("jTable3")) 
			tableData_3 = myTable3.model.data;
		else
			tableData_4 = myTable4.model.data;
		
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
	 * remove one row item from the jTable2. i.g., one in jTable2 corresponding to
	 * one row in jTable1.
	 * 
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
	 * remove two rows items from the jTable2. i.g., two rows in jTable2
	 * corresponding to one row in jTable1.
	 * 
	 * @param rowIndex the row index of jTable2. i.g., the 1st row of the two rows.
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
	 * Inserts a new row into the target index. It is used for the separator menu.
	 * After insert the row, rebuild the jTable and add listener into the new
	 * jTable.
	 * 
	 * @param name
	 * @param s
	 * @param rowIndex
	 */
	public void insertRowInTable2(int rowIndex) {

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
	public void removeRowInTable2(int rowIndex) {
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

	// remove the target row of the jTable1, following the remove of jTable2.
	public void removeRowInTable1(int rowIndex, int rowIndex2) {
		// update indexMap
		// int rowIndexInTable2 = firstKey(indexMap, rowIndex);
		for (int i = rowIndex2; i < jTable2.getRowCount(); i++) {
			indexMap.put(i, indexMap.get(i) - 1);
		}

		// update jTable1
		myTable1.model.deleteRow(rowIndex);
		jTable1.repaint();
		subScrollPane_2.setViewportView(jTable2);
		subScrollPane_1.setViewportView(jTable1);
		scrollPane_1.setViewportView(panel_1);
	}
	
	/**
	 * update resultComponents when remove a item from the jTable. here delete the
	 * latest item, e.g., there are A1,A2,A3, when delete A1 from the jTable, in
	 * fact, delete A3 from result. Because after delete A1, we need to change A2 to
	 * A1, change A3 to A2. The easy way is to delete A3 directly.
	 * 
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
	 * update other items of the tables when delete the target item, e.g., there are
	 * pond#1 and pond#2, when delete the pond#1, all pond#2 need to be changed to
	 * pand#1.
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
	 * update the items of jTable1 and jTable2, when exchange two new items. e.g.,
	 * there are pond#1 and pond#2, when "pond #1" change to "pond #2", pond#2 needs
	 * to be changed to pond#1.
	 * 
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

	/**
	 * gets the pure name, i.g., the string without "#" and "number".
	 * 
	 * @param s the target string, with the format: "name" + " #" + No.
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

	// check whether String[] contains the target String
	private boolean isContain(String[] list, String s) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(s))
				return true;
		}
		return false;
	}

	/**
	 * get the first key of the target value in a many-to-one hashmap. Mainly used
	 * to locate the index of the row in jTable2.
	 * 
	 * @param map   the hashmap, e.g., the indexMap
	 * @param value the value, e.g., the row in jTable1.
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
	
	// find the target element of the AdditionsPanelOutput
	private AdditionsPanelOutputElement getAdditionElement(ArrayList<AdditionsPanelOutputElement> list, String name) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equals(name))
				return list.get(i);
		}
		return null;
	}

	// find the target element of the AnimalPanelOutput
	private AnimalPanelOutputElement getAnimalElement(ArrayList<AnimalPanelOutputElement> list, String name) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equals(name))
				return list.get(i);
		}
		return null;
	}

	// find the target element of the LocationPanelOutput
	private LocationPanelOutputElement getLocationElement(ArrayList<LocationPanelOutputElement> list, String name) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equals(name))
				return list.get(i);
		}
		return null;
	}

	// find the target element of the Separator
	private SeparatorInfo getSeparatorElement(ArrayList<SeparatorInfo> list, String name) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equals(name))
				return list.get(i);
		}
		return null;
	}

	/**
	 *  delete target stream name from jtable1 and jtable2.
	 *  when animalPanel, locationPanel, additionsPanel and runoffPanel change, mgmtTrainPanel will be changed.
	 * @param s	the stream name
	 */
	public void deleteRow(String s) {
		int rowInTable1 = -1;
		for (int i = 0; i < jTable1.getRowCount(); i++) {
			if (myTable1.model.data[i][0].toString().equals(s))
				rowInTable1 = i;
		}
		if (rowInTable1 > -1) {
			int rowInTable2 = firstKey(indexMap, rowInTable1);
			String v = myTable1.model.data[rowInTable1][1].toString();
			if (isContain(sepComponents, v)) {
				int rowExtraInTable2 = rowInTable2 + 1;
				removeTwoRowsItems(rowInTable2);
				removeOneItem(v);
				removeRowInTable2(rowExtraInTable2);
				removeRowInTable2(rowInTable2);
				removeRowInTable1(rowInTable1, rowInTable2);
			} else if (!(isContain(sepComponents, v))) {
				removeOneRowItems(rowInTable2);
				removeOneItem(v);
				removeRowInTable2(rowInTable2);
				removeRowInTable1(rowInTable1, rowInTable2);
			}
		}
	}

	/**
	 *  add new stream name into jTable1 and jTable2.
	 *  when animalPanel, locationPanel, additionsPanel and runoffPanel change, mgmtTrainPanel will be changed.
	 * @param s stream name
	 * @param rowIndex	
	 */
	public void addRow(String s, int rowIndex) {
		
		String[] newData_1 = new String[2];		
		newData_1[0] = s;
		newData_1[0] = " ";
		String[] newData_2 = new String[2];		
		newData_2[0] = " ";
		newData_2[1] = " ";
		
		int rowInTable2 = firstKey(indexMap, rowIndex);
		myTable1.model.insertRow(newData_1, rowIndex);
		myTable1.model.mySetValueAt(s, rowIndex+1, 0);
		myTable1.model.mySetValueAt(" ", rowIndex+1, 1);
		myTable2.model.insertRow(newData_2, rowInTable2);		
		indexMap.put(jTable2.getRowCount(), jTable1.getRowCount());
		
		updateTable2();
		jTable1.updateUI();
		subScrollPane_2.setViewportView(jTable2);
		subScrollPane_1.setViewportView(jTable1);
		scrollPane_1.setViewportView(panel_1);
	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
