package managers.taskmanager.httptaskmanager;

import com.google.gson.Gson;
import http.client.KVTaskClient;
import managers.taskmanager.filemanager.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient taskClient;

    // FileBackedTasksManager нужен, чтобы добавляемым на сервер задачам присваивался корректный id
    FileBackedTasksManager fileBackedTasksManager
            = new FileBackedTasksManager("src\\resources\\Testing (sprint 7).csv");

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        super(url);
        taskClient = new KVTaskClient(url);
    }

    @Override
    public int addTask(Task task) {
        fileBackedTasksManager.addTask(task);
        try {
            taskClient.put(idToString(task), taskToJson(task));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return task.getId();
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        fileBackedTasksManager.updateTask(taskId, task);
        try {
            taskClient.put(String.valueOf(taskId), taskToJson(task));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public int addEpic(Epic epic) {
        fileBackedTasksManager.addEpic(epic);
        try {
            taskClient.put(idToString(epic), taskToJson(epic));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return epic.getId();
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        fileBackedTasksManager.updateEpic(epicId, epic);
        try {
            taskClient.put(String.valueOf(epicId), taskToJson(epic));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return epic;
    }

    @Override
    public int addSubTask(SubTask subTask) {
        fileBackedTasksManager.addSubTask(subTask);
        try {
            taskClient.put(idToString(subTask), taskToJson(subTask));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return subTask.getId();
    }

    @Override
    public SubTask updateSubTask(int subTaskId, SubTask subTask) {
        fileBackedTasksManager.updateSubTask(subTaskId, subTask);
        try {
            taskClient.put(String.valueOf(subTaskId), taskToJson(subTask));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return subTask;
    }

    private static String taskToJson(Task task) {
        return new Gson().toJson(task);
    }

    private static String idToString(Task task) {
        return String.valueOf(task.getId());
    }
}
