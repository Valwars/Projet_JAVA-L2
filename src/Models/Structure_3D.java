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
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class Structure_3D extends Group implements Serializable {

	private static final long serialVersionUID =1L;
	
	Legos_collection legos_collections;
	
	public Stack<Node> deleted_blocs;
	
	public String selected_bloc = "BASE";
	
	public Color selected_color = Color.ROYALBLUE;
	
	public String selected_matiere = null;
	
	public int taille = 1;

	
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
	


	
	public int sun_orientation = 0;
	
	
	public PointLight pointLight;
	
	public PausableAnimationTimer timer = new PausableAnimationTimer() {
	        @Override
	        public void tick(long animationTime) {
	    		pointLight.setRotate(pointLight.getRotate() - 2);
	        }
	    };
	

	    
	    

	public Structure_3D() {

		deleted_blocs = new Stack<Node>();
	}
	
	public void resetStructure() {
		System.out.println("RESET STRUCTURE");
		this.selected_bloc = "BASE";
		this.getChildren().clear();
		createBase();
		
	
	}
	
	
	public void deleteLastBloc() {
		
		if(this.getChildren().size() > 901) {
		
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
	
	
	
	public void move_sun(Node[] points) {
		
		this.pointLight = (PointLight) points[0];
		
		Sphere sphere = (Sphere) points[1];
		
		System.out.println(pointLight);
		System.out.println(sphere);

		
		pointLight.setRotate(pointLight.getRotate() - 25);
		System.out.println(pointLight.getRotate());
		
		
	}
	
	public void time_laps(Node[] points) {
		
		 this.pointLight = (PointLight) points[0];
		 
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
			
			for (int i = 0; i < 15; i++) {

				for (int j = 0; j < 15; j++) {

					pateforme_de_construction = create_selectedBlock();
					pateforme_de_construction.setTranslateX(x);
					pateforme_de_construction.setTranslateZ(y);

					this.getChildren().add(pateforme_de_construction);
					
					if(k == 0 || k == 2) {
						x += 51;
					}else if(k == 1  || k == 3){
						x -= 51;
					}
					 
				}

				if(k == 0 || k == 3) {
					y -= 51;
				} else if(k == 1 || k == 2){
					y += 51;
				}
				
				x = 0;
			}
			
			x = 0;
			y = 0;
		}
		
		this.selected_bloc = "CUBE";
		System.out.println(this.getChildren().size());
	}

	public Lego create_selectedBlock() {
		return new Lego(this.legos_collections.legos.get(selected_bloc).width,this.legos_collections.legos.get(selected_bloc).height,this.legos_collections.legos.get(selected_bloc).depth,selected_bloc, null , this);
	}

}
