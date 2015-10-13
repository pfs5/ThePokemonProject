package hr.patrik.newgame.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MainCharacter {
	
	public int x;
	public int y;
	private String direction;
	
	public int imageWidth;
	public int imageHeight; 
	
	private String path = "/resources/testCharacterD.png";
	
	public BufferedImage image;
	
	public MainCharacter (int x, int y, int scale) {
		this.x = x;
		this.y = y;
		//Load image
		try {
			image = ImageIO.read(getClass().getResource(path));
			imageWidth = image.getWidth();
			imageHeight = image.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.y-=(imageHeight*scale);
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
		setImage();
	}
	
	public void setImage() {
		try {
			path = "/resources/testCharacter" + direction + ".png";
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
