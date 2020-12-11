package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.ListView;

public class TaskList {
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private Player player;
    private Game game;
    private ListView<String> taskListView = new ListView<>();

    public TaskList(Game game, Player player) {
        this.game = game;
        this.player = player;


        createTasks();
    }

    private void createTasks() {

        // Create Tasks
        tasks.add(new Task("Buy Scythe ", ""));
        tasks.add(new Task("Buy Shovel ", ""));
        tasks.add(new Task("Buy Watering Can ", ""));
        tasks.add(new Task("Buy Bag of Clover ", ""));
        tasks.add(new Task("Buy a tractor", "Unlock a 2nd field"));
        tasks.add(new Task("Buy a harvester", "Unlock a 3rd field"));
        tasks.add(new Task("Get $1000", "The grand finale!"));
        tasks.add(new Task("Go bed", "After a good days harvest, you feel tired"));

        // set first tasks active
        tasks.get(0).setActive(true);
        tasks.get(1).setActive(true);
        tasks.get(2).setActive(true);
        tasks.get(3).setActive(true);
    }

    public ObservableList<String> getTasks() {
        ObservableList<String> tempList = FXCollections.observableArrayList();


        for (Task t : tasks) {
            if (t.isActive()) {
                tempList.add(t.getDescription() + "\t" + t.getReward());
            }
        }
        return tempList;
    }

    public void update() {
        if (tasks.get(0).isActive()) {
            if (player.itemOwned(ItemName.SCYTHE)) {
                //Task completed, grant reward

                //hide the task
                tasks.get(0).setActive(false);
            }
        }
        if (tasks.get(1).isActive()) {
            if (player.itemOwned(ItemName.SHOVEL)) {
                //Task completed, grant reward

                //hide the task
                tasks.get(1).setActive(false);
                //give next task

            }
        }
        if (tasks.get(2).isActive()) {
            if (player.itemOwned(ItemName.WATER_CAN)) {
                //Task completed, grant reward

                //hide the task
                tasks.get(2).setActive(false);
                //give next task

            }
        }
        if (tasks.get(3).isActive()) {
            if (player.itemOwned(ItemName.BAG_OF_CLOVER)) {
                //Task completed, grant reward

                //hide the task
                tasks.get(3).setActive(false);
                //give next task

            }
        }

        if(!tasks.get(0).isActive() && !tasks.get(1).isActive() && !tasks.get(2).isActive() && !tasks.get(3).isActive()){
            tasks.get(4).setActive(true);
        }

        if (tasks.get(4).isActive()) {
            if (player.itemOwned(ItemName.TRACTOR)) {
                //Task completed, grant reward
                game.unlock("field2");
                //hide the task
                tasks.get(4).setActive(false);
                //give next task
                tasks.get(5).setActive(true);
            }
        }

        if (tasks.get(5).isActive()) {
            if (player.itemOwned(ItemName.HARVESTER)) {
                //Task completed, grant reward
                game.unlock("field3");
                //hide the task
                tasks.get(5).setActive(false);
                //give next task
                tasks.get(6).setActive(true);

            }
        }

        if (tasks.get(6).isActive() && !game.getField().getIsSowed()) {
            if (player.getWallet() >= 1000) {
                //hide the task
                tasks.get(6).setActive(false);
                //give next task
                tasks.get(7).setActive(true);

            }
        }
        taskListView.setItems(getTasks());
    }

    public void createTaskListView()    {
        taskListView.setItems(getTasks());
        game.getCurrentRoom().getRoomPane().getChildren().add(this.taskListView);
        taskListView.setPrefSize(355, 107);
        taskListView.setLayoutX(911);
        taskListView.setLayoutY(14);
        taskListView.setVisible(false);
    }

    public ListView<String> getTaskListView()   {
        return taskListView;
    }

    public void nextDay() {
        if (tasks.get(7).isActive()) {
            if (player.getWallet() > 1000) {
                //Task completed, grant reward;
                game.setGameFinished(true);
                tasks.get(7).setActive(false);
            }
        }
        taskListView.setItems(getTasks());
    }
}
