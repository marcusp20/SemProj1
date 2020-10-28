import java.util.HashMap;

public class Player {
    //Attributes
    private String name;
    private int wallet;
    private HashMap<String, Boolean> playerInventory;

    //Constructor
    public Player() {
        playerInventory = new HashMap<>();
        playerInventory.put("shovel", false);
        playerInventory.put("bagofseeds", false);
        playerInventory.put("watering can", false);
        playerInventory.put("tractor", false);
        playerInventory.put("harvester", false);
        playerInventory.put("bagoffertilizer", false);
        playerInventory.put("pesticides", false);

        //add all items
    }

    //Methods



}
