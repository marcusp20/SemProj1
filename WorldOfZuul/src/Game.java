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
        }
        else if (commandWord == CommandWord.GO) {
            goRoom(command);
        }
        else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        }
        else if (commandWord == CommandWord.USE) {
            System.out.println("This command is used to interact with our fields, PC's, NPC's and all interactebles. ");
        } else if (commandWord == CommandWord.FIELD_SOW) {
            sowField();

        } else if (commandWord == CommandWord.FIELD_USE_PESTICIDES) {
            System.out.println("pesticies");
        } else if (commandWord == CommandWord.FIELD_HARVEST) {
            harvestField();
        } else if (commandWord == CommandWord.FIELD_SOIL_SAMPLE) {
            System.out.println(" ");
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


    //Methods for Field
    public void sowField() {
        if (!testField.getIsSowed()) {                             //Check if field is ready to be harvested
            if (!testPlayer.itemOwned("bagOfGrain")) {                          //Check for bagOfSeeds, add more seeds the further we get.
                System.out.println("No seeds in inventory, go buy some");
            } else if (testPlayer.itemOwned("tractor")) {                       //Check for tractor, shovel, or no item.
                testField.sowFieldTractor();
                //System.out.println(testField.showInfo());                 //Delete this later
            } else if (testPlayer.itemOwned("shovel")) {
                testField.sowFieldShovel();
                //System.out.println(testField.showInfo());                  //delete this later
            } else {
                System.out.println("Hmm... you don't have a shovel, or a tractor yet, better go shopping");
            }
        } else {
            System.out.println("The field has already been sowed... Try watering or harvesting");
            System.out.println(testField.getIsSowed());
        }
    }


    public void harvestField() {
        if (testField.getIsReadyToHarvest()) {                            //Checks if ready to harvest is True
            if (testPlayer.itemOwned("harvester")) {
                System.out.println("Used harvester to harvest field");
                testPlayer.sellYields(testField.getYield());  //REMEMBER TO MULTIPLY MONEYS F
                testField.harvestDone();
            } else if (testPlayer.itemOwned("scythe")) {
                System.out.println("Used the slow scythe to harvest field");
                testField.harvestDone();

            } else {
                System.out.println("Hmm... you don't have a scythe, or a harvester yet, better go shopping");
            }
        } else {
            System.out.println("Field not ready to harvest. Try to sow some seeds or watering first");
        }
        System.out.println(testField.getYield());
        System.out.println("Wallet " + testPlayer.checkwallet());
    }

    public void fertilizeField() {
        if (testPlayer.itemOwned("bagoffertilizer")) {
            if (testField.getIsSowed()) {
                testField.useFertilizerAfterSow();
                System.out.println(testField.getYield());

            } else {
                testField.useFertilizerBeforeSow();
                System.out.println(testField.getYield());

            }
        }
        else {
            System.out.println("No fertilizer in inventory");
        }
    }

    public void waterField() {
        if (testField.getIsSowed()) {
            testField.waterField();

        } else {
            System.out.println("You have nothing to water...");
        }
        System.out.println(testField.getYield());
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
