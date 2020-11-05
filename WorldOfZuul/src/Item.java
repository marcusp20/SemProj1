public class Item {

    private ItemName name;

    // Item Constructor
    public Item(ItemName name) {
        this.name = name;

    }

    public String getName() {
        return name.getItemNameString();
    }

    public String getDescription() {
        return name.getDescription();
    }

    public int getPrice() {
        return name.getPrice();
    }
    public ItemName getEnum() {
        return this.name;
    }
}
