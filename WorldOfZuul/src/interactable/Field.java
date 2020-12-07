package interactable;
/*
    FieldExplainer
    - To get MOST amount of money in wallet from interactable.Field:
    Fertilize field twice.
    Sow seeds with tractor
    Water crops twice
    Harvest using harvester
    Use different crops every new harvest

    - To get LEAST amount of money in wallet from Field:
    Fertilize too much
    Saw seeds with shovel
    Water too much
    Harvest using Scythe
    Use the same strain of crop every new harvest.
*/

import game.Command;
import game.CommandWord;
import game.CommandWords;
import game.Game;

import java.util.Random;


public class Field extends Interactable implements TimeProgression {
    //Attributes
    private Boolean isReadyToHarvest = false;
    private Boolean isSowed = false;
    private Boolean pests = false;
    private Boolean fieldInfected = false;


    private String previousHarvest = "wheat";
    private String currentHarvest = "wheat";

    private int fertilizerCounter;
    private int pesticidesCounter;
    private int waterCounter;
    private int harvestCounter;

    private double yields;

    private Random random = Game.getRandom();


    public Field(CommandWords commandWords) {
        super(commandWords);
    }

    @Override
    public void nextDay() {
        if (isSowed && waterCounter > 0) {
            isReadyToHarvest = true;
        }
    }

    public void setCurrentHarvest(String crop) {
        currentHarvest = crop;
    }

    public void sowFieldTractor() {
        isSowed = true;
        System.out.println("You used the Tractor to sow");
        yields += 25;

    }

    public void sowFieldShovel() {
        isSowed = true;
        System.out.println("You used the shovel to sow");
        yields += 5;
    }

    public void useFertilizerBeforeSow() {
        if (fertilizerCounter <= 2) {
            yields += 19;
            fertilizerCounter += 1;
            System.out.println("Used fertilizer, soil condition is increasing");

        } else {
            yields -= 10;
            fertilizerCounter += 1;
            System.out.println("Too much fertilizer used, try sowing other crops after harvest");
        }
    }

    public void useFertilizerAfterSow() {
        System.out.println("Fertilizer is most effective before sowing, used it anyway");
        if (fertilizerCounter < 2) {
            fertilizerCounter += 1;
            yields += 7;
        } else {
            yields -= 10;
            fertilizerCounter += 1;
            System.out.println("Too much fertilizer used, try sowing other crops after harvest");
        }
    }


    public void moistField() {
        if (waterCounter < 3) {
            yields += 13;
            waterCounter++;
            System.out.println("Soil moistened, ready to harvest");
        } else {
            yields -= 10;
            waterCounter++;
            System.out.println("Are you trying to make your field into a pool?");
            if (waterCounter >= 4) {
                System.out.println("You're watering too much!");
                if (yields < -50) {
                    //TSUNAMI
                    System.out.println("Tsunami");
                    //quit game.
                }
            }
        }
    }


    public void getPests() {
        int pestRNG = random.nextInt(1); //returns a value from 0 to 4 (inclusive)
        if (pestRNG == 1) {
            pests = true;
        }
    }

    public void usePesticides() {
        pests = false;
        pesticidesCounter += 1;
        System.out.println("Pesticides was used");
        if (pesticidesCounter >= 3) {
            fieldInfected = true;
            System.out.println("Chemicals from pesticides started seeping into the groundwater");
            System.out.println("The Mayor has fined you 100 $ for groundwater contamination");
            yields -= 500;
        }
    }

    public void useScythe(double currentYield) {
        yields = currentYield * 0.9;
    }

    public void useHarvester(double currentYield) {
        yields = currentYield * 1.3;
    }

    public Boolean getIsReadyToHarvest() {
        return isReadyToHarvest;
    }

    public Boolean getIsSowed() {
        return isSowed;
    }

    public double getYield() {
        return Math.round(yields);
    }

    public boolean isWatered() {
        if (waterCounter > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getCurrentHarvest() {
        return currentHarvest;
    }

    public String getPreviousHarvest() {
        return previousHarvest;
    }

    public int getHarvestCounter() {
        return harvestCounter;
    }

    public void checkPreviousHarvest() {
        if (previousHarvest.equals(currentHarvest) && harvestCounter >= 2) {
            yields -= 30;
            System.out.println("The soil is too saturated, try sowing a new crop");

        } else if (previousHarvest.equals(currentHarvest)) {
            harvestCounter++;

        } else {
            harvestCounter = 1;
            fertilizerCounter = 0;
            yields += 20;

        }
    }

    public void getCropYields() {
        switch (currentHarvest) {
            case "wheat":
                yields += 45;
                break;
            case "clover":
                yields += 15;
                break;
            case "cannabis":
                yields += 200;
                break;
            case "corn":
                yields += 75;
                break;
        }
    }

    public int getPesticidesCounter() {
        return pesticidesCounter;
    }

    public void calcBeeYield(double bee) {
        double amount = 0;
        if (bee > 15) {
            amount = 2.5;
        } else if (bee > 10) {
            amount = 1.5;
        } else if (bee > 5) {
            amount = 1.15;
        } else if (bee > 3) {
            amount = 0.8;
        } else if (bee > 1) {
            amount = 0.75;
        } else {
            amount = 0.4;
        }
        yields = yields * amount;

    }


    public void harvestDone() {
        isReadyToHarvest = false;
        isSowed = false;
        waterCounter = 0;

        //Update yield dependent on crops.
        getCropYields();
        //check for pests
        if (pests) {
            System.out.println("Heck! there are pests in the field!");
            //System.out.println(getYield());
            if (!previousHarvest.equals(currentHarvest)) {
                System.out.println("...But good crop rotation kept the pests at a distance");
                yields = yields * 0.95;
            } else {
                yields = yields * 0.80;
            }
        }



        if (yields <= 0) {
            System.out.println("The harvested " + currentHarvest + " has cost you " + getYield() + " to produce.");
        } else {
            System.out.println("The harvested " + currentHarvest + " was sold for a profit of: " + getYield() + ".");
        }

        //Update yields, dependent on bees
        previousHarvest = currentHarvest;
        getPests();
    }

    public void resetYield() {
        yields = 0;
    }

    public void rainEvent() {
        moistField();
        moistField();
    }

    public void extremeSunEvent() {
        waterCounter = -2;
        yields = -26; //TODO Separate yields from waterCounter
    }

    public boolean pestEvent() {
        if (pesticidesCounter > 2) {
            return false;
        }
        yields = -30;
        return true;
    }

    @Override
    public String interact() {
        return "field";
    }
}