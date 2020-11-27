package GUI;

import game.*;
import interactable.Interactable;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
    Learnt from https://www.youtube.com/watch?v=FVo1fm52hz0
 */

public class Main extends Application {

    //Create structure
    Scene scene, introScene;

    private Game game;
    private static File saveFile;

    //Movement keys
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean w;

    private boolean e;

    //Buttons
    Button loadGameButton;
    Button newGameButton;


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //TODO Init main menu
        //TODO call launchNewGame or launchLoadGame based on user choice.

        //startscreen
        ImageView startScreen = new ImageView(load("IntroScreenVer1.png"));
        startScreen.setPreserveRatio(false);
        startScreen.setFitHeight(832);
        startScreen.setFitWidth(1280);

        //Buttons
        loadGameButton = new Button();
        newGameButton = new Button();
        loadGameButton.setPrefSize(420, 69);
        newGameButton.setPrefSize(420, 69);
        loadGameButton.setOpacity(0);
        newGameButton.setOpacity(0);

        loadGameButton.setLayoutX(480);
        loadGameButton.setLayoutY(410);
        newGameButton.setLayoutX(480);
        newGameButton.setLayoutY(280);


        newGameButton.setOnAction(e -> {
            try {
                newGame(stage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        loadGameButton.setOnAction(e -> loadGame(stage));

        //Parent rootButtonLayout = FXMLLoader.load(getClass().getResource("ButtonLayout.fxml"));
        //Parent rootStartScreen = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));

        Pane startPane = new Pane();
        startPane.setPrefSize(1280, 832);
        startPane.getChildren().addAll(startScreen, loadGameButton, newGameButton);

        introScene = new Scene(startPane);
        stage.setScene(introScene);
        stage.show();

        //startGame(stage);
    }

    private void startGame(Stage stage) throws IOException {

        //Create new game object
        launchNewGame();

        //Set scene
        Pane p = game.getCurrentRoom().getRoomPane();

        //FXML root OR JavaFX Code...
        Parent rootButtonLayout = FXMLLoader.load(getClass().getResource("ButtonLayout.fxml"));

        //add Children
        p.getChildren().addAll(game.getPlayer().getPlayerSprite(), rootButtonLayout);
        scene = new Scene(p);

        //Start timer
        startTimer();

        //Call checkInput on keyPress/release
        scene.setOnKeyPressed(this::checkInput);
        scene.setOnKeyReleased(this::checkInput);

        //Set stage (window) tile and scene (scene includes root)
        stage.setTitle("FARMVILL 99 RETARDO EDITION");
        stage.setScene(scene);
        stage.setOpacity(0);
        stage.show();
        setFadeIn(stage);
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

    //Main loop content
    private void update()   {

        //Moves player based on movement keys
        move();

        playerRoomChangeCheck();
        checkCollision();
    }

    private void playerRoomChangeCheck()    {
        //playerSprite used to check player location
        ImageView playerSprite = game.getPlayer().getPlayerSprite();

        //NORTH
        if(playerSprite.getY() < -40)  {
            System.out.println("GO NORTH");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "north"));

            if(game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setY(-40);
                System.out.println("HELP");
            } else {
                playerSprite.setY(scene.getHeight()-200);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
        //EAST
        if(playerSprite.getX() > scene.getWidth() - 120) {
            System.out.println("GO EAST");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "east"));

            if(game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setX(scene.getWidth() - 120);
                System.out.println("HELP");
            } else {
                playerSprite.setX(10);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
        //SOUTH
        if(playerSprite.getY() > scene.getHeight() - 180)  {
            System.out.println("GO SOUTH");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "south"));

            if(game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setY(scene.getHeight() - 180);
                System.out.println("HELP");
            } else {
                playerSprite.setY(20);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
        //WEST
        if(playerSprite.getX() < - 10) {
            System.out.println("GO WEST");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "west"));

            if(game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setX(-10);
                System.out.println("HELP");
            } else {
                playerSprite.setX(scene.getWidth() - 140);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
    }

    private void checkCollision()    {
        ImageView player = game.getPlayer().getPlayerSprite();

        for(Interactable i: game.getCurrentRoom().getInteractables())  {
            if(i.getImageView().intersects(player.getLayoutBounds()))   {

                //Moves player to previous position if intersecting with intractable, still allows other movement
                //TODO Fix acceleration
                if(game.getPlayer().getWestSpeed() > 0 || game.getPlayer().getEastSpeed() > 0)   {
                    double curX = player.getX();
                    int curAc = game.getPlayer().getWestSpeed() - game.getPlayer().getEastSpeed();

                    player.setX(game.getPlayer().getPrevX());
                    if(i.getImageView().intersects(player.getLayoutBounds()))   {
                        player.setX(curX);
                        if(game.getPlayer().getNorthSpeed() > 0 || game.getPlayer().getSouthSpeed() > 0)   {
                            player.setY(game.getPlayer().getPrevY());
                        }
                    }
                }
                if(i.getImageView().intersects(player.getLayoutBounds())) {
                    if (game.getPlayer().getNorthSpeed() > 0 || game.getPlayer().getSouthSpeed() > 0) {
                        player.setY(game.getPlayer().getPrevY());
                        if (i.getImageView().intersects(player.getLayoutBounds())) {
                            if (game.getPlayer().getWestSpeed() > 0 || game.getPlayer().getEastSpeed() > 0) {
                                player.setX(game.getPlayer().getPrevX());
                            }
                        }
                    }
                }
            }

            //Create new bounds that extend the original, creating an area where from the player can interact
            double minX = i.getImageView().getLayoutBounds().getMinX();
            double minY = i.getImageView().getLayoutBounds().getMinY();
            double width = i.getImageView().getLayoutBounds().getWidth();
            double height = i.getImageView().getLayoutBounds().getHeight();

            BoundingBox interactionBounds = new BoundingBox(minX - 15,minY - 15,width + 30,height + 30);
            if(interactionBounds.intersects(player.getLayoutBounds()))   {
                if(this.e)  {
                    i.interact();
                    this.e = false;
                }
            }
        }
    }

    private void launchNewGame() {
        game = new Game(true);
        game.playGUI();
    }

    private void launchLoadGame() {
        saveFile = new File(System.getProperty("user.dir") + "\\saveFile.txt");
        boolean saveFileExists = saveFile.exists();
        System.out.println(saveFile);
        if(saveFileExists) {
            game = GameLogger.loadGameFrom(saveFile, true);
            game.playGUI();
        } else {
            System.err.println("Could not load saveFile");
            launchNewGame();
        }
    }

    public void newGame(Stage stage) throws IOException {
        setFadeOut(stage);
        stage.close();
        startGame(stage);
        System.out.println("NewGame");
    }

    public void loadGame(Stage stage){
        setFadeOut(stage);
        stage.close();
        launchLoadGame();
        System.out.println("LoadGame");
    }

    public void setFadeOut(Stage stage) {
        for (double i=1; i>=0.02; i = i-0.00001) {
            stage.setOpacity(i);
        }
    }

    public void setFadeIn(Stage stage) {
        for (double i=0; i<=0.999; i = i+0.00001) {
            stage.setOpacity(i);
        }
    }

    //Check pressed key and react accordingly
    private void checkInput(KeyEvent e) {
        //Key pressed event
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            //Movement
            switch (e.getCode())    {
                case W:
                    w = true;
                    break;
                case A:
                    a = true;
                    break;
                case S:
                    s = true;
                    break;
                case D:
                    d = true;
                    break;
                //Interact
                case E:
                    this.e = true;
                    break;
            }
        }

        //Key released event
        if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            //Movement
            switch (e.getCode())    {
                case W:
                    w = false;
                    break;
                case A:
                    a = false;
                    break;
                case S:
                    s = false;
                    break;
                case D:
                    d = false;
                    break;
                //Interact
                case E:
                    this.e = false;
                    break;
            }
        }
    }

    //Move player
    public void move() {
        ImageView playerSprite = game.getPlayer().getPlayerSprite();

        game.getPlayer().setPrevX(playerSprite.getX());
        game.getPlayer().setPrevY(playerSprite.getY());

        Player player = game.getPlayer();
        if(w) {
            if(player.getNorthSpeed() != 8)    {
                player.setNorthSpeed(player.getNorthSpeed() + 1);
            }
        } else  {
            if(player.getNorthSpeed() != 0)    {
                player.setNorthSpeed(player.getNorthSpeed() - 1);
            }
        }
        if(a)  {
            if(player.getEastSpeed() != 8) {
                player.setEastSpeed(player.getEastSpeed() + 1);
            }
        } else  {
            if(player.getEastSpeed() != 0) {
                player.setEastSpeed(player.getEastSpeed() - 1);
            }
        }
        if(s)   {
            if(player.getSouthSpeed() != 8) {
                player.setSouthSpeed(player.getSouthSpeed() + 1);
            }
        } else  {
            if(player.getSouthSpeed() != 0) {
                player.setSouthSpeed(player.getSouthSpeed() - 1);
            }
        }
        if(d)   {
            if(player.getWestSpeed() != 8) {
                player.setWestSpeed(player.getWestSpeed() + 1);
            }
        } else  {
            if(player.getWestSpeed() != 0) {
                player.setWestSpeed(player.getWestSpeed() - 1);
            }
        }

        playerSprite.setY(playerSprite.getY() - player.getNorthSpeed() + player.getSouthSpeed());
        playerSprite.setX(playerSprite.getX() - player.getEastSpeed() + player.getWestSpeed());

        /*
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

         */

    }

    private Image load(String fileName) throws FileNotFoundException {
        String path = System.getProperty("user.dir");
        //System.out.println("Path: " + path);
        //System.out.println("Should be: " + "C:\\Users\\Marcus\\IdeaProjects\\OOP\\SemProj1\\WorldOfZuul\\src\\resources\\img\\backG1.png");
        if (path.endsWith("SemProj1")) {
            return new Image(new FileInputStream(path + "\\WorldOfZuul\\src\\resources\\img\\" + fileName));    //Add remaining path to dialog text file
        } else if (path.endsWith("WorldOfZuul")) {
            return new Image(new FileInputStream(path + "\\src\\resources\\img\\" + fileName));
        }
        //Default - probably not gonna work
        return new Image(new FileInputStream(path + "\\img\\" + fileName));
    }
}
