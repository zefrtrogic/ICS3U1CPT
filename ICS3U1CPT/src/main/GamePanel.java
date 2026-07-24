package main;
import javax.swing.JPanel;
import javax.swing.JButton;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

	//Game State
	public final int titleState = 0; //sitting on the start screen, waiting for the player to press Start
	public final int playState = 1; //the actual timed run is in progress
	public final int endState = 2; //all keys collected, showing the finish time
	public int gameState = titleState; //game boots straight into the title screen
	public JButton startButton; //the visible "Start" button shown only during titleState
	private BufferedImage titleBackground; //a blurred snapshot of the game world, cached the first time the title screen is drawn

	//Sound
	public final int SOUND_BACKGROUND = 0;
	public final int SOUND_KEY = 1;
	public final int SOUND_START = 2;
	public final int SOUND_END = 3;
	public final int SOUND_BOOST = 4;
	public Sound music = new Sound(); //dedicated to the looping background track, kept separate so sound effects don't interrupt it
	public Sound se = new Sound(); //one-shot sound effects: start, key pickup, end

	//Font
	//the retro pixel font used everywhere text is drawn (HUD, title screen, end screen, the Start button);
	//loaded once in the constructor. Use gameFont.deriveFont(size) to get it at a specific size.
	public Font gameFont;
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
		this.setLayout(null); //switching off the default layout manager so the start button can be positioned manually with setBounds
		aSetter.setObject(); //scattering the keys onto the map before the game starts
		timerRunning = false; //the timer shouldn't run while sitting on the title screen
		gameFont = loadGameFont();
		setupStartButton();
	}
	//loads the retro pixel font from res/fonts; falls back to a built-in monospaced font
	//if the file isn't there yet, so the game never crashes over a missing font
	private Font loadGameFont() {
		try (InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P.ttf")) {
			if (is == null) {
				throw new IOException("Font resource not found at /fonts/PressStart2P.ttf");
			}
			Font custom = Font.createFont(Font.TRUETYPE_FONT, is);
			return custom;
		} catch (Exception e) {
			System.out.println("Retro font not found in res/fonts - using a built-in monospaced font instead.");
			return new Font("Monospaced", Font.BOLD, 20);
		}
	}
	//creates the "Start" button shown on the title screen, styled as plain text that grows/brightens on hover
	private void setupStartButton() {
		startButton = new JButton("Start");
		startButton.setFont(gameFont.deriveFont(18f));
		startButton.setForeground(Color.white);
		startButton.setContentAreaFilled(false); //no button background
		startButton.setBorderPainted(false); //no button border/box
		startButton.setFocusPainted(false); //no focus rectangle
		startButton.setOpaque(false);
		startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //signals it's clickable even without a visible button shape

		int centerX = screenWidth / 2;
		int baseY = screenHeight / 2 + 30; //placed just under the description text drawn in drawTitleScreen

		//normal size/position
		int normalWidth = 100, normalHeight = 40;
		startButton.setBounds(centerX - normalWidth / 2, baseY, normalWidth, normalHeight);

		//slightly larger size/position used on hover, to create the "pop out" effect
		int hoverWidth = 130, hoverHeight = 50;

		startButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				startButton.setFont(gameFont.deriveFont(22f));
				startButton.setForeground(Color.yellow);
				startButton.setBounds(centerX - hoverWidth / 2, baseY - 5, hoverWidth, hoverHeight);
			}
			public void mouseExited(MouseEvent e) {
				startButton.setFont(gameFont.deriveFont(18f));
				startButton.setForeground(Color.white);
				startButton.setBounds(centerX - normalWidth / 2, baseY, normalWidth, normalHeight);
			}
		});

		startButton.addActionListener(e -> startGame());
		this.add(startButton);
	}
	//called when the player clicks Start: begins the timed run and hands keyboard focus back to the game panel
	public void startGame() {
		gameState = playState;
		timerRunning = true; //the run officially starts now
		startButton.setVisible(false); //hide the button, it's only needed on the title screen
		this.requestFocusInWindow(); //clicking the button steals keyboard focus; this gives it back so WASD/E/R work immediately
		playSE(SOUND_START); //the little jingle for pressing Start
		playMusic(SOUND_BACKGROUND); //background music kicks in as the run begins
	}
	//(re)starts the looping background music, stopping whatever it was playing first
	public void playMusic(int i) {
		music.stop();
		music.setFile(i);
		music.loop();
	}
	//cuts off the background music (used when the run ends)
	public void stopMusic() {
		music.stop();
	}
	//plays a one-shot sound effect (start jingle, key pickup, end fanfare) without affecting the background music
	public void playSE(int i) {
		se.setFile(i);
		se.play();
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
		if (gameState == playState) {
			player.update(); //runs the update method in entity class
			if (key.ePressed) {
				activateBoost();
				key.ePressed = false; //consume the press so holding "E" doesn't keep re-triggering it
			}
		}
		if (gameState == playState || gameState == endState) {
			if (key.rPressed) {
				resetGame(); //restart the run: player position, keys, and timer all go back to their starting state
				key.rPressed = false; //consume the press so it only resets once per key-down, not every frame it's held
			}
		}
	}
	//starts a 3-second speed boost, as long as one isn't already active and charges remain
	public void activateBoost() {
		if (!boostActive && boostsRemaining > 0) {
			boostActive = true;
			boostRemainingNanos = boostDurationNanos;
			boostsRemaining--;
			player.speed = boostSpeed;
			playSE(SOUND_BOOST); //little whoosh sound the moment the boost kicks in
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
		gameState = playState; //in case this was called from the end screen, jump back into gameplay
		playMusic(SOUND_BACKGROUND); //restart the background track fresh for the new run
	}
	//built in method that draws things
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //needed for the pointComponenet to work
		Graphics2D g2 = (Graphics2D)g; //Graphics 2D is more sophisticated that regular graphics

		if (gameState == titleState) {
			drawTitleScreen(g2); //just the description text; the Start button draws itself as a normal Swing component
		} else if (gameState == playState) {
			tileM.draw(g2); //draws the tile through tile manager class, tile first before character overlaps the tile, from the draw method
			drawObjects(g2); //draws any keys still left on the map, on top of the tiles but underneath the player
			player.draw(g2); // runs the draw method in the player class, generating the image for the player chracter
			ui.draw(g2); //drawing the key counter last so it sits on top of everything else
			//draw the fps counter last so it stays on top of everything else
			if (showFPS) {
				drawFPS(g2);
			}
			drawTimer(g2); //always show the timer, right under the FPS text
		} else if (gameState == endState) {
			drawEndScreen(g2);
		}
		//NOTE: g2.dispose() used to be called here. g2 is the SAME object as the "g" parameter Swing
		//passes in (not a copy from g.create()), and Swing reuses that exact object to paint child
		//components - like the Start button - right after this method returns. Disposing it here was
		//silently breaking the button's text rendering. Don't dispose a Graphics object you didn't create.
	}
	//draws the title screen's heading and description over a blurred snapshot of the game world;
	//the Start button itself is a real JButton, added in setupStartButton()
	private void drawTitleScreen(Graphics2D g2) {
		if (titleBackground == null) {
			titleBackground = createBlurredBackground(); //only needs to be rendered once, it never changes
		}
		g2.drawImage(titleBackground, 0, 0, null);

		//dark translucent overlay so the white text stays readable over the busy blurred scene
		g2.setColor(new Color(0, 0, 0, 140));
		g2.fillRect(0, 0, screenWidth, screenHeight);

		g2.setColor(Color.white);
		g2.setFont(gameFont.deriveFont(28f));
		String title = "Key Chaser";
		int titleWidth = g2.getFontMetrics().stringWidth(title);
		g2.drawString(title, (screenWidth - titleWidth) / 2, screenHeight / 2 - 80);

		g2.setFont(gameFont.deriveFont(16f));
		String line1 = "Hi, Welcome to Key Chaser!";
		String line2 = "Collect all 10 keys as fast as you can!";
		int line1Width = g2.getFontMetrics().stringWidth(line1);
		int line2Width = g2.getFontMetrics().stringWidth(line2);
		g2.drawString(line1, (screenWidth - line1Width) / 2, screenHeight / 2 - 20);
		g2.drawString(line2, (screenWidth - line2Width) / 2, screenHeight / 2 + 10);
	}
	//renders a normal frame of gameplay off-screen, then blurs it with a box-blur convolution
	//to use as the title screen's background
	private BufferedImage createBlurredBackground() {
		BufferedImage snapshot = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D sg2 = snapshot.createGraphics();
		tileM.draw(sg2); //drawing the map, keys, and player exactly like a normal frame, just off-screen
		drawObjects(sg2);
		player.draw(sg2);
		sg2.dispose();

		//averaging every pixel with a wide neighborhood around it produces a simple, cheap blur
		int blurSize = 9;
		float weight = 1.0f / (blurSize * blurSize);
		float[] data = new float[blurSize * blurSize];
		Arrays.fill(data, weight);
		Kernel kernel = new Kernel(blurSize, blurSize, data);
		ConvolveOp blurOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		return blurOp.filter(snapshot, null);
	}
	//draws the victory screen once all keys have been collected, reusing the same blurred background as the title screen
	private void drawEndScreen(Graphics2D g2) {
		if (titleBackground == null) {
			titleBackground = createBlurredBackground(); //safety net in case this somehow got reached without visiting the title screen first
		}
		g2.drawImage(titleBackground, 0, 0, null);

		g2.setColor(new Color(0, 0, 0, 140));
		g2.fillRect(0, 0, screenWidth, screenHeight);

		g2.setColor(Color.white);
		g2.setFont(gameFont.deriveFont(18f));
		String message = "Congratulations, you have collected all keys!";
		int messageWidth = g2.getFontMetrics().stringWidth(message);
		g2.drawString(message, (screenWidth - messageWidth) / 2, screenHeight / 2 - 40);

		int minutes = secondsElapsed / 60;
		int seconds = secondsElapsed % 60;
		String timeText = "Your time: " + String.format("%02d:%02d", minutes, seconds);
		g2.setFont(gameFont.deriveFont(14f));
		int timeWidth = g2.getFontMetrics().stringWidth(timeText);
		g2.drawString(timeText, (screenWidth - timeWidth) / 2, screenHeight / 2);

		String restartHint = "Press R to play again";
		g2.setFont(gameFont.deriveFont(11f));
		int hintWidth = g2.getFontMetrics().stringWidth(restartHint);
		g2.drawString(restartHint, (screenWidth - hintWidth) / 2, screenHeight / 2 + 40);
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
		g2.setFont(gameFont.deriveFont(14f));
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
		g2.setFont(gameFont.deriveFont(20f));
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