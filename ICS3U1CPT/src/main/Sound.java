package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//Handles loading and playing .wav files from res/audio.
//One Sound instance can only hold/play one clip at a time - GamePanel keeps a separate
//instance for background music vs one-shot sound effects so they don't interrupt each other.
public class Sound {
	Clip clip;
	URL soundURL[] = new URL[5]; //index matches the constants defined in GamePanel (SOUND_BACKGROUND, SOUND_KEY, etc)

	public Sound() {
		soundURL[0] = getClass().getResource("/audio/background.wav");
		soundURL[1] = getClass().getResource("/audio/key.wav");
		soundURL[2] = getClass().getResource("/audio/start.wav");
		soundURL[3] = getClass().getResource("/audio/end.wav");
		soundURL[4] = getClass().getResource("/audio/boost.wav");
	}

	//loads the given sound file into this Sound's clip, ready to play() or loop()
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//plays the loaded clip once, from the beginning
	public void play() {
		if (clip != null) {
			clip.setFramePosition(0); //rewinding in case this clip was played before
			clip.start();
		}
	}

	//plays the loaded clip on a continuous loop (used for background music)
	public void loop() {
		if (clip != null) {
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	//stops whatever this clip is currently doing (used to cut off background music)
	public void stop() {
		if (clip != null) {
			clip.stop();
		}
	}
}