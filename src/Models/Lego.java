package Models;


import java.util.HashMap;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Lego extends Box  {
	
	Lego parent;
	Lego enfant;
	
	public int width;
	public int height;
	public int depth;
	
	public String type;
	public Color color = Color.AQUAMARINE;
	
	public static SmartGroup group;
	
	
	private static Legos_collection Legos_collection;


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
				Lego new_lego = new Lego( Legos_collection.new_selectedBlock().width,  Legos_collection.new_selectedBlock().height,  Legos_collection.new_selectedBlock().depth,Legos_collection.selected_bloc, this);
				
				new_lego.setTranslateX( event.getX());
				new_lego.setTranslateY( -1 * (Legos_collection.new_selectedBlock().height / 2));
				new_lego.setTranslateZ( event.getZ());
				
				System.out.println(new_lego.parent);
				
				
				group.getChildren().add(new_lego);
			}else {
				Lego new_lego = new Lego(Legos_collection.new_selectedBlock().width, Legos_collection.new_selectedBlock().height, Legos_collection.new_selectedBlock().depth,Legos_collection.selected_bloc, this);
				
				new_lego.setTranslateX(  this.getTranslateX());
				new_lego.setTranslateY( -1 * (this.getTotalHeight()));
				new_lego.setTranslateZ( this.getTranslateZ());
				
				System.out.println(new_lego.parent);
				
				
				group.getChildren().add(new_lego);
			}
			
			
		});
		
	}
	
	public void setLegos(Legos_collection l ) {
		Legos_collection = l ;
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