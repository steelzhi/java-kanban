package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private static int taskId = 1;
    private static int epicId = 1;
    private static int subTaskId = 1;
    private static Scanner scanner = new Scanner(System.in);

    // Методы для работы с задачами:
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        if (isTasksMapEmpty()) {
            writeAboutEmptyMap();
            return;
        }
        tasks.clear();
    }

    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    public int addTask(Task task) {
        task.setId(taskId);
        tasks.put(taskId, task);
        return taskId++;
    }

    public Task updateTask(int taskId, Task task) {
        if (!tasks.keySet().contains(taskId)) {
            writeAboutMissingId();
            return null;
        }
        task.setId(taskId);
        tasks.put(taskId, task);
        return task;
    }

    public void removeTask(int id) {
        if (isTasksMapEmpty())
            return;
        if (doesTaskExist(id)) {
            tasks.remove(id);
            return;
        }
    }

    public boolean doesTaskExist(int id) {
        return tasks.containsKey(id);
    }

    public boolean isTasksMapEmpty() {
        return tasks.isEmpty();
    }

    // ---------------------------------------------------------------------
    // Методы для работы с эпиками:
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        if (isEpicsMapEmpty())
            return;
        epics.clear();
        subTasks.clear();
    }

    public Epic findEpicById(int epicId) {
        return epics.get(epicId);
    }

    public int addEpic(Epic epic) {
        epic.setId(epicId);
        epics.put(epicId, epic);
        return epicId++;
    }

    public Epic updateEpic(int epicId, Epic epic) {
        if (!epics.keySet().contains(epicId))
            return null;
        else {
            ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
            epic.setId(epicId);
            epics.put(epicId, epic);
            epics.get(epicId).addAllSubTasksIds(subTaskIds);
            updateEpicStatus(epicId);
            return epic;
        }
    }

    public void removeEpic(int id) {
        if (isEpicsMapEmpty())
            return;
        else if (doesEpicExist(id)) {
            ArrayList<Integer> subTasksIds = epics.get(id).getSubTasksIds();
            for (Integer subTaskId : subTasksIds) {
                if (subTasks.containsKey(subTaskId))
                    subTasks.remove(subTaskId);
            }
            epics.remove(id);
            return;
        }
    }

    public ArrayList<SubTask> getSubTasksInEpic(int epicId) {
        if (isEpicsMapEmpty())
            return null;
        else if (doesEpicExist(epicId)) {
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

    public boolean doesEpicExist(int id) {
        return epics.containsKey(id);
    }

    public boolean isEpicsMapEmpty() {
        return epics.isEmpty();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus("NEW");
            return;
        }
        String currentStatus = "DONE";
        for (Integer subTaskId : epic.getSubTasksIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask.getStatus().equals("IN_PROGRESS")) {
                currentStatus = "IN_PROGRESS";
            }
            if (subTask.getStatus().equals("NEW")) {
                epic.setStatus("NEW");
                return;
            }
        }
        epic.setStatus(currentStatus);
    }


    // ---------------------------------------------------------------------
    // Методы для работы с подзадачами:
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void removeAllSubTasks() {
        if (isSubTasksMapEmpty())
            return;
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasksIds();
            epic.setStatus("NEW");
        }
    }

    public SubTask findSubTaskById(int subTaskId) {
        return subTasks.get(subTaskId);
    }

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

    public SubTask updateSubTask(int subTaskId, SubTask subTask) {
        if (!subTasks.keySet().contains(subTaskId))
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

    public boolean doesSubTaskExist(int id) {
        return subTasks.containsKey(id);
    }

    public boolean isSubTasksMapEmpty() {
        return subTasks.isEmpty();
    }

    // ---------------------------------------------------------------------
    // Универсальные методы, работающие и с задачами, и с эпиками, и с подзадачами:
    public void writeAboutMissingId() {
        System.out.println("Указанного id нет в списке.\n");
    }

    public void writeAboutEmptyMap() {
        System.out.println("Список пуст.\n");
    }
}
