package managers;

import managers.historymanager.HistoryManager;
import managers.historymanager.InMemoryHistoryManager;
import managers.taskmanager.FileBackedTasksManager;
import managers.taskmanager.InMemoryTaskManager;
import managers.taskmanager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
