package interactable;

public class Shop extends Interactable {
    public void removeItem(String word)    {
        if(this.getCommandList() != null) {
            for (int i = 0; i < this.getCommandList().getItems().size(); i++) {
                if (this.getCommandList().getItems().get(i).contains(word)) {
                    this.getCommandList().getItems().remove(i);
                    break;
                }
            }
        }
    }
}
