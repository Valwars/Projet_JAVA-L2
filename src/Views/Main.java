package Views;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import Controllers.Controller_3D_Environnement;
import Models.Legos_collection;
import Models.Structure_3D;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private FXMLLoader loader;
	private Controller_3D_Environnement environnement_Controller;
	private int BLOC_SIZE = 50;

	private ArrayList<Structure_3D> structures = new ArrayList<Structure_3D>();

	@Override
	public void start(Stage primaryStage) {

		try {

			Structure_3D structure = new Structure_3D(BLOC_SIZE);
			primaryStage.getIcons().add(new Image("icon.png"));

			Legos_collection legos_collection = new Legos_collection(structure, BLOC_SIZE);

			structure.setCollec(legos_collection);
			structures.add(structure);

			loader = new FXMLLoader(getClass().getResource("interface.fxml"));

			environnement_Controller = new Controller_3D_Environnement();
			environnement_Controller.setEvent(primaryStage);
			loader.setController(environnement_Controller);

			BorderPane root = (BorderPane) loader.load();

			SubScene subscene = new SubScene(structure, 800, 1000, true, SceneAntialiasing.BALANCED);
			environnement_Controller.start(primaryStage, structure, subscene, null);
			subscene.setFill(Color.valueOf("#2C2D32"));

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			TabPane tp = new TabPane();

			Tab tb1;
			if (structure.getNom_structure() != null) {
				tb1 = new Tab(structure.getNom_structure());

			} else {
				tb1 = new Tab("Structure1");

			}
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {
			        Platform.exit();
			    }
			});

			Tab new_tab = new Tab("+");
			new_tab.setClosable(false);

			ArrayList<String> t = new ArrayList<>();
			t.contains(root);

			new_tab.setOnSelectionChanged(new EventHandler<Event>() {

				public void handle(Event e) {

					if (new_tab.isSelected()) {

						File file = new File("sauvegardes");

						if (file.isDirectory()) {

							if (file.list().length == 0) {

								create_newTab(tp, legos_collection, primaryStage, null);

							} else {

								if (tp.getTabs().size() <= 1) {
									create_newTab(tp, legos_collection, primaryStage, null);

								} else {

									SingleSelectionModel<Tab> selectionModel2 = tp.getSelectionModel();

									selectionModel2.select(tp.getTabs().get(0)); // select by object

									String[] dos = new String[file.list().length];

									String[] verif = file.list();

									dos[0] = "Nouvelle structure";

									for (int i = 1; i < file.list().length; i++) {
										System.out.println(verif[i]);
										dos[i] = verif[i].substring(0, verif[i].length() - 4);
									}

									ChoiceDialog dialog = new ChoiceDialog("Nouvelle structure", dos);
									environnement_Controller.set_dark_dialogue(dialog);

									Stage stageAlert1 = (Stage) dialog.getDialogPane().getScene().getWindow();
									stageAlert1.setAlwaysOnTop(true);
									stageAlert1.toFront();
									dialog.getDialogPane().setPrefWidth(500);

									dialog.setTitle("Nouvelle/charger structure");
									dialog.setHeaderText("Choix en cours...");
									Optional<String> result = dialog.showAndWait();

									if (result.get() == "Nouvelle structure") {

										create_newTab(tp, legos_collection, primaryStage, null);

									} else {

										create_newTab(tp, legos_collection, primaryStage, result.get());

									}
								}

							}
						}

					}

				}
			});

			tb1.setContent(subscene);

			subscene.widthProperty().bind(tp.widthProperty());
			subscene.heightProperty().bind(tp.heightProperty());

			tp.getTabs().add(tb1);

			tp.getTabs().add(new_tab);

			root.setCenter(tp);

			environnement_Controller.setCssScene(scene);

			primaryStage.setTitle("Lego Builder");
			primaryStage.setScene(scene);
			primaryStage.show();

			tb1.setOnSelectionChanged(new EventHandler<Event>() {

				@Override
				public void handle(Event arg0) {
					environnement_Controller.structure = structures.get(0);
					environnement_Controller.setScene(subscene);
				}

			});

			tb1.setOnClosed(new EventHandler<Event>() {
				@Override
				public void handle(Event t) {

					SingleSelectionModel<Tab> selectionModel2 = tp.getSelectionModel();

					structures.remove(structure);

					selectionModel2.select(tp.getTabs().get(0));
					environnement_Controller.structure = structures.get(0);
					
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create_newTab(TabPane tp, Legos_collection legos_collection, Stage primaryStage, String fileName) {

		Tab tb;
		if (fileName != null) {
			tb = new Tab(fileName);
		} else {
			tb = new Tab("Structure" + tp.getTabs().size());

		}

		Structure_3D structure2 = new Structure_3D(BLOC_SIZE);

		structure2.setCollec(legos_collection);
		structures.add(structure2);


		tb.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event t) {
				SingleSelectionModel<Tab> selectionModel2 = tp.getSelectionModel();

				structures.remove(structure2);

				selectionModel2.select(tp.getTabs().get(0));
				environnement_Controller.structure = structures.get(0);
			}
		});

		SubScene subscene2 = new SubScene(structure2, 800, 600, true, SceneAntialiasing.BALANCED);

		subscene2.widthProperty().bind(tp.widthProperty());
		subscene2.heightProperty().bind(tp.heightProperty());


		tb.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				SingleSelectionModel<Tab> selectionModel = tp.getSelectionModel();

				environnement_Controller.structure = structures.get(selectionModel.getSelectedIndex());
				environnement_Controller.setScene(subscene2);

			}
			
		});
		
		try {

			environnement_Controller.start(primaryStage, structure2, subscene2, fileName);
			structure2.translateXProperty().set(subscene2.getWidth() / 1.5);
			structure2.translateYProperty().set(subscene2.getHeight() / 1.5);
			structure2.translateZProperty().set(0);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		subscene2.setFill(Color.valueOf("#2C2D32"));

		tb.setContent(subscene2);

		tp.getTabs().add(tp.getTabs().size() - 1, tb);

		SingleSelectionModel<Tab> selectionModel = tp.getSelectionModel();

		selectionModel.select(tb); // select by object

		environnement_Controller.structure = structure2;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
