package game;

import interactable.Interactable;
import interactable.NPC;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashMap;

public class Room {
    private String description;
    private HashMap<String, Room> exits;
    private NPC npc;
    private boolean isLocked;
    private Pane roomPane;
    private ArrayList<Interactable> interactables = new ArrayList<>();

    private Label feedbackText;


    public Room(String description) {
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

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    //Method could be deleted.
    public String getShortDescription() {
        return description;
    }

    public String getLongDescription() {
        return "You are " + description + ".\n" + getExitString();
    }

    private String getExitString() {
        StringBuilder returnString = new StringBuilder("Exits:");
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString.append(" ").append(exit);
        }
        if (this.npc != null) {
            returnString.append("\n");
            returnString.append(npc.getName()).append(" stands in the room.");
        }
        return returnString.toString();
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public void setRoomPane(Pane pane) {
        feedbackText = new Label(" DUMMY TEXT DATA HERE");
        feedbackText.setLayoutX(429);
        feedbackText.setLayoutY(520);
        feedbackText.setTextFill(Color.web("#FFFFFF"));
        feedbackText.setWrapText(true);
        feedbackText.setPrefSize(350, 150);
        feedbackText.setFont(new Font("Arial", 9));
        pane.setPrefSize(1280, 832); //720
        pane.getChildren().addAll(feedbackText);


        this.roomPane = pane;
    }

    public Pane getRoomPane() {
        return this.roomPane;
    }

    public void addInteractable(Interactable i) {
        this.interactables.add(i);
        this.roomPane.getChildren().add(i.getImageView());

    }

    public Label getFeedbackText() {
        return feedbackText;
    }

    public ArrayList<Interactable> getInteractables() {
        return interactables;
    }

}

