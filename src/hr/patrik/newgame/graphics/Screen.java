package hr.patrik.newgame.graphics;

import hr.patrik.newgame.main.GameState;
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
	
	//Game state
	private GameState gameState;

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
		gameState = new GameState();

		mapName = "map";
		movable = true;
		moving = false;
		turning = false;
		direction = "N";
		tick = 0;
		
		load();
		gameState.setState(xOffset, yOffset, xOffset, yOffset, mapName, direction);
	}

	public void load () {

		getLists();

		mapWidth = matrix.mapWidth;
		mapHeight = matrix.mapHeight;

		System.out.println("Loading window.");
		System.out.println("");
		System.out.println("Window loaded.");
		System.out.println("Aspect ratio 9/16");
		System.out.println("Size: "+width+"x"+height+" px");
		System.out.println("Scale: "+scale+"x");
		
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
		
		gameState.setState(xOffset, yOffset, xOffset, yOffset, mapName, direction);
		
		//Init char
		mainCharacterImage = mainCharacter.image.getRGB(0, 0, mainCharacterWidth,
				mainCharacterHeight, mainCharacterImage, 0, mainCharacterWidth);
		
		for (int y=0; y<height; y++) {
			int mapY = (y+yOffset)/scale;

			for (int x=0; x<width; x++) {
				int mapX = (x+xOffset)/scale;
				int mapIndex = mapY*mapWidth + mapX;

				int colorBottom = 0;
				int colorTop = 0;
				boolean top = false;
				boolean bottom = false;

				switch (mapName) {

				case "map":
					if (matrix.mapMatrix[mapIndex].layer.equals("bottom")) {
						colorBottom = matrix.mapMatrix[mapIndex].color;
						bottom = true;
					}
					else {
						colorTop = matrix.mapMatrix[mapIndex].color;
						top = true;
					}
					break;

				case "house001":
					if (matrix.house001Matrix[mapIndex].layer.equals("bottom")) {
						colorBottom = matrix.house001Matrix[mapIndex].color;
						bottom = true;
					}
					else {
						colorTop = matrix.house001Matrix[mapIndex].color;
						top = true;
					}
					break;

				case "house001b":
					if (matrix.house001bMatrix[mapIndex].layer.equals("bottom")) {
						colorBottom = matrix.house001bMatrix[mapIndex].color;
						bottom = true;
					}
					else {
						colorTop = matrix.house001bMatrix[mapIndex].color;
						top = true;
					}
					break;
					
				case "house002":
					if (matrix.house002Matrix[mapIndex].layer.equals("bottom")) {
						colorBottom = matrix.house002Matrix[mapIndex].color;
						bottom = true;
					}
					else {
						colorTop = matrix.house002Matrix[mapIndex].color;
						top = true;
					}
					break;
				}

				//Draw top layer
				if (bottom)
					pixels [width*y+x] = colorBottom;

				//Draw Character
				int charX = (x-mainCharacterX)/scale;
				int charY = (y-mainCharacterY)/scale;
				
				if (x>=mainCharacterX && x<mainCharacterX+mainCharacterWidth*scale
						&& y>= mainCharacterY && y<mainCharacterY+mainCharacterHeight*scale)
					if (mainCharacterImage[charY*mainCharacterWidth+charX] != ColorData.transparent())
						pixels[width*y+x] = mainCharacterImage[charY*mainCharacterWidth+charX];

				//Draw bottom layer
				if (top)
					pixels[width*y+x] = colorTop;
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
			firstCheckId = matrix.mapMatrix[mapLocationIndex1].data;
			firstCheckType = matrix.mapMatrix[mapLocationIndex1].type;
			firstCheckName = matrix.mapMatrix[mapLocationIndex1].name;

			secondCheckId = matrix.mapMatrix[mapLocationIndex2].data;
			secondCheckType = matrix.mapMatrix[mapLocationIndex2].type;
			secondCheckName = matrix.mapMatrix[mapLocationIndex2].name;
		break;
		
		case ("house001"):
			firstCheckId = matrix.house001Matrix[mapLocationIndex1].data;
			firstCheckType = matrix.house001Matrix[mapLocationIndex1].type;
			firstCheckName = matrix.house001Matrix[mapLocationIndex1].name;

			secondCheckId = matrix.house001Matrix[mapLocationIndex2].data;
			secondCheckType = matrix.house001Matrix[mapLocationIndex2].type;
			secondCheckName = matrix.house001Matrix[mapLocationIndex2].name;
		break;
		
		case ("house001b"):
			firstCheckId = matrix.house001bMatrix[mapLocationIndex1].data;
			firstCheckType = matrix.house001bMatrix[mapLocationIndex1].type;
			firstCheckName = matrix.house001bMatrix[mapLocationIndex1].name;

			secondCheckId = matrix.house001bMatrix[mapLocationIndex2].data;
			secondCheckType = matrix.house001bMatrix[mapLocationIndex2].type;
			secondCheckName = matrix.house001bMatrix[mapLocationIndex2].name;
		break;
		
		case ("house002"):
			firstCheckId = matrix.house002Matrix[mapLocationIndex1].data;
			firstCheckType = matrix.house002Matrix[mapLocationIndex1].type;
			firstCheckName = matrix.house002Matrix[mapLocationIndex1].name;

			secondCheckId = matrix.house002Matrix[mapLocationIndex2].data;
			secondCheckType = matrix.house002Matrix[mapLocationIndex2].type;
			secondCheckName = matrix.house002Matrix[mapLocationIndex2].name;
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
		break;
		
		case ("house002"):
			mapWidth = matrix.house002Width;
			mapHeight = matrix.house002Height;

			mapOffsetX = xOffset;
			mapOffsetY = yOffset;
			xOffset = 4*BASE;
			yOffset = 8*BASE;
		break;	
		}
		
		moving = false;
	}

	public static void printMemory() {
		//Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
        
        int mb = 1024*1024;
         
        System.out.println("");
        System.out.println("");
        
        System.out.println("##### Heap utilization statistics [MB] #####");
         
        //Print used memory
        System.out.println("Used Memory: "
            + (runtime.totalMemory() - runtime.freeMemory()) / mb + " MB");
 
        //Print free memory
        System.out.println("Free Memory: "
            + runtime.freeMemory() / mb + " MB");
         
        //Print total available memory
        System.out.println("Total Memory: " + runtime.totalMemory() / mb + " MB");
 
        //Print Maximum available memory
        System.out.println("Max Memory: " + runtime.maxMemory() / mb + " MB");
        
        System.out.println("");
        System.out.println("");
    
	}

	public void printStuff() {
	}

}
