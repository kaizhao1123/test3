package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import Controller.PanelManager;
import Model_Entity.AnimalInfo;
import Model_Entity.AnimalPanelOutputElement;
import Model_Tables.AnimalsTable;
/**
 * The purpose of this class is to build the animal panel.
 * In this panel, the user can choose different kinds of animals,
 * or create new kind of animal. What the user do is just input the 
 * quantity and the weight of the animal after choosing the animal. 
 * The user can also set up the property of the animals based on 
 * user's experience. 
 * @author Kai Zhao
 *
 */
public class AnimalsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	LocationsPanel locationsPanel;
	AddAnimalDialog newAnimalDialog;

	Font font = new Font("Arial Narrow", Font.PLAIN, 13);
	
	/**********************************************************
	 *  declare the data structure used in the panel
	 */
	
	ArrayList<AnimalInfo> animalData; 				// store the input data filtered by source
	HashMap<String, JList<String>> choicesMap; 		// K is the name of animal type, V is the original JList respond the name
	HashMap<String, JList<String>> selectedMap;		// K is the name of animal, V is the JList where the animal come from.
	String[] animalTypes; 							// store the types of the animal
	AnimalInfo newAnimalInfo; 						// to build new animal info
	
	// the animals showed in the table, including all selected and new build animals
	ArrayList<AnimalInfo> animalInTable; 			
	
	// store the output of panel: the name, and part of data of each animal(manure and ts).
	public ArrayList<AnimalPanelOutputElement> animalPanelOutput;  
	
	// the header of table
	String[]  columnNamess = { "<html> Animal  </html>", 
			"<html>Animal <br> (type) </html>", "<html>Quantity </html>", "<html>Weight <br> (lbs) </html>",
			"<html>Manure <br> (cu.ft/day/AU) </html>", "<html> VS <br> (lbs/day/AU) </html>",
			"<html> TS <br> (lbs/day/AU) </html>", "<html> Manure <br> (cu.ft/day) </html>",
			"<html> VS <br> (lbs/day) </html>", "<html> TS <br> (lbs/day) </html>",
			"<html>Manure <br> (lbs/day) </html>" };
	Object[][] dataa;        // the data of table
	
	JList<String> choicesList; // to display the choices, corresponding to the choicesScrollPane
	JList<String> selectedList; // to display the selected, corresponding to the selectedScrollPane
	DefaultListModel<String> selectedModel;
	
	
	/**********************************************************
	 * declare the elements shown in the panel
	 */	
	
	JLabel jl1, jl2, jl3, jl4, jl5, jl6;
	JScrollPane choicesScrollPane;
	JScrollPane selectedScrollPane;
	JScrollPane tableScrollPane;
	AnimalsTable animalsTable;
	JTable jTable;
	JComboBox<String> comboBoxAnimalType;
	JButton buttonAdd;
	JButton buttonRemove;
	JButton buttonAddAll;
	JButton buttonRemoveAll;
	JButton buttonNewAnimal;
	JButton buttonDeleteRow;
	JButton buttonOK;
	JButton buttonHelp;
	GridBagConstraints gc;
	
	/**
	 * The constructor of this class.
	 * @param pm	The "controller" of this project.
	 */
	public AnimalsPanel(PanelManager pm) {
		panelManager = pm;
		initialData();
		initialElements();
		initialListeners();
		initialLayout();
	}

	// initials all data structure, mainly to get the input data.
	private void initialData() {		
		// get animal data
		animalData = panelManager.filterByDataSource(panelManager.startPanelOutput[0], panelManager.allAnimalData);
		choicesMap = new HashMap<>();
		selectedMap = new HashMap<>();
		animalInTable = new ArrayList<AnimalInfo>();
		animalTypes = getTypes(animalData);

		// build several lists to store different type of animal
		ArrayList<AnimalInfo> beef = filterByType("Beef", animalData);
		ArrayList<AnimalInfo> dairy = filterByType("Dairy", animalData);
		ArrayList<AnimalInfo> goat = filterByType("Goat", animalData);
		ArrayList<AnimalInfo> horse = filterByType("Horse", animalData);
		ArrayList<AnimalInfo> poultry = filterByType("Poultry", animalData);
		ArrayList<AnimalInfo> sheep = filterByType("Sheep", animalData);
		ArrayList<AnimalInfo> swine = filterByType("Swine", animalData);
		ArrayList<AnimalInfo> veal = filterByType("Veal", animalData);

		// build each Jlist for each type of animal, and store in the map
		DefaultListModel<String> beefModel = new DefaultListModel<>();
		InputElementIntoModel(beef, beefModel);
		JList<String> beefList = new JList<String>(beefModel);
		beefList.setFont(font);
		choicesMap.put("beef", beefList);

		DefaultListModel<String> dairyModel = new DefaultListModel<>();
		InputElementIntoModel(dairy, dairyModel);
		JList<String> dairyList = new JList<String>(dairyModel);
		dairyList.setFont(font);
		choicesMap.put("dairy", dairyList);

		DefaultListModel<String> goatModel = new DefaultListModel<>();
		InputElementIntoModel(goat, goatModel);
		JList<String> goatList = new JList<String>(goatModel);
		goatList.setFont(font);
		choicesMap.put("goat", goatList);

		DefaultListModel<String> horseModel = new DefaultListModel<>();
		InputElementIntoModel(horse, horseModel);
		JList<String> horseList = new JList<String>(horseModel);
		horseList.setFont(font);
		choicesMap.put("horse", horseList);

		DefaultListModel<String> poultryModel = new DefaultListModel<>();
		InputElementIntoModel(poultry, poultryModel);
		JList<String> poultryList = new JList<String>(poultryModel);
		poultryList.setFont(font);
		choicesMap.put("poultry", poultryList);

		DefaultListModel<String> sheepModel = new DefaultListModel<>();
		InputElementIntoModel(sheep, sheepModel);
		JList<String> sheepList = new JList<String>(sheepModel);
		sheepList.setFont(font);
		choicesMap.put("sheep", sheepList);

		DefaultListModel<String> swineModel = new DefaultListModel<>();
		InputElementIntoModel(swine, swineModel);
		JList<String> swineList = new JList<String>(swineModel);
		swineList.setFont(font);
		choicesMap.put("swine", swineList);

		DefaultListModel<String> vealModel = new DefaultListModel<>();
		InputElementIntoModel(veal, vealModel);
		JList<String> vealList = new JList<String>(vealModel);
		vealList.setFont(font);
		choicesMap.put("veal", vealList);

		// initial beef as the first display data in choices
		choicesList = beefList;
	}

	// initials all elements in this panel
	private void initialElements() {
		// initial the labels and buttons
		jl1 = new JLabel("Choices");
		jl2 = new JLabel("Animal Type:");
		jl3 = new JLabel("Selected");
		jl4 = new JLabel("AU = Animal Unit");
		jl5 = new JLabel("VS = Volatile Solids");
		jl6 = new JLabel("TS = Total Solids");
		jl1.setFont(font);
		jl2.setFont(font);
		jl3.setFont(font);
		jl4.setFont(font);
		jl5.setFont(font);
		jl6.setFont(font);
		buttonAdd = new JButton("Add >");
		buttonAdd.setFont(font);
		buttonAdd.setPreferredSize(new Dimension(100, 25));
		buttonRemove = new JButton("< Remove");
		buttonRemove.setFont(font);
		buttonRemove.setPreferredSize(new Dimension(100, 25));
		buttonAddAll = new JButton("Add All >>");
		buttonAddAll.setFont(font);
		buttonAddAll.setPreferredSize(new Dimension(100, 25));
		buttonRemoveAll = new JButton("<<Remove All");
		buttonRemoveAll.setFont(font);
		buttonRemoveAll.setPreferredSize(new Dimension(100, 25));
		buttonNewAnimal = new JButton("New Animal");
		buttonNewAnimal.setFont(font);
		buttonNewAnimal.setPreferredSize(new Dimension(100, 25));
		buttonDeleteRow = new JButton("delete Row");
		buttonDeleteRow.setFont(font);
		buttonDeleteRow.setPreferredSize(new Dimension(100, 25));
		buttonOK = new JButton("ok");
		buttonOK.setFont(font);
		buttonOK.setPreferredSize(new Dimension(100, 25));
		buttonHelp = new JButton("help");
		buttonHelp.setFont(font);
		buttonHelp.setPreferredSize(new Dimension(100, 25));
		
		// initial beef in choices
		choicesScrollPane = new JScrollPane(choicesList);
		choicesScrollPane.setPreferredSize(new Dimension(120, 160));

		// initial empty in selected
		selectedModel = new DefaultListModel<>();
		selectedList = new JList<String>(selectedModel);
		selectedList.setFont(font);
		selectedScrollPane = new JScrollPane(selectedList);
		selectedScrollPane.setPreferredSize(new Dimension(120, 160));

		// initial animalType
		comboBoxAnimalType = new JComboBox<>(animalTypes);
		comboBoxAnimalType.setFont(font);
		comboBoxAnimalType.setPreferredSize(new Dimension(100, 20));
		comboBoxAnimalType.setSelectedIndex(0);

		// initial table
		animalsTable = new AnimalsTable();
		jTable = animalsTable.buildMyTable(columnNamess, dataa);
		jTable.getTableHeader().setPreferredSize(new Dimension(10, 35));
		tableScrollPane = new JScrollPane(jTable);
		tableScrollPane.setPreferredSize(new Dimension(800, 120));
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		tableScrollPane.setViewportBorder(border);
		tableScrollPane.setBorder(border);
		
		// initial gridBagConstraints
		gc = new GridBagConstraints();
	}

	// initials all listeners of this panel
	private void initialListeners() {		
		// initial actionlistener of JCombox animalType
		comboBoxAnimalType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String data = comboBoxAnimalType.getItemAt(comboBoxAnimalType.getSelectedIndex()).toString().toLowerCase();
				choicesList = choicesMap.get(data);
				choicesScrollPane.setViewportView(choicesList);
			}
		});

		/*
		 *  "add" listener, move the target animal from the "choiceList" to "selectedList",
		 *  and add into the table.
		 */
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jTable.isEditing())
	            	  jTable.getCellEditor().stopCellEditing(); 
				addToSelectedList();				
			}
		});
		/*
		 * "remove" listener, move the target animal from the "selectedList" to "choiceList",
		 * and remove from the table
		 */
		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jTable.isEditing())
	            	  jTable.getCellEditor().stopCellEditing(); 
				removeFromSelectedList();
			}
		});
		/*
		 * "add all" listener, move all animals in the "choicesList" to " selectedList",
		 * and add into the table.
		 */
		buttonAddAll.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if (jTable.isEditing())
	            	  jTable.getCellEditor().stopCellEditing(); 
				addAllToSelectedList();
			}
		});
		/*
		 * "remove all" listener, move all animals in the "selectedList" back to "choicesList",
		 * and remove from the table.
		 */
		buttonRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jTable.isEditing())
	            	  jTable.getCellEditor().stopCellEditing(); 
				removeAllFromSelectedList();
			}
		});
		// "new animal" listener, creates new animalInfo, and add into the table
		buttonNewAnimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jTable.isEditing())
	            	  jTable.getCellEditor().stopCellEditing(); 
				String source = panelManager.startPanelOutput[0];
				String station = panelManager.startPanelOutput[1];
				newAnimalDialog = new AddAnimalDialog(animalsTable, jTable, source, station);
				newAnimalDialog.setParent(parent);
			}
		});
		// "delete row" listener, delete the target row from the table.
		buttonDeleteRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jTable.isEditing())
	            	  jTable.getCellEditor().stopCellEditing(); 
				int row = jTable.getSelectedRow();
				if (row != -1) {
					String item = animalsTable.model.data[row][0].toString();
					DefaultListModel<String> model = (DefaultListModel<String>) selectedList.getModel();
					if (model.contains(item)) {
						model.remove(model.indexOf(item));
						JList<String> oldList = selectedMap.get(item);
						DefaultListModel<String> oldModel = (DefaultListModel<String>) oldList.getModel();
						AnimalInfo ele = getInfoByName(item, animalData);
						addToModel(ele, oldModel, animalData);
						selectedMap.remove(item, oldList);
						deleteTableRow(item);											
					} else {
						if (!item.equals("Total"))
							deleteTableRow(item);
					}
				}
			}
		});
		// after getting the output, stores the output into the "controller", and creates the next panel
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pane = parent.tabbedPane;
				if (jTable.isEditing())
	            	  jTable.getCellEditor().stopCellEditing(); 
				if (locationsPanel == null) {					
					getOutput();
					if(!animalPanelOutput.isEmpty()) {
						panelManager.storeAnimalPanelOutput(animalPanelOutput);
						locationsPanel = new LocationsPanel(panelManager);
						locationsPanel.setParent(parent);
						pane.add("locations", locationsPanel);

						if (parent.startPanel.periodDialog.secondOption == true) {
							locationsPanel.update2();
						}
						pane.setSelectedIndex(pane.indexOfTab("locations"));
					}
					else
						JOptionPane.showMessageDialog(null,"No animals selected!");
					
				} else {
										
				}
				
			}
		});
	}

	// initials the layout of this panel
	private void initialLayout() {
		setLayout(new GridBagLayout());
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.insets = new Insets(0, 10, 0, 10);

		gc.gridx = 0;
		gc.gridy = 0;
		add(jl1, gc);
		gc.gridx = 2;
		add(jl2, gc);
		gc.gridx = 4;
		add(jl3, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridheight = 5;
		add(choicesScrollPane, gc);
		gc.gridx = 4;
		add(selectedScrollPane, gc);
		gc.gridx = 2;
		gc.gridheight = 1;
		add(comboBoxAnimalType, gc);

		gc.insets = new Insets(2, 10, 2, 10);
		gc.gridx = 2;
		gc.gridy = 2;
		add(buttonAdd, gc);
		gc.gridy = 3;
		add(buttonRemove, gc);
		gc.gridy = 4;
		add(buttonAddAll, gc);
		gc.gridy = 5;
		add(buttonRemoveAll, gc);

		gc.insets = new Insets(30, 0, 0, 0);
		gc.gridx = 4;
		gc.gridy = 6;
		add(buttonNewAnimal, gc);
		gc.gridx = 5;
		add(buttonDeleteRow, gc);

		gc.insets = new Insets(5, 0, 0, 0);
		gc.gridx = 0;
		gc.gridy = 7;
		gc.gridwidth = 8;
		gc.gridheight = 2;
		add(tableScrollPane, gc);

		gc.gridy = 9;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.insets = new Insets(0, 10, 0, 10);
		add(jl4, gc);
		gc.gridy = 10;
		add(jl5, gc);
		gc.gridy = 11;
		add(jl6, gc);
		gc.gridx = 5;
		add(buttonHelp, gc);
		gc.gridx = 6;
		add(buttonOK, gc);
	}
	
	// gets the output of this panel
	private void getOutput() {
		animalPanelOutput = new ArrayList<>();
		for(int i = 0; i < animalInTable.size(); i++) {
			String quantity = animalsTable.model.data[i][2].toString();						
			if(Double.parseDouble(quantity) > 0) {
				String name = animalsTable.model.data[i][0].toString();
				String[] data = new String[2];
				String manure = animalsTable.model.data[i][7].toString();
				String ts = animalsTable.model.data[i][9].toString();
				data[0] = manure;
				data[1] = ts;
				AnimalPanelOutputElement ele = new AnimalPanelOutputElement(name, data);
				animalPanelOutput.add(ele);
			}				
		}				
	}
	// update output
	public void updateOutput() {
		getOutput();
		panelManager.storeAnimalPanelOutput(animalPanelOutput);
	}
	

	//move the target animal from the "choiceList" to "selectedList", and add into the table.
	private void addToSelectedList() {
		int index = choicesList.getSelectedIndex();
		if (index >= 0) {
			DefaultListModel<String> model = (DefaultListModel<String>) choicesList.getModel();
			String item = model.getElementAt(index);
			model.remove(index);
			selectedModel.addElement(item);
			selectedMap.put(item, choicesList);
			addTableRow(item);			
		}
	}

	//move the target animal from the "selectedList" to "choiceList", and remove from the table.
	private void removeFromSelectedList() {
		int index = selectedList.getSelectedIndex();
		if (index >= 0) {
			DefaultListModel<String> model = (DefaultListModel<String>) selectedList.getModel();
			String item = model.getElementAt(index);
			model.remove(index);
			JList<String> oldList = selectedMap.get(item);
			DefaultListModel<String> oldModel = (DefaultListModel<String>) oldList.getModel();
			AnimalInfo ele = getInfoByName(item, animalData);
			addToModel(ele, oldModel, animalData);
			selectedMap.remove(item, oldList);
			deleteTableRow(item);
		}
	}

	//move all animals in the "choiceList" to "selectedList", and add into the table.
	private void addAllToSelectedList() {
		DefaultListModel<String> model = (DefaultListModel<String>) choicesList.getModel();
		if (model != null) {
			int size = model.getSize();
			while (size > 0) {
				String item = model.getElementAt(0);
				model.remove(0);
				selectedModel.addElement(item);
				selectedMap.put(item, choicesList);
				addTableRow(item);
				size--;
				
			}
		}
	}

	//move all animals in the "selectedList" back to "choiceList", and remove from the table.
	private void removeAllFromSelectedList() {
		DefaultListModel<String> model = (DefaultListModel<String>) selectedList.getModel();
		int size = model.getSize();
		if (model != null) {
			while (size > 0) {
				String item = model.getElementAt(0);
				model.remove(0);
				JList<String> oldList = selectedMap.get(item);
				if (oldList != null) {
					DefaultListModel<String> oldModel = (DefaultListModel<String>) oldList.getModel();
					AnimalInfo ele = getInfoByName(item, animalData);
					// oldModel.add(ele.index ,item);
					addToModel(ele, oldModel, animalData);
					selectedMap.remove(item, oldList);
					deleteTableRow(item);
				}
				size--;
			}
		}
	}

	// delete the selected row from the table
	private void deleteTableRow(String item) {
		if (animalsTable.model.isContained(item)) {
			int rowIndex = animalsTable.model.rowIndexOfElement(item);
			String itemName = null;	// to store the animal name before the model deleting.
			if(Double.parseDouble(animalsTable.model.data[rowIndex][2].toString()) > 0 ) {
				itemName = item;
			}
			
			// delete the item data from the table
			animalsTable.model.deleteRow(rowIndex);
			
			// set special value of the "total" row in column 1,3,4,5,6
			animalsTable.model.mySetValueAt(" ", animalsTable.model.getRowCount() - 1, 1);
			for (int i = 3; i < 7; i++) {
				if (i == 3 || i == 4 || i == 5 || i == 6) 
					animalsTable.model.mySetValueAt("N/A", animalsTable.model.getRowCount() - 1, i);
			}
			
			// refresh the table
			jTable.updateUI();
			
			// remove this animal info from the data structure
			AnimalInfo a = getInfoByName(item, animalInTable);			
			animalInTable.remove(a);
			
			// delete the corresponding column of the "location Table"
			if(locationsPanel != null) {
				if(itemName != null) {
					//int col = panelManager.getColumn(itemName, locationPanel.columnName);
					locationsPanel.deleteTableColumn(itemName);
				}
					
			}
			
		}
	}

	// add a row into the table
	private void addTableRow(String item) {
		
		// add the item data into the table
		AnimalInfo ele = getInfoByName(item, animalData);
		if (ele != null) {
			DecimalFormat df = new DecimalFormat("0.00");

			String[] rowData = new String[11];
			String quantity = "0";
			String weight = "0";
			String m = ele.data[0];
			String ts = ele.data[2];
			String vs = ele.data[3];
			double qDou = Double.parseDouble(quantity);
			double wDou = Double.parseDouble(weight);
			double mDou = Double.parseDouble(m);
			double tsDou = Double.parseDouble(ts);
			double vsDou = Double.parseDouble(vs);

			rowData[0] = item;
			rowData[1] = ele.type;
			rowData[2] = quantity;
			rowData[3] = weight;
			rowData[4] = m;
			rowData[5] = vs;
			rowData[6] = ts;
			rowData[7] = df.format(mDou * qDou * wDou / 1000);
			rowData[8] = df.format(vsDou * qDou * wDou / 1000);
			rowData[9] = df.format(tsDou * qDou * wDou / 1000);
			rowData[10] = df.format(mDou * qDou * wDou * 60 / 1000);
			animalsTable.model.insertRow(rowData, animalsTable.model.getRowCount()-2);
			
			// set special value of the "total" row in column 1,3,4,5,6
			animalsTable.model.mySetValueAt(" ", animalsTable.model.getRowCount() - 1, 1);
			for (int i = 3; i < 7; i++) {			
				if (i == 3 || i == 4 || i == 5 || i == 6) 
					animalsTable.model.mySetValueAt("N/A", animalsTable.model.getRowCount() - 1, i);				
			}
			
			// refresh the table
			jTable.updateUI();
			// add this animal info into the data structure
			animalInTable.add(ele);
			// transfer pane into the table.
			pane = parent.tabbedPane;
			animalsTable.getTabbedPane(pane);
		}

	}

	// add the input data into "model", which used for create JList at the beginning.
	private void InputElementIntoModel(ArrayList<AnimalInfo> list, DefaultListModel<String> model) {
		for (int i = 0; i < list.size(); i++) {
			AnimalInfo ele = list.get(i);
			ele.index = i;
			model.add(i, ele.name);
		}
	}
	
	// add animalInfo into model, used for "add" or "delete" animal in the "choiceList" and "seletedList".
	private void addToModel(AnimalInfo ani, DefaultListModel<String> model, ArrayList<AnimalInfo> data) {
		if (model == null)
			model.add(0, ani.name);
		if (ani != null) {
			int size = model.getSize();
			int index = ani.index;
			if (index == 0) {
				model.add(0, ani.name);
			} else {
				for (int i = 0; i < model.size(); i++) {
					String name = model.get(i);
					AnimalInfo nAni = getInfoByName(name, data);

					if (nAni != null) {
						if (nAni.index > index) {
							model.add(i, ani.name);
							break;
						} else
							continue;
					}
				}
				if (model.size() == size) {
					model.setSize(size + 1);
					model.add(model.size() - 1, ani.name);
				}
			}
		}
	}

	// gets all types of animals, used for show in the JcomboBox "type"
	private String[] getTypes(ArrayList<AnimalInfo> data) {
		ArrayList<String> list = new ArrayList<>();
		for (AnimalInfo a : data) {
			if (list.contains(a.type)) {
				continue;
			} else
				list.add(a.type);
		}
		String[] sList = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			sList[i] = list.get(i);
		}
		Arrays.sort(sList);
		return sList;
	}

	// gets animalInfo with target name
	private AnimalInfo getInfoByName(String name, ArrayList<AnimalInfo> data) {
		AnimalInfo ele = null;
		for (AnimalInfo ani : data) {
			if (ani.name == name) {
				ele = ani;
			}
		}
		return ele;
	}

	// gets all animalInfo with the target type.
	private ArrayList<AnimalInfo> filterByType(String tp, ArrayList<AnimalInfo> data) {
		ArrayList<AnimalInfo> list = new ArrayList<>();
		for (AnimalInfo ani : data) {
			if (ani.type.equals(tp)) {
				list.add(ani);
			}
		}
		return list;
	}

	public void setParent(MainFrame frame) {
		this.parent = frame;
	}
}
