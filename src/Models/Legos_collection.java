package Models;

import java.util.HashMap;

public class Legos_collection {

	public HashMap<String, Lego> legos;


	public Legos_collection(Structure_3D structure) {
		
		legos = new HashMap<String, Lego>();
	
		legos.put("CUBE", new Lego(50, 50, 50, "CUBE", null,structure));

		legos.put("RECTANGLE", new Lego(150, 50, 50, "RECTANGLE", null,structure));
		legos.put("PILLIER", new Lego(50, 200, 50, "PILLIER", null,structure));
		legos.put("BASE", new Lego(50, 1, 50, "BASE", null,structure));
	}



}
