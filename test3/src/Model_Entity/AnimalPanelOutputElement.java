package Model_Entity;

/**
 * This class is used to create the AnimalPanelOutputElement entity.
 * It includes name and data, used for storing the output of the animalPanel.
 * 
 * @author Kai Zhao
 *
 */
public class AnimalPanelOutputElement {
	public String name ;
	public String[] data; // including Manure, TS.
	
	
	public AnimalPanelOutputElement(String n, String[] list) {
		name = n;
		data = list;
	}
	
	
}
