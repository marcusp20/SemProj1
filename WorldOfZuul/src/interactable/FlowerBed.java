package interactable;

import game.CommandWords;

public class FlowerBed extends Interactable {
    private int flowerBeds[] = new int[6];  //Each index is a flower bed, int describes flowers quality
    private int pesticides = 0;

    FlowerBed(CommandWords commandWords)    {
        super(commandWords);
    }

    private void setFlower(int bed, int quality) {
        this.flowerBeds[bed] = quality;;
    }

    private int[] getFlowers()   {
        return this.flowerBeds;
    }

    public int getPesticides() {
        return this.pesticides;
    }
}
