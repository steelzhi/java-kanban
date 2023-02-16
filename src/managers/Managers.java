package managers;

import managers.historymanager.HistoryManager;
import managers.historymanager.InMemoryHistoryManager;
import managers.taskmanager.TaskManager;
import managers.taskmanager.filemanager.FileBackedTasksManager;
import managers.taskmanager.httpmanager.HttpTaskManager;

public class Managers {
    private static final String URL = "http://localhost:8080/";

    public static TaskManager getDefault() {
        return new HttpTaskManager(URL);
    }

    public static FileBackedTasksManager getFileBackedTasksManager() {
        final String fileName = "src\\resources\\Testing (sprint 7).csv";
        return new FileBackedTasksManager(fileName);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
