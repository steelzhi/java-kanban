package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds = new ArrayList<>();
    private LocalDateTime endTime;

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
        return TaskTypes.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasksIds=" + subTasksIds +
                ", id=" + getId() +
                ", type=" + getType() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
