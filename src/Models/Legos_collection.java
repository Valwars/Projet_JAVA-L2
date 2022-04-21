package Models;

import java.util.HashMap;

public class Legos_collection {

	public HashMap<String, Lego> legos;
	// public HashMap<String, Decoration> decorations;
	
	public int bloc_size;

	public Legos_collection(Structure_3D structure,int bs) {
		
		this.bloc_size = bs;
		
		legos = new HashMap<String, Lego>();
		
		// DECORATION :
		legos.put("TAPIS", new Lego(bloc_size, 5, bloc_size, "TAPIS", null,structure));
		legos.put("SPHERE1", new Lego(bloc_size, bloc_size, bloc_size, "SPHERE1", null,structure));
		
		// BLOCS : 
		legos.put("CUBE", new Lego(bloc_size, bloc_size, bloc_size, "CUBE", null,structure));
			
		legos.put("PLATEAU_4", new Lego(bloc_size*4, bloc_size, bloc_size*4, "PLATEAU_4", null,structure));
		
		legos.put("RECTANGLE2_DROITE", new Lego(bloc_size*2, bloc_size, bloc_size, "RECTANGLE2_DROITE", null,structure));
		legos.put("RECTANGLE2_GAUCHE", new Lego(-bloc_size*2, bloc_size, bloc_size, "RECTANGLE2_GAUCHE", null,structure));
		legos.put("RECTANGLE2_ARRIERE", new Lego(bloc_size, bloc_size, bloc_size*2, "RECTANGLE2_DROITE", null,structure));
		legos.put("RECTANGLE2_AVANT", new Lego(bloc_size, bloc_size, -bloc_size*2, "RECTANGLE2_GAUCHE", null,structure));
		
		legos.put("RECTANGLE4_DROITE", new Lego(bloc_size*4, bloc_size, bloc_size, "RECTANGLE4_DROITE", null,structure));
		legos.put("RECTANGLE4_GAUCHE", new Lego(-bloc_size*4, bloc_size, bloc_size, "RECTANGLE4_GAUCHE", null,structure));
		legos.put("RECTANGLE4_ARRIERE", new Lego(bloc_size,  bloc_size, bloc_size*4, "RECTANGLE4_ARRIERE", null,structure));
		legos.put("RECTANGLE4_AVANT", new Lego(bloc_size, bloc_size,-bloc_size*4, "RECTANGLE4_AVANT", null,structure));

		legos.put("RECTANGLE3_DROITE", new Lego(bloc_size*3, bloc_size, bloc_size, "RECTANGLE3_DROITE", null,structure));
		legos.put("RECTANGLE3_GAUCHE", new Lego(-bloc_size*3, bloc_size, bloc_size, "RECTANGLE3_GAUCHE", null,structure));
		legos.put("RECTANGLE3_ARRIERE", new Lego(bloc_size, bloc_size, bloc_size*3, "RECTANGLE2_DROITE", null,structure));
		legos.put("RECTANGLE3_AVANT", new Lego(bloc_size, bloc_size, -bloc_size*3, "RECTANGLE2_GAUCHE", null,structure));
		
		legos.put("RECTANGLE5_DROITE", new Lego(bloc_size*5, bloc_size, bloc_size, "RECTANGLE5_DROITE", null,structure));
		legos.put("RECTANGLE5_GAUCHE", new Lego(-bloc_size*5, bloc_size, bloc_size, "RECTANGLE5_GAUCHE", null,structure));
		legos.put("RECTANGLE5_ARRIERE", new Lego(bloc_size,  bloc_size, bloc_size*5, "RECTANGLE5_ARRIERE", null,structure));
		legos.put("RECTANGLE5_AVANT", new Lego(bloc_size, bloc_size,-bloc_size*5, "RECTANGLE5_AVANT", null,structure));
		
		legos.put("ANGLE_DROITE", new Lego(bloc_size*2, bloc_size,bloc_size*2, "ANGLE_DROITE", null,structure));
		legos.put("ANGLE_GAUCHE", new Lego(bloc_size*2, bloc_size,-bloc_size*2, "ANGLE_GAUCHE", null,structure));
		legos.put("ANGLE_ARRIERE", new Lego(-bloc_size*2, bloc_size,bloc_size*2, "ANGLE_ARRIERE", null,structure));
		legos.put("ANGLE_AVANT", new Lego(-bloc_size*2, bloc_size,-bloc_size*2, "ANGLE_AVANT", null,structure));
		
		
		legos.put("BASE", new Lego(bloc_size, 1, bloc_size, "BASE", null,structure));
	}

	

}
