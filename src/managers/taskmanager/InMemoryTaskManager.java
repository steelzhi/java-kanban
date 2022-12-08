package managers.taskmanager;

import managers.Managers;
import managers.historymanager.HistoryManager;
import status.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int taskId = 1;
    private int epicId = 1;
    private int subTaskId = 1;
    HistoryManager historyManager = Managers.getDefaultHistory();

    // Методы для работы с задачами:
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        if (isTasksMapEmpty())
            return;
        tasks.clear();
    }

    @Override
    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public int addTask(Task task) {
        task.setId(taskId);
        tasks.put(taskId, task);
        return taskId++;
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        if (!tasks.keySet().contains(taskId))
            return null;

        task.setId(taskId);
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public void removeTask(int id) {
        if (isTasksMapEmpty())
            return;

        tasks.remove(id);
        return;
    }

    @Override
    public Task getTask(int taskId) {
        if (!doesTaskExist(taskId))
            return null;

        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    private boolean doesTaskExist(int id) {
        return tasks.containsKey(id);
    }

    private boolean isTasksMapEmpty() {
        return tasks.isEmpty();
    }

    // ---------------------------------------------------------------------
    // Методы для работы с эпиками:
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        if (isEpicsMapEmpty())
            return;

        epics.clear();
        subTasks.clear();
    }

    @Override
    public Epic findEpicById(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(epicId);
        epics.put(epicId, epic);
        return epicId++;
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        if (!epics.keySet().contains(epicId))
            return null;

        ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
        epic.setId(epicId);
        epics.put(epicId, epic);
        epics.get(epicId).addAllSubTasksIds(subTaskIds);
        updateEpicStatus(epicId);
        return epic;
    }

    @Override
    public void removeEpic(int id) {
        if (isEpicsMapEmpty())
            return;

        if (doesEpicExist(id)) {
            ArrayList<Integer> subTasksIds = epics.get(id).getSubTasksIds();
            for (Integer subTaskId : subTasksIds) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
            return;
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksInEpic(int epicId) {
        if (isEpicsMapEmpty())
            return null;

        if (doesEpicExist(epicId)) {
            Epic epic = epics.get(epicId);
            if (!epic.getSubTasksIds().isEmpty()) {
                ArrayList<SubTask> subTasksInEpic = new ArrayList<>();
                for (Integer subTaskId : epic.getSubTasksIds()) {
                    SubTask subTask = subTasks.get(subTaskId);
                    subTasksInEpic.add(subTask);
                }
                return subTasksInEpic;
            }
        }
        return null;
    }

    @Override
    public Epic getEpic(int epicId) {
        if (!doesEpicExist(epicId))
            return null;

        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    private boolean doesEpicExist(int id) {
        return epics.containsKey(id);
    }

    private boolean isEpicsMapEmpty() {
        return epics.isEmpty();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        Status currentStatus = Status.DONE;
        for (Integer subTaskId : epic.getSubTasksIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask.getStatus() == Status.IN_PROGRESS) {
                currentStatus = Status.IN_PROGRESS;
            }
            if (subTask.getStatus() == Status.NEW) {
                epic.setStatus(Status.NEW);
                return;
            }
        }
        epic.setStatus(currentStatus);
    }

    // ---------------------------------------------------------------------
    // Методы для работы с подзадачами:
    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        if (isSubTasksMapEmpty())
            return;

        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasksIds();
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public SubTask findSubTaskById(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    @Override
    public int addSubTask(SubTask subTask) {
        if (!epics.containsKey(subTask.getEpicId()))
            return 0;

        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskId(subTaskId);
        updateEpicStatus(epic.getId());
        return subTaskId++;
    }

    @Override
    public SubTask updateSubTask(int subTaskId, SubTask subTask) {
        if (!subTasks.containsKey(subTaskId))
            return null;
        else {
            SubTask currentSubTask = subTasks.get(subTaskId);
            int epicId = currentSubTask.getEpicId();

            subTask.setId(subTaskId);
            subTask.setEpicId(epicId);
            subTasks.put(subTaskId, subTask);

            updateEpicStatus(epicId);
            return subTask;
        }
    }

    @Override
    public void removeSubTask(int id) {
        if (isSubTasksMapEmpty())
            return;

        if (doesSubTaskExist(id)) {
            int epicId = subTasks.get(id).getEpicId();
            Epic epic = epics.get(epicId);
            epic.removeSubTaskId(id);
            updateEpicStatus(epicId);

            subTasks.remove(id);
            return;
        }
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        if (!doesSubTaskExist(subTaskId))
        return null;

        SubTask subTask = subTasks.get(subTaskId);
        historyManager.add(subTask);
        return subTask;
    }

    private boolean doesSubTaskExist(int id) {
        return subTasks.containsKey(id);
    }

    private boolean isSubTasksMapEmpty() {
        return subTasks.isEmpty();
    }

    // ---------------------------------------------------------------------
    //Метод, общий для всех видов задач
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
