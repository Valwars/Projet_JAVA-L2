package Controllers;

import java.util.ArrayDeque;
import java.util.Deque;

import Models.Legos_collection;
import Models.Structure_3D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Controller_3D_Environnement extends Application {

	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;

	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	private Camera camera;
	private Camera firstPersoncamera;

	public Structure_3D structure;

	private Legos_collection leg;

	int couleur = 0;
	
	public String type;

	Color[] tab_couleur = { Color.ROYALBLUE, Color.GREEN, Color.SANDYBROWN, Color.IVORY, Color.AQUA,
			new Color(0.6, 0.6, 0.6, 0.6) };

	int rota = 0;

	boolean r = false;
	String[] rotations = { "DROITE", "GAUCHE", "AVANT", "ARRIERE" };

	String bloc = "RECTANGLE2_" + this.rotations[this.rota];

	public Controller_3D_Environnement(Structure_3D structure) {
		this.structure = structure;
	}

	@Override
	public void start(Stage primaryStage) {

		structure.createBase();
		structure.getChildren().addAll(prepareLight());

		camera = new PerspectiveCamera();

		/*
		 * firstPersoncamera = new PerspectiveCamera(true);
		 * firstPersoncamera.getTransforms().addAll(new Translate(0,800, -100), new
		 * Rotate(90, Rotate.X_AXIS)); firstPersoncamera.setFarClip(10);
		 * firstPersoncamera.setNearClip(0);
		 * 
		 * Camera pour minecraft MODE
		 * 
		 */
		Scene scene = new Scene(structure, 1200, 800, true);

		scene.setFill(Color.valueOf("#2C2D32"));

		scene.setCamera(camera);
		structure.translateXProperty().set(1200 / 2);
		structure.translateYProperty().set(1000 / 2);
		structure.translateZProperty().set(0);

		initMouseControl(structure, scene, primaryStage);

		primaryStage.setTitle("LEGOLAND");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			switch (event.getCode()) {

			/*
			 * case L: structure.selected_color = Color.GREEN; break; case J:
			 * structure.selected_color = Color.SANDYBROWN; break; case K:
			 * structure.selected_color = Color.IVORY; break; case S:
			 * structure.selected_color = Color.AQUA; break; case T: structure.selected_bloc
			 * = "RECTANGLE2_DROITE"; break; case I: structure.selected_bloc =
			 * "RECTANGLE4_AVANT"; break; case O: structure.selected_bloc =
			 * "RECTANGLE4_ARRIERE"; break; case Y: structure.selected_bloc =
			 * "RECTANGLE2_GAUCHE"; break;
			 * 
			 * case A: structure.selected_bloc = "RECTANGLE4_DROITE"; break; case E:
			 * structure.selected_bloc = "CUBE"; break;
			 * 
			 * case M: structure.selected_bloc = "RECTANGLE4_GAUCHE"; break;
			 */

			case Z:
				System.out.println("AVANT");
				camera.setTranslateZ(camera.getTranslateZ() + 50);

				break;
			case S:
				System.out.println("arriere");
				camera.setTranslateZ(camera.getTranslateZ() - 50);

				break;
			case A:
				System.out.println("gauche");
				camera.setTranslateX(camera.getTranslateX() - 50);

				break;
			case D:
				System.out.println("droite");
				camera.setTranslateX(camera.getTranslateX() + 50);

				break;

			case R:

				if (r == false) {

					structure.setRotationAxis(Rotate.Y_AXIS);
					r = true;
					spinAnimation();
				} else {
					structure.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS), new Rotate(0, Rotate.Y_AXIS)

					);
					r = false;
					spinAnimation();
				}

				break;

			case U:
				structure.selected_bloc = "CUBE";
				break;
			case I:
				String bloc = "RECTANGLE2_" + this.rotations[this.rota];
				System.out.println(rota);
				structure.selected_bloc = bloc;
				break;
			case O:
				String bloc2 = "RECTANGLE3_" + this.rotations[this.rota];
				structure.selected_bloc = bloc2;
				break;
			case P:
				String bloc3 = "RECTANGLE4_" + this.rotations[this.rota];
				structure.selected_bloc = bloc3;
				break;

			case B:
				structure.deleteLastBloc();
				break;

			case N:
				structure.recupDeletedBloc();
				break;

			case C:

				if (this.couleur == this.tab_couleur.length - 1) {
					this.couleur = 0;
				} else {
					this.couleur += 1;

				}

				structure.selected_color = this.tab_couleur[this.couleur];
				break;

			case V:

				if (structure.selected_bloc != "CUBE") {
					if (this.rota == this.rotations.length - 1) {
						this.rota = 0;
					} else {
						this.rota += 1;

					}
					String[] blocs = structure.selected_bloc.split("_");

					structure.selected_bloc = blocs[0] + "_" + this.rotations[rota];
				}

				break;

			}

		});

	}

	private void initMouseControl(Structure_3D group, Scene scene, Stage stage) {
		Rotate xRotate;
		Rotate yRotate;

		group.getTransforms().addAll(xRotate = new Rotate(0, Rotate.X_AXIS), yRotate = new Rotate(0, Rotate.Y_AXIS)

		);

		xRotate.angleProperty().bind(angleX);
		yRotate.angleProperty().bind(angleY);

		scene.setOnMousePressed(event -> {

			anchorX = event.getSceneX();
			anchorY = event.getSceneY();
			anchorAngleX = angleX.get();
			anchorAngleY = angleY.get();

		});

		scene.setOnMouseDragged(event -> {
			if (event.isPrimaryButtonDown()) {
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

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long arg0) {
				if (r == true) {

					structure.rotateProperty().set(structure.getRotate() + 0.2);
				} else {
					structure.rotateProperty().set(structure.getRotate());

				}
			}

		};
		if (r == true) {
			timer.start();

		} else {
			timer.stop();
			;
		}

	}

	private Node[] prepareLight() {
		PointLight pointLight = new PointLight();

		pointLight.getTransforms().add(new Translate(0, -1000, -1000));

		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.YELLOW);

		Sphere sphere = new Sphere(10);

		sphere.getTransforms().setAll(pointLight.getTransforms());
		sphere.setMaterial(material);

		return new Node[] { pointLight, sphere };

	}

}
