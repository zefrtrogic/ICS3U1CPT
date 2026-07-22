package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.net.URL;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setTitle("Pokemon");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		URL logoURL = Main.class.getResource("/logo/pokemon.png");
		if (logoURL == null) {
			throw new IllegalStateException("Cannot find logo resource: /logo/pokemon.png");
		}

		ImageIcon logo = new ImageIcon(logoURL);
		GamePanel gamepanel = new GamePanel();
		window.add(gamepanel);
		window.pack();
		window.setIconImage(logo.getImage());
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		gamepanel.startGameThread();
	}
}