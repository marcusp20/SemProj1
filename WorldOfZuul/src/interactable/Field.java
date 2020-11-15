package interactable;/*
    FieldExplainer
    - To get MOST amount of money in wallet from iInteractibles.Field:
    Fertilize field twice.
    Sow seeds with tractor
    Water crops twice
    Harvest using harvester
    Use different crops every new harvest

    To get LEAST amount of money in wallet from Field:
    Fertilize too much
    Saw seeds with shovel
    Water too much
    Harvest using Scythe
    Use the same strain of crop every new harvest.
*/

import game.CommandWords;

public class Field extends Interactable implements TimeProgression {
    //Attributes
    private Boolean isReadyToHarvest = false;
    private Boolean isSowed = false;
    private Boolean isPesticidesUsed = false; //Bool to check for pesticides, Value is reset everytime a new harvest is done.'
    private Boolean pests = false;


    private String previousHarvest = "big ass cannabis plants";
    private String currentHarvest;


    private int fertilizerCounter;
    private int pesticidesCounter;
    private int waterCounter;
    private int harvestCounter;
    private double yields;


    public Field(CommandWords commandWords) {
        super(commandWords);
    }

    @Override
    public void nextDay() {
        if(isSowed && waterCounter > 0) {
            isReadyToHarvest = true;
        }
    }

    public void setCurrentHarvest(String crop) {
        currentHarvest = crop;
    }

    public void sowFieldTractor() {
        isSowed = true;
        System.out.println("You used the Tractor to sow");
        yields += 15;
    }

    public void sowFieldShovel() {
        isSowed = true;
        System.out.println("You used the shovel to sow");
        yields += 5;
    }

    public void useFertilizerBeforeSow() {
        if (fertilizerCounter < 2) {
            yields += 15;
            fertilizerCounter += 1;
            System.out.println("Used fertilizer, soil condition is increasing");

        } else {
            yields -= 10;
            fertilizerCounter += 1;
            System.out.println("Too much fertilizer used, try sowing other crops after harvest...");
        }
    }

    public void useFertilizerAfterSow() {
        System.out.println("Fertilizer is most effective before sowing, used it anyway...");
        if (fertilizerCounter < 2) {
            fertilizerCounter += 1;
            yields += 5;

        } else {
            yields -= 10;
            fertilizerCounter += 1;
            System.out.println("Too much fertilizer used, try sowing other crops after harvest...");
        }
    }



    //
    public void moistField() {
        //isReadyToHarvest = true;
        if (waterCounter < 2) {
            yields += 10;
            waterCounter++;
            System.out.println("Soil moistened");
        } else {
            yields -= 10;
            waterCounter++;
            System.out.println("Are you trying to make your field into a pool?");
            if (waterCounter >= 3) {
                System.out.println("You're watering too much!");
                if (yields < -50) {
                    //TSUNAMI
                    System.out.println("Tsunami");
                    //quit game.
                }
            }
        }
    }

    public void usePesticides() {
        if (isPesticidesUsed) {
            pests = false;
           // ()
        }
    }

    public void useScythe(double currentYield) {
        yields = currentYield*0.9;
    }

    public void useHarvester(double currentYield) {
        yields = currentYield*1.3;
    }

    public Boolean getIsReadyToHarvest() {
        return isReadyToHarvest;
    }

    public Boolean getIsSowed() {
        return isSowed;
    }

    public double getYield() {
        return yields;
    }

    public boolean isWatered()  {
        if(waterCounter > 0)    {
            return true;
        } else  {
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
            yields -= 20;
            System.out.println("Oh no! the fields nutrition levels are getting too high for crops, try sowing different types of crops.");
            System.out.println(" ");
        }
        else if  (previousHarvest.equals(currentHarvest)) {
            harvestCounter++;

        } else {
            harvestCounter = 1;
            fertilizerCounter = 0;
        }
    }

    public void harvestDone() {
        isReadyToHarvest = false;
        isSowed = false;
        waterCounter = 0;
        if (yields <= 0) {
            System.out.println("The crops harvested has cost you " + yields + " to produce.");
        } else {
            System.out.println("The crops harvested was sold for a profit of: " + yields + ".");
        }

        yields = 0;
        previousHarvest = currentHarvest;
    }

}