package tasks;

import java.time.Instant;
import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;
    private TaskTypes type = TaskTypes.TASK;
    private int epicId = -1;

    private int duration;
    private Instant startTime;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        if (status == Status.IN_PROGRESS || status == Status.DONE)
            this.status = status;
        else this.status = Status.NEW;
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
        return type;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + type +
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
        return this.getName().equals(task.getName()) &&
                this.getDescription().equals(task.getDescription()) &&
                this.getStatus() == task.getStatus() &&
                this.getId() == task.getId() &&
                this.getType() == task.getType() &&
                this.getEpicId() == task.getEpicId() &&
                this.getStartTime() == task.getEndTime() &&
                this.getDuration() == task.getDuration();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        if (startTime == null || duration == 0) {
            return null;
        }

        return startTime.plusSeconds(duration * 60);
    }
}
