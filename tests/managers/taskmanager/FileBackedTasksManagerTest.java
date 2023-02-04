package managers.taskmanager;

import managers.taskmanager.filemanager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    Task task1;
    Task task2;
    Task task3;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    SubTask subTask4;
    String fileName = "src\\resources\\test_" + System.nanoTime() + ".csv";

    @BeforeEach
    public void setManager() {
        manager = new FileBackedTasksManager(fileName);
    }

    @Test
    public void saveAndLoadCaseStandard() {
        addTasksForTests();
        addEpicsForTests();
        addSubTasksForTests();

        manager.getSubTask(subTask4.getId());

        FileBackedTasksManager loadingManager = FileBackedTasksManager.loadFromFile(
                new File(fileName));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");
        assertEquals(manager.getHistory(), List.of(subTask4), "Списки не совпадают");

        deleteTestFilesInMainFolder();
    }

    @Test
    public void saveAndLoadCaseEmptyListOfTasks() {
        addEpicsForTests();
        addSubTasksForTests();

        manager.getSubTask(subTask4.getId());

        FileBackedTasksManager loadingManager = FileBackedTasksManager.loadFromFile(
                new File(fileName));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");
        assertEquals(manager.getHistory(), List.of(subTask4), "Списки не совпадают");

        deleteTestFilesInMainFolder();
    }

    @Test
    public void saveAndLoadCaseEpicWithoutTasks() {
        addTasksForTests();

        epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);

        subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());

        manager.getTask(task2.getId());

        FileBackedTasksManager loadingManager = FileBackedTasksManager.loadFromFile(
                new File(fileName));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");

        deleteTestFilesInMainFolder();
    }

    @Test
    public void saveAndLoadCaseEmptyHistory() {
        addTasksForTests();
        addEpicsForTests();
        addSubTasksForTests();

        FileBackedTasksManager loadingManager = FileBackedTasksManager.loadFromFile(
                new File(fileName));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");

        deleteTestFilesInMainFolder();
    }

    @AfterEach
    private void deleteTestFile() {
        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
        }
    }

    //Без этого метода в корневой папке java-kanban тестовые файлы не удаляются
    private void deleteTestFilesInMainFolder() {
        try {
            Files.deleteIfExists(Path.of(fileName.substring(14)));
        } catch (IOException e) {
        }
    }

    private void addTasksForTests() {
        task1 = new Task("t1", "1", Status.NEW);
        task2 = new Task("t2", "1", Status.IN_PROGRESS);
        task3 = new Task("t3", "1", Status.DONE);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
    }

    private void addEpicsForTests() {
        epic1 = new Epic("e1", "2");
        epic2 = new Epic("e2", "2");
        epic3 = new Epic("e3", "2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
    }

    private void addSubTasksForTests() {
        subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        subTask3 = new SubTask("st3", "3", Status.NEW, epic2.getId());
        subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        manager.addSubTask(subTask4);
    }

}