package interactable;

import game.CommandWords;
import game.Game;
import java.util.Random;

public class FlowerBed extends Interactable implements TimeProgression {
    private int flowerBeds[] = new int[6];  //Each index is a flower bed, int describes flowers quality
    private double bees = 0;
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

    public double getBees() {
        return bees;
    }

    private int[] getFlowers()   {
        return this.flowerBeds;
    }

    private int getFlowerQuality() {
        int flowerQuality = 0;
        for (int i : flowerBeds) {
            flowerQuality += i;
        }
        return flowerQuality;
    }

    public void plantFlower() {
        //Check if bed is full
        boolean bedFull = false;
        for (int i = 5; i>0 ; i--) {
            if (flowerBeds[i] == 0) {
                break;
            } else {
                bedFull = true;
            }
        }

        if (bedFull) {
            System.out.println("Maximum amount of flowers planted already");
        //adds new flower to empty bed and breaks after.
        } else {
            int flowerRNG = random.nextInt((6 - 1) + 1) + 1; //Generate random int
            for (int i=0; i<flowerBeds.length; i++) {
                if (flowerBeds[i] == 0) {
                    flowerBeds[i] = flowerRNG;
                    System.out.println("New flower planted!");
                    break;
                }
            }
        }
    }

    public void calcBees(int pestCounter) {
        double pestMultiplier;
        switch (pestCounter) {
            case 0: pestMultiplier = 1;
                    break;
            case 1: pestMultiplier = 0.7;
                break;
            case 2: pestMultiplier = 0.5;
                break;
            case 3: pestMultiplier = 0.3;
                break;
            case 4: pestMultiplier = 0.2;
                break;
            case 5: pestMultiplier = 0.1;
                break;
            default: pestMultiplier = 0.01;
                break;
        }

        bees = getFlowerQuality() * pestMultiplier;
        if (bees > 15) {
            System.out.println("Bee population is very high");
        } else if (bees > 12) {
            System.out.println("Bee population is high");
        } else if (bees > 7) {
            System.out.println("Bee population is normal");
        } else if (bees > 3) {
            System.out.println("Bee population is low ");
        } else {
            System.out.println("Bee population is very low ");
        }
    }
}
