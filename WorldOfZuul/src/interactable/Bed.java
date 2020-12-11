package interactable;

public class Bed extends Interactable {

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
