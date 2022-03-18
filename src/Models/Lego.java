package Models;


import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

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
		System.out.println(type);

		this.setWidth(width);
		this.setHeight(height);
		this.setDepth(depth);

		this.setMaterial(material);

		this.setOnMouseClicked(event -> {

			String[] rot = structure.selected_bloc.split("_");

			String rotate = "";
			if (rot.length > 1) {
				rotate = rot[1];
				System.out.println(rotate);
			}

			Group group = new Group();

			Lego model = structure.legos_collections.legos.get(structure.selected_bloc);

			if (this.type.equals("BASE")) {
				Lego new_lego = new Lego(50, model.height, 50, structure.selected_bloc, this, structure);

				new_lego.setTranslateX(this.getTranslateX());
				new_lego.setTranslateY(-1 * (model.height / 2));
				new_lego.setTranslateZ(this.getTranslateZ());

				System.out.println(new_lego.parent);

				group.getChildren().add(new_lego);

				if (Math.abs(model.width) > 50) {

					for (int i = 0; i < (Math.abs(model.width) / 50); i++) {
						System.out.println("JE DOIS AJOUTER UN BLOC");

						Lego child = new Lego(50, model.height, model.depth, structure.selected_bloc, this, structure);
						
						if(rotate.equals("GAUCHE")){
							System.out.println("GAUCHE CHILD");
							System.out.println(i);
							child.setTranslateX(this.getTranslateX() + i * (-51));
						}else {
							child.setTranslateX(this.getTranslateX() + i * 51);
						}
						child.setTranslateY(-1 * (model.height / 2));
						child.setTranslateZ(this.getTranslateZ());

						group.getChildren().add(child);

					}
					
				}else if(Math.abs(model.depth) > 50) {
					
					for (int i = 0; i < (Math.abs(model.depth) / 50); i++) {
						System.out.println("JE DOIS AJOUTER UN BLOC");

						Lego child = new Lego(model.width, model.height, 50, structure.selected_bloc, this, structure);
						
						if(rotate.equals("AVANT")){
							child.setTranslateZ(this.getTranslateZ() + i * (-51));
						}else {
							System.out.println("ARRIERE : " + i);

							child.setTranslateZ(this.getTranslateZ() + (i * 51));
						}
						child.setTranslateY(-1 * (model.height / 2));
						child.setTranslateX(this.getTranslateX());

						group.getChildren().add(child);

					}
				}

			} else {
				Lego new_lego = new Lego(50, model.height, 50, structure.selected_bloc, this, structure);

				System.out.println(model.width);
				if (Math.abs(model.width) > 50) {

					for (int i = 0; i < (Math.abs(model.width)/ 50); i++) {
						System.out.println("JE DOIS AJOUTER UN BLOC");

						Lego child = new Lego(50, model.height, model.depth, structure.selected_bloc, this, structure);

						if(rotate.equals("GAUCHE")){
							System.out.println("GAUCHE CHILD");
							System.out.println(i);
							child.setTranslateX(this.getTranslateX() + i * (-51));
						}else {
							child.setTranslateX(this.getTranslateX() + i * 51);
						}
						child.setTranslateY(-1 * (this.getTotalHeight()));
						child.setTranslateZ(this.getTranslateZ());

						group.getChildren().add(child);

					}
				}else if(Math.abs(model.depth) > 50) {
					
					for (int i = 0; i < (Math.abs(model.depth) / 50); i++) {
						System.out.println("JE DOIS AJOUTER UN BLOC");

						Lego child = new Lego(model.width, model.height, 50, structure.selected_bloc, this, structure);
						
						if(rotate.equals("AVANT")){
							child.setTranslateZ(this.getTranslateZ() + i * (-51));
						}else {
							child.setTranslateZ(this.getTranslateZ() +  (i * 51));
						}
						
						child.setTranslateY(-1 * (this.getTotalHeight()));
						child.setTranslateX(this.getTranslateX());

						group.getChildren().add(child);

					}
				}

				new_lego.setTranslateX(this.getTranslateX());
				new_lego.setTranslateY(-1 * (this.getTotalHeight()));
				new_lego.setTranslateZ(this.getTranslateZ());

				System.out.println(new_lego.parent);

				group.getChildren().add(new_lego);
			}

			

			structure.getChildren().addAll(group);

		});

	}

	public int getTotalHeight() {

		int height = this.height / 2;

		Lego curr = this;

		while (curr.parent != null) {

			height += curr.height;

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