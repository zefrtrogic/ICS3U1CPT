package main;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.net.URL;
import java.io.*;

public class Main {
	public static void main(String[] args) throws IOException {
		JFrame window = new JFrame();
		window.setTitle("Pokemon");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Load the logo through the classpath instead of a raw file path
		URL logoURL = Main.class.getResource("/logo/pokemon.png");
		if (logoURL == null) {
			System.out.println("Logo not found! Check that res is marked as a source/resources folder.");
		} else {
			ImageIcon logo = new ImageIcon(logoURL);
			window.setIconImage(logo.getImage());
		}

		GamePanel gamepanel = new GamePanel();
		window.add(gamepanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		gamepanel.startGameThread();
	}
}