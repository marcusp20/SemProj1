public class Field extends Interactable {
    //Attributes
    private Boolean isReadyToHarvest;
    private int harvestValue;
    private int soilQuality;



    public Field(CommandWords commandWords) {
        super(commandWords);

    }

    public int showInfo() {
        return soilQuality;
    }

}
