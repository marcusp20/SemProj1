package GUI;

import game.Game;
import game.GameLogger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/*
    Learnt from https://www.youtube.com/watch?v=FVo1fm52hz0
 */

public class Main extends Application {

    //Create structure
    Scene scene;
    Pane sceneHeadquarters = new Pane();
    Pane sceneField = new Pane();

    private Game game;
    private static File saveFile;

    //Create playerSprite
    private ImageView playerSprite;

    private double t;

    //Movement keys
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean w;

    private int room = 1;


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        //TODO Init main menu
        //TODO call launchNewGame or launchLoadGame based on user choice.

        //Create objects
        createPlayer();
        //createFrameRateLabel();
        //createRootNodes();

        launchNewGame();

        //Set scene
        Pane p = game.getCurrentRoom().getRoomPane();
        p.getChildren().add(playerSprite);
        scene = new Scene(p);

        startTimer();
        //createContent();

        //Call checkInput on keyPress/release
        scene.setOnKeyPressed(this::checkInput);
        scene.setOnKeyReleased(this::checkInput);

        //Set stage (window) tile and scene (scene includes root)
        stage.setTitle("FARMVILL 99 RETARDO EDITION");
        stage.setScene(scene);
        stage.show();
    }

    private void startTimer()    {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
    }
    private void launchNewGame() {
        game = new Game(true);
        game.playGUI();
    }

    private void launchLoadGame() {
        saveFile = new File(System.getProperty("user.dir") + "\\saveFile.txt");
        boolean saveFileExists = saveFile.exists();
        if(saveFileExists) {
            game = GameLogger.loadGameFrom(saveFile, true);
            game.playGUI();
        } else {
            System.err.println("Could not load saveFile");
            launchNewGame();
        }
    }

    //Main loop content
    private void update()   {
        //t represents current game time
        t += 0.016;

        //Moves player based on movement keys
        move();
    }

    /* Keeping increase code is good
    private void create1stScene()   {
        sceneHeadquarters.getChildren().add(playerSprite);
        scene.setRoot(sceneHeadquarters);
        playerSprite.setX(sceneHeadquarters.getWidth()-75);
    }
    */

    //Check pressed key and react accordingly
    private void checkInput(KeyEvent e) {
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            if (e.getCode() == KeyCode.W) {
                w = true;
            }
            if (e.getCode() == KeyCode.A) {
                a = true;
            }
            if (e.getCode() == KeyCode.S) {
                s = true;
            }
            if (e.getCode() == KeyCode.D) {
                d = true;
            }
        }

        if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            if (e.getCode() == KeyCode.W) {
                w = false;
            }
            if (e.getCode() == KeyCode.A) {
                a = false;
            }
            if (e.getCode() == KeyCode.S) {
                s = false;
            }
            if (e.getCode() == KeyCode.D) {
                d = false;
            }
        }
    }

    //Move player
    public void move() {
        int speed = 8;
        if(w) {
            playerSprite.setY(playerSprite.getY() - speed);
        }
        if(a)  {
            playerSprite.setX(playerSprite.getX() - speed);
        }
        if(s)   {
            playerSprite.setY(playerSprite.getY() + speed);
        }
        if(d)   {
            playerSprite.setX(playerSprite.getX() + speed);
        }
    }

    private Image load(String fileName) throws FileNotFoundException {
        String path = System.getProperty("user.dir");
        System.out.println("Path: " + path);
        System.out.println("Should be: " + "C:\\Users\\Marcus\\IdeaProjects\\OOP\\SemProj1\\WorldOfZuul\\src\\resources\\img\\backG1.png");
        if (path.endsWith("SemProj1")) {
            return new Image(new FileInputStream(path + "\\WorldOfZuul\\src\\resources\\img\\" + fileName));    //Add remaining path to dialog text file
        } else if (path.endsWith("WorldOfZuul")) {
            return new Image(new FileInputStream(path + "\\src\\resources\\img\\" + fileName));
        }
        //Default - probably not gonna work
        return new Image(new FileInputStream(path + "\\img\\" + fileName));
    }

    //Create player object
    private void createPlayer()   {
        try {
            Image img = load("FarmerSprite.png");

            playerSprite = new ImageView(img);

            playerSprite.setX(80);
            playerSprite.setY(400);
        } catch (FileNotFoundException e)   {
            System.out.println("File not found");
        }
    }

}
