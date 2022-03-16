package application;
	
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;


public class Main extends Application {
	
	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;
	
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	@Override
	public void start(Stage primaryStage) {
		SmartGroup group = new SmartGroup();
		Box box = prepareBox(group);
		Box box2 = new Box(30,20,200);
		box2.setTranslateX(50);  
		box2.setTranslateY(-20); 
		
		Box box3 = new Box(30,20,200);
		box3.setTranslateX(-50);  
		box3.setTranslateY(-20); 

		group.getChildren().add(box);
		group.getChildren().add(box2);
		group.getChildren().add(box3);
		
		group.getChildren().addAll(prepareLight());

		Camera camera = new PerspectiveCamera();
		Scene scene = new Scene(group, 1200, 800,  true);
		scene.setFill(Color.SILVER);
		
		scene.setCamera(camera);

		group.translateXProperty().set(1200/2);
		group.translateYProperty().set(800/2);
		group.translateZProperty().set(-1000);
		
		initMouseControl(group, scene, primaryStage);

		primaryStage.setTitle("gerogr");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
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
	
	private Box prepareBox(SmartGroup group) {
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.ROYALBLUE);
		material.setSpecularColor(Color.valueOf("#424242"));
		Box box = new Box(100,20,50);
		box.setMaterial(material);
		box.setTranslateX(0);  
		box.setTranslateY(0);  
		
		box.setOnMouseClicked(event ->{
			
			System.out.println(event.getTarget().toString());
			System.out.println(event.getX());
			System.out.println(event.getY());
	
			Box box2 = new Box(50,50,50);
			box.setMaterial(material);
			
			box.setTranslateX(event.getX());  
			box.setTranslateY(event.getY());
			box.setTranslateZ(event.getZ());  
			group.getChildren().add(box2);

		});
			
		return box;
	}
	
	class SmartGroup extends Group{
		Rotate r;
		Transform t = new Rotate();
		
		void rotateByX(int ang) {
			r = new Rotate(ang, Rotate.X_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}
		
		void rotateByY(int ang) {
			r = new Rotate(ang, Rotate.Y_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}
		
	}
}
