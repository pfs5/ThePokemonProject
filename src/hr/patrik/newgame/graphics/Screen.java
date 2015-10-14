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

	public int BASE = 30;

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
	public int[] mapBottom;
	public int[] mapTop;
	public int[] mapDataInt;
	public Pixel[] mapData;

	//Main character
	private MainCharacter mainCharacter;
	private int mainCharacterX;
	private int mainCharacterY;
	private int mainCharacterWidth;
	private int mainCharacterHeight;
	private int[] mainCharacterImage;

	private String mapTopPath = "/map_resources/testMapTop.png";
	private String mapBottomPath = "/map_resources/testMapBottom.png";
	private String dataPath = "/map_resources/testMapData.png";

	private BufferedImage mapTopImage;
	private BufferedImage mapBottomImage;
	private BufferedImage mapDataImage;

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

	public void load () {

		try {
			//Load map layers
			mapBottomImage = ImageIO.read(getClass().getResource(mapBottomPath)); 
			mapTopImage = ImageIO.read(getClass().getResource(mapTopPath));

			mapWidth = mapBottomImage.getWidth();
			mapHeight = mapBottomImage.getHeight();

			//Load map data
			mapDataImage = ImageIO.read(getClass().getResource(dataPath));
			mapDataInt = new int[mapWidth*mapHeight];
			mapDataImage.getRGB(0,0,mapWidth,mapHeight,mapDataInt,0,mapWidth);
			mapData = new Pixel[mapWidth*mapHeight];
			for (int i=0; i<mapDataInt.length; i++) {
				int id = mapDataInt[i];
				mapData[i] = new Pixel(id);
			}

			//Load map layers arrays
			mapTop= new int[mapWidth*mapHeight];
			mapTopImage.getRGB(0,0,mapWidth,mapHeight,mapTop,0,mapWidth);

			mapBottom= new int[mapWidth*mapHeight];
			mapBottomImage.getRGB(0,0,mapWidth,mapHeight,mapBottom,0,mapWidth);

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

		//Draw bottom map layer
		for (int y=0; y<height; y++) {
			int mapY = (y+yOffset)/scale;

			for (int x=0; x<width; x++) {
				int mapX = (x+xOffset)/scale;
				int mapIndex = mapY*mapWidth + mapX;
				pixels [width*y+x] = mapBottom[mapIndex];
			}
		}

		//Draw main character
		mainCharacterImage = mainCharacter.image.getRGB(0, 0, mainCharacterWidth,
				mainCharacterHeight, mainCharacterImage, 0, mainCharacterWidth);

		for (int y=0; y<mainCharacterHeight*scale; y++) {
			for (int x=0; x<mainCharacterWidth*scale; x++) {
				int realY = y/scale;
				int realX = x/scale;
				int mapY = (y+mainCharacterY);
				int mapX = (x+mainCharacterX);
				int mainCharIndex = realY*mainCharacterWidth+realX;
				if (mainCharacterImage[mainCharIndex] != ColorData.transparent())	//transparency
					pixels [width*mapY+mapX] = mainCharacterImage[mainCharIndex];
			}
		}

		//Draw top map layer
		for (int y=0; y<height; y++) {
			int mapY = (y+yOffset)/scale;

			for (int x=0; x<width; x++) {
				int mapX = (x+xOffset)/scale;
				int mapIndex = mapY*mapWidth + mapX;
				if (mapTop[mapIndex] != ColorData.transparent())
					pixels [width*y+x] = mapTop[mapIndex];
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
			
			//Move
			if (direction.equals("U"))
				yOffset--;
			if (direction.equals("D"))
				yOffset++;
			if (direction.equals("L"))
				xOffset--;
			if (direction.equals("R"))
				xOffset++;
			tick++;
			
			//Check map edges
			if (xOffset<0)
				xOffset = 0;
			if (yOffset<0)
				yOffset = 0;

			if (xOffset+width>mapWidth*scale)
				xOffset--;
			if (yOffset+height>mapHeight*scale)
				yOffset--;
			
			//Check movement
			boolean passable = true;
			int locationX;
			int locationY;
			int mapLocationIndex;
			
			//Left + bottom edge
			locationX = (xOffset+mainCharacterX)*scale;
			locationY = (yOffset+mainCharacterY+mainCharacter.imageHeight-1)*scale;
			mapLocationIndex = locationY*mapWidth+locationX;
			if (mapData[mapLocationIndex].id == ColorData.impassable())
				passable = false;
			
			//Right + top edge
			locationX = (xOffset+mainCharacterX+BASE-1)*scale;
			locationY = (yOffset+mainCharacterY+mainCharacter.imageHeight-BASE)*scale;
			mapLocationIndex = locationY*mapWidth+locationX;
			if (mapData[mapLocationIndex].id == ColorData.impassable())
				passable = false;
			
			if (passable == false) {
				//Undo move
				if (direction.equals("U"))
					yOffset++;
				if (direction.equals("D"))
					yOffset--;
				if (direction.equals("L"))
					xOffset++;
				if (direction.equals("R"))
					xOffset--;
			}
				
			
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

	}
}
