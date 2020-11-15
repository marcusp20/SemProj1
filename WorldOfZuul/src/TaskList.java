import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;
    private Player player;
    private Game game;

    public  TaskList(Game game, Player player) {
        this.game = game;
        this.player = player;
        tasks = new ArrayList<>();

        createTasks();
    }

    private void createTasks() {
        tasks.add(new Task("Get $100", "Unlock your garden"));
        tasks.get(0).setActive(true);
        tasks.get(0).setVisible(true);

        tasks.add(new Task("Buy a tractor", "Unlock a 2nd field"));
        tasks.add(new Task("Buy a harvester", "Unlock a 3rd field"));
    }



    public List<Task> getTasks() {
        return tasks;
    }

    public void update() {
        if(tasks.get(0).isActive()) {
            if(player.getWallet() >= 100) {
                //Task completed, grant reward
                game.unlock("garden");
                //hide the task
                tasks.get(0).setVisible(false);
                tasks.get(0).setActive(false);
                //give next task
                tasks.get(1).setActive(true);
                tasks.get(1).setVisible(true);
            }
        }

        if(tasks.get(1).isActive()) {
            if(player.itemOwned(ItemName.TRACTOR)) {
                //Task completed, grant reward
                game.unlock("field2");
                //hide the task
                tasks.get(1).setVisible(false);
                tasks.get(1).setActive(false);
                //give next task
                tasks.get(2).setActive(true);
                tasks.get(2).setVisible(true);
            }
        }

        if(tasks.get(2).isActive()) {
            if(player.itemOwned(ItemName.HARVESTER)) {
                //Task completed, grant reward
                game.unlock("field3");
                //hide the task
                tasks.get(2).setVisible(false);
                tasks.get(2).setActive(false);
                //no more tasks
                /*tasks.get(2).setActive(true);
                tasks.get(2).setVisible(true);*/
            }
        }
    }
}
