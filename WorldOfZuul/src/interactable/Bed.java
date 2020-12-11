package interactable;

public class Bed extends Interactable {

    //Bed object is used to display bed in gui, and contain its commands (sleep)
    public Bed()   {
        super();
    }

    public void sleep() {
        System.out.println("zzzzzzzzzzzzz");
    }

    @Override
    public String interact() {
        System.out.println("You are interacting with bed");
        return "bed";
    }
}
