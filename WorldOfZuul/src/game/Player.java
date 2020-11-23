package game;

import game.ItemName;

import java.util.HashMap;

public class Player {
    //Attributes
    private String name;
    private double wallet = 750;
    private HashMap<ItemName, Boolean> playerInventory; //TODO can be a set
    private boolean noCropsOwned;

    //Constructor
    public Player(String name) {
        this.name = name;

        //Initialize the players inventory to hold no items.
        playerInventory = new HashMap<>();
        for(ItemName itemName: ItemName.values()) {
            playerInventory.put(itemName, false);
        }

    }

    //Method to return value of hashmap.
    public boolean itemOwned(ItemName item) {
        return playerInventory.get(item);
    }

    public HashMap<ItemName, Boolean> getPlayerInventory() {
        return playerInventory;
    }

    public double getWallet() {
        return wallet;
    }

    public boolean checkForNoCrops() {
        if (!itemOwned(ItemName.BAG_OF_WHEAT) && !itemOwned(ItemName.BAG_OF_CLOVER) && !itemOwned(ItemName.BAG_OF_CORN) && !itemOwned(ItemName.BAG_OF_CANNABIS)) {
            noCropsOwned = true;
        } else {
            noCropsOwned = false;
        }
        return noCropsOwned;
    }

    public double checkWallet() {
        return wallet;
    }

    /**
     * @param input Adds money to input (Use negative for subtraction)
     * @return false if balance would become negative.
     */
    public boolean addWallet(double input) {
        if (this.wallet + input < 0) { // This does not allow a large negative
                                       // balance to have a small positive input added
            return false;
        }
        this.wallet += input;
        return true;
    }

    public void sellYields(double yields) {
        wallet += yields;
    }
}
