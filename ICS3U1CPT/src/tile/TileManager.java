package tile;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
public class TileManager {
	//creating variables
	GamePanel gp;
	public Tile[] tile;
	public int map[][];
	// creating constructor
	public TileManager(GamePanel gp) {
		this.gp=gp;
		tile = new Tile[20]; //Initializing new tile array
		map = new int[gp.maxColsize][gp.maxRowsize]; //Initializing 2d array to put tiles in map
		getTileImage(); //running tile image method
		loadMap(); //runs map generator method
	}
	
	public void getTileImage() {
		//uploading images to the tile array
		try {
			tile[0] = new Tile(); //Initializing the tile array
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png")); //uploading tile pictures to the tile array through a buffered image
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
			tile[2].collision = true; //making this type of tile have collision so that players and entities cannot pass through the tile
			
			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
			tile[3].collision = true;
			
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
			
			tile[5] = new Tile();
			tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
			
			tile[6] = new Tile();
			tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/lab.png"));
			tile[6].collision = true;
			
			tile[7] = new Tile();
			tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/house1.png"));
			tile[7].collision = true;
			
			tile[8] = new Tile();
			tile[8].image = ImageIO.read(getClass().getResourceAsStream("/tiles/house2.png"));
			tile[8].collision = true;
			
			tile[9] = new Tile();
			tile[9].image = ImageIO.read(getClass().getResourceAsStream("/tiles/house3.png"));	
			tile[9].collision = true;
			
			tile[10] = new Tile();
			tile[10].image = ImageIO.read(getClass().getResourceAsStream("/tiles/gym.png"));	
			tile[10].collision = true;
			
			tile[11] = new Tile();
			tile[11].image = ImageIO.read(getClass().getResourceAsStream("/tiles/centre.png"));	
			tile[11].collision = true;
			
			tile[12] = new Tile();
			tile[12].image = ImageIO.read(getClass().getResourceAsStream("/tiles/starting.png"));	
			tile[12].collision = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap() {
		try {
			InputStream is = getClass().getResourceAsStream("/maps/CinderstoneTown.txt"); //getting text file from map package, using superclass of all classes to get the file form another package
			BufferedReader br = new BufferedReader(new InputStreamReader(is)); //Initializing bufferedreader that inputs from the text file
			//Variables to check boundaries
			int col = 0;
			int row = 0;
			//while loop to check boundaries of map
			while(col < gp.maxColsize && row < gp.maxRowsize) {
			String line = br.readLine();
			//Running loop to print each tile in a column
				while (col < gp.maxColsize) {
					String[] nums = line.split(" "); //splits the array into different indexes based on the conditions in the brackets, in this case, a space
					int num = Integer.parseInt(nums[col]); //converting the string into an integer
					map[col][row] = num; //taking the number into the 2d array
					col++; //adding to the column counter to make sure we don't create tiles outside the screen/map
				}
				// checking if the max column value as been hit
				if (col == gp.maxColsize) {
					col = 0; //resets the column value
					row++; //goes into the next row since the previous row has been filled
				}
			}
			br.close(); // closes the bufferedreader and its inputs to prevent error
		} catch (Exception e) {
			
		}
	}
	//printing the graphic
	public void draw(Graphics2D g2) {
		//Variables to check for parameters
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		//loop to move through the entire map
		while ( col < gp.maxColsize && row < gp.maxRowsize ) {
			g2.drawImage(tile[map[col][row]].image, x, y, gp.finalsize, gp.finalsize, null); //printing the wall
			col++; //adding to the column so the tiles do not go out of frame and printing the tile one down
			x+=gp.finalsize;//updating the position of the tile through its width within the screen
			//checking if the tiles has hit the bottom of the screen
			if (col == gp.maxColsize) {
				//resetting columns and add 1 to row to move to the next row
				col = 0;
				x = 0;
				row++;
				y += gp.finalsize;//updating the position of the tile through its length within the screen
			}
		}
	}
}
