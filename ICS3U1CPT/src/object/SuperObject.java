package object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
//Base class for pickup items and other placeable world objects.
//Later objects (potions, pokeballs, etc) can extend this the same way OBJ_Key does.
public class SuperObject {

	public BufferedImage image; //the picture drawn for this object
	public String name; //used to identify which object was picked up (e.g. "Key")
	public boolean collision = false; //true if the player should be blocked instead of picking it up
	public int worldX, worldY; //the object's position in world pixel coordinates
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48); //pickup/collision box, matches one tile (finalsize)
}