package interactable;

import game.CommandWords;

import java.util.ArrayList;

public class FlowerBed extends Interactable implements TimeProgression {
    private int flowerBeds[] = new int[6];  //Each index is a flower bed, int describes flowers quality
    private int pesticides = 0;
    private int bees = 0;


    public FlowerBed(CommandWords commandWords)    {
        super(commandWords);
    }

    @Override
    public void nextDay() {
    }

    private void setFlower(int bed, int quality) {
        this.flowerBeds[bed] = quality;
    }

    private int[] getFlowers()   {
        return this.flowerBeds;
    }

    public int getPesticides() {
        return this.pesticides;
    }

    public void plantFlower() {
        System.out.println(" ");
    }

    public void calcBees() {

    }

}
