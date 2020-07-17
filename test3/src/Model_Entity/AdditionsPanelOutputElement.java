package Model_Entity;

/**
 * This class is used to create the AdditionPanelTableInfo entity.
 * It includes name and data;
 * 
 * @author Kai Zhao
 *
 */
public class AdditionsPanelOutputElement {
	public String name;
	public String[] data; // including wash, flush, and cv.
	
	public AdditionsPanelOutputElement(String n, String[] list) {
		name = n;
		data = list;
	}
	
	
}
