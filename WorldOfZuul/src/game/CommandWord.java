package game;

public enum CommandWord
{
    //game.Game command words
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), USE("use"),
    LEAVE("leave"), MONEY("money"), SAVE("save"), TASK("task"),
    //Intractable command words
    //Field
    FIELD_SOW("sow"), FIELD_HARVEST("harvest"), FIELD_USE_PESTICIDES("pesticides"),
    FIELD_SOIL_SAMPLE("sample"), FIELD_WATER("water"), FIELD_FERTILIZE("fertilize"),
    //StoreNPC
    STORE_BUY("buy"),
    STORE_BROWSE("browse"),
    //Garden
    GARDEN_CHECK_BEES("bees");


    private String commandString;
    
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }
    
    public String toString()
    {
        return commandString;
    }
}
