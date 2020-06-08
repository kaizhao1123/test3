package Model_Entity;

/**
 * This class is used to create the climate info entity.
 * @author Kai Zhao
 *
 */
public class ClimateInfo {
	
	public String state;
	public String county;
	public String name; 
	public String[] data;
	
	public ClimateInfo(String s, String c, String n, String[] d ){
		state = s;
		county = c;
		name = n;
		data = d;
	}
}
