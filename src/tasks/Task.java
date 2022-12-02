package tasks;

public class Task {
    String name;
    String description;
    String status;
    int id;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        if (status.equals("IN_PROGRESS") || status.equals("DONE"))
            this.status = status;
        else this.status = "NEW";
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
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
        return "id - \"" + id + "\", название - \"" + name + "\", описание - \"" + description + "\", статус - \""
                + status + "\"\n";
    }
}
