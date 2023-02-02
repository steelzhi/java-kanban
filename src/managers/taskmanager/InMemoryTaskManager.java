package managers.taskmanager;

import managers.Managers;
import managers.historymanager.HistoryManager;
import tasks.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int taskId = 1;

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
        if (doesTasksCrossingExist()) {
            tasks.remove(taskId);
            return 0;
        }
        return taskId++;
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        if (!tasks.containsKey(taskId)) {
            return null;
        }

        Task oldTask = tasks.get(taskId);

        task.setId(taskId);
        tasks.put(taskId, task);
        if (doesTasksCrossingExist()) {
            tasks.put(taskId, oldTask);
            return null;
        }
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

    private void updateEpicStartAndEndTime(int epicId) {
        ArrayList<SubTask> subTasksInEpic = getSubTasksInEpic(epicId);
        if (subTasksInEpic.isEmpty()) {
            return;
        }

        Instant startTime = subTasksInEpic.get(0).getStartTime();
        Instant endTime = subTasksInEpic.get(0).getEndTime();

        for (SubTask subTask : subTasksInEpic) {
            if (subTask.getStartTime() == null || subTask.getDuration() == 0) {
                break;
            }

            if (subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
            if (subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
            }
        }

        epics.get(epicId).setStartTime(startTime);
        epics.get(epicId).setEndTime(endTime);
        epics.get(epicId).setDuration(epics.get(epicId).getDuration());
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
            updateEpicStartAndEndTime(epic.getId());
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

        if (doesTasksCrossingExist()) {
            subTasks.remove(taskId);
            epic.removeSubTaskId(taskId);
            return 0;
        }

        updateEpicStatus(epic.getId());
        updateEpicStartAndEndTime(epic.getId());
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

        if (doesTasksCrossingExist()) {
            subTasks.put(subTaskId, currentSubTask);
            return null;
        }

        updateEpicStatus(epicId);
        updateEpicStartAndEndTime(epicId);
        return subTask;
    }

    @Override
    public void removeSubTask(int id) {
        if (doesSubTaskExist(id)) {
            int epicId = subTasks.get(id).getEpicId();
            Epic epic = epics.get(epicId);
            epic.removeSubTaskId(id);
            updateEpicStatus(epicId);
            updateEpicStartAndEndTime(epic.getId());

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
     * Методы, общие для всех видов задач:
     *
     * @return
     */
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = (o1, o2) -> {
            if (o2.getStartTime() == null) {
                return -1;
            }
            if (o1.getStartTime() == null) {
                return 1;
            }

            return (int) (o1.getStartTime().getEpochSecond() - o2.getStartTime().getEpochSecond());
        };

        TreeSet<Task> tasksByTime = new TreeSet<>(comparator);
        for (Task task : getTasks()) {
            tasksByTime.add(task);
            System.out.println(tasksByTime);
        }
        for (SubTask subTask : getAllSubTasks()) {
            tasksByTime.add(subTask);
        }

        return tasksByTime;
    }

    @Override
    public boolean doesTasksCrossingExist() {
        TreeSet<Task> tasksByTime = getPrioritizedTasks();
        if (tasksByTime.size() < 2) {
            return false;
        }

        for (int i = 0; i < tasksByTime.size() - 1; i++) {
            Task first = tasksByTime.pollFirst();
            Task second = tasksByTime.first();

            if (first.getStartTime() == null || second.getStartTime() == null) {
                break;
            }

            if (first.getEndTime().isAfter(second.getStartTime())) {
                System.out.println("Невозможно добавить/изменить задачу из-за пересечения по времени!");
                return true;
            }
        }

        return false;
    }
}
