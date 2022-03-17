package Models;

import java.util.HashMap;

public class Legos_collection {

	public HashMap<String, Lego> legos;


	public Legos_collection(Structure_3D structure) {
		
		legos = new HashMap<String, Lego>();
	
		legos.put("CUBE", new Lego(50, 50, 50, "CUBE", null,structure));
		legos.put("RECTANGLE_DROITE", new Lego(200, 50, 50, "RECTANGLE_DROITE", null,structure));
		legos.put("RECTANGLE_GAUCHE", new Lego(200, 50, 50, "RECTANGLE_GAUCHE", null,structure));

		legos.put("PILLIER", new Lego(50, 200, 50, "PILLIER", null,structure));
		legos.put("BASE", new Lego(50, 1, 50, "BASE", null,structure));
	}



}
