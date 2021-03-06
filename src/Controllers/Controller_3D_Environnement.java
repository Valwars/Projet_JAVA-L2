package Controllers;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import Models.Lego;
import Models.Legos_collection;
import Models.PausableAnimationTimer;
import Models.Structure_3D;
import Models.ToggleSwitch;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Controller_3D_Environnement {

	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;

	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	private int i = 0;
	private Scene cssScene;
	private ChoiceDialog cd;

	public Button getPause_button() {
		return pause_button;
	}

	public void setPause_button(Button pause_button) {
		this.pause_button = pause_button;
	}

	private boolean first_init = false;

	private VBox vbx;

	@FXML
	private MenuBar menu_bar;

	@FXML
	private Button boutton_onglet, mute_sound, light_dark, btn_plus, btn_moins, previous_button, pause_button,
			next_button;

	@FXML
	private AnchorPane anch, anch_top, anch_cat;

	@FXML
	private BorderPane root;

	@FXML
	private ScrollPane Coulscrollpane, cat, Textscrollpane, Imagescrollpane;

	@FXML
	private MenuItem sauvegarder, managed_structures, aide, reglages;

	@FXML
	private Label l, lbl_clr_txtr, lbl_taille, lbl_taille2, orientation_label;

	private int BLOC_SIZE = 50;

	SubScene subscene;

	MediaPlayer player;

	GridPane panneau = new GridPane();

	public HashMap<String, Color> couleurs_correspond = new HashMap<String, Color>();
	public HashMap<String, String> textures_correspond = new HashMap<String, String>();

	public HashMap<Color, String> couleurs_correspond_reversed = new HashMap<Color, String>();
	public HashMap<String, String> textures_correspond_reversed = new HashMap<String, String>();

	String[] tab_couleur1 = { "-fx-base: royalblue;", "-fx-Base: green", "-fx-Base: khaki", "-fx-Base: snow",
			"-fx-Base: turquoise", "-fx-Base: darkgray", "-fx-Base: saddlebrown", "-fx-Base: black",
			"-fx-Base: darkorange", "-fx-Base: brown", "-fx-Base: red" };

	Label[] tab_categorie = { new Label("Cube"), new Label("Rectangle2"), new Label("Rectangle3"),
			new Label("Rectangle4"), new Label("Angle"), new Label("Tapis") };

	private Camera camera;
	private Camera firstPersoncamera;

	public Structure_3D structure;

	private Legos_collection leg;

	int x = 0;

	int couleur = 0;
	int matiere = 0;
	int a = 0;

	public String type;

	Color[] tab_couleur = { Color.ROYALBLUE, Color.GREEN, Color.KHAKI, Color.SNOW, Color.TURQUOISE, Color.DARKGRAY,
			Color.SADDLEBROWN, new Color(0.1, 0.1, 0.1, 1), Color.DARKORANGE, Color.BROWN, Color.RED,
			new Color(0.6, 0.6, 0.6, 0.6) };

	String[] tab_matiere = { "verre.png", "dirt.png", "wood.jpeg", "lave.jpeg", "feuille.png", "cobble.jpeg",
			"wood2.png" };

	int rota = 0;
	public int musiques = 0;
	boolean r = false;
	String[] rotations = { "DROITE", "GAUCHE", "AVANT", "ARRIERE" };

	String bloc = "RECTANGLE2_" + this.rotations[this.rota];

	public boolean isRotating = false;

	public PausableAnimationTimer timer = new PausableAnimationTimer() {
		@Override
		public void tick(long animationTime) {

			structure.rotateProperty().set(structure.getRotate() + 0.2);

		}
	};
	private Music_Controller mc;

	public Controller_3D_Environnement() {

		mc = new Music_Controller(this);

	}

	public void setScene(SubScene subscene) {

		this.subscene = subscene;

		camera = new PerspectiveCamera();

		subscene.setCamera(camera);
		camera.translateZProperty().set(-2000);

	}

	public void start(Stage primaryStage, Structure_3D st, SubScene subscene, String fileName)
			throws FileNotFoundException {

		this.structure = st;
		this.subscene = subscene;

		Image img = new Image(getClass().getResourceAsStream("cursor.png"));

		ImageCursor cursor = new ImageCursor(img, 10, 10);
		subscene.setCursor(cursor);

		System.out.println(structure);

		remplir_dico();
		remplir_dico2();
		File f = new File("sauvegardes/" + fileName + ".xml");

		if (f.exists() && !f.isDirectory()) {

			loadStructure(fileName);
		} else {

			structure.createBase();
		}

		anch.getChildren().remove(l);

		l = new Label("CUBE");
		anch.setBottomAnchor(l, 5.0);
		anch.setLeftAnchor(l, 7.0);
		anch.getChildren().add(l);

		anch.getChildren().remove(lbl_clr_txtr);

		lbl_clr_txtr = new Label("BLEU");
		anch.setBottomAnchor(lbl_clr_txtr, 5.0);
		anch.setLeftAnchor(lbl_clr_txtr, 100.0);
		anch.getChildren().add(lbl_clr_txtr);

		camera = new PerspectiveCamera();

		subscene.setCamera(camera);

		structure.translateXProperty().set(subscene.getWidth());
		structure.translateYProperty().set(subscene.getHeight() / 2);
		structure.translateZProperty().set(0);

		camera.translateZProperty().set(-2000);

		anchorAngleX = 0;
		anchorAngleY = 0;

		initMouseControl(st, subscene, primaryStage);

	}

	public void setEvent(Stage primaryStage) {

		EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				structure.getPressedKeys().add(e.getCode());

				System.out.println("event handler");
				listenerKeyboard(e);
			}
		};

		EventHandler<KeyEvent> eventHandler2 = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				structure.getPressedKeys().remove(e.getCode());

			}
		};

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, eventHandler2);

	}

	private void listenerKeyboard(KeyEvent event) {

		switch (event.getCode()) {

		case ESCAPE:

			Iterator<Lego> it2 = structure.getLego_selected().iterator();

			while (it2.hasNext()) {

				Lego l = it2.next();

				PhongMaterial m = (PhongMaterial) l.getMaterial();

				if (l.getCoul() != null) {
					String color = "#" + l.getCoul().split("x")[1];

					Color c = Color.valueOf(color);
					m.setDiffuseColor(c);

					if (!l.getType().equals("BASE")) {
						l.searsh_cylinder(c, null);

					}

				} else {
					m.setDiffuseColor(new Color(1, 1, 1, 1));

					String texture = l.getTexture();

					m.setDiffuseMap(new Image(getClass().getResourceAsStream("../Models/" + texture)));

					if (!l.getType().equals("BASE")) {
						l.searsh_cylinder(null, texture);

					}
				}

			}

			structure.setLast_lego_selected(null);
			structure.getLego_selected().clear();

			break;

		case J:

			Iterator<Lego> it = structure.getLego_selected().iterator();

			while (it.hasNext()) {

				Lego l = it.next();

				PhongMaterial m = (PhongMaterial) l.getMaterial();

				if (structure.getSelected_matiere() == null) {

					m.setDiffuseColor(this.tab_couleur[this.couleur]);
					l.setCoul(this.tab_couleur[this.couleur].toString());

				} else {
					m.setDiffuseColor(new Color(1, 1, 1, 1));

					m.setDiffuseMap(
							new Image(getClass().getResourceAsStream("../Models/" + structure.getSelected_matiere())));
					l.setTexture(structure.getSelected_matiere());

				}

				if (!l.getType().equals("BASE")) {
					l.searsh_cylinder(this.tab_couleur[this.couleur], structure.getSelected_matiere());

				}

			}

			structure.getLego_selected().clear();

			break;

		case L:
			sauvegarder();
			break;

		case Z:

			System.out.println("AVANT");
			camera.setTranslateY(camera.getTranslateY() - 5);

			break;

		case S:
			System.out.println("arriere");
			camera.setTranslateY(camera.getTranslateY() + 5);

			break;
		case Q:
			System.out.println("gauche");
			camera.setTranslateX(camera.getTranslateX() - 5);

			break;
		case D:
			System.out.println("droite");
			camera.setTranslateX(camera.getTranslateX() + 5);

			break;

		case R:
			structure.setRotationAxis(Rotate.Y_AXIS);
			spinAnimation();
			break;

		case W:
			if (isRotating == false) {
				structure.time_laps();

			}
			break;
		case T:
			if (structure.getPressedKeys().contains(KeyCode.SHIFT) && structure.getPressedKeys().size() == 2) {
				structure.move_sun();
			} else {
				structure.move_sun2();
			}
			break;
		case Y:

			structure.setTaille(0);
			lbl_taille.setText(0 + "");
			lbl_taille2.setText(1 + "");
			structure.setSelected_bloc("TAPIS");

			l.setText("TAPIS");

			break;
		case U:
			structure.setTaille(0);
			lbl_taille.setText(0 + "");
			lbl_taille2.setText(1 + "");
			structure.setSelected_bloc("CUBE");

			l.setText("CUBE");

			break;
		case I:
			structure.setTaille(0);
			lbl_taille.setText(0 + "");
			lbl_taille2.setText(2 + "");
			String bloc = "RECTANGLE2_" + this.rotations[this.rota];

			l.setText("RECTANGLE");

			System.out.println(rota);
			structure.setSelected_bloc(bloc);
			break;
		case O:
			structure.setTaille(0);
			lbl_taille.setText(0 + "");
			lbl_taille2.setText(3 + "");
			String bloc2 = "RECTANGLE3_" + this.rotations[this.rota];

			l.setText("RECTANGLE");

			structure.setSelected_bloc(bloc2);
			break;
		case P:

			structure.setTaille(0);
			lbl_taille.setText(0 + "");
			lbl_taille2.setText(4 + "");
			String bloc3 = "RECTANGLE4_" + this.rotations[this.rota];

			l.setText("RECTANGLE");

			structure.setSelected_bloc(bloc3);
			break;

		case K:

			structure.resetStructure();

			break;

		case H:

			if ((l.getText().equals("RECTANGLE") || l.getText().equals("ANGLE"))) {

				structure.setTaille(structure.getTaille() + 1);
				lbl_taille.setText(String.valueOf(Integer.valueOf(structure.getTaille())));
				lbl_taille2.setText(String.valueOf(Integer.valueOf(lbl_taille2.getText()) + 1));

			}

			break;

		case G:

			if (structure.getTaille() > 0) {

				if ((l.getText().equals("RECTANGLE") || l.getText().equals("ANGLE"))) {

					structure.setTaille(structure.getTaille() - 1);
					lbl_taille.setText(String.valueOf(Integer.valueOf(structure.getTaille())));
					lbl_taille2.setText(String.valueOf(Integer.valueOf(lbl_taille2.getText()) - 1));

				}

			}
			break;

		case F:
			String bloc6 = "ANGLE_" + this.rotations[this.rota];
			structure.setSelected_bloc(bloc6);

			l.setText("ANGLE");

			break;
		case B:
			structure.deleteLastBloc();
			System.out.println(structure.getChildren());
			break;

		case N:
			structure.recupDeletedBloc();
			break;

		case X:

			if (this.matiere == this.tab_matiere.length - 1) {
				this.matiere = 1;
			} else {
				this.matiere += 1;

			}
			structure.setSelected_matiere(this.tab_matiere[this.matiere]);
			anch.getChildren().remove(lbl_clr_txtr);

			lbl_clr_txtr = new Label(this.textures_correspond_reversed.get(this.tab_matiere[this.matiere]));
			anch.setBottomAnchor(lbl_clr_txtr, 5.0);
			anch.setLeftAnchor(lbl_clr_txtr, 100.0);
			anch.getChildren().add(lbl_clr_txtr);
			break;

		case C:

			structure.setSelected_matiere(null);

			if (this.couleur == this.tab_couleur.length - 1) {
				this.couleur = 0;
			} else {
				this.couleur += 1;

			}

			structure.setSelected_color(this.tab_couleur[this.couleur]);
			anch.getChildren().remove(lbl_clr_txtr);

			lbl_clr_txtr = new Label(this.couleurs_correspond_reversed.get(this.tab_couleur[this.couleur]));
			anch.setBottomAnchor(lbl_clr_txtr, 5.0);
			anch.setLeftAnchor(lbl_clr_txtr, 100.0);
			anch.getChildren().add(lbl_clr_txtr);
			break;

		case V:

			if (structure.getSelected_bloc() != "CUBE") {
				if (this.rota == this.rotations.length - 1) {
					this.rota = 0;
				} else {
					this.rota += 1;

				}
				String[] blocs = structure.getSelected_bloc().split("_");

				structure.setSelected_bloc(blocs[0] + "_" + this.rotations[rota]);
				orientation_label.setText(this.rotations[rota]);
			}

			break;

		case ENTER:

			Iterator<Lego> it3 = structure.getLego_selected().iterator();

			while (it3.hasNext()) {

				Lego l = it3.next();
				PhongMaterial m = (PhongMaterial) l.getMaterial();

				if (!l.getType().equals("BASE")) {
					int index = structure.getChildren().indexOf(l);
					structure.getChildren().remove(l);

					for (int i = 0; i < 4; i++) {
						structure.getChildren().remove(index - (i + 1));

					}
					l.getLegoParent().setEnfant(null);

				} else {
					System.out.println("");
					m.setDiffuseColor(Color.GREY);
					l.setCoul(Color.GREY.toString());
				}

			}

			structure.getLego_selected().clear();

			break;
			
		

		}
		
		
	}

	private void sauvegarder() {

		subscene.setCursor(Cursor.WAIT);
		BorderPane parent = (BorderPane) structure.getScene().getRoot();
		System.out.println(parent.getChildren());

		TabPane pane = (TabPane) parent.getChildren().get(2);
		if (structure.getChildren().size() > 1065) {

			if (structure.getNom_structure() == null) {

				TextInputDialog td = new TextInputDialog("Structure0");
				Stage stageAlert1 = (Stage) td.getDialogPane().getScene().getWindow();
				stageAlert1.setAlwaysOnTop(true);
				stageAlert1.toFront();
				td.setTitle("Sauvegarde");
				td.setContentText("Choisir un nom : ");

				td.setHeaderText("Sauvegarde...");

				boolean drap = false;

				while (drap == false) {

					Optional<String> result = td.showAndWait();

					File file = new File("sauvegardes/" + result.get() + ".xml");

					if (!file.exists()) {

						drap = true;

					}

					result.ifPresent(name_structure -> {

						if (!file.exists()) {
							subscene.setCursor(Cursor.WAIT);

							structure.setNom_structure("sauvegardes/" + name_structure + ".xml");
							structure.enregistrer();
							td.close();
							subscene.setCursor(Cursor.DEFAULT);

						} else {
							td.setHeaderText("Ce nom est d???j??? pris !");
						}

					});

				}

				SingleSelectionModel<Tab> selectionModel = pane.getSelectionModel();

				selectionModel.getSelectedItem().setText(structure.getNom_structure().split("/")[1].substring(0,
						structure.getNom_structure().split("/")[1].length() - 4));
				selectionModel.clearSelection(); // clear your selection

			} else {
				subscene.setCursor(Cursor.WAIT);

				structure.enregistrer();
				subscene.setCursor(Cursor.DEFAULT);

			}

		}

	}

	private void initMouseControl(Structure_3D group, SubScene scene, Stage stage) {
		Rotate xRotate;
		Rotate yRotate;

		group.getTransforms().addAll(xRotate = new Rotate(0, Rotate.X_AXIS), yRotate = new Rotate(0, Rotate.Y_AXIS)

		);

		xRotate.angleProperty().bind(angleX);
		yRotate.angleProperty().bind(angleY);

		scene.setOnMousePressed(event -> {

			if (this.isRotating == false) {
				anchorX = event.getSceneX();
				anchorY = event.getSceneY();
				anchorAngleX = angleX.get();
				anchorAngleY = angleY.get();

			}

		});

		scene.setOnMouseDragged(event -> {

			if (event.isPrimaryButtonDown() && this.isRotating == false) {
				angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
				angleY.set(anchorAngleY - (anchorX - event.getSceneX()));
			}

		});

		scene.addEventHandler(ScrollEvent.SCROLL, event -> {
			double delta = event.getDeltaY();
			group.translateZProperty().set(group.getTranslateZ() + delta);
		});

	}

	private void spinAnimation() {

		if (!timer.isActive || timer.isPaused) {
			timer.start();
			isRotating = true;
			structure.translateYProperty().set(700);
			structure.translateXProperty().set(1200 / 2);

			structure.translateZProperty().set(0);

		} else {
			timer.pause();
			isRotating = false;
			structure.setRotate(0);
			structure.translateYProperty().set(800 / 2);
		}

	}

	public GridPane container3 = new GridPane();

	public void ajout_couleur(boolean dark) {

		if (!this.first_init) {

			for (int i = 0; i < tab_couleur1.length; i++) {
				container3.getColumnConstraints().add(new ColumnConstraints(30));
				Button bt1 = new Button();
				bt1.setTranslateY(8);
				bt1.setPrefSize(25, 25);
				bt1.setStyle(tab_couleur1[i]);
				bt1.setId(LC[i]);
				int j = i;
				EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent evt) {

						if (bt1.getStyleClass().contains("btn_coul")) {

							bt1.getStyleClass().remove("btn_coul");
						} else {
							bt1.getStyleClass().add("btn_coul");

						}

						String c = bt1.getId();
						if (bt_active[j] == true && x == 0) {
							LCO.clear();
							for (int h = 0; h < bt_active.length; h++) {
								bt_active[h] = false;
							}
							LCO.add(c);
							bt_active[j] = true;
							x = 1;
						} else if (bt_active[j] == true) {
							LCO.remove(c);
							bt_active[j] = false;
						} else {
							LCO.add(c);
							bt_active[j] = true;
						}
						try {
							panneau_block(dark);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				};

				bt1.setOnAction(event);
				container3.add(bt1, i, 0);

			}

			this.Coulscrollpane.setContent(container3);

		}

		if (dark) {
			this.Coulscrollpane.getStylesheets().clear();

			this.Coulscrollpane.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			this.Coulscrollpane.getStyleClass().add("scroll-bar");
			container3.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			container3.getStyleClass().add("grid-pane");
		} else {
			this.Coulscrollpane.getStylesheets().clear();
			this.container3.getStylesheets().clear();
		}

	}

	GridPane container2 = new GridPane();

	public void ajout_texture(boolean dark) {

		if (!this.first_init) {
			for (int i = 0; i < tab_matiere.length; i++) {
				container2.getColumnConstraints().add(new ColumnConstraints(45));
				Button bt1 = new Button();
				bt1.getStyleClass().add("btn_text");
				bt1.setPrefSize(25, 25);
				Image img = new Image(getClass().getResourceAsStream("../Models/" + tab_matiere[i]));
				ImageView view = new ImageView(img);
				view.setPreserveRatio(true);
				view.setFitHeight(25);
				view.setFitWidth(25);
				bt1.setGraphic(view);
				bt1.setId(LC[i + 11]);
				int j = i;
				EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent evt) {
						if (bt1.getStyleClass().contains("btn_coul")) {

							bt1.getStyleClass().remove("btn_coul");
						} else {
							bt1.getStyleClass().add("btn_coul");

						}
						String t = bt1.getId();
						if (bt_active2[j] == true && x == 0) {
							LCO.clear();
							for (int h = 0; h < bt_active2.length; h++) {
								bt_active2[h] = false;
							}
							LCO.add(t);
							bt_active2[j] = true;
							x = 1;
						} else if (bt_active2[j] == true) {
							LCO.remove(t);
							bt_active2[j] = false;
						} else {
							LCO.add(t);
							bt_active2[j] = true;
						}
						try {
							panneau_block(dark);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				};

				bt1.setOnAction(event);
				container2.add(bt1, i, 0);

			}

			this.Textscrollpane.setContent(container2);

		}

		if (dark) {
			this.Textscrollpane.getStylesheets().clear();
			container.getStylesheets().clear();
			this.Textscrollpane.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			this.Textscrollpane.getStyleClass().add("scroll-bar");
			container2.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			container2.getStyleClass().add("grid-pane");
		} else {
			this.Textscrollpane.getStylesheets().clear();
			this.container2.getStylesheets().clear();

		}

	}

	public void remplir_dico() {
		String[] name = { "BLEU", "VERT", "SABLE", "NEIGE", "CYAN", "GRIS", "MARRON", "NOIR", "ORANGE", "ROUGE1",
				"ROUGE2", "TRANSPARENT" };

		for (int i = 0; i < name.length; i++) {
			this.couleurs_correspond.put(name[i], this.tab_couleur[i]);
			this.couleurs_correspond_reversed.put(this.tab_couleur[i], name[i]);

		}

	}

	public void remplir_dico2() {

		String[] name = { "VERRE", "TERRE", "BOIS", "LAVE", "FEUILLE", "PIERRE", "PLANCHE" };

		for (int i = 1; i < name.length; i++) {
			this.textures_correspond.put(name[i], this.tab_matiere[i]);
			this.textures_correspond_reversed.put(this.tab_matiere[i], name[i]);

		}

	}

	public GridPane container = new GridPane();;

	public void ajout_categorie(boolean dark) {

		if (!this.first_init) {

			container.setTranslateX(25);
			container.setTranslateY(25);
			Label lbl1 = new Label("Categorie :");
			anch_cat.getChildren().add(lbl1);
			anch_cat.setLeftAnchor(lbl1, 6.0);
			anch_cat.setTopAnchor(lbl1, 4.0);
			for (int i = 0; i < tab_categorie.length; i++) {

				container.getColumnConstraints().add(new ColumnConstraints(60));
				container.getRowConstraints().add(new RowConstraints(24));
				Label lbl = tab_categorie[i];
				CheckBox cbx = new CheckBox();
				lbl.setMinWidth(Region.USE_PREF_SIZE);

				cbx.setSelected(true);
				lbl.setTextFill(Color.GREY);

				EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent evt) {
						String f = lbl.getText().toUpperCase();

						if (cbx.isSelected()) {
							LFO.add(f);
							try {
								panneau_block(dark);
							} catch (FileNotFoundException e) {
								System.out.print("Erreur mon reuf");
								e.printStackTrace();
							}
						} else {
							LFO.remove(f);
							try {

								panneau_block(dark);
							} catch (FileNotFoundException e) {
								System.out.print("Erreur mon reuf");
								e.printStackTrace();
							}
						}

					}
				};
				cbx.setOnAction(event);
				container.add(lbl, 0, i);
				container.add(cbx, 3, i);

			}
			anch_cat.getChildren().add(container);
		}

		if (dark) {
			container.getStylesheets().clear();
			anch_cat.getStylesheets().clear();
			container.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			container.getStyleClass().add("background");
			anch_cat.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			anch_cat.getStyleClass().add("background");
		} else {
			container.getStylesheets().clear();
			anch_cat.getStylesheets().clear();
			container.getStylesheets().add(getClass().getResource("../Views/application.css").toExternalForm());
			container.getStyleClass().add("grid-pane-ligth");
			anch_cat.getStylesheets().add(getClass().getResource("../Views/application.css").toExternalForm());
			anch_cat.getStyleClass().add("grid-pane-ligth");

		}

	}

	@FXML
	private void initialize() throws FileNotFoundException {
		lbl_taille.setText("0");
		lbl_taille2.setText("1");
		orientation_label.setText(this.rotations[rota]);
		ajout_couleur(false);
		ajout_categorie(false);
		ajout_texture(false);
		remplissage_LFOLCO();
		panneau_block(false);
		this.first_init = true;
		ToggleSwitch toggle = new ToggleSwitch();
		anch_top.getChildren().add(toggle);
		anch_top.setRightAnchor(toggle, 200.0);
		anch_top.setBottomAnchor(toggle, 2.0);

		toggle.setOnMousePressed(event -> {
			if (!toggle.select().getValue()) {
				System.out.println("hello");
				ajout_categorie(true);

				ajout_couleur(true);
				ajout_texture(true);
				anch.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
				anch.getStyleClass().add("anchor-pane");
				menu_bar.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
				menu_bar.getStyleClass().add("menu-bar");

				dark();
				try {
					panneau_block(true);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				System.out.println("helloooooo");
				ajout_categorie(false);
				ajout_couleur(false);
				ajout_texture(false);
				anch.getStylesheets().clear();
				menu_bar.getStylesheets().clear();

				ligth();
				try {
					panneau_block(false);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		btn_plus.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if ((l.getText().equals("RECTANGLE") || l.getText().equals("ANGLE"))) {
					structure.setTaille(structure.getTaille() + 1);

					lbl_taille.setText(String.valueOf(Integer.valueOf(lbl_taille.getText()) + 1));
					lbl_taille2.setText(String.valueOf(Integer.valueOf(lbl_taille2.getText()) + 1));

				}

			}
		});
		btn_moins.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if ((l.getText().equals("RECTANGLE") || l.getText().equals("ANGLE")) && structure.getTaille() > 1) {
					structure.setTaille(structure.getTaille() - 1);
					lbl_taille.setText(String.valueOf(Integer.valueOf(lbl_taille.getText()) - 1));
					lbl_taille2.setText(String.valueOf(Integer.valueOf(lbl_taille2.getText()) - 1));

				}

			}
		});

		mute_sound.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (mute_sound.getText().equals("Mute")) {
					mc.mute();
					mute_sound.setText("Unmute");
				} else {
					mc.demute();
					mute_sound.setText("Mute");
				}

			}
		});

		aide.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				Stage stage = new Stage();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Help_window.fxml"));
				ScrollPane root;
				try {
					root = loader.load();
					Scene scene = new Scene(root);
					stage.setResizable(false);
					if (toggle.select().getValue()) {
						root.getStylesheets().clear();
						root.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
						root.getContent().getStyleClass().add("vbox");

					} else {
						root.getStylesheets().clear();
					}
					stage.setScene(scene);
					stage.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		reglages.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				Stage stage = new Stage();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/reglages.fxml"));

				loader.setController(mc);

				Pane root;
				try {
					root = loader.load();

					Scene scene = new Scene(root);
					stage.setResizable(false);

					if (toggle.select().getValue()) {
						root.getStylesheets().clear();
						root.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());

					} else {
						root.getStylesheets().clear();
					}
					stage.setScene(scene);
					stage.show();

					if (!mc.start) {
						mc.start();
					} else {
						mc.setImg();

					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		ImageView next = new ImageView(new Image(getClass().getResourceAsStream("next.png")));
		next.setPreserveRatio(true);
		next.setFitHeight(15);
		next.setFitWidth(15);
		next_button.setGraphic(next);

		ImageView previous = new ImageView(new Image(getClass().getResourceAsStream("previous.png")));
		previous.setPreserveRatio(true);
		previous.setFitHeight(15);
		previous.setFitWidth(15);
		previous_button.setGraphic(previous);

		ImageView start = new ImageView(new Image(getClass().getResourceAsStream("start.png")));
		start.setPreserveRatio(true);
		start.setFitHeight(15);
		start.setFitWidth(15);

		pause_button.setGraphic(start);

		pause_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (!mc.start) {
					mc.start();
				}
				ImageView pause;
				if (mc.sp) {
					pause = new ImageView(new Image(getClass().getResourceAsStream("pause.png")));
					mc.sp = true;

				} else {
					pause = new ImageView(new Image(getClass().getResourceAsStream("start.png")));
					mc.sp = false;
				}

				pause.setPreserveRatio(true);
				pause.setFitHeight(15);
				pause.setFitWidth(15);
				pause_button.setGraphic(pause);
				mc.playMedia();
			}

		});

		next_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				mc.nextMedia();
			}
		});

		previous_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				mc.previousMedia();
			}
		});

		sauvegarder.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				sauvegarder();

			}
		});

		managed_structures.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				Stage stage = new Stage();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/gestion_structures.fxml"));

				ScrollPane root;

				try {
					root = (ScrollPane) loader.load();

					VBox v = (VBox) root.getContent();
					File file = new File("sauvegardes");
					if (file.isDirectory()) {

						if (file.list().length != 0) {

							for (int i = 1; i < file.list().length; i++) {
								HBox b = new HBox();
								b.getStyleClass().add("hbox");

								Label l = new Label(file.list()[i].substring(0, file.list()[i].length() - 4));
								l.setPrefSize(100, 0);
								Button inp = new Button("SUPPRIMER");
								inp.getStyleClass().add("inp");

								inp.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {

										Alert alert = new Alert(AlertType.CONFIRMATION);
										alert.setTitle("Supprimer structure");
										String s = "Voulez vous vraiment supprimer " + l.getText() + " ?";
										alert.setContentText(s);

										Optional<ButtonType> result = alert.showAndWait();

										if ((result.isPresent()) && (result.get() == ButtonType.OK)) {

											File file = new File("sauvegardes/" + l.getText() + ".xml");

											if (file.delete()) {
												System.out.println(file.getName() + " est supprim??.");
												v.getChildren().remove(b);
											} else {
												System.out.println("Op??ration de suppression echou??e");
											}
										}

									}
								});

								Insets margin = new Insets(0, 0, 0, 100);
								b.setMargin(inp, margin);
								b.getChildren().add(l);
								b.getChildren().add(inp);

								v.getChildren().add(b);
							}

						}
					}
					Scene scene = new Scene(root);
					if (toggle.select().getValue()) {
						v.getStylesheets().clear();
						v.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
						v.getStyleClass().add("vbox");
					} else {
						scene.getStylesheets().clear();
						scene.getStylesheets()
								.add(getClass().getResource("../Views/structures_gestion.css").toExternalForm());
					}

					stage.setResizable(false);

					stage.setScene(scene);
					stage.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	public String[] LC = { "BLEU", "VERT", "SABLE", "NEIGE", "CYAN", "GRIS", "MARRON", "NOIR", "ORANGE", "ROUGE1",
			"ROUGE2", "TRANSPARENT", "TERRE", "BOIS", "LAVE", "FEUILLE", "PIERRE", "PLANCHE" };

	public Boolean[] bt_active = { true, true, true, true, true, true, true, true, true, true, true };

	public Boolean[] bt_active2 = { true, true, true, true, true, true, true };

	public ArrayList<String> LFO = new ArrayList<String>();
	public ArrayList<String> LCO = new ArrayList<String>();

	public void remplissage_LFOLCO() {

		LCO.add("BLEU");
		LCO.add("VERT");
		LCO.add("SABLE");
		LCO.add("NEIGE");
		LCO.add("CYAN");
		LCO.add("GRIS");
		LCO.add("MARRON");
		LCO.add("NOIR");
		LCO.add("ORANGE");
		LCO.add("ROUGE1");
		LCO.add("ROUGE2");
		LCO.add("TRANSPARENT");
		LCO.add("TERRE");
		LCO.add("BOIS");
		LCO.add("LAVE");
		LCO.add("FEUILLE");
		LCO.add("PIERRE");
		LCO.add("PLANCHE");
		LFO.add("CUBE");
		LFO.add("RECTANGLE2");
		LFO.add("RECTANGLE3");
		LFO.add("RECTANGLE4");
		LFO.add("ANGLE");
		LFO.add("TAPIS");

	}

	public void panneau_block(boolean dark) throws FileNotFoundException {

		int x = 0;
		int y = 0;

		panneau.getStylesheets().clear();
		panneau.getChildren().clear();

		this.Imagescrollpane.setContent(panneau);

		for (int i = 0; i < LFO.size(); i++) {
			for (int j = 0; j < LCO.size(); j++) {
				panneau.add(afficher_block(LFO.get(i), LCO.get(j)), x, y);
				if (x < 2) {
					x += 1;
				} else if (x == 2) {
					x = 0;
					y += 1;
				}
			}
		}

		if (dark) {

			this.Imagescrollpane.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			this.Imagescrollpane.getStyleClass().add("scroll-bar");
			panneau.getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
			panneau.getStyleClass().add("grid-pane");

		} else {

			this.Imagescrollpane.setContent(panneau);
			this.Imagescrollpane.getStylesheets().clear();
			panneau.getStylesheets().add(getClass().getResource("../Views/application.css").toExternalForm());
			panneau.getStyleClass().add("pic");

		}
	}

	public BorderPane afficher_block(String f, String c) throws FileNotFoundException {

		Image img = new Image("File:images/" + f + "_" + c + ".png");
		ImageView pic = new ImageView(img);
		BorderPane imageViewWrapper = new BorderPane(pic);

		imageViewWrapper.getStyleClass().add("pic");

		pic.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

			System.out.println(f);
			String size = f.substring(f.length() - 1, f.length());
			if (!f.equals("CUBE") && !f.equals("TAPIS")) {
				if (f.substring(0, f.length() - 1).equals("RECTANGLE")) {
					structure.setTaille(0);
					lbl_taille.setText(0 + "");
					lbl_taille2.setText(size);
					l.setText(f.substring(0, f.length() - 1));

				} else {
					structure.setTaille(0);
					lbl_taille.setText(0 + "");
					lbl_taille2.setText(size);
					l.setText(f);
				}
				orientation_label.setText(this.rotations[rota]);

				structure.setSelected_bloc(f + "_" + rotations[rota]);

			} else {
				structure.setTaille(0);
				lbl_taille.setText(0 + "");
				lbl_taille2.setText(1 + "");
				structure.setSelected_bloc(f);
				l.setText(f);

			}
			if (this.couleurs_correspond.get(c) != null) {
				lbl_clr_txtr.setText(c);
				structure.setSelected_matiere(null);
				structure.setSelected_color(this.couleurs_correspond.get(c));

			} else {
				lbl_clr_txtr.setText(c);
				structure.setSelected_matiere(this.textures_correspond.get(c));

			}

		});

		pic.getStyleClass().add("pic");

		pic.setFitWidth(93);
		pic.setFitHeight(70);

		return imageViewWrapper;

	}

	public void loadStructure(String fileName) throws FileNotFoundException {

		XMLDecoder decoder = null;

		FileInputStream fis = new FileInputStream(new File("sauvegardes/" + fileName + ".xml"));
		BufferedInputStream bos = new BufferedInputStream(fis);

		decoder = new XMLDecoder(bos);

		ArrayList<Shape3D> l = (ArrayList<Shape3D>) decoder.readObject();

		for (int i = 0; i < l.size(); i++) {

			Shape3D shape = l.get(i);

			PhongMaterial material = new PhongMaterial();

			if (i < 1602 && shape.getClass() != Cylinder.class) {

				((Lego) shape).setStructure(structure);

				material.setDiffuseColor(Color.GREY);
			} else {
				if (shape.getClass() == Lego.class) {

					((Lego) shape).setStructure(structure);

					if (((Lego) shape).getCoul() != null) {
						String color = "#" + ((Lego) shape).getCoul().split("x")[1];

						Color c = Color.valueOf(color);
						material.setDiffuseColor(c);
					} else {
						String texture = ((Lego) shape).getTexture();

						material.setDiffuseMap(new Image(getClass().getResourceAsStream("../Models/" + texture)));
					}

					for (int j = 1; j < 5; j++) {
						Shape3D shape2 = l.get(i - j);
						shape2.setMaterial(material);
						shape2.setOnMousePressed(event -> {
							if (event.isSecondaryButtonDown() && ((Lego) shape).getEnfant() == null) {

								System.out.println("JE CLIQUE SUR CE PTN DE CYLINDRE");
								Group g = (Group) shape2.getParent();

								if (g.getChildren().size() > 5) {

									for (int k = 0; k < g.getChildren().size() - 1; k++) {

										if (g.getChildren().get(k).equals(shape2)) {
											boolean drap = false;
											for (int o = k; o < g.getChildren().size(); o++) {

												if (g.getChildren().get(o).toString().contains("Lego")
														&& drap == false) {
													Lego le = (Lego) g.getChildren().get(o);
													le.create_blocs();
													drap = true;
												}
											}
										}

									}

								} else {
									Lego lz = (Lego) g.getChildren().get(4);
									lz.create_blocs();
								}

							}

						});

					}

				}

			}

			shape.setMaterial(material);

			structure.getChildren().add(l.get(i));

		}

		structure.setSelected_bloc("CUBE");
		structure.prepareLight();
		structure.setNom_structure(((Lego) structure.getChildren().get(0)).getParent_name());
	}

	public void setCssScene(Scene sc) {
		this.cssScene = sc;
	}

	public void set_dark_dialogue(ChoiceDialog dialog) {
		this.cd = dialog;

	}

	public void dark() {
		this.cssScene.getStylesheets().clear();
		this.cssScene.getStylesheets().add(getClass().getResource("../Views/dark_tab_pane.css").toExternalForm());
		// this.cd.getDialogPane().getStylesheets().add(getClass().getResource("../Views/dark.css").toExternalForm());
		// this.cd.getDialogPane().getStyleClass().add("choice-dialog");
	}

	public void ligth() {
		this.cssScene.getStylesheets().clear();
		this.cssScene.getStylesheets().add(getClass().getResource("../Views/application.css").toExternalForm());

		// this.cd.getDialogPane().getStylesheets().clear();

	}

}
