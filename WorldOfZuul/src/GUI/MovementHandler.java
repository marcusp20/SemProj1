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
    //for (Interactable i : game.getCurrentRoom().getInteractables()) {
        //Code for intractable collision
        //boolean playerIntersectsInteractable = i.getImageView().intersects(playerSprite.getLayoutBounds());
        RoomCollisions collisionsBoxes = game.getCurrentRoom().getRoomCollisions();
        if (collisionsBoxes.intersectsWith(playerSprite.getX(), playerSprite.getY())) {

            boolean isMovingHorizontal = player.getWestSpeed() > 0 || player.getEastSpeed() > 0;
            if (isMovingHorizontal) { // The player is moving AT LEAST horizontal (Can also be moving vertical)
                double curX = playerSprite.getX(); //Attempt to fix collision - by moving the player to a
                playerSprite.setX(game.getPlayer().getPrevX()); //previous non colliding X-coordinate.
                if (collisionsBoxes.intersectsWith(playerSprite.getX(), playerSprite.getY())) { //The player is still stuck, after being moves on the X axis
                    playerSprite.setX(curX); //Then, we try to undo our fix, and move the player on the Y axis instead.
                    if (game.getPlayer().getNorthSpeed() > 0 || game.getPlayer().getSouthSpeed() > 0) {
                        playerSprite.setY(game.getPlayer().getPrevY());
                    }
                }
            }
            if (collisionsBoxes.intersectsWith(playerSprite.getX(), playerSprite.getY())) { //Then, if the player is still colliding
                boolean isMovingVertical = game.getPlayer().getNorthSpeed() > 0 || game.getPlayer().getSouthSpeed() > 0;
                if (isMovingVertical) {
                    playerSprite.setY(game.getPlayer().getPrevY()); //We try to push the player back in the Y axis to a safe position
                    if (collisionsBoxes.intersectsWith(playerSprite.getX(), playerSprite.getY())) {
                        if (game.getPlayer().getWestSpeed() > 0 || game.getPlayer().getEastSpeed() > 0) {
                            playerSprite.setX(game.getPlayer().getPrevX());
                        }
                    }
                }
            }
        }
       // }
         //copy of old collision detection - detects colission with the ImageViews of the Interactable objects
        /*for (Interactable i : game.getCurrentRoom().getInteractables()) {
            //Code for intractable collision
            if (i.getImageView().intersects(playerSprite.getLayoutBounds())) {

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
        }*/

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
