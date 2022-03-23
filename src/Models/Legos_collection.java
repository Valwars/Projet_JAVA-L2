package Models;

import java.util.HashMap;

public class Legos_collection {

	public HashMap<String, Lego> legos;


	public Legos_collection(Structure_3D structure) {
		
		legos = new HashMap<String, Lego>();
	
		legos.put("CUBE", new Lego(50, 50, 50, "CUBE", null,structure));
		
		legos.put("RECTANGLE2_DROITE", new Lego(100, 50, 50, "RECTANGLE2_DROITE", null,structure));
		legos.put("RECTANGLE2_GAUCHE", new Lego(-100, 50, 50, "RECTANGLE2_GAUCHE", null,structure));
		legos.put("RECTANGLE2_ARRIERE", new Lego(50, 50, 100, "RECTANGLE2_DROITE", null,structure));
		legos.put("RECTANGLE2_AVANT", new Lego(50, 50, -100, "RECTANGLE2_GAUCHE", null,structure));
		
		
		legos.put("RECTANGLE4_DROITE", new Lego(200, 50, 50, "RECTANGLE4_DROITE", null,structure));
		legos.put("RECTANGLE4_GAUCHE", new Lego(-200, 50, 50, "RECTANGLE4_GAUCHE", null,structure));
		legos.put("RECTANGLE4_ARRIERE", new Lego(50,  50, 200, "RECTANGLE4_ARRIERE", null,structure));
		legos.put("RECTANGLE4_AVANT", new Lego(50, 50,-200, "RECTANGLE4_AVANT", null,structure));

		legos.put("RECTANGLE3_DROITE", new Lego(150, 50, 50, "RECTANGLE3_DROITE", null,structure));
		legos.put("RECTANGLE3_GAUCHE", new Lego(-150, 50, 50, "RECTANGLE3_GAUCHE", null,structure));
		legos.put("RECTANGLE3_ARRIERE", new Lego(50, 50, 150, "RECTANGLE2_DROITE", null,structure));
		legos.put("RECTANGLE3_AVANT", new Lego(50, 50, -150, "RECTANGLE2_GAUCHE", null,structure));
		
		legos.put("RECTANGLE5_DROITE", new Lego(250, 50, 50, "RECTANGLE5_DROITE", null,structure));
		legos.put("RECTANGLE5_GAUCHE", new Lego(-250, 50, 50, "RECTANGLE5_GAUCHE", null,structure));
		legos.put("RECTANGLE5_ARRIERE", new Lego(50,  50, 250, "RECTANGLE5_ARRIERE", null,structure));
		legos.put("RECTANGLE5_AVANT", new Lego(50, 50,-250, "RECTANGLE5_AVANT", null,structure));

		legos.put("BASE", new Lego(50, 1, 50, "BASE", null,structure));
	}



}
