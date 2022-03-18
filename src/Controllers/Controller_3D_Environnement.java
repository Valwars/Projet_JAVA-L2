package Controllers;

import java.util.ArrayList;
import java.util.HashMap;

import Models.Lego;
import Models.Legos_collection;
import Models.SmartGroup;
import Models.Structure_3D;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Controller_3D_Environnement extends Application {

	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;

	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	public Structure_3D structure;

	private Legos_collection leg;

	public Controller_3D_Environnement(Structure_3D structure) {
		this.structure = structure;
	}

	@Override
	public void start(Stage primaryStage) {

		structure.createBase();
		structure.getChildren().addAll(prepareLight());

		Camera camera = new PerspectiveCamera();
		
		Scene scene = new Scene(structure, 1200, 800, true);

		scene.setFill(Color.valueOf("#2C2D32"));

		scene.setCamera(camera);
		structure.translateXProperty().set(1200/2);
		structure.translateYProperty().set(1000 / 2);
		structure.translateZProperty().set(0);

		initMouseControl(structure, scene, primaryStage);

		primaryStage.setTitle("gerogr");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			switch (event.getCode()) {
			
			case T:
				structure.selected_bloc = "RECTANGLE2_DROITE";
				System.out.println("RECTANGLE");
				break;
			case I:
				structure.selected_bloc = "RECTANGLE4_AVANT";
				System.out.println("RECTANGLE");
				break;
			case Y:
				structure.selected_bloc = "RECTANGLE2_GAUCHE";
				System.out.println("RECTANGLE");
				break;
				
			case A:
				structure.selected_bloc = "RECTANGLE4_DROITE";
				System.out.println("RECTANGLE");
				break;
			case E:
				structure.selected_bloc = "CUBE";
				break;
			case P:
				structure.selected_bloc = "PILLIER";
				break;

			case C:
				structure.selected_bloc = "RECTANGLE4_GAUCHE";
				break;
				
			case B:
				structure.deleteLastBloc();
				break;
				
			case N:
				structure.recupDeletedBloc();
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
			angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
			angleY.set(anchorAngleY - (anchorX - event.getSceneX()));

		});

		stage.addEventHandler(ScrollEvent.SCROLL, event -> {
			double delta = event.getDeltaY();
			group.translateZProperty().set(group.getTranslateZ() + delta);
		});

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
