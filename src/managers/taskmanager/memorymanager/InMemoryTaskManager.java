package managers.taskmanager.memorymanager;

import managers.Managers;
import managers.taskmanager.TaskManager;
import managers.historymanager.HistoryManager;
import managers.taskmanager.TaskValidationException;
import tasks.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/*
Никита, здравствуйте.
1. Вы оставили такой комментарий: "Сортировка должна быть просто по startTime, то есть можно написать просто короче
Comparator.comparing(Task::getStartTime), если использовать LocalDateTime, хотя так тоже верно :)".
Но я проверяю также случаи, если у какой-то задачи не задано время старта (startTime == null). Соответственно, можно ли
каким-то образом использовать нотацию Comparator.comparing(Task::getStartTime) для такой проверки? Или же по логике
приложения у всех задач должны быть обязательно заданы поля startTime и duration и вышеуказанная ситуация будет исключена?
2. Все остальное поправил -  проверьте, пожалуйста.
 */

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int taskId = 1;

    protected final Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o2 == null || o2.getStartTime() == null) {
            return -1;
        }
        if (o1 == null || o1.getStartTime() == null) {
            return 1;
        }

        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else if (o1.getStartTime().equals(o2.getStartTime())) {
            return 0;
        } else {
            return 1;
        }
    });

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
            prioritizedTasks.remove(tasks.get(taskId));
        }
        tasks.clear();
    }

    @Override
    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public int addTask(Task task) {
        try {
            analyzeDoesTaskHaveNoCrossing(task);
        } catch (TaskValidationException e) {
            e.getMessage();
            return 0;
        }

        task.setId(taskId);
        tasks.put(taskId, task);
        prioritizedTasks.add(task);
        return taskId++;
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        if (!tasks.containsKey(taskId)) {
            return null;
        }

        try {
            analyzeDoesTaskHaveNoCrossing(task);
        } catch (TaskValidationException e) {
            e.getMessage();
            return null;
        }

        Task oldTask = tasks.get(taskId);
        task.setId(taskId);
        tasks.put(taskId, task);
        prioritizedTasks.remove(oldTask);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public void removeTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
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

        LocalDateTime startTime = subTasksInEpic.get(0).getStartTime();
        LocalDateTime endTime = subTasksInEpic.get(0).getEndTime();

        bypass:
        for (SubTask subTask : subTasksInEpic) {
            if (subTask.getStartTime() == null || subTask.getDuration() == 0) {
                continue bypass;
            }

            if (startTime == null || subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
            if (endTime == null || subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
            }
        }

        epics.get(epicId).setStartTime(startTime);
        epics.get(epicId).setEndTime(endTime);
        if (startTime != null && endTime != null) {
            epics.get(epicId).setDuration((int) Duration.between(startTime, endTime).toMinutes());
        }
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
            prioritizedTasks.remove(subTasks.get(subTaskId));
        }

        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasksIds();
            updateEpic(epic.getId());
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

        try {
            analyzeDoesTaskHaveNoCrossing(subTask);
        } catch (TaskValidationException e) {
            e.getMessage();
            return 0;
        }

        subTask.setId(taskId);
        subTasks.put(taskId, subTask);
        prioritizedTasks.add(subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskId(taskId);
        updateEpic(epic.getId());
        return taskId++;
    }

    @Override
    public SubTask updateSubTask(int subTaskId, SubTask subTask) {
        if (!subTasks.containsKey(subTaskId)) {
            return null;
        }

        try {
            analyzeDoesTaskHaveNoCrossing(subTask);
        } catch (TaskValidationException e) {
            e.getMessage();
            return null;
        }

        SubTask currentSubTask = subTasks.get(subTaskId);
        int epicId = currentSubTask.getEpicId();
        subTask.setId(subTaskId);
        subTask.setEpicId(epicId);
        subTasks.put(subTaskId, subTask);
        prioritizedTasks.remove(currentSubTask);
        prioritizedTasks.add(subTask);
        updateEpic(epicId);
        return subTask;
    }

    @Override
    public void removeSubTask(int id) {
        if (doesSubTaskExist(id)) {
            int epicId = subTasks.get(id).getEpicId();
            Epic epic = epics.get(epicId);
            epic.removeSubTaskId(id);
            updateEpic(epicId);

            prioritizedTasks.remove(subTasks.get(id));
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

    private void updateEpic(int epicId) {
        updateEpicStatus(epicId);
        updateEpicStartAndEndTime(epicId);
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
        return (TreeSet<Task>) prioritizedTasks;
    }

    private void analyzeDoesTaskHaveNoCrossing(Task task) {
        TreeSet<Task> tasksByTime = getPrioritizedTasks();
        if (tasksByTime.size() < 2) {
            return;
        }

        LocalDateTime newTaskStartTime = task.getStartTime();
        LocalDateTime newTaskEndTime = task.getEndTime();

        if (newTaskStartTime == null || newTaskEndTime == null) {
            return;
        }

        for (Task existingTask : tasksByTime) {
            if (existingTask.getStartTime() == null) {
                return;
            }

            if ((newTaskStartTime.isBefore(existingTask.getStartTime())
                    && newTaskEndTime.isAfter(existingTask.getStartTime()))
                    || (newTaskStartTime.isBefore(existingTask.getEndTime())
                    && newTaskEndTime.isAfter(existingTask.getEndTime()))) {
                throw new TaskValidationException(
                        "Невозможно добавить/изменить задачу из-за пересечения по времени с имеющимися задачами!");
            }
        }
    }
}
