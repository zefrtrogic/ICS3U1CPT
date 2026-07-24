package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
//Handles drawing on-screen UI elements (the key counter for now, more can be added later)
public class UI {
	GamePanel gp;
	BufferedImage keyImage; //small icon shown next to the counter, separate from the in-world key objects

	public UI(GamePanel gp) {
		this.gp = gp;
		try {
			keyImage = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g2) {
		g2.setFont(gp.gameFont.deriveFont(20f)); //bigger so the key count is easy to read at a glance

		int iconSize = 32;
		int margin = 10;
		int iconX = gp.screenWidth - iconSize - margin; //top right corner, offset by the margin
		int iconY = margin;

		if (keyImage != null) {
			g2.drawImage(keyImage, iconX, iconY, iconSize, iconSize, null);
		}

		String text = "x " + gp.keyCount;
		int textWidth = g2.getFontMetrics().stringWidth(text); //measuring so the bigger text still lines up cleanly next to the icon
		int textX = iconX - textWidth - 8;
		int textY = iconY + 24;

		//black outline so the number stays readable over any background, same trick as the FPS counter
		g2.setColor(Color.black);
		g2.drawString(text, textX - 1, textY);
		g2.drawString(text, textX + 1, textY);
		g2.drawString(text, textX, textY - 1);
		g2.drawString(text, textX, textY + 1);

		g2.setColor(Color.white);
		g2.drawString(text, textX, textY);

		drawBoostCounter(g2);
		drawCoordinates(g2);
	}

	//shows how many speed boosts are left, bottom-left corner; turns yellow while a boost is currently active
	private void drawBoostCounter(Graphics2D g2) {
		g2.setFont(gp.gameFont.deriveFont(14f)); //resetting the size back down, since draw() just left it bigger for the key count
		String text = "Boosts: " + gp.boostsRemaining + "/" + gp.maxBoosts;
		int x = 10;
		int y = gp.screenHeight - 15; //small margin up from the bottom edge

		g2.setColor(Color.black);
		g2.drawString(text, x - 1, y);
		g2.drawString(text, x + 1, y);
		g2.drawString(text, x, y - 1);
		g2.drawString(text, x, y + 1);

		g2.setColor(gp.boostActive ? Color.yellow : Color.white);
		g2.drawString(text, x, y);
	}

	//shows the player's position on the map as a tile column/row (a simple grid coordinate system), bottom-right corner
	private void drawCoordinates(Graphics2D g2) {
		g2.setFont(gp.gameFont.deriveFont(14f)); //resetting the size back down, same reason as drawBoostCounter
		int col = gp.player.x / gp.finalsize; //converting the player's pixel position into a tile grid position
		int row = gp.player.y / gp.finalsize;
		String text = "(" + col + ", " + row + ")";

		int textWidth = g2.getFontMetrics().stringWidth(text);
		int x = gp.screenWidth - textWidth - 10; //right-aligned with a small margin
		int y = gp.screenHeight - 15; //same baseline as the boost counter, opposite corner

		g2.setColor(Color.black);
		g2.drawString(text, x - 1, y);
		g2.drawString(text, x + 1, y);
		g2.drawString(text, x, y - 1);
		g2.drawString(text, x, y + 1);

		g2.setColor(Color.white);
		g2.drawString(text, x, y);
	}
}