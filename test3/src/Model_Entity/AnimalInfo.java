package Model_Entity;

public class AnimalInfo {

	public String name;
	public String type;
	public String dataSource; 
	public String[] data;
	public int index;
			
	public AnimalInfo(String n, String t, String s, String[] d, int i ){
		name = n;
		type = t;
		dataSource = s;
		data = d;
		index = 0;
	}
	
}
