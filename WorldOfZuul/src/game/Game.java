package game;

import GUI.CollisionBox;
import GUI.RoomCollisions;
import chadChicken.ChadChicken;
import chadChicken.Quiz;
import chadChicken.TextQuiz;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import interactable.*;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class Game {
    //CommandWords
    private CommandWords gameCommandWords;
    private CommandWords storeCommandWords;
    private CommandWords fieldCommandWords;
    private CommandWords flowerBedCommandWords;
    private CommandWords beeHiveCommandWords;

    //Interactables
    private Shop shop;
    private NPC majorBob;
    private NPC shopkeeperLizzy;
    private NPC farmerBob;
    private NPC beekeeperBetti;
    private NPC fieldExpertBenny;
    private Bed hqBed;
    private FlowerBed flowerBed;
    private BeeHive beeHive;
    private Field field, field2, field3;

    //Rooms
    private HashMap<String, Room> unLockableRooms;
    private Room currentRoom;

    //Player
    private Player player;
    private List<Item> storeItemList;

    //Quiz
    private ChadChicken chadChicken;
    private Quiz preQuiz;
    private Quiz postQuiz;

    //Game
    private Parser parser;
    private GameLogger logger;
    private boolean isCreatedFromSaveFile;
    private TaskList taskList;
    private int gameTimer = 0;
    private static final Random random = new Random();
    private long seed;
    private boolean isGUI;
    private boolean gameFinished = false;

    //console output
    private final PrintStream old = System.out;
    ByteArrayOutputStream baos;

    public Game(long seed, boolean isGUI) {
        this.seed = seed;
        this.isGUI = isGUI;
        random.setSeed(seed);
        unLockableRooms = new HashMap<>();

        //Create command words
        createCommandWords();
        if(isGUI) {
            CommandWords allCommandWords = new CommandWords();
            allCommandWords.addAllCommandWords();
            parser = new Parser(allCommandWords);
        } else {
            parser = new Parser(gameCommandWords);
        }

        //Create intractables
        createNPC();
        createStore();
        createField();
        createPlayer();
        createFlowerBed();

        createBed();
        createBeeHive();

        //create task list
        taskList = new TaskList(this, player);

        //Create room objects
        createRooms();

        //Create store items
        createStoreItemList();

        //Create quiz
        createQuiz();

        //Create game logger (for saving games)
        createGameLogger();

        //create
        baos = getOutputStream();
    }

    public Game(long seed) {
        this(seed, false);
    }

    public Game(boolean isGUI) {
        this(random.nextLong(), isGUI);
    }

    public Game() {
        this(random.nextLong(), false);
    }

    public static Random getRandom() {
        return random;
    }

/////////////////////////////////////////////////////////////////////////////////////
    ///////////////// Create Methods used in constructor ////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    private void createGameLogger() {
        logger = new GameLogger(seed);
        isCreatedFromSaveFile = false;
    }

    private void createQuiz() {
        chadChicken = new ChadChicken();
        if (!isGUI) {
            preQuiz = new TextQuiz(chadChicken.getPreQuestions());
            postQuiz = new TextQuiz(chadChicken.getPostQuestions());
        }
    }

    public void createStoreItemList() {
        storeItemList = new ArrayList<>();

        for (ItemName itemName : ItemName.values()) {
            storeItemList.add(new Item(itemName));
        }
    }

    public void createStore() {
        shop = new Shop();
        shop.getImageView().setX(568);
        shop.getImageView().setY(51);
        shop.getImageView().setFitWidth(152);
        shop.getImageView().setFitHeight(150);
        shop.getImageView().setVisible(false);
    }

    private void createNPC() {
        File majorBobDialog = FileLoader.loadFile("majorBobDialog.txt");
        majorBob = new NPC(majorBobDialog);
        try {
            majorBob.getImageView().setImage(FileLoader.loadImage("MayorBobSprite.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        majorBob.getImageView().setX(250);
        majorBob.getImageView().setY(300);

        File storeNPCDialog = FileLoader.loadFile("shopKeeperLizzyDialog.txt");
        shopkeeperLizzy = new NPC(storeNPCDialog);
        try {
            shopkeeperLizzy.getImageView().setImage(FileLoader.loadImage("Shopkeeperlizzysprite.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        shopkeeperLizzy.getImageView().setX(360);
        shopkeeperLizzy.getImageView().setY(150);

        File fieldNPCDialog = FileLoader.loadFile("fieldNPCDialog.txt");
        farmerBob = new NPC(fieldNPCDialog);
        try {
            farmerBob.getImageView().setImage(FileLoader.loadImage("FarmerBob.png"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        farmerBob.getImageView().setX(170);
        farmerBob.getImageView().setY(190);

        File beekeeperDialog = FileLoader.loadFile("beekeeperBetti.txt");
        beekeeperBetti = new NPC(beekeeperDialog);
        try {
            beekeeperBetti.getImageView().setImage(FileLoader.loadImage("BeeKeeperBettiSprite.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        beekeeperBetti.getImageView().setX(200);
        beekeeperBetti.getImageView().setY(320);

        File fieldExpertDialog = FileLoader.loadFile("fieldExpertBenny.txt");
        fieldExpertBenny = new NPC(fieldExpertDialog);
        try {
            fieldExpertBenny.getImageView().setImage(FileLoader.loadImage("FieldExpertBennySprite.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fieldExpertBenny.getImageView().setX(100);
        fieldExpertBenny.getImageView().setY(100);
    }

    private void createBed() {
        hqBed = new Bed();
        hqBed.getImageView().setX(822);
        hqBed.getImageView().setY(396);
        hqBed.getImageView().setFitHeight(317);
        hqBed.getImageView().setFitWidth(246);
        hqBed.getImageView().setVisible(false);

    }

    private void createBeeHive() {
        beeHive = new BeeHive();
        beeHive.getImageView().setX(221);
        beeHive.getImageView().setY(190);
        beeHive.getImageView().setFitHeight(129);
        beeHive.getImageView().setFitWidth(430);
        beeHive.getImageView().setVisible(false);
    }

    private void createFlowerBed() {
        flowerBed = new FlowerBed();
        flowerBed.getImageView().setX(742);
        flowerBed.getImageView().setY(525);
        flowerBed.getImageView().setFitWidth(339);
        flowerBed.getImageView().setFitHeight(232);
        flowerBed.getImageView().setVisible(false);
    }



    private void createCommandWords() {
        gameCommandWords = new CommandWords();
        gameCommandWords.addCommandWord(CommandWord.GO);
        gameCommandWords.addCommandWord(CommandWord.HELP);
        gameCommandWords.addCommandWord(CommandWord.QUIT);
        gameCommandWords.addCommandWord(CommandWord.USE);
        gameCommandWords.addCommandWord(CommandWord.SAVE);
        gameCommandWords.addCommandWord(CommandWord.TASK);
        gameCommandWords.addCommandWord(CommandWord.MONEY);

        // Adding additional commands
        storeCommandWords = new CommandWords();
        storeCommandWords.addCommandWord(CommandWord.STORE_BUY);
        storeCommandWords.addCommandWord(CommandWord.STORE_BROWSE);
        storeCommandWords.addCommandWord(CommandWord.HELP);
        storeCommandWords.addCommandWord(CommandWord.LEAVE);

        fieldCommandWords = new CommandWords();
        fieldCommandWords.addCommandWord(CommandWord.FIELD_SOW);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_HARVEST);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_USE_PESTICIDES);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_SOIL_SAMPLE);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_WATER);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_FERTILIZE);
        fieldCommandWords.addCommandWord(CommandWord.LEAVE);

        beeHiveCommandWords = new CommandWords();
        beeHiveCommandWords.addCommandWord(CommandWord.GARDEN_CHECK_BEES);

        flowerBedCommandWords = new CommandWords();
        flowerBedCommandWords.addCommandWord(CommandWord.GARDEN_PLANT_FLOWER);
        flowerBedCommandWords.addCommandWord(CommandWord.LEAVE);
    }

    private void createField() {
        field = new Field(fieldCommandWords);
        field.getImageView().setX(297);
        field.getImageView().setY(312);
        field.getImageView().setFitWidth(712);
        field.getImageView().setFitHeight(463);
        field.getImageView().setVisible(false);

        field2 = new Field(fieldCommandWords);
        field2.getImageView().setX(360);
        field2.getImageView().setY(360);
        field2.getImageView().setFitHeight(440);
        field2.getImageView().setFitWidth(600);
        field2.getImageView().setVisible(false);

        field3 = new Field(fieldCommandWords);
        field3.getImageView().setX(540);
        field3.getImageView().setY(385);
        field3.getImageView().setFitHeight(420);
        field3.getImageView().setFitWidth(655);
        field3.getImageView().setVisible(false);
    }

    private void createPlayer() {
        player = new Player("Lars Tyndskid");
    }

    public Player getPlayer() {
        return player;
    }

    private void createRooms() {
        Room headquarter, shed, field, field2, field3, garden, store;

        headquarter = new Room("In the headquarter"); //update description and add hashmap.
        shed = new Room("in your shed");
        field = new Room("in the field");
        field2 = new Room("in the 2nd field");
        field3 = new Room("in the 3rd field");
        garden = new Room("in the beautiful garden");
        store = new Room("in the store, smells like flower seeds in here");
        RoomCollisions headquarterCollision = new RoomCollisions();
        RoomCollisions gardenCollision = new RoomCollisions();
        RoomCollisions storeCollision = new RoomCollisions();
        RoomCollisions fieldCollision = new RoomCollisions();
        RoomCollisions field2Collision = new RoomCollisions();
        RoomCollisions field3Collision = new RoomCollisions();

        ////////////////
        //HEADQUARTERS//
        ////////////////
        headquarter.setExit("north", store);
        headquarter.setExit("east", shed);
        headquarter.setExit("south", field);
        headquarter.setExit("west", garden);
        headquarter.setNpc(majorBob);

        headquarter.setRoomPane(createPane("Headquarter.png"));
        headquarter.addInteractable(majorBob);
        headquarter.addInteractable(hqBed);

        Image headquarterImg = headquarter.getRoomPane().getBackground().getImages().get(0).getImage();
        //Manually add all collision boxes in the room
        headquarterCollision.addCollisionBox( //East wall
                new CollisionBox(headquarterImg.getWidth()-300, -40,
                        800, headquarterImg.getHeight()+180));
        headquarterCollision.addCollisionBox( // top left wall
                new CollisionBox(-20, -20, 555, 170));
        headquarterCollision.addCollisionBox( // top right wall
                new CollisionBox(610, -20, 800, 170));

        headquarterCollision.addCollisionBox( // left wall top part
                new CollisionBox(-20, -20, 190, 320));
        headquarterCollision.addCollisionBox( // left wall bottom part
                new CollisionBox(-20, 385, 190, 800));

        headquarterCollision.addCollisionBox( // bottom wall left part
                new CollisionBox(-20, 547, 555, 100));
        headquarterCollision.addCollisionBox( // bottom wall right part
                new CollisionBox(610, 547, 800, 100));

        headquarter.setRoomCollisions(headquarterCollision);

        headquarter.setIntroText(
                "Welcome Sir, " +
                "to this humble town. My name is " +
                "bob, i'm the mayor of this town.");

        ////////////////
        //FIELDS////////
        ////////////////
        field.setExit("north", headquarter);
        field.setExit("west", field2);
        field.setExit("east", field3);

        field.setRoomPane(createPane("FieldVer1.png"));
        field.addInteractable(farmerBob);
        field.addInteractable(this.field);
        field.setNpc(farmerBob);

        Image fieldImg = field.getRoomPane().getBackground().getImages().get(0).getImage();
        fieldCollision.addCollisionBox( //South border
                new CollisionBox(-20, fieldImg.getHeight()-190,
                        fieldImg.getWidth()+100, 200));
        fieldCollision.addCollisionBox( //North West fence
                new CollisionBox(-20, -20, 555, 40));
        fieldCollision.addCollisionBox( //North East fence
                new CollisionBox(610, -20, fieldImg.getWidth(), 40));
        field.setRoomCollisions(fieldCollision);

        field.setIntroText(
                "Welcome to the farm mister. " +
                "I've been tasked with getting you " +
                "started. Feel free to ask me any questions anytime.");

        ///// FIELD 2 /////
        field2.setLocked(true);
        unLockableRooms.put("field2", field2);
        field2.setExit("east", field);
        field2.setExit("north", garden);

        field2.setRoomPane(createPane("Field2Ver1.png"));
        field2.addInteractable(fieldExpertBenny);
        field2.setNpc(fieldExpertBenny);
        field2.addInteractable(this.field2);
        Image field2Img = field2.getRoomPane().getBackground().getImages().get(0).getImage();
        field2Collision.addCollisionBox(//West wall
                new CollisionBox(-20, -80,
                        10, field2Img.getHeight()+200));
        field2Collision.addCollisionBox( //North West fence
                new CollisionBox(-20, -20, 500, 40));
        field2Collision.addCollisionBox( //North East fence
                new CollisionBox(580, -20, field2Img.getWidth(), 40));
        field2Collision.addCollisionBox( //South border
                new CollisionBox(-20, field2Img.getHeight()-190,
                        field2Img.getWidth()+100, 200));
        field2.setRoomCollisions(field2Collision);

        field2.setIntroText(
                "Now that you have bought a new tractor and field, " +
                "you can have double the yields! " +
                "Feel free to ask me questions about pesticides or fertilizers"
                );


        ///// FIELD 3 /////
        field3.setLocked(true);
        unLockableRooms.put("field3", field3);
        field3.setExit("west", field);
        field3.setExit("north", shed);
        field3.setRoomPane(createPane("Field3ver1.png"));
        field3.addInteractable(this.field3);

        Image field3Img = field3.getRoomPane().getBackground().getImages().get(0).getImage();
        field3Collision.addCollisionBox( //South border
                new CollisionBox(-20, field3Img.getHeight()-190,
                        field3Img.getWidth()+100, 200));
        field3Collision.addCollisionBox( //North border
                new CollisionBox(-20, -80,
                        field3Img.getWidth()+200, 85));
        field3Collision.addCollisionBox( //East border
                new CollisionBox(field3Img.getWidth()-30, -40,
                        200, field3Img.getHeight()+180));
        field3.setRoomCollisions(field3Collision);

        field3.setIntroText(
                "After entering your third and final field " +
                "you can feel the overwhelming joy due to " +
                "the mad money you're about to make "
        );

        ////////////////
        //STORE////////
        ///////////////
        store.setExit("south", headquarter);
        store.setNpc(shopkeeperLizzy);

        store.setRoomPane(createPane("StoreVer1.png"));
        store.addInteractable(shopkeeperLizzy);
        store.addInteractable(shop);

        Image storeImg = store.getRoomPane().getBackground().getImages().get(0).getImage();
        storeCollision.addCollisionBox( //North wall
                new CollisionBox(-20, -80,
                        storeImg.getWidth()+200, 140));
        storeCollision.addCollisionBox( //East wall
                new CollisionBox(storeImg.getWidth()-85, -40,
                        200, storeImg.getHeight()+180));
        storeCollision.addCollisionBox( //West wall
                new CollisionBox(-20, -80,
                        20, storeImg.getHeight()+200));
        storeCollision.addCollisionBox(//Blue Shelf
                new CollisionBox(1080, 245, 200, 800));
        storeCollision.addCollisionBox(//Counter
                new CollisionBox(-20, -20, 325, 405));
        storeCollision.addCollisionBox(//Box below counter
                new CollisionBox(-20, 330, 220, 165));
        storeCollision.addCollisionBox( // bottom wall left part
                new CollisionBox(-20, 647, 555, 100));
        storeCollision.addCollisionBox( // bottom wall right part
                new CollisionBox(610, 647, 800, 100));
        store.setRoomCollisions(storeCollision);

        store.setIntroText(
                "Hello darling, you must be the new " +
                "farmer. My name is Lizzy, I'm the " +
                "only shopkeeper in this small town...");

        ////////////////
        //GARDEN////////
        ///////////////
        garden.setLocked(false);
        unLockableRooms.put("garden", garden);
        garden.setExit("east", headquarter);
        garden.setExit("south", field2);
        garden.setNpc(beekeeperBetti);

        garden.setRoomPane(createPane("GardenVer1.png"));
        garden.addInteractable(beekeeperBetti);
        garden.addInteractable(beeHive);
        garden.addInteractable(flowerBed);

        Image gardenImg = garden.getRoomPane().getBackground().getImages().get(0).getImage();
        gardenCollision.addCollisionBox(//West wall
                new CollisionBox(-20, -80,
                        10, gardenImg.getHeight()+200));
        gardenCollision.addCollisionBox(//North wall
                new CollisionBox(-20, -80,
                        gardenImg.getWidth()+200, 105));
        gardenCollision.addCollisionBox(//BeeHive
                new CollisionBox(155, -20, 535-55, 205));
        gardenCollision.addCollisionBox( // right wall top part
                new CollisionBox(gardenImg.getWidth()-85, -20, 190, 320));
        gardenCollision.addCollisionBox( // right wall bottom part
                new CollisionBox(gardenImg.getWidth()-85, 385, 190, 800));
        garden.setRoomCollisions(gardenCollision);

        garden.setIntroText(
                "Hello there good Farmer! " + "My name is BeeKeeper Betti " +
                        " And I love bees!" + " Come talk to me about bees and flowers any day!");

        //////////
        //SHED////
        //////////
        shed.setExit("west", headquarter);
        shed.setExit("south", field3);

        shed.setRoomPane(createPane("SHED", Color.BLANCHEDALMOND));
        shed.setIntroText("Hey! How did you get in here? Sneaky bastard");

        //Start in headquarter
        currentRoom = headquarter;
    }

    //Create pane with color background
    private Pane createPane(String name, Color color) {
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        Text text = new Text(name);
        text.setX(10);
        text.setY(10);
        pane.getChildren().add(text);
        return pane;
    }

    //Create pane with image background
    private Pane createPane(String fileName) {
        Pane pane = new Pane();
        try {
            Image img = FileLoader.loadImage(fileName);
            BackgroundImage back = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

            pane.setBackground(new Background(back));
        } catch (FileNotFoundException e) {
            System.out.println("Image not found");
        }

        return pane;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////// play method, and the methods it is using //////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Called to start a textBased game
     * The 2D-implementation is run by calling the main method in GUI/Main
     */
    public void play() {
        if (!isCreatedFromSaveFile) {
            //only if new game
            playIntro();
            printWelcome();
        }
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            if (hasLostGame()) {
                System.out.println("You have no more money or seeds, game over");
                finished = true;
            }
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    private void playIntro() {
        //print intro of ChadChicken
        //launch preQuiz
        preQuiz.run();
        chadChicken.uploadAnswers(preQuiz.getAnswers());
    }

    //Return true if no seeds, money and harvest ready.
    public Boolean hasLostGame() {
        int minSeedPrice = Math.min(Math.min(Math.min(
                ItemName.BAG_OF_CANNABIS.getPrice(),
                ItemName.BAG_OF_CORN.getPrice()),
                ItemName.BAG_OF_CLOVER.getPrice()),
                ItemName.BAG_OF_WHEAT.getPrice());
        if (player.checkWallet() <= minSeedPrice && player.checkForNoCrops() && !field.getIsSowed()) {
            System.out.println("You have no more money or seeds, game over");
            return true;
        } else {
            return false;
        }
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    public void setCreatedFromSaveFile(boolean createdFromSaveFile) {
        isCreatedFromSaveFile = createdFromSaveFile;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////// processCommand and the methods it calls ///////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    public boolean processCommand(Command command) {

        baos.reset();
        boolean wantToQuit = false;
        CommandWord commandWord = command.getCommandWord();

        if (commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        } else if (commandWord == CommandWord.LEAVE) {
            logger.log(command);
            System.out.println("You leave...");
            parser.setCommands(gameCommandWords);
        } else if (commandWord == CommandWord.HELP) {
            printHelp();
        } else if (commandWord == CommandWord.GO) {
            goRoom(command);
        } else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        } else if (commandWord == CommandWord.USE) {
            use(command);
        } else if (commandWord == CommandWord.TASK) {
            //printTaskList();
        } else if (commandWord == CommandWord.MONEY) {
            System.out.println("You have $ " + player.getWallet());
            return false;
        } else if (commandWord == CommandWord.SAVE) {
            if (logger.save()) {
                System.out.println("Game saved successfully");
            } // else the save method prints an error and a stacktrace
        }
        // Store commands
        else if (commandWord == CommandWord.STORE_BROWSE) {
            printStoreItemList();
        } else if (commandWord == CommandWord.STORE_BUY) {
            logger.log(command);
            return buyStore(command);
        }
        // Field commands
        else if (commandWord == CommandWord.FIELD_SOW) {
            sowField(command);
        } else if (commandWord == CommandWord.FIELD_USE_PESTICIDES) {
            logger.log(command);
            usePesticide();
        } else if (commandWord == CommandWord.FIELD_HARVEST) {
            logger.log(command);
            harvestField();
        } else if (commandWord == CommandWord.FIELD_SOIL_SAMPLE) {
            getFieldSample();
        } else if (commandWord == CommandWord.FIELD_WATER) {
            logger.log(command);
            waterField();
        } else if (commandWord == CommandWord.FIELD_FERTILIZE) {
            logger.log(command);
            fertilizeField();
        }
        //Garden CommandWords
        else if (commandWord == CommandWord.GARDEN_CHECK_BEES) {
            checkBees(field.getPesticidesCounter());
        } else if (commandWord == CommandWord.GARDEN_PLANT_FLOWER) {
            logger.log(command);
            plantFlower();
        }

        return wantToQuit; //only used in the text version
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Game Commands  ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    private void use(Command command) {
        baos.reset();
        if (command.getSecondWord() != null) {           //If player is attempting to use something then...
            //Checks if player is in the right place to use intractable
            String end = " used successfully";
            if (command.getSecondWord().equals("field") && currentRoom.getShortDescription().equals("in the field")) {
                logger.log(command);
                System.out.println("Field" + end);
                parser.setCommands(fieldCommandWords);
                parser.showCommands();
            } else if (command.getSecondWord().equals("store") && currentRoom.getShortDescription().equals("in the store, smells like flower seeds in here")) {
                logger.log(command);
                System.out.println("Store" + end);
                parser.setCommands(storeCommandWords);
                parser.showCommands();
            } else if (command.getSecondWord().equals("flowers") && currentRoom.getShortDescription().equals("in the beautiful garden")) {
                System.out.println("Flower bed" + end);
                parser.setCommands(flowerBedCommandWords);
                parser.showCommands();
            } else if (command.getSecondWord().equals("bed") && currentRoom.getShortDescription().equals("In the headquarter")) {
                sleep();
                logger.log(command);
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("In the headquarter")) {
                majorBob.converse();
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the store, smells like flower seeds in here")) {
                shopkeeperLizzy.converse();
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the field")) {
                farmerBob.converse();
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the beautiful garden")) {
                // System.out.println("Beekeeper Betti" + end);
                beekeeperBetti.converse();
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the 2nd field")) {
                  fieldExpertBenny.converse();
            }
        } else {
            System.out.println("This command is used to interact \n" +
                    "with your injectables: npc, store, field, beehive...");
        }
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else if (nextRoom.isLocked()) {
            System.out.println("You don't have access to that area yet.");
        } else {
            logger.log(command);
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;
        }
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at your farm.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    public void sleep() {
        hqBed.sleep();            //Used in Gui implementation
        field.nextDay();          //nextDay makes the crops grow if watered.
        field2.nextDay();
        field3.nextDay();
        checkField(field);        //update GUI component
        checkField(field2);
        checkField(field3);
        gameTimer++;              //used for event timing
        eventChecker();
        taskList.nextDay();       //Final task required user to sleep.
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Garden Commands  ///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    public void plantFlower() {
        if (player.itemOwned(ItemName.BAG_OF_FLOWER_SEEDS)) {
            flowerBed.plantFlower();
            player.removeOne(ItemName.BAG_OF_FLOWER_SEEDS);
        } else {
            System.out.println("No flower in inventory");
        }
    }

    public void checkBees(int pestCounter) {
        flowerBed.calcBees(pestCounter);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Store Commands  ///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    private boolean buyStore(Command command) {
        Item item = null;
        if (!isGUI) {
            if (!command.hasSecondWord()) {
                System.out.println("Please specify the item you want to buy.");
                return false;
            }
            try {
                int itemindex = Integer.parseInt(command.getSecondWord());
                item = storeItemList.get(itemindex);

            } catch (NumberFormatException nfe) {
                System.out.println("Give me the index of the item you wish to buy.");
                return false;
            } catch (IndexOutOfBoundsException eobe) {
                System.out.println("Please give me a number between 0 & " + (storeItemList.size() - 1));
                return false;
            }
        } else {
            for (Item i : storeItemList) {
                if (i.getName().equals(command.getSecondWord())) {
                    System.out.println(i.getName());
                    item = i;
                    break;
                }
            }
            if (item == null) {
                return false;
            }
        }
        if (!player.addWallet(-item.getPrice())) {
            System.out.println("You cannot afford it.");
        } else {
            boolean noRemove = item.getName().startsWith("Bag of")
                    || item.getName().startsWith("Pesticides"); //Check if item bought starts with "bag of"
            if (!noRemove) {
                storeItemList.remove(item);                             // remove item from StoreItemList.
                shop.removeItem(command.getSecondWord());
            }
            player.addOne(item.getEnum());  // change item hashmap value to true.
            System.out.println("you bought a " + item.getName());

            //Successfully bought an item, update tasks list, to see if purchase fulfilled a task requirement
            taskList.update();
        }
        return false;
    }

    private void printStoreItemList() {
        System.out.println("The items we have for sale are:");
        for (Item item : storeItemList) {
            System.out.println(storeItemList.indexOf(item) + " ) $" + item.getPrice() + "  \t" + item.getName() + ", " + item.getDescription());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Field Commands  ///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    //Choose which crop will be planted with Scanner.
    //Checks which crops owned to use.
    //Updates currentHarvest to choice.
    //Loops until a valid crop has been chosen
    public boolean chooseCrop(Command command) {

        Field currentField = getCurrentField();
        String choice = "";

        if (!command.hasSecondWord()) {
            System.out.println("Sow what?");
            return false;
        }
        if (command.hasSecondWord()) {
            choice = command.getSecondWord();
        }
        if (choice.equals("wheat") && player.itemOwned(ItemName.BAG_OF_WHEAT)) {
            currentField.setCurrentHarvest("wheat");
            System.out.println("Wheat was used.");
            player.removeOne(ItemName.BAG_OF_WHEAT);
        } else if (choice.equals("clover") && player.itemOwned(ItemName.BAG_OF_CLOVER)) {
            currentField.setCurrentHarvest("clover");
            System.out.println("Clover was used.");
            player.removeOne(ItemName.BAG_OF_CLOVER);
        } else if (choice.equals("corn") && player.itemOwned(ItemName.BAG_OF_CORN)) {
            currentField.setCurrentHarvest("corn");
            System.out.println("Corn was used.");
            player.removeOne(ItemName.BAG_OF_CORN);
        } else if (choice.equals("cannabis") && player.itemOwned(ItemName.BAG_OF_CANNABIS)) {
            currentField.setCurrentHarvest("cannabis");
            System.out.println("cannabis was sowed.");
            player.removeOne(ItemName.BAG_OF_CANNABIS);
        } else {
            System.out.println("You don't have \"" + choice + "\" in inventory...");
            return false;
        }
        return true;
    }


    //SowField method
    //Checks if field is sowed already. Checks for crops in inventory.
    //Checks for tractor in inventory, if not, shovel is used. If no shovel, nothing happens.
    public void sowField(Command command) {
        //Check conditions
        Field currentField = getCurrentField();
        if (currentField.getIsSowed()) {
            System.out.println("Field already sowed with " + currentField.getCurrentHarvest() + ".");
            return;
        }
        if (player.checkForNoCrops()) {
            System.out.println("Nothing to sow in inventory.");
            return;
        }
        boolean isValidCropChoice = chooseCrop(command);
        if (!isValidCropChoice) {
            return;
        }
        if (player.itemOwned(ItemName.TRACTOR)) {
            currentField.sowFieldTractor();
            logger.log(command);
        } else if (player.itemOwned(ItemName.SHOVEL)) {
            currentField.sowFieldShovel();
            logger.log(command);
        } else {
            System.out.println("No tractor er shovel in inventory.");
        }
        checkField(currentField);
    }

    //harvestField method
    //Checks if player has sowed and watered crops.
    //Checks if player owns harvester, if not, check scythe instead.
    //Calculates value yield, after scythe or harvester is used, and adds money to player wallet.
    //Resets field.
    public void harvestField() {
        Field currentField = getCurrentField();
        checkField(currentField);
        if (!currentField.getIsReadyToHarvest()) {
            if (currentField.isWatered() && currentField.getIsSowed()) {
                System.out.println("Field has not had time to grow, go take a nap at HQ");
            } else if (currentField.getIsSowed()) {
                System.out.println("The plants haven't been watered, so they have yet to grown");
            } else {
                System.out.println("There is nothing to harvest, try planting something");
            }
            return;
        }
        if (player.itemOwned(ItemName.HARVESTER)) {
            System.out.println("Used harvester on field.");
            currentField.useHarvester(currentField.getYield());
        } else if (player.itemOwned(ItemName.SCYTHE)) {
            System.out.println("Used scythe to harvest field.");
            currentField.useScythe(currentField.getYield());
        } else {
            System.out.println("You don't have a scythe, or a harvester yet, better go shopping");
            return;
        }

        currentField.calcBeeYield(flowerBed.getBees());  //Bees impact on field
        currentField.checkPreviousHarvest();             //Crop rotations impact on field
        currentField.harvestDone();                      //calc rest of yield
        player.forceAddWallet(currentField.getYield());      //yields sold to money.
        currentField.resetYield();

        System.out.println("Wallet is now " + player.checkWallet());
        checkField(currentField);

        //Check to see if player has enough money to complete task
        taskList.update();
    }

    //FertilizeField method
    //Checks for fertilizer and isSowed.
    //Fertilizer strength depends on isSowed condition.
    public void fertilizeField() {
        Field currentField = getCurrentField();
        if (player.itemOwned(ItemName.BAG_OF_FERTILIZER)) {
            if (currentField.getIsSowed()) {
                currentField.useFertilizerAfterSow();
            } else {
                currentField.useFertilizerBeforeSow();
            }
            player.removeOne(ItemName.BAG_OF_FERTILIZER);
        } else {
            System.out.println("No fertilizer in inventory.");
        }
    }


    //waterField method
    //Check for isSowed
    //See moistField method for further explanation.
    public void waterField() {
        Field currentField = getCurrentField();
        if (player.itemOwned(ItemName.WATER_CAN)) {
            if (currentField.getIsSowed()) {
                currentField.moistField();
            } else {
                System.out.println("The field needs to be sowed first.");
            }
        } else {
            System.out.println("No watering can in inventory.");
        }
        checkField(currentField);
    }


    public void usePesticide() {
        Field currentField = getCurrentField();
        if (player.itemOwned(ItemName.PESTICIDES)) {
            currentField.usePesticides();
            player.removeOne(ItemName.PESTICIDES);
        } else {
            System.out.println("No pesticides in inventory");
        }
    }


    //getFieldSample method
    //shows condition of field based on yield.
    public void getFieldSample() {
        Field currentField = getCurrentField();
        if (player.itemOwned(ItemName.SOIL_SAMPLE_COLLECTOR)) {
            if (currentField.getYield() > 64) {
                System.out.println("Your soil is in excellent condition!");
            } else if (currentField.getYield() > 33) {
                System.out.println("your soil is in good condition.");
            } else if (currentField.getYield() > 15) {
                System.out.println("Your soil could be worse.");
            } else {
                System.out.println("Your soil is not too great.");
                System.out.println("Have you tried fertilizing the soil?");
            }
        } else {
            System.out.println("You don't have a soil sample collector");
        }
    }

    //Updates GUI Image based on field state: ready to harvest, sowed, sowed and wet, barren.
    public void checkField(Field field) {
        if (field.getIsReadyToHarvest()) {
            try {
                field.getImageView().setVisible(true);
                field.getImageView().setImage((FileLoader.loadImage("FieldHarvest.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (field.getIsSowed() && field.isWatered()) {
            try {
                field.getImageView().setVisible(true);
                field.getImageView().setImage((FileLoader.loadImage("FieldSowRain.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (field.getIsSowed()) {
            try {
                field.getImageView().setVisible(true);
                field.getImageView().setImage(FileLoader.loadImage("FieldSow.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            field.getImageView().setVisible(false);
        }
    }


    public Field getCurrentField() {
        Field currentField;
        if(currentRoom.getShortDescription().endsWith("in the 3rd field")) {
            currentField = field3;
        } else if(currentRoom.getShortDescription().endsWith("in the 2nd field")) {
            currentField = field2;
        } else {
            currentField = field;
        }
        return currentField;
    }

    //This is used by the TaskList class,
    //to unlock rewards from completed tasks
    public void unlock(String roomName) {

        //Error handling - to catch mismatch typos in strings across classes.
        boolean roomExists = unLockableRooms.containsKey(roomName);
        if (!roomExists) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Typo in code: \"")
                    .append(roomName)
                    .append("\" is not a key in hashmap.\n")
                    .append("KeySet: \n");

            for (String s : unLockableRooms.keySet()) {
                errorMessage.append(s)
                        .append("\n");
            }
            System.err.println(errorMessage.toString());
            return;
        }

        Room room = unLockableRooms.get(roomName);
        room.setLocked(false);
    }

    //For running in text based, not used in Gui
    public void startEndEvent() {
        System.out.println("You collect the last of your yield.");
        System.out.println("Another honest days work.");
        System.out.println("But the earth tremors...");
        System.out.println("A heart wrenching screech fills the air!");
        System.out.println("CoCk-A-dOoDlE-dOoOO!");
        System.out.println("The rooster appears before you!");
        postQuiz.run();
    }

    public void eventChecker() {
        switch(gameTimer){
            case 2:
                field.rainEvent();
                field2.rainEvent();
                field3.rainEvent();
                baos.reset();
                System.out.println("It's raining...");
                break;
            case 3:
                baos.reset();
                System.out.println("You feel a chill down your spine,");
                System.out.println("as you spy the legendary OmegaAlphaChickenChad");
                System.out.println("through your bedroom window.");
                break;
            case 4:
                baos.reset();
                field.extremeSunEvent();
                field2.extremeSunEvent();
                field3.extremeSunEvent();
                System.out.println("It's very hot today, you wonder");
                System.out.println("about how this could affect your crops.");
                break;
            case 5:
                baos.reset();
                System.out.println("The mighty chicken roams your farm once again.");
                break;
            case 6:
                baos.reset();
                System.out.println("You see an abundance of flies today.");
                if (field.pestEvent() | field2.pestEvent() | field3.pestEvent()) { //Use bitwise or to prevent short circuit - to ensure all pestEvents are called.
                    System.out.println("Your plants were devoured by a swarm of pests.");
                } else {
                    System.out.println("Your crops was unharmed because of");
                    System.out.println("appropriate use of pesticides.");
                }
                break;
            case 7:
                baos.reset();
                System.out.println("THE CHICKEN IS WATCHING...");
                break;
        }
    }

    //returns object used for displaying console output to GUIlabel
    public ByteArrayOutputStream getOutputStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);
        return byteArrayOutputStream;
    }

    //Misc getters
    public ByteArrayOutputStream getBaos() {
        return baos;
    }

    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    public Bed getHqBed() {
        return this.hqBed;
    }

    public FlowerBed getFlowerBed() {
        return this.flowerBed;
    }

    public Field getField() {
        return this.field;
    }

    public BeeHive getBeeHive() {
        return this.beeHive;
    }

    public Shop getShop() {
        return this.shop;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public List<Item> getStoreItemList() {
        return storeItemList;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public Interactable getField2() {
        return field2;
    }

    public Field getField3() {
        return field3;
    }
}

