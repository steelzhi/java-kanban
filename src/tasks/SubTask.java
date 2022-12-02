package tasks;

public class SubTask extends Task{
    private int epicId;

    public SubTask(String name, String description, String status, int epicId) {
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
    public String toString() {
        return "id - \"" + id + "\", название - \"" + super.name + "\", описание - \"" + super.description
                + "\", статус - \"" + super.status + "\". " + "ID эпика, в который входит подзадача, - "
                + epicId + "\n";
    }
}
