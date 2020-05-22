package Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import AWS.PanelManager;
import Tables.RunoffTable;


public class RunoffPanel extends JPanel{
	
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	
	
String[] columnName = {" ", "Pervious","Impervious","Monthly Totals"};
	
    // the data from the database
	Object[][] tableData1 = { 	{ "January", "1.00", "0.00" , "0.00"}, 
		{ "February", "0.00", "0.00" , "0.00" },
		{ "March", "0.00", "0.00" , "0.00"}, 
		{ "April", "0.00", "0.00" , "0.00"}, 
		{ "May", "0.00", "0.00" , "0.00"},
		{ "June", "0.00", "0.00" , "0.00"}, 
		{ "July", "0.00", "0.00" , "0.00"}, 
		{ "August", "0.00", "0.00" , "0.00"},
		{ "September", "0.00", "0.00" , "0.00"}, 
		{ "October", "0.00", "0.00" , "0.00"}, 
		{ "November", "0.00", "0.00" , "0.00"},
		{ "December", "0.00", "0.00" , "0.00"}};
	// the data for customer 
	Object[][] tableData2 = { 	{ "January", "0.00", "0.00" , "0.00"}, 
								{ "February", "0.00", "0.00" , "0.00" },
								{ "March", "0.00", "0.00" , "0.00"}, 
								{ "April", "0.00", "0.00" , "0.00"}, 
								{ "May", "0.00", "0.00" , "0.00"},
								{ "June", "0.00", "0.00" , "0.00"}, 
								{ "July", "0.00", "0.00" , "0.00"}, 
								{ "August", "0.00", "0.00" , "0.00"},
								{ "September", "0.00", "0.00" , "0.00"}, 
								{ "October", "0.00", "0.00" , "0.00"}, 
								{ "November", "0.00", "0.00" , "0.00"},
								{ "December", "0.00", "0.00" , "0.00"}};
 
	
	JLabel labelMethods;
    JLabel label_1, label_2;
    JRadioButton r1, r2;
    ButtonGroup bg;
    JLabel labelPWA, labelPCN1, labelPCN2, labelIA;
    JLabel label_3; JLabel label_4;
    JLabel labelRV;
    JLabel label_5;
    JLabel labelWarning;
    
    JTextField textPWA, textPCN1, textPCN2, textIA;
    JTextField text_1,text_2,text_3;
    
    RunoffTable myTable1;
    JTable databaseTable;
    RunoffTable myTable2;
    JTable customerTable;
    JScrollPane scrollPane;
	
	JButton buttonHelp;
	JButton buttonOK;
	
	JPanel panel;
	JPanel childPanel_1, childPanel_2;
	
	GridBagConstraints gc;

	
    public RunoffPanel(PanelManager pm) {
    	
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
    	labelMethods = new JLabel("Methods for determining monthly runoff volumes:");
    	label_1 = new JLabel("1.) Calculate volumes from climate and watershed data.");
    	label_1.setFont(new Font(label_1.getFont().getName(),Font.PLAIN,12));
    	label_2 = new JLabel("2.) Enter runoff volumes directly in the table on the right.");
    	label_2.setFont(new Font(label_2.getFont().getName(),Font.PLAIN,12));
    	childPanel_1 = buildPanel_1();
    	childPanel_2 = buildPanel_2();
    	labelRV = new JLabel("Runoff Volumes (1000 cu.ft)");
    	
    	myTable1 = new RunoffTable();
    	databaseTable = myTable1.buildMyTable(columnName, tableData1);
    	databaseTable.setRowHeight(20);
    	myTable2 = new RunoffTable();
    	customerTable = myTable2.buildMyTable(columnName, tableData2);
    	customerTable.setRowHeight(20);
    	scrollPane = new JScrollPane(databaseTable);
		scrollPane.setPreferredSize(new Dimension(310, 283));
		
		label_5 = new JLabel("25-Yr 24-Hr Storm Runoff:");
		text_1 = new JTextField("0.00");
		text_1.setPreferredSize(new Dimension(63,25));
		text_1.setBackground(Color.lightGray);
		text_2 = new JTextField("0.00");		
		text_2.setPreferredSize(new Dimension(73,25));
		text_2.setBackground(Color.lightGray);
		text_3 = new JTextField("0.00");
		text_3.setPreferredSize(new Dimension(102,25));
		text_3.setBackground(Color.cyan);
		text_3.setEditable(false);
		
		labelWarning = new JLabel("<html> WARNING: The volumes computed by the program are conservative over-estimates. <br>"
										+ "The user is encouraged to use a method outside of the program to compute runoff <br>"
										+ "volumes for larger watersheds and where precision is vital. Methods for computing <br>"
										+ "monthly runoff volumes include the NEH-4 stream gauge procedure and SPAW. </html>");
		buttonHelp = new JButton("Help");
		buttonHelp.setPreferredSize(new Dimension( 60, 25));
		buttonOK = new JButton("OK");
		buttonOK.setPreferredSize(new Dimension( 60, 25));
    }
    private JPanel buildPanel_1() {
    	JPanel p = new JPanel();
    	r1 = new JRadioButton("Calculate Monthly Runoff Volumes");
    	r1.setSelected(true);
    	r2 = new JRadioButton("Enter Monthly Runoff Volumes");
    	//bg = new ButtonGroup();
		//bg.add(r1);
		//bg.add(r2);
		
		p.setLayout(new GridLayout(2, 1));
		p.add(r1);
		p.add(r2);
		p.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Runoff Volume Method"));
		p.setPreferredSize(new Dimension(300, 80));
    	return p;
    }
    private JPanel buildPanel_2() {
    	JPanel p = new JPanel();
    	labelPWA = new JLabel("Pervious Watershed Area: ");
    	labelPCN1 = new JLabel("<html> Pervious Curve Number (1-day) <br>"
    			+ "for 25-Yr 24-Hr Storm Runoff: </html>");
    	labelPCN2 = new JLabel("<html> Pervious Curve Number  <br>"
    			+ "for Monthly Runoff: </html>");
    	labelIA = new JLabel("Impervious Area (roofs, slabs, etc): ");
    	label_3 = new JLabel("acres");
    	label_4 = new JLabel("sq.ft.");
    	
    	textPWA = new JTextField(" ");
    	textPWA.setPreferredSize(new Dimension(50,25));
    	textPCN1 = new JTextField(" ");
    	textPCN1.setPreferredSize(new Dimension(50,25));
    	textPCN2 = new JTextField(" ");
    	textPCN2.setPreferredSize(new Dimension(50,25));
    	textIA = new JTextField(" ");
    	textIA.setPreferredSize(new Dimension(50,25));
    	
    	p.setLayout(new GridBagLayout());
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.anchor = GridBagConstraints.NORTHWEST;
		gc1.insets = new Insets(5, 2, 0, 2);
		
		// first row
		gc1.gridx = 0;
		gc1.gridy = 0;
		p.add(labelPWA, gc1);
		gc1.gridx = 2;
		p.add(textPWA, gc1);
		gc1.gridx = 4;
		p.add(label_3, gc1);
		
		// second row
		gc1.gridx = 0;
		gc1.gridy = 1;
		p.add(labelPCN1, gc1);
		gc1.gridx = 2;
		p.add(textPCN1, gc1);
		
		// third row
		gc1.gridx = 0;
		gc1.gridy = 2;
		p.add(labelPCN2, gc1);
		gc1.gridx = 2;
		p.add(textPCN2, gc1);
    	
		// fourth row
		gc1.gridx = 0;
		gc1.gridy = 3;
		p.add(labelIA, gc1);
		gc1.gridx = 2;
		p.add(textIA, gc1);
		gc1.gridx = 4;
		p.add(label_4, gc1);
		
		p.setPreferredSize(new Dimension(300, 140));
    	return p;
    }
    
    private void initialActionLiseners() {
    	
    	r1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r1.setSelected(true);
					r2.setSelected(false);
					scrollPane.setViewportView(databaseTable);
					gc.gridx = 0;
					gc.gridy = 4;
					gc.gridheight = 1;
					add(childPanel_2, gc);				
					updateUI();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		r2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					r2.setSelected(true);
					r1.setSelected(false);
					scrollPane.setViewportView(customerTable);
					System.out.print(getComponentZOrder(childPanel_2));
					remove(childPanel_2);

					updateUI();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
    	
    	
    	text_1.getDocument().addDocumentListener(new DocumentListener(){   	  
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				double v1 = Double.parseDouble(text_1.getText().toString());
    	    	double v2 = Double.parseDouble(text_2.getText().toString());

    	    	String s = Double.toString(v1 + v2);
    	    	text_3.setText(s);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
    	});
    	
    	text_2.getDocument().addDocumentListener(new DocumentListener(){   	  
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub			
				double v1 = Double.parseDouble(text_1.getText().toString());
    	    	double v2 = Double.parseDouble(text_2.getText().toString());

    	    	String s = Double.toString(v1 + v2);
    	    	text_3.setText(s);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
    	});
    	
    	
    }
    private void initialLayout(GridBagConstraints gbc) {
    	gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(5, 10, 5, 5);
		
		// first column
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		add(labelMethods, gbc);			
		gbc.gridy = 1;
		add(label_1, gbc);
		gbc.gridy = 2;
		add(label_2, gbc);
		gbc.gridy = 3;
		gbc.gridheight = 1;
		add(childPanel_1, gbc);
		gbc.gridy = 4;
		gbc.gridheight = 1;
		add(childPanel_2, gbc);
		gbc.gridy = 6;
		gbc.gridheight = 2;
		gbc.gridwidth = 5;
		add(labelWarning, gbc);
		
		// second column
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 4;
		add(labelRV, gbc);
		gbc.gridy = 1;
		gbc.gridheight = 4;
		gbc.gridwidth = 5;
		add(scrollPane, gbc);
		
		//gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridheight = 1;
		gbc.gridwidth = 6;
		add(label_5, gbc);	
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(5, 82, 5, 0);
		gbc.gridx = 3;
		gbc.gridwidth = 1;
		add(text_1, gbc);
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.gridx = 4;
		add(text_2, gbc);
		gbc.gridx = 5;
		add(text_3, gbc);
		
		// button
		gbc.insets = new Insets(5, 10, 5, 5);
		gbc.gridx = 5;
		gbc.gridy = 6;
		add(buttonHelp, gbc);
		gbc.gridy = 7;
		add(buttonOK, gbc);	
		
    }
    
	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
