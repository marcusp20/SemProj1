package GUI;

import game.Game;
import game.Player;
import interactable.Interactable;
import javafx.scene.image.ImageView;

public class MovementHandler {
    private Game game;
    private Player player;
    private ImageView playerSprite;
    public boolean w, a, s, d;

    public MovementHandler(Game game, Player player, ImageView playerSprite) {
        this.game = game;
        this.player = player;
        this.playerSprite = playerSprite;
    }

    //Move player
    public void move() {
        game.getPlayer().setPrevX(playerSprite.getX());
        game.getPlayer().setPrevY(playerSprite.getY());

        if (w) {
            if (player.getNorthSpeed() != 8) {
                player.setNorthSpeed(player.getNorthSpeed() + 1);
            }
        } else {
            if (player.getNorthSpeed() != 0) {
                player.setNorthSpeed(player.getNorthSpeed() - 1);
            }
        }
        if (a) {
            if (player.getEastSpeed() != 8) {
                player.setEastSpeed(player.getEastSpeed() + 1);
            }
        } else {
            if (player.getEastSpeed() != 0) {
                player.setEastSpeed(player.getEastSpeed() - 1);
            }
        }
        if (s) {
            if (player.getSouthSpeed() != 8) {
                player.setSouthSpeed(player.getSouthSpeed() + 1);
            }
        } else {
            if (player.getSouthSpeed() != 0) {
                player.setSouthSpeed(player.getSouthSpeed() - 1);
            }
        }
        if (d) {
            if (player.getWestSpeed() != 8) {
                player.setWestSpeed(player.getWestSpeed() + 1);
            }
        } else {
            if (player.getWestSpeed() != 0) {
                player.setWestSpeed(player.getWestSpeed() - 1);
            }
        }

        playerSprite.setY(playerSprite.getY() - player.getNorthSpeed() + player.getSouthSpeed());
        playerSprite.setX(playerSprite.getX() - player.getEastSpeed() + player.getWestSpeed());
    }

    public void checkCollision() {
        if(game.getCurrentRoom().getRoomCollisions().intersectsWith(player)) {
            //System.out.println("IT'S WORKING! IT'S WOoORRrRKING!");
        }
        for (Interactable i : game.getCurrentRoom().getInteractables()) {
            //Code for intractable collision
            boolean playerIntersectsInteractable = i.getImageView().intersects(playerSprite.getLayoutBounds());
            if (playerIntersectsInteractable) {

                //Moves player to previous position if intersecting with intractable, still allows other movement
                boolean isMovingHorizontal = player.getWestSpeed() > 0 || player.getEastSpeed() > 0;
                if (isMovingHorizontal) {
                    double curX = playerSprite.getX();

                    playerSprite.setX(game.getPlayer().getPrevX());
                    if (i.getImageView().intersects(playerSprite.getLayoutBounds())) {
                        playerSprite.setX(curX);
                        if (game.getPlayer().getNorthSpeed() > 0 || game.getPlayer().getSouthSpeed() > 0) {
                            playerSprite.setY(game.getPlayer().getPrevY());
                        }
                    }
                }
                if (i.getImageView().intersects(playerSprite.getLayoutBounds())) {  //Always true?
                    if (game.getPlayer().getNorthSpeed() > 0 || game.getPlayer().getSouthSpeed() > 0) {
                        playerSprite.setY(game.getPlayer().getPrevY());
                        if (i.getImageView().intersects(playerSprite.getLayoutBounds())) {
                            if (game.getPlayer().getWestSpeed() > 0 || game.getPlayer().getEastSpeed() > 0) {
                                playerSprite.setX(game.getPlayer().getPrevX());
                            }
                        }
                    }
                }
            }
        }
    }

    public void haltPlayerMovement() {
        w = false;
        a = false;
        s = false;
        d = false;
        player.setEastSpeed(0);
        player.setNorthSpeed(0);
        player.setWestSpeed(0);
        player.setSouthSpeed(0);
    }
}
