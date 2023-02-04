package managers;

import managers.historymanager.HistoryManager;
import managers.historymanager.InMemoryHistoryManager;
import managers.taskmanager.TaskManager;
import managers.taskmanager.memorymanager.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
