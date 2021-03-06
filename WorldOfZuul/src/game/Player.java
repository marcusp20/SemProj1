package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Player {
    //Attributes
    private String name;
    private HashMap<ItemName, Integer> playerInventory;
    private boolean noCropsOwned;
    private double wallet = //The player have to afford these 4 items at the start of the game.
            ItemName.SCYTHE.getPrice() +
            ItemName.SHOVEL.getPrice() +
            ItemName.WATER_CAN.getPrice() +
            ItemName.BAG_OF_CLOVER.getPrice() + 1000;

    //Gui attributes
    ImageView playerSprite;

    private int walkingTimer = 0;

    //Animated Images
    //Up
    Image standUpImage;
    Image walkUpImage;
    //Left
    Image standLeftImage;
    Image walkLeftImage;
    //Right
    Image standRightImage;
    Image walkRightImage;
    //Down
    Image standDownImage;
    Image walkDownImage;

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
        addPlayerImages();
        setPlayerSprite();
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
     * @param amount Adds money to input (Use negative for subtraction)
     * @return false if balance would become negative.
     */
    public boolean addWallet(double amount) {
        if (this.wallet + amount < 0) { // This does not allow a large negative
            // balance to have a small positive input added
            return false;
        }
        this.wallet += amount;
        return true;
    }

    public void forceAddWallet(double amount) {
        this.wallet += amount;
    }

    public String getInventoryToString() {
        StringBuilder inventory = new StringBuilder();
        Iterator<Map.Entry<ItemName, Integer>> entry = playerInventory.entrySet().iterator();
        boolean isFirstItem = true;
        while (entry.hasNext()) {
            Map.Entry<ItemName, Integer> curEntry = entry.next();

            if (curEntry.getValue() == 1) {
                if(isFirstItem) {
                    isFirstItem = false;
                } else {
                    inventory.append(", ");
                }
                inventory.append(curEntry.getKey().toString());
            } else if (curEntry.getValue() > 1) {
                if(isFirstItem) {
                    isFirstItem = false;
                } else {
                    inventory.append(", ");
                }
                inventory.append(curEntry.getValue() + "X " + curEntry.getKey().toString());
            }
        }

        if (inventory.isEmpty()) {
            return "Nothing in inventory, go to the shop to buy items";
        }
        return "Inventory: "
                + inventory + ".";
    }

    private void addPlayerImages()  {
        try {
            //Up
            standUpImage = FileLoader.loadImage("FarmerSpriteBackStanding.png");
            walkUpImage = FileLoader.loadImage("FarmerSpriteBackWalk.png");
            //Left
            standLeftImage = FileLoader.loadImage("FarmerSpriteLeftStanding.png");
            walkLeftImage = FileLoader.loadImage("FarmerSpriteLeftWalking.png");
            //Right
            standRightImage = FileLoader.loadImage("FarmerSpriteRightStanding.png");
            walkRightImage = FileLoader.loadImage("FarmerSpriteRightWalk.png");
            //Down
            standDownImage = FileLoader.loadImage("FarmerSpriteFrontStanding.png");
            walkDownImage = FileLoader.loadImage("FarmerSpriteFrontWalk.png");

        } catch (FileNotFoundException e)  {
            System.out.println("Error creating player image" + e);
        }
    }

    private void setPlayerSprite() {
        this.playerSprite = new ImageView(standDownImage);

        this.playerSprite.setFitHeight(180);
        this.playerSprite.setPreserveRatio(true);

        this.playerSprite.setX(600);
        this.playerSprite.setY(400);
        this.playerSprite.setCache(true); //Add moving images to cache - improves performance
    }

    //Change image based on movement
    public void checkDirection() {

        if(Math.max(westSpeed, eastSpeed) > Math.max(northSpeed, southSpeed))   {
            if (westSpeed > eastSpeed) {
                if(walkingTimer < 20) {
                    playerSprite.setImage(standRightImage);
                } else  {
                    playerSprite.setImage(walkRightImage);
                }
            } else if (westSpeed < eastSpeed) {
                if(walkingTimer < 20) {
                    playerSprite.setImage(standLeftImage);
                } else  {
                    playerSprite.setImage(walkLeftImage);
                }
            }
            walkingTimer++;
        } else if(Math.max(westSpeed, eastSpeed) < Math.max(northSpeed, southSpeed))  {
            if (southSpeed > northSpeed) {
                if(walkingTimer < 20) {
                    playerSprite.setImage(standDownImage);
                } else  {
                    playerSprite.setImage(walkDownImage);
                }
            } else if (southSpeed < northSpeed) {
                if(walkingTimer < 20) {
                    playerSprite.setImage(standUpImage);
                } else  {
                    playerSprite.setImage(walkUpImage);
                }
            }
            walkingTimer++;
        }
        if(walkingTimer > 40) walkingTimer = 0;
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
