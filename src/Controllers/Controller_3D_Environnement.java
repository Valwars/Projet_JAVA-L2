package Controllers;

import java.util.ArrayDeque;
import java.util.Deque;

import Models.Legos_collection;
import Models.PausableAnimationTimer;
import Models.Structure_3D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

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
	int matiere = 0;
	
	public String type;

	Color[] tab_couleur = { Color.ROYALBLUE, Color.GREEN, Color.KHAKI, Color.IVORY, Color.TURQUOISE,
			new Color(0.6, 0.6, 0.6, 0.6), Color.BROWN, Color.DARKORANGE, Color.DARKGRAY, Color.SADDLEBROWN, Color.RED, Color.SNOW};
	
	String[] tab_matiere = {"cobble.jpeg", "dirt.png" ,"lave.jpeg", "wood.jpeg", "feuille.png"};
	
	int rota = 0;

	boolean r = false;
	String[] rotations = { "DROITE", "GAUCHE", "AVANT", "ARRIERE" };

	String bloc = "RECTANGLE2_" + this.rotations[this.rota];
	
	Node[] pointLight ;
	
	public boolean isRotating = false;
	
	public PausableAnimationTimer timer = new PausableAnimationTimer() {
        @Override
        public void tick(long animationTime) {
        	
			structure.rotateProperty().set(structure.getRotate() + 0.2);

        }
    };

	public Controller_3D_Environnement(Structure_3D structure) {
		this.structure = structure;
	}

	@Override
	public void start(Stage primaryStage) {

		structure.createBase();
		this.pointLight = prepareLight();
		structure.getChildren().addAll(this.pointLight);

		camera = new PerspectiveCamera();
		
		Scene scene = new Scene(structure, 1200, 800, true);

		scene.setFill(Color.valueOf("#2C2D32"));

		scene.setCamera(camera);
		structure.translateXProperty().set(1200 / 2);
		structure.translateYProperty().set(800 / 2);
		structure.translateZProperty().set(0);		
		
		camera.translateZProperty().set(-1000);
		
		initMouseControl(structure, scene, primaryStage);

		primaryStage.setTitle("LEGOLAND");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
			structure.enregistrer();
			System.exit(0);
				
			}
			
		});
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			switch (event.getCode()) {
		
			case L:
				structure.resetStructure();
				structure.getChildren().addAll(prepareLight());
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
				if(isRotating == false) {
					structure.time_laps(this.pointLight);

				}
				break;
			case T:
				structure.move_sun(this.pointLight);
				break;
			case Y:
				structure.selected_bloc = "TAPIS";
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
				
			case K:
				String bloc5 = "RECTANGLE5_" + this.rotations[this.rota];
				structure.selected_bloc = bloc5;
				break;
			
			case H:
				
				structure.taille += 1;
				break;
			case G:
				
				structure.taille -= 1;
				break;
			
			case F:
				String bloc6 = "ANGLE_" + this.rotations[this.rota];
				structure.selected_bloc = bloc6;
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
				structure.selected_matiere = this.tab_matiere[this.matiere];
				break;
				
			case C:
				
				structure.selected_matiere = null;
				
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
			
			if(this.isRotating == false) {
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

		if(!timer.isActive || timer.isPaused) {
			 timer.start();
			 isRotating = true;
			 structure.translateYProperty().set(700);
			 structure.translateXProperty().set(1200 / 2);
			
			 structure.translateZProperty().set(0);	

		 }else {
			 timer.pause();
			 isRotating = false;
			 structure.setRotate(0);
			 structure.translateYProperty().set(800 / 2);
		 }

	}

	private Node[] prepareLight() {
		
		PointLight pointLight = new PointLight();
		
		pointLight.getTransforms().add(new Translate(0, -500, -1000));
		pointLight.setRotationAxis(Rotate.X_AXIS);
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.RED);

		Sphere sphere = new Sphere(10);

		sphere.getTransforms().setAll(pointLight.getTransforms());
	    sphere.rotateProperty().bind(pointLight.rotateProperty());
	    sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());

		return new Node[] { pointLight, sphere };

	}
	
	
	

}
