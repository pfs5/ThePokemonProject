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
	private enum STATE {
		GAME,
		BATTLE
	}
	//Window settings
	private static int WIDTH = 500;
	private static int HEIGHT = (int) (WIDTH*(9./16.));
	private static int scale = 2;
	private final static String title = "New Game";
	private final int speed = 200;
	private static int BASE = 30;		//If changed - must be updated in Screen class			

	private static int width = WIDTH*scale;
	private static int height = HEIGHT*scale;
	final int ups = speed*scale;
	private STATE state;

	//Initial position
	int xOffset = 0;
	int yOffset = BASE*scale*4;

	private Thread thread;
	private JFrame frame;
	private boolean running = false;

	private Screen screen;
	private Keyboard keyboard;

	private BufferedImage currentScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)currentScreen.getRaster().getDataBuffer()).getData();

	public Game () {
		Dimension size = new Dimension (width, height);
		setPreferredSize(size);

		screen = new Screen(width, height, scale, xOffset, yOffset);
		frame = new JFrame();
		keyboard = new Keyboard();
		state = STATE.GAME;

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
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			//Print UPS, FPS
			if (System.currentTimeMillis()-timer > 1000) {
				timer+=1000;
				frame.setTitle(title + "  |  " + updates + " ups, " + frames + " fps"
						+ "   |   " + width + "x" + height);
				updates = 0;
				frames = 0;
			}
		}
		stop(); //TODO maybe remove
	}

	//Game logics
	public void update () {
		if (state == STATE.GAME) {
			getKeyGame();
			screen.move();
		}
		else if (state == STATE.BATTLE) {

		}
	}

	public void getKeyGame () {	
		if (keyboard.keys[KeyEvent.VK_W])
			screen.setMove("U");

		if (keyboard.keys[KeyEvent.VK_A])
			screen.setMove("L");

		if (keyboard.keys[KeyEvent.VK_S])
			screen.setMove("D");

		if (keyboard.keys[KeyEvent.VK_D]) 
			screen.setMove("R");

	}

	//Game graphics
	public void render () {

		//Create buffer strategy
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		if (state == STATE.GAME) {
			//Draw screen
			screen.clear();
			screen.render();

			//Copy image pixels into current screen
			for (int i=0; i<pixels.length; i++) {
				pixels[i] = screen.pixels[i];
			}

		}
		Graphics g = bs.getDrawGraphics();

		//Display graphics
		{
			//Draw map
			g.drawImage(currentScreen,0,0,getWidth(),getHeight(),null);
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
