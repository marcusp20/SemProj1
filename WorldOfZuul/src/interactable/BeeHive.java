package interactable;

public class BeeHive extends Interactable {

    public BeeHive() {
        super();
    }

    @Override
    public String interact()    {
        System.out.println("You are interacting with your Beehive");
        return "Beehive";
    }
}
