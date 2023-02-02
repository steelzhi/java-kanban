package tasks;

public class SubTask extends Task {
    private int epicId;
    private TaskTypes type = TaskTypes.SUBTASK;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                ", id=" + getId() +
                ", type=" + type +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}
