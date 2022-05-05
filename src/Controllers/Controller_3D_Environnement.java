package Controllers;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import Models.Lego;
import Models.Legos_collection;
import Models.PausableAnimationTimer;
import Models.Structure_3D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller_3D_Environnement {

	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;

	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	private int i = 0;
	@FXML
	private Button boutton_onglet;

	@FXML
	private AnchorPane anch;

	@FXML
	private BorderPane root;

	@FXML
	private ScrollPane Coulscrollpane;

	@FXML
	private ScrollPane cat;

	@FXML
	private Button mute_sound;

	private int BLOC_SIZE = 50;

	@FXML
	private TextField searsh_bar;

	@FXML
	private Button light_dark;

	SubScene subscene;

	MediaPlayer player;

	String[] tab_couleur1 = { "-fx-Base: #4169E1", "-fx-Base: #006400", "-fx-Base: #F0E68C", "-fx-Base: #FFFFF0",
			"-fx-Base: #40E0D0", "-fx-Base: #8B4513", "-fx-Base: #FF8C00", "-fx-Base: #A9A9A9", "-fx-Base: #8b4513",
			"-fx-Base: #FF0000", "-fx-Base: #FFFAFA" };
	Label[] tab_categorie = { new Label("Cube"), new Label("Angle"), new Label("Rectangle"), new Label("Tapis"),
			new Label("Texture") };

	private Camera camera;
	private Camera firstPersoncamera;

	public Structure_3D structure;

	private Legos_collection leg;

	int couleur = 0;
	int matiere = 0;

	public String type;

	Color[] tab_couleur = { Color.ROYALBLUE, Color.GREEN, Color.KHAKI, Color.IVORY, Color.TURQUOISE,
			new Color(0.6, 0.6, 0.6, 0.6), Color.BROWN, Color.DARKORANGE, Color.DARKGRAY, Color.SADDLEBROWN, Color.RED,
			Color.SNOW, new Color(0.1, 0.1, 0.1, 1) };

	String[] tab_matiere = { "cobble.jpeg", "dirt.png", "lave.jpeg", "wood.jpeg", "feuille.png", "wood2.png" };

	int rota = 0;

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
	/*
	public Controller_3D_Environnement() {
		
		Media buzzer = new Media(getClass().getResource("back_music.mp3").toExternalForm());
		player = new MediaPlayer(buzzer);
		player.setOnEndOfMedia(new Runnable() {
			public void run() {
				player.seek(Duration.ZERO);
			}
		});
		player.play();


	}
	*/

	public void start(Stage primaryStage, Structure_3D st, SubScene subscene, String fileName)
			throws FileNotFoundException {

		this.structure = st;
		this.subscene = subscene;

		Image img = new Image(getClass().getResourceAsStream("cursor.png"));

		ImageCursor cursor = new ImageCursor(img, 10, 10);
		subscene.setCursor(cursor);

		System.out.println(structure);

		File f = new File("sauvegardes/" + fileName + ".xml");

		if (f.exists() && !f.isDirectory()) {
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

						}

					}

				}

				shape.setMaterial(material);

				structure.getChildren().add(l.get(i));

			}

			structure.setSelected_bloc("CUBE");
			structure.prepareLight();
			structure.setNom_structure(((Lego) structure.getChildren().get(0)).getParent_name());

		} else {

			structure.createBase();
		}

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
				System.out.println("event handler");
				listenerKeyboard(e);
			}
		};

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);

	}

	private void listenerKeyboard(KeyEvent event) {
		System.out.println("je clique");
		switch (event.getCode()) {

		case L:
			subscene.setCursor(Cursor.WAIT);
			BorderPane parent = (BorderPane) structure.getScene().getRoot();
			TabPane pane = (TabPane) parent.getChildren().get(2);

			if (structure.getChildren().size() > 1065) {

				if (structure.getNom_structure() == null) {

					TextInputDialog td = new TextInputDialog("Structure0");
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
								td.setHeaderText("Ce nom est d�j� pris !");
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
			structure.move_sun2();
			break;
		case Y:
			// structure.move_sun();

			structure.setTaille(0);

			structure.setSelected_bloc("TAPIS");
			break;
		case U:
			structure.setTaille(0);

			structure.setSelected_bloc("CUBE");
			break;
		case I:
			structure.setTaille(0);

			String bloc = "RECTANGLE2_" + this.rotations[this.rota];
			System.out.println(rota);
			structure.setSelected_bloc(bloc);
			break;
		case O:
			structure.setTaille(0);

			String bloc2 = "RECTANGLE3_" + this.rotations[this.rota];
			structure.setSelected_bloc(bloc2);
			break;
		case P:

			structure.setTaille(0);

			String bloc3 = "RECTANGLE4_" + this.rotations[this.rota];
			structure.setSelected_bloc(bloc3);
			break;

		case K:

			structure.setTaille(0);

			String bloc5 = "RECTANGLE5_" + this.rotations[this.rota];
			structure.setSelected_bloc(bloc5);
			break;

		case H:
			structure.setTaille(structure.getTaille() + 1);
			break;
		case G:
			if (structure.getTaille() > 0) {
				structure.setTaille(structure.getTaille() - 1);

			}
			break;

		case F:
			String bloc6 = "ANGLE_" + this.rotations[this.rota];
			structure.setSelected_bloc(bloc6);
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
				this.matiere = 0;
			} else {
				this.matiere += 1;

			}
			structure.setSelected_matiere(this.tab_matiere[this.matiere]);
			break;

		case C:

			structure.setSelected_matiere(null);

			if (this.couleur == this.tab_couleur.length - 1) {
				this.couleur = 0;
			} else {
				this.couleur += 1;

			}

			structure.setSelected_color(this.tab_couleur[this.couleur]);
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
			}

			break;

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

		stage.addEventHandler(ScrollEvent.SCROLL, event -> {
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

	public void ajout_couleur() {
		GridPane container = new GridPane();
		for (int i = 0; i < tab_couleur1.length - 1; i++) {
			container.getColumnConstraints().add(new ColumnConstraints(30));
			Button bt1 = new Button();
			bt1.setPrefSize(25, 25);
			bt1.setStyle(tab_couleur1[i]);
			bt1.setId("bt" + String.valueOf(i) + "color");
			EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent evt) {
					changer_couleur(bt1);
				}
			};
			bt1.setOnAction(event);
			container.add(bt1, i, 0);

		}
		this.Coulscrollpane.setContent(container);

	}

	public void changer_couleur(Button bt) {
		structure.setSelected_matiere(null);
		int clr = 0;
		for (int i = 0; i < this.tab_couleur1.length; i++) {
			if (this.tab_couleur1[i] == bt.getStyle()) {
				clr = i;
			}
		}
		structure.setSelected_color(this.tab_couleur[clr]);
	}

	public void ajout_categorie() {
		GridPane container = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		container.getColumnConstraints().add(col1);
		Label lbl1 = new Label("Categorie :");
		container.add(lbl1, 0, 0);
		for (int i = 0; i < tab_categorie.length; i++) {
			container.getColumnConstraints().add(new ColumnConstraints(20));
			container.getRowConstraints().add(new RowConstraints(20));
			Label lbl = tab_categorie[i];
			CheckBox cbx = new CheckBox();
			lbl.setTextFill(Color.GREY);
			EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent evt) {
					changer_categorie(cbx, lbl);
				}
			};
			cbx.setOnAction(event);
			container.add(lbl, 0, i + 1);
			container.add(cbx, 1, i + 1);

		}
		this.cat.setContent(container);

	}

	public void changer_categorie(CheckBox cb, Label lbl) {
		structure.setTaille(0);
		String blc = "";
		String label = lbl.getText();
		if (cb.isSelected()) {
			blc = label.toString().toUpperCase() + "_" + this.rotations[this.rota];
			System.out.println(blc);
			structure.setSelected_bloc(blc);
		}

	}

	@FXML
	private void initialize() {

		ajout_categorie();
		ajout_couleur();
		searsh_bar.setFocusTraversable(false);

		mute_sound.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (mute_sound.getText().equals("Mute")) {
					player.setMute(true);
					mute_sound.setText("Unmute");
				} else {
					player.setMute(false);
					mute_sound.setText("Mute");
				}

			}
		});

	}
	private void loadStructure(String name) throws FileNotFoundException {

		File f = new File(name);

		if (f.exists() && !f.isDirectory()) {

			XMLDecoder decoder = null;

			FileInputStream fis = new FileInputStream(name);
			BufferedInputStream bos = new BufferedInputStream(fis);

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

						}

					}

				}

				shape.setMaterial(material);

				structure.getChildren().add(l.get(i));

			}
			structure.setSelected_bloc("CUBE");
			structure.prepareLight();
			structure.setNom_structure(((Lego) structure.getChildren().get(0)).getParent_name());

		} else {
			System.out.println("Cette sauvegarde n'existe pas");
		}

	}

}
