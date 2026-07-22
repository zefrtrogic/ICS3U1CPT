package object;

import java.io.IOException;
import javax.imageio.ImageIO;
//The key item the player collects; loads its own image and sets its own name
public class OBJ_Key extends SuperObject {

	public OBJ_Key() {
		name = "Key"; //used later to check which object type was picked up
		try {
			//loading the key image from res/objects/key.png through the classpath
			image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}