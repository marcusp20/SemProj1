package game;

import java.util.HashMap;

/**
 * CommandWords contains a hashmap from String to CommandWord.
 */
public class CommandWords {
    private HashMap<String, CommandWord> validCommands;

    public CommandWords() {
        validCommands = new HashMap<>();
    }

    public void addCommandWord(CommandWord commandWord) {
        this.validCommands.put(commandWord.toString(), commandWord);
    }

    /**
     *
     * @param commandWord a String
     * @return the CommandWord matching the param-String - or CommandWord.UNKNOWN if no CommandWord matches
     */
    public CommandWord getCommandWord(String commandWord) {
        CommandWord command = validCommands.get(commandWord);
        if (command != null) {
            return command;
        } else {
            return CommandWord.UNKNOWN;
        }
    }

    public boolean isCommand(String aString) {
        return validCommands.containsKey(aString);
    }

    public void showAll() {
        for (String command : validCommands.keySet()) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }

    public void addAllCommandWords() {
        for (CommandWord c : CommandWord.values()) {
            validCommands.put(c.toString(), c);
        }
    }
}
