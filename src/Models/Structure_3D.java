package Models;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class Structure_3D extends Group implements Serializable {

	private static final long serialVersionUID =1L;
	
	Legos_collection legos_collections;
	
	public Stack<Node> deleted_blocs;
	
	public String selected_bloc = "BASE";
	
	public Color selected_color = Color.ROYALBLUE;
	
	public String selected_matiere = null;
	
	public int taille = 0;
	
	public int BLOC_SIZE;
	
	public int PLATEAU_TAILLE = 20;
		
	public int sun_orientation = 0;
	
	
	public PointLight pointLight;
	Sphere sphere;

	
	public void enregistrer() {
		try {
			FileOutputStream fos =  new FileOutputStream(new File("Sauvegarde.dat"));;
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(this.getChildren());
			oos.close();
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException("Impossible d'�crire les donn�es");
		}
	}
	
	public void charger() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(new File("Sauvegarde.dat"));
		ObjectInputStream ois = new ObjectInputStream(fis);
		this.getChildren().addAll((ArrayList<Group>) ois.readObject());
		ois.close();
		fis.close();
	}
	

	private void prepareLight() {
		
		PointLight pointLight = new PointLight();
		
		pointLight.getTransforms().add(new Translate(0, -500, -1500));
		pointLight.setRotationAxis(Rotate.X_AXIS);
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.RED);

		Sphere sphere = new Sphere(10);

		sphere.getTransforms().setAll(pointLight.getTransforms());
	    sphere.rotateProperty().bind(pointLight.rotateProperty());
	    sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());
	    
	    this.pointLight = pointLight;
	    this.sphere = sphere;

	}

	
	public PausableAnimationTimer timer = new PausableAnimationTimer() {
	        @Override
	        public void tick(long animationTime) {
	    		pointLight.setRotate(pointLight.getRotate() - 2);
	        }
	    };
	

	public Structure_3D(int bs) {
		this.BLOC_SIZE = bs;
		deleted_blocs = new Stack<Node>();
	}
	
	public void resetStructure() {
		this.selected_bloc = "BASE";
		this.getChildren().clear();
		createBase();
		
	
	}
	
	public void deleteLastBloc() {
		
		if(this.getChildren().size() > (PLATEAU_TAILLE*4 +1)) {
		
			try {
				Group g =  (Group) this.getChildren().get(this.getChildren().size() - 1);
				
			
				System.out.println(g.getChildren().size());
				if(g.getChildren().size() > 5) {
					
					Lego l = (Lego) g.getChildren().get(4);
					
					l.parent.enfant = null;
				
				}else{
					
					System.out.println(g.getChildren().get(4));
					Lego l = (Lego) g.getChildren().get(4);
					
					l.parent.enfant = null;
				}
				
			
				deleted_blocs.push(this.getChildren().get(this.getChildren().size() - 1));
				this.getChildren().remove(this.getChildren().size() - 1);
				
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		
	}
	
	
	
	public void move_sun() {
		this.pointLight.setRotate(pointLight.getRotate() - 25);		
		
	}
	
	public void time_laps() {
		 
		 if(!timer.isActive || timer.isPaused) {
			 timer.start();
		 }else {
			 timer.pause();
		 }
		 
		
	}
	
	public void recupDeletedBloc() {
		try {
			Group g =  (Group) deleted_blocs.pop();
			
			
			if(g.getChildren().size() > 5) {
				
				Lego l = (Lego) g.getChildren().get(4);
				
				l.parent.enfant = l;
			
			}else{
				
				System.out.println(g.getChildren().get(4));
				Lego l = (Lego) g.getChildren().get(4);
				
				l.parent.enfant = l;
			}
			
			System.out.println(g.getChildren());
			
			this.getChildren().add(g);

		}catch(Exception e){
			System.out.println(e);
		}
		
	}
	 
	public void setCollec(Legos_collection leg) {
		this.legos_collections = leg;
	}

	public void createBase() {

		int x = 0;
		int y = 0;

		Lego pateforme_de_construction;
		
		for(int k = 0 ; k < 4; k ++) {
			
			for (int i = 0; i < PLATEAU_TAILLE; i++) {

				for (int j = 0; j < PLATEAU_TAILLE; j++) {

					pateforme_de_construction = create_selectedBlock();
					pateforme_de_construction.setTranslateX(x);
					pateforme_de_construction.setTranslateZ(y);

					this.getChildren().add(pateforme_de_construction);
					
					if(k == 0 || k == 2) {
						x += (this.BLOC_SIZE +1);
					}else if(k == 1  || k == 3){
						x -= (this.BLOC_SIZE +1);
					}
					 
				}

				if(k == 0 || k == 3) {
					y -= (this.BLOC_SIZE +1);
				} else if(k == 1 || k == 2){
					y += (this.BLOC_SIZE +1);
				}
				
				x = 0;
			}
			
			x = 0;
			y = 0;
		}
		
		prepareLight();
		this.getChildren().add(this.pointLight);
		this.getChildren().add(this.sphere);
		this.selected_bloc = "CUBE";
	}

	public Lego create_selectedBlock() {
		return new Lego(this.legos_collections.legos.get(selected_bloc).width,this.legos_collections.legos.get(selected_bloc).height,this.legos_collections.legos.get(selected_bloc).depth,selected_bloc, null , this);
	}

}
