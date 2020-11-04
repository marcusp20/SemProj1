public enum ItemNames {
    SHOVEL("Shovel"),
    BAG_OF_SEEDS("Bag of seeds"),
    WATER_CAN("Watering can"),
    TRACTOR("Tractor"),
    HARVESTER("Harvester"),
    BAG_OF_FERTILIZER("Bag of fertilizer"),
    PESTICIDES("pesticides"),
    SCYTHE("scythe");

    private String itemNameString;

    ItemNames(String commandString)
    {
        this.itemNameString = commandString;
    }

    public String toString()
    {
        return itemNameString;
    }
}
