import java.util.HashMap;

public class Player {
    //Attributes
    private String name;
    private int wallet = 0;
    private HashMap<ItemName, Boolean> playerInventory;

    //Constructor
    public Player(String name) {
        this.name = name;

        //Initialize the players inventory to hold no items.
        playerInventory = new HashMap<>();
        for(ItemName itemName: ItemName.values()) {
            playerInventory.put(itemName, false);
        }

        //Player starts with a shovel
        playerInventory.put(ItemName.SHOVEL, true);
    }

    //Method to return value of hashmap.
    public Boolean itemOwned(String item) {
        return playerInventory.get(item);
    }

    public HashMap<ItemName, Boolean> getPlayerInventory() {
        return playerInventory;
    }
}
