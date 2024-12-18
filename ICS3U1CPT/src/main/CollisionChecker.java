package main;
import entity.Entity;
public class CollisionChecker {
	GamePanel gp;
	//creating constructor to be able to use gamepanel
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	//method to check the collision of player by receiving the player's position
	public void Checktile(Entity player) {
		//finding the approximate coordinates that the player is at by checking the entities coordinate point on the tile and then checking its pixel point within the tile
		int playerleftworldx = player.x + player.solidArea.x;
		int playerrightworldx = player.x + player.solidArea.x + player.solidArea.width;
		int playertopworldy = player.y + player.solidArea.y;
		int playerbottomworldy = player.y + player.solidArea.y + player.solidArea.height;
		
		//finding the column and row of the box by dividing the tile size by the calculated player's coordinate position
		int playerleftcol = playerleftworldx/gp.finalsize;
		int playerrightcol = playerrightworldx/gp.finalsize;
		int playertoprow = playertopworldy/gp.finalsize;
		int playerbottomrow = playerbottomworldy/gp.finalsize;
		
		int tileNum1, tileNum2;
		//checking the direction of the sprite
		switch(player.direction) {
		case "up":
			playertoprow = (playertopworldy-player.speed)/gp.finalsize; 
			tileNum1 = gp.tileM.map[playerleftcol] [playertoprow]; //finding leftmost top coordinate of the player
			tileNum2 = gp.tileM.map[playerrightcol] [playertoprow]; //finding the rightmost top coordinate of the player
			//checking if the tile in that coordinate has collision on through a boolean, if so, sets boolean collisionOn to true
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) player.collisionOn = true;
			break;
		case "down":
			playerbottomrow = (playerbottomworldy-player.speed)/gp.finalsize;
			tileNum1 = gp.tileM.map[playerleftcol] [playerbottomrow];
			tileNum2 = gp.tileM.map[playerrightcol] [playerbottomrow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) player.collisionOn = true;
			break;
		case "right":
			playerrightcol = (playerrightworldx-player.speed)/gp.finalsize;
			tileNum1 = gp.tileM.map[playerrightcol] [playertoprow];
			tileNum2 = gp.tileM.map[playerrightcol] [playerbottomrow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) player.collisionOn = true;
			break;
		case "left":
			playerleftcol = (playerleftworldx+player.speed)/gp.finalsize;
			tileNum1 = gp.tileM.map[playerleftcol] [playertoprow];
			tileNum2 = gp.tileM.map[playerleftcol] [playerbottomrow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) player.collisionOn = true;
			break;
		}
		
		
		
	}
}
