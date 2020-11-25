package game;

import chadChicken.ChadChicken;
import chadChicken.GUIQuiz;
import chadChicken.Quiz;
import chadChicken.TextQuiz;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private Parser parser;
    private Room currentRoom;
    private CommandWords gameCommandWords;
    private CommandWords storeCommandWords;
    private CommandWords fieldCommandWords;
    private CommandWords gardenCommandWords;
    private Field field;
    private Player player;
    private List<Item> storeItemList;
    private NPC majorBob;
    private NPC shopkeeperLizzy;
    private NPC farmerBob;
    private NPC beekeeperBetti;
    private ChadChicken chadChicken;
    private Quiz preQuiz;
    private Quiz postQuiz;
    private GameLogger logger;
    private boolean isCreatedFromSaveFile;
    private HashMap<String, Room> unLockableRooms;
    private TaskList taskList;
    private Bed hqBed;
    private int gameTimer = 0;
    private static final Random random = new Random();
    private long seed;
    private boolean isGUI;

    public Game(long seed, boolean isGUI) {
        this.seed = seed;
        this.isGUI = isGUI;
        random.setSeed(seed);
        System.out.println(seed);
        unLockableRooms = new HashMap<>();
        createCommandWords();
        createNPC();
        createRooms();
        parser = new Parser(gameCommandWords);
        createField();
        createPlayer();
        taskList = new TaskList(this, player);
        createStoreItemList();
        createQuiz();
        createGameLogger();
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
        if(isGUI) {
            preQuiz = new GUIQuiz(chadChicken.getPreQuestions());
            postQuiz = new GUIQuiz(chadChicken.getPostQuestions());
        } else {
            //TODO change TextQuiz to GUIQuiz when QUIQuiz has been implemented
            preQuiz = new TextQuiz(chadChicken.getPreQuestions());
            postQuiz = new TextQuiz(chadChicken.getPostQuestions());
        }

    }

    public void createStoreItemList() {
        storeItemList = new ArrayList<Item>();

        for (ItemName itemName : ItemName.values()) {
            storeItemList.add(new Item(itemName));
        }
    }

    private void createNPC() {
        File majorBobDialog = load("majorBobDialog.txt");
        majorBob = new NPC(majorBobDialog);

        File storeNPCDialog = load("shopKeeperLizzyDialog.txt");
        shopkeeperLizzy = new NPC(storeNPCDialog);

        File fieldNPCDialog = load("fieldNPCDialog.txt");
        farmerBob = new NPC(fieldNPCDialog);

        File beekeeperDialog = load("beekeeperBetti.txt");
        beekeeperBetti = new NPC(beekeeperDialog);

        //majorBob.converse();
        //storeNPC.converse();
    }

    private void createBed()    {
        hqBed = new Bed();
    }

    /**
     * Used by createNPC to properly load textFiles
     * @param fileName name of the text file (including the .txt)
     * @return the file path of the given fileName
     */
    private File load(String fileName) {
        String path = System.getProperty("user.dir");
        if (path.endsWith("SemProj1")) {
            return new File(path + "\\WorldOfZuul\\src\\dialog\\" + fileName);    //Add remaining path to dialog text file
        } else if (path.endsWith("WorldOfZuul")) {
            return new File(path + "\\src\\dialog\\" + fileName);
        }
        //Default - probably not gonna work
        return new File(path + "\\dialog\\" + fileName);

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

        gardenCommandWords = new CommandWords();
        gardenCommandWords.addCommandWord(CommandWord.GARDEN_CHECK_BEES);
        gardenCommandWords.addCommandWord(CommandWord.LEAVE);


    }

    //Used for testing Field methods
    private void createField() {
        field = new Field(fieldCommandWords);

    }

    private void createPlayer() {
        player = new Player("Lars Tyndskid");

        try {
            Image sprite = loadImage("FarmerSprite.png");
            player.setPlayerSprite(sprite);
        } catch (FileNotFoundException e)   {
            System.out.println("Player image not found");
        }
    }

    public Player getPlayer()   {
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

        ////////////////
        //HEADQUARTERS//
        ////////////////
        headquarter.setExit("north", store);
        headquarter.setExit("east", shed);
        headquarter.setExit("south", field);
        headquarter.setExit("west", garden);
        headquarter.setNpc(majorBob);

        headquarter.setRoomPane(createPane("Headquarter.png"));

        ////////////////
        //FIELD////////
        ////////////////
        field.setExit("north", headquarter);
        field.setExit("west", field2);
        field.setExit("east", field3);

        field.setRoomPane(createPane("FieldVer1.png"));

        ////////////////
        //STORE////////
        ///////////////

        store.setExit("south", headquarter);
        store.setNpc(shopkeeperLizzy);

        store.setRoomPane(createPane("StoreVer1.png"));

        ////////////////
        //GARDEN////////
        ///////////////
        garden.setLocked(false);
        unLockableRooms.put("garden", garden);
        garden.setExit("east", headquarter);
        garden.setExit("south", field2);

        garden.setRoomPane(createPane("GardenVer1.png"));

        shed.setExit("west", headquarter);
        shed.setExit("south", field3);
        shed.setRoomPane(createPane("SHED", Color.BLANCHEDALMOND));

        //????? Declaerd twice
        field.setExit("north", headquarter);
        field.setExit("west", field2);
        field.setExit("east", field3);

        field2.setLocked(true);
        unLockableRooms.put("field2", field2);
        field2.setExit("east", field);
        field2.setExit("north", garden);

        field3.setLocked(true);
        unLockableRooms.put("field3", field3);
        field3.setExit("west", field);
        field3.setExit("north", shed);

        garden.setLocked(true);
        unLockableRooms.put("garden", garden);
        garden.setExit("east", headquarter);
        garden.setExit("south", field2);

        currentRoom = headquarter;
    }

    private Pane createPane(String name, Color color)   {
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        Text text = new Text(name);
        text.setX(10);
        text.setY(10);
        pane.getChildren().add(text);
        return pane;
    }

    private Pane createPane(String fileName)   {
        Pane pane = new Pane();
        try {
            Image img = loadImage(fileName);

            BackgroundImage back = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

            pane.setBackground(new Background(back));
        } catch (FileNotFoundException e)   {
            System.out.println("Image not found");
        }

         return  pane;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////// play method, and the methods it is using //////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

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
            if (checkForDebt()) {
                System.out.println("You have no more money or seeds, game over");
                finished = true;
            }
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    public void playGUI() {
        if (!isCreatedFromSaveFile) {
            //only if new game
            playIntro();
            printWelcome();

        }
    }

    private void playIntro() {
        //print intro of ChadChicken

        //launch preQuiz
        preQuiz.run();
        chadChicken.uploadAnswers(preQuiz.getAnswers());
    }

    //Return true if no seeds, money and harvest ready.
    private Boolean checkForDebt() {
        if (player.checkWallet() <= 0 && player.checkForNoCrops() && !field.getIsReadyToHarvest()) {
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
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        if (commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        if (commandWord == commandWord.LEAVE) {
            logger.log(command);
            System.out.println("You leave...");
            parser.setCommands(gameCommandWords);
        }
        if (commandWord == CommandWord.HELP) {
            printHelp();
        } else if (commandWord == CommandWord.GO) {
            logger.log(command);
            goRoom(command);
        } else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        } else if (commandWord == CommandWord.USE) {
            use(command);
        } else if (commandWord == CommandWord.TASK) {
            printTaskList();
        } else if (commandWord == CommandWord.MONEY) {
            System.out.println("You have $" + player.getWallet());
            return false;
        } //TODO print player inventory
        else if (commandWord == CommandWord.SAVE) {
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
            //TODO implement pesticides
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
            System.out.println("Bees are cool");
        }

        return wantToQuit;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Game Commands  ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    private void use(Command command) {
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
            } else if (command.getSecondWord().equals("beehive") && currentRoom.getShortDescription().equals("in the beautiful garden")) {
                System.out.println("Garden" + end);
                parser.setCommands(gardenCommandWords);
                parser.showCommands();
            }

            else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("In the headquarter")) {
                majorBob.converse();
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the store, smells like flower seeds in here")) {
                shopkeeperLizzy.converse();
            }  else if(command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the field")) {
                farmerBob.converse();
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the beautiful garden")) {
                // System.out.println("Beekeeper Betti" + end);
                beekeeperBetti.converse();
            } else if (command.getSecondWord().equals("bed") && currentRoom.getShortDescription().equals("In the headquarter"))   {
                sleep();
                logger.log(command);
            }

        }else {
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

    private void printTaskList() {
        for (Task t : taskList.getTasks()) {
            if (t.isActive()) {
                System.out.println(t.getDescription() + "->" + t.getReward());
            }
        }
    }
        //Prob not a game command, room command? or something...
    public void sleep()    {
        //hqBed.sleep();            //Used in 2d implementation
        field.nextDay();
        gameTimer++;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Store Commands  ///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    private boolean buyStore(Command command) {
        // 1. check if you can afford it.
        if (!command.hasSecondWord()) {
            System.out.println("Please specify the item you want to buy.");
            return false;
        }
        Item item = null;
        try {
            int itemindex = Integer.parseInt(command.getSecondWord());
            item = storeItemList.get(itemindex);

        } catch (NumberFormatException nfe) {
            System.out.println("Give me the index of the item you wish to buy.");
            return false;
        } catch (IndexOutOfBoundsException eobe) {
            System.out.println("Please give me a number between 0 & " + (storeItemList.size()-1));
            return false;
        }
        if (!player.addWallet(-item.getPrice())) {
            System.out.println("You cannot afford it.");
        } else {
            boolean noRemove = item.getName().startsWith("Bag of")
                    || item.getName().startsWith("pesticides"); //Check if item bought starts with "bag of"
            if (!noRemove) {
                storeItemList.remove(item);                             // remove item from StoreItemList.
            }
            player.getPlayerInventory().put(item.getEnum(), true);  // change item hashmap value to true.
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
        //while (true) {
            //Scanner s = new Scanner(System.in);
            //System.out.println("Which crop would you like to use? Last used crop was " + field.getPreviousHarvest() +  ". Type 'options' for choices.");
            String choice = "";

            if (!command.hasSecondWord()) {
                choice = "?";
            }
            if (command.hasSecondWord()) {
                choice = command.getSecondWord();//s.nextLine();
            }
            if (choice.equals("wheat") && player.itemOwned(ItemName.BAG_OF_WHEAT)) {
                field.setCurrentHarvest("wheat");
                System.out.println("Wheat was used.");
                player.getPlayerInventory().put(ItemName.BAG_OF_WHEAT, false);

                //break;
            } else if (choice.equals("clover") && player.itemOwned(ItemName.BAG_OF_CLOVER)) {
                field.setCurrentHarvest("clover");
                System.out.println("Clover was used.");
                player.getPlayerInventory().put(ItemName.BAG_OF_CLOVER, false);
                //break;
            } else if (choice.equals("corn") && player.itemOwned(ItemName.BAG_OF_CORN)) {
                field.setCurrentHarvest("corn");
                System.out.println("Corn was used.");
                player.getPlayerInventory().put(ItemName.BAG_OF_CORN, false);

                //break;
            } else if (choice.equals("cannabis") && player.itemOwned(ItemName.BAG_OF_CANNABIS)) {
                field.setCurrentHarvest("cannabis");
                System.out.println("cannabis was sowed.");
                player.getPlayerInventory().put(ItemName.BAG_OF_CANNABIS, false);

                //break;
            } else if (choice.equals("?")) {
                System.out.println("Sow what?");
                return false;
            }
            else {
                System.out.println("You don't have \"" + choice + "\" in inventory...");
                return false;
            }
            return true;
        //}
    }


    //SowField method
    //Checks if field is sowed already. Checks for crops in inventory.
    //Checks for tractor in inventory, if not, shovel is used. If no shovel, nothing happens.
    public void sowField(Command command) {
        //Check conditions
        if (field.getIsSowed()) {
            System.out.println("Field already sowed with " + field.getCurrentHarvest() + ".");
            return;
        }
        if (player.checkForNoCrops()) {
            System.out.println("Nothing to sow in inventory.");
            return;
        }

        boolean isValidCropChoice = chooseCrop(command);
        if(!isValidCropChoice) {
            System.out.println("Specify what to sow please.");
            return;
        }

        if (player.itemOwned(ItemName.TRACTOR)) {
            field.sowFieldTractor();
            logger.log(command);
        } else if (player.itemOwned(ItemName.SHOVEL)) {
            field.sowFieldShovel();
            logger.log(command);
        } else {
            System.out.println("No tractor er shovel in inventory.");
        }
    }

    //harvestField method
    //Checks if player has sowed and watered crops.
    //Checks if player owns harvester, if not, check scythe instead.
    //Calculates value yield, after scythe or harvester is used, and adds money to player wallet.
    //Resets field.
    public void harvestField() {
        if (!field.getIsReadyToHarvest()) {
            if(field.isWatered()  && field.getIsSowed()) {
                System.out.println("Field has not had time to grow, go take a nap at HQ");
            } else if(field.getIsSowed()) {
                System.out.println("The plants haven't been watered, so they have yet to grown");
            } else {
                System.out.println("There is nothing to harvest, try planting something");
            }
            //System.out.println("Field not ready to harvest, try watering or sowing...");
            return;
        }

        if (player.itemOwned(ItemName.HARVESTER)) {
            System.out.println("Used harvester on field.");
            field.useHarvester(field.getYield());
        } else if (player.itemOwned(ItemName.SCYTHE)) {
            System.out.println("Used scythe to harvest field.");
            field.useScythe(field.getYield());
        } else {
            System.out.println("You don't have a scythe, or a harvester yet, better go shopping");
            return;
        }

        field.checkPreviousHarvest();
        player.sellYields(field.getYield()); //yields sold to money.
        field.harvestDone();

        System.out.println("Wallet is now "  + player.checkWallet());

        //Check to see if player has enough money to complete task
        taskList.update();
    }

    //FertilizeField method
    //Checks for fertilizer and isSowed.
    //Fertilizer strength depends on isSowed condition.
    public void fertilizeField() {
        if (player.itemOwned(ItemName.BAG_OF_FERTILIZER)) {
            if (field.getIsSowed()) {
                field.useFertilizerAfterSow();
                player.getPlayerInventory().put(ItemName.BAG_OF_FERTILIZER, false);

            } else {
                field.useFertilizerBeforeSow();
                player.getPlayerInventory().put(ItemName.BAG_OF_FERTILIZER, false);

            }
        } else {
            System.out.println("No fertilizer in inventory.");
        }
    }

    //waterField method
    //Check for isSowed
    //See moistField method for further explanation.
    public void waterField() {
        if (player.itemOwned(ItemName.WATER_CAN)) {
            if (field.getIsSowed()) {
                field.moistField();
            } else {
                System.out.println("The field needs to be sowed first.");
            }
        } else {
            System.out.println("No watering can in inventory.");
        }
    }


    public void usePesticide() {
        if (player.itemOwned(ItemName.PESTICIDES)) {
            field.usePesticides();
            System.out.println("Used pesticides on field, all pests where killed");
            player.getPlayerInventory().put(ItemName.PESTICIDES, false);
        } else {
            System.out.println("No pesticides in inventory");
        }
    }


    //getFieldSample method
    //shows condition of field based on yield.
    public void getFieldSample() {
        if (field.getYield() > 64) {
            System.out.println("Your soil is in excellent condition!");
        } else if (field.getYield() > 33) {
            System.out.println("your soil is in good condition.");
        } else if (field.getYield() > 15) {
            System.out.println("Your soil could be worse.");
        } else {
            System.out.println("Your soil is not too great.");
            System.out.println("Have you tried fertilizing the soil?");
        }
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

    public void startEndEvent() {
        System.out.println("You collect the last of your yield.");
        System.out.println("Another honest days work.");
        System.out.println("But the earth tremors...");
        System.out.println("A heart wrenching screech fills the air!");
        System.out.println("CoCk-A-dOoDlE-dOoOO!");
        System.out.println("The rooster appears before you!");
        postQuiz.run();
    }
    public void eventChecker(){
       if(gameTimer == 2) {
           field.rainEvent();
           System.out.println("It's raining");
       } else if(gameTimer == 3) {
           System.out.println("OmegaAlphaChickenChad is on your farm");
       } else if(gameTimer == 4) {
           field.extremeSunEvent();
           System.out.println("It's very hot today");
       } else if(gameTimer == 5) {
           System.out.println("OmegaAlphaChickenChad is stirring at you");
       } else if(gameTimer == 6) {
           System.out.println("You see an abundance of flies today");
           if(field.pestEvent()) {
               System.out.println("Your plants were eating by pest");
           } else
               System.out.println("Your crops was unharmed because of appropriate use of pesticides");
       } else if(gameTimer == 7) {
           System.out.println("OmegaAlphaChickenChad is keeping an eye on you");
       }
    }
    public Room getCurrentRoom() {
        return this.currentRoom;
    }


}
