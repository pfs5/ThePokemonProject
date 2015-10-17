package hr.patrik.newgame.objects;

public class Maps {

	//Map
	public String mapTopPath;
	public String mapBottomPath; 
	public String mapDataPath;
	
	//Houses
	public String house001TopPath;
	public String house001BottomPath;
	public String house001DataPath;
	
	public String house001bTopPath;
	public String house001bBottomPath;
	public String house001bDataPath;
	
	public Maps () {
		initMaps();
	}
	
	public void initMaps() {
		mapTopPath = "/resources_maps/testMapTop.png";
		mapBottomPath = "/resources_maps/testMapBottom.png";
		mapDataPath = "/resources_maps/testMapData.png";
		
		house001TopPath = "/resources_maps_houses/house001Top.png";
		house001BottomPath = "/resources_maps_houses/house001Bottom.png";
		house001DataPath = "/resources_maps_houses/house001Data.png";
		
		house001bTopPath = "/resources_maps_houses/house001bTop.png";
		house001bBottomPath = "/resources_maps_houses/house001bBottom.png";
		house001bDataPath = "/resources_maps_houses/house001bData.png";
	}
	
}
