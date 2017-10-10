package rendering;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

// Source : http://www.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html   


/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.init() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */
public enum SoundEffect 
{
	Collid("Sound/puckHitPaddle.wav"),
	HitBorder("Sound/puckHitWall.wav"),
	HitGoal("Sound/goal.wav"),
	BackgroundMusic("Sound/music.wav");
   
	
   public static enum Volume { MUTE, LOW, MEDIUM, HIGH } // not working
   public static Volume volume = Volume.LOW;             // not working
   
   
   // Each sound effect has its own clip, loaded with its own sound file.
   private Clip clip;
   
   // Constructor to construct each element of the enum with its own sound file.
   SoundEffect(String soundFileName) 
   {
      try
      {
         // Use URL (instead of File) to read from disk and JAR.
         URL url = this.getClass().getClassLoader().getResource(soundFileName);
         // Set up an audio input stream piped from the sound file.
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
         // Get a clip resource.
         clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioInputStream);
      }
      catch (Exception e) {  }
   }
   
   // Play or Re-play the sound effect from the beginning.
   public void play() 
   {
      if (volume != Volume.MUTE) 
      {
         if (clip.isRunning())
            clip.stop();
         clip.setFramePosition(0);
         clip.start();
      }
   }
   
   public void playLoop() 
   {
      if (volume != Volume.MUTE)
      {
         if (clip.isRunning())
            clip.stop();
         clip.setFramePosition(0);
         clip.loop(Clip.LOOP_CONTINUOUSLY);
      }
   }
   public void stop() 
   {
         if (clip.isRunning())
            clip.stop();
         clip.setFramePosition(0);
   }
   
   public static void Mute()
   {
       volume = Volume.MUTE;
       SoundEffect.BackgroundMusic.stop();
   }
   
   public static void init() 
   {
      values();
   }
}