public enum ItemName { //TODO update prices
    SHOVEL("Shovel", "for plowing the field.", 0),
    WATER_CAN("Watering can", "to water crops.", 0),
    TRACTOR("Tractor", "for fertilizing, plowing, sowing, and more.", 5000),
    HARVESTER("Harvester", "Used for harvesting", 7000),
    BAG_OF_FERTILIZER("Bag of fertilizer", "used for fertilizing", 200),
    PESTICIDES("pesticides", "used for destroying water-collection", 150),
    SCYTHE("scythe", "for manual harvesting", 0),
    SOIL_SAMPLE_COLLECTOR("Soil Sample collector", "to collects soil samples, and test it.", 750),
    BAG_OF_CANNABIS("Bag of Cannabis", "holds seeds for planting 420", 10),
    BAG_OF_WHEAT("Bag of Wheat", "holds seeds for planting", 50),
    BAG_OF_CORN("Bag of Corn", "holds seeds for planting", 50),
    BAG_OF_CLOVER("Bag of Clover", "holds seeds for planting", 0);


    private String itemNameString;
    private String description;
    private int price;

    ItemName(String itemNameString, String description, int price) {
        this.itemNameString = itemNameString;
        this.description = description;
        this.price = price;
    }

    public String toString() {
        return itemNameString;
    }

    public String getItemNameString() {
        return itemNameString;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
