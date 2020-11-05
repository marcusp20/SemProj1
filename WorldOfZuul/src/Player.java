import java.util.HashMap;

public class Player {
    //Attributes
    private String name;
    private double wallet = 0;
    private HashMap<String, Boolean> playerInventory;
    private Boolean noCropsOwned;


    //Constructor
    public Player(String name) {
        this.name = name;
        playerInventory = new HashMap<>();
        playerInventory.put("shovel", true);
        playerInventory.put("bagOfWheat", true);
        playerInventory.put("bagOfClover", false);
        playerInventory.put("bagOfCorn", true);
        playerInventory.put("bagOfCannabisSeeds", false);


        playerInventory.put("tractor", false);
        playerInventory.put("harvester", false);
        playerInventory.put("bagoffertilizer", true);
        playerInventory.put("pesticides", false);
        playerInventory.put("scythe", true);

        //add all items
    }

    //Method to return value of hashmap.
    public Boolean itemOwned(String item) {
        return playerInventory.get(item);
    }

    public void sellYields(double yields) {
        wallet += yields;

    }

    public boolean checkForNoCrops() {
        if (!itemOwned("bagOfWheat") && !itemOwned("bagOfClover") && !itemOwned("bagOfCorn") && !itemOwned("bagOfCannabisSeeds")) {
            noCropsOwned = true;
        } else {
            noCropsOwned = false;
        }
        return noCropsOwned;
    }

    public double checkWallet() {
        return wallet;
    }


}
