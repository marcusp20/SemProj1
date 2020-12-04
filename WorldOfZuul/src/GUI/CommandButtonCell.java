package GUI;

import game.Command;
import game.Game;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.HashMap;

/*
AHHHHHHHH
https://stackoverflow.com/questions/15661500/javafx-listview-item-with-an-image-button

http://fxexperience.com/2012/05/listview-custom-cell-factories-and-context-menus/
 */

class CommandButtonCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button("->");
    String lastItem;

    public CommandButtonCell(Main main, Game game, HashMap<String, Command> commandHashMap) {
        super();
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(e -> {
            System.out.println(lastItem + " : " + e);
            Command command = commandHashMap.get(lastItem);
            game.processCommand(command);
            main.updateTask();
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            label.setText(item != null ? item : "<null>");
            setGraphic(hbox);
        }
    }
}


