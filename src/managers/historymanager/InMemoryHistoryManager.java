package managers.historymanager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> viewedTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (viewedTasks.size() == 10) {
            ArrayList<Task> temporaryListForViewedTasks = new ArrayList<>();
            for (int i = 1; i < viewedTasks.size(); i++) {
                temporaryListForViewedTasks.add(viewedTasks.get(i));
            }
            viewedTasks.clear();
            viewedTasks.addAll(temporaryListForViewedTasks);
        }
        viewedTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}
