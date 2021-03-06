package GUI;

import interactable.NPC;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.HashMap;

public class NpcButtonCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("empty");
    Pane pane = new Pane();
    Button button = new Button("->");
    String lastItem;

    //Create new cell with a label and button based on hashmap. Buttons action is different for npc buttonCell
    public NpcButtonCell(NPC npc, HashMap<String, Integer> answerMap) {
        super();
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(e -> {
            Integer i = answerMap.get(lastItem);
            npc.updateAnswer(i);
        });
    }

    //Update item in cell
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            //Check if item is null
            label.setText(item != null ? item : "null");
            setGraphic(hbox);
        }
    }
}


