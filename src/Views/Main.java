package Views;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import Controllers.Controller_3D_Environnement;
import Models.Lego;
import Models.Legos_collection;
import Models.Structure_3D;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

	
	private int BLOC_SIZE = 50;
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
		
			Structure_3D structure = new Structure_3D(BLOC_SIZE);
			
			
			Legos_collection legos_collection = new Legos_collection(structure,BLOC_SIZE);
			
			structure.setCollec(legos_collection);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("interface.fxml"));
			
			Controller_3D_Environnement environnement_Controller = new Controller_3D_Environnement();
			
			loader.setController(environnement_Controller);
			
			
			BorderPane root = (BorderPane) loader.load();
			
			
			SubScene subscene = new SubScene(structure, 800, 600, true, SceneAntialiasing.BALANCED);
			environnement_Controller.start(primaryStage,structure, subscene);
			subscene.setFill(Color.valueOf("#2C2D32"));
			root.setCenter(subscene);
			
			 
			Scene scene = new Scene(root);
			

			primaryStage.setTitle("L'ego World !");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public static void main(String[] args) {
		launch(args);
	}
}
