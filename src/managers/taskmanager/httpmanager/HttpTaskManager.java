package managers.taskmanager.httpmanager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.client.KVTaskClient;
import managers.taskmanager.filemanager.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient taskClient;
    private List<Task> tasks;
    private List<Epic> epics;
    private List<SubTask> subTasks;
    private List<Task> history;
    private final Gson gson = new Gson();
    private String key;

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        super("src\\resources\\Testing (sprint 7).csv");
        taskClient = new KVTaskClient(url);
        tasks = super.getTasks();
        epics = super.getEpics();
        subTasks = super.getAllSubTasks();
        history = super.getHistory();
    }

    @Override
    public void save() {
        tasks = super.getTasks();
        epics = super.getEpics();
        subTasks = super.getAllSubTasks();
        history = super.getHistory();
        TasksData tasksData = new TasksData(this);

        String json = gson.toJson(tasksData);

        try {
            taskClient.put(key, json);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class TasksData {
        private List<Task> tasksToSerialize = new ArrayList<>();
        private List<Epic> epicsToSerialize = new ArrayList<>();
        private List<SubTask> subTasksToSerialize = new ArrayList<>();
        private List<Task> historyToSerialize = new ArrayList<>();

        public TasksData(HttpTaskManager httpTaskManager) {
            tasksToSerialize.addAll(httpTaskManager.tasks);
            epicsToSerialize.addAll(httpTaskManager.epics);
            subTasksToSerialize.addAll(httpTaskManager.subTasks);
            historyToSerialize.addAll(httpTaskManager.history);
        }

        public List<Task> getTasks() {
            return tasksToSerialize;
        }

        public List<Epic> getEpics() {
            return epicsToSerialize;
        }

        public List<SubTask> getSubTasks() {
            return subTasksToSerialize;
        }

        public List<Task> getHistory() {
            return historyToSerialize;
        }
    }

    public HttpTaskManager loadFromServer(String url, String key) throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);

        String serverData = taskClient.load(key);
        Type type = new TypeToken<TasksData>() {
        }.getType();
        TasksData tasksData = gson.fromJson(serverData, type);

        for (Task task : tasksData.getTasks()) {
            Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
            newTask.setId(task.getId());
            newTask.setStartTime(task.getStartTime());
            newTask.setDuration(task.getDuration());
            httpTaskManager.addTask(newTask);
        }

        for (Epic epic : tasksData.getEpics()) {
            Epic newEpic = new Epic(epic.getName(), epic.getDescription());
            newEpic.setId(epic.getId());
            newEpic.setStartTime(epic.getStartTime());
            newEpic.setDuration(epic.getDuration());
            httpTaskManager.addEpic(newEpic);
        }

        for (SubTask subTask : tasksData.getSubTasks()) {
            SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                    subTask.getEpicId());
            newSubTask.setId(subTask.getId());
            newSubTask.setStartTime(subTask.getStartTime());
            newSubTask.setDuration(subTask.getDuration());
            httpTaskManager.addSubTask(subTask);
        }

        if (tasksData.getHistory() != null) {
            List<Integer> viewedTasksIds = new ArrayList<>();
            for (Task task : tasksData.getHistory()) {
                viewedTasksIds.add(task.getId());
            }
            for (int id : viewedTasksIds) {
                httpTaskManager.getTask(id);
                httpTaskManager.getEpic(id);
                httpTaskManager.getSubTask(id);
            }
        }

        return httpTaskManager;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public KVTaskClient getTaskClient() {
        return taskClient;
    }
}