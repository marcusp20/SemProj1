import java.util.Scanner;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private CommandWords gameCommandWords;
    private CommandWords fieldCommandWords;
    private Field testField;
    private Player testPlayer;


    public Game() {
        initCommandWords();
        createRooms();
        parser = new Parser(fieldCommandWords);
        createField();
        createPlayer();
    }

    private void initCommandWords() {
        gameCommandWords = new CommandWords();
        gameCommandWords.addCommandWord(CommandWord.GO);
        gameCommandWords.addCommandWord(CommandWord.HELP);
        gameCommandWords.addCommandWord(CommandWord.QUIT);
        gameCommandWords.addCommandWord(CommandWord.USE);

        fieldCommandWords = new CommandWords();
        fieldCommandWords.addCommandWord(CommandWord.FIELD_SOW);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_HARVEST);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_USE_PESTICIDES);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_SOIL_SAMPLE);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_WATER);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_FERTILIZE);
        
    }

    //Used for testing Field methods
    private void createField() {
        testField = new Field(fieldCommandWords);

    }
    private void createPlayer() {
        testPlayer = new Player("Lars TyndSkid");
    }


    private void createRooms() {
        Room headquarter, shed , field , stables, garden, store;

        headquarter = new Room("In the headquarter"); //update description and add hashmap.
        shed = new Room("in your shed");
        field = new Room("in the field");
        stables = new Room("in the stable, smells nice in here");
        garden = new Room("in the beautiful garden");
        store = new Room("in the store, smells like flower seeds in here");
        
        headquarter.setExit("east", shed);
        headquarter.setExit("south", field);

        headquarter.setExit("north", store);

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
        while (! finished) {
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
        }

        if (commandWord == CommandWord.HELP) {
            printHelp();
        } else if (commandWord == CommandWord.GO) {
            goRoom(command);
        } else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        } else if (commandWord == CommandWord.USE) {
            System.out.println("This command is used to interact with our fields, PC's, NPC's and all interactebles. ");
        } else if (commandWord == CommandWord.FIELD_SOW) {
            sowField();
        } else if (commandWord == CommandWord.FIELD_USE_PESTICIDES) {
            System.out.println("Implement pesticides");
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

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }



    //Methods for Field(s)

    //Choose which crop will be planted with Scanner.
    //Checks which crops owned to use.
    //Updates currentHarvest to choice.
    //Loops until a valid crop has been chosen
    public void chooseCrop() {
        while (true) {

            Scanner s = new Scanner(System.in);
            System.out.println("Which crop would you like to use? Last used crop was " + testField.getPreviousHarvest() +  ". Type 'options' for choices.");
            String choice = s.nextLine();

            if (choice.equals("wheat") && testPlayer.itemOwned("bagOfWheat")) {
                testField.setCurrentHarvest("wheat");
                System.out.println("Wheat was used");
                break;
            } else if (choice.equals("clover") && testPlayer.itemOwned("bagOfClover")) {
                testField.setCurrentHarvest("clover");
                System.out.println("Clover was used");
                break;
            } else if (choice.equals("corn") && testPlayer.itemOwned("bagOfCorn")) {
                testField.setCurrentHarvest("corn");
                System.out.println("Corn was used");
                break;
            } else if (choice.equals("cannabis") && testPlayer.itemOwned("bagOfCannabisSeeds")) {
                testField.setCurrentHarvest("cannabis");
                System.out.println("cannabis was sowed");
                break;
            } else if (choice.equals("options")) {
                System.out.println("Corn, Wheat, Clover and illegal plant...");
            }
            else {
                System.out.println("You don't have " + choice + " in inventory...");
            }
        }
    }


    //SowField method
    //Checks if field is sowed already. Checks for crops in inventory.
    //Checks for tractor in inventory, if not, shovel is used. If no shovel, nothing happens.
    public void sowField() {
        if (!testField.getIsSowed()) {
            if (testPlayer.checkForNoCrops()) {
                System.out.println("No seeds or crops in inventory, go buy some");
            } else if (testPlayer.itemOwned("tractor")) {
                chooseCrop();
                testField.sowFieldTractor();
            } else if (testPlayer.itemOwned("shovel")) {
                chooseCrop();
                testField.sowFieldShovel();
            } else {
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
            if (testPlayer.itemOwned("harvester")) {
                System.out.println("Used harvester on field.");

                testField.useHarvester(testField.getYield());
                testField.checkPreviousHarvest();
                testPlayer.sellYields(testField.getYield()); //yields sold to money.
                testField.harvestDone();

                System.out.println("Wallet is now "  + testPlayer.checkWallet());

            } else if (testPlayer.itemOwned("scythe")) {
                System.out.println("Used scythe to harvest field.");

                testField.useScythe(testField.getYield());
                testField.checkPreviousHarvest();
                testPlayer.sellYields(testField.getYield()); //yields sold to money.
                testField.harvestDone();

                System.out.println("Wallet is now "  + testPlayer.checkWallet());

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
        if (testPlayer.itemOwned("bagoffertilizer")) {
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
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    private boolean quit(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;
        }
    }
}
