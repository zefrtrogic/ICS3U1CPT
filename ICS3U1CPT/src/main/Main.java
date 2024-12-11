package main;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.io.*;
public class Main {
	public static void main(String[] args) throws IOException {
		//Creating the Window
		JFrame window = new JFrame(); //creating window
		window.setTitle("Pokemon"); //naming game
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes when the user closes the program
		ImageIcon logo = new ImageIcon("pokemon.png"); //making variable to store logo
		//Finding Dimensions and scaling
		GamePanel gamepanel = new GamePanel(); //Running gamepanel class to get dimension of the screen
		window.add(gamepanel); //adding the dimension of the gamepanel to the JFrame
		window.pack(); //fits the screen to the preffered size of the window
		//Creating Logo
		window.setIconImage(logo.getImage()); //Displaying logo
		window.setLocationRelativeTo(null); //Displaying window at center of screen
		window.setVisible(true); //allows user to see the window
		//Calling and stating the game from the gamepanel class and startgamethread method
		gamepanel.startGameThread();
		
		
		
		
	}
}
