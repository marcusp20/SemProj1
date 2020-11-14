import java.io.File;
import java.util.Scanner;

/**
 * Class containing the main method, to launch the WorldOfZuul game.
 *
 * @author marcus pedersen
 * @version 23-09-2020
 */
public class SustainableFarmer {
    private static File saveFile;

    public static void main(String[] args) {

        Game game;

        //choose newGame or loadGame
        saveFile = new File(System.getProperty("user.dir") + "\\saveFile.txt");
        boolean saveFileExists = saveFile.exists();
        if(saveFileExists && chooseLoadGame()) {
                game = GameLogger.loadGameFrom(saveFile);
        } else {
            game = new Game();
        }
        game.play();

    }

    private static boolean chooseLoadGame() {
        while (true) {
            System.out.println("Type 'new' or 'load', to start a new game or load from a save file");
            Scanner reader = new Scanner(System.in);
            String inputLine = reader.nextLine();
            if (inputLine.equals("load")) {
                return true;
            }
            if (inputLine.equals("new")) {
                return false;
            }
        }
    }
}

