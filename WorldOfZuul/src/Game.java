import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private CommandWords gameCommandWords;
    private CommandWords storeCommandWords;
    private List<Item> storeItemList;

    public void initStoreItemlist() {
        storeItemList = new ArrayList<Item>();
    private CommandWords fieldCommandWords;
    private Field testField;
    private Player player;


        storeItemList.add(new Item("Watering can", "Water crops"));
        storeItemList.add(new Item("Shovel", "Used for digging"));
        storeItemList.add(new Item("Soil Sample collector", "Collects soils"));
        storeItemList.add(new Item("Tractor", "Used for harvesting"));
        storeItemList.add(new Item("Bag of Seeds", "holds seeds for planting"));
        storeItemList.add(new Item("Bag of fertilizer", "used for fertilizing"));
        storeItemList.add(new Item("Pesticides", "used for destroying water-collection"));
        storeItemList.add(new Item("Mobile Phone", "used for Weather info"));

    }

    public Game() {
        initCommandWords();
        createRooms();
        parser = new Parser(gameCommandWords);
        createField();
        createPlayer();

        createNPC();
        initStoreItemlist();
    }

    private void createNPC() {
        NPC storeNPC;
        //File storeNPC = new File("storeNPCDialog.txt");
        //storeNPC = new NPC(storeNPC, storeCommandWords);
        File major = new File("majorBobDialog.txt");

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

        fieldCommandWords = new CommandWords();
        fieldCommandWords.addCommandWord(CommandWord.FIELD_SOW);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_HARVEST);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_USE_PESTICIDES);
        fieldCommandWords.addCommandWord(CommandWord.FIELD_SOIL_SAMPLE);

    }

    //Used for testing Field methods
    private void createField() {
        testField = new Field(fieldCommandWords);

    }
    private void createPlayer() {
        player = new Player("Lars TyndSkid");
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
        }

        if (commandWord == CommandWord.HELP) {
            printHelp();
        } else if (commandWord == CommandWord.GO) {
            goRoom(command);
        } else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        } else if (commandWord == CommandWord.STORE_BROWSE) {
            printStoreItemList();
        } else if (commandWord == CommandWord.STORE_BUY) {
            // add functionality

            //parser.setCommandWords(storeCommandWords);
        }
        else if (commandWord == CommandWord.USE) {
            //Tjekke Current. Hvis Currentroom == field {}
            System.out.println("This command is used to interact with our fields, PC's, NPC's and all interactebles. ");

        } else if (commandWord == CommandWord.FIELD_SOW) {                  //Note; Horrorcode ahead...
            if (!testField.getIsReadyToHarvest()) {                         //Check if field is ready to be harvested
                if (player.itemOwned("tractor")) {                          //Check for tractor, shovel, or no item.
                    testField.sowFieldTractor();
                    System.out.println(testField.showInfo());               //Delete this later
                } else if (player.itemOwned("shovel")) {
                    testField.sowFieldShovel();
                    System.out.println(testField.showInfo());               //delete this later
                } else {
                    System.out.println("Hmm... you don't have a shovel, or a tractor yet, better go shopping");
                }
            } else {
                System.out.println("The field has already been sowed, try harvesting it");
            }

        } else if (commandWord == CommandWord.FIELD_USE_PESTICIDES) {
            //Soil quality - 1.
            //Harvest quality + 1
            //Hvis quality allerede er 3, skal den ikke lægge mere sammen.
            System.out.println("pesticies");

        } else if (commandWord == CommandWord.FIELD_HARVEST) {
            //If isReadyToHarvest is True, proceed
            //Afhængeigt af hvilke værdi'er vores harvestQuality er (1/3) bestemmes vores udbetalte monetos.
            System.out.println("harvest");

        } else if (commandWord == CommandWord.FIELD_SOIL_SAMPLE) {
            System.out.println(" ");
        }


        return wantToQuit;
    }

    private void printStoreItemList() {
        System.out.println("The Itmes we have for sale are:");
        for (Item item : storeItemList) {
            System.out.println(item.getName() + ", " + item.getDescription());
        }
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
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
