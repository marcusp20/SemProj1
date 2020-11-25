package GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public AnchorPane pane;
    @FXML
    Button saveGameButton;

    private int playerPosX = (1280 / 2);
    private int playerPosY = (832 / 2);
    private int height = 832;
    private int width = 1280;
    private Circle player = new Circle(5.0, 5.2, 5.1);
    private int playerSpeed = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveGameButton.setOnAction(e -> {
            System.out.println("Player pos X " + player.getLayoutX());
            System.out.println("Player Pos Y " + player.getLayoutY());
        });

        pane.getChildren().add(player);
        player.setOpacity(0.0);
        player.setRadius(20.0);
        player.setLayoutX(playerPosX);
        player.setLayoutY(playerPosY);
    }

    public void movePlayer(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.W && playerPosY + playerSpeed > 316.0) {
            playerPosY -= playerSpeed;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
            return;
        }
        if (keyEvent.getCode() == KeyCode.S && playerPosY + playerSpeed < 661) {
            playerPosY += playerSpeed;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
            return;
        }
        if (keyEvent.getCode() == KeyCode.A && playerPosX + playerSpeed > 240) {
            playerPosX -= playerSpeed;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
            return;
        }
        if (keyEvent.getCode() == KeyCode.D && playerPosX + playerSpeed < 1020) {
            playerPosX += playerSpeed;
            player.setLayoutX(playerPosX);
            player.setLayoutY(playerPosY);
        }
        return;

    }

}