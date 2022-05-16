package Models;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Structure_3D extends Group implements Serializable {

	private static final long serialVersionUID = 1L;

	private Legos_collection legos_collections;

	private Stack<Node> deleted_blocs;

	private String selected_bloc = "BASE";

	private Color selected_color = Color.ROYALBLUE;

	private String selected_matiere = null;

	private Set<Lego> lego_selected = new HashSet<Lego>();

	private Lego last_lego_selected;

	public Lego getLast_lego_selected() {
		return last_lego_selected;
	}

	public void setLast_lego_selected(Lego last_lego_selected) {
		this.last_lego_selected = last_lego_selected;
	}

	private final Set<KeyCode> pressedKeys = new HashSet<>();

	public Set<KeyCode> getPressedKeys() {
		return pressedKeys;
	}

	public Set<Lego> getLego_selected() {
		return lego_selected;
	}

	public void setLego_selected(Set<Lego> lego_selected) {
		this.lego_selected = lego_selected;
	}

	private int taille = 0;

	private int BLOC_SIZE;

	private int PLATEAU_TAILLE = 20;

	private int sun_orientation = 0;

	private PointLight pointLight;
	private Sphere sphere;

	private String nom_structure;

	public String getNom_structure() {
		return nom_structure;
	}

	public void setNom_structure(String nom_structure) {
		this.nom_structure = nom_structure;
	}

	public Structure_3D() {

	}

	public Legos_collection getLegos_collections() {
		return legos_collections;
	}

	public void setLegos_collections(Legos_collection legos_collections) {
		this.legos_collections = legos_collections;
	}

	public Stack<Node> getDeleted_blocs() {
		return deleted_blocs;
	}

	public void setDeleted_blocs(Stack<Node> deleted_blocs) {
		this.deleted_blocs = deleted_blocs;
	}

	public String getSelected_bloc() {
		return selected_bloc;
	}

	public void setSelected_bloc(String selected_bloc) {
		this.selected_bloc = selected_bloc;
	}

	public Color getSelected_color() {
		return selected_color;
	}

	public void setSelected_color(Color selected_color) {
		this.selected_color = selected_color;
	}

	public String getSelected_matiere() {
		return selected_matiere;
	}

	public void setSelected_matiere(String selected_matiere) {
		this.selected_matiere = selected_matiere;
	}

	public int getTaille() {
		return taille;
	}

	public void setTaille(int taille) {
		this.taille = taille;
	}

	public int getBLOC_SIZE() {
		return BLOC_SIZE;
	}

	public void setBLOC_SIZE(int bLOC_SIZE) {
		BLOC_SIZE = bLOC_SIZE;
	}

	public int getPLATEAU_TAILLE() {
		return PLATEAU_TAILLE;
	}

	public void setPLATEAU_TAILLE(int pLATEAU_TAILLE) {
		PLATEAU_TAILLE = pLATEAU_TAILLE;
	}

	public int getSun_orientation() {
		return sun_orientation;
	}

	public void setSun_orientation(int sun_orientation) {
		this.sun_orientation = sun_orientation;
	}

	public PointLight getPointLight() {
		return pointLight;
	}

	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}

	public Sphere getSphere() {
		return sphere;
	}

	public void setSphere(Sphere sphere) {
		this.sphere = sphere;
	}

	public PausableAnimationTimer getTimer() {
		return timer;
	}

	public void setTimer(PausableAnimationTimer timer) {
		this.timer = timer;
	}

	public void enregistrer() {

		File fichier;

		if (this.nom_structure != null) {
			System.out.println("le fichier existe déjà, on l'écrase");
			fichier = new File(this.nom_structure);
		} else {
			System.out.println("Aucune sauvergarde de cette structure, on en créer une");

			fichier = new File("sauvegardes/sauvegarde0.xml");

			int j = 0;

			while (fichier.exists()) {
				fichier = new File("sauvegardes/sauvegarde" + j + ".xml");
				j += 1;
			}

			this.nom_structure = "sauvegardes/sauvegarde" + j + ".xml";

		}

		
		this.getChildren().remove(1600);
		this.getChildren().remove(1600);

		ArrayList<Shape3D> array = new ArrayList<Shape3D>();

		for (int i = 0; i < this.getChildren().size(); i++) {

			if (this.getChildren().get(i).getClass() == Lego.class) {
				Lego l = (Lego) this.getChildren().get(i);
				l.setParent_name(this.nom_structure);
			}

			array.add((Shape3D) this.getChildren().get(i));
		}

		XMLEncoder encoder = null;

		try {
			FileOutputStream fos = new FileOutputStream(fichier);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			encoder = new XMLEncoder(bos);
			encoder.writeObject(array);

			encoder.flush();

			this.prepareLight();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.nom_structure);
	}

	public void prepareLight() {

		PointLight pointLight = new PointLight();

		pointLight.getTransforms().add(new Translate(0, -500, -1500));
		pointLight.setRotationAxis(Rotate.X_AXIS);
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.RED);

		Sphere sphere = new Sphere(10);

		sphere.getTransforms().setAll(pointLight.getTransforms());
		sphere.rotateProperty().bind(pointLight.rotateProperty());
		sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());

		this.sphere = sphere;
		this.pointLight = pointLight;
		this.getChildren().add(1600, sphere);
		this.getChildren().add(1600, pointLight);

	}

	public PausableAnimationTimer timer = new PausableAnimationTimer() {
		@Override
		public void tick(long animationTime) {
			pointLight.setRotate(pointLight.getRotate() - 2);
		}
	};

	public Structure_3D(int bs) {
		this.BLOC_SIZE = bs;
		deleted_blocs = new Stack<Node>();
	}

	public void resetStructure() {
		this.selected_bloc = "BASE";
		this.getChildren().clear();
		createBase();

	}

	public void deleteLastBloc() {
		System.out.println(this);
		if (this.getChildren().size() > (PLATEAU_TAILLE * 4 + 1)) {

			try {
				

				System.out.println(this.getChildren());

				if (this.getChildren().get(this.getChildren().size() - 1).getClass() == Lego.class) {

					Lego l = (Lego) this.getChildren().get(this.getChildren().size() - 1);
					
					if(this.lego_selected.contains(l)) {
						this.lego_selected.remove(l);
					}
					Lego model = this.getLegos_collections().getLegos().get(l.getType());

					if (Math.abs(model.getWidth()) > this.BLOC_SIZE && Math.abs(model.getDepth()) > this.BLOC_SIZE) {

						for (int i = 0; i < ((Math.abs(model.getWidth()) + (this.getBLOC_SIZE() * l.getTaille()))
								/ this.getBLOC_SIZE()) * 2 - 1; i++) {

							Lego child = (Lego) this.getChildren().get(this.getChildren().size() - 1);
							child.getLegoParent().setEnfant(null);

							this.getChildren().remove(this.getChildren().size() - 1);

							for (int j = 0; j < 4; j++) {

								deleted_blocs.push(this.getChildren().get(this.getChildren().size() - 1));
								this.getChildren().remove(this.getChildren().size() - 1);

							}

							deleted_blocs.push(child);

						}

						for (int i = 0; i < l.getChilds().size(); i++) {
							l.getLegoParent().getChilds().get(i).setEnfant(null);
							System.out.println(l.getLegoParent().getChilds().get(i).getEnfant());
						}

					} else if (Math.abs(model.getWidth()) > this.BLOC_SIZE) {

						for (int i = 0; i < ((Math.abs(model.getWidth()) + (this.getBLOC_SIZE() * l.getTaille()))
								/ this.getBLOC_SIZE()); i++) {

							Lego child = (Lego) this.getChildren().get(this.getChildren().size() - 1);
							child.getLegoParent().setEnfant(null);

							this.getChildren().remove(this.getChildren().size() - 1);

							for (int j = 0; j < 4; j++) {

								deleted_blocs.push(this.getChildren().get(this.getChildren().size() - 1));
								this.getChildren().remove(this.getChildren().size() - 1);

							}

							deleted_blocs.push(child);

						}
						if (l.getDirection() != null) {

							if (l.getLegoParent().getDirection().equals(l.getDirection())) {
								System.out.println("MEME DIRECTION");

								if (l.getLegoParent().getChilds().size() >= l.getChilds().size()) {
									for (int i = 0; i < l.getChilds().size(); i++) {

										System.out.println("BLOC ENFANT");
										l.getLegoParent().getChilds().get(i).setEnfant(null);
										System.out.println(l.getLegoParent().getChilds().get(i).getEnfant());

									}
								} else {
									for (int i = 0; i < l.getLegoParent().getChilds().size(); i++) {
										l.getLegoParent().getChilds().get(i).setEnfant(null);
										System.out.println(l.getChilds().get(i).getEnfant());

									}

								}
							}
						}

					} else if (Math.abs(model.getDepth()) > this.BLOC_SIZE) {

						for (int i = 0; i < ((Math.abs(model.getDepth()) + (this.getBLOC_SIZE() * l.getTaille()))
								/ this.getBLOC_SIZE()); i++) {

							Lego child = (Lego) this.getChildren().get(this.getChildren().size() - 1);
							child.getLegoParent().setEnfant(null);

							this.getChildren().remove(child);

							for (int j = 0; j < 4; j++) {

								deleted_blocs.push(this.getChildren().get(this.getChildren().size() - 1));
								this.getChildren().remove(this.getChildren().size() - 1);

							}

							deleted_blocs.push(child);

						}

						if (l.getDirection() != null) {

							if (l.getLegoParent().getDirection().equals(l.getDirection())) {
								System.out.println("MEME DIRECTION");

								if (l.getLegoParent().getChilds().size() >= l.getChilds().size()) {
									for (int i = 0; i < l.getChilds().size(); i++) {

										System.out.println("BLOC ENFANT");
										l.getLegoParent().getChilds().get(i).setEnfant(null);
										System.out.println(l.getLegoParent().getChilds().get(i).getEnfant());

									}
								} else {
									for (int i = 0; i < l.getLegoParent().getChilds().size(); i++) {
										l.getLegoParent().getChilds().get(i).setEnfant(null);
										System.out.println(l.getChilds().get(i).getEnfant());

									}

								}
							}
						}

					} else {

						l.getLegoParent().setEnfant(null);
						this.getChildren().remove(this.getChildren().size() - 1);

						for (int i = 0; i < 4; i++) {

							deleted_blocs.push(this.getChildren().get(this.getChildren().size() - 1));
							this.getChildren().remove(this.getChildren().size() - 1);

						}

						deleted_blocs.push(l);

					}

				}

			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	public void move_sun2() {
		this.pointLight.setRotationAxis(new Point3D(200, 0, 0));

		this.pointLight.setRotate(pointLight.getRotate() - 25);

	}

	public void move_sun() {

		// Setting pivot points for the rotation
		this.pointLight.setRotationAxis(new Point3D(0, 200, 0));

		this.pointLight.setRotate(pointLight.getRotate() - 25);

	}

	public void time_laps() {

		if (!timer.isActive || timer.isPaused) {
			timer.start();
		} else {
			timer.pause();
		}

	}

	public void recupDeletedBloc() {
		try {

			Lego l = (Lego) deleted_blocs.peek();

			Lego model = this.getLegos_collections().getLegos().get(l.getType());

			if (Math.abs(model.getWidth()) > this.BLOC_SIZE && Math.abs(model.getDepth()) > this.BLOC_SIZE) {

				for (int i = 0; i < ((Math.abs(model.getWidth()) + (this.getBLOC_SIZE() * l.getTaille()))
						/ this.getBLOC_SIZE()) * 2 - 1; i++) {

					Lego lego = (Lego) deleted_blocs.pop();
					lego.setEnfant(null);
					for (int j = 0; j < 4; j++) {

						Cylinder child = (Cylinder) deleted_blocs.pop();
						this.getChildren().add(child);
					}

					this.getChildren().add(lego);

				}

			} else if (Math.abs(model.getWidth()) > this.BLOC_SIZE) {

				for (int i = 0; i < ((Math.abs(model.getWidth()) + (this.getBLOC_SIZE() * l.getTaille()))
						/ this.getBLOC_SIZE()); i++) {

					Lego lego = (Lego) deleted_blocs.pop();
					lego.setEnfant(null);

					for (int j = 0; j < 4; j++) {

						Cylinder child = (Cylinder) deleted_blocs.pop();
						this.getChildren().add(child);
					}

					this.getChildren().add(lego);

				}

			} else if (Math.abs(model.getDepth()) > this.BLOC_SIZE) {

				for (int i = 0; i < ((Math.abs(model.getDepth()) + (this.getBLOC_SIZE() * l.getTaille()))
						/ this.getBLOC_SIZE()); i++) {
					Lego lego = (Lego) deleted_blocs.pop();
					lego.setEnfant(null);

					for (int j = 0; j < 4; j++) {

						Cylinder child = (Cylinder) deleted_blocs.pop();
						this.getChildren().add(child);
					}

					this.getChildren().add(lego);

				}

			} else if (Math.abs(model.getDepth()) <= this.BLOC_SIZE && Math.abs(model.getWidth()) <= this.BLOC_SIZE) {
				Lego lego = (Lego) deleted_blocs.pop();
				lego.setEnfant(null);

				for (int j = 0; j < 4; j++) {

					Cylinder child = (Cylinder) deleted_blocs.pop();
					this.getChildren().add(child);

				}

				this.getChildren().add(lego);

			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void setCollec(Legos_collection leg) {
		this.legos_collections = leg;
	}

	public void createBase() {

		int x = 0;
		int y = 0;

		Lego pateforme_de_construction;

		for (int k = 0; k < 4; k++) {

			for (int i = 0; i < PLATEAU_TAILLE; i++) {

				for (int j = 0; j < PLATEAU_TAILLE; j++) {

					pateforme_de_construction = create_selectedBlock();
					pateforme_de_construction.setTranslateX(x);
					pateforme_de_construction.setTranslateZ(y);

					this.getChildren().add(pateforme_de_construction);

					if (k == 0 || k == 2) {
						x += (this.BLOC_SIZE + 1);
					} else if (k == 1 || k == 3) {
						x -= (this.BLOC_SIZE + 1);
					}

				}

				if (k == 0 || k == 3) {
					y -= (this.BLOC_SIZE + 1);
				} else if (k == 1 || k == 2) {
					y += (this.BLOC_SIZE + 1);
				}

				x = 0;
			}

			x = 0;
			y = 0;
		}
		prepareLight();

		this.selected_bloc = "CUBE";
	}

	public Lego create_selectedBlock() {
		return new Lego(this.legos_collections.getLegos().get(selected_bloc).getWidth(),
				this.legos_collections.getLegos().get(selected_bloc).getHeight(),
				this.legos_collections.getLegos().get(selected_bloc).getDepth(), selected_bloc, null, this);
	}

}
