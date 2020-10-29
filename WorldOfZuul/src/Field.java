public class Field extends Interactable {
    //Attributes
    private Boolean isReadyToHarvest = false;
    private int harvestValue = 0; //Value ranging from 1-5.
    private int soilQuality = 2;
    private Boolean isPesticidesUsed = true; //Bool to check for pesticides, Value is reset everytime a new harvest is done.


    public Field(CommandWords commandWords) {
        super(commandWords);
    }

    //Method used to showInfo after Sow methods has been called. Used to check if we get the expected values.
    public String showInfo() {
        return ("Pest: " + isPesticidesUsed + ", HarvVal: " + harvestValue + ", SoilQual: " + soilQuality + ", IsReadyToHarv? " + isReadyToHarvest);
    }


    //Sow Methods. Maybe the isPesticidesU sed checker ought to be in the harvestFieldMethod, as we can still use pests after sowing...
    public void sowFieldTractor() {
        isReadyToHarvest = true;
        if (isPesticidesUsed) {
            if (soilQuality <= 0) {
                System.out.println("You're using a lot of pesticides...");
            } else {
                soilQuality -= 1;
            }
        }
        harvestValue += 2;
        System.out.println("You used the Tractor to sow");
    }

    public void sowFieldShovel() {
        isReadyToHarvest = true;
        if (isPesticidesUsed) {
            if (soilQuality <= 0) {
                System.out.println("You're using a lot of pesticides...");
            } else {
                soilQuality -= 1;
            }
        }
        harvestValue += 1;
        System.out.println("You used the shovel to sow");
    }

    //Method for harvesting the field.
    /*

    public void harvestField() {
    }

    //Method for increasing harvestQual
    public void useFertilizer() {
    }

    //Method for measuring the soil quality
    public void measureSoil() {
    }

     */


    //method to return value of isReadyToHarvest
    public Boolean getIsReadyToHarvest() {
        return isReadyToHarvest;
    }
    public void harvestDone() {
        isReadyToHarvest = false;
        isPesticidesUsed = false;
        soilQuality -= 1;
    }
}