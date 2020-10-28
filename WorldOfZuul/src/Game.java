import java.util.ArrayList;
import java.util.List;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private CommandWords gameCommandWords;
    private CommandWords storeCommandwords;
    private List<Item> storeItemList;

    public void initStoreItemlist() {
        storeItemList = new ArrayList<Item>();

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
        createNPC();
        initStoreItemlist();
    }

    private void createNPC() {
        NPC storeNPC;
        storeNPC = new NPC("Slimcognito", storeCommandwords);

    }

    private void initCommandWords() {
        gameCommandWords = new CommandWords();
        gameCommandWords.addCommandWord(CommandWord.GO);
        gameCommandWords.addCommandWord(CommandWord.HELP);
        gameCommandWords.addCommandWord(CommandWord.QUIT);
        gameCommandWords.addCommandWord(CommandWord.USE);

        // Adding additional commands
        storeCommandwords = new CommandWords();
        storeCommandwords.addCommandWord(CommandWord.STORE_BUY);
        storeCommandwords.addCommandWord(CommandWord.STORE_BROWSE);

    }


    private void createRooms() {
        Room outside, theatre, pub, lab, office;

        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;
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

        if (commandWord == CommandWord.UNKNOWN) {
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
        if (!command.hasSecondWord()) {
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
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;
        }
    }
}
