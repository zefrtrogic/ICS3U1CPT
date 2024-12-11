package entity;

import java.awt.image.BufferedImage;

public class Entity {
	
	public int x, y; 
	public int speed;
	
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; //creating and storing player image files
	public String direction; //finding direction of input
	
	public int spriteCounter = 0; //creating counter to use in player class
	public int spriteNum = 1; // global creating variable to check position of image
}
