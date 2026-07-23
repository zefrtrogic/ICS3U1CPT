package main;
import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;	
//Class for Gamepanel that is a subclass of JPanel 
public class GamePanel extends JPanel implements Runnable{
	//Scree Settings
	public final int regularTileScale = 16; //All Tiles in the window will be 16 by 16 (size)
	public final int scale = 3; //Scaling since resolution is higher on modern computers, this change allows the picture to be 16x16 but looks like 48x48
	public final int finalsize = regularTileScale * scale; //getting 48*48 tile
	public final int maxColsize = 16; //making 16 tiles in one column
	public final int maxRowsize = 12; //making 12 tiles in one row
	public final int screenWidth = finalsize * maxColsize; //tile size multiplied by the amount of tiles there are in a column to find the width of the window (768) pixels
	public final int screenHeight = finalsize * maxRowsize; //tile size multiplied by the amount of tiles there are in a row to find the length of the window (576) pixels
	public int maxMap = 10; //showing the maximun number of maps availble
	public int currentMap = 0; //updates as we transition into different maps
	//World Setting
	public final int maxWorldCol = 112;
	public final int maxWorldRow = 12;
	public final int worldWidth = regularTileScale*maxWorldCol;
	public final int worldHeight = regularTileScale*maxWorldRow;
	
	//FPS
	int FPS = 150;
	//Holds the most recently measured frames-per-second value so it can be drawn on screen.
	//Updated once per second in run(); paintComponent() just reads this and displays it.
	public int currentFPS = 0;
	//Toggle this to true/false to show or hide the on-screen FPS counter
	public boolean showFPS = true;
	TileManager tileM = new TileManager(this); //adds tilemanager into gamepanel from the tilemanager class
	KeyHandler key = new KeyHandler(); //initialize the keyhandler
	Thread gameThread; //creating a thread that allows frames
	public CollisionChecker checker = new CollisionChecker(this); //intializing collision checker
	AssetSetter aSetter = new AssetSetter(this); //places objects (keys, etc) onto the map
	public UI ui = new UI(this); //draws the on-screen key counter and any future HUD elements
	public Player player = new Player(this,key); //Initiating the player class
	//Objects
	public SuperObject obj[] = new SuperObject[10]; //holds every placeable object currently on the map (keys, etc); empty slots are left null
	public int keyCount = 0; //how many keys the player has collected so far
	public final int totalKeys = 10; //how many keys need to be collected before the timer stops

	//Timer
	public int secondsElapsed = 0; //counts up once per real second while timerRunning is true
	public boolean timerRunning = true; //Player sets this to false once keyCount reaches totalKeys

	//Speed Boost
	public final int maxBoosts = 3; //how many times the boost can be used per run
	public int boostsRemaining = maxBoosts; //ticks down each use; UI shows this so the player knows when they're out
	public boolean boostActive = false; //true for the 3 seconds after activating a boost
	private long boostRemainingNanos = 0; //counts down while boostActive; the boost ends when this reaches 0
	private final long boostDurationNanos = 3000000000L; //3 seconds, in nanoseconds
	private final int normalSpeed = 1; //player's regular walking speed
	private final int boostSpeed = 3; //player's speed while boosted
	//setting up variables
	int playerX = 500;
	int playerY = 500;
	int playerSpeed = 1;
		
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); //setting the dimensions the screen
		this.setBackground(Color.black); //making sure that background is back so no interference with dimensions
		this.setDoubleBuffered(true); //Improves game rendering performance by drawing components on an off screen painting buffer
		this.addKeyListener(key); //adds the key handler, (a.k.a the user controls up, down, right, left)
		this.setFocusable(true); //allows the computer to receive input
		aSetter.setObject(); //scattering the keys onto the map before the game starts
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
			if (boostActive) {
				boostRemainingNanos -= (currentTime-lastTime); //counting down the 3-second boost window in real time
				if (boostRemainingNanos <= 0) {
					boostActive = false; //boost window is over
					player.speed = normalSpeed; //back to regular walking speed
				}
			}
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
				currentFPS = (int) FPScount; //store the measured fps so paintComponent can draw it
				if (timerRunning) {
					secondsElapsed++; //one real second has passed since the game started
				}
				FPStimer = 0; //reset the fps timer
				FPScount = 0; // reset the fps counter
			}
		}
	}
	//method that updates the players position
	public void update() {
		player.update(); //runs the update method in entity class
		if (key.rPressed) {
			resetGame(); //restart the run: player position, keys, and timer all go back to their starting state
			key.rPressed = false; //consume the press so it only resets once per key-down, not every frame it's held
		}
		if (key.ePressed) {
			activateBoost();
			key.ePressed = false; //consume the press so holding "E" doesn't keep re-triggering it
		}
	}
	//starts a 3-second speed boost, as long as one isn't already active and charges remain
	public void activateBoost() {
		if (!boostActive && boostsRemaining > 0) {
			boostActive = true;
			boostRemainingNanos = boostDurationNanos;
			boostsRemaining--;
			player.speed = boostSpeed;
		}
	}
	//restarts a timed run from scratch: repositions the player, clears the key counter,
	//re-scatters fresh keys onto the map, and starts the timer running again from zero
	public void resetGame() {
		player.DefaultValues(); //puts the player back at their starting x/y/direction (also resets speed to normalSpeed)
		keyCount = 0;
		secondsElapsed = 0;
		timerRunning = true;
		boostsRemaining = maxBoosts; //refill boost charges for the new run
		boostActive = false;
		boostRemainingNanos = 0;
		aSetter.setObject(); //recreates all 10 keys fresh, overwriting any that were picked up (null) or left over
	}
	//built in method that draws things
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //needed for the pointComponenet to work
		Graphics2D g2 = (Graphics2D)g; //Graphics 2D is more sophisticated that regular graphics
		tileM.draw(g2); //draws the tile through tile manager class, tile first before character overlaps the tile, from the draw method
		drawObjects(g2); //draws any keys still left on the map, on top of the tiles but underneath the player
		player.draw(g2); // runs the draw method in the player class, generating the image for the player chracter
		ui.draw(g2); //drawing the key counter last so it sits on top of everything else
		//draw the fps counter last so it stays on top of everything else
		if (showFPS) {
			drawFPS(g2);
		}
		drawTimer(g2); //always show the timer, right under the FPS text
		g2.dispose(); //gets rid of the drawing, saving resources
	}
	//draws every object currently on the map (keys, etc), skipping any slot that's been picked up (null)
	public void drawObjects(Graphics2D g2) {
		for (int i = 0; i < obj.length; i++) {
			if (obj[i] != null) {
				//same horizontal-scroll math the tile map uses; the map's full height already fits on screen so no vertical offset is needed
				int screenX = obj[i].worldX - player.x + player.screenX;
				int screenY = obj[i].worldY;
				g2.drawImage(obj[i].image, screenX, screenY, finalsize, finalsize, null);
			}
		}
	}
	//draws the current fps value in the top-left corner of the screen
	private void drawFPS(Graphics2D g2) {
		g2.setFont(new Font("Arial", Font.BOLD, 20));
		String fpsText = "FPS: " + currentFPS;
		int x = 10;
		int y = 25;
		//black outline so the text stays readable over any background
		g2.setColor(Color.black);
		g2.drawString(fpsText, x - 1, y);
		g2.drawString(fpsText, x + 1, y);
		g2.drawString(fpsText, x, y - 1);
		g2.drawString(fpsText, x, y + 1);
		//main text on top
		g2.setColor(Color.white);
		g2.drawString(fpsText, x, y);
	}
	//draws the elapsed time (mm:ss) directly under the fps counter; stops updating once timerRunning is false
	private void drawTimer(Graphics2D g2) {
		g2.setFont(new Font("Arial", Font.BOLD, 20));
		int minutes = secondsElapsed / 60;
		int seconds = secondsElapsed % 60;
		String timerText = String.format("%02d:%02d", minutes, seconds);
		int x = 10;
		int y = 50; //25 (fps line) + 25 spacing puts this line directly under it

		g2.setColor(Color.black);
		g2.drawString(timerText, x - 1, y);
		g2.drawString(timerText, x + 1, y);
		g2.drawString(timerText, x, y - 1);
		g2.drawString(timerText, x, y + 1);

		g2.setColor(Color.white);
		g2.drawString(timerText, x, y);
	}
	
}