package GUI;

import chadChicken.ChadChicken;
import chadChicken.Question;
import chadChicken.Quiz;
import game.*;
import interactable.Interactable;
import interactable.NPC;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.util.*;

public class Main extends Application {

    public TabPane tutorialPane;
    private static TabPane tutorialPaneInstance;
    //Create structure
    Scene scene, introScene;

    private static Game game;
    private MovementHandler movementHandler;
    private AnimationTimer timer;


    @FXML
    public static Button a1, a2, a3, a4;
    @FXML
    public static TextArea questionField;

    //Interaction keys
    private boolean e;
    private boolean backSpace;

    //Contains last opened menu
    Node lastNode;

    //Button layout fxml
    Parent rootButtonLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        showStartScreen(stage);
    }

    public void newGame(Stage stage) {
        fadeOut(stage);
        stage.close();
        game = new Game(true);
        playIntroQuiz(stage);
    }

    private void playIntroQuiz(Stage stage) {
        questionField = new TextArea();
        a1 = new Button();
        a2 = new Button();
        a3 = new Button();
        a4 = new Button();
        MainGUIQuiz guiQuiz = new MainGUIQuiz(new ChadChicken().getPreQuestions());
        guiQuiz.show(this, stage);
        fadeIn(stage);
    }

    private void playFinalQuiz(Stage stage) {
        questionField = new TextArea();
        a1 = new Button();
        a2 = new Button();
        a3 = new Button();
        a4 = new Button();
        MainGUIQuiz guiQuiz = new MainGUIQuiz(new ChadChicken().getPostQuestions());
        guiQuiz.setResumeGameAfterQuiz(false);
        guiQuiz.show(this, stage);
        questionField.setText("You collect the last of your yield. Another honest days work.\n" +
                "But the earth tremors... A heart wrenching screech fills the air!\n" +
                "CoCk-A-dOoDlE-dOoOO!\n" +
                "The rooster appears before you!");

        fadeIn(stage);
    }

    public void loadGame(Stage stage) {
        File saveFile = new File(System.getProperty("user.dir") + "\\saveFile.txt");
        boolean saveFileExists = saveFile.exists();
        if (saveFileExists) {
            game = GameLogger.loadGameFrom(saveFile, true);
        } else {
            System.err.println("Could not load saveFile");
            newGame(stage);
            return;
        }
        fadeOut(stage);
        stage.close();
        startGame(stage);
        System.out.println("LoadGame");
    }

    public void showStartScreen(Stage stage) {
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
        Button loadGameButton;
        Button newGameButton;

        newGameButton = new Button();
        newGameButton.setPrefSize(420, 69);
        newGameButton.setOpacity(0);
        newGameButton.setLayoutX(480);
        newGameButton.setLayoutY(280);
        newGameButton.setOnAction(e -> {    //This button will close this window, and start a new game
            newGame(stage);
        });

        loadGameButton = new Button();
        loadGameButton.setPrefSize(420, 69);
        loadGameButton.setOpacity(0);
        loadGameButton.setLayoutX(480);
        loadGameButton.setLayoutY(410);
        loadGameButton.setOnAction(e -> {    //This button will close this window, and load a saved game
            loadGame(stage);
        });

        Pane startPane = new Pane();
        startPane.setPrefSize(1280, 832);
        startPane.getChildren().addAll(startScreen, loadGameButton, newGameButton);

        introScene = new Scene(startPane);
        stage.setScene(introScene);
        stage.show();
    }

    //Must only be called through newGame or loadGame
    public void startGame(Stage stage) {
        movementHandler = new MovementHandler(game, game.getPlayer(), game.getPlayer().getPlayerSprite());  //Don't really need to send playerSprite since its inside player

        //Create new objects
        createListViews();

        //Set scene
        Pane p = game.getCurrentRoom().getRoomPane();

        //FXML root
        try {
            rootButtonLayout = FXMLLoader.load(getClass().getResource("ButtonLayout.fxml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // Add buttonlayout+player to first scene
        p.getChildren().addAll(game.getPlayer().getPlayerSprite(),rootButtonLayout);

        //add Children
        scene = new Scene(p);

        //Start timer
        startTimer(stage);

        //Call checkInput on keyPress/release
        scene.setOnKeyPressed(this::checkInput);
        scene.setOnKeyReleased(this::checkInput);

        //Set stage (window) tile and scene (scene includes root)
        stage.setTitle("FARMVILL 99 RETARDO EDITION");
        stage.setScene(scene);
        stage.setOpacity(0);
        stage.show();
        fadeIn(stage);

        game.getTaskList().createTaskListView();

        //Show intro
        game.getCurrentRoom().openIntroWindow();
    }

    private void startTimer(Stage stage) {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(stage);
            }
        };
        timer.start();
    }

    //Main loop content
    private void update(Stage stage) {
        //Moves player based on movement keys
        movementHandler.move();
        //Check and correct collision between player and object
        movementHandler.checkCollision();
        checkInteraction();
        //Check if room should be changed (player position)
        playerRoomChangeCheck();
        //check if text label should output console
        updateFeedbackText(game.getBaos(), game.getCurrentRoom().getFeedbackText());

        if (backSpace) {
            game.getCurrentRoom().getRoomPane().getChildren().remove(lastNode);
            backSpace = false;
        }

        if (game.hasLostGame()) {
            timer.stop();
            showLostGame(stage);
        }

        if(game.isGameFinished()) {
            timer.stop();
            playFinalQuiz(stage);
        }
    }

    private void showLostGame(Stage stage) {
        fadeOut(stage);
        TextField youLostText = new TextField("You've lost the game!");
        youLostText.setEditable(false);
        youLostText.setFont(Font.font(Font.getDefault().getName(),40));

        Button returnToMainMenu = new Button("Return to main menu");
        returnToMainMenu.setOnAction(e -> {
            fadeOut(stage);
            showStartScreen(stage);
            fadeIn(stage);
        });

        Pane pane = new Pane();
        pane.setPrefSize(1280, 832);
        pane.getChildren().addAll(youLostText, returnToMainMenu);
        scene = new Scene(pane);

        stage.setTitle("FARMVILL 99 RETARDO EDITION");
        stage.setScene(scene);
        stage.setOpacity(0);
        stage.show();
        fadeIn(stage);
    }

    private void playerRoomChangeCheck() {
        //playerSprite used to check player location
        ImageView playerSprite = game.getPlayer().getPlayerSprite();

        //NORTH
        if (playerSprite.getY() < -40) {
            removeRoomContent();
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "north"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setY(-40);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
            } else {
                playerSprite.setY(scene.getHeight() - 200);
                addRoomContent();
            }
        }
        //EAST
        if (playerSprite.getX() > scene.getWidth() - 120+55) {
            removeRoomContent();
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "east"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setX(scene.getWidth() - 120+55);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
            } else {
                playerSprite.setX(10);
                addRoomContent();
            }
        }
        //SOUTH
        if (playerSprite.getY() > scene.getHeight() - 180) {
            removeRoomContent();
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "south"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setY(scene.getHeight() - 180);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
            } else {
                playerSprite.setY(20);
                addRoomContent();
            }
        }
        //WEST
        if (playerSprite.getX() < -10) {
            removeRoomContent();
            Pane oldPane = game.getCurrentRoom().getRoomPane();
            game.processCommand(new Command(CommandWord.GO, "west"));

            if (game.getCurrentRoom().getRoomPane() == oldPane) {
                playerSprite.setX(-10);
                movementHandler.haltPlayerMovement(); // stop the player, to stop calling "go north" every frame"
            } else {
                playerSprite.setX(scene.getWidth() - 140);
                addRoomContent();
            }
        }
    }

    private void removeRoomContent()    {
        terminateTutorialPane();
        game.getCurrentRoom().getRoomPane().getChildren().remove(lastNode);
        game.getCurrentRoom().getRoomPane().getChildren().remove(game.getTaskList().getTaskListView());
        game.getCurrentRoom().getRoomPane().getChildren().remove(rootButtonLayout);
    }


    private void addRoomContent()   {
        game.getCurrentRoom().getRoomPane().getChildren().add(game.getPlayer().getPlayerSprite());
        game.getCurrentRoom().getRoomPane().getChildren().add(game.getTaskList().getTaskListView());
        scene.setRoot(game.getCurrentRoom().getRoomPane());

        game.getCurrentRoom().getRoomPane().getChildren().add(rootButtonLayout);

        if(!game.getCurrentRoom().hasBeenVisited()) {
            movementHandler.haltPlayerMovement();
            game.getCurrentRoom().openIntroWindow();
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
                double offSet = 15;

                BoundingBox interactionBounds = new BoundingBox(minX - offSet, minY - offSet, width + offSet * 2, height + offSet * 2);

                if (interactionBounds.intersects(game.getPlayer().getPlayerSprite().getLayoutBounds())) {
                    game.getBaos().reset(); //Resets Buffer before interacting
                    game.getCurrentRoom().getRoomPane().getChildren().remove(lastNode);
                    //TODO make abstract method for getting gui visuals (replace getCommandList & getNpcWindow)
                    if (i.interact().equals("npc")) {
                        NPC npc = (NPC) i;
                        if(!npc.isFirstMeeting())    {
                            npc.resetNpcWindow();
                        }
                        game.getCurrentRoom().getRoomPane().getChildren().add(npc.getNpcWindow());
                        lastNode = npc.getNpcWindow();
                    } else {
                        game.getCurrentRoom().getRoomPane().getChildren().add(i.getCommandList());
                        lastNode = i.getCommandList();
                    }
                }
            }
        }
        this.e = false;
    }

    public void updateFeedbackText(ByteArrayOutputStream b, Label l) {
        l.setOpacity(0.69);
        l.setText(b.toString());
        l.toFront();
    }

    public void fadeOut(Stage stage) {
        for (double i = 1; i >= 0.01; i = i - 0.001) {
            stage.setOpacity(i);
        }
    }

    public void fadeIn(Stage stage) {
        for (double i = 0; i <= 0.999; i = i + 0.001) {
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
                case F -> toggleTaskList();
                case I -> game.getBaos().reset();
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
        createListFromMap(fieldCommands, game.getField2());
        createListFromMap(fieldCommands, game.getField3());

        HashMap<String, Command> beeHiveCommands = new HashMap<>();

        beeHiveCommands.put("Check bee population", new Command(CommandWord.GARDEN_CHECK_BEES, ""));
        createListFromMap(beeHiveCommands, game.getBeeHive());

        HashMap<String, Command> flowerBedCommands = new HashMap<>();
        flowerBedCommands.put("Plant flower", new Command(CommandWord.GARDEN_PLANT_FLOWER, ""));
        createListFromMap(flowerBedCommands, game.getFlowerBed());


        HashMap<String, Command> shopCommands = new HashMap<>();
        for (Item i : game.getStoreItemList()) {
            shopCommands.put(("$" + i.getPrice() + "\t\t" + i.getName()), new Command(CommandWord.STORE_BUY, i.getName()));
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
        if (path.endsWith("SemProj1")) {
            return new Image(new FileInputStream(path + "\\WorldOfZuul\\src\\resources\\img\\" + fileName));    //Add remaining path to dialog text file
        } else if (path.endsWith("WorldOfZuul")) {
            return new Image(new FileInputStream(path + "\\src\\resources\\img\\" + fileName));
        }
        //Default - probably not gonna work
        return new Image(new FileInputStream(path + "\\img\\" + fileName));
    }

    public void toggleTaskList() {
        if (game.getTaskList().getTaskListView().isVisible()) {
            game.getTaskList().getTaskListView().setVisible(false);
        } else {
            game.getTaskList().getTaskListView().setVisible(true);
        }
    }

    public void printInventoryList() {
        game.getBaos().reset();
        System.out.println("You have " + game.getPlayer().getWallet() + " $ in your wallet");
        System.out.println(game.getPlayer().getInventory());
    }

    public void invButtonClicked(ActionEvent actionEvent) {
        printInventoryList();
    }

    public void saveButtonClicked(ActionEvent actionEvent) {
        game.processCommand(new Command(CommandWord.SAVE, null));
    }
    public void toggleTutorial() {
        if (tutorialPaneInstance == null) {
            tutorialPaneInstance = tutorialPane;
        }
        if (tutorialPaneInstance.isVisible()) {
            tutorialPaneInstance.setVisible(false);
        } else {
            tutorialPaneInstance.setVisible(true);
        }
    }
    public void terminateTutorialPane() {
        if (tutorialPaneInstance != null) {
            tutorialPaneInstance.setVisible(false);
            tutorialPaneInstance = null;
            tutorialPane = null;
        }
    }

    public void helpButtonClicked(ActionEvent actionEvent) {
        toggleTutorial();
    }

    private class MainGUIQuiz extends Quiz {

        private Iterator<Question> questionIterator;
        private Stage stage;
        private Main main;
        private boolean resumeGameAfterQuiz = true;

        public MainGUIQuiz(List<Question> questions) {
            super(questions);
        }

        private void storeAnswer(Question question, String answer) {
            getAnswers().put(question, answer);

            if(questionIterator.hasNext()) {
                getAnswerFromUser(questionIterator.next());
            } else if(resumeGameAfterQuiz) {
                saveAnswersToFile("PreQuestionsAnswers.txt");
                returnToGame();
            }
            else {
                saveAnswersToFile("PostQuestionsAnswers.txt");
                fadeOut(stage);
                showStartScreen(stage);
                fadeIn(stage);
            }
        }

        private void saveAnswersToFile(String fileName) {
            try {
                PrintWriter myWriter = new PrintWriter(fileName);
                myWriter.write(hashMapAnswers(fileName));
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred: Could not save Quiz Answers - " + fileName);
                e.printStackTrace();
            }
        }

        private String hashMapAnswers(String fileName) {
            StringBuilder stringBuilder = new StringBuilder();
            ChadChicken chadChicken = new ChadChicken();
            Map<Question, String> answers = getAnswers();
            List<Question> questionList = null;
            if (fileName.equals("PreQuestionsAnswers.txt")) {
                questionList = chadChicken.getPreQuestions();
            } else if (fileName.equals("PostQuestionsAnswers.txt")) {
                questionList = chadChicken.getPostQuestions();
            }
            for (Question question : questionList) {
                stringBuilder.append(question);
                stringBuilder.append(" -> ");
                stringBuilder.append(answers.get(question));
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }

        public void setResumeGameAfterQuiz(boolean resumeGameAfterQuiz) {
            this.resumeGameAfterQuiz = resumeGameAfterQuiz;
        }

        private void returnToGame() {
            fadeOut(stage);
            stage.close();
            main.startGame(stage);
        }

        public void fadeOut(Stage stage) {
            for (double i = 1; i >= 0.01; i = i - 0.0001) {
                stage.setOpacity(i);
            }
        }

        @Override
        public void run() {
            Collections.shuffle(questions);
            questionIterator = questions.listIterator();
            getAnswerFromUser(questionIterator.next());
        }

        @Override
        protected String getAnswerFromUser(Question q) {
            questionField.setText(q.getQ());
            a1.setText(q.A1);
            a1.setOnAction(e -> storeAnswer(q, q.A1));
            a2.setText(q.A2);
            a2.setOnAction(e -> storeAnswer(q, q.A2));
            a3.setText(q.A3);
            a3.setOnAction(e -> storeAnswer(q, q.A3));
            a4.setText(q.A4);
            a4.setOnAction(e -> storeAnswer(q, q.A4));

            return null;
        }

        public void show(Main main, Stage stage) {
            this.main = main;
            Pane p = new Pane();
            try {
                AnchorPane fxmlAnchorPane = FXMLLoader.load(getClass().getResource("chickenChadGUI.fxml"));
                p.getChildren().add(fxmlAnchorPane);
                AnchorPane innerFxmlAnchorPane = (AnchorPane) fxmlAnchorPane.getChildren().get(0);
                VBox vBox = (VBox) innerFxmlAnchorPane.getChildren().get(0);
                questionField = (TextArea) vBox.getChildren().get(0);
                VBox innerVBox = (VBox) vBox.getChildren().get(1);
                a1 = (Button) innerVBox.getChildren().get(0);
                a2 = (Button) innerVBox.getChildren().get(1);
                a3 = (Button) innerVBox.getChildren().get(2);
                a4 = (Button) innerVBox.getChildren().get(3);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            p.setPrefSize(1280, 832);

            this.stage = stage;
            this.stage.setScene(new Scene(p));
            this.stage.show();
            this.stage.setTitle("FARMVILL 99 RETARDO EDITION");
            this.stage.setOpacity(0);

            a1.setOnAction(e -> run());
            a1.setText("Continue");
            a2.setText("");
            a3.setText("");
            a4.setText("");
        }
    }
}
