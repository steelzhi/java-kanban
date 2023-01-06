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
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private static int taskId = 1;

    /**
     * Методы для работы с задачами:
     */
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        for (int taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
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
        if (!tasks.containsKey(taskId)) {
            return null;
        }

        task.setId(taskId);
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Task getTask(int taskId) {
        if (!doesTaskExist(taskId)) {
            return null;
        }

        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    private boolean doesTaskExist(int id) {
        return tasks.containsKey(id);
    }

    /**
     * Методы для работы с эпиками:
     */
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        for (int epicId : epics.keySet()) {
            ArrayList<Integer> subTasksIds = epics.get(epicId).getSubTasksIds();
            for (int subTaskId : subTasksIds) {
                historyManager.remove(subTaskId);
            }
            historyManager.remove(epicId);
        }

        epics.clear();
        subTasks.clear();
    }

    @Override
    public Epic findEpicById(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(taskId);
        epics.put(taskId, epic);
        return taskId++;
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        if (!epics.containsKey(epicId)) {
            return null;
        }

        ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
        epic.setId(epicId);
        epics.put(epicId, epic);
        epics.get(epicId).addAllSubTasksIds(subTaskIds);
        updateEpicStatus(epicId);
        return epic;
    }

    @Override
    public void removeEpic(int id) {
        if (doesEpicExist(id)) {
            ArrayList<Integer> subTasksIds = epics.get(id).getSubTasksIds();
            for (Integer subTaskId : subTasksIds) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
        }

        historyManager.remove(id);
    }

    @Override
    public ArrayList<SubTask> getSubTasksInEpic(int epicId) {
        if (!doesEpicExist(epicId)) {
            return null;
        }

        final Epic epic = epics.get(epicId);
        final ArrayList<SubTask> subTasksInEpic = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasksIds()) {
            final SubTask subTask = subTasks.get(subTaskId);
            subTasksInEpic.add(subTask);
        }
        return subTasksInEpic;
    }

    @Override
    public Epic getEpic(int epicId) {
        if (!doesEpicExist(epicId)) {
            return null;
        }

        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    private boolean doesEpicExist(int id) {
        return epics.containsKey(id);
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

    /**
     * Методы для работы с подзадачами:
     */
    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        for (int subTaskId : subTasks.keySet()) {
            historyManager.remove(subTaskId);
        }

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
        if (!epics.containsKey(subTask.getEpicId())) {
            return 0;
        }

        subTask.setId(taskId);
        subTasks.put(taskId, subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskId(taskId);
        updateEpicStatus(epic.getId());
        return taskId++;
    }

    @Override
    public SubTask updateSubTask(int subTaskId, SubTask subTask) {
        if (!subTasks.containsKey(subTaskId)) {
            return null;
        }

        SubTask currentSubTask = subTasks.get(subTaskId);
        int epicId = currentSubTask.getEpicId();

        subTask.setId(subTaskId);
        subTask.setEpicId(epicId);
        subTasks.put(subTaskId, subTask);

        updateEpicStatus(epicId);
        return subTask;
    }

    @Override
    public void removeSubTask(int id) {
        if (doesSubTaskExist(id)) {
            int epicId = subTasks.get(id).getEpicId();
            Epic epic = epics.get(epicId);
            epic.removeSubTaskId(id);
            updateEpicStatus(epicId);

            subTasks.remove(id);
        }

        historyManager.remove(id);
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        if (!doesSubTaskExist(subTaskId)) {
            return null;
        }

        SubTask subTask = subTasks.get(subTaskId);
        historyManager.add(subTask);
        return subTask;
    }

    private boolean doesSubTaskExist(int id) {
        return subTasks.containsKey(id);
    }

    /**
     * Метод, общий для всех видов задач:
     * @return
     */
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
