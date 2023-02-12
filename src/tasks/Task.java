package tasks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;

    private int duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public TaskTypes getType() {
        return TaskTypes.TASK;
    }

    public int getEpicId() {
        return 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + getType() +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass() || obj == null) {
            return false;
        }

        Task task = (Task) obj;

        boolean doesStartTimeEquals =
                this.startTime == null ? (task.startTime == null ? true : false)
                : (task. startTime == null ? false : this.getStartTime().equals(task.getStartTime()) ? true : false);

        return this.getName().equals(task.getName())
                && this.getDescription().equals(task.getDescription())
                && this.getStatus() == task.getStatus()
                && this.getId() == task.getId()
                && this.getType() == task.getType()
                && this.getEpicId() == task.getEpicId()
                && doesStartTimeEquals
                && this.getDuration() == task.getDuration();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == 0) {
            return null;
        }

        return startTime.plusMinutes(duration);
    }
}
