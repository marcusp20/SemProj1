import java.util.HashMap;

public class Player {
    //Attributes
    private String name;
    private int wallet = 0;
    private HashMap<String, Boolean> playerInventory;

    //Constructor
    public Player(String name) {
        this.name = name;
        playerInventory = new HashMap<>();
        playerInventory.put("shovel", true);
        playerInventory.put("bagofseeds", false);
        playerInventory.put("watering can", false);
        playerInventory.put("tractor", false);
        playerInventory.put("harvester", false);
        playerInventory.put("bagoffertilizer", false);
        playerInventory.put("pesticides", false);

        //add all items
    }

    //Method to return value of hashmap.
    public Boolean itemOwned(String item) {
        return playerInventory.get(item);
    }


}
