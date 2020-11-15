import chadChicken.ChadChicken;
import chadChicken.Quiz;
import chadChicken.TextQuiz;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private CommandWords gameCommandWords;
    private CommandWords storeCommandWords;
    private CommandWords fieldCommandWords;
    private Field field;
    private Player player;
    private List<Item> storeItemList;
    private NPC majorBob;
    private NPC shopkeeperLizzy;
    private NPC storeNPC;
    private ChadChicken chadChicken;
    private Quiz preQuiz;
    private Quiz postQuiz;
    private GameLogger logger;
    private boolean isCreatedFromSaveFile;
    private HashMap<String, Room> unLockableRooms;
    private TaskList taskList;

    public Game() {
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



    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////// Create Methods used in constructor ////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    private void createGameLogger() {
        logger = new GameLogger();
        isCreatedFromSaveFile = false;
    }

    private void createQuiz() {
        chadChicken = new ChadChicken();
        //TODO change TextQuiz to GUIQuiz when QUIQuiz has been implemented
        preQuiz = new TextQuiz(chadChicken.getPreQuestions());
        postQuiz = new TextQuiz(chadChicken.getPostQuestions());

    }

    public void createStoreItemList() {
        storeItemList = new ArrayList<Item>();

        for (ItemName itemName : ItemName.values()) {
            storeItemList.add(new Item(itemName));
        }
    }

    private void createNPC() {
        File majorBobDialog = load("majorBobDialog.txt");
        majorBob = new NPC(majorBobDialog, gameCommandWords);

        File storeNPCDialog = load("shopKeeperLizzyDialog.txt");
        shopkeeperLizzy = new NPC(storeNPCDialog, storeCommandWords);

        //majorBob.converse();
        //storeNPC.converse();
    }

    /**
     * Used by createNPC to properly load textFiles
     * @param fileName name of the text file (including the .txt)
     * @return the file path of the given fileName
     */
    private File load(String fileName) {
        String path = System.getProperty("user.dir");
        if(path.endsWith("SemProj1")) {
            return new File(path + "\\WorldOfZuul\\src\\dialog\\"+fileName);    //Add remaining path to dialog text file
        } else if(path.endsWith("WorldOfZuul")) {
            return new File(path + "\\src\\dialog\\"+fileName);
        }
        //Default - probably not gonna work
            return new File(path + "\\dialog\\"+fileName);

    }

    private void createCommandWords() {
        gameCommandWords = new CommandWords();
        gameCommandWords.addCommandWord(CommandWord.GO);
        gameCommandWords.addCommandWord(CommandWord.HELP);
        gameCommandWords.addCommandWord(CommandWord.QUIT);
        gameCommandWords.addCommandWord(CommandWord.USE);
        gameCommandWords.addCommandWord(CommandWord.SAVE);

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

    }

    //Used for testing Field methods
    private void createField() {
        field = new Field(fieldCommandWords);

    }

    private void createPlayer() {
        player = new Player("Lars TyndSkid");
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


        headquarter.setExit("north", store);
        headquarter.setExit("east", shed);
        headquarter.setExit("south", field);
        headquarter.setExit("west", garden);
        headquarter.setNpc(majorBob);


        store.setExit("south", headquarter);
        store.setNpc(shopkeeperLizzy);

        shed.setExit("west", headquarter);
        shed.setExit("south", field3);

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
        unLockableRooms.put("garden",garden);
        garden.setExit("east", headquarter);
        garden.setExit("south", field2);

        currentRoom = headquarter;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////// play method, and the methods it is using //////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    public void play() {
        if(!isCreatedFromSaveFile) {
            //only if new game
            playIntro();
            printWelcome();
        }


        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            //Why does processCommand return a boolean? -rhetorical, obviously it returns false if the given command is "quit"
            //Is it benifical to make the return type void, and simply give the method more power?
            //The method already calls other methods that change the attribute "currentRoom".
            //This change would have to make the "running" variable into a field.
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    private void playIntro() {
        //print intro of ChadChicken

        //launch preQuiz
        preQuiz.run();
        chadChicken.uploadAnswers(preQuiz.getAnswers());
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

        if(commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        if(commandWord == commandWord.LEAVE) {
            logger.log(command);
            System.out.println("You leave...");
            parser.setCommands(gameCommandWords);
        }
        if (commandWord == CommandWord.HELP) {
            printHelp();
        }
        else if (commandWord == CommandWord.GO) {
            logger.log(command);
            goRoom(command);
        }
        else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        }
        else if (commandWord == CommandWord.USE) {
            use(command);
        }
        else if (commandWord == CommandWord.MONEY) {
            System.out.println("You have $" + player.getWallet());
            return false;
        } //TODO print player inventory
        else if (commandWord == CommandWord.SAVE) {
            if(logger.save()) {
                System.out.println("Game saved successfully");
            } // else the save method prints an error and a stacktrace
        }
        // Store commands
        else if (commandWord == CommandWord.STORE_BROWSE) {
            printStoreItemList();
        }
        else if (commandWord == CommandWord.STORE_BUY) {
            logger.log(command);
            return buyStore(command);
        }
        // Field commands
        else if (commandWord == CommandWord.FIELD_SOW) {
            logger.log(command);
            sowField(command);
        }
        else if (commandWord == CommandWord.FIELD_USE_PESTICIDES) {
            logger.log(command);
            //TODO implement pesticides
        }
        else if (commandWord == CommandWord.FIELD_HARVEST) {
            logger.log(command);
            harvestField();
        }
        else if (commandWord == CommandWord.FIELD_SOIL_SAMPLE) {
            getFieldSample();
        }
        else if (commandWord == CommandWord.FIELD_WATER) {
            logger.log(command);
            waterField();
        }
        else if (commandWord == CommandWord.FIELD_FERTILIZE) {
            logger.log(command);
            fertilizeField();
        }
        return wantToQuit;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Game Commands  ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    private void use(Command command) {
        if(command.getSecondWord() != null) {           //If player is attempting to use something then...
                                                        //Checks if player is in the right place to use intractable
            //TODO provide more feedback to the use
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
            } else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("In the headquarter")) {
                //System.out.println("Major Bob" + end);
                majorBob.converse();
            }else if (command.getSecondWord().equals("npc") && currentRoom.getShortDescription().equals("in the store, smells like flower seeds in here")) {
                //System.out.println("Shopkeeper Lizzy" + end);
                shopkeeperLizzy.converse();
            }

        }else   {
            System.out.println("This command is used to interact \n" +
                    "with your injectables: npc, store, field...");
        }
    }

    private void goRoom(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    private boolean quit(Command command) {
        if(command.hasSecondWord()) {
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
            String itemName = item.getName();
            boolean isUnlimitedSupply = itemName.startsWith("bag of") || itemName.startsWith("pesticides");
            if(!isUnlimitedSupply) {
                storeItemList.remove(item);                             // remove item from StoreItemList.
            }
            player.getPlayerInventory().put(item.getEnum(), true);  // change item hashmap value to true.
            System.out.println("you brought a " + item.getName());

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
            //System.out.println("Which crop would you like to use? Last used crop was " + testField.getPreviousHarvest() +  ". Type 'options' for choices.");
            String choice = "";
            if (command.hasSecondWord()) {
                choice = command.getSecondWord();//s.nextLine();
            }

            if (choice.equals("wheat") && player.itemOwned(ItemName.BAG_OF_WHEAT)) {
                field.setCurrentHarvest("wheat");
                System.out.println("Wheat was used");
                //break;
            } else if (choice.equals("clover") && player.itemOwned(ItemName.BAG_OF_CLOVER)) {
                field.setCurrentHarvest("clover");
                System.out.println("Clover was used");
                //break;
            } else if (choice.equals("corn") && player.itemOwned(ItemName.BAG_OF_CORN)) {
                field.setCurrentHarvest("corn");
                System.out.println("Corn was used");
                //break;
            } else if (choice.equals("cannabis") && player.itemOwned(ItemName.BAG_OF_CANNABIS)) {
                field.setCurrentHarvest("cannabis");
                System.out.println("cannabis was sowed");
                //break;
            } else if (choice.equals("options")) {
                System.out.println("Corn, Wheat, Clover and illegal plant...");
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
        if (field.getIsSowed()) {
            System.out.println("The field has already been sowed... Try watering or harvesting");
            return;
        }
        if (player.checkForNoCrops()) {
            System.out.println("No seeds or crops in inventory, go buy some");
            return;
        }
        boolean hasChosenACrop = chooseCrop(command);
        if(!hasChosenACrop) {
            return;
        }

        if (player.itemOwned(ItemName.TRACTOR)) {
            field.sowFieldTractor();
            //TODO remove crop when sown
        } else if (player.itemOwned(ItemName.SHOVEL)) {
            field.sowFieldShovel();
            //TODO remove crop when sown
        } else {
            System.out.println("You don't have a shovel, or a tractor yet, better go shopping...");
        }
    }

    //harvestField method
    //Checks if player has sowed and watered crops.
    //Checks if player owns harvester, if not, check scythe instead.
    //Calculates value yield, after scythe or harvester is used, and adds money to player wallet.
    //Resets field.
    public void harvestField() {
        if (!field.getIsReadyToHarvest()) {
            System.out.println("Field not ready to harvest, try watering or sowing...");
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
            } else {
                field.useFertilizerBeforeSow();
            }
        }
        else {
            System.out.println("No fertilizer in inventory");
        }
    }

    //waterField method
    //Check for isSowed
    //See moistField method for further explanation.
    public void waterField() {
        if (field.getIsSowed()) {
            field.moistField();
        } else {
            System.out.println("You have nothing to water...");
        }
    }

    //getFieldSample method
    //shows condition of field based on yield.
    public void getFieldSample() {
        if (field.getYield() > 64) {
            System.out.println("Your soil is in excellent condition!");
        } else if (field.getYield() > 33  ) {
            System.out.println("your soil is in good condition.");
        } else if (field.getYield() > 15) {
            System.out.println("Your soil could be worse...");
        } else {
            System.out.println("Your soil is not too great...");
        }
    }


    //This is used by the TaskList class,
    //to unlock rewards from completed tasks
    public void unlock(String roomName) {

        //Error handling - to catch mismatch typos in strings across classes.
        boolean roomExists = unLockableRooms.containsKey(roomName);
        if(!roomExists) {
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
}
