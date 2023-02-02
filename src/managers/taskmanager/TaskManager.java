package managers.taskmanager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    ArrayList<Task> getTasks();

    void removeAllTasks();

    Task findTaskById(int id);

    int addTask(Task task);

    Task updateTask(int taskId, Task task);

    void removeTask(int id);

    Task getTask(int taskId);

    // Методы для работы с эпиками:
    ArrayList<Epic> getEpics();

    void removeAllEpics();

    Epic findEpicById(int epicId);

    int addEpic(Epic epic);

    Epic updateEpic(int epicId, Epic epic);

    void removeEpic(int id);

    ArrayList<SubTask> getSubTasksInEpic(int epicId);

    Epic getEpic(int epicId);

    // Методы для работы с подзадачами:
    ArrayList<SubTask> getAllSubTasks();

    void removeAllSubTasks();

    SubTask findSubTaskById(int subTaskId);

    int addSubTask(SubTask subTask);

    SubTask updateSubTask(int subTaskId, SubTask subTask);

    void removeSubTask(int id);

    List<Task> getHistory();

    SubTask getSubTask(int subTaskId);

    TreeSet<Task> getPrioritizedTasks();

    boolean doesTasksCrossingExist();
}
