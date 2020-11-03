public enum CommandWord
{
    //Game command words
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), USE("use"),
    //Interactable command words
    //Field
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
