package com.twilightCarnival.controller;

import com.twilightCarnival.model.Directions;
import com.twilightCarnival.model.Music;
import com.twilightCarnival.model.Player;
import com.twilightCarnival.model.Script;
import com.twilightCarnival.model.SetMap;
import com.twilightCarnival.model.SoundEffect;
import com.twilightCarnival.model.SoundEffect.Volume;
import com.twilightCarnival.model.Station;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;


public class Game {

  private static final String RED_COLOR = "\u001B[31m";
  private static final String GREEN_COLOR = "\u001B[32m";
  private static final String CLEAR_COLOR = "\u001B[0m";

  private Player player;
  private Script script;
  private SetMap map;
  private List<Station> stations;
  private Stack<String> stationsVisited = new Stack<>();
  private final SoundEffect soundEffect = SoundEffect.SOUNDEFFECT;

  private Music music;
  private boolean result;

  public Game() {

    music = new Music();
    player = new Player();
    script = new Script();
    script.load();
    map = new SetMap();
    map.load();
    for (Station s : map.getStations()) {
      s.setMonster(s.getVillain());
    }
    stations = map.getStations();
    result = false;

  }

  public void playAgain() throws InterruptedException {
    System.out.println(getTryAgainMessage());
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")) {
      soundEffect.stop();
      StartGame startGame = new StartGame();
      startGame.start();
    } else if (input.equalsIgnoreCase("n")) {
      if (quit()) {
        System.exit(0);
      } else {
        playAgain();
      }
    } else {
      System.out.println("That is not valid input. Please type y or n.");
      playAgain();
    }
  }

  /**
   * when the user type "quit", it quits the game
   */
  public void quitFromStartedGame() {
    System.out.println("Do you really want to quit the game?(y/n)");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")) {
      System.out.println("Thank you! Have a great day!\n");
      System.exit(0);
    } else if (input.equalsIgnoreCase("n")) {
      System.out.println("Thanks for staying with us! Please enter a command to continue");
    } else {
      System.out.println("That is not valid input. Please type y or n.\n");
      quitFromStartedGame();
    }
  }

  public boolean quit() {
    boolean result = false;
    System.out.println("Are you sure?(y/n)");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")) {
      System.out.println("Thank you! Have a great day!\n");
      result = true;
    } else if (input.equalsIgnoreCase("n")) {
      System.out.println("You can start the game.\n");
      result = false;
    } else {
      System.out.println("That is not valid input. Please type y or n.\n");
      quit();
    }
    return result;
  }

  public void getItem(String item) {

    for (Station s : stations) {
      if (s.getName().equals(player.getCurrentLocation()) && s.getItem() != null && s.getItem()
          .equals(item)) {
        System.out.printf("> I pick up the %s and put it in my pockets.\n", item);
        if (SoundEffect.volume.equals(Volume.OFF)) {
          soundEffect.stop();
        }
        if (SoundEffect.volume.equals(Volume.ON)) {
          soundEffect.play("audio/pickupFX.wav");
        }
        player.setInventory(s.getItem());
        s.setItem(null);
        return;
      } else if (s.getName().equals(player.getCurrentLocation()) && s.getItem() == null) {
        System.out.println("> There is not anything I really want to pickup.");
      }
    }
  }

  public void status() {
    System.out.println(
        "=============================================================================================");
    System.out.println("Location:" + player.getCurrentLocation() + "\t Tokens: " + player.getToken()
        + "\tInventory: [" + player.displayInventory() + "]");
    System.out.println(
        "=============================================================================================");
    System.out.println("Available commands: go [direction], use [item], help, quit");
    System.out.println(
        "=============================================================================================");
    System.out.println(getCurrentStation().getLocationDescription());
    System.out.println(
        "=============================================================================================");
    if (getCurrentStation().getName().equalsIgnoreCase("Dreamland Gate")){
      System.out.println("> If I get all 4 keys I could " + GREEN_COLOR + "unlock" + CLEAR_COLOR + " this gate.");
    }
    for (Station s : stations) {
      if (s.getName().equals(player.getCurrentLocation())
          && getPlayer().hasMap()) { // TODO: 12/19/2022 remove this and only prompt on pickup?
        System.out.println("> I am carrying a map.");
        System.out.println("> I can view the map anytime.\n");
      }
      if (s.getName().equals(player.getCurrentLocation()) && s.getItem() != null && !s.getItem()
          .equalsIgnoreCase("NULL")) {
        if (s.getName().equalsIgnoreCase("Hot Dog Stand")) {
          System.out.println("> I see something strange in the hot dog water.");
          System.out.println("> There is a " + s.getItem() + " within the hot dog water.");
          System.out.println("> This " + s.getItem() + " might be something I want to pickup.\n");
        } else {
          System.out.println("> There is a " + s.getItem() + ".");
          System.out.println("> This " + s.getItem() + " might be something I want to pickup.\n");
        }
      }
      if (s.getName().equals(player.getCurrentLocation()) && s.getMonster().getName() != null
          && s.getMonster().isAlive()) {
        System.out.println("> There is a " + s.getMonster().getName() + "!");
        System.out.println("> I see some items in the area.\n"
            + "> I could use some of them on the " + s.getMonster().getName() + ".\n");
        s.displayTools();
      }
    }
    System.out.println(
        "=============================================================================================");
  }

  /**
   * viewMap() will display the locations adjacent to the player's current location. It will color
   * red if not visited before and green if previously visited.
   */
  public void viewMap() throws InterruptedException {
    if (player.hasMap()) {
      if (SoundEffect.volume.equals(Volume.OFF)) {
        soundEffect.stop();
      } else {
        SoundEffect.volume.equals(Volume.ON);
        soundEffect.play("audio/openMap.wav");
      }
      System.out.println("> I look at my map and see my current surroundings.\n"
          + "> I marked locations" + GREEN_COLOR + " green" + CLEAR_COLOR
          + " for areas I visited, and "
          + RED_COLOR + "red" + CLEAR_COLOR + " for places I have not.");
      for (Station s : stations) {
        if (s.getName().equals(player.getCurrentLocation())) {
          for (Directions direction : s.getSurroundings().keySet()) {
            String adjacentLocation = s.getSurroundings().get(direction);
            if (hasBeenVisited(adjacentLocation)) {
              System.out.println(direction + ": " + GREEN_COLOR + adjacentLocation + CLEAR_COLOR);
            } else {
              System.out.println(direction + ": " + RED_COLOR + adjacentLocation + CLEAR_COLOR);

            }
          }
        }
      }
    } else {
      System.out.println("> I don't know why I tried to use a map, when I don't have one.\n");
    }
  }

  public void help() {
    System.out.println(getHelpMessage());

  }

  public void display() {
    System.out.println(getIntroduction());

  }

  public boolean isMonsterDefeated(String noun) {
    for (Station s : stations) {
      if (s.getName().equals(player.getCurrentLocation()) && s.getMonster() != null) {
        if (noun.equals(s.getMonster().getWeakness())) {
          return true;
        }

      }
    }
    return false;
  }

  public void defeatMonsterOrLoseGame(String noun) throws InterruptedException {
    if (isMonsterDefeated(noun)) {
      for (Station s : stations) {
        if (s.getName().equals(player.getCurrentLocation()) && s.getMonster() != null && s.getMonster().isAlive()) {
          if (SoundEffect.volume.equals(Volume.OFF)) {
            soundEffect.stop();
          } else {
            SoundEffect.volume.equals(Volume.ON);
            soundEffect.play("audio/earnAKey.wav");
          }
          s.getMonster().setStatus(false);
          player.setInventory(s.getMonster().getKey());
          System.out.println(s.getMonster().getWinMessage());
          System.out.println("> That " + s.getMonster().getKey() + " looks important.");
          System.out.println("> I'll put it in my pocket for safe keeping.\n");
          s.getMonster().setKey(null);
          return;

        } else if (!s.getMonster().isAlive()) {
          System.out.println(
              "> There is no longer a monster here.\n> I should go explore other areas.");
        }
      }

    } else {
      for (Station s : stations) {
        if (s.getName().equals(player.getCurrentLocation()) && !s.getMonster().isAlive()) {
          System.out.println(
              "> There is no longer a monster here.\n> I should go explore other areas.");
          return;
        }
      }
      if (player.getToken() > 0) {

        if (SoundEffect.volume.equals(Volume.OFF)) {
          soundEffect.stop();
        } else {
          SoundEffect.volume.equals(Volume.ON);
          soundEffect.play("audio/enterMonsterRoom.wav");
        }

        String monster = getCurrentStation().getMonster().getName();
        System.out.println("> The " + monster + " was not pleased by my action.");
        System.out.println("> The monster reaches out and demands a token.");
        System.out.println("> I currently have " + player.getToken() + " tokens.");
        boolean condition = false;
        do {
          System.out.println("> Do I want to give 1 token to appease this monster?(y/n)");
          Scanner scanner = new Scanner(System.in);
          String input = scanner.nextLine();
          if (input.equalsIgnoreCase("y")) {
            System.out.println("> I give the " + monster + " a token.");
            player.setToken(player.getToken() - 1);
            condition = false;
          } else if (input.equalsIgnoreCase("n")) {
            System.out.println("> I decline to give a token.");
            System.out.println("> Maybe I should visit other areas.");
            condition = false;
          } else {
            System.out.println("> It seems like I need to make a decision.(y/n)");
            condition = true;
          }
        } while (condition);
      } else {
        for (Station s : stations) {
          if (SoundEffect.volume.equals(Volume.OFF)) {
            soundEffect.stop();
          }
          if (SoundEffect.volume.equals(Volume.ON)) {
            soundEffect.play("audio/losingGame.wav");
          }
          if (s.getName().equals(player.getCurrentLocation())) {
            System.out.println(s.getMonster().getLostMessage());
            result = true;

            return;
          }
        }
      }
    }
  }

  public void win() throws InterruptedException {
    if (player.getCurrentLocation().equalsIgnoreCase("Dreamland Gate")) {
      if (hasAllKeys()) {
        System.out.println("unlocking gate...");
        if (SoundEffect.volume.equals(Volume.OFF)) {
          soundEffect.stop();
        } else {
          SoundEffect.volume.equals(Volume.ON);
          soundEffect.play("audio/door-unlocking-with-keys.wav");
        }
        TimeUnit.SECONDS.sleep(7);
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if (SoundEffect.volume.equals(Volume.OFF)) {
          soundEffect.stop();
        }
        if (SoundEffect.volume.equals(Volume.ON)) {
          soundEffect.play("audio/winning.wav");
        }
        System.out.println(getWinMessage());
        TimeUnit.SECONDS.sleep(10);
        playAgain();
      }

    }
    System.out.println("> I don't have enough keys to escape."
        + "\n> There are a total of four locks on the gate."
        + "\n> Perhaps the monsters have keys or there is one somewhere to be found.");


  }

  /**
   * hasAllKeys is used to check if the player has all keys. for win condition and the use of
   * "unlock" command and not at the final gate.
   *
   * @return true if all keys are in inventory.
   */
  public boolean hasAllKeys() {
    boolean results = false;
    boolean hasMasterKey = player.getInventory().contains("master key");
    boolean hasGoldKey = player.getInventory().contains("gold key");
    boolean hasSilverKey = player.getInventory().contains("silver key");
    boolean hasBronzeKey = player.getInventory().contains("bronze key");

    if (hasBronzeKey && hasGoldKey && hasMasterKey && hasSilverKey) {
      results = true;
    }

    return results;
  }

  /**
   * hasAKey is used to check if the player has at-least one key.
   *
   * @return true if there is one key in inventory.
   */
  public boolean hasAKey() {
    boolean results = false;
    boolean hasMasterKey = player.getInventory().contains("master key");
    boolean hasGoldKey = player.getInventory().contains("gold key");
    boolean hasSilverKey = player.getInventory().contains("silver key");
    boolean hasBronzeKey = player.getInventory().contains("bronze key");

    if (hasBronzeKey || hasGoldKey || hasMasterKey || hasSilverKey) {
      results = true;
    }

    return results;
  }

  /**
   * changingLocations will change the player's current location to chosen valid location.
   * stationsVisited is tracked here through trackLocation() method call. Needs to be called before
   * setting new location.
   *
   * @param direction the user wants to move.
   */
  public void changingLocation(Directions direction) {
    for (Station s : stations) {
      if (s.getName().equals(player.getCurrentLocation())) {
        if (s.getSurroundings().containsKey(direction)) {
          trackLocation();
          player.setCurrentLocation(s.getSurroundings().get(direction));
          System.out.print("\033[H\033[2J");
          System.out.flush();
          status();
          return;
        } else {
          System.out.println(s.getUnreachableDirectionMessage());
        }

      }
    }
  }


  /**
   * Method tracks the current player and adds it to the stationsVisited.
   */
  private void trackLocation() {
    if (!hasBeenVisited(player.getCurrentLocation())) {
      stationsVisited.push(player.getCurrentLocation().toLowerCase());
    }
  }

  /**
   * hasBeenVisited() will check if the location has been tracked in stationsVisited.
   *
   * @param location to searched in tracked locations.
   * @return true if visited, otherwise false.
   */
  public boolean hasBeenVisited(String location) {
    return stationsVisited.contains(location.toLowerCase());
  }

  public Player getPlayer() {
    return player;

  }


  /**
   * getCurrentStation gets the current station the player is located in. we could always track it
   * in a field, but this was created in absence of that.
   *
   * @return current station.
   */
  public Station getCurrentStation() {
    Station currentStation = null;
    for (Station station : stations) {
      if (station.getName().equalsIgnoreCase(player.getCurrentLocation())) {
        currentStation = station;
      }
    }
    return currentStation;
  }

  public String getHelpMessage() {
    return script.getScript().getHelp();
  }

  public String getTryAgainMessage() {
    return script.getScript().getPlayAgainMessage();
  }

  public String getIntroduction() {
    return script.getScript().getIntroduction();
  }

  public String getWinMessage() {
    return script.getScript().getWinMessage();
  }

  public boolean getResult() {
    return result;
  }
}


