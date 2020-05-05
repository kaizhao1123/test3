package Panels;

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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import AWS.PanelManager;
import Entity.AnimalInfo;
import Entity.AnimalTable;
import Entity.ClimateTable;
import Entity.OutputOfAnimalTable;

public class AnimalPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MainFrame parent;
	JTabbedPane pane;
	PanelManager panelManager;
	LocationPanel locationPanel;
	AddAnimalDialog modelDialog;

	// declare the data structure used in the panel
	ArrayList<AnimalInfo> animalData; // store the input data filtered by source
	HashMap<String, JList<String>> choicesMap; // K is the name of animal type, V is the original JList respond the name
	HashMap<String, JList<String>> selectedMap; // K is the name of animal, V is the JList where the animal come from.
	String[] animalTypes; // store the types of the animal
	AnimalInfo newAnimalInfo; // to build new animal info
	ArrayList<AnimalInfo> animalInTable; // the animals showed in the table, including all selected and new build
										 // animals, used to generate report
	ArrayList<OutputOfAnimalTable> animalPanelOutput;  // store the output of panel: the name, the quantity and the weight of each animal
	
	String[]  columnNamess = { "<html> Animal  </html>", // the header of table
			"<html>Animal <br> (type) </html>", "<html>Quantity </html>", "<html>Weight <br> (lbs) </html>",
			"<html>Manure <br> (cu.ft/day/AU) </html>", "<html> VS <br> (lbs/day/AU) </html>",
			"<html> TS <br> (lbs/day/AU) </html>", "<html> Manure <br> (cu.ft/day) </html>",
			"<html> VS <br> (lbs/day) </html>", "<html> TS <br> (lbs/day) </html>",
			"<html>Manure <br> (lbs/day) </html>" };
	Object[][] dataa;        // the data of table
	
	// declare the elements shown in the panel
	Font font = new Font("Arial Narrow", Font.PLAIN, 13);
	JLabel jl1, jl2, jl3, jl4, jl5, jl6;
	JScrollPane choicesScrollPane;
	JScrollPane selectedScrollPane;
	JScrollPane tableScrollPane;
	AnimalTable mTable;
	JTable jtable;
	JComboBox<String> animalType;
	JButton buttonAdd;
	JButton buttonRemove;
	JButton buttonAddAll;
	JButton buttonRemoveAll;
	JButton buttonNewAnimal;
	JButton buttonDeleteRow;
	JButton buttonOK;
	JButton buttonHelp;
	JList<String> choicesList; // to display the choices
	JList<String> selectedList; // to display the selected
	DefaultListModel<String> selectedModel;

	public AnimalPanel(PanelManager pm) {
		panelManager = pm;

		// get animal dataset
		animalData = panelManager.filterByDataSource(panelManager.startPanelOutput[0], panelManager.allAnimalData);

		// initial this panel
		initialData();
		initialElements();
		initialActionLiseners();
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		initialLayout(gc);
	}

	private void initialData() {
		// initial data structures
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
		animalType = new JComboBox<>(animalTypes);
		animalType.setFont(font);
		animalType.setPreferredSize(new Dimension(100, 20));
		animalType.setSelectedIndex(0);

		// initial table
		mTable = new AnimalTable();
		jtable = mTable.buildMyTable(columnNamess, dataa);
		jtable.getTableHeader().setPreferredSize(new Dimension(10, 35));
		tableScrollPane = new JScrollPane(jtable);
		tableScrollPane.setPreferredSize(new Dimension(800, 120));
	}

	private void initialActionLiseners() {
		animalType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String data = animalType.getItemAt(animalType.getSelectedIndex()).toString().toLowerCase();
				choicesList = choicesMap.get(data);
				choicesScrollPane.setViewportView(choicesList);

			}
		});

		// add listeners of each button
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToSelectedList();
			}
		});
		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFromSelectedList();
			}
		});
		buttonAddAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAllToSelectedList();
			}
		});
		buttonRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeAllFromSelectedList();
			}
		});
		buttonNewAnimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String source = panelManager.startPanelOutput[0];
				String station = panelManager.startPanelOutput[1];
				modelDialog = new AddAnimalDialog(mTable, jtable, source, station);
				modelDialog.setParent(parent);
			}
		});
		buttonDeleteRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = jtable.getSelectedRow();
				if (row != -1) {
					String item = mTable.model.data[row][0].toString();
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
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pane = parent.tabbedPane;
				if (locationPanel == null) {
					getOutput();
					panelManager.storeAnimalPanelOutput(animalPanelOutput);
					locationPanel = new LocationPanel(panelManager);
					locationPanel.setParent(parent);
					pane.add("location", locationPanel);

					if (parent.startPanel.periodDialog.secondOption == true) {
						locationPanel.update2();
					}

				} else {
					// get the location table and add column.
					
				}
				pane.setSelectedIndex(pane.indexOfTab("location"));
			}
		});
	}

	private void initialLayout(GridBagConstraints gc) {

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
		add(animalType, gc);

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
	
	private void getOutput() {
		animalPanelOutput = new ArrayList<>();
		for(int i = 0; i < animalInTable.size(); i++) {
			AnimalInfo ani = animalInTable.get(i);
			String quantity = mTable.model.data[i][2].toString();
			String weight = mTable.model.data[i][3].toString();
			OutputOfAnimalTable ele = new OutputOfAnimalTable(ani,quantity,weight);
			animalPanelOutput.add(ele);
		}				
	}
	

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

	private void deleteTableRow(String item) {
		if (mTable.model.isContained(item)) {
			int row = mTable.model.rowOfElement(item);
			mTable.model.deleteRow(row);
			for (int i = 2; i < mTable.model.getColumnCount(); i++) {
				if (i == 3 || i == 4 || i == 5 || i == 6) {
					mTable.model.mySetValueAt("N/A", mTable.model.getRowCount() - 1, i);

				} else
					mTable.model.mySetValueAt(mTable.model.getNewSum(i), mTable.model.getRowCount() - 1, i);
			}
			jtable.updateUI();
			AnimalInfo a = getInfoByName(item, animalInTable);
			animalInTable.remove(a);
		}
	}

	private void addTableRow(String item) {
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
			// rowData[10] = Double.toString(mDou * qDou * wDou * 60 / 1000);
			mTable.model.addRow(rowData);

			for (int i = 2; i < rowData.length; i++) {
				if (i == 3 || i == 4 || i == 5 || i == 6) {
					mTable.model.mySetValueAt("N/A", mTable.model.getRowCount() - 1, i);

				} else
					mTable.model.mySetValueAt(mTable.model.getNewSum(i), mTable.model.getRowCount() - 1, i);
			}
			jtable.updateUI();
			animalInTable.add(ele);
		}

	}

	private void InputElementIntoModel(ArrayList<AnimalInfo> list, DefaultListModel<String> model) {
		for (int i = 0; i < list.size(); i++) {
			AnimalInfo ele = list.get(i);
			ele.index = i;
			model.add(i, ele.name);
		}
	}

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

	private AnimalInfo getInfoByName(String name, ArrayList<AnimalInfo> data) {
		AnimalInfo ele = null;
		for (AnimalInfo ani : data) {
			if (ani.name == name) {
				ele = ani;
			}
		}
		return ele;
	}

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
