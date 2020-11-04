public class Field extends Interactable {
    //Attributes
    private Boolean isReadyToHarvest = false;
    private Boolean isSowed = false;
    private int soilQuality = 50; //Decides the wield
    private Boolean isPesticidesUsed = false; //Bool to check for pesticides, Value is reset everytime a new harvest is done.'
    private Boolean pests = false;
    private int grainFieldCounter;

    private int fertilizerCounter;
    private int pesticidesCounter;
    private int waterCounter;
    private int harvestCounter;
    private double yields;


    public Field(CommandWords commandWords) {
        super(commandWords);
    }

    //Method used to showInfo after Sow methods has been called. Used to check if we get the expected values.
    public String showInfo() {
        return ("Pest: " + isPesticidesUsed + ", HarvVal: "  + ", SoilQual: " + soilQuality + ", IsReadyToHarv? " + isReadyToHarvest);
    }


    //Sow Methods. Maybe the isPesticidesU sed checker ought to be in the harvestFieldMethod, as we can still use pests after sowing...
    public void sowFieldTractor() {
        isSowed = true;
        System.out.println("You used the Tractor to sow");
    }

    public void sowFieldShovel() {
        isSowed = true;
        System.out.println("You used the shovel to sow");
    }


    public void useFertilizerBeforeSow() {
        if (fertilizerCounter < 2) {
            yields += 15;
            fertilizerCounter += 1;
            System.out.println("Used fertilizer, the soil is MOIST and fresh, ready for sowing!");

        } else {
            yields -= 10;
            fertilizerCounter += 1;
            if (fertilizerCounter >= 3) {
                System.out.println("Too much fertilizer used, try sowing other crops after harvest...");
            }

        }

    }

    public void useFertilizerAfterSow() {
        fertilizerCounter +=1;
        System.out.println("Try to use fertilizers before sowing next time...");
    }

    public void waterField() {
        isReadyToHarvest = true;
        if (waterCounter < 2) {
            yields += 10;
            waterCounter++;
            System.out.println("Your field has been watered, and is ready for harvest");
        } else {
            yields -= 10;
            waterCounter++;
            System.out.println("Are you trying to make your field into a pool?");
            if (waterCounter >= 3) {
                System.out.println("You're watering too much!");
                if (yields < -100) {
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

    //method to return value of isReadyToHarvest
    public Boolean getIsReadyToHarvest() {
        return isReadyToHarvest;
    }

    public Boolean getIsSowed() {
        return isSowed;
    }

    public double getYield() {
        return yields;
    }

    public void harvestDone() {
        isReadyToHarvest = false;
        isSowed = false;
        waterCounter = 0;
        yields = 0;
        harvestCounter++;

    }


}