public class Game {
    private Parser parser;
    private Room currentRoom;
    private CommandWords gameCommandWords;


    public Game() {
        initCommandWords();
        createRooms();
        parser = new Parser(gameCommandWords);
    }

    private void initCommandWords() {
        gameCommandWords = new CommandWords();
        gameCommandWords.addCommandWord(CommandWord.GO);
        gameCommandWords.addCommandWord(CommandWord.HELP);
        gameCommandWords.addCommandWord(CommandWord.QUIT);
        gameCommandWords.addCommandWord(CommandWord.USE);

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
            //Tjekke Current. Hvis Currentroom == field {}
            System.out.println("This command is used to interact with our fields, PC's, NPC's and all interactebles. ");

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
