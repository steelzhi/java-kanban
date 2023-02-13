package managers.taskmanager.httpmanager;

import com.google.gson.Gson;
import http.client.KVTaskClient;
import http.servers.KVServer;
import managers.Managers;
import managers.taskmanager.TaskValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest {
    KVServer server;
    HttpTaskManager httpTaskManager;
    KVTaskClient taskClient;
    static Gson gson = new Gson();

    @BeforeEach
    public void addValuesAndActionsForTest() throws IOException, InterruptedException {
        server = new KVServer();
        server.start();
        httpTaskManager = (HttpTaskManager) Managers.getDefault();
        taskClient = httpTaskManager.getTaskClient();
    }

    @Test
    void addTask() {
        Task task1 = new Task("t1", "1", Status.NEW);
        String loadedTask = null;
        try {
            httpTaskManager.addTask(task1);
            loadedTask = taskClient.load(String.valueOf(task1.getId()));
        } catch (TaskValidationException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(toJson(task1), loadedTask, "Загруженная задача не совпадает с сохраненной");
    }

    @Test
    void updateTask() {
        Task task1 = new Task("t1", "1", Status.NEW);
        try {
            httpTaskManager.addTask(task1);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }

        Task task2 = new Task("t2", "1", Status.NEW);
        task2.setId(task1.getId());
        try {
            httpTaskManager.updateTask(task1.getId(), task2);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(server.getData().get(String.valueOf(task1.getId())),
                toJson(task2), "Задача некорректно обновлена");
    }

    @Test
    void addEpic() {
        Epic epic1 = new Epic("e1", "2");
        String loadedEpic = null;
        try {
            httpTaskManager.addEpic(epic1);
            loadedEpic = taskClient.load(String.valueOf(epic1.getId()));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(toJson(epic1), loadedEpic, "Загруженный эпик не совпадает с сохраненным");
    }

    @Test
    void updateEpic() {
        Epic epic1 = new Epic("e1", "2");
        httpTaskManager.addEpic(epic1);
        Epic epic2 = new Epic("e2", "2");
        epic2.setId(epic1.getId());
        httpTaskManager.updateEpic(epic1.getId(), epic2);

        assertEquals(server.getData().get(String.valueOf(epic1.getId())),
                toJson(epic2), "Эпик некорректно обновлен");
    }

    @Test
    void addSubTask() {
        Epic epic3 = new Epic("e3", "2");
        httpTaskManager.addEpic(epic3);
        SubTask subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());

        String loadedSubTask = null;
        try {
            httpTaskManager.addSubTask(subTask4);
            loadedSubTask = taskClient.load(String.valueOf(subTask4.getId()));
        } catch (TaskValidationException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(toJson(subTask4), loadedSubTask, "Загруженная подзадача не совпадает с сохраненной");
    }

    @Test
    void updateSubTask() {
        Epic epic3 = new Epic("e3", "2");
        httpTaskManager.addEpic(epic3);
        SubTask subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());
        try {
            httpTaskManager.addSubTask(subTask4);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }

        SubTask subTask5 = new SubTask("st5", "55", Status.DONE, epic3.getId());
        subTask5.setId(subTask4.getId());
        try {
            httpTaskManager.updateTask(subTask4.getId(), subTask5);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(server.getData().get(String.valueOf(subTask4.getId())),
                toJson(subTask5), "Подзадача некорректно обновлена");
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    private static String toJson(Task task) {
        return gson.toJson(task);
    }
}