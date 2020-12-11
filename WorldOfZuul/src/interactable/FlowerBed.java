package interactable;

import game.Game;
import java.util.Random;

public class FlowerBed extends Interactable implements TimeProgression {
    private int flowerBeds[] = new int[7];  //Each index is a flower bed, int describes flowers quality
    private double bees = 0;
    private Random random = Game.getRandom();


    public FlowerBed()    {
        super();
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

    //Used to plant flowers
    public void plantFlower() {
        //Check if bed is full
        boolean bedFull = false;
        for (int i = 6; i>0 ; i--) {
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
            int flowerRNG = random.nextInt((5 - 1) + 1) + 1; //Generate random int from 1-5
            for (int i=0; i<flowerBeds.length; i++) {
                if (flowerBeds[i] == 0) {
                    flowerBeds[i] = flowerRNG;
                    System.out.println("New flower planted");
                    if (flowerRNG >= 3) {
                        System.out.println("Your bees love the new flower!");
                    }
                    break;
                }
            }
        }
    }

    //Calculate current bees
    public void calcBees(int pestCounter) {
        double pestMultiplier;
        switch (pestCounter) {
            case 0: pestMultiplier = 1.5;
                    break;
            case 1: pestMultiplier = 0.95;
                break;
            case 2: pestMultiplier = 0.8;
                break;
            case 3: pestMultiplier = 0.5;
                break;
            case 4: pestMultiplier = 0.2;
                break;
            case 5: pestMultiplier = 0.1;
                break;
            default: pestMultiplier = 0.01;
                break;
        }

        bees = getFlowerQuality() * pestMultiplier;
        if (bees > 18) {
            System.out.println("Bee population is at max!");
        } else if (bees > 15) {
            System.out.println("Bee population is very high!");
        } else if (bees > 12) {
            System.out.println("Bee population is high");
        } else if (bees > 7) {
            System.out.println("Bee population is normal");
        } else if (bees > 3) {
            System.out.println("Bee population is low ");
        } else {
            System.out.println("Bee population is very low");
        }
    }
    @Override
    public String interact()    {
        System.out.println("You are interacting with flower bed");
        return "FlowerBed";
    }
}
