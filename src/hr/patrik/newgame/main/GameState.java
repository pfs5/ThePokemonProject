package hr.patrik.newgame.main;

public class GameState {

	public int xOffset;
	public int yOffset;
	
	public int mapOffsetX;
	public int mapOffsetY;
	
	public String mapName;
	
	public String direction;
	
	public void setState(int xOffset, int yOffset, int mapOffsetX, 
			int mapOffsetY, String mapName, String direction) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		this.mapOffsetX = mapOffsetX;
		this.mapOffsetY = mapOffsetY;
		
		this.mapName = mapName;
		this.direction = direction;
	}

}
