public enum CommandWord
{
    //Game command words
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), USE("use"),
    //Interactable command words
    //Field
    FIELD_SOW("sow");
    //StureNPC

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
