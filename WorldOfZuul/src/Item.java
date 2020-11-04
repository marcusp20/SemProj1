public class Item {

    private ItemName name;
    private String description;

    // Item Constructor
    public Item(ItemName name, String description) {
        this.name = name;
        this.description = description;

    }

    public ItemName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
