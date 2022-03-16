package Views;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import Controllers.Controller_3D_Environnement;
import Models.Legos_collection;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("interface.fxml"));
			
			Legos_collection legos_collection = new Legos_collection();
			
			Controller_3D_Environnement environnement_Controller = new Controller_3D_Environnement(legos_collection);
			
			environnement_Controller.start(primaryStage);
			
			loader.setController(environnement_Controller);

			Parent root = loader.load();
			Scene scene = new Scene(root);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public static void main(String[] args) {
		launch(args);
	}
}
