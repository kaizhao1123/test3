package Model_Entity;

/**
 * This class is used to create the bedding info entity.
 * @author Kai Zhao
 *
 */
public class BeddingInfo {

	public String name;
	public String density;
	public String eff_Density;
	public String state;

	public BeddingInfo(String n, String d, String eff, String s){
		name = n;
		density = d;
		eff_Density = eff;
		state = s;		
	}
	
}
