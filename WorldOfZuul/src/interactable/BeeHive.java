package interactable;

import game.CommandWords;

public class BeeHive extends Interactable implements TimeProgression{
    private int bees;

    BeeHive(CommandWords commandWords)  {
        super(commandWords);
    }

    @Override
    public void nextDay() {
        //Move flowers = more bees, pesticides = dead bees
    }

    public int getBees() {
        return bees;
    }
}
