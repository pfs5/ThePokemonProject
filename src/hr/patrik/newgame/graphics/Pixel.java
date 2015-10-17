package hr.patrik.newgame.graphics;

/*
 * ####	id	####
 * 0 			 	passable
 * 1  			 	impassable
 * 100 - 999  	 	doorway
 * 1000 - 9999   	NPC
 * 
 * 
 */

public class Pixel {
	
	public int color;
	public String layer;

	public int data;
	public String type;
	public String name;
	
	public Pixel (int color, String layer, int data) {
		this.color = color;
		this.layer = layer;
		this.data = data;
		
		name = "Pixel";
		type = "Pixel";
	}
	
	public void setType (String type) {
		this.type = type;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	
}
