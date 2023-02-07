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
1. По предложенной Вами конструкции
     new TreeSet<>(Comparator
            .comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Task::getId))
Для меня она пока сложна для восприятия;) Поэтому к имеющейся своей конструкции из Вашей я добавил сравнение по id - в
случае, если у обеих задач startTime == null. Можно так оставить?

2. Вы указали, что в методе addTask исключение не нужно отлавливать - пусть оно летит дальше. Но, в этом методе я
отлавливаю исключение с тем расчетом, чтобы предотвратить дальнейшее добавление задачи, пересекающейся по времени с  уже
имеющимися задачами, в хэшмапу и сет. Если в методе addTask не отлавливать исключение, то каким образом следует
реализовать недобавление такой задачи в хэшмапу и сет?
Изначально метод analyzeDoesTaskHaveNoCrossing у меня возвращал тип boolean ("есть ли пересечение с уже имеющимися
задачами или нет") и по возвращаемому значению определялось, стоит ли добавлять новую задачу в хэшмап и сет. Но, Вы
написали, что analyzeDoesTaskHaveNoCrossing должен бросать исключение в случае наличия пересечений (поэтому я сделал
у этого метода тип void).
Если убрать отлов исключения в методе addTask и передать такой отлов дальше, то, получается, ловить исключение нужно
будет в main(), где добавляются задачи. Не будет ли это, наоборот, намного более громоздким, чем оставить отлов
исключения раньше - в методе addTask?
Или здесь есть какое-то еще, более оптимальное, решение?

3. Все остальные Ваши замечания исправил - проверьте, пожалуйста.
 */

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int taskId = 1;

    protected final Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null && o2.getStartTime() == null) {
            return o1.getId() - o2.getId();
        }
        if (o1.getStartTime() == null) {
            return 1;
        }
        if (o2.getStartTime() == null) {
            return -1;
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
        analyzeDoesTaskHaveNoCrossing(task);

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

        analyzeDoesTaskHaveNoCrossing(task);

        Task oldTask = tasks.get(taskId);
        task.setId(taskId);
        tasks.put(taskId, task);
        prioritizedTasks.remove(oldTask);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public void removeTask(int id) {
        if (tasks.get(id) != null) {
            prioritizedTasks.remove(tasks.get(id));
        }
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
                prioritizedTasks.remove(subTasks.get(subTaskId));
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
                prioritizedTasks.remove(subTasks.get(subTaskId));
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

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        for (SubTask subTask : subTasksInEpic) {
            if (subTask.getStartTime() != null && subTask.getDuration() != 0) {
                if (startTime == null || subTask.getStartTime().isBefore(startTime)) {
                    startTime = subTask.getStartTime();
                }
                if (endTime == null || subTask.getEndTime().isAfter(endTime)) {
                    endTime = subTask.getEndTime();
                }
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

        analyzeDoesTaskHaveNoCrossing(subTask);

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

        analyzeDoesTaskHaveNoCrossing(subTask);

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
        if (tasksByTime.isEmpty()) {
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
