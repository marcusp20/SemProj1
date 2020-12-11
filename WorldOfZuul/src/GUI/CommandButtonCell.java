package GUI;

import game.Command;
import game.Game;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

class CommandButtonCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button("->");
    String lastItem;

    //Create new cell with a label and button based on hashmap.
    public CommandButtonCell(Game game, HashMap<String, Command> commandHashMap) {
        super();
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(e -> {
            System.out.println(lastItem + " : " + e);
            Command command = commandHashMap.get(lastItem);
            game.processCommand(command);
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
            //Check for null
            label.setText(item != null ? item : "null");
            setGraphic(hbox);
        }
    }

    public void labelOutputStart() {
        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream
        System.out.println("...");
        // Put things back
        System.out.flush();
        System.setOut(old);
        // Show what happened
        System.out.println("Here: " + baos.toString());
    }

}


