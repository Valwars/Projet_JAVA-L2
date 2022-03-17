package Models;

import java.util.HashMap;

public class Legos_collection {

	public HashMap<String, Lego> legos;

	public String selected_bloc = "BASE";

	public Legos_collection() {
		legos = new HashMap<String, Lego>();
	
		legos.put("CUBE", new Lego(50, 50, 50, "CUBE", null));

		legos.put("RECTANGLE", new Lego(150, 50, 50, "RECTANGLE", null));
		legos.put("PILLIER", new Lego(50, 200, 50, "PILLIER", null));
		legos.put("RECTANGLE_DROITE", new Lego(50, 50, 150, "RECTANGLE_DROITE", null));
		legos.put("BASE", new Lego(50, 1, 50, "BASE", null));
	}


		
	public Lego new_selectedBlock() {

		String[] blocs = selected_bloc.split("_");

		Lego leg = new Lego(legos.get(selected_bloc).width, legos.get(selected_bloc).height,
				legos.get(selected_bloc).depth, selected_bloc, legos.get(selected_bloc).parent);

		if (blocs.length > 1) {
			switch (blocs[1]) {

			case ("DROITE"):
				System.out.println("DOITE");
				leg.rotateLego(90);
				leg.translate(50);

				break;
			}
		}

		return leg;
	}

}
