package GUI;

import game.*;
import interactable.Interactable;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/*
    Learnt from https://www.youtube.com/watch?v=FVo1fm52hz0
 */

public class Main extends Application {

    //Create structure
    Scene scene, introScene;

    private Game game;
    private File saveFile;
    private MovementHandler movementHandler;

    //Interaction keys
    private boolean e;
    private boolean backSpace;

    //Buttons
    Button loadGameButton;
    Button newGameButton;

    //Contains last opened menu
    ListView<String> lastLV = new ListView<>();

    //Easy access to player and player sprite objects
    private ImageView playerSprite;
    private Player player;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {

        //StartScreen
        ImageView startScreen = null;
        try {
            startScreen = new ImageView(load("IntroScreenVer1.png"));
        } catch (IOException ioException) {
            System.err.println("'IntroScreenVer1.png' not found");
            ioException.printStackTrace();
        }

        startScreen.setPreserveRatio(false);
        startScreen.setFitHeight(832);
        startScreen.setFitWidth(1280);

        //Buttons
        newGameButton = new Button();
        newGameButton.setPrefSize(420, 69);
        newGameButton.setOpacity(0);
        newGameButton.setLayoutX(480);
        newGameButton.setLayoutY(280);
        newGameButton.setOnAction(e -> newGame(stage)); //This button will close this window, and start a new game

        loadGameButton = new Button();
        loadGameButton.setPrefSize(420, 69);
        loadGameButton.setOpacity(0);
        loadGameButton.setLayoutX(480);
        loadGameButton.setLayoutY(410);
        loadGameButton.setOnAction(e -> loadGame(stage)); //This button will close this window, and load a saved game

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

    public void newGame(Stage stage) {
        fadeOut(stage);
        stage.close();
        game = new Game(true);
        game.playGUI();
        startGame(stage);
        //System.out.println("NewGame");
    }

    public void loadGame(Stage stage) {
        fadeOut(stage);
        stage.close();
        saveFile = new File(System.getProperty("user.dir") + "\\saveFile.txt");
        boolean saveFileExists = saveFile.exists();
        //System.out.println(saveFile);
        if (saveFileExists) {
            game = GameLogger.loadGameFrom(saveFile, true);
            game.playGUI();
        } else {
            System.err.println("Could not load saveFile");
            newGame(stage);
        }
        startGame(stage);
        System.out.println("LoadGame");
    }

    //Must only be called through newGame or loadGame
    private void startGame(Stage stage) {
        player = game.getPlayer();
        playerSprite = player.getPlayerSprite();
        movementHandler = new MovementHandler(game, player, playerSprite);
        //Create new objects
        createListViews();

        //Set scene
        Pane p = game.getCurrentRoom().getRoomPane();

        //FXML root OR JavaFX Code...
        try {
            Parent rootButtonLayout = FXMLLoader.load(getClass().getResource("ButtonLayout.fxml"));
            p.getChildren().addAll(game.getPlayer().getPlayerSprite(), rootButtonLayout);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        //add Children
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
        fadeIn(stage);
    }

    private void startTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    //Main loop content
    private void update() {
        //Moves player based on movement keys
        movementHandler.move();
        //Check and correct collision between player and object
        movementHandler.checkCollision();
        checkInteraction();
        //Check if room should be changed (player position)
        playerRoomChangeCheck();

        if (backSpace) {
            game.getCurrentRoom().getRoomPane().getChildren().remove(lastLV);
            backSpace = false;
            //System.out.println("??");
        }
    }

    private void playerRoomChangeCheck() {
        //playerSprite used to check player location
        ImageView playerSprite = game.getPlayer().getPlayerSprite();

        //NORTH
        if (playerSprite.getY() < -40) {
            game.getCurrentRoom().getRoomPane().getChildren().remove(lastLV);
            //System.out.println("GO NORTH");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "north"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setY(-40);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
                //System.out.println("HELP");
            } else {
                playerSprite.setY(scene.getHeight() - 200);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
        //EAST
        if (playerSprite.getX() > scene.getWidth() - 120) {
            game.getCurrentRoom().getRoomPane().getChildren().remove(lastLV);
            //System.out.println("GO EAST");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "east"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setX(scene.getWidth() - 120);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
                //System.out.println("HELP");
            } else {
                playerSprite.setX(10);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
        //SOUTH
        if (playerSprite.getY() > scene.getHeight() - 180) {
            game.getCurrentRoom().getRoomPane().getChildren().remove(lastLV);
            //System.out.println("GO SOUTH");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "south"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setY(scene.getHeight() - 180);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
                //System.out.println("HELP");
            } else {
                playerSprite.setY(20);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
        //WEST
        if (playerSprite.getX() < -10) {
            game.getCurrentRoom().getRoomPane().getChildren().remove(lastLV);
            //System.out.println("GO WEST");
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "west"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setX(-10);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
                //System.out.println("HELP");
            } else {
                playerSprite.setX(scene.getWidth() - 140);
                game.getCurrentRoom().getRoomPane().getChildren().add(playerSprite);
                scene.setRoot(game.getCurrentRoom().getRoomPane());
            }
        }
    }

    private void checkInteraction() {
        if (this.e) {
            for (Interactable i : game.getCurrentRoom().getInteractables()) {
                //Create new bounds that extend the original, creating an area where from the player can interact with said object
                //Potentially optimize by placing interactionBounds in objects
                double minX = i.getImageView().getLayoutBounds().getMinX();
                double minY = i.getImageView().getLayoutBounds().getMinY();
                double width = i.getImageView().getLayoutBounds().getWidth();
                double height = i.getImageView().getLayoutBounds().getHeight();
                BoundingBox interactionBounds = new BoundingBox(minX - 15, minY - 15, width + 30, height + 30);

                if (interactionBounds.intersects(playerSprite.getLayoutBounds())) {
                    game.getCurrentRoom().getRoomPane().getChildren().remove(lastLV);
                    game.getCurrentRoom().getRoomPane().getChildren().add(i.getCommandList());
                    lastLV = i.getCommandList();
                }
            }
        }
        this.e = false;
    }

    public void fadeOut(Stage stage) {
        for (double i = 1; i >= 0.02; i = i - 0.00001) {
            stage.setOpacity(i);
        }
    }

    public void fadeIn(Stage stage) {
        for (double i = 0; i <= 0.999; i = i + 0.00001) {
            stage.setOpacity(i);
        }
    }

    //Check pressed key and react accordingly
    private void checkInput(KeyEvent keyEvent) {
        //Key pressed event
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (keyEvent.getCode()) {
                //Movement
                case W -> movementHandler.w = true;
                case A -> movementHandler.a = true;
                case S -> movementHandler.s = true;
                case D -> movementHandler.d = true;
                //Interact
                case E -> e = true;
                case BACK_SPACE -> this.backSpace = true;
            }
        }

        //Key released event
        if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
            switch (keyEvent.getCode()) {
                //Movement
                case W -> movementHandler.w = false;
                case A -> movementHandler.a = false;
                case S -> movementHandler.s = false;
                case D -> movementHandler.d = false;
                //Interact
                case E -> e = false;
                case BACK_SPACE -> this.backSpace = false;
            }
        }
    }

    private void createListViews() {
        HashMap<String, Command> bedCommands = new HashMap<>();
        bedCommands.put("Use bed", new Command(CommandWord.USE, "bed"));
        createListFromMap(bedCommands, game.getHqBed());

        HashMap<String, Command> fieldCommands = new HashMap<>();
        fieldCommands.put("Water field", new Command(CommandWord.FIELD_WATER, ""));
        fieldCommands.put("Harvest field", new Command(CommandWord.FIELD_HARVEST, ""));
        fieldCommands.put("Spread fertilizer", new Command(CommandWord.FIELD_FERTILIZE, ""));
        fieldCommands.put("Spread pesticides", new Command(CommandWord.FIELD_USE_PESTICIDES, ""));
        fieldCommands.put("Take soil sample", new Command(CommandWord.FIELD_SOIL_SAMPLE, ""));
        fieldCommands.put("Sow clover", new Command(CommandWord.FIELD_SOW, "clover"));
        fieldCommands.put("Sow wheat", new Command(CommandWord.FIELD_SOW, "wheat"));
        fieldCommands.put("Sow corn", new Command(CommandWord.FIELD_SOW, "corn"));
        fieldCommands.put("Sow cannabis", new Command(CommandWord.FIELD_SOW, "cannabis"));
        createListFromMap(fieldCommands, game.getField());

        HashMap<String, Command> beeHiveCommands = new HashMap<>();
        beeHiveCommands.put("Use beehive", new Command(CommandWord.USE, "beehive"));
        beeHiveCommands.put("Bees?", new Command(CommandWord.GARDEN_CHECK_BEES, ""));
        createListFromMap(beeHiveCommands, game.getBeeHive());

        HashMap<String, Command> shopCommands = new HashMap<>();
        for (ItemName i : ItemName.values()) {
            shopCommands.put(("Buy " + i.getItemNameString()), new Command(CommandWord.STORE_BUY, i.getItemNameString()));
        }
        createListFromMap(shopCommands, game.getShop());
    }

    private void createListFromMap(HashMap<String, Command> commandHashMap, Interactable interactable) {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(commandHashMap.keySet());
        ListView<String> listView = new ListView<>(list);

        Callback<ListView<String>, ListCell<String>> commandCellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new CommandButtonCell(game, commandHashMap);
            }
        };
        listView.setCellFactory(commandCellFactory);
        listView.setLayoutX(interactable.getImageView().getX());
        listView.setLayoutY(interactable.getImageView().getY());
        listView.setPrefHeight(35 * commandHashMap.size());
        if (listView.getPrefHeight() > 300) {
            listView.setPrefHeight(300);
        }
        listView.setPrefWidth(200);

        interactable.setCommandList(listView);
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
