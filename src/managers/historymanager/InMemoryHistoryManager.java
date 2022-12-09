package managers.historymanager;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final LinkedList<Task> viewedTasks = new LinkedList<>();
    private static final int MAX_NUMBER_OF_VIEWED_TASKS = 10;

    @Override
    public void add(Task task) {
        if (viewedTasks.size() == MAX_NUMBER_OF_VIEWED_TASKS) {
            viewedTasks.removeFirst();
        }

        viewedTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}
