package game;

import GUI.RoomCollisions;
import interactable.Interactable;
import interactable.NPC;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private RoomCollisions roomCollisions;

    //Gui
    private Label feedbackText;
    private boolean hasBeenVisited;
    private Text introText;
    private Stage roomIntroStage  = new Stage();

    public Room(String description) {
        this.description = description;
        exits = new HashMap<String, Room>();
        isLocked = false;
        roomCollisions = new RoomCollisions();
        this.hasBeenVisited = false;
        this.introText = new Text("No intro text set");
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
        //creating the Label for console output
        feedbackText = createFeedbackLabel();

        pane.setPrefSize(1280, 832); //720
        pane.getChildren().add(feedbackText);
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

    public Label createFeedbackLabel() {
        feedbackText = new Label(" DUMMY TEXT DATA HERE");
        feedbackText.setLayoutX(353);
        feedbackText.setLayoutY(720);
        feedbackText.setTextFill(Color.web("#f0ffff"));
        feedbackText.setOpacity(0.92);
        feedbackText.setPrefSize(550, 110);
        feedbackText.setFont(new Font("Arial", 22));
        feedbackText.setStyle("-fx-border-color:darkgoldenrod; -fx-border-width:3; -fx-background-color:black;");
        feedbackText.setAlignment(Pos.CENTER);
        feedbackText.setOpacity(0);
        return feedbackText;
    }

    public ArrayList<Interactable> getInteractables() {
        return interactables;
    }

    public RoomCollisions getRoomCollisions() {
        return roomCollisions;
    }

    public void setRoomCollisions(RoomCollisions roomCollisions) {
        this.roomCollisions = roomCollisions;
    }

    public void openIntroWindow()    {
        this.hasBeenVisited = true;

        roomIntroStage.initModality(Modality.APPLICATION_MODAL);
        VBox textVbox = new VBox(20);
        textVbox.setStyle("-fx-font: 24 arial;");
        textVbox.setSpacing(20);
        textVbox.setBackground(new Background(new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY)));
        introText.setWrappingWidth(360);
        textVbox.getChildren().add(introText);
        Scene dialogScene = new Scene(textVbox, 400, 300);
        roomIntroStage.setScene(dialogScene);
        roomIntroStage.show();
    }

    public void setIntroText(String introText)  {
        this.introText = new Text(introText);
    }

    public boolean hasBeenVisited() {
        return hasBeenVisited;
    }
}

