package application;
	
import java.util.ArrayList;
import java.util.HashMap;

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


public class Main extends Application {
	
	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;
	
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);
	
	private HashMap<String, Lego> legos = new HashMap<String, Lego>();
	
	public String selected_bloc = "CUBE";
	
	public SmartGroup group;

	
	@Override
	public void start(Stage primaryStage) {
		
		final Group axisGroup = new Group();
		
		System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
 
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
 
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
 
        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);
        
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
 
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        
 
		legos.put("CUBE",new Lego(100,100,100,"CUBE",null));
		
		legos.put("RECTANGLE",new Lego(200,50, 50,"RECTANGLE",null));
		legos.put("PILLIER",new Lego(50,200, 50,"PILLIER",null));
		legos.put("BASE",new Lego(1000,1,1000,"BASE",null));
		
		Lego pateforme_de_construction = legos.get("BASE");
		
		
	    group = new SmartGroup();
	    

		group.getChildren().add(pateforme_de_construction);
		
		
		group.getChildren().addAll(prepareLight());

		Camera camera = new PerspectiveCamera();
		Scene scene = new Scene(group, 1200, 800, true);
		scene.setFill(Color.SILVER);
		
		scene.setCamera(camera);
		group.getChildren().addAll(axisGroup);
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
				selected_bloc = "RECTANGLE";
				System.out.println("RECTANGLE");
				break;
			case E:
				selected_bloc = "CUBE";
				break;
			case P:
				selected_bloc = "PILLIER";
				break;
			}
			
			
		});	
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
	
	

	class Lego extends Box  {
		
		Lego parent;
		Lego enfant;
		
		public int width;
		public int height;
		public int depth;
		
		public String type;
		public Color color = Color.AQUAMARINE;
		
		public Lego(int width, int height , int depth,String type, Lego parent) {
			
			this.width = width;
			this.height = height;
			this.depth = depth;
			this.parent =parent;
			this.type = type;
			PhongMaterial material = new PhongMaterial();
			material.setDiffuseColor(Color.ROYALBLUE);
			material.setSpecularColor(Color.valueOf("#424242"));
			this.setMaterial(material);
			this.setWidth(width);
			this.setHeight(height);
			this.setDepth(depth);
			
			
			this.setOnMouseClicked(event ->{
				
				
				if(this.type.equals("BASE")) {
					Lego new_lego = new Lego(legos.get(selected_bloc).width, legos.get(selected_bloc).height, legos.get(selected_bloc).depth,selected_bloc, this);
					
					new_lego.setTranslateX( event.getX());
					new_lego.setTranslateY( -1 * (legos.get(selected_bloc).height / 2));
					new_lego.setTranslateZ( event.getZ());
					
					System.out.println(new_lego.parent);
					
					
					group.getChildren().add(new_lego);
				}else {
					Lego new_lego = new Lego(legos.get(selected_bloc).width, legos.get(selected_bloc).height, legos.get(selected_bloc).depth,selected_bloc, this);
					
					new_lego.setTranslateX(  this.getTranslateX());
					new_lego.setTranslateY( -1 * (this.getTotalHeight()));
					new_lego.setTranslateZ( this.getTranslateZ());
					
					System.out.println(new_lego.parent);
					
					
					group.getChildren().add(new_lego);
				}
				
				
			});
			
		}
		
		public void addChild(Lego child) {
			this.enfant = child;
		}
		
		
		public int getTotalHeight() {
			
			int height = this.height;

			
			Lego curr = this;
			
			
				while(curr.parent != null) {
					
					if(curr.parent.type != "BASE") {
						height += curr.parent.height;
						System.out.println("HAUTEUR PAPA :"+curr.parent.height);

				
					}
					
					curr = curr.parent;
					
				}
			
			
			System.out.println("HAUTEUR TOTALE :"+ height);
			return height;
		}
		
		
	}
}
