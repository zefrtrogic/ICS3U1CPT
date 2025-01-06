package entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import tile.TileManager;
//creating class a subclass of entity
public class Player extends Entity {
	GamePanel gp; //Initializing GamePanel from main class
	KeyHandler key; //Initializing Keyhandler from main class
	public final int screenX; //creating varible to track center poisiton of player on map
	public Player(GamePanel gp, KeyHandler key) {
		this.gp = gp; //Allowing us to use gamepanel from player class
		this.key = key; //Allowing us to use keyhandler from player class
		solidArea = new Rectangle(8, 16, 28, 28); //Initiating the rectangle at pos 8, 16 with a 28 by 28 size
		screenX = gp.screenHeight/2; //returns the halfway point of the screen on the x axis
		DefaultValues(); //runs the default value method to set the players starting position
		getPlayerImage(); //running the method to upload images for the player
	}
	
	public void DefaultValues() {
		//creating default values for the players position
		x = 580; 
		y = 430;
		speed = 1;
		direction = "down";
	}
	public void getPlayerImage() {
		try {
			//getting the images of each player position
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
		}catch(IOException e) {
			//uploading all the player images
			e.printStackTrace();
		}
	}
	public void update() {
		//Checking if the user has pressed a key and updates the direction based on the input, then updating so that we can include collision
				if (key.upPressed == true || key.downPressed == true || key.leftPressed == true || key.rightPressed == true) {
					//checking which key the user has clicked
				if (key.upPressed == true) direction = "up";
				else if (key.downPressed == true) direction = "down";
				else if (key.leftPressed == true) direction = "left";
				else if (key.rightPressed == true) direction = "right";
				collisionOn = false; //setting collision to off
				gp.checker.Checktile(this); //we run the check tile method that passes the Player class as an entity as it is a subclass of the entity class
				//Checking if player is allowed to move for collision
				//checking if collision is toggled
				if (collisionOn == false) {
					//switch statement to update position of sprite
					switch (direction) {
					case "up": y-=speed; break;
					case "down": y+=speed; break;
					case "left": x-=speed; break;
					case "right": x+=speed; break;
					}
				}
				spriteCounter++; // adding to counter
				// checking if we have hit 12 frames, confirming that is time to change the picture, making a running animation
				if (spriteCounter > 12) {
					//if statements to switch from pos 1 to 2
					if (spriteNum == 1) {
						spriteNum = 2;
					}
					else if (spriteNum == 2) {
						spriteNum = 1;
					}
					spriteCounter = 0; // resetting counter
				}
				}
	}
	public void draw(Graphics2D g2) {
	//g2.setColor(Color.white); // setting color of the drawing
	//g2.fillRect(x, y, gp.finalsize, gp.finalsize); // fills the drawing based on x and y coordinate and width and height of the drawing
		BufferedImage image = null; // Initializing variable to find the image of the input of direction from the user
		//switch statement to correlate input to image and adding animation with SpriteNum
		switch(direction) {
		//checking which direction the input is in through variable
		case "up":
			//checking which frame the player is in
			if (spriteNum == 1) {
				//switching the image based on the original image
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			}
			if (spriteNum == 2) {
				image = down2;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		}
		g2.drawImage(image, screenX, y, gp.finalsize, gp.finalsize, null); //printing the new player tile on the screen
	}
}
