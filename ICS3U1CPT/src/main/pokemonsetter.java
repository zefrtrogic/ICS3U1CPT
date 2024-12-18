package main;

import pokemon.objectkey;

public class pokemonsetter {
	 
	GamePanel gp;
	
	public pokemonsetter(GamePanel gp) {
		this.gp=gp;
	}
	
	public void setObject() {
		gp.slot[0] = new objectkey(); //running the objectkey method that uploads the object into the slot of the game
		gp.slot[0].pokemonx = 8 * gp.finalsize; //creating the x coordinate for the object
		gp.slot[0].pokemony = 3 * gp.finalsize; //creating the y coordinate for the object
		
	}
}
