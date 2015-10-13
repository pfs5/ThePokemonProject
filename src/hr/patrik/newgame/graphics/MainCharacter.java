package hr.patrik.newgame.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MainCharacter {
	
	public int x;
	public int y;
	private String direction;
	private int leg;
	
	public int imageWidth;
	public int imageHeight; 
	
	private String path = "/resources/testCharacterD.png";
	
	public BufferedImage image;
	
	public MainCharacter (int x, int y, int scale) {
		this.x = x;
		this.y = y;
		leg = 1;
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
		setStandImage();
	}
	
	public void setWalkImage() {
		try {
			String newPath = "/resources/testCharacter" + direction + "" + leg + ".png";
			image = ImageIO.read(getClass().getResource(newPath));
			if (leg == 1)
				leg = 2;
			else
				leg = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setStandImage() {
		try {
			path = "/resources/testCharacter" + direction + ".png";
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
