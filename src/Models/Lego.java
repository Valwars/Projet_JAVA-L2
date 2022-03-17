package Models;

import java.util.HashMap;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Lego extends Box {

	Lego parent;
	Lego enfant;

	public int width;
	public int height;
	public int depth;
	

	public String type;
	public Color color = Color.AQUAMARINE;
	
	public Structure_3D structure;

	public Lego(int width, int height, int depth, String type, Lego parent, Structure_3D structure) {

		this.width = width;
		this.height = height;
		this.depth = depth;
		this.parent = parent;
		
		this.structure = structure;
		this.type = type;
		PhongMaterial material = new PhongMaterial();

		if (parent == null) {
			material.setDiffuseColor(Color.valueOf("#6B6E74"));
		} else {
			material.setDiffuseColor(Color.ROYALBLUE);
 
		}
		material.setSpecularColor(Color.valueOf("#424242"));
		

			this.setWidth(width);
			this.setHeight(height);
			this.setDepth(depth);
		
		this.setMaterial(material);
		
		
		
		this.setOnMouseClicked(event -> {
			
			Lego model = structure.legos_collections.legos.get(structure.selected_bloc);

			if (this.type.equals("BASE")) {
				Lego new_lego = new Lego(model.width,
						model.height, model.depth,
						structure.selected_bloc, this, structure);

				new_lego.setTranslateX(this.getTranslateX());
				new_lego.setTranslateY(-1 * (model.height / 2));
				new_lego.setTranslateZ(this.getTranslateZ());

				System.out.println(new_lego.parent);

				structure.getChildren().add(new_lego);
			} else {
				Lego new_lego = new Lego(model.width,
						model.height, model.depth,
						structure.selected_bloc, this, structure);

				new_lego.setTranslateX(this.getTranslateX());
				new_lego.setTranslateY(-1 * (this.getTotalHeight()));
				new_lego.setTranslateZ(this.getTranslateZ());

				System.out.println(new_lego.parent);

				structure.getChildren().add(new_lego);
			}

		});
		

	}
	
	public int getTotalHeight() {

		int height = this.height;

		Lego curr = this;

		while (curr.parent != null) {

			if (curr.parent.type != "BASE") {
				height += curr.parent.height;
				System.out.println("HAUTEUR PAPA :" + curr.parent.height);

			}

			curr = curr.parent;

		}

		System.out.println("HAUTEUR TOTALE :" + height);
		return height;
	}
	
	public void rotateLego(int angle) {
		this.setRotate(angle);

	}
	
	public void translate(int translate) {
		this.setTranslateX(translate);

	}

}