package main;
import java.util.*;
import entity.Entity;
import java.io.*;
public class CollisionChecker {
	GamePanel gp;
	//creating constructor to be able to use gamepanel
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	public void Checktile(Entity entity) {
		int playerleftworldx = entity.x + entity.solidArea.x; //finding the bottom x coordinate of the entity to the top corner x of the entity
		int playerrightworldx = entity.x + entity.solidArea.x + entity.solidArea.width; //same thing but finding the width of the rectangle
		int playertopworldy = entity.y + entity.solidArea.y;
		int playerbottomworldy = entity.y + entity.solidArea.y + entity.solidArea.height;
		
		int playerleftcol = playerleftworldx/gp.finalsize;
		int playerrightcol = playerrightworldx/gp.finalsize;
		int playertoprow = playertopworldy/gp.finalsize;
		int playerbottomrow = playerbottomworldy/gp.finalsize;
		
		int tileNum1, tileNum2;
		//checking the direction of the sprite
		switch(entity.direction) {
		case "up":
			playertoprow = (playertopworldy-entity.speed)/gp.finalsize; 
			tileNum1 = gp.tileM.map[playerleftcol] [playertoprow]; //finding leftmost top coordinate of the player
			tileNum2 = gp.tileM.map[playerrightcol] [playertoprow]; //finding the rightmost top coordinate of the player
			//checking if the tile in that coordinate has collision on through a boolean, if so, sets boolean collisionOn to true
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			break;
		case "down":
			playerbottomrow = (playerbottomworldy-entity.speed)/gp.finalsize;
			tileNum1 = gp.tileM.map[playerleftcol] [playerbottomrow];
			tileNum2 = gp.tileM.map[playerrightcol] [playerbottomrow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			break;
		case "right":
			playerrightcol = (playerrightworldx-entity.speed)/gp.finalsize;
			tileNum1 = gp.tileM.map[playerrightcol] [playertoprow];
			tileNum2 = gp.tileM.map[playerrightcol] [playerbottomrow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			break;
		case "left":
			playerleftcol = (playerleftworldx+entity.speed)/gp.finalsize;
			tileNum1 = gp.tileM.map[playerleftcol] [playertoprow];
			tileNum2 = gp.tileM.map[playerleftcol] [playerbottomrow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			break;
		}
		
		
		
	}
}
