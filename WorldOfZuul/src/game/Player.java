package game;

import game.ItemName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Player {
    //Attributes
    private String name;
    private double wallet = 8;
    private HashMap<ItemName, Integer> playerInventory;
    private boolean noCropsOwned;

    //Gui attributes
    ImageView playerSprite;
    private double prevX = 0;
    private double prevY = 0;

    private int northSpeed = 0;
    private int eastSpeed = 0;
    private int southSpeed = 0;
    private int westSpeed = 0;

    //Constructor
    public Player(String name) {
        this.name = name;

        //Initialize the players inventory to hold no items.
        playerInventory = new HashMap<>();
        for (ItemName itemName : ItemName.values()) {
            playerInventory.put(itemName, 0);
        }

    }

    //Method to return value of hashmap.
    public boolean itemOwned(ItemName item) {
        return playerInventory.get(item) > 0;
    }

    public HashMap<ItemName, Integer> getPlayerInventory() {
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

    public void remove(ItemName item, int amount) {
        int currentAmount = playerInventory.get(item);
        playerInventory.put(item, currentAmount - amount);
    }

    public void removeOne(ItemName item) {
        remove(item, 1);
    }

    public void add(ItemName itemName, int amount) {
        remove(itemName, -amount);
    }

    public void addOne(ItemName item) {
        add(item, 1);
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

    public String getInventory() {
        StringBuilder inventory = new StringBuilder();
        Iterator<Map.Entry<ItemName, Integer>> entry = playerInventory.entrySet().iterator();
        while (entry.hasNext()) {
            Map.Entry<ItemName, Integer> curEntry = entry.next();
            if (curEntry.getValue() == 1) {
                inventory.append(curEntry.getKey().toString());
            } else if (curEntry.getValue() > 1) {
                inventory.append(curEntry.getValue() + "X " + curEntry.getKey().toString());
            }
            if (entry.hasNext() && curEntry.getValue() > 0) {
                inventory.append(", ");
            }
        }

        if (inventory.isEmpty()) {
            return "Nothing in inventory, go to the shop to buy items";
        }
        return "Inventory: "
                + inventory + ".";
    }

    public void setPlayerSprite(Image img) {
        this.playerSprite = new ImageView(img);

        this.playerSprite.setX(600);
        this.playerSprite.setY(400);
        this.playerSprite.setCache(true); //Add moving images to cache - improves performance
    }

    public void checkDirection() {
        if (this.getWestSpeed() > this.getEastSpeed()) {
            playerSprite.setScaleX(1);
        } else if (this.getWestSpeed() < this.getEastSpeed()) {
            playerSprite.setScaleX(-1);
        }
    }

    public ImageView getPlayerSprite() {
        return playerSprite;
    }

    public void setPrevX(double x) {
        checkDirection();
        this.prevX = x;
    }

    public void setPrevY(double y) {
        this.prevY = y;
    }

    public double getPrevX() {
        return prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public int getNorthSpeed() {
        return northSpeed;
    }

    public void setNorthSpeed(int northSpeed) {
        this.northSpeed = northSpeed;
    }

    public int getEastSpeed() {
        return eastSpeed;
    }

    public void setEastSpeed(int eastSpeed) {
        this.eastSpeed = eastSpeed;
    }

    public int getSouthSpeed() {
        return southSpeed;
    }

    public void setSouthSpeed(int southSpeed) {
        this.southSpeed = southSpeed;
    }

    public int getWestSpeed() {
        return westSpeed;
    }

    public void setWestSpeed(int westSpeed) {
        this.westSpeed = westSpeed;
    }
}
