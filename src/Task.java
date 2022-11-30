public class Task {
    private String name;
    private String description;
    private String status;
    private int taskId;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "название - \"" + name + "\", описание - \"" + description + "\", статус - \"" + status + "\"";
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
