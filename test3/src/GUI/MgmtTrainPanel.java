package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import Controller.PanelManager;
import Model_Tables.MgmtTrainTable_1;
import Model_Tables.MgmtTrainTable_2;

public class MgmtTrainPanel extends JPanel{
	
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	

	String[] columnName1 = {"Waste Stream", "Step 1","Step 2","Step 3"};
	Object[][] tableData1 = { {"a", " ", " ", " "},
							  {"b", " ", " ", " "},
							  {"Runoff", " ", " ", " "}};
	String[] columnName2 = {"Component Name", "Manure","Wash Water","Flush Water", "Bedding", "Total Waste Volume"};
	Object[][] tableData2 = { {"","","","","","",""} };
	
    MgmtTrainTable_1 myTable1;
    JTable jTable1;
    MgmtTrainTable_2 myTable2;
    JTable jTable2;
    JScrollPane scrollPane1;
    JScrollPane scrollPanel2;
	
    JLabel label;
	JButton buttonHelp;
	JButton buttonOK;
	

	JPopupMenu popupMenu1;
	JPopupMenu popupMenu2;
	JPopupMenu popupMenu3;
	JMenu separatorMenu;
	String[] components = { "Storage Pond", "Storage Tank", "Dry Stack(Uncovered)", "Dry Stack(covered)",
			"Anaerobic Lagoon", "Anaerobic Lagoon(Ext.)" };
	String[] sepComponents = { "Decanter Centrifuge 16-30 gpm", "Screw Press", "Settling Basin",
			"Static Inclined Screen", "Static Inclined Screen 12 Mesh", "Static Inclined Screen 36 Mesh",
			"Vibrating Screen", "Vibrating Screen 16 Mesh", "Vibrating Screen 18 Mesh", "Vibrating Screen 24 Mesh",
			"Vibrating Screen 30 Mesh" };

	// record the storage component and its number, such as: <Storage Tank, 1>.
	HashMap<String, Integer> map = new HashMap<>();
	// record the storage component with its number and its count, such as: <Storage Tank #1, 1>.
	HashMap<String, Integer> countMap = new HashMap<>();
	// store the terminal storage components, used for the second table.
	ArrayList<String> resultComponents;
	
	//JPanel panel;
	GridBagConstraints gc;
	
	public MgmtTrainPanel(PanelManager pm) {

		panelManager = pm;
		initialData();
		initialElements();
		initialActionLiseners();
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		initialLayout(gc);
	}

	private void initialData() {

	}

	private void initialElements() {
		label = new JLabel("Component Volumens(cu.ft/day)");
		myTable1 = new MgmtTrainTable_1();
		jTable1 = myTable1.buildMyTable(columnName1, tableData1);
		jTable1.setRowHeight(25);
		// TableColumn column = jTable1.getColumnModel().getColumn(2);
		// column.setPreferredWidth(120);

		scrollPane1 = new JScrollPane(jTable1);
		scrollPane1.setPreferredSize(new Dimension(550, 150));
		myTable2 = new MgmtTrainTable_2();
		jTable2 = myTable2.buildMyTable(columnName2, tableData2);
		jTable2.enable(false);
		jTable2.setRowHeight(25);

		// TableColumn column = jTable2.getColumnModel().getColumn(2);
		// column.setPreferredWidth(150);
		// column.setPreferredWidth(new Dimension(120,25));

		scrollPanel2 = new JScrollPane(jTable2);
		scrollPanel2.setPreferredSize(new Dimension(550, 100));

		buttonHelp = new JButton("Help");
		buttonHelp.setPreferredSize(new Dimension(60, 25));
		buttonOK = new JButton("OK");
		buttonOK.setPreferredSize(new Dimension(60, 25));
		
		popupMenu1 = new JPopupMenu();
		popupMenu2 = new JPopupMenu();
		popupMenu3 = new JPopupMenu();
		separatorMenu = new JMenu("Solid-Liquid Separator");
		resultComponents = new ArrayList<>();
		initialPopupMenu();
		
	}

	// build menuItem with name "None(Clear)"
	private JMenuItem createNullItem() {
		JMenuItem nullItem = new JMenuItem();
		nullItem.setText("None(Clear)");
		nullItem.addActionListener(new java.awt.event.ActionListener() {			
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				int row = jTable1.getSelectedRow();
				int col = jTable1.getSelectedColumn();				
				if (!myTable1.model.data[row][col].toString().equals(" ")) {
					String cur = myTable1.model.data[row][col].toString();
					countMap.put(cur, countMap.get(cur) - 1);
					if(countMap.get(cur) == 0) {
						String pureName = getName(cur);
						map.put(pureName, map.get(pureName) - 1);
						
						int index1 = getIndex(cur, popupMenu1.getComponents());
						JMenuItem curItem1 = (JMenuItem) popupMenu1.getComponents()[index1];
						popupMenu1.remove(curItem1);
						
						int index2 = getIndex(cur, popupMenu2.getComponents());
						if(index2 != -1) {
							JMenuItem curItem2 = (JMenuItem) popupMenu2.getComponents()[index2];						
							popupMenu2.remove(curItem2);
						}
							
					}								
				}

				myTable1.model.mySetValueAt(" ", row, col);							
				jTable1.repaint();
			}
		});
		return nullItem;
	}
	
	/**
	 * creates new items after the separator line of the popupMenu, under this format: name + # + num
	 * @param cur	cur value of the cell, under this format: name + # + num
	 * @param n		new value of the cell, under this format: name + # + num
	 * @return
	 */
	private JMenuItem createNewItem(String n) {
		JMenuItem item = new JMenuItem();
		item.setText(n);
		item.addActionListener(new java.awt.event.ActionListener() {					
			public void actionPerformed(java.awt.event.ActionEvent evt) {				
				int row = jTable1.getSelectedRow();
				int col = jTable1.getSelectedColumn();
				String cur = myTable1.model.data[row][col].toString(); 
				
				if(!cur.equals(n)) {
					if(cur != " ") {					
						countMap.put(n, countMap.get(n) + 1);
						countMap.put(cur, countMap.get(cur) - 1);
						
						if(countMap.get(cur) == 0) {
							String pureName = getName(cur);
							map.put(pureName, map.get(pureName) - 1);
							int index1 = getIndex(cur, popupMenu1.getComponents());
							JMenuItem curItem1 = (JMenuItem) popupMenu1.getComponents()[index1];
							popupMenu1.remove(curItem1);
							
							int index2 = getIndex(cur, popupMenu2.getComponents());
							JMenuItem curItem2 = (JMenuItem) popupMenu2.getComponents()[index2];						
							popupMenu2.remove(curItem2);
							
						}						
					}					
				}
				else {
					System.out.print("==");
				}		
				myTable1.model.mySetValueAt(n, row, col);
				jTable1.repaint();
			}
		});		
		return item;
	}
	
	/**
	 * creates normal item when initializing the popupMenu.
	 * @param s the source of the item name.
	 * @return
	 */
	private JMenuItem createNormalItem(String s) {
		JMenuItem item = new JMenuItem();
		item.setText("New " + s);
		item.addActionListener(new java.awt.event.ActionListener() {			
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				int row = jTable1.getSelectedRow();
				int col = jTable1.getSelectedColumn();
				String value;
				
				/*
				 *  At the beginning, each cell is " ", that is, before we choose the menuItem,
				 *  the current value is " ", what to do is add items into the two maps, and add
				 *  separator line and the new item into the popupMenu, in addition, store into 
				 *  the result components list, which used for the second table.
				 */
				
				if (myTable1.model.data[row][col].toString().equals(" ")) {
					// add into the 1st map
					if(!map.containsKey(s))						
						map.put(s, 1);
					else
						map.put(s, map.get(s) + 1);
					
					// add into the 2nd map, that is, the count map.
					value = s + " #" + map.get(s).toString();					
					if(!countMap.containsKey(value))
						countMap.put(value, 1);
					else
						countMap.put(value, countMap.get(value) + 1);
					
					// add separator line
					if (resultComponents.size() == 0) {
						popupMenu1.addSeparator();
						popupMenu2.addSeparator();
					}
											
					// add into result components list, used for the second table.
					resultComponents.add(value);
					// add item into the popupMenu.
					popupMenu1.add(createNewItem(value));	
					popupMenu2.add(createNewItem(value));
				}
				
				/*
				 *  Already have chosen one item, but need to change to another one.
				 *  that is, the current value of the cell is not null. What to do is:
				 *  1. the number of the new item add 1;
				 *  2. the count of the new item with number add 1;
				 *  3, the count of the cur item with number minus 1, if the count is 0, then
				 *     the number of the cur item minus 1, delete cur item from the popupmenu.
				 */
				else {
					map.put(s, map.get(s) + 1);	
					String cur = myTable1.model.data[row][col].toString();
					countMap.put(cur, countMap.get(cur) - 1);
					if(countMap.get(cur) == 0) {
						String pureName = getName(cur);
						map.put(pureName, map.get(pureName) - 1);
						int index1 = getIndex(cur, popupMenu1.getComponents());
						JMenuItem curItem1 = (JMenuItem) popupMenu1.getComponents()[index1];
						popupMenu1.remove(curItem1);
						
						int index2 = getIndex(cur, popupMenu2.getComponents());
						JMenuItem curItem2 = (JMenuItem) popupMenu2.getComponents()[index2];						
						popupMenu2.remove(curItem2);
					}					
					value = s + " #" + map.get(s).toString();					
					if(!countMap.containsKey(value))
						countMap.put(value, 1);
					else
						countMap.put(value, countMap.get(value) + 1);					
					popupMenu1.add(createNewItem(value));
					popupMenu2.add(createNewItem(value));
				}

				// reset the value of the table cell
				myTable1.model.mySetValueAt(value, row, col);							
				jTable1.repaint();
			}
		});
		return item;

	}

	// gets the pure name, that is, the string without "#" and "number"
	private String getName(String s) {
		if (s == null)
			return " ";
		int index = s.indexOf('#');
		return s.substring(0, index - 1);
	}

	// gets the index of the menuItem in the popupMenu
	private int getIndex(String s, Component[] list) {
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

	// initial several popupMenus which will be used frequently.
	private void initialPopupMenu() {
		// adds elements into separator menu
		for (int i = 0; i < sepComponents.length; i++) {
			separatorMenu.add(createNormalItem(sepComponents[i]));
		}

		// adds elements into popupMenu with separator menu
		JMenuItem nullItem1 = createNullItem();
		popupMenu1.add(nullItem1);
		popupMenu1.add(separatorMenu);
		for (int i = 0; i < components.length; i++) {
			popupMenu1.add(createNormalItem(components[i]));
		}

		// adds elements into popupMenu without separator menu
		JMenuItem nullItem2 = createNullItem();
		popupMenu2.add(nullItem2);
		for (int i = 0; i < components.length; i++) {			
			popupMenu2.add(createNormalItem(components[i]));
		}

		// adds elements into popupMenu
		JMenuItem nullItem3 = createNullItem();
		popupMenu3.add(nullItem3);
		for (int i = 0; i < 2; i++) {
			popupMenu3.add(createNormalItem(components[i]));
		}

	}

	private void initialActionLiseners() {
		jTable1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int col = jTable1.columnAtPoint(jTable1.getMousePosition());
				if (col == 1)
					popupMenu1.show(jTable1, e.getX(), e.getY());
				if (col == 2)
					popupMenu2.show(jTable1, e.getX(), e.getY());
				if (col == 3)
					popupMenu3.show(jTable1, e.getX(), e.getY());
				;
				jTable1.repaint();
			}
		});

	}


	private void initialLayout(GridBagConstraints gbc) {

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		// gbc.gridwidth = 3;
		gbc.gridheight = 1;
		add(scrollPane1, gbc);

		gbc.gridy = 2;
		add(label, gbc);

		gbc.gridy = 3;
		add(scrollPanel2, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridheight = 1;
		gbc.gridy = 4;
		add(buttonHelp, gbc);
		gbc.gridy = 5;
		add(buttonOK, gbc);

	}
	
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
	
	
}
