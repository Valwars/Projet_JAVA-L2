package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;

public class Lego extends Box implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Lego legoParent;
	private Lego enfant;

	private String type;
	private Structure_3D structure;
	
	private String coul;
	
	private String texture;
	
	private int taille;
	
	private String parent_name;
	
	private String direction;
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public ArrayList<Lego> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<Lego> childs) {
		this.childs = childs;
	}


	private ArrayList<Lego> childs;
	
	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public int getTaille() {
		return taille;
	}

	public void setTaille(int taille) {
		this.taille = taille;
	}

	public Lego() {
		

		this.setOnMousePressed(event -> {
			
			if (event.isSecondaryButtonDown() &&  this.getEnfant() == null) {
			
				this.create_blocs();

			}else if(structure.getPressedKeys().contains(KeyCode.SHIFT) && structure.getPressedKeys().size() == 1 ){
				
				
				structure.setLast_lego_selected(this);
				
				System.out.println("Dernier lego selectionné : "+ structure.getLast_lego_selected());

				structure.getLego_selected().add(this);
				
				System.out.println("legos selectionnés : "+ structure.getLego_selected());

				((PhongMaterial) this.getMaterial()).setDiffuseColor(Color.FUCHSIA);
				searsh_cylinder(Color.FUCHSIA,null);
				

			}
		});
	}
	
	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public Lego(double width, double height, double depth, String type, Lego parent, Structure_3D structure) {
		childs = new ArrayList<Lego>();
		this.legoParent = parent;
		this.structure = structure;
		this.type = type;
		this.taille = structure.getTaille();
		this.parent_name = structure.getNom_structure();
		String[] rot = structure.getSelected_bloc().split("_");
		if(rot.length >1) {
			this.direction = rot[1];
			System.out.println("ROTATION : "+ this.direction);

		}
		PhongMaterial material = new PhongMaterial();

		if (parent == null) {
			material.setDiffuseColor(Color.GRAY);
			this.coul = Color.GRAY.toString();
		} else {
			if(structure.getSelected_matiere() == null) {
				
				this.coul = structure.getSelected_color().toString();

				material.setDiffuseColor(structure.getSelected_color());

			}else {
				this.texture = structure.getSelected_matiere();
				material.setDiffuseMap(new Image(getClass().getResourceAsStream(structure.getSelected_matiere())));

			}

		}
		
		material.setSpecularColor(Color.valueOf("#424242"));

		this.setWidth(width);
		this.setHeight(height);
		this.setDepth(depth);

		this.setMaterial(material);
		

		this.setOnMousePressed(event -> {
			
			System.out.println("je clique sur un lego");
			if (event.isSecondaryButtonDown() && this.enfant == null) {

				create_blocs();

			}else if(structure.getPressedKeys().contains(KeyCode.SHIFT) && structure.getPressedKeys().size() == 1 ){
				
				structure.setLast_lego_selected(this);
				
				System.out.println("Dernier lego selectionné : "+ structure.getLast_lego_selected());

				structure.getLego_selected().add(this);
				
				System.out.println("legos selectionnés : "+ structure.getLego_selected());

				material.setDiffuseColor(Color.FUCHSIA);
				searsh_cylinder(Color.FUCHSIA,null);

			}
		});

	}

	public String getCoul() {
		return coul;
	}

	public void setCoul(String coul) {
		this.coul = coul;
	}


	public void create_blocs() {
		String[] rot = structure.getSelected_bloc().split("_");
		String rotate = "";
		if (rot.length > 1) {
			rotate = rot[1];
			

		}
		
		Group group = new Group();
		
		Lego model = structure.getLegos_collections().getLegos().get(structure.getSelected_bloc());
		System.out.println(this.enfant);
		if (this.enfant == null) {
			if (!this.type.equals("TAPIS")) {

				if (this.type.equals("BASE")) {
							
					Lego new_lego = new Lego(structure.getBLOC_SIZE(), model.getHeight(), structure.getBLOC_SIZE(), structure.getSelected_bloc(), this, structure);
					
					if (Math.abs(model.getWidth()) > structure.getBLOC_SIZE()) {
						System.out.println(model.getType() + " : " + model.getWidth() + "," + model.getDepth());
						for (int i = 1; i < ((Math.abs(model.getWidth())+(structure.getBLOC_SIZE()*structure.getTaille())) / structure.getBLOC_SIZE()); i++) {
								

							Lego child = new Lego(structure.getBLOC_SIZE(), model.getHeight(), structure.getBLOC_SIZE(), structure.getSelected_bloc(), this,
									structure);

							if ((rotate.equals("GAUCHE") || ((rotate.equals("AVANT")  && structure.getSelected_bloc().split("_")[0].equals("ANGLE")))))  {
								
								child.setTranslateX(this.getTranslateX() + i * (-structure.getBLOC_SIZE() - 1));
							} else {
								child.setTranslateX(this.getTranslateX() + i * (structure.getBLOC_SIZE() +1));
							}
							child.setTranslateY(-1 * (model.getHeight() / 2));
							child.setTranslateZ(this.getTranslateZ());

							create3DAsset(group, model, child);
							child.getLegoParent().enfant = child;

							structure.getChildren().add(child);
							
							new_lego.childs.add(child);

						}
					

					} 
					
					if (Math.abs(model.getDepth()) > structure.getBLOC_SIZE()) {
						
						System.out.println(model.getType() + " : " + model.getWidth() + "," + model.getDepth());
						for (int i = 1; i < ((Math.abs(model.getDepth())+(structure.getBLOC_SIZE()*structure.getTaille()))/ structure.getBLOC_SIZE()); i++) {

							Lego child = new Lego(structure.getBLOC_SIZE(), model.getHeight(), structure.getBLOC_SIZE(), structure.getSelected_bloc(), this,
									structure);

							if ( ((rotate.equals("AVANT")  && structure.getSelected_bloc().split("_")[0].equals("ANGLE"))) || ((rotate.equals("ARRIERE")  && structure.getSelected_bloc().split("_")[0].equals("ANGLE"))) ||  (rotate.equals("AVANT")  && !structure.getSelected_bloc().split("_")[0].equals("ANGLE")) ) {
								child.setTranslateZ(this.getTranslateZ() + i * (-structure.getBLOC_SIZE() - 1));
							} else {

								child.setTranslateZ(this.getTranslateZ() + (i * (structure.getBLOC_SIZE() +1)));
							}
							
							child.setTranslateY(-1 * (model.getHeight() / 2));
							child.setTranslateX(this.getTranslateX());
							create3DAsset(group, model, child);
							child.getLegoParent().enfant = child;

							structure.getChildren().add(child);
							new_lego.childs.add(child);

						}
					}
					
					for(int i = 0; i < new_lego.childs.size();i ++) {
						
						Lego c = new_lego.childs.get(i);
						
						for(int j = i + 1; j < new_lego.childs.size(); j ++ ) {
							c.childs.add(new_lego.childs.get(j));
						}
						
					}

				
					new_lego.setTranslateX(this.getTranslateX());
					new_lego.setTranslateY(-1 * (model.getHeight() / 2));
					new_lego.setTranslateZ(this.getTranslateZ());
					
					
					this.enfant = new_lego;
					create3DAsset(group, model, new_lego);
					structure.getChildren().add(new_lego);

				} else {
					Lego new_lego = new Lego(structure.getBLOC_SIZE(), model.getHeight(), structure.getBLOC_SIZE(), structure.getSelected_bloc(), this, structure);

					if (Math.abs(model.getWidth()) > structure.getBLOC_SIZE()) {

						for (int i = 1; i < ((Math.abs(model.getWidth())+(structure.getBLOC_SIZE()*structure.getTaille()))/ structure.getBLOC_SIZE()); i++) {

							Lego child = new Lego(structure.getBLOC_SIZE(), model.getHeight(), structure.getBLOC_SIZE(), structure.getSelected_bloc(), this,
									structure);

							if ((rotate.equals("GAUCHE") || ((rotate.equals("AVANT")  && structure.getSelected_bloc().split("_")[0].equals("ANGLE")))))  {
								
								child.setTranslateX(this.getTranslateX() + i * (-structure.getBLOC_SIZE() - 1));
							} else {
								child.setTranslateX(this.getTranslateX() + i * (structure.getBLOC_SIZE() +1));
							}
							child.setTranslateY(-1 * (this.getTotalHeight()));
							child.setTranslateZ(this.getTranslateZ());
							create3DAsset(group, model, child);
							child.getLegoParent().enfant = child;

							structure.getChildren().add(child);
							new_lego.childs.add(child);

						}
					} if (Math.abs(model.getDepth()) > structure.getBLOC_SIZE()) {
						
						
						for (int i = 1; i < ((Math.abs(model.getDepth())+(structure.getBLOC_SIZE()*structure.getTaille())) / structure.getBLOC_SIZE()); i++) {

							Lego child = new Lego(structure.getBLOC_SIZE(), model.getHeight(), structure.getBLOC_SIZE(), structure.getSelected_bloc(), this,
									structure);

							if ( ((rotate.equals("AVANT")  && structure.getSelected_bloc().split("_")[0].equals("ANGLE"))) || ((rotate.equals("ARRIERE")  && structure.getSelected_bloc().split("_")[0].equals("ANGLE"))) ||  (rotate.equals("AVANT")  && !structure.getSelected_bloc().split("_")[0].equals("ANGLE")) ) {
								child.setTranslateZ(this.getTranslateZ() + i * (-structure.getBLOC_SIZE() - 1));
							} else {
								child.setTranslateZ(this.getTranslateZ() + i * (structure.getBLOC_SIZE() +1));
							}

							child.setTranslateY(-1 * (this.getTotalHeight()));
							child.setTranslateX(this.getTranslateX());
							child.getLegoParent().enfant = child;
							create3DAsset(group, model, child);
							
						
							structure.getChildren().add(child);
							new_lego.childs.add(child);
							
						

						}
							

					}
					
					for(int i = 0; i < new_lego.childs.size();i ++) {
						
						Lego c = new_lego.childs.get(i);
						
						for(int j = i + 1; j < new_lego.childs.size(); j ++ ) {
							c.childs.add(new_lego.childs.get(j));
						}
						
					}

					new_lego.setTranslateX(this.getTranslateX());
					new_lego.setDirection(rotate);
					
					if (new_lego.getHeight() == 5) {

						new_lego.setTranslateY(-1 * (this.getTotalHeight()) + 22);

					} else {
						new_lego.setTranslateY(-1 * (this.getTotalHeight()));
					}

					new_lego.setTranslateZ(this.getTranslateZ());
					
			
					this.enfant = new_lego;
				
					if(this.direction != null) {
					
						if(this.direction.equals(rotate)) {
							System.out.println("MEME DIRECTION");

							if(this.childs.size() >= new_lego.childs.size()) {
								for(int i = 0; i < new_lego.childs.size();i++) {
									
									System.out.println("BLOC ENFANT");
									this.childs.get(i).setEnfant(new_lego.childs.get(i));
									System.out.println(	this.childs.get(i).getEnfant());

								}
							}else {
								for(int i = 0; i < this.childs.size();i++) {
									this.childs.get(i).setEnfant(new_lego.childs.get(i));
									System.out.println(	this.childs.get(i).getEnfant());

								}

							}
						}
					}
					
			
					create3DAsset(group, model, new_lego);

					structure.getChildren().add(new_lego);
				}
				
			}
		}
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
		
		if(structure.getSelected_matiere() == null) {
			material2.setDiffuseColor(structure.getSelected_color());

		}else {
			material2.setDiffuseMap(new Image(getClass().getResourceAsStream(structure.getSelected_matiere())));

		}

		cylinder10.setMaterial(material2);
		cylinder5.setMaterial(material2);

		cylinder9.setMaterial(material2);

		cylinder7.setMaterial(material2);

		if (this.type.equals("BASE")) {
			cylinder7.setTranslateY(-1 * (model.getHeight()));
			cylinder9.setTranslateY(-1 * (model.getHeight()));

			cylinder10.setTranslateY(-1 * (model.getHeight()));

			cylinder5.setTranslateY(-1 * (model.getHeight()));

		} else if (model.getHeight() == 5) {
			cylinder7.setTranslateY(-1 * (this.getTotalHeight() - 20));
			cylinder9.setTranslateY(-1 * (this.getTotalHeight() - 20));

			cylinder10.setTranslateY(-1 * (this.getTotalHeight() - 20));

			cylinder5.setTranslateY(-1 * (this.getTotalHeight() - 20));

		} else {
			cylinder7.setTranslateY(-1 * (this.getTotalHeight() + model.getHeight() / 2));
			cylinder9.setTranslateY(-1 * (this.getTotalHeight() + model.getHeight() / 2));

			cylinder10.setTranslateY(-1 * (this.getTotalHeight() + model.getHeight() / 2));

			cylinder5.setTranslateY(-1 * (this.getTotalHeight() + model.getHeight() / 2));
		}

		structure.getChildren().add(cylinder9);
		structure.getChildren().add(cylinder5);
		structure.getChildren().add(cylinder7);
		structure.getChildren().add(cylinder10);
	}

	public void searshAndCreate(Cylinder cylindre) {
		
		
		Group g = (Group) cylindre.getParent();

		if (g.getChildren().size() > 5) {

			for (int i = 0; i < g.getChildren().size() - 1; i++) {

				if (g.getChildren().get(i).equals(cylindre)) {
					boolean drap = false;
					for (int j = i; j < g.getChildren().size(); j++) {

						if (g.getChildren().get(j).toString().contains("Lego") && drap == false) {
							Lego l = (Lego) g.getChildren().get(j);
							l.create_blocs();
							drap = true;
						}
					}
				}

			}

		} else {
			Lego l = (Lego) g.getChildren().get(4);
			l.create_blocs();
		}
	}

	public void searsh_cylinder(Color c, String texture) {
		
		
		if(!this.type.equals("BASE")) {
			
		
			int index = structure.getChildren().indexOf(this);
			
		
			for(int i = 1; i <= 4; i ++) {
				
				PhongMaterial m = (PhongMaterial) ((Shape3D) structure.getChildren().get(index - i)).getMaterial();
				if(texture == null) {
					m.setDiffuseColor(c);

				}else {
					m.setDiffuseColor(new Color(1,1,1,1));

					m.setDiffuseMap(new Image(getClass().getResourceAsStream(texture)));

				}
				
			}
		
		}
		
	}
	
	public int getTotalHeight() {

		int height = (int) (this.getHeight() / 2);

		Lego curr = this;

		while (curr.legoParent != null) {

			height += curr.getHeight();

			curr = curr.legoParent;

		}
		return height;
	}

	public void rotateLego(int angle) {
		this.setRotate(angle);

	}

	
	
	public Lego child() {
		return legoParent;
	}

	public void setLegoParent(Lego parent) {
		this.legoParent = parent;
	}
	
	public Lego getLegoParent() {
		return this.legoParent ;
	}

	public Lego getEnfant() {
		return enfant;
	}

	public void setEnfant(Lego enfant) {
		this.enfant = enfant;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Structure_3D getStructure() {
		return structure;
	}
	

	public void setStructure(Structure_3D structure) {
		this.structure = structure;
	}
	
	
	

}