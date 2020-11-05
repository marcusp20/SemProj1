public enum CommandWord
{
    //Game command words
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), USE("use"),
    LEAVE("leave"),
    //Intractable command words
    //Field
    FIELD_SOW("sow"), FIELD_HARVEST("harvest"), FIELD_USE_PESTICIDES("pesticides"),
    FIELD_SOIL_SAMPLE("sample"),
    //StoreNPC
    STORE_BUY("buy"),
    STORE_BROWSE("browse");

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
