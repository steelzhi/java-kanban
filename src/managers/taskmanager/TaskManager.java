package managers.taskmanager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    void removeAllTasks();

    Task findTaskById(int id);

    int addTask(Task task);

    Task updateTask(int taskId, Task task);

    void removeTask(int id);

    Task getTask(int taskId);

    // Методы для работы с эпиками:
    List<Epic> getEpics();

    void removeAllEpics();

    Epic findEpicById(int epicId);

    int addEpic(Epic epic);

    Epic updateEpic(int epicId, Epic epic);

    void removeEpic(int id);

    List<SubTask> getSubTasksInEpic(int epicId);

    Epic getEpic(int epicId);

    // Методы для работы с подзадачами:
    List<SubTask> getAllSubTasks();

    void removeAllSubTasks();

    SubTask findSubTaskById(int subTaskId);

    int addSubTask(SubTask subTask);

    SubTask updateSubTask(int subTaskId, SubTask subTask);

    void removeSubTask(int id);

    SubTask getSubTask(int subTaskId);

    // Метод для получения истории:
    List<Task> getHistory();

    // Метод для получения задач в порядке приоритета:
    List<Task> getPrioritizedTasks();
}
