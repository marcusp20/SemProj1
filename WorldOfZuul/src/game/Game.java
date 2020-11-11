package game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import interactibles.*;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private CommandWords gameCommandWords;
    private CommandWords storeCommandWords;
    private CommandWords fieldCommandWords;
    private Field testField;
    private Player player;
    private List<Item> storeItemList;
    private NPC majorBob;
    private NPC shopkeeperLizzy;
    private NPC storeNPC;

    public Game() {
        initCommandWords();
        createNPC();
        createRooms();
        parser = new Parser(gameCommandWords);
        createField();
        createPlayer();

        initStoreItemList();
    }

    public void initStoreItemList() {
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

    private void createGarden() {
        //BeeHive
    }

    private void initCommandWords() {
        gameCommandWords = new CommandWords();
        gameCommandWords.addCommandWord(CommandWord.GO);
        gameCommandWords.addCommandWord(CommandWord.HELP);
        gameCommandWords.addCommandWord(CommandWord.QUIT);
        gameCommandWords.addCommandWord(CommandWord.USE);

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
        testField = new Field(fieldCommandWords);

    }

    private void createPlayer() {
        player = new Player("Lars TyndSkid");
    }


    private void createRooms() {
        Room headquarter, shed, field, stables, garden, store;

        headquarter = new Room("In the headquarter"); //update description and add hashmap.
        shed = new Room("in your shed");
        field = new Room("in the field");
        stables = new Room("in the stable, smells nice in here");
        garden = new Room("in the beautiful garden");
        store = new Room("in the store, smells like flower seeds in here");
        
        headquarter.setExit("east", shed);
        headquarter.setExit("south", field);
        headquarter.setNpc(majorBob);

        headquarter.setExit("north", store);

        store.setExit("south", headquarter);
        store.setNpc(shopkeeperLizzy);

        shed.setExit("west", headquarter);

        field.setExit("north", headquarter);
        field.setExit("south", stables);
        field.setExit("west", garden);

        stables.setExit("north", headquarter);
        stables.setExit("east", garden);

        garden.setExit("west", stables);

        currentRoom = headquarter;
    }

    public void play() {
        printWelcome();


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

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        if(commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        } if(commandWord == commandWord.LEAVE) {
            System.out.println("You leave...");
            parser.setCommands(gameCommandWords);
        } if (commandWord == CommandWord.HELP) {
            printHelp();
        } else if (commandWord == CommandWord.GO) {
            goRoom(command);
        } else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        } else if (commandWord == CommandWord.USE) {
            use(command);
        } else if (commandWord == CommandWord.MONEY) {
            System.out.println("You have $" + player.getWallet());
            return false;
        } //TODO print player inventory
        // Store commands
        else if (commandWord == CommandWord.STORE_BROWSE) {
            printStoreItemList();
        } else if (commandWord == CommandWord.STORE_BUY) {
            return buyStore(command);
        }
        // Field commands
        else if (commandWord == CommandWord.FIELD_SOW) {
            sowField(command);
        } else if (commandWord == CommandWord.FIELD_USE_PESTICIDES) {
            //TODO implement pesticides
        } else if (commandWord == CommandWord.FIELD_HARVEST) {
            harvestField();
        } else if (commandWord == CommandWord.FIELD_SOIL_SAMPLE) {
            getFieldSample();
        } else if (commandWord == CommandWord.FIELD_WATER) {
            waterField();
        } else if (commandWord == CommandWord.FIELD_FERTILIZE) {
            fertilizeField();
        }
        return wantToQuit;
    }

    private void use(Command command) {
        if(command.getSecondWord() != null) {           //If player is attempting to use something then...
                                                        //Checks if player is in the right place to use intractable
            //TODO provide more feedback to the use
            String end = " used successfully";
            if (command.getSecondWord().equals("field") && currentRoom.getShortDescription().equals("in the field")) {
                System.out.println("Field" + end);
                parser.setCommands(fieldCommandWords);
                parser.showCommands();
            } else if (command.getSecondWord().equals("store") && currentRoom.getShortDescription().equals("in the store, smells like flower seeds in here")) {
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
            //TODO Don't remove items with prefix "BAG_OF_"
            storeItemList.remove(item);                             // remove item from StoreItemList.
            player.getPlayerInventory().put(item.getEnum(), true);  // change item hashmap value to true.
            System.out.println("you brought a " + item.getName());
        }
        return false;
    }

    private void printStoreItemList() {
        System.out.println("The items we have for sale are:");
        for (Item item : storeItemList) {
            System.out.println(storeItemList.indexOf(item) + " ) $" + item.getPrice() + "  \t" + item.getName() + ", " + item.getDescription());
        }
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at your farm.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    //Methods for Field(s)

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
                testField.setCurrentHarvest("wheat");
                System.out.println("Wheat was used");
                //break;
            } else if (choice.equals("clover") && player.itemOwned(ItemName.BAG_OF_CLOVER)) {
                testField.setCurrentHarvest("clover");
                System.out.println("Clover was used");
                //break;
            } else if (choice.equals("corn") && player.itemOwned(ItemName.BAG_OF_CORN)) {
                testField.setCurrentHarvest("corn");
                System.out.println("Corn was used");
                //break;
            } else if (choice.equals("cannabis") && player.itemOwned(ItemName.BAG_OF_CANNABIS)) {
                testField.setCurrentHarvest("cannabis");
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
        if (!testField.getIsSowed()) {
            if (player.checkForNoCrops()) { //TODO reconsidder how the chooseCrop method is used
                System.out.println("No seeds or crops in inventory, go buy some");
            } else if (player.itemOwned(ItemName.TRACTOR) && chooseCrop(command)) {
                testField.sowFieldTractor();//TODO remove crop when sown
            } else if (player.itemOwned(ItemName.SHOVEL) && chooseCrop(command)) {
                testField.sowFieldShovel();//TODO remove crop when sown
            } else { //TODO fix dialog
                System.out.println("You don't have a shovel, or a tractor yet, better go shopping...");
            }
        } else {
            System.out.println("The field has already been sowed... Try watering or harvesting");
        }
    }

    //harvestField method
    //Checks if player has sowed and watered crops.
    //Checks if player owns harvester, if not, check scythe instead.
    //Calculates value yield, after scythe or harvester is used, and adds money to player wallet.
    //Resets field.
    public void harvestField() {
        if (testField.getIsReadyToHarvest()) {
            if (player.itemOwned(ItemName.HARVESTER)) {
                System.out.println("Used harvester on field.");

                testField.useHarvester(testField.getYield());
                testField.checkPreviousHarvest();
                player.sellYields(testField.getYield()); //yields sold to money.
                testField.harvestDone();

                System.out.println("Wallet is now "  + player.checkWallet());

            } else if (player.itemOwned(ItemName.SCYTHE)) {
                System.out.println("Used scythe to harvest field.");

                testField.useScythe(testField.getYield());
                testField.checkPreviousHarvest();
                player.sellYields(testField.getYield()); //yields sold to money.
                testField.harvestDone();

                System.out.println("Wallet is now "  + player.checkWallet());

            } else {
                System.out.println("You don't have a scythe, or a harvester yet, better go shopping");
            }
        } else {
            System.out.println("Field not ready to harvest, try watering or sowing...");
        }
    }


    //FertilizeField method
    //Checks for fertilizer and isSowed.
    //Fertilizer strength depends on isSowed condition.
    public void fertilizeField() {
        if (player.itemOwned(ItemName.BAG_OF_FERTILIZER)) {
            if (testField.getIsSowed()) {
                testField.useFertilizerAfterSow();
            } else {
                testField.useFertilizerBeforeSow();
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
        if (testField.getIsSowed()) {
            testField.moistField();
        } else {
            System.out.println("You have nothing to water...");
        }
    }


    //getFieldSample method
    //shows condition of field based on yield.
    public void getFieldSample() {
        if (testField.getYield() > 64) {
            System.out.println("Your soil is in excellent condition!");
        } else if (testField.getYield() > 33  ) {
            System.out.println("your soil is in good condition.");
        } else if (testField.getYield() > 15) {
            System.out.println("Your soil could be worse...");
        } else {
            System.out.println("Your soil is not too great...");
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
}
