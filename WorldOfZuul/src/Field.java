public class Field extends Interactable {
    //Attributes
    private Boolean isReadyToHarvest;
    private int harvestValue;
    private int soilQuality;



    public Field(CommandWords commandWords) {
        super(commandWords);

    }


    public void usePesticides() {


        //Soil quality - 1.
        //Harvest quality + 1 hvis
        //Hvis quality allerede er 3, skal den ikke lægge mere sammen.
    }

    public void harvestField() {
        //If isReadyToHarvest is True, proceed
        //Afhængeigt af hvilke værdi'er vores harvestQuality er (1/3) bestemmes vores udbetalte monetos.
    }

    public int showInfo() {
        return soilQuality;
    }

}
