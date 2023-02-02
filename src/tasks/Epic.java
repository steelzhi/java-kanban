package tasks;

import java.time.Instant;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds = new ArrayList<>();
    private TaskTypes type = TaskTypes.EPIC;
    private Instant endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTaskId(int subTaskId) {
        subTasksIds.add(subTaskId);
    }

    public void addAllSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds.addAll(subTasksIds);
    }

    public void removeSubTaskId(int subTaskId) {
        subTasksIds.remove(Integer.valueOf(subTaskId));
    }

    public void removeAllSubTasksIds() {
        subTasksIds.clear();
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasksIds=" + subTasksIds +
                ", id=" + getId() +
                ", type=" + type +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    @Override
    public int getDuration() {
        if (getStartTime() == null || getEndTime() == null) {
            return 0;
        }

        return (int) ((getEndTime().getEpochSecond() - getStartTime().getEpochSecond()) / 60);
    }
}
