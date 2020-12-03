package GUI;

import game.Player;

import java.util.ArrayList;
import java.util.List;

public class RoomCollisions {
    private List<CollisionBox> collisionBoxList;

    public RoomCollisions() {
        collisionBoxList = new ArrayList<>();
    }

    public void addCollisionBox(CollisionBox collisionBox) {
        this.collisionBoxList.add(collisionBox);
    }

    public List<CollisionBox> getCollisionBoxList() {
        return collisionBoxList;
    }

    public boolean intersectsWith(Player player) {
        double playerX = player.getPrevX();
        double playerY = player.getPrevY();
        /*return collisionBoxList.stream()
                .anyMatch(box -> playerX >= box.x && playerX <= box.x+box.width &&
                        playerY >= box.y && playerY <= box.y + box.height);
        */
        /*the above stream is semantically equivalent to this for-each loop.
        However, the stream should be slightly faster, so we chose to use it
        - regardless that it is not OOP paradime but function oriented programming paradime
        */for (CollisionBox box : collisionBoxList) {
            if(playerX >= box.x && playerX <= box.x+box.width
                    && playerY >= box.y && playerY <= box.y + box.height)
                return true;
        }
        return false;
        //*/



    }
}
