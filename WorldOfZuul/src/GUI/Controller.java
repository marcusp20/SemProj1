package GUI;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    public AnchorPane pane;

    private int playerPosX = (1280 / 2);
    private int playerPosY = (832 / 2);
    private int height = 832;
    private int width = 1280;
    private Circle player = new Circle(5.0,5.2,5.1);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.getChildren().add(player);
        player.setRadius(10.0);
        player.setLayoutX(playerPosX);
        player.setLayoutY(playerPosY);
    }

    public void movePlayer(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.W) {
            playerPosY -= 5;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
            return;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            playerPosY += 5;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
            return;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            playerPosX -= 5;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
            return;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            playerPosX += 5;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
            return;
        }

    }
}
