public class Task {
    private String description;
    private String reward;
    private boolean active;
    private boolean visible;

    public Task(String description, String reward) {
        this.description = description;
        this.reward = reward;
        this.active = false;
        this.visible = false;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
