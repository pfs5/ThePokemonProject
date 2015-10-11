package hr.patrik.newgame.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

/*
 * Class implements all drawing
 * 
 * 
 */

import javax.imageio.ImageIO;

public class Screen {


	private int width;
	private int heigth;
	private int scale;
	public int[] pixels;		//Current window

	public int mapWidth;
	public int mapHeight;
	public int[] map;			//Whole map
	
	String path = "/resources/newMap.png";
	BufferedImage mapImage;
	
	public Screen (int width, int height, int scale) {
		this.width = width;
		this.heigth = height;
		this.scale = scale;
		pixels = new int [width*height];
		
		load();
	}
	
	//Load whole map
	public void load () {
		try {
			mapImage = ImageIO.read(getClass().getResource(path));
			mapWidth = mapImage.getWidth();
			mapHeight = mapImage.getHeight();
			map = new int[mapWidth*mapHeight];
			mapImage.getRGB(0,0,mapWidth,mapHeight,map,0,mapWidth);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Clear screen
	public void clear () {
		for (int y=0; y<heigth; y++)
			for (int x=0; x<width; x++) {
				pixels[x+width*y] = 0x000000;
			}
	}

	//Draw
	public void render (int xOffset, int yOffset) {
		for (int y=0; y<heigth; y++) {
			int mapY = (y+yOffset)/scale;

			for (int x=0; x<width; x++) {
				int mapX = (x+xOffset)/scale;
				int mapIndex = mapY*mapWidth + mapX;
				pixels [width*y+x] = map[mapIndex];
			}
		}
	}
}
