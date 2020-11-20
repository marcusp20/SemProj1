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
import java.util.Scanner;

/*
    Learnt from https://www.youtube.com/watch?v=FVo1fm52hz0
 */

public class Main extends Application {

    //Create structure
    Scene scene;
    Pane root = new Pane();
    Pane root2 = new Pane();

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
        createFrameRateLabel();
        createRootNodes();

        //Set scene
        scene = new Scene(createContent(root));

        //Call checkInput on keyPress/release
        scene.setOnKeyPressed(this::checkInput);
        scene.setOnKeyReleased(this::checkInput);

        //Set stage (window) tile and scene (scene includes root)
        stage.setTitle("FARMVILL 99 RETARDO EDITION");
        stage.setScene(scene);
        stage.show();


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




    private void createRootNodes()  {
        //Create root
        try {
            Image img = load("backG1.png");

            BackgroundImage back = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

            root.setBackground(new Background(back));
        } catch (FileNotFoundException e)   {
            System.out.println("File not found");
        }

        //Create root 2
        root2.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        Text text = new Text("Hello there");
        text.setX(10);
        text.setY(10);
        root2.getChildren().add(text);
    }

    //Contains methods that are true for all scenes. Ie size, having a player & having a timer (main loop)
    private Parent createContent(Pane p)  {

        p.setPrefSize(1280,720);
        root.getChildren().add(playerSprite);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
        return p;
    }

    //Main loop content
    private void update()   {
        //t represents current game time
        t += 0.016;

        //Moves player based on movement keys
        move();

        //The following shows that only 1 player object exists
        if(false) {
            System.out.print(root.getChildren().size());
            System.out.println("  " + root2.getChildren().size());
        }

        if(room == 1) {
            if (playerSprite.getX() > root.getWidth() - 70) {
                room = 2;
                System.out.println("next scene");
                create2ndScene();
            }
        }
        if(room == 2)   {
            if (playerSprite.getX() < -70) {
                room = 1;
                System.out.println("next scene");
                create1stScene();
            }
        }
    }

    //
    private void create1stScene()   {
        root.getChildren().add(playerSprite);
        scene.setRoot(root);
        playerSprite.setX(root.getWidth()-75);
    }

    private void create2ndScene()   {
        root2.getChildren().add(playerSprite);
        scene.setRoot(root2);
        playerSprite.setX(-70);
    }

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

    //Crate Label object that displays fps
    private void createFrameRateLabel() {
        FrameTimer fps = new FrameTimer();
        Label fpsLabel = fps.run();
        fpsLabel.setFont(new Font("Arial", 40));
        fpsLabel.setTextFill(Color.HOTPINK);
        root.getChildren().add(fpsLabel);
    }

}
