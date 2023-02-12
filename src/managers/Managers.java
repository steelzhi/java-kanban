package managers;

import managers.historymanager.HistoryManager;
import managers.historymanager.InMemoryHistoryManager;
import managers.taskmanager.TaskManager;
import managers.taskmanager.filemanager.FileBackedTasksManager;
import managers.taskmanager.httptaskmanager.HttpTaskManager;
import managers.taskmanager.memorymanager.InMemoryTaskManager;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8080/");
    }

    public static FileBackedTasksManager getFileBackedTasksManager() {
        String fileName = "src\\resources\\Testing (sprint 7).csv";
        return new FileBackedTasksManager(fileName);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
