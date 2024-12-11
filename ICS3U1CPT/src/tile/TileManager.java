package tile;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	GamePanel gp;
	Tile[] tile;
	// creating constructor
	public TileManager(GamePanel gp) {
		this.gp=gp;
		tile = new Tile[10];
		getTileImage();
	}
	
	public void getTileImage() {
		//uploading images to the tile array
		try {
			tile[0] = new Tile(); //Initializing the tile array
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png")); //uploading tile pictures to the tile array through a buffered image through tile conversion
			
			tile[1] = new Tile(); //Initializing the tile array
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//printing the graphic
	public void draw(Graphics2D g2) {
		//creating an automation system so I don't have to write 200000 lines of hardcode
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		//while loop to fill the row
		while ( col < gp.maxColsize && row < gp.maxRowsize ) {
			g2.drawImage(tile[1].image , x, y, gp.finalsize, gp.finalsize, null); //printing the wall
			col++; //adding to the column so the tiles do not go out of frame and printing the tile one down
			x+=gp.finalsize;//??
			//checking if the tiles has hit the bottom of the screen
			if (col == gp.maxColsize) {
				//resetting columns and add 1 to row to move to the next row
				col = 0;
				x = 0;
				row++;
				y += gp.finalsize;
			}
		}
	}
}
