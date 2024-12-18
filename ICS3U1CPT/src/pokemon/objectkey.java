package pokemon;

import java.io.IOException;

import javax.imageio.ImageIO;

//this class is used to upload pokemon and is a subclass of the master class
public class objectkey extends masterpokemon{
	// constructor
	public objectkey() {
		name = "key";
		//loading image with try-catch statement
		try {
			pokemon = ImageIO.read(getClass().getResourceAsStream("/pokemon/charizard.png")); //finding image location and loading image
		} catch(IOException e) {
			//error message if error occurs
			e.printStackTrace();
		}
	}
}
