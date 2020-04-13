package test3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import test3.InputData.stationInfo;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public JPanel mainPanel;     //the main panel include the MenuBar and tabbedPane
	public JTabbedPane tabbedPane;
	
	public StartPanel startPanel;
	public ClimatePanel climatePanel;
	public AnimalPanel animalPanel;
	public AddAnimalDialog addAnimalDialog;
	
	
	public MenuBar menubar;		
	public InputData excelData;			// Be uesed for tabbedPanel 
	public InputData excelData_1;   	// Be used for menubar

	
	public MainFrame(InputData data, int width, int height) throws IOException {
		super("AWM"); 												//the title of the window		
		mainPanel = new JPanel();   	                                //include the menubarPane and tabbedPane		
		tabbedPane = new JTabbedPane();                             //include the list of panels in tab				
		excelData = data;											
			
		createTabbedPanes(tabbedPane, width, height);	
		createMainPane(mainPanel, tabbedPane, startPanel, climatePanel, animalPanel);		
		add(mainPanel);	
	}
	
	private void createMainPane(JPanel main, JTabbedPane tab, StartPanel start, ClimatePanel climate, AnimalPanel animal) {
		Container[] containers =  {start, climate, animal};		
		menubar = new MenuBar(excelData_1, containers) ;		
		main.setLayout(new BorderLayout());
		main.add(menubar, BorderLayout.NORTH);
		main.add(tabbedPane);
	}
	
	private void createTabbedPanes(JTabbedPane pane, int width, int height) throws IOException {
		
		
		/***k***
		 *  build the introduction panel
		 ***z***/
		
		Font font1 = new Font("Arial Narrow", Font.PLAIN, 13);				
				
		String introductionLabel1 = "AWM";		
		String introductionLabel2 = "Animal Waste Management";
		String introductionLabel3 = "Version 2.4.1";
		String introductionLabel4 = "Database Version 2.81";
		
		String introductionLabel5 = 
			"<html>	--- As with any engineering software, use of AWM, or any information on this software package, <br/>"
				+ "is not a replacement for a professinal engineering evaluation of the situation. <br/>"
				+ "<br/>"
				+ "	---	This software is meant for supporting you in evaluating and designing animal waste storage <br/>"
				+ "structures and is in no way a replacement for a thoroug engineering revies.<br/>"
				+ "<br/>"
				+ "	---	A professional engineer, licensed in the appropriate field of engineering, should always conduct <br/>"
				+ "the final evaluation of the application; and that individual, not NRCS or its employees and representatives, <br/>"
				+ "is responsible for the final engineering design and performance of the application at hand. <br/>"
				+ "<br/>"
				+ "	---	This software is supplied 'as is', and NRCS specifically excludes warranties, expressed or implied, <br/>"
				+ "as to the accuracy of the data generated by the use of this program and does not assume liability for<br/>"
				+ "any losses or damage resulting from the use of this program or the infromation supplied with it.<br/>"
				+ "<br/>"
				+ "	---	NRCS does not warrant, guarantee, or make any representations regarding the use, or the results <br/>"
				+ "of use, of this software or accompanying information in terms of its accuracy, validty,completeness,<br/>"
				+ "applicability, or compliance with any regulatory code or governmental laws and regulations.<br/>"
				+ "<br/>"
				+ "	---	Use of this software and information involves some risks, and the entire risk as to the results and<br/>"
				+ "performance of the software and accompanying information is the user's responsibility and liability.</html> ";
		
		//BufferedImage myPicture = ImageIO.read(new File("C:/Users/Kai Zhao/Desktop/1.png"));
		
		//JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		
		JLabel label1 = new JLabel(introductionLabel1);
		label1.setFont(new Font("Arial Narrow",Font.BOLD,30));
		JLabel label2 = new JLabel(introductionLabel2);
		JLabel label3 = new JLabel(introductionLabel3);
		JLabel label4 = new JLabel(introductionLabel4);
		JLabel label5 = new JLabel(introductionLabel5);

		
		JPanel introPanel = new JPanel();
		introPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		/******** INTRO LABELS ******/
		//gc.anchor = GridBagConstraints.LINE_END;
		//gc.fill = GridBagConstraints.HORIZONTAL;
		//gc.anchor = GridBagConstraints.LINE_START;
		
		//gc.gridheight = 5;
		//gc.gridx = 0;
		//gc.gridy = 0;
		//introPanel.add(picLabel, gc);
		
		gc.gridheight = 1;
		gc.gridx = 1;
		gc.gridy = 0;
		introPanel.add(label1, gc);

		gc.gridx = 1;
		gc.gridy = 1;
		introPanel.add(label2, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		introPanel.add(label3, gc);
				
		gc.gridx = 1;
		gc.gridy = 3;
		introPanel.add(label4, gc);
		
		gc.insets = new Insets(20,0,0,0);
		gc.gridwidth = 2;
		gc.gridx = 0;
		gc.gridy = 6;
		introPanel.add(label5, gc);
		
		
		/***** FORMATTING ROW *********/
		/*
		JLabel fillSpaceLabel = new JLabel(" ");
		gc.weighty = 10;
		gc.weightx = 5;
		gc.gridx = 0;
		gc.gridy = 3;
		introPanel.add(fillSpaceLabel, gc);*/
		
		/***
		 * add the function panels to the tabbedPane
		 */
		

		startPanel = new StartPanel(excelData);
		startPanel.setParent(this);
					
		pane.addTab("Introduction", introPanel);
		pane.addTab("Start", startPanel);		
	}
}
