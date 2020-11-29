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

class ButtonCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button("->");
    String lastItem;

    public ButtonCell(Game game, HashMap<String, Command> hM) {
        super();
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(e -> {
            System.out.println(lastItem + " : " + e);
            Command command = hM.get(lastItem);
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
            label.setText(item != null ? item : "<null>");
            setGraphic(hbox);
        }
    }
}

/*
    //TEST CODE, delete when code works
        game = new Game();

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 300, 150);
        stage.setScene(scene);
        ObservableList<String> list = FXCollections.observableArrayList(
                "use npc", "use bed");
        ListView<String> lv = new ListView<>(list);
        HashMap<String, Command> commands = new HashMap();
        commands.put("use npc", new Command(CommandWord.USE, "npc"));
        commands.put("use bed", new Command(CommandWord.USE, "bed"));
        Callback<ListView<String>, ListCell<String>> customCellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new ButtonCell(game, commands);
            }
        };
        lv.setCellFactory(customCellFactory);
        pane.getChildren().add(lv);
        stage.show();
 */

