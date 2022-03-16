package Controllers;

	
import java.util.ArrayList;
import java.util.HashMap;

import Models.Lego;
import Models.Legos_collection;
import Models.SmartGroup;
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
	

	
	public  SmartGroup group;
	
	private Legos_collection leg;
	
	public Controller_3D_Environnement(Legos_collection leg) {
		this.leg = leg;
	}
	

	@Override
	public void start(Stage primaryStage) {
		
	    group = new SmartGroup();
	    
	    
	    Lego.group = group;
	   
		Lego pateforme_de_construction =  leg.new_selectedBlock();
		
		pateforme_de_construction.setLegos(leg);
			
		
		
		leg.selected_bloc = "CUBE";

		group.getChildren().add(pateforme_de_construction);
		
		
		group.getChildren().addAll(prepareLight());

		Camera camera = new PerspectiveCamera();
		Scene scene = new Scene(group, 1200, 800, true);
		scene.setFill(Color.SILVER);
		
		scene.setCamera(camera);
		group.translateXProperty().set(1200/2);
		group.translateYProperty().set(800/2);
		group.translateZProperty().set(0);
		
		initMouseControl(group, scene, primaryStage);

		primaryStage.setTitle("gerogr");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
			switch(event.getCode()) {
			
			case W:
				leg.selected_bloc = "RECTANGLE";
				System.out.println("RECTANGLE");
				break;
			case E:
				leg.selected_bloc = "CUBE";
				break;
			case P:
				leg.selected_bloc = "PILLIER";
				break;
			}
			
			
		});	
}

	
	
	private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
		Rotate xRotate;
		Rotate yRotate;
		
		group.getTransforms().addAll(
				xRotate = new Rotate(0,Rotate.X_AXIS),
				yRotate = new Rotate(0,Rotate.Y_AXIS)

		);
		
		xRotate.angleProperty().bind(angleX);
		yRotate.angleProperty().bind(angleY);
		
		scene.setOnMousePressed(event -> {
			anchorX = event.getSceneX();
			anchorY = event.getSceneY();
			anchorAngleX = angleX.get();
			anchorAngleY = angleY.get();

		});
		
		scene.setOnMouseDragged(event ->{
			angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
			angleY.set(anchorAngleY - (anchorX - event.getSceneX()));

		});
		
		
		
		stage.addEventHandler(ScrollEvent.SCROLL, event ->{
			double delta = event.getDeltaY();
			group.translateZProperty().set(group.getTranslateZ() + delta);
		});
		

	}
	
	private Node[] prepareLight(){
		PointLight pointLight = new PointLight();
		
		pointLight.getTransforms().add(new Translate(50,-150, -150 ));
		
		
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.YELLOW);
		
		Sphere sphere = new Sphere(2);

		sphere.getTransforms().setAll(pointLight.getTransforms());
		sphere.setMaterial(material);
		
		return new Node[]{pointLight, sphere};
		
	}


	

}
