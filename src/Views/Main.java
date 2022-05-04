package Views;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Controllers.Controller_3D_Environnement;
import Models.Legos_collection;
import Models.Structure_3D;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	

	private FXMLLoader loader;
	private Controller_3D_Environnement environnement_Controller;
	private int BLOC_SIZE = 50;
	
	
	
	private ArrayList<Structure_3D> structures = new ArrayList<Structure_3D>();
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
		
			
			Structure_3D structure = new Structure_3D(BLOC_SIZE);
			primaryStage.getIcons().add(new Image("icon.png"));
			
			Legos_collection legos_collection = new Legos_collection(structure,BLOC_SIZE);
			
			structure.setCollec(legos_collection);
			structures.add(structure);

			loader = new FXMLLoader(getClass().getResource("interface.fxml"));

			environnement_Controller = new Controller_3D_Environnement();
			environnement_Controller.setEvent(primaryStage);
			loader.setController(environnement_Controller);
			
			
			BorderPane root = (BorderPane) loader.load();
			
			
			SubScene subscene = new SubScene(structure, 800,1000 , true, SceneAntialiasing.BALANCED);
			environnement_Controller.start(primaryStage,structure, subscene);
			subscene.setFill(Color.valueOf("#2C2D32"));
			
			Scene scene = new Scene(root);
			
			TabPane tp = new TabPane();
			
			
			Tab tb1;
			if(structure.getNom_structure() != null) {
				 tb1 = new Tab(structure.getNom_structure());

			}else {
	             tb1 = new Tab("Structure1");

			}
			
			Tab new_tab =new Tab("+");
			new_tab.setClosable(false);
			

			new_tab.setOnSelectionChanged(new EventHandler<Event>(){

	            public void handle(Event e) {
	            	
	            	
	                if (new_tab.isSelected()) {
	                	File file = new File("sauvegardes");
				        if(file.isDirectory()){
				            if(file.list().length==0){
			                	Tab tb = new Tab("Structure"+ tp.getTabs().size());
            	        		
			                	
			                	Structure_3D structure2 = new Structure_3D(BLOC_SIZE);
			                	
			        			structure2.setCollec(legos_collection);
			        			structures.add(structure2);
			        			

			                	tb.setOnSelectionChanged(new EventHandler<Event>() {

			        				@Override
			        				public void handle(Event arg0) {
			        					SingleSelectionModel<Tab> selectionModel = tp.getSelectionModel();
			        			    	
			        			    	environnement_Controller.structure = structures.get(selectionModel.getSelectedIndex());
			        				}
			        				
			        		
			        		
			        			});

			        			SubScene subscene2 = new SubScene(structure2, 800, 600, true, SceneAntialiasing.BALANCED);
			        			
			        			subscene2.widthProperty().bind(tp.widthProperty());
			        			subscene2.heightProperty().bind(tp.heightProperty());
			        			
			        			try {

			        				environnement_Controller.start(primaryStage,structure2, subscene2);
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

			        			subscene2.setFill(Color.valueOf("#2C2D32"));
			        		

			        			tb.setContent(subscene2);

			                	
			                	tp.getTabs().add(tp.getTabs().size() - 1, tb);
			                	
			                	SingleSelectionModel<Tab> selectionModel = tp.getSelectionModel();
			                	
			                	selectionModel.select(tb); //select by object
			                	
				            	
				            }
				            else {
				            	
				            	SingleSelectionModel<Tab> selectionModel2 = tp.getSelectionModel();
			                	
			                	selectionModel2.select(tp.getTabs().get(0)); //select by object
			            	
				            
				            	ChoiceDialog dialog = new ChoiceDialog();
				            	ButtonType charger=new ButtonType("Charger un projet",ButtonData.APPLY);
				            	ButtonType nouveau=new ButtonType("Nouveau projet",ButtonData.OK_DONE);
				            	dialog.getDialogPane().getButtonTypes().add(nouveau);
				            	dialog.getDialogPane().getButtonTypes().add(charger);
				            	
				            	dialog.showAndWait();
				            	ButtonType result = (ButtonType) dialog.resultProperty().getValue();
				            	if (result.getText()==nouveau.getText()) {
				            		Tab tb = new Tab("Structure"+ tp.getTabs().size());
	            	        		
				                	
				                	Structure_3D structure2 = new Structure_3D(BLOC_SIZE);
				                	
				        			structure2.setCollec(legos_collection);
				        			structures.add(structure2);
				        			

				                	tb.setOnSelectionChanged(new EventHandler<Event>() {

				        				@Override
				        				public void handle(Event arg0) {
				        					SingleSelectionModel<Tab> selectionModel = tp.getSelectionModel();
				        			    	System.out.println(structures.toString());
				        			    	System.out.println(selectionModel.getSelectedIndex());
				        			    	environnement_Controller.structure = structures.get(selectionModel.getSelectedIndex());
				        			    	System.out.println(environnement_Controller.structure);
				        				}
				        				
				        		
				        		
				        			});

				        			SubScene subscene2 = new SubScene(structure2, 800, 600, true, SceneAntialiasing.BALANCED);
				        			
				        			subscene2.widthProperty().bind(tp.widthProperty());
				        			subscene2.heightProperty().bind(tp.heightProperty());
				        			
				        			try {

				        				environnement_Controller.start(primaryStage,structure2, subscene2);
									} catch (FileNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

				        			subscene2.setFill(Color.valueOf("#2C2D32"));
				        		

				        			tb.setContent(subscene2);

				                	
				                	tp.getTabs().add(tp.getTabs().size() - 1, tb);
				                	
				                	SingleSelectionModel<Tab> selectionModel = tp.getSelectionModel();
				                	
				                	selectionModel.select(tb); //select by object
				            	
				            	}
				            	
				            	
				            	if(result.getText()==charger.getText()) {
				            		
				            		ScrollPane sauvegardes = new ScrollPane();
				            		GridPane container = new GridPane();
				            		sauvegardes.setPrefSize(250,250);
				            		File[] liste = file.listFiles();
				            		for (int k = 1;k<liste.length;k++) {
				            			container.getColumnConstraints().add(new ColumnConstraints(100));
					        			container.getRowConstraints().add(new RowConstraints(40));
				            			File f = new File("sauvegardes/"+liste[k].getName());
				            			Label lbl = new Label(liste[k].getName().substring(0,liste[k].getName().length()-4));
				            			Button bt_charger = new Button("charger");
				            			String ch = new String(liste[k].getName());
				            			EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

				            				@Override
				            				public void handle(ActionEvent evt) {
				            					
				            					
				            				}
				            			};
				            			bt_charger.setOnAction(event);
				            			container.add(lbl,0,k-1);
				            			container.add(bt_charger, 1, k-1);
				            			
				            			
				            		}
				            		sauvegardes.setContent(container);
				            		Scene scene = new Scene(sauvegardes);
				            		primaryStage.setScene(scene);
				            		primaryStage.show();
				            		
				            		
				            	}
				            	
				            		
				            	}
				            	
				            }
				        }else{
				            System.out.println("Ce n'est pas un répertoire!");
				        } 
	                	

	                	
	            }
	            });
	       
			
        	

			
            tb1.setContent(subscene);

            subscene.widthProperty().bind(tp.widthProperty());
            subscene.heightProperty().bind(tp.heightProperty());

            tp.getTabs().add(tb1);
            
            tp.getTabs().add(new_tab);
            

			root.setCenter(tp);

			primaryStage.setTitle("Lego Builder");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			tb1.setOnSelectionChanged(new EventHandler<Event>() {

				@Override
				public void handle(Event arg0) {
					environnement_Controller.structure = structures.get(0);

				}
				
				
				
			});
				
		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	

	public static void main(String[] args) {
		launch(args);
	}
}
