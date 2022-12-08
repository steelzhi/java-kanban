package tasks;

import status.Status;

public class SubTask extends Task{
    private int epicId;

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
    public String toString() {
        return "Тип - подзадача, id - \"" + id + "\", название - \"" + super.name + "\", описание - \""
                + super.description + "\", статус - \"" + super.status + "\". "
                + "ID эпика, в который входит подзадача, - " + epicId + "\n";
    }
}
