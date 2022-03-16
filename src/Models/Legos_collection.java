package Models;

import java.util.HashMap;


public class Legos_collection {
	
	public static  HashMap<String, Lego> legos = new HashMap<String, Lego>();
	
	public static String selected_bloc = "BASE";
	

	public Legos_collection() {
		legos.put("CUBE",new Lego(100,100,100,"CUBE",null));
		
		legos.put("RECTANGLE",new Lego(200,50, 50,"RECTANGLE",null));
		legos.put("PILLIER",new Lego(50,200, 50,"PILLIER",null));
		legos.put("BASE",new Lego(1000,1,1000,"BASE",null));
	}
	
	public static Lego new_selectedBlock() {
		
		return legos.get(selected_bloc);
	}
	
	
	

}
