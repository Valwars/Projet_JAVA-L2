package Models;

import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Lego extends Box {

	Lego parent;
	Lego enfant;

	public int width;
	public int height;
	public int depth;

	public String type;

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
			material.setDiffuseColor(structure.selected_color);

		}
		material.setSpecularColor(Color.valueOf("#424242"));
		System.out.println(type);

		this.setWidth(width);
		this.setHeight(height);
		this.setDepth(depth);

		this.setMaterial(material);

		this.setOnMousePressed(event -> {

			if (event.isSecondaryButtonDown() && this.enfant == null) {

				create_blocs();
			}
		});

	}

	public void create_blocs() {
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

			if (Math.abs(model.width) > 50) {

				for (int i = 1; i < (Math.abs(model.width) / 50); i++) {
					System.out.println("JE DOIS AJOUTER UN BLOC");

					Lego child = new Lego(50, model.height, model.depth, structure.selected_bloc, this, structure);

					if (rotate.equals("GAUCHE")) {
						System.out.println("GAUCHE CHILD");
						System.out.println(i);
						child.setTranslateX(this.getTranslateX() + i * (-51));
					} else {
						child.setTranslateX(this.getTranslateX() + i * 51);
					}
					child.setTranslateY(-1 * (model.height / 2));
					child.setTranslateZ(this.getTranslateZ());

					create3DAsset(group, model, child);

					group.getChildren().add(child);

				}

			} else if (Math.abs(model.depth) > 50) {

				for (int i = 1; i < (Math.abs(model.depth) / 50); i++) {
					System.out.println("JE DOIS AJOUTER UN BLOC");

					Lego child = new Lego(model.width, model.height, 50, structure.selected_bloc, this, structure);

					if (rotate.equals("AVANT")) {
						child.setTranslateZ(this.getTranslateZ() + i * (-51));
					} else {
						System.out.println("ARRIERE : " + i);

						child.setTranslateZ(this.getTranslateZ() + (i * 51));
					}
					child.setTranslateY(-1 * (model.height / 2));
					child.setTranslateX(this.getTranslateX());
					create3DAsset(group, model, child);

					group.getChildren().add(child);

				}
			}
			
			
			new_lego.setTranslateX(this.getTranslateX());
			new_lego.setTranslateY(-1 * (model.height / 2));
			new_lego.setTranslateZ(this.getTranslateZ());

			System.out.println(new_lego.parent);
			create3DAsset(group, model, new_lego);
			group.getChildren().add(new_lego);
	
		} else {
			Lego new_lego = new Lego(50, model.height, 50, structure.selected_bloc, this, structure);

			System.out.println(model.width);
			if (Math.abs(model.width) > 50) {

				for (int i = 1; i < (Math.abs(model.width) / 50); i++) {
					System.out.println("JE DOIS AJOUTER UN BLOC");

					Lego child = new Lego(50, model.height, model.depth, structure.selected_bloc, this, structure);

					if (rotate.equals("GAUCHE")) {
						System.out.println("GAUCHE CHILD");
						System.out.println(i);
						child.setTranslateX(this.getTranslateX() + i * (-51));
					} else {
						child.setTranslateX(this.getTranslateX() + i * 51);
					}
					child.setTranslateY(-1 * (this.getTotalHeight()));
					child.setTranslateZ(this.getTranslateZ());
					create3DAsset(group, model, child);

					group.getChildren().add(child);

				}
			} else if (Math.abs(model.depth) > 50) {

				for (int i = 1; i < (Math.abs(model.depth) / 50); i++) {
					System.out.println("JE DOIS AJOUTER UN BLOC");

					Lego child = new Lego(model.width, model.height, 50, structure.selected_bloc, this, structure);

					if (rotate.equals("AVANT")) {
						child.setTranslateZ(this.getTranslateZ() + i * (-51));
					} else {
						child.setTranslateZ(this.getTranslateZ() + (i * 51));
					}

					child.setTranslateY(-1 * (this.getTotalHeight()));
					child.setTranslateX(this.getTranslateX());
					create3DAsset(group, model, child);

					group.getChildren().add(child);

				}
			}
			
			new_lego.setTranslateX(this.getTranslateX());
			new_lego.setTranslateY(-1 * (this.getTotalHeight()));
			new_lego.setTranslateZ(this.getTranslateZ());

			System.out.println(new_lego.parent);
			this.enfant = new_lego;
			create3DAsset(group, model, new_lego);
			group.getChildren().add(new_lego);
		}

		

		structure.getChildren().add(group);
	}

	public void create3DAsset(Group group, Lego model, Lego cube) {

		Cylinder cylinder7 = new Cylinder();

		int height = 10;
		cylinder7.setHeight(height);
		cylinder7.setRadius(7);
		cylinder7.setTranslateX(cube.getTranslateX() + 10);

		cylinder7.setTranslateZ(cube.getTranslateZ() + 10);

		cylinder7.setOnMousePressed(event -> {

			if (event.isSecondaryButtonDown()) {
				searshAndCreate(cylinder7);
				

			}

		});

		Cylinder cylinder9 = new Cylinder();

		cylinder9.setHeight(height);
		cylinder9.setRadius(7);
		cylinder9.setTranslateX(cube.getTranslateX() - 10);

		cylinder9.setTranslateZ(cube.getTranslateZ() - 10);


		cylinder9.setOnMousePressed(event -> {

			if (event.isSecondaryButtonDown()) {
				searshAndCreate(cylinder9);
				

			}

		});

		Cylinder cylinder5 = new Cylinder();

		cylinder5.setHeight(height);
		cylinder5.setRadius(7);

		cylinder5.setTranslateX(cube.getTranslateX() + 10);

		cylinder5.setTranslateZ(cube.getTranslateZ() - 10);

		cylinder5.setOnMousePressed(event -> {

			if (event.isSecondaryButtonDown()) {
				searshAndCreate(cylinder5);
				

			}

		});
		
		Cylinder cylinder10 = new Cylinder();

		cylinder10.setHeight(height);
		cylinder10.setRadius(7);
		cylinder10.setTranslateX(cube.getTranslateX() - 10);

		cylinder10.setTranslateZ(cube.getTranslateZ() + 10);

		cylinder10.setOnMousePressed(event -> {

			if (event.isSecondaryButtonDown()) {
				searshAndCreate(cylinder10);
				

			}

		});
		PhongMaterial material2 = new PhongMaterial();
		material2.setDiffuseColor(structure.selected_color);

		cylinder10.setMaterial(material2);
		cylinder5.setMaterial(material2);

		cylinder9.setMaterial(material2);

		cylinder7.setMaterial(material2);

		if (this.type.equals("BASE")) {
			cylinder7.setTranslateY(-1 * (model.height));
			cylinder9.setTranslateY(-1 * (model.height));

			cylinder10.setTranslateY(-1 * (model.height));

			cylinder5.setTranslateY(-1 * (model.height));

		} else {
			cylinder7.setTranslateY(-1 * (this.getTotalHeight() + model.height / 2));
			cylinder9.setTranslateY(-1 * (this.getTotalHeight() + model.height / 2));

			cylinder10.setTranslateY(-1 * (this.getTotalHeight() + model.height / 2));

			cylinder5.setTranslateY(-1 * (this.getTotalHeight() + model.height / 2));

		}

		group.getChildren().add(cylinder9);
		group.getChildren().add(cylinder5);
		group.getChildren().add(cylinder7);
		group.getChildren().add(cylinder10);
	}
	
	public void searshAndCreate(Cylinder cylindre) {
		System.out.println("CYLINDRE PARENT :" + cylindre.getParent().getChildrenUnmodifiable());
		System.out.println( cylindre.toString());
		Group g = (Group) cylindre.getParent();
		
		if(g.getChildren().size() > 5) {
			
			for(int i = 0; i < g.getChildren().size()-1; i ++) {
				
				if(g.getChildren().get(i).equals(cylindre)) {
					System.out.println("J'AI TROUVÉ LE CYLINDRE");
					boolean drap = false;
					for(int j = i ; j < g.getChildren().size(); j ++) {
						
						if( g.getChildren().get(j).toString().contains("Lego") && drap == false) {
							System.out.println("J'AI LE LGO PARENT");
							Lego l = (Lego) g.getChildren().get(j);
							l.create_blocs();
							drap = true;
						}
					}
				}
				
			}
			
		}else {
			Lego l = (Lego) g.getChildren().get(4);
			l.create_blocs();
		}
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