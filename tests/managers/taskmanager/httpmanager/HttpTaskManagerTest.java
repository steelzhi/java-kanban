package managers.taskmanager.httpmanager;

import http.servers.KVServer;
import managers.taskmanager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer server;
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

    @BeforeEach
    public void setManager() throws IOException {
        server = new KVServer();
        server.start();
        manager = new HttpTaskManager("http://localhost:8080/");
    }

    @Test
    public void checkLoadingFromServer() {
        task1 = new Task("t1", "1", Status.NEW);
        task2 = new Task("t2", "1", Status.IN_PROGRESS);
        task3 = new Task("t3", "1", Status.DONE);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        epic1 = new Epic("e1", "2");
        epic2 = new Epic("e2", "2");
        epic3 = new Epic("e3", "2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        subTask3 = new SubTask("st3", "3", Status.NEW, epic2.getId());
        subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        manager.addSubTask(subTask4);

        HttpTaskManager loadingManager = manager.loadFromServer("http://localhost:8080/", manager.getKEY());

        assertEquals(manager.getTasks().size(), loadingManager.getTasks().size(),
                "Сохраняемый и загружаемый списки задач не совпадают");

        assertEquals(manager.getEpics().size(), loadingManager.getEpics().size(),
                "Сохраняемый и загружаемый списки эпиков не совпадают");

        assertEquals(manager.getAllSubTasks().size(), loadingManager.getAllSubTasks().size(),
                "Сохраняемый и загружаемый списки подзадач не совпадают");

        for (int i = 0; i < manager.getTasks().size(); i++) {
            assertEquals(manager.getTasks().get(i), loadingManager.getTasks().get(i),
                    "Задачи с номером " + i + " не совпадают");
        }

        for (int i = 0; i < manager.getEpics().size(); i++) {
            assertEquals(manager.getEpics().get(i), loadingManager.getEpics().get(i),
                    "Эпики с номером " + i + " не совпадают");
        }

        for (int i = 0; i < manager.getAllSubTasks().size(); i++) {
            assertEquals(manager.getAllSubTasks().get(i), loadingManager.getAllSubTasks().get(i),
                    "Подзадачи с номером " + i + " не совпадают");
        }

        assertEquals(manager.getTasks().size(), loadingManager.getTasks().size(),
                "Сохраняемый и загружаемый списки задач не совпадают");

        assertEquals(manager.getEpics().size(), loadingManager.getEpics().size(),
                "Сохраняемый и загружаемый списки эпиков не совпадают");

        assertEquals(manager.getAllSubTasks().size(), loadingManager.getAllSubTasks().size(),
                "Сохраняемый и загружаемый списки подзадач не совпадают");

        for (int i = 0; i < manager.getTasks().size(); i++) {
            assertEquals(manager.getTasks().get(i), loadingManager.getTasks().get(i),
                    "Задачи с номером " + i + " не совпадают");
        }

        for (int i = 0; i < manager.getEpics().size(); i++) {
            assertEquals(manager.getEpics().get(i), loadingManager.getEpics().get(i),
                    "Эпики с номером " + i + " не совпадают");
        }

        for (int i = 0; i < manager.getAllSubTasks().size(); i++) {
            assertEquals(manager.getAllSubTasks().get(i), loadingManager.getAllSubTasks().get(i),
                    "Подзадачи с номером " + i + " не совпадают");
        }

        assertEquals(manager.getTasks().size(), loadingManager.getTasks().size(),
                "Сохраняемый и загружаемый списки задач не совпадают");

        assertEquals(manager.getEpics().size(), loadingManager.getEpics().size(),
                "Сохраняемый и загружаемый списки эпиков не совпадают");

        assertEquals(manager.getAllSubTasks().size(), loadingManager.getAllSubTasks().size(),
                "Сохраняемый и загружаемый списки подзадач не совпадают");

        for (int i = 0; i < manager.getTasks().size(); i++) {
            assertEquals(manager.getTasks().get(i), loadingManager.getTasks().get(i),
                    "Задачи с номером " + i + " не совпадают");
        }

        for (int i = 0; i < manager.getEpics().size(); i++) {
            assertEquals(manager.getEpics().get(i), loadingManager.getEpics().get(i),
                    "Эпики с номером " + i + " не совпадают");
        }

        for (int i = 0; i < manager.getAllSubTasks().size(); i++) {
            assertEquals(manager.getAllSubTasks().get(i), loadingManager.getAllSubTasks().get(i),
                    "Подзадачи с номером " + i + " не совпадают");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }
}