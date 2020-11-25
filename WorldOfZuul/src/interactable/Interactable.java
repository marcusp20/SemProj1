package interactable;

import game.CommandWords;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Interactable {
    private CommandWords commandWords;
    private ImageView image;

    public Interactable(CommandWords commandWords) {
        this.commandWords = commandWords;
        setDefaultImage();
    }

    public Interactable()   {   //interactable wo command words
        setDefaultImage();
    }

    private void setDefaultImage()   {
        try {
            Image img = loadImage("placeHolder.png");
            image = new ImageView(img);
            image.setX(Math.random()*1000);
            image.setY(Math.random()*800);
        } catch (FileNotFoundException e)   {
            System.out.println("placeHolder.png not found");
        }
    }

    public void addImage(ImageView imageView)  {
        this.image = imageView;
    }

    public ImageView getImageView() {
        return image;
    }

    private Image loadImage(String fileName) throws FileNotFoundException {
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
