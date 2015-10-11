package hr.patrik.newgame.main;

import hr.patrik.newgame.graphics.Screen;
import hr.patrik.newgame.input.Keyboard;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static int WIDTH = 500;
	public static int HEIGHT = WIDTH/ 16 * 9;
	public static int scale = 2;		//Multiplication
	public final static String title = "New Game";
	public final int speed = 600;

	public static int width = WIDTH*scale;
	public static int height = HEIGHT*scale;
	final int ups = speed*scale;
	
	int xOffset = 0;
	int yOffset = 0;

	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	
	private Screen screen;
	private Keyboard keyboard;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

	public Game () {
		Dimension size = new Dimension (width, height);
		setPreferredSize(size);

		screen = new Screen(width, height, scale);
		frame = new JFrame();
		keyboard = new Keyboard();
		
		frame.addKeyListener(keyboard);
	}

	public synchronized void start () {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop () {
		running = false;
		try {
			thread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Game loop
	public void run () {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / ups;
		double delta = 0;
		int frames = 0;
		int updates = 0;

		//Game loop
		while (running) {
			//Measure FPS and UPS
			long now = System.nanoTime();
			delta += (now-lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			//Print UPS, FPS
			if (System.currentTimeMillis()-timer > 1000) {
				timer+=1000;
				frame.setTitle(title + "  |  " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop(); //TODO maybe remove
	}

	//Game logics
	public void tick () {
		getKey();
	}
	
	public void getKey () {	
		if (keyboard.keys[KeyEvent.VK_W])
			yOffset--;
		
		if (keyboard.keys[KeyEvent.VK_A])
			xOffset--;
		
		if (keyboard.keys[KeyEvent.VK_S])
			yOffset++;
		
		if (keyboard.keys[KeyEvent.VK_D]) 
			xOffset++;

		//Check map edges
		int mapWidth = screen.mapWidth;
		int mapHeight = screen.mapHeight;
		
		if (xOffset<0)
			xOffset = 0;
		if (yOffset<0)
			yOffset = 0;
		
		if (xOffset+getWidth()>mapWidth*scale)
			xOffset--;
		if (yOffset+getHeight()>mapHeight*scale)
			yOffset--;
	}

	//Game graphics
	public void render () {
		
		//Create buffer strategy
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		//Draw screen
		screen.clear();
		screen.render(xOffset,yOffset);
		
		for (int i=0; i<pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//Display graphics
		{
			g.drawImage(image,0,0,getWidth(),getHeight(),null);
		}
		g.dispose();
		bs.show();
	}

	public static void main (String args []) {
		Game game = new Game();

		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}

}
