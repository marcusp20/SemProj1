package interactable;

import game.CommandWords;

public class FlowerBed extends Interactable {
    int flowerBeds[] = new int[6];
    FlowerBed(CommandWords commandWords)    {
        super(commandWords);
    }

    private void setFlower(int bed, String flower) {
        int fNum = -1;
        switch(flower){
            case "Dandelion":
                fNum = 0;
                break;
            case "Poppy":
                fNum = 1;
                break;
            case "Snapdragon":
                fNum = 2;
                break;
            case "Sun flower":
                fNum = 3;
                break;
            case "Buttercup":
                fNum = 4;
                break;
            case "Tulip":
                fNum = 5;
                break;
        }
        if(fNum != -1)   {
            this.flowerBeds[bed] = fNum;
        }
    }

    private String getFlower(int bed)   {
        switch(bed){
            case 0:
                return "Dandelion";
            case 1:
                return "Poppy";
            case 2:
                return "Snapdragon";
            case 3:
                return "Sun flower";
            case 4:
                return "Buttercup";
            case 5:
                return "Tulip";
            default:
                return "Invalid flower";
        }
    }
}
