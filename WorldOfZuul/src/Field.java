public class Field extends Interactable {
    //Attributes
    private Boolean isReadyToHarvest = false;
    private Boolean isSowed = false;
    private int soilQuality = 50; //Decides the wield
    private Boolean isPesticidesUsed = false; //Bool to check for pesticides, Value is reset everytime a new harvest is done.'
    private Boolean insects = false;
    private Boolean water = false;
    private int fertilizerCounter;
    private int pesticidesCounter;
    private int waterCounter;
    private int harvestCounter;
    private int yields;


    public Field(CommandWords commandWords) {
        super(commandWords);
    }

    //Method used to showInfo after Sow methods has been called. Used to check if we get the expected values.
    public String showInfo() {
        return ("Pest: " + isPesticidesUsed + ", HarvVal: "  + ", SoilQual: " + soilQuality + ", IsReadyToHarv? " + isReadyToHarvest);
    }


    //Sow Methods. Maybe the isPesticidesU sed checker ought to be in the harvestFieldMethod, as we can still use pests after sowing...
    public void sowFieldTractor() {
        System.out.println("You used the Tractor to sow");
    }

    public void sowFieldShovel() {
        isSowed = true;
        System.out.println("You used the shovel to sow");
    }


    public void useFertilizerBeforeSow() {
        if (fertilizerCounter < 5) {
            yields += 20;
            fertilizerCounter += 1;
        } else {
            yields -= 10;
            fertilizerCounter += 1;
        }
        System.out.println("Used fertilizer, the soil is MOIST and fresh!");

    }

    public void useFertilizerAfterSow() {
        fertilizerCounter +=1;
        System.out.println("Try to use fertilizers before sowing next time...");
    }

    public void waterField() {
        isReadyToHarvest = true;
        if (waterCounter < 3) {
            yields += 10;
            waterCounter++;
        } else {
            yields -= 10;
            waterCounter++;
        }
        System.out.println("Field has been watered, your wield migt increase, or not...");
    }



    //method to return value of isReadyToHarvest
    public Boolean getIsReadyToHarvest() {
        return isReadyToHarvest;
    }

    public Boolean getIsSowed() {
        return isSowed;
    }

    public int getYield() {
        return yields;
    }

    public void harvestDone() {
        isReadyToHarvest = false;
        waterCounter = 0;
        yields = 10;
    }


}