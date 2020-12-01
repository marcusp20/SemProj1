package interactable;

public class Shop extends Interactable {
    public void removeItem(String word)    {
        for(int i = 0; i < this.getCommandList().getItems().size(); i++ )   {
            if(this.getCommandList().getItems().get(i).equals("Buy " + word))    {
                System.out.println("Removed");
                this.getCommandList().getItems().remove(i);
            }
        }
    }
}
