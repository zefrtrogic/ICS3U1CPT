package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//class for input of user for directions E.X Left, Right, Up, Down inputs, implements KeyListener interface for this function
public class KeyHandler implements KeyListener{
	
	public boolean upPressed, downPressed, leftPressed, rightPressed; //global boolean variables to check the status of the key input

	@Override
	public void keyTyped(KeyEvent e) {
		 //Not using this method as it is not needed for this game but needed for the KeyListener interface to run
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode(); //returns the keycode that the user inputs
		
		//setting boolean to true if the key is pressed
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode(); //returns the keycode that the user inputs
		
		//setting boolean to false if the key is released
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;
		
	}
	
}
