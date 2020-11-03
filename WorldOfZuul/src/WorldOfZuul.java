import java.io.File;

/**
 * Class containing the main method, to launch the WorldOfZuul game.
 *
 * @author marcus pedersen
 * @version 23-09-2020
 */
public class WorldOfZuul {

    public static void main(String[] args) {

        File mayorBobDialog = new File("C:\\Users\\olive\\Documents\\Programming\\Java\\SemProj1\\WorldOfZuul\\src\\majorBobDialog.txt");
        NPC mayorBob = new NPC(mayorBobDialog);

        mayorBob.converse();
        //mayorBob.test();
        /*
        Game game = new Game();
        game.play();
         */
    }
}

