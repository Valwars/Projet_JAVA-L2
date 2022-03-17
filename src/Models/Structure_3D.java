package Models;

import javafx.scene.Group;

public class Structure_3D extends Group {

	Legos_collection legos_collections;

	public String selected_bloc = "BASE";

	public Structure_3D() {

		
	}
	 
	public void setCollec(Legos_collection leg) {
		this.legos_collections = leg;
	}

	public void createBase() {

		int x = 0;
		int y = 0;

		Lego pateforme_de_construction;
		
		for(int k = 0 ; k < 4; k ++) {
			
			for (int i = 0; i < 15; i++) {

				for (int j = 0; j < 15; j++) {

					pateforme_de_construction = create_selectedBlock();
					pateforme_de_construction.setTranslateX(x);
					pateforme_de_construction.setTranslateZ(y);

					this.getChildren().add(pateforme_de_construction);
					
					if(k == 0 || k == 2) {
						x += 51;
					}else if(k == 1  || k == 3){
						x -= 51;
					}
					 
				}

				if(k == 0 || k == 3) {
					y -= 51;
				} else if(k == 1 || k == 2){
					y += 51;
				}
				
				x = 0;
			}
			
			x = 0;
			y = 0;
		}
		
		this.selected_bloc = "CUBE";

	}

	public Lego create_selectedBlock() {
		return new Lego(this.legos_collections.legos.get(selected_bloc).width,this.legos_collections.legos.get(selected_bloc).height,this.legos_collections.legos.get(selected_bloc).depth,selected_bloc, null , this);
	}

}
