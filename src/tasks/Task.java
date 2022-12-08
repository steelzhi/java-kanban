package tasks;

import status.Status;

public class Task {
    String name;
    String description;
    Status status;
    int id;

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

    @Override
    public String toString() {
        return "Тип - задача, id - \"" + id + "\", название - \"" + name + "\", описание - \"" + description
                + "\", статус - \"" + status + "\"\n";
    }
}
