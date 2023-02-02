package managers.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

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


    FileBackedTasksManager manager = new FileBackedTasksManager(
            "C:\\dev\\java-kanban\\Тестирование (7-й спринт).csv");;

    @BeforeEach
    public void setManager() {
        super.setManager(manager);
    }

    @Test
    public void getTasksCaseStandard() {
        super.getTasksCaseStandard();
    }

    @Test
    public void getTasksCaseEmptyList() {
        super.getTasksCaseEmptyList();
    }

    @Test
    public void removeAllTasksCaseStandard() {
        super.removeAllTasksCaseStandard();
    }

    @Test
    public void removeAllTasksCaseEmptyList() {
        super.removeAllTasksCaseEmptyList();
    }

    @Test
    public void findTaskByIdCaseStandard() {
        super.findTaskByIdCaseStandard();
    }

    @Test
    public void findTaskByIdCaseEmptyList() {
        super.findTaskByIdCaseEmptyList();
    }

    @Test
    public void findTaskByIdCaseIncorrectData() {
        super.findTaskByIdCaseIncorrectData();
    }

    @Test
    public void addTaskCaseStandard() {
        super.addTaskCaseStandard();
    }

    @Test
    public void updateTaskCaseStandard() {
        super.updateTaskCaseStandard();
    }

    @Test
    public void updateTaskCaseEmptyList() {
        super.updateTaskCaseEmptyList();
    }

    @Test
    public void updateTaskCaseIncorrectData() {
        super.updateTaskCaseIncorrectData();
    }

    @Test
    public void removeTaskCaseStandard() {
        super.removeTaskCaseStandard();
    }

    @Test
    public void removeTaskCaseEmptyList() {
        super.removeTaskCaseEmptyList();
    }

    @Test
    public void removeTaskCaseIncorrectData() {
        super.removeTaskCaseIncorrectData();
    }

    @Test
    public void getTaskCaseStandard() {
        super.getTaskCaseStandard();
    }

    @Test
    public void getTaskCaseEmptyList() {
        super.getTaskCaseEmptyList();
    }

    @Test
    public void getTaskCaseIncorrectData() {
        super.getTaskCaseIncorrectData();
    }

    @Test
    public void getEpicsCaseStandard() {
        super.getEpicsCaseStandard();
    }

    @Test
    public void getEpicsCaseEmptyList() {
        super.getEpicsCaseEmptyList();
    }

    @Test
    public void removeAllEpicsCaseStandard() {
        super.removeAllEpicsCaseStandard();
    }

    @Test
    public void removeAllEpicsCaseEmptyList() {
        super.removeAllEpicsCaseEmptyList();
    }


    @Test
    public void findEpicByIdCaseStandard() {
        super.findEpicByIdCaseStandard();
    }

    @Test
    public void findEpicByIdCaseEmptyList() {
        super.findEpicByIdCaseEmptyList();
    }

    @Test
    public void findEpicByIdCaseIncorrectData() {
        super.findEpicByIdCaseIncorrectData();
    }

    @Test
    public void addEpicCaseStandard() {
        super.addEpicCaseStandard();
    }

    @Test
    public void addEpicCaseEmptyList() {
        super.addEpicCaseEmptyList();
    }


    @Test
    public void updateEpicCaseStandard() {
        super.updateEpicCaseStandard();
    }

    @Test
    public void updateEpicCaseEmptyList() {
        super.updateEpicCaseEmptyList();
    }

    @Test
    public void updateEpicCaseIncorrectData() {
        super.updateEpicCaseIncorrectData();
    }

    @Test
    public void removeEpicCaseStandard() {
        super.removeEpicCaseStandard();
    }

    @Test
    public void removeEpicCaseEmptyList() {
        super.removeEpicCaseEmptyList();
    }

    @Test
    public void getSubTasksInEpicCaseStandard() {
        super.getSubTasksInEpicCaseStandard();
    }

    @Test
    public void getSubTasksInEpicCaseEmptyList() {
        super.getSubTasksInEpicCaseEmptyList();
    }

    @Test
    public void getEpicCaseStandard() {
        super.getEpicCaseStandard();
    }

    @Test
    public void getEpicCaseEmptyList() {
        super.getEpicCaseEmptyList();
    }


    @Test
    public void countEpicStatusCaseStandard() {
        super.countEpicStatusCaseStandard();
    }

    @Test
    public void countEpicStatusCaseEmptyList() {
        super.countEpicStatusCaseEmptyList();
    }

    @Test
    public void getAllSubTasksCaseStandard() {
        super.getAllSubTasksCaseStandard();
    }

    @Test
    public void getAllSubTasksCaseEmptyList() {
        super.getAllSubTasksCaseEmptyList();
    }


    @Test
    public void removeAllSubTasksCaseStandard() {
        super.removeAllSubTasksCaseStandard();
    }

    @Test
    public void removeAllSubTasksCaseEmptyList() {
        super.removeAllSubTasksCaseEmptyList();
    }


    @Test
    public void findSubTaskByIdCaseStandard() {
        super.findSubTaskByIdCaseStandard();
    }

    @Test
    public void findSubTaskByIdCaseEmptyList() {
        super.findSubTaskByIdCaseEmptyList();
    }

    @Test
    public void findSubTaskByIdCaseIncorrectData() {
        super.findSubTaskByIdCaseIncorrectData();
    }

    @Test
    public void addSubTaskCaseStandard() {
        super.addSubTaskCaseStandard();
    }


    @Test
    public void addSubTaskCaseIncorrectData() {
        super.addSubTaskCaseIncorrectData();
    }

    @Test
    public void updateSubTaskCaseStandard() {
        super.updateSubTaskCaseStandard();
    }

    @Test
    public void updateSubTaskCaseEmptyList() {
        super.updateSubTaskCaseEmptyList();
    }

    @Test
    public void updateSubTaskCaseIncorrectData() {
        super.updateSubTaskCaseIncorrectData();
    }

    @Test
    public void removeSubTaskCaseStandard() {
        super.removeSubTaskCaseStandard();
    }

    @Test
    public void removeSubTaskCaseEmptyList() {
        super.removeSubTaskCaseEmptyList();
    }

    @Test
    public void removeSubTaskCaseIncorrectData() {
        super.removeSubTaskCaseIncorrectData();
    }

    @Test
    public void doesEpicExistForSubTaskCaseStandard() {
        super.doesEpicExistForSubTaskCaseStandard();
    }

    @Test
    public void doesEpicExistForSubTaskCaseEmptyList() {
        super.doesEpicExistForSubTaskCaseEmptyList();
    }

    @Test
    public void doesEpicExistForSubTaskCaseIncorrectData() {
        super.doesEpicExistForSubTaskCaseIncorrectData();
    }

    @Test
    public void getHistoryCaseStandard() {
        super.getHistoryCaseStandard();
    }

    @Test
    public void getHistoryCaseEmptyList() {
        super.getHistoryCaseEmptyList();
    }

    @Test
    public void getHistoryCaseIncorrectData() {
        super.getHistoryCaseIncorrectData();
    }

    @Test
    public void getSubTaskCaseStandard() {
        super.getSubTaskCaseStandard();
    }

    @Test
    public void getSubTaskCaseEmptyList() {
        super.getSubTaskCaseEmptyList();
    }

    @Test
    public void getSubTaskCaseIncorrectData() {
        super.getSubTaskCaseIncorrectData();
    }

    @Test
    public void saveAndLoadCaseStandard() {
        addTasksForTests();
        addEpicsForTests();
        addSubTasksForTests();

        manager.getSubTask(subTask4.getId());

        FileBackedTasksManager loadingManager = FileBackedTasksManager.loadFromFile(
                new File("C:\\dev\\java-kanban\\Тестирование (7-й спринт).csv"));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");
        assertEquals(manager.getHistory(), List.of(subTask4), "Списки не совпадают");

    }

    @Test
    public void saveAndLoadCaseEmptyListOfTasks() {
        addEpicsForTests();
        addSubTasksForTests();

        manager.getSubTask(subTask4.getId());

        FileBackedTasksManager loadingManager = FileBackedTasksManager.loadFromFile(
                new File("C:\\dev\\java-kanban\\Тестирование (7-й спринт).csv"));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");
        assertEquals(manager.getHistory(), List.of(subTask4), "Списки не совпадают");
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
                new File("C:\\dev\\java-kanban\\Тестирование (7-й спринт).csv"));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");
    }

    @Test
    public void saveAndLoadCaseEmptyHistory() {
        addTasksForTests();
        addEpicsForTests();
        addSubTasksForTests();

        FileBackedTasksManager loadingManager = FileBackedTasksManager.loadFromFile(
                new File("C:\\dev\\java-kanban\\Тестирование (7-й спринт).csv"));

        assertEquals(manager.getTasks(), loadingManager.getTasks(), "Списки не совпадают");
        assertEquals(manager.getEpics(), loadingManager.getEpics(), "Списки не совпадают");
        assertEquals(manager.getAllSubTasks(), loadingManager.getAllSubTasks(), "Списки не совпадают");
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