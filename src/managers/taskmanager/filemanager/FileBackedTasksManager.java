package managers.taskmanager.filemanager;

import managers.taskmanager.memorymanager.InMemoryTaskManager;
import managers.taskmanager.ManagerSaveException;
import tasks.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String fileName;

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        String fileName = "src\\resources\\Testing (sprint 7).csv";
        FileBackedTasksManager savingManager =
                new FileBackedTasksManager(fileName);
        Task task1 = new Task("t1", "1", Status.NEW);

        Task task2 = new Task("t2", "1", Status.IN_PROGRESS);
        Task task3 = new Task("t3", "1", Status.DONE);
        Task task4 = new Task("t4", "1", Status.DONE);

        task2.setStartTime(LocalDateTime.now().minusSeconds(1000));
        task2.setDuration(10);
        task3.setStartTime(LocalDateTime.now().plusSeconds(10001));
        task3.setDuration(100000);
        //task4.setStartTime(LocalDateTime.now().minusSeconds(1000000));
        //task4.setDuration(200000);
        savingManager.addTask(task1);
        savingManager.addTask(task2);
        savingManager.addTask(task3);
        savingManager.addTask(task4);

        Epic epic1 = new Epic("e1", "2");
        Epic epic2 = new Epic("e2", "2");
        Epic epic3 = new Epic("e3", "2");
        epic1.setStartTime(LocalDateTime.now());
        epic1.setDuration(10000);
        epic2.setStartTime(LocalDateTime.now());
        epic2.setDuration(10000);
        savingManager.addEpic(epic1);
        savingManager.addEpic(epic2);
        savingManager.addEpic(epic3);

        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        SubTask subTask3 = new SubTask("st3", "3", Status.NEW, epic2.getId());
        SubTask subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());
        subTask1.setStartTime(LocalDateTime.now().minusSeconds(1000000));
        subTask1.setDuration(100000);
        subTask2.setStartTime(LocalDateTime.now().minusSeconds(10000000));
        subTask2.setDuration(200000);
        subTask3.setStartTime(LocalDateTime.now().minusSeconds(2000000));
        subTask3.setDuration(400000);
        savingManager.addSubTask(subTask1);
        savingManager.addSubTask(subTask2);
        savingManager.addSubTask(subTask3);
        savingManager.addSubTask(subTask4);
        System.out.println("S1: " + subTask1.getStartTime() + " " + subTask1.getEndTime());
        System.out.println("S2: " + subTask2.getStartTime() + " " + subTask2.getEndTime());
        System.out.println("S3: " + subTask3.getStartTime() + " " + subTask3.getEndTime());
        System.out.println("S4: " + subTask4.getStartTime() + " " + subTask4.getEndTime());

        System.out.println("Epic: " + epic1.getStartTime() + " " + epic1.getEndTime());

        savingManager.getTask(1);
        savingManager.getTask(2);
        savingManager.getEpic(5);
        savingManager.getTask(1);
        savingManager.getTask(3);
        savingManager.getSubTask(8);
        savingManager.getEpic(4);
        savingManager.getSubTask(10);
        savingManager.getEpic(6);

        savingManager.updateTask(1, new Task("t1", "fasdfas", Status.DONE));

        TreeSet<Task> prioritizedtasks = savingManager.getPrioritizedTasks();
        System.out.println("Задачи в порядке приоритета:");
        for (Task task : prioritizedtasks) {
            System.out.println(task);
        }

        //savingManager.removeAllSubTasks();

        savingManager.addSubTask(subTask3);
        savingManager.getSubTask(11);

        savingManager.removeEpic(4);

        FileBackedTasksManager loadingManager = loadFromFile(new File(fileName));
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(CSVTaskFormatter.getTitle());

            for (Task task : super.getTasks()) {
                writer.write(CSVTaskFormatter.toString(task));
            }

            for (Epic epic : super.getEpics()) {
                writer.write(CSVTaskFormatter.toString(epic));
            }

            for (SubTask subTask : super.getAllSubTasks()) {
                writer.write(CSVTaskFormatter.toString(subTask));
            }

            writer.newLine();
            writer.write(CSVTaskFormatter.historyToString(super.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file.getName());

        final String csv;
        try {
            csv = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла.");
        }

        final String[] lines = csv.split("\n");
        List<Task> tasks = new ArrayList<>();

        boolean doesHistoryExist = true;
        int lastLineWithTask = lines.length - 3;
        if (lines[lines.length - 1].equals("\r")) {
            doesHistoryExist = false;
            lastLineWithTask = lines.length - 2;
        }

        for (int i = 1; i <= lastLineWithTask; i++) {
            tasks.add(CSVTaskFormatter.fromString(lines[i]));
        }

        for (Task task : tasks) {
            TaskTypes type = task.getType();
            manager.taskId = task.getId();

            switch (type) {
                case TASK:
                    Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
                    newTask.setStartTime(task.getStartTime());
                    newTask.setDuration(task.getDuration());
                    manager.addTask(newTask);
                    break;
                case EPIC:
                    Epic newEpic = new Epic(task.getName(), task.getDescription());
                    newEpic.setStartTime(task.getStartTime());
                    newEpic.setDuration(task.getDuration());
                    manager.addEpic(newEpic);
                    break;
                case SUBTASK:
                    SubTask newSubTask = new SubTask(task.getName(), task.getDescription(), task.getStatus(),
                            task.getEpicId());
                    newSubTask.setStartTime(task.getStartTime());
                    newSubTask.setDuration(task.getDuration());
                    manager.addSubTask(newSubTask);
                    break;
                default:
                    break;
            }
        }

        if (doesHistoryExist) {
            List<Integer> viewedTasksIds = CSVTaskFormatter.historyFromString(lines[lines.length - 1]);
            for (int id : viewedTasksIds) {
                manager.getTask(id);
                manager.getEpic(id);
                manager.getSubTask(id);
            }
        }

        try {
            Files.deleteIfExists(Path.of(file.getName()));
        } catch (IOException e) {
        }

        return manager;
    }

    /**
     * Переопределенные методы для работы с задачами:
     */
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public int addTask(Task task) {
        int newId = super.addTask(task);
        save();
        return newId;
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        Task updatedTask = super.updateTask(taskId, task);
        save();
        return updatedTask;
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    /**
     * Переопределенные методы для работы с эпиками:
     */
    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public int addEpic(Epic epic) {
        int newId = super.addEpic(epic);
        save();
        return newId;
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        Epic updatedEpic = super.updateEpic(epicId, epic);
        save();
        return updatedEpic;
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    /**
     * Переопределенные методы для работы с подзадачами:
     */
    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public int addSubTask(SubTask subTask) {
        int newId = super.addSubTask(subTask);
        save();
        return newId;
    }

    @Override
    public SubTask updateSubTask(int subTaskId, SubTask subTask) {
        SubTask updatedSubTask = super.updateSubTask(subTaskId, subTask);
        save();
        return updatedSubTask;
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = super.getSubTask(subTaskId);
        save();
        return subTask;
    }
}
