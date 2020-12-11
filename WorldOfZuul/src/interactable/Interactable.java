package interactable;

import game.CommandWords;
import game.FileLoader;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Interactable {
    private CommandWords commandWords;

    //Gui
    private ImageView image;
    private ListView<String> commandList;

    public Interactable(CommandWords commandWords) {
        this.commandWords = commandWords;
        setDefaultImage();
    }

    public Interactable()   {   //interactable w.o command words
        setDefaultImage();
    }

     public String interact()   {
         return "No type";
    }

    private void setDefaultImage()   {
        try {
            Image img = FileLoader.loadImage("placeHolder.png");
            image = new ImageView(img);
            image.setX(500);
            image.setY(400);
        } catch (FileNotFoundException e)   {
            System.out.println("placeHolder.png not found");
        }
    }

    public void setImageView(ImageView imageView)  {
        this.image = imageView;
    }

    public ImageView getImageView() {
        return image;
    }

    public void setCommandList(ListView<String> commandList)    {
        this.commandList = commandList;
    }

    public ListView<String> getCommandList()    {
        return this.commandList;
    }
}
