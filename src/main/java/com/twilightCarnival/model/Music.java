package com.twilightCarnival.model;

import java.net.URL;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class Music {

  private Clip musicClip;
  private boolean musicOn = true;


  public void playMusic(String musicLocation) {
    try {
      URL url = Music.class.getClassLoader().getResource(musicLocation);
      assert url != null;
      AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
      musicClip = AudioSystem.getClip();
      musicClip.open(audioInput);
      volumeMedium();
      musicClip.start();
      musicClip.loop(Clip.LOOP_CONTINUOUSLY);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }


  public void muteMusic() {
    System.out.println("Do you really want to mute the game?(y/n)");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")) {
      System.out.println("Game muted. Please enter a command.");
      musicClip.stop();
      musicClip.close();
    } else if (input.equalsIgnoreCase("n")) {
      System.out.println("Please enter a command to continue");
    } else {
      System.out.println("That is not valid input. Please type y or n.");
      muteMusic();
    }
  }

  public void stopMusic() {
    musicClip.stop();
    musicClip.close();
  }

  public void volumeHigh() {
    FloatControl gainControl =
        (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(6.0f);
  }

  public void volumeMedium() {
    FloatControl gainControl =
        (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-6.0f);
  }

  public void volumeLow() {
    FloatControl gainControl =
        (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-20.0f);
  }


  public boolean isMusicOn() {
    return musicOn;
  }

  public void setMusicOn(boolean musicOn) {
    this.musicOn = musicOn;
  }
}
