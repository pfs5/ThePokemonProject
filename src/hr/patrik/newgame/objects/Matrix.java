package hr.patrik.newgame.objects;

import hr.patrik.newgame.graphics.ColorData;
import hr.patrik.newgame.graphics.Pixel;
import hr.patrik.newgame.objects.Maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Matrix {

	private Maps maps;

	//Images
	private BufferedImage TopImage;
	private BufferedImage BottomImage;
	private BufferedImage DataImage;

	//Arrays
	public int[] dataArray;

	//Map
	public int mapWidth;
	public int mapHeight;
	public int[] mapBottom;
	public int[] mapTop;
	public Pixel[] mapData;

	//house001
	public int house001Width;
	public int house001Height;
	public int[] house001Bottom;
	public int[] house001Top;
	public Pixel[] house001Data;

	//house001b
	public int house001bWidth;
	public int house001bHeight;
	public int[] house001bBottom;
	public int[] house001bTop;
	public Pixel[] house001bData;

	public Matrix () {
		maps = new Maps();
		initMatrix();
	}

	private void initMatrix() {
		initMap();
		initHouse001();
		initHouse001b();
	}

	private void initMap() {
		try {
			//Load map layers
			BottomImage = ImageIO.read(getClass().getResource(maps.mapBottomPath)); 
			TopImage = ImageIO.read(getClass().getResource(maps.mapTopPath));

			mapWidth = BottomImage.getWidth();
			mapHeight = BottomImage.getHeight();

			int w = mapWidth;
			int h = mapHeight;

			//Load map data
			DataImage = ImageIO.read(getClass().getResource(maps.mapDataPath));
			dataArray = new int[w*h];
			DataImage.getRGB(0,0,w,h,dataArray,0,w);
			mapData = new Pixel[w*h];
			for (int i=0; i<dataArray.length; i++) {
				int id = dataArray[i];
				mapData[i] = new Pixel(id);
				mapData[i] = checkSpecial(mapData[i]);
			}

			//Load map layers arrays
			mapTop= new int[w*h];
			TopImage.getRGB(0,0,w,h,mapTop,0,w);

			mapBottom= new int[w*h];
			BottomImage.getRGB(0,0,w,h,mapBottom,0,w);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Pixel checkSpecial(Pixel newPixel) {

		int id = newPixel.id;

		switch (id) {
		case ColorData.house001: newPixel.setType("Door"); newPixel.setName("house001");
		break;
		case ColorData.house001b: newPixel.setType("Door"); newPixel.setName("house001b");
		break;
		case ColorData.map: newPixel.setType("Door"); newPixel.setName("map");
		break;
		}

		return newPixel;
	}

	private void initHouse001 () {
		try {
			//Load map layers
			BottomImage = ImageIO.read(getClass().getResource(maps.house001BottomPath)); 
			TopImage = ImageIO.read(getClass().getResource(maps.house001TopPath));

			house001Width = BottomImage.getWidth();
			house001Height = BottomImage.getHeight();

			int w = house001Width;
			int h = house001Height;

			//Load map dataSystem.out.println(house001Data[i].id);
			DataImage = ImageIO.read(getClass().getResource(maps.house001DataPath));
			dataArray = new int[w*h];
			DataImage.getRGB(0,0,w,h,dataArray,0,w);
			house001Data = new Pixel[w*h];
			for (int i=0; i<dataArray.length; i++) {
				int id = dataArray[i];
				house001Data[i] = new Pixel(id);
				house001Data[i] = checkSpecial(house001Data[i]);
			}

			//Load map layers arrays
			house001Top= new int[w*h];
			TopImage.getRGB(0,0,w,h,house001Top,0,w);

			house001Bottom= new int[w*h];
			BottomImage.getRGB(0,0,w,h,house001Bottom,0,w);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initHouse001b () {
		try {
			//Load map layers
			BottomImage = ImageIO.read(getClass().getResource(maps.house001bBottomPath)); 
			TopImage = ImageIO.read(getClass().getResource(maps.house001bTopPath));

			house001bWidth = BottomImage.getWidth();
			house001bHeight = BottomImage.getHeight();

			int w = house001bWidth;
			int h = house001bHeight;

			//Load map data
			DataImage = ImageIO.read(getClass().getResource(maps.house001bDataPath));
			dataArray = new int[w*h];
			DataImage.getRGB(0,0,w,h,dataArray,0,w);
			house001bData = new Pixel[w*h];
			for (int i=0; i<dataArray.length; i++) {
				int id = dataArray[i];
				house001bData[i] = new Pixel(id);
				house001bData[i] = checkSpecial(house001bData[i]);
			}

			//Load map layers arrays
			house001bTop= new int[w*h];
			TopImage.getRGB(0,0,w,h,house001bTop,0,w);

			house001bBottom= new int[w*h];
			BottomImage.getRGB(0,0,w,h,house001bBottom,0,w);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
