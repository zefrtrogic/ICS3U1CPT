package pokemon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
//declaring variables to be used in the pokemon placement process
public class masterpokemon {
	public BufferedImage pokemon;
	public String name;
	public boolean collision = false;
	public int pokemonx, pokemony;
	//draw method  that will display the object
	public void draw(Graphics2D g2, GamePanel gp) {
		int x = 100;
		int y = 100;
		g2.drawImage(pokemon, x, y, gp.finalsize, gp.finalsize, null); //printing the pokemon at the right position based on x and y coordinate that fills the tile
	}
	}
