package game;

public class Task {
    private String description;
    private String reward;
    private boolean active;

    public Task(String description, String reward) {
        this.description = description;
        this.reward = reward;
        this.active = false;
    }

    public String getDescription() {
        return description;
    }

    public String getReward() {
        return reward;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
