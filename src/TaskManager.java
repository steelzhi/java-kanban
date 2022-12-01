import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    private static int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    // Методы для работы с задачами:
    public HashMap<Integer, Task> getTasks() {
        if (isTasksMapEmpty()) {
            writeAboutEmptyMap();
        } else {
            for (Integer number : tasks.keySet()) {
                System.out.println("id задачи = " + number + ", " + tasks.get(number).toString() + ".");
            }
            System.out.println();
        }
        return tasks;
    }

    public void removeAllTasks() {
        if (isTasksMapEmpty()) {
            writeAboutEmptyMap();
            return;
        }
        tasks.clear();
        System.out.println("Все задачи удалены из списка.\n");
    }

    public Task findTaskById() {
        if (isTasksMapEmpty()) {
            writeAboutEmptyMap();
            return null;
        } else {
            System.out.println("Введите id:");
            int taskId = scanner.nextInt();

            for (Integer number : tasks.keySet()) {
                if (number == taskId) {
                    System.out.println("Задача найдена в списке!");
                    System.out.println(tasks.get(taskId) + "\n");
                    return tasks.get(taskId);
                }
            }
            writeAboutMissingId();
            return null;
        }
    }

    public void addTask(Task task) {
        task.setTaskId(id);
        tasks.put(id++, task);
    }

    public Task updateTask(int taskId, Task task) {
        if (!tasks.keySet().contains(taskId)) {
            writeAboutMissingId();
            return null;
        } else {
            tasks.put(taskId, task);
            System.out.println("Задача с id = " + taskId + " обновлена!\n");
            return task;
        }
    }

    public void removeTask() {
        if (isTasksMapEmpty()) {
            writeAboutEmptyMap();
            return;
        } else {
            System.out.println("Ниже перечислены доступные задачи:");
            getTasks();
            System.out.println("Введите id задачи:");
            int taskId = scanner.nextInt();
            if (doesTaskExist(taskId)) {
                tasks.remove(taskId);
                System.out.println("Задача с id = " + taskId + " удалена из списка.\n");
                return;
            } else {
                writeAboutMissingId();
            }
        }
    }

    private boolean doesTaskExist(int id) {
        if (tasks.containsKey(id))
            return true;
        return false;
    }

    public boolean isTasksMapEmpty() {
        if (tasks.size() == 0)
            return true;
        return false;
    }

    public boolean doesTaskManagerContainTask(int id) {
        if (tasks.keySet().contains(id))
            return true;
        return false;
    }

    // ---------------------------------------------------------------------
    // Методы для работы с эпиками:
    public HashMap<Integer, Epic> getEpics() {
        if (isEpicsMapEmpty()) {
            writeAboutEmptyMap();
        } else {
            for (Integer number : epics.keySet()) {
                System.out.println("id эпика = " + number + ", " + epics.get(number).toString() + ".");
            }
            System.out.println();
        }
        return epics;
    }

    public void removeAllEpics() {
        if (isEpicsMapEmpty()) {
            writeAboutEmptyMap();
            return;
        }
        epics.clear();
        System.out.println("Все эпики удалены из списка.\n");
    }

    public Task findEpicById() {
        if (isEpicsMapEmpty()) {
            writeAboutEmptyMap();
            return null;
        } else {
            System.out.println("Введите id:");
            int epicId = scanner.nextInt();

            for (Integer number : epics.keySet()) {
                if (number == epicId) {
                    System.out.println("Эпик найден в списке!");
                    System.out.println(epics.get(epicId) + "\n");
                    return epics.get(epicId);
                }
            }
            writeAboutMissingId();
            return null;
        }
    }

    public void addEpic(Epic epic) {
        epic.setTaskId(id);
        epics.put(id++, epic);
    }

    public Epic updateEpic(int epicId, Epic epic) {
        if (!epics.keySet().contains(epicId)) {
            writeAboutMissingId();
            return null;
        } else {
            ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
            epics.put(epicId, epic);
            epics.get(epicId).addAllSubTasksIds(subTaskIds);
            updateEpicsStatuses();
            System.out.println("Эпик с id = " + epicId + " обновлен!\n");
            return epic;
        }
    }

    public void removeEpic() {
        if (isEpicsMapEmpty()) {
            writeAboutEmptyMap();
            return;
        } else {
            System.out.println("Ниже перечислены доступные эпики:");
            getEpics();
            System.out.println("Введите id эпика:");
            int epicId = scanner.nextInt();
            if (doesEpicExist(epicId)) {
                ArrayList<Integer> subTasksIds = epics.get(epicId).getSubTasksIds();
                for (Integer subTaskId : subTasksIds) {
                    if (subTasks.containsKey(subTaskId))
                        subTasks.remove(subTaskId);
                }
                epics.remove(epicId);
                System.out.println("Эпик с id = " + epicId + " удален из списка. Все соответствующие ему подзадачи также удалены.\n");
                return;
            } else {
                writeAboutMissingId();
            }
        }
    }

    public ArrayList<SubTask> getSubTasksInEpic() {
        if (isEpicsMapEmpty()) {
            writeAboutEmptyMap();
            return null;
        } else {
            System.out.println("Ниже перечислены доступные эпики:");
            getEpics();
            System.out.println("Введите id эпика:");
            int epicId = scanner.nextInt();
            if (doesEpicExist(epicId)) {
                Epic epic = epics.get(epicId);
                if (epic.getSubTasksIds().size() != 0) {
                    ArrayList<SubTask> subTasksInEpic = new ArrayList<>();
                    System.out.println("В данный эпик входят следующие подзадачи:");
                    for (Integer subTaskId : epic.getSubTasksIds()) {
                        SubTask subTask = subTasks.get(subTaskId);
                        System.out.println("id подзадачи = " + subTaskId + ", " + subTask);
                        subTasksInEpic.add(subTask);
                    }
                    return subTasksInEpic;
                } else {
                    System.out.println("В данном эпике нет подзадач.\n");
                    return null;
                }
            } else {
                writeAboutMissingId();
                return null;
            }
        }
    }

    public boolean doesEpicExist(int id) {
        if (epics.containsKey(id))
            return true;
        return false;
    }

    public boolean isEpicsMapEmpty() {
        if (epics.size() == 0)
            return true;
        return false;
    }

    public boolean doesTaskManagerContainEpic(int id) {
        if (epics.keySet().contains(id))
            return true;
        return false;
    }

    private void updateEpicsStatuses() {
        for (Epic epic : epics.values()) {
            if (epic.getSubTasksIds().size() == 0) {
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
    }


    // ---------------------------------------------------------------------
    // Методы для работы с подзадачами:
    public HashMap<Integer, SubTask> getAllSubTasks() {
        if (isSubTasksMapEmpty()) {
            writeAboutEmptyMap();
        } else {
            for (Integer number : subTasks.keySet()) {
                System.out.println("id подзадачи = " + number + ", " + subTasks.get(number).toString() + ".");
            }
            System.out.println();
        }
        return subTasks;
    }

    public void removeAllSubTasks() {
        if (isSubTasksMapEmpty()) {
            writeAboutEmptyMap();
            return;
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasksIds();
        }
        updateEpicsStatuses();
        System.out.println("Все подзадачи удалены из списка.\n");
    }

    public Task findSubTaskById() {
        if (isSubTasksMapEmpty()) {
            writeAboutEmptyMap();
            return null;
        } else {
            System.out.println("Введите id:");
            int subTaskId = scanner.nextInt();

            for (Integer number : subTasks.keySet()) {
                if (number == subTaskId) {
                    System.out.println("Подзадача найдена в списке!");
                    System.out.println(subTasks.get(subTaskId) + "\n");
                    return subTasks.get(subTaskId);
                }
            }
            writeAboutMissingId();
            return null;
        }
    }

    public void addSubTask(int epicId, SubTask subTask) {
        int currentId = id;
        subTask.setTaskId(id);
        subTasks.put(id++, subTask);
        Epic epic = epics.get(epicId);
        epic.addSubTaskId(currentId);
        updateEpicsStatuses();
    }

    public SubTask updateSubTask(int subTaskId, SubTask subTask) {
        if (!subTasks.keySet().contains(subTaskId)) {
            writeAboutMissingId();
            return null;
        } else {
            getAllSubTasks();
            subTasks.put(subTaskId, subTask);
            updateEpicsStatuses();
            System.out.println("Подзадача с id = " + subTaskId + " обновлена!\n");
            return subTask;
        }
    }

    public void removeSubTask() {
        if (isSubTasksMapEmpty()) {
            writeAboutEmptyMap();
            return;
        } else {
            System.out.println("Ниже перечислены доступные подзадачи:");
            getAllSubTasks();
            System.out.println("Введите id подзадачи:");
            int subTaskId = scanner.nextInt();
            if (doesSubTaskExist(subTaskId)) {
                subTasks.remove(subTaskId);
                for (Epic epic : epics.values()) {
                    if (epic.getSubTasksIds().contains(subTaskId))
                        epic.removeSubTaskId(subTaskId);
                }
                System.out.println("Подзадача с id = " + subTaskId + " удалена из списка.\n");
                updateEpicsStatuses();
                return;
            } else {
                writeAboutMissingId();
            }
        }
    }

    private boolean doesSubTaskExist(int id) {
        if (subTasks.containsKey(id))
            return true;
        return false;
    }

    public boolean isSubTasksMapEmpty() {
        if (subTasks.size() == 0)
            return true;
        return false;
    }

    public boolean doesTaskManagerContainSubTask(int id) {
        if (subTasks.keySet().contains(id))
            return true;
        return false;
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
