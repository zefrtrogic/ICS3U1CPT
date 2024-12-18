package main;
import javax.swing.JPanel;

import entity.Player;
import pokemon.masterpokemon;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;	
//Class for Gamepanel that is a subclass of JPanel 
public class GamePanel extends JPanel implements Runnable{
	public final int regularTileScale = 16; //All Tiles in the window will be 16 by 16 (size)
	public final int scale = 3; //Scaling since resolution is higher on modern computers, this change allows the picture to be 16x16 but looks like 48x48
	public final int finalsize = regularTileScale * scale; //getting 48*48 tile
	public final int maxColsize = 16; //making 16 tiles in one column
	public final int maxRowsize = 12; //making 12 tiles in one row
	public final int screenWidth = finalsize * maxColsize; //tile size multiplied by the amount of tiles there are in a column to find the width of the window (768) pixels
	public final int screenHeight = finalsize * maxRowsize; //tile size multiplied by the amount of tiles there are in a row to find the length of the window (576) pixels
	
	//FPS
	int FPS = 150;
	TileManager tileM = new TileManager(this); //adds tilemanager into gamepanel from the tilemanager class
	KeyHandler key = new KeyHandler(); //initialize the keyhandler
	Thread gameThread; //creating a thread that allows frames
	public CollisionChecker checker = new CollisionChecker(this); //intializing collision checker
	public pokemonsetter pSetter = new pokemonsetter(this); //Initializing pokemonsetter class and takes in gamepanel class
	public Player player = new Player(this,key); //Initiating the player class
	public masterpokemon slot[] = new masterpokemon[10];
	//Player's starting position on the screen
	int playerX = 500;
	int playerY = 500;
	int playerSpeed = 1;
		
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); //setting the dimensions the screen
		this.setBackground(Color.black); //making sure that background is back so no interference with dimensions
		this.setDoubleBuffered(true); //Improves game rendering performance by drawing components on an off screen painting buffer
		this.addKeyListener(key); //adds the key handler, (a.k.a the user controls up, down, right, left)
		this.setFocusable(true); //allows the computer to receive input
	}
	
	public void GameSet() {
		pSetter.setObject();
	}
	public void startGameThread() {
		gameThread = new Thread(this); //Initializing game thread aka timer
		gameThread.start(); //Starts the thread that starts the run method since the class implemented runnable
	}
	@Override
	public void run() {
		//creating variables for timer to stop moving player super fast
		double interval = 1000000000/FPS;
		double remaining = 0;
		long lastTime = System.nanoTime(); //getting current time of game open in nanoseconds
		long currentTime;
		//Saving variables for fps
		long FPStimer = 0;
		long FPScount = 0;
		//while loop to continue the game until gameThread dosen't exist, meaning the game is not running
		while (gameThread != null)  {
			currentTime = System.nanoTime(); // getting current time of game open in nanoseconds
			remaining+= (currentTime-lastTime)/interval; //formula to find how much time is needed between each interval
			FPStimer+=currentTime-lastTime;
			lastTime = currentTime; //making the current time in nanoseconds to the precious time to keep the system running and updating
			//Statement to check if we have hit the interval time and updates the coordinate points and drawing 
			if (remaining >= 1) {
			update(); //updating the coordinate position of the player after inputs
			repaint(); //updating the player's drawing based on their input
			//This loop allows the game to update the location of the map based on where the sprite is
			remaining--; //reseting the accumulator
			FPScount++; //adding each frame to the fps counter
			}
			//if the timer has hit 1 second
			if (FPStimer >= 1000000000) {
				//System.out.println("FPS:" + FPScount); //print the counter of the fps value
				FPStimer = 0; //reset the fps timer
				FPScount = 0; // reset the fps counter
			}
		}
	}
	//method that updates the players position
	public void update() {
		player.update(); //runs the update method in entity class
	}
	//built in method that draws things
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //needed for the pointComponenet to work
		Graphics2D g2 = (Graphics2D)g; //Graphics 2D is more sophisticated that regular graphics
		tileM.draw(g2); //draws the tile through tile manager class, tile first before character overlaps the tile, from the draw method
		//Looping through each slot in the array
		for (int i = 0; i < slot.length; i++) {
			if (slot[i] != null) {
				slot[i].draw(g2, this); //setting the slow position to the 
			}
		}
		player.draw(g2); // runs the draw method in the player class, generating the image for the player chracter
		g2.dispose(); //gets rid of the drawing, saving resources
	}
	
}
