package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	}

	private void initialActionLiseners() {

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
