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

	private int BASE = 30;

	private int tick;
	private int maxTick = BASE;

	private String direction;
	private boolean moving;
	private boolean movable;
	private boolean turning;
	private int xOffset;
	private int yOffset;

	public int mapWidth;
	public int mapHeight;
	public int[] map;			//Whole map

	//Main character
	private MainCharacter mainCharacter;
	private int mainCharacterX;
	private int mainCharacterY;
	private int mainCharacterWidth;
	private int mainCharacterHeight;
	private int[] mainCharacterImage;

	private String path = "/resources/testMap.png";
	private BufferedImage mapImage;

	public Screen (int width, int height, int scale, int xOffset, int yOffset) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		BASE*=scale;
		maxTick = BASE;

		pixels = new int [width*height];

		movable = true;
		moving = false;
		turning = false;
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

		//Load main character
		mainCharacterX = ((width/2)/BASE)*BASE;
		mainCharacterY = ((height/2)/BASE)*BASE;
		mainCharacter = new MainCharacter(mainCharacterX, mainCharacterY, scale);
		mainCharacterWidth = mainCharacter.imageWidth;
		mainCharacterHeight = mainCharacter.imageHeight;
		mainCharacterX = mainCharacter.x;
		mainCharacterY = mainCharacter.y;
		mainCharacterImage = new int [mainCharacterWidth*mainCharacterHeight];
		mainCharacterImage = mainCharacter.image.getRGB(0, 0, mainCharacterWidth,
				mainCharacterHeight, mainCharacterImage, 0, mainCharacterWidth);
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

		//Draw map
		for (int y=0; y<height; y++) {
			int mapY = (y+yOffset)/scale;

			for (int x=0; x<width; x++) {
				int mapX = (x+xOffset)/scale;
				int mapIndex = mapY*mapWidth + mapX;
				pixels [width*y+x] = map[mapIndex];
			}
		}

		//Add main character to map
		mainCharacterImage = mainCharacter.image.getRGB(0, 0, mainCharacterWidth,
				mainCharacterHeight, mainCharacterImage, 0, mainCharacterWidth);
		
		for (int y=0; y<mainCharacterHeight*scale; y++) {
			for (int x=0; x<mainCharacterWidth*scale; x++) {
				int realY = y/scale;
				int realX = x/scale;
				int mapY = (y+mainCharacterY);
				int mapX = (x+mainCharacterX);
				int mainCharIndex = realY*mainCharacterWidth+realX;
				if (mainCharacterImage[mainCharIndex] != -2972731)	//transparency
					pixels [width*mapY+mapX] = mainCharacterImage[mainCharIndex];
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
			String oldDirection = this.direction;
			this.direction = direction;
			mainCharacter.setDirection(direction);
			mainCharacterImage = mainCharacter.image.getRGB(0, 0, mainCharacterWidth,
					mainCharacterHeight, mainCharacterImage, 0, mainCharacterWidth);
			if (oldDirection != this.direction) {
				turning = true;
				tick = 0;
			}
			else if (turning == false) {
				moving = true;
				tick = 0;
			}
		}
	}

	public void move () {
		
		if (moving == true && movable == true) {
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
		if (turning == true)
			tick++;
		
		//Mid tick image change
		if (tick == maxTick/2)
			mainCharacter.setWalkImage();
		
		if (tick == maxTick) {
			turning = false;
			moving = false;
			mainCharacter.setStandImage();
		}

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
