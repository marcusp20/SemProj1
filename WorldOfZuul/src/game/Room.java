package game;

import interactable.Interactable;
import interactable.NPC;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;

public class Room {
    private String description;
    private HashMap<String, Room> exits;
    private NPC npc;
    private boolean isLocked;
    private Pane roomPane;
    private ArrayList<Interactable> interactables = new ArrayList<>();

    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    //Method could be deleted.
    public String getShortDescription()
    {
        return description;
    }

    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    private String getExitString()
    {
        StringBuilder returnString = new StringBuilder("Exits:");
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString.append(" ").append(exit);
        }
        if(this.npc != null) {
            returnString.append("\n");
            returnString.append(npc.getName()).append(" stands in the room.");
        }
        return returnString.toString();
    }

    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    public void setRoomPane(Pane pane)    {
        pane.setPrefSize(1280,832); //720

        this.roomPane = pane;
    }

    public Pane getRoomPane()   {
        return this.roomPane;
    }

    public void addInteractable(Interactable i)   {
        this.interactables.add(i);
        this.roomPane.getChildren().add(i.getImageView());
    }

    public ArrayList<Interactable> getInteractables()   {
        return interactables;
    }

}

