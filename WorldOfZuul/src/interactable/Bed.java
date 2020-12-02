package interactable;

import game.Command;
import game.CommandWord;

public class Bed extends Interactable {

    public Bed()   {
        super();    //Will contain location
    }

    public void sleep() {
        System.out.println("zzzzzzzzzzzzz");
    }

    @Override
    public String interact() {
        return "bed";
    }
}
