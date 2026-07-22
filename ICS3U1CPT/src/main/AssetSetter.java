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
		gp.obj[0].worldX = gp.finalsize * 13;
		gp.obj[0].worldY = gp.finalsize * 2;

		gp.obj[1] = new OBJ_Key();
		gp.obj[1].worldX = gp.finalsize * 18;
		gp.obj[1].worldY = gp.finalsize * 1;

		gp.obj[2] = new OBJ_Key();
		gp.obj[2].worldX = gp.finalsize * 18;
		gp.obj[2].worldY = gp.finalsize * 10;

		gp.obj[3] = new OBJ_Key();
		gp.obj[3].worldX = gp.finalsize * 28;
		gp.obj[3].worldY = gp.finalsize * 2;

		gp.obj[4] = new OBJ_Key();
		gp.obj[4].worldX = gp.finalsize * 33;
		gp.obj[4].worldY = gp.finalsize * 3;

		gp.obj[5] = new OBJ_Key();
		gp.obj[5].worldX = gp.finalsize * 46;
		gp.obj[5].worldY = gp.finalsize * 2;

		gp.obj[6] = new OBJ_Key();
		gp.obj[6].worldX = gp.finalsize * 54;
		gp.obj[6].worldY = gp.finalsize * 10;

		gp.obj[7] = new OBJ_Key();
		gp.obj[7].worldX = gp.finalsize * 70;
		gp.obj[7].worldY = gp.finalsize * 1;

		gp.obj[8] = new OBJ_Key();
		gp.obj[8].worldX = gp.finalsize * 90;
		gp.obj[8].worldY = gp.finalsize * 10;

		gp.obj[9] = new OBJ_Key();
		gp.obj[9].worldX = gp.finalsize * 104;
		gp.obj[9].worldY = gp.finalsize * 2;
	}
}