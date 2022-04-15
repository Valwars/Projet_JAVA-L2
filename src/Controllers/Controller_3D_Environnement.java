package Controllers;

import Models.Legos_collection;
import Models.PausableAnimationTimer;
import Models.Structure_3D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Controller_3D_Environnement{
	
	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;

	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);
	
	@FXML private ScrollPane Coulscrollpane;
	@FXML private ScrollPane cat;
	
	String[] tab_couleur1 = { "-fx-Base: #4169E1", "-fx-Base: #006400", "-fx-Base: #F0E68C","-fx-Base: #FFFFF0", "-fx-Base: #40E0D0","-fx-Base: #8B4513","-fx-Base: #FF8C00", "-fx-Base: #A9A9A9","-fx-Base: #8b4513" ,"-fx-Base: #FF0000" , "-fx-Base: #FFFAFA"};
	Label[] tab_categorie = {new Label("Cube"),new Label("Angle"),new Label("Rectangle"),new Label("Tapis")};
	
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
	
	
	public boolean isRotating = false;
	
	public PausableAnimationTimer timer = new PausableAnimationTimer() {
        @Override
        public void tick(long animationTime) {
        	
			structure.rotateProperty().set(structure.getRotate() + 0.2);

        }
    };

	

	public void start(Stage primaryStage, Structure_3D st, SubScene subscene) {
		

		this.structure = st;
		

		structure.createBase();

		camera = new PerspectiveCamera();
	
		subscene.setCamera(camera);
		structure.translateXProperty().set(1200 / 2);
		structure.translateYProperty().set(800 / 2);
		structure.translateZProperty().set(0);		
		
		camera.translateZProperty().set(-1000);
		
		
        initMouseControl(st,subscene,primaryStage);
		/*
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
			structure.enregistrer();
			System.exit(0);
				
			}
			
		});
		
		*/
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			switch (event.getCode()) {
			
			case L:
				structure.resetStructure();
				resetCamera();
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
					structure.time_laps();

				}
				break;
			case T:
				structure.move_sun();
				break;
			case Y:
				structure.taille = 0;


				structure.selected_bloc = "TAPIS";
				break;
			case U:
				structure.taille = 0;


				structure.selected_bloc = "CUBE";
				break;
			case I:
				structure.taille = 0;


				String bloc = "RECTANGLE2_" + this.rotations[this.rota];
				System.out.println(rota);
				structure.selected_bloc = bloc;
				break;
			case O:
				structure.taille = 0;


				String bloc2 = "RECTANGLE3_" + this.rotations[this.rota];
				structure.selected_bloc = bloc2;
				break;
			case P:
			
				structure.taille = 0;

				String bloc3 = "RECTANGLE4_" + this.rotations[this.rota];
				structure.selected_bloc = bloc3;
				break;
				
			case K:
				
				structure.taille = 0;

				String bloc5 = "RECTANGLE5_" + this.rotations[this.rota];
				structure.selected_bloc = bloc5;
				break;
			
			case H:
				structure.taille += 1;
				break;
			case G:
				if(structure.taille >0) {
					structure.taille -= 1;

				}
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
	
	private void resetCamera() {
		
	
	}

	private void initMouseControl(Structure_3D group, SubScene scene, Stage stage) {
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

	public void ajout_couleur() {
        GridPane container = new GridPane();
        for(int i = 0; i<tab_couleur1.length-1;i++) {
            container.getColumnConstraints().add(new ColumnConstraints(30));
            Button  bt1 = new Button(); 
            bt1.setPrefSize(25,25);
            bt1.setStyle(tab_couleur1[i]);
            bt1.setId("bt"+String.valueOf(i)+"color");
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>(){
            	
            	@Override
            	public void handle(ActionEvent evt) {
            		changer_couleur(bt1);
            	}
            };
            bt1.setOnAction(event);
            container.add(bt1,i,0);





        }
        this.Coulscrollpane.setContent(container);

    }
	public void changer_couleur(Button bt) {
		structure.selected_matiere = null;
		int clr=0;
		for (int i =0;i<this.tab_couleur1.length;i++) {
			if (this.tab_couleur1[i]==bt.getStyle()) {
				clr = i;
			}
		}
		structure.selected_color=this.tab_couleur[clr];
	}

    public void ajout_categorie(){
        GridPane container=new GridPane();
        Label lbl1= new Label("Categorie :");
        container.add(lbl1,0,0);
        for (int i = 0;i<tab_categorie.length;i++){
            container.getColumnConstraints().add(new ColumnConstraints(150));
            container.getRowConstraints().add(new RowConstraints(20));
            Label lbl= tab_categorie[i];
            CheckBox cbx= new CheckBox();
            lbl.setTextFill(Color.GREY);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>(){
            	
            	@Override
            	public void handle(ActionEvent evt) {
            		changer_categorie(cbx,lbl);
            	}
            };
            cbx.setOnAction(event);
            container.add(lbl,0,i+1);
            container.add(cbx,1,i+1);

        }
        this.cat.setContent(container);

    }
	public void changer_categorie(CheckBox cb, Label lbl) {
		structure.taille = 0;
		String blc="";
		String label = lbl.getText();
		if (cb.isSelected()) {
			blc = label.toString().toUpperCase()+"_"+ this.rotations[this.rota];
			System.out.println(blc);
			structure.selected_bloc=blc;
		}
		
	}
    
	@FXML
	private void initialize() {
		
		ajout_categorie();
		ajout_couleur();
		
		
	}
	

}
