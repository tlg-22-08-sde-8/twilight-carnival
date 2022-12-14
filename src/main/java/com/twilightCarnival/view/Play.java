package com.twilightCarnival.view;

import com.twilightCarnival.controller.StartGame;
import com.twilightCarnival.model.Music;
import java.util.Scanner;
public class Play {

  public static void main(String[] args) throws InterruptedException {
    String musicPath = "audio/introMusic.wav";
    Music music = new Music();
    music.playMusic(musicPath);
    music.volumeLow();
    StartGame startGame = new StartGame();
    System.out.println(startGame.getTitleCard1());
    System.out.println(startGame.getClown());
    boolean condition = false;
    do {
      System.out.println("Do you want to start a new game?(y/n)");
      Scanner scanner = new Scanner(System.in);
      String input = scanner.nextLine();
      if (input.equalsIgnoreCase("y")) {
        music.stopMusic();
        startGame.start();
      } else if (input.equalsIgnoreCase("n")) {
        if (!startGame.getGame().quit()) {
          music.stopMusic();
          startGame.start();
        }
      } else {
        System.out.println("It is not a valid input. Please use 'y' or 'n'.");
        condition = true;
      }
    } while (condition);

  }
}
