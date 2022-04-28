package Views;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Controllers.Controller_3D_Environnement;
import Models.Lego;
import Models.Legos_collection;
import Models.Structure_3D;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
			
			 
			Scene scene = new Scene(root);
			
			TabPane tp = new TabPane();
			
			
			Tab tb1;
			if(structure.getNom_structure() != null) {
				 tb1 = new Tab(structure.getNom_structure());

			}else {
	             tb1 = new Tab("Structure"+ tp.getTabs().size());

			}
			
			Tab new_tab =new Tab("+");
			new_tab.setClosable(false);
			new_tab.setOnSelectionChanged(new EventHandler<Event>() {

	            public void handle(Event e) {
	            	
	                if (new_tab.isSelected()) {
	                	
	                	Tab tb = new Tab("Structure"+ tp.getTabs().size());
	                	
	                	
	        			Controller_3D_Environnement environnement_Controller = new Controller_3D_Environnement();
	        			
	        			loader.setController(environnement_Controller);
	        			
	                	Structure_3D structure = new Structure_3D(BLOC_SIZE);
	                	
	        			structure.setCollec(legos_collection);

	        			SubScene subscene = new SubScene(structure, 800, 600, true, SceneAntialiasing.BALANCED);
	        			try {
							environnement_Controller.start(primaryStage,structure, subscene);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

	        			subscene.setFill(Color.valueOf("#2C2D32"));

	        			tb.setContent(subscene);

	                	
	                	tp.getTabs().add(tp.getTabs().size() - 1, tb);
	                	
	                	SingleSelectionModel<Tab> selectionModel = tp.getSelectionModel();
	                	
	                	selectionModel.select(tb1); //select by object
	                	
	                	selectionModel.clearSelection(); //clear your selection
	                	

	                }
	            }
	        });

            tb1.setContent(subscene);

            tp.getTabs().add(tb1);
            
            tp.getTabs().add(new_tab);
            

			root.setCenter(tp);

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
