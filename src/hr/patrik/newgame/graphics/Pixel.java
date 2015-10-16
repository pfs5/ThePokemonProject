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
	
	public int id;
	public String type;
	public String name;
	
	public Pixel (int id) {
		this.id = id;
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
