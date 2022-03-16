package Models;

import java.util.HashMap;


public class Legos_collection {
	
	public  HashMap<String, Lego> legos = new HashMap<String, Lego>();
	
	public  String selected_bloc = "BASE";
	

	public Legos_collection() {
		legos.put("CUBE",new Lego(50,50,50,"CUBE",null));
		
		legos.put("RECTANGLE",new Lego(200,50, 50,"RECTANGLE",null));
		legos.put("PILLIER",new Lego(50,200, 50,"PILLIER",null));
		legos.put("RECTANGLE_GAUCHE",new Lego(50,50, 200,"RECTANGLE_GAUCHE",null));
		legos.put("BASE",new Lego(50,1,50,"BASE",null));

	}
	
	public  Lego new_selectedBlock() {
		
		return new Lego(legos.get(selected_bloc).width, legos.get(selected_bloc).height, legos.get(selected_bloc).depth, selected_bloc, legos.get(selected_bloc).parent);
	}
	
	
	

}
