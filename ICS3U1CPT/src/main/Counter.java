package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
//Handles drawing on-screen UI elements (the key counter for now, more can be added later)
public class Counter {
	GamePanel gp;
	Font font;
	BufferedImage keyImage; //small icon shown next to the counter, separate from the in-world key objects

	public Counter(GamePanel gp) {
		this.gp = gp;
		font = new Font("Arial", Font.BOLD, 20);
		try {
			keyImage = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g2) {
		g2.setFont(font);

		int iconSize = 32;
		int margin = 10;
		int iconX = gp.screenWidth - iconSize - margin; //top right corner, offset by the margin
		int iconY = margin;

		if (keyImage != null) {
			g2.drawImage(keyImage, iconX, iconY, iconSize, iconSize, null);
		}

		String text = "x " + gp.keyCount;
		int textX = iconX - 45; //placing the count just to the left of the icon
		int textY = iconY + 24;

		//black outline so the number stays readable over any background, same trick as the FPS counter
		g2.setColor(Color.black);
		g2.drawString(text, textX - 1, textY);
		g2.drawString(text, textX + 1, textY);
		g2.drawString(text, textX, textY - 1);
		g2.drawString(text, textX, textY + 1);

		g2.setColor(Color.white);
		g2.drawString(text, textX, textY);
	}
}