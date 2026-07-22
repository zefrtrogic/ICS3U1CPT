package main;

import object.OBJ_Key;
//Handles placing objects (like keys) onto the map at specific tile coordinates.
//Keeping this separate from GamePanel means adding/moving objects later is a one-file change.
public class AssetSetter {
	GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}

	//creates each key and places it at a chosen column/row on the tile grid
	//worldX/worldY = column/row multiplied by finalsize (48), same math the tile map itself uses
	public void setObject() {
		gp.obj[0] = new OBJ_Key();
		gp.obj[0].worldX = gp.finalsize * 5;
		gp.obj[0].worldY = gp.finalsize * 2;

		gp.obj[1] = new OBJ_Key();
		gp.obj[1].worldX = gp.finalsize * 10;
		gp.obj[1].worldY = gp.finalsize * 4;

		gp.obj[2] = new OBJ_Key();
		gp.obj[2].worldX = gp.finalsize * 8;
		gp.obj[2].worldY = gp.finalsize * 6;
	}
}