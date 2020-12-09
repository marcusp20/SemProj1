package interactable;

import game.CommandWords;

public class BeeHive extends Interactable implements TimeProgression {
    private int bees;

    public BeeHive(CommandWords commandWords) {
        super(commandWords);
    }

    @Override
    public void nextDay() {
        //More flowers = more bees, pesticides = dead bees
    }

    public int getBees() {
        return bees;
    }

    public void addBees() {

    }

    @Override
    public String interact()    {
        System.out.println("You are interacting with your Beehive");
        return "Beehive";
    }
}
