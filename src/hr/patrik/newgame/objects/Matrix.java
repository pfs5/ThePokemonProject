package hr.patrik.newgame.objects;

import hr.patrik.newgame.graphics.ColorData;
import hr.patrik.newgame.graphics.Pixel;
import hr.patrik.newgame.graphics.Screen;
import hr.patrik.newgame.objects.Maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Matrix {

	private Maps maps;
	int totalSize = 0;
	int mb = 1024*1024;

	//Map
	public int mapWidth;
	public int mapHeight;
	public Pixel[] mapMatrix;

	//house001
	public int house001Width;
	public int house001Height;
	public Pixel[] house001Matrix;

	//house001b
	public int house001bWidth;
	public int house001bHeight;
	public Pixel[] house001bMatrix;
	
	//house002
	public int house002Width;
	public int house002Height;
	public Pixel[] house002Matrix;

	public Matrix () {
		maps = new Maps();
		initMatrix();
	}

	private void initMatrix() {
		System.out.println("Initializing game.");
		System.out.println("Analizing memory.");
		Screen.printMemory();
		System.out.println("Loading maps.");


		//Initialize maps
		initMap();
		initHouse001();
		initHouse001b();
		initHouse002();
		System.gc();

		System.out.println("##### Initializing maps #####");
		System.out.println("");
		System.out.println("Maps initialized.");
		Screen.printMemory();
	}

	private void initMap() {
		//Images
		BufferedImage TopImage;
		BufferedImage BottomImage;
		BufferedImage DataImage;

		//Arrays

		try {
			//Load map layers
			BottomImage = ImageIO.read(getClass().getResource(maps.mapBottomPath)); 
			TopImage = ImageIO.read(getClass().getResource(maps.mapTopPath));
			DataImage = ImageIO.read(getClass().getResource(maps.mapDataPath));

			mapWidth = BottomImage.getWidth();
			mapHeight = BottomImage.getHeight();

			int w = mapWidth;
			int h = mapHeight;

			int[] bottomLayer;
			bottomLayer = new int[w*h];
			BottomImage.getRGB(0,0,w,h,bottomLayer,0,w);

			int[] topLayer;
			topLayer = new int[w*h];
			TopImage.getRGB(0,0,w,h,topLayer,0,w);

			int[] dataLayer;
			dataLayer= new int[w*h];
			DataImage.getRGB(0,0,w,h,dataLayer,0,w);

			mapMatrix = new Pixel[w*h];

			for (int i=0; i<w*h; i++) {

				int colorTop = topLayer[i];
				int colorBottom = bottomLayer[i];
				int data = dataLayer[i];

				//Save pixel color
				if (colorTop == ColorData.transparent()) {
					Pixel newPixel = new Pixel(colorBottom, "bottom", data);
					mapMatrix[i] = newPixel;
				}
				else {
					Pixel newPixel = new Pixel(colorTop, "top", data);
					mapMatrix[i] = newPixel;
				}

				//Save pixel data
				mapMatrix[i] = checkSpecial(mapMatrix[i]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initHouse001() {
		//Images
		BufferedImage TopImage;
		BufferedImage BottomImage;
		BufferedImage DataImage;

		//Arrays

		try {
			//Load map layers
			BottomImage = ImageIO.read(getClass().getResource(maps.house001BottomPath)); 
			TopImage = ImageIO.read(getClass().getResource(maps.house001TopPath));
			DataImage = ImageIO.read(getClass().getResource(maps.house001DataPath));

			house001Width = BottomImage.getWidth();
			house001Height = BottomImage.getHeight();

			int w = house001Width;
			int h = house001Height;

			int[] bottomLayer;
			bottomLayer = new int[w*h];
			BottomImage.getRGB(0,0,w,h,bottomLayer,0,w);

			int[] topLayer;
			topLayer = new int[w*h];
			TopImage.getRGB(0,0,w,h,topLayer,0,w);

			int[] dataLayer;
			dataLayer= new int[w*h];
			DataImage.getRGB(0,0,w,h,dataLayer,0,w);

			house001Matrix = new Pixel[w*h];

			for (int i=0; i<w*h; i++) {

				int colorTop = topLayer[i];
				int colorBottom = bottomLayer[i];
				int data = dataLayer[i];

				//Save pixel color
				if (colorTop == ColorData.transparent()) {
					Pixel newPixel = new Pixel(colorBottom, "bottom", data);
					house001Matrix[i] = newPixel;
				}
				else {
					Pixel newPixel = new Pixel(colorTop, "top", data);
					house001Matrix[i] = newPixel;
				}

				//Save pixel data
				house001Matrix[i] = checkSpecial(house001Matrix[i]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initHouse001b() {
		//Images
		BufferedImage TopImage;
		BufferedImage BottomImage;
		BufferedImage DataImage;

		//Arrays

		try {
			//Load map layers
			BottomImage = ImageIO.read(getClass().getResource(maps.house001bBottomPath)); 
			TopImage = ImageIO.read(getClass().getResource(maps.house001bTopPath));
			DataImage = ImageIO.read(getClass().getResource(maps.house001bDataPath));

			house001bWidth = BottomImage.getWidth();
			house001bHeight = BottomImage.getHeight();

			int w = house001bWidth;
			int h = house001bHeight;

			int[] bottomLayer;
			bottomLayer = new int[w*h];
			BottomImage.getRGB(0,0,w,h,bottomLayer,0,w);

			int[] topLayer;
			topLayer = new int[w*h];
			TopImage.getRGB(0,0,w,h,topLayer,0,w);

			int[] dataLayer;
			dataLayer= new int[w*h];
			DataImage.getRGB(0,0,w,h,dataLayer,0,w);

			house001bMatrix = new Pixel[w*h];

			for (int i=0; i<w*h; i++) {

				int colorTop = topLayer[i];
				int colorBottom = bottomLayer[i];
				int data = dataLayer[i];

				//Save pixel color
				if (colorTop == ColorData.transparent()) {
					Pixel newPixel = new Pixel(colorBottom, "bottom", data);
					house001bMatrix[i] = newPixel;
				}
				else {
					Pixel newPixel = new Pixel(colorTop, "top", data);
					house001bMatrix[i] = newPixel;
				}

				//Save pixel data
				house001bMatrix[i] = checkSpecial(house001bMatrix[i]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initHouse002() {
		//Images
		BufferedImage TopImage;
		BufferedImage BottomImage;
		BufferedImage DataImage;

		//Arrays

		try {
			//Load map layers
			BottomImage = ImageIO.read(getClass().getResource(maps.house002BottomPath)); 
			TopImage = ImageIO.read(getClass().getResource(maps.house002TopPath));
			DataImage = ImageIO.read(getClass().getResource(maps.house002DataPath));

			house002Width = BottomImage.getWidth();
			house002Height = BottomImage.getHeight();

			int w = house002Width;
			int h = house002Height;

			int[] bottomLayer;
			bottomLayer = new int[w*h];
			BottomImage.getRGB(0,0,w,h,bottomLayer,0,w);

			int[] topLayer;
			topLayer = new int[w*h];
			TopImage.getRGB(0,0,w,h,topLayer,0,w);

			int[] dataLayer;
			dataLayer= new int[w*h];
			DataImage.getRGB(0,0,w,h,dataLayer,0,w);

			house002Matrix = new Pixel[w*h];

			for (int i=0; i<w*h; i++) {

				int colorTop = topLayer[i];
				int colorBottom = bottomLayer[i];
				int data = dataLayer[i];

				//Save pixel color
				if (colorTop == ColorData.transparent()) {
					Pixel newPixel = new Pixel(colorBottom, "bottom", data);
					house002Matrix[i] = newPixel;
				}
				else {
					Pixel newPixel = new Pixel(colorTop, "top", data);
					house002Matrix[i] = newPixel;
				}

				//Save pixel data
				house002Matrix[i] = checkSpecial(house002Matrix[i]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Pixel checkSpecial(Pixel newPixel) {

		int data = newPixel.data;

		switch (data) {
		
		case ColorData.house001: newPixel.setType("Door"); newPixel.setName("house001");
		break;
		
		case ColorData.house001b: newPixel.setType("Door"); newPixel.setName("house001b");
		break;

		case ColorData.house002: newPixel.setType("Door"); newPixel.setName("house002");
		break;
		
		case ColorData.map: newPixel.setType("Door"); newPixel.setName("map");
		break;
		}

		return newPixel;
	}


}