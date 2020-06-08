package Model_Entity;

/**
 * This class is used to create the animal info entity.
 * @author Kai Zhao
 *
 */
public class AnimalInfo {

	public String name;
	public String type;
	public String dataSource; 
	public String[] data;
	public int index;
			
	/**
	 * the constructor of this class
	 * @param n	name of the animal
	 * @param t type of the animal
	 * @param s source of the animal
	 * @param d data of the animal
	 * @param i the location will be shown in the list, to guarantee that the location in the list
	 * is the same as before moving
	 */
	public AnimalInfo(String n, String t, String s, String[] d, int i ){
		name = n;
		type = t;
		dataSource = s;
		data = d;
		index = 0;
	}
	
}
