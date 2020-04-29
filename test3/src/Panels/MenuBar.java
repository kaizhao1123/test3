package Panels;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import AWS.PanelManager;

public class MenuBar extends JPanel{
	
	//private SaveData savedata;
	//private JFileChooser fc;
	//private InputData data;
	PanelManager PM;
	Container[] containers;
	//private FileFilter fileFilter;
	
	public MenuBar(PanelManager pm, Container[] c) {
		//this.savedata = saveData;
		containers = c;
		PM = pm;
		//fc = new JFileChooser();
		//fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//fileFilter = new FileTypeFilter(".efm", "Estimated Runoff and Peak Discharge");
		//fc.addChoosableFileFilter(fileFilter);
		//fc.setAcceptAllFileFilterUsed(true);
		
		JMenuBar menuBar = createFileMenu();
		setLayout(new BorderLayout());
		add(menuBar);
	}
	
	private JMenuBar createFileMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu tools = new JMenu("Tools");
		JMenu help = new JMenu("Help");
		
		/* File Items */
		JMenuItem newFile = new JMenuItem("New");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem recaclulate = new JMenuItem("Recalculate");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem print = new JMenuItem("Print");
		JMenuItem exit = new JMenuItem("Exit");
		
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(tools);
		menuBar.add(help);
		
		return menuBar;
		
	}
	
	

}
