public class Field extends Interactable {
    //Attributes
    private Boolean isReadyToHarvest = false;
    private int harvestValue = 0; //Value ranging from 1-5.
    private int soilQuality = 1;
    private Boolean isPesticidesUsed = false; //Bool to check for pesticides, Value is reset everytime a new harvest is done.





    public Field(CommandWords commandWords) {
        super(commandWords);


    }

    //Method used to showInfo after Sow methods has been called. Used to check if we get the expected values.
    public String showInfo() {
        return ("Pest: " + isPesticidesUsed + ", HarvVal: " + harvestValue + ", SoilQual: " + soilQuality + ", IsReadyToHarv? " + isReadyToHarvest);
    }



    //Sow Methods. Maybe the isPesticidesUsed checker ought to be in the harvestFieldMethod, as we can still use pests after sowing...
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
        System.out.println("Field has been sowed with the tractor and is soon ready to be harvested UwU");
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
        System.out.println("Field has been sowed with the shovel and is soon ready to be harvested UwU");

    }

    //Method for harvesting the field.
    public void harvestField() {


    }


    //method to return value of isReadyToHarvest
    public Boolean getIsReadyToHarvest() {
        return isReadyToHarvest;
    }
}