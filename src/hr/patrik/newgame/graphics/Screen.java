package hr.patrik.newgame.graphics;

import hr.patrik.newgame.objects.Attack;
import hr.patrik.newgame.objects.Item;
import hr.patrik.newgame.objects.Lists;
import hr.patrik.newgame.objects.Matrix;
import hr.patrik.newgame.objects.Pokemon;

import java.util.ArrayList;

/*
 * Class implements all drawing
 * 
 * 
 */

public class Screen {


	//Main window variables
	private int width;
	private int height;
	private int scale;
	public int[] pixels;		//Current window

	//Base block size
	public int BASE = 30;

	//Ticks
	private int tick;
	private int maxTick = BASE;

	//State variables
	private String direction;
	private boolean moving;
	private boolean movable;
	private boolean turning;
	private int xOffset;
	private int yOffset;
	private int mapOffsetX;
	private int mapOffsetY;

	//Map variables
	private String mapName;
	private int mapWidth;
	private int mapHeight;

	//Main character
	private MainCharacter mainCharacter;
	private int mainCharacterX;
	private int mainCharacterY;
	private int mainCharacterWidth;
	private int mainCharacterHeight;
	private int[] mainCharacterImage;

	//Map matrix'
	private Matrix matrix;



	//Lists
	Lists lists;
	ArrayList<Pokemon> pokemonList;
	ArrayList<Attack> attackList;
	ArrayList<Item> itemList;

	public Screen (int width, int height, int scale, int xOffset, int yOffset) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		BASE*=scale;
		maxTick = BASE;

		pixels = new int [width*height];
		lists = new Lists();
		matrix = new Matrix();

		mapName = "map";
		movable = true;
		moving = false;
		turning = false;
		direction = "N";
		tick = 0;

		load();
	}

	public void load () {

		getLists();

		mapWidth = matrix.mapWidth;
		mapHeight = matrix.mapHeight;

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

	public void getLists() {
		pokemonList = lists.getPokemonList();
		attackList = lists.getAttackList();
		itemList = lists.getItemList();
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
				switch (mapName) {
				case "map":
					pixels [width*y+x] = matrix.mapBottom[mapIndex];
					break;

				case "house001":
					pixels [width*y+x] = matrix.house001Bottom[mapIndex];
					break;

				case "house001b":
					pixels [width*y+x] = matrix.house001bBottom[mapIndex];
					break;
				}
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
				switch (mapName) {
				case "map":
					if (matrix.mapTop[mapIndex] != ColorData.transparent())
						pixels [width*y+x] = matrix.mapTop[mapIndex];
					break;

				case "house001":
					if (matrix.house001Top[mapIndex] != ColorData.transparent())
						pixels [width*y+x] = matrix.house001Top[mapIndex]; 
					break;

				case "house001b":
					if (matrix.house001bTop[mapIndex] != ColorData.transparent())
						pixels [width*y+x] = matrix.house001bTop[mapIndex]; 
					break;
				}
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
		printStuff();
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

			checkMovement();



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



	public void checkMovement(){
		//Check movement
		boolean passable = true;

		//Left + bottom edge
		int locationX1 = (xOffset+mainCharacterX)/scale;
		int locationY1 = (yOffset+mainCharacterY)/scale+mainCharacter.imageHeight-1; //TODO
		int mapLocationIndex1 = locationY1*mapWidth+locationX1;
		//Right + top edge
		int locationX2 = (xOffset+mainCharacterX+BASE)/scale-1;
		int locationY2 = (yOffset+mainCharacterY)/scale+mainCharacter.imageHeight-BASE/scale;
		int mapLocationIndex2 = locationY2*mapWidth+locationX2;

		int firstCheckId;
		String firstCheckType;
		String firstCheckName;
		int secondCheckId;
		String secondCheckType;
		String secondCheckName;

		//Set pixels to be examined
		switch (mapName) {
		case ("map"):
			firstCheckId = matrix.mapData[mapLocationIndex1].id;
			firstCheckType = matrix.mapData[mapLocationIndex1].type;
			firstCheckName = matrix.mapData[mapLocationIndex1].name;
			
			secondCheckId = matrix.mapData[mapLocationIndex2].id;
			secondCheckType = matrix.mapData[mapLocationIndex2].type;
			secondCheckName = matrix.mapData[mapLocationIndex2].name;
			
			break;
		case ("house001"):
			firstCheckId = matrix.house001Data[mapLocationIndex1].id;
			firstCheckType = matrix.house001Data[mapLocationIndex1].type;
			firstCheckName = matrix.house001Data[mapLocationIndex1].name;
			
			secondCheckId = matrix.house001Data[mapLocationIndex2].id;
			secondCheckType = matrix.house001Data[mapLocationIndex2].type;
			secondCheckName = matrix.house001Data[mapLocationIndex2].name;
		break;
		case ("house001b"):
			firstCheckId = matrix.house001bData[mapLocationIndex1].id;
			firstCheckType = matrix.house001bData[mapLocationIndex1].type;
			firstCheckName = matrix.house001bData[mapLocationIndex1].name;
		
			secondCheckId = matrix.house001bData[mapLocationIndex2].id;
			secondCheckType = matrix.house001bData[mapLocationIndex2].type;
			secondCheckName = matrix.house001bData[mapLocationIndex2].name;
		break;
		
		default:
			firstCheckId = -1;
			firstCheckType = null;
			firstCheckName = null;
			
			secondCheckId = -1;
			secondCheckType = null;
			secondCheckName = null;
			break;
		
		}

		//Check pixels
		if (firstCheckId == ColorData.impassable())
			passable = false;
		else if (secondCheckId == ColorData.impassable())
			passable = false;

		else if (firstCheckType.equals("Door"))
			setMap(firstCheckName);
		else if (secondCheckType.equals("Door"))
			setMap(secondCheckName);

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

	public void setMap (String mapName) {
		String from = this.mapName;
		this.mapName = mapName;
		
		switch (mapName) {
		case ("map"):
			mapWidth = matrix.mapWidth;
			mapHeight = matrix.mapHeight;
			xOffset = mapOffsetX;
			yOffset = mapOffsetY;
		break;

		case ("house001"):
			mapWidth = matrix.house001Width;
			mapHeight = matrix.house001Height;
			
			if (from.equals("map")) {
				mapOffsetX = xOffset;
				mapOffsetY = yOffset;
				xOffset = 4*BASE;
				yOffset = 8*BASE;
			}
			else {
				xOffset = 9*BASE;
				yOffset = 2*BASE;
			}
		break;	
		
		case ("house001b"):
			mapWidth = matrix.house001Width;
			mapHeight = matrix.house001Height;
			xOffset = 12*BASE;
			yOffset = 2*BASE;
		}
		moving = false;
	}

	public void printStuff() {
		long total = Runtime.getRuntime().totalMemory()/(1024*1024);
		long free = Runtime.getRuntime().freeMemory()/(1024*1024);
		System.out.println("total: " + total + "    free: " + free);
	}
}
