package Model_Entity;

/**
 * This class is used to create the AnimalPanelTableInfo entity, that is, the row info of animal table.
 * It includes animalInfo, the quantity, and the weight, used for storing the output of the animalPanel.
 * 
 * @author Kai Zhao
 *
 */
public class AnimalPanelTableInfo {
	public AnimalInfo aniInfo ;
	public String quantity;
	public String weight;
	
	public AnimalPanelTableInfo(AnimalInfo ani, String q, String w) {
		aniInfo = ani;
		quantity = q;
		weight = w;
	}
	
	
}
