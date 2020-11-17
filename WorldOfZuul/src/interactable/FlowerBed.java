package interactable;

import game.CommandWords;
import game.Game;


import java.util.ArrayList;
import java.util.Random;

public class FlowerBed extends Interactable implements TimeProgression {
    private int flowerBeds[] = new int[6];  //Each index is a flower bed, int describes flowers quality
    private int pesticides = 0;
    private int bees = 0;
    private Random random = Game.getRandom();




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
        int flowerRNG = random.nextInt((3 - 1) + 1) + 1;

        for (int i=0; i<flowerBeds.length; i++) {
            if (flowerBeds[i] == 0) {

                flowerBeds[i] = flowerRNG;
                System.out.println("Added flower new flower to garden!");
                System.out.println(flowerBeds[i]);
                break;
            } else if (flowerBeds[i] != 0) {

            }

        }
    }

    public void calcBees() {

    }

}
