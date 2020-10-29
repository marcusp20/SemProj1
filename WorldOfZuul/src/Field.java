public class Field extends Interactable {
    //Attributes
    private Boolean isReadyToHarvest = false;
    private int harvestValue = 0; //Value ranging from 1-5.
    private int soilQuality = 1;
    private Boolean isPesticidesUsed = false; //Bool to check for pesticides, Value is reset everytime a new harvest is done.





    public Field(CommandWords commandWords) {
        super(commandWords);


    }

    public String showInfo() {
        return (isPesticidesUsed + " " + harvestValue + " " + soilQuality + " " + isReadyToHarvest);
    }

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
    }


    //method to return value of isReadyToHarvest
    public Boolean getIsReadyToHarvest() {
        return isReadyToHarvest;
    }
}
