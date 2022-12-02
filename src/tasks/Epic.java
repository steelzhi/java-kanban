package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, "NEW");
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTaskId(int subTaskId) {
        subTasksIds.add(subTaskId);
    }

    public void addAllSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    public void removeSubTaskId(int subTaskId) {
        subTasksIds.remove(Integer.valueOf(subTaskId));
    }

    public void removeAllSubTasksIds() {
        subTasksIds.clear();
    }

    @Override
    public String toString() {
        return "id - \"" + id + "\", название - \"" + super.name + "\", описание - \"" + super.description
                + "\", статус - \"" + super.status + "\". " + "ID входящих в эпик подзадач: " + subTasksIds + "\n";
    }
}
