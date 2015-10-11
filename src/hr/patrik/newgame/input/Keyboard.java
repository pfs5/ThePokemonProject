package hr.patrik.newgame.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * 
 * Class for getting key info
 * 
 */

public class Keyboard implements KeyListener{

	public boolean[] keys = new boolean[256];
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		keys[key] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		keys[key] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}