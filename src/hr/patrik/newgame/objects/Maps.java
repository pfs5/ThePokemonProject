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
	
	public String house002TopPath;
	public String house002BottomPath;
	public String house002DataPath;
	
	public Maps () {
		initMaps();
	}
	
	public void initMaps() {
		mapTopPath = "/resources_maps_map/testMapTop.png";
		mapBottomPath = "/resources_maps_map/testMapBottom.png";
		mapDataPath = "/resources_maps_map/testMapData.png";
		
		house001TopPath = "/resources_maps_house001/house001Top.png";
		house001BottomPath = "/resources_maps_house001/house001Bottom.png";
		house001DataPath = "/resources_maps_house001/house001Data.png";
		
		house001bTopPath = "/resources_maps_house001/house001bTop.png";
		house001bBottomPath = "/resources_maps_house001/house001bBottom.png";
		house001bDataPath = "/resources_maps_house001/house001bData.png";
		
		house002TopPath = "/resources_maps_house002/house002Top.png";
		house002BottomPath = "/resources_maps_house002/house002Bottom.png";
		house002DataPath = "/resources_maps_house002/house002Data.png";
	}
	
}
