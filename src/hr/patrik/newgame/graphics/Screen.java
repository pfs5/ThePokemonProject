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
	private int height;
	private int scale;
	public int[] pixels;		//Current window

	private int tick;
	private int maxTick = 40;

	private String direction;
	private boolean moving;
	private int xOffset;
	private int yOffset;

	public int mapWidth;
	public int mapHeight;
	public int[] map;			//Whole map

	String path = "/resources/newMap.png";
	BufferedImage mapImage;

	public Screen (int width, int height, int scale, int xOffset, int yOffset) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		pixels = new int [width*height];

		moving = false;
		direction = "N";
		tick = 0;

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
		for (int y=0; y<height; y++)
			for (int x=0; x<width; x++) {
				pixels[x+width*y] = 0x000000;
			}
	}

	//Draw
	public void render () {
		for (int y=0; y<height; y++) {
			int mapY = (y+yOffset)/scale;

			for (int x=0; x<width; x++) {
				int mapX = (x+xOffset)/scale;
				int mapIndex = mapY*mapWidth + mapX;
				pixels [width*y+x] = map[mapIndex];
			}
		}
	}

	/*
	 * directions
	 * U up
	 * D down
	 * R right
	 * L left
	 * N neutral
	 */
	public void setMove(String direction) {
		if (moving == false) {
			this.direction = direction;
			moving = true;
			tick = 0;
		}
	}

	public void move () {

		if (moving == true) {
			if (direction.equals("U"))
				yOffset--;
			if (direction.equals("D"))
				yOffset++;
			if (direction.equals("L"))
				xOffset--;
			if (direction.equals("R"))
				xOffset++;
			tick++;
		}
		
		if (tick == maxTick)
			moving = false;

		//Map edges
		if (xOffset<0)
			xOffset = 0;
		if (yOffset<0)
			yOffset = 0;

		if (xOffset+width>mapWidth*scale)
			xOffset--;
		if (yOffset+height>mapHeight*scale)
			yOffset--;
	}
}
