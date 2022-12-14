package com.twilightCarnival.controller;

import com.twilightCarnival.model.Directions;
import com.twilightCarnival.model.InputValidator;
import com.twilightCarnival.model.Music;
import com.twilightCarnival.model.SoundEffect;
import com.twilightCarnival.model.SoundEffect.Volume;
import java.util.Scanner;

public class StartGame {

  private final String CLOWN = "       ,            _..._            ,\n"
      + "      {'.         .'     '.         .'}\n"
      + "     { ~ '.      _|=    __|_      .'  ~}\n"
      + "    { ~  ~ '-._ (___________) _.-'~  ~  }\n"
      + "   {~  ~  ~   ~.'           '. ~    ~    }\n"
      + "  {  ~   ~  ~ /   /\\     /\\   \\   ~    ~  }\n"
      + "  {   ~   ~  /    __     __    \\ ~   ~    }\n"
      + "   {   ~  /\\/  -<( o)   ( o)>-  \\/\\ ~   ~}\n"
      + "    { ~   ;(      \\/ .-. \\/      );   ~ }\n"
      + "     { ~ ~\\_  ()  ^ (   ) ^  ()  _/ ~  }\n"
      + "      '-._~ \\   (`-._'-'_.-')   / ~_.-'\n"
      + "          '--\\   `'._'\"'_.'`   /--'\n"
      + "              \\     \\`-'/     /\n"
      + "               `\\    '-'    /'\n"
      + "                 `\\       /'\n"
      + "                   '-...-'\n"
      + "------------------------------------------------";
//      + "Thank you for visiting https://asciiart.website/\n"
//      + "This ASCII pic can be found at\n"
//      + "https://asciiart.website/index.php?art=people/occupations/clowns\n";


  private final String TITLE_CARD_1 = "\n \u001B[32m"
      + " ,ggggggggggggggg                                                             ,gggg,                                                                            \n"
      + "dP\"\"\"\"\"\"88\"\"\"\"\"\"\"               ,dPYb,                   ,dPYb,      I8     ,88\"\"\"Y8b,                                                                    ,dPYb,\n"
      + "Yb,_    88                      IP'`Yb                   IP'`Yb      I8    d8\"     `Y8                                                                    IP'`Yb\n"
      + " `\"\"    88                 gg   I8  8I  gg               I8  8I   88888888d8'   8b  d8                                      gg                            I8  8I\n"
      + "        88                 \"\"   I8  8'  \"\"               I8  8'      I8  ,8I    \"Y88P'                                      \"\"                            I8  8'\n"
      + "        88 gg    gg    gg  gg   I8 dP   gg     ,gggg,gg  I8 dPgg,    I8  I8'            ,gggg,gg   ,gggggg,   ,ggg,,ggg,    gg      ggg    gg   ,gggg,gg  I8 dP \n"
      + "        88 I8    I8    88bg88   I8dP    88    dP\"  \"Y8I  I8dP\" \"8I   I8  d8            dP\"  \"Y8I   dP\"\"\"\"8I  ,8\" \"8P\" \"8,   88     d8\"Yb   88bgdP\"  \"Y8I  I8dP  \n"
      + "  gg,   88 I8    I8    8I  88   I8P     88   i8'    ,8I  I8P    I8  ,I8, Y8,          i8'    ,8I  ,8'    8I  I8   8I   8I   88    dP  I8   8I i8'    ,8I  I8P   \n"
      + "   \"Yb,,8P,d8,  ,d8,  ,8I_,88,_,d8b,_ _,88,_,d8,   ,d8I ,d8     I8,,d88b,`Yba,,_____,,d8,   ,d8b,,dP     Y8,,dP   8I   Yb,_,88,_,dP   I8, ,8I,d8,   ,d8b,,d8b,_ \n"
      + "     \"Y8P'P\"\"Y88P\"\"Y88P\" 8P\"\"Y88P'\"Y888P\"\"Y8P\"Y8888P\"88888P     `Y88P\"\"Y8  `\"Y8888888P\"Y8888P\"`Y88P      `Y88P'   8I   `Y88P\"\"Y88\"     \"Y8P\" P\"Y8888P\"`Y88P'\"Y88\n"
      + "                                                   ,d8I'                                                                                                        \n"
      + "                                                 ,dP'8I                                                                                                         \n"
      + "                                                ,8\"  8I                                                                                                         \n"
      + "                                                I8   8I                                                                                                         \n"
      + "                                                `8, ,8I                                                                                                         \n"
      + "                                                 `Y8P\"                                                                                                          \n \u001B[0m";

  private final Game game;
  private final Music music;

  public StartGame() throws InterruptedException {
    game = new Game();
    music = new Music();
  }


  /**
   * start() starts the game, with splash screen and following method to begin the logic.
   */
  public void start() throws InterruptedException {
    String musicPath = "audio/Some-Dreamy-Place.wav";
    music.playMusic(musicPath);
    game.display();
    System.out.println("\nPress the \"Enter\" key to continue.");
    Scanner input = new Scanner(System.in);
    input.nextLine();
    System.out.print("\033[H\033[2J");
    System.out.flush();
    game.status();
    userInput();
  }


  /**
   * userInput is the main loop to handle user input from 'help', 'quit', and input commands like
   * 'go north'
   */
  private void userInput() throws InterruptedException {
    InputValidator validator = new InputValidator();
    String musicPath = "audio/Some-Dreamy-Place.wav";
    String[] validInput = new String[2];

    while (true) {
      Scanner input = new Scanner(System.in);
      String userChoice = input.nextLine();
      validator.generateCombatTools(game.getCurrentStation());
      System.out.println();
      if (userChoice.equals("")) {
        System.out.println(
            "> I don't think standing around doing anything will get me out of here.");
        System.out.println(
            "> Maybe I might need some " + "\u001B[32m" + "help" + "\u001B[0m" + ".");
      } else if (userChoice.equalsIgnoreCase("help")) {
        game.help();
      } else if (userChoice.equalsIgnoreCase("unlock")) {
        unlockAction();
      } else if (userChoice.equalsIgnoreCase("quit")) {
        game.quitFromStartedGame();
      } else if (userChoice.equalsIgnoreCase("mute")) {
        if (music.isMusicOn()) {
          SoundEffect.mute();
          music.muteMusic();
          music.setMusicOn(false);
        } else {
          System.out.println("Music is currently muted.");
        }

      } else if (userChoice.equalsIgnoreCase("unmute")) {
        music.playMusic(musicPath);
        SoundEffect.unmute();

        music.setMusicOn(true);
        System.out.println("Game unmuted. Please enter a command to continue.");
      } else if (userChoice.equalsIgnoreCase("lv")) {
        if (music.isMusicOn()) {
          music.volumeLow();
          System.out.println(
              "Low Volume. [Type 'hv',or 'mdv' to control volume]-  Please enter a command to continue.");
        } else {
          System.out.println("Music is currently muted.");
        }

      } else if (userChoice.equalsIgnoreCase("mdv")) {
        if (music.isMusicOn()) {
          music.volumeMedium();
          System.out.println(
              "Medium Volume. [Type 'hv',or 'lv' to control volume]-  Please enter a command to continue.");
        } else {
          System.out.println("Music is currently muted.");
        }

      } else if (userChoice.equalsIgnoreCase("hv")) {
        if (music.isMusicOn()) {
          music.volumeHigh();
          System.out.println(
              "High Volume. [Type 'lv',or 'mdv' to control volume]-  Please enter a command to continue.");
        } else {
          System.out.println("Music is currently muted.");
        }
      } else if (validator.isValid(userChoice)) {
        validInput = validator.getInput();
        operation(validInput[0], validInput[1]);
      }
//      System.out.println();
    }
  }

  /**
   * operation handles the logic of valid input and carries out desired action.
   *
   * @param verb is a filtered verb handled by InputValidator.isValid()
   * @param noun filtered noun handled by InputValidator.isValid()
   */
  private void operation(String verb, String noun)
      throws InterruptedException {
    if (verb.equals("open") && noun.equals("map")) {
      if (game.getPlayer().hasMap()) {
        game.viewMap();
      } else {
        System.out.println("> I do not have a map to open.");
      }
    } else if (verb.equals("pickup")) {
      game.getItem(noun);
      game.getPlayer().displayInventory();
    } else if (verb.equals("go")) {
      game.changingLocation(Directions.valueOf(noun));
    } else if (verb.equals("use")) {
      game.defeatMonsterOrLoseGame(noun);
      if (game.getResult()) {
        music.stopMusic();
        game.playAgain();
      }
    }
  }

  /**
   * unlockAction() handles the multiple ways the user will use the keyword "unlock" checking if
   * player has all keys, at-least one key, and has/has not visited the DreamLand Gate.
   */
  private void unlockAction() throws InterruptedException {
    boolean notAtGate = !game.getCurrentStation().getName().equalsIgnoreCase("Dreamland Gate");
    boolean visitedGate = game.hasBeenVisited("Dreamland Gate");
    if (game.getPlayer().getCurrentLocation().equalsIgnoreCase("Dreamland Gate")) {
      music.stopMusic();
      game.win();
    } else if (visitedGate && game.hasAllKeys() && notAtGate) {
      System.out.println("> It would be best if I used these keys at the Dreamland Gate.");
    } else if (visitedGate && !game.hasAllKeys() && game.hasAKey() && notAtGate) {
      System.out.println("> I think I might need more keys, and I am not at the gate.");
    } else if (visitedGate && !game.hasAllKeys() && game.hasAKey() && !notAtGate) {
      System.out.println("> I am going to need more keys. There are 4 locks on the door.");
    } else if (!visitedGate && game.hasAllKeys() && notAtGate) {
      System.out.println(
          "> I wonder what all these keys could unlock. Nothing where I am currently.");
    } else if (!visitedGate && !game.hasAllKeys() && game.hasAKey() && notAtGate) {
      System.out.println("> I do not see anything I could unlock in this area.");
    } else {
      System.out.println("> What am I trying to unlock with no keys.");
    }
  }

  public Game getGame() {
    return game;
  }

  public String getClown() {
    return CLOWN;
  }

  public String getTitleCard1() {
    return TITLE_CARD_1;
  }
}
