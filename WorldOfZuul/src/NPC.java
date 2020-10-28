public class NPC extends Interactable {
    private CommandWords commandWords;
    private String name;

    public NPC(String name, CommandWords commandWords) {
        super(commandWords);
        this.name = name;
    }
}
