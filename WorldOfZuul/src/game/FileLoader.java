package game;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileLoader {

    /**
     * @param fileName name of the text file (including the .txt)
     * @return the file path of the given fileName
     */
    public static File loadFile(String fileName) {
        String path = System.getProperty("user.dir");
        if (path.endsWith("SemProj1")) {
            return new File(path + "\\WorldOfZuul\\src\\dialog\\" + fileName);    //Add remaining path to dialog text file
        } else if (path.endsWith("WorldOfZuul")) {
            return new File(path + "\\src\\dialog\\" + fileName);
        }
        //Default - probably not gonna work
        return new File(path + "\\dialog\\" + fileName);

    }

    public static Image loadImage(String fileName) throws FileNotFoundException {
        String path = System.getProperty("user.dir");
        if (path.endsWith("SemProj1")) {
            return new Image(new FileInputStream(path + "\\WorldOfZuul\\src\\resources\\img\\" + fileName));    //Add remaining path to dialog text file
        } else if (path.endsWith("WorldOfZuul")) {
            return new Image(new FileInputStream(path + "\\src\\resources\\img\\" + fileName));
        }
        //Default - probably not gonna work
        return new Image(new FileInputStream(path + "\\img\\" + fileName));
    }
}
