import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
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
        for (int i = 0; i < subTasksIds.size(); i++) {
            if (subTasksIds.get(i) == subTaskId)
                subTasksIds.remove(i);
        }
    }

    public void removeAllSubTasksIds() {
        subTasksIds.clear();
    }
}
