// Тесты findTaskById() и addTask() мешают друг другу: по отдельности они запускаются нормально. Если запустить их вместе,
// сбивается id и startTime у задач.

package http.servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.taskmanager.TaskManager;
import managers.taskmanager.TaskValidationException;
import managers.taskmanager.filemanager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import static http.servers.HttpTaskServer.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    FileBackedTasksManager manager = (FileBackedTasksManager) HttpTaskServer.getfileBackedTasksManager();
    Gson gson = new Gson();
    HttpClient client = HttpClient.newHttpClient();;
    URI url;
    HttpRequest request;
    HttpResponse<String> response;
    Type type;
    List<Task> taskList;
    HttpServer httpServer;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    SubTask subTask4;

    @BeforeEach
    public void addValuesAndActionsForTest() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks/task/", new HttpTaskServer.TasksHandler());
        httpServer.createContext("/tasks/subtask/", new HttpTaskServer.SubTasksHandler());
        httpServer.createContext("/tasks/epic/", new HttpTaskServer.EpicsHandler());
        httpServer.createContext("/tasks/history/", new HttpTaskServer.HistoryHandler());
        httpServer.createContext("/tasks/", new HttpTaskServer.PrioritizedTasksHandler());
        httpServer.start();
        System.out.println("Сервер начал работу.");

        LocalDateTime now = LocalDateTime.now();;

        task1 = new Task("t1", "1", Status.NEW);
        task2 = new Task("t2", "1", Status.IN_PROGRESS);
        task3 = new Task("t3", "1", Status.DONE);
        task4 = new Task("t4", "1", Status.DONE);
        task2.setStartTime(now.minusSeconds(1000));
        task2.setDuration(10);
        task3.setStartTime(now.plusSeconds(10001));
        task3.setDuration(100000);
        try {
            manager.addTask(task1);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            manager.addTask(task2);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            manager.addTask(task3);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            manager.addTask(task4);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }

        epic1 = new Epic("e1", "2");
        epic2 = new Epic("e2", "2");
        epic3 = new Epic("e3", "2");
        epic1.setStartTime(now);
        epic1.setDuration(10000);
        epic2.setStartTime(now);
        epic2.setDuration(10000);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        subTask3 = new SubTask("st3", "3", Status.NEW, epic2.getId());
        subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());
        subTask1.setStartTime(now.minusSeconds(1000000));
        subTask1.setDuration(100000);
        subTask2.setStartTime(now.minusSeconds(10000000));
        subTask2.setDuration(200000);
        subTask3.setStartTime(now.minusSeconds(2000000));
        subTask3.setDuration(400000);
        try {
            manager.addSubTask(subTask1);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            manager.addSubTask(subTask2);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            manager.addSubTask(subTask3);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            manager.addSubTask(subTask4);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }

        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(5);
        manager.getTask(1);
        manager.getTask(3);
        manager.getSubTask(8);
        manager.getEpic(4);
        manager.getSubTask(10);
        manager.getEpic(6);

        try {
            manager.addSubTask(subTask3);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        manager.getSubTask(11);

        /*HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            List<Task> taskList = gson.fromJson(response.body(), type);
            System.out.println(taskList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Task task5 = new Task("t6", "1", Status.DONE);
        String json = gson.toJson(task5);
        System.out.println(json);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/task/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            System.out.println("2");
        }

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            Task task = gson.fromJson(responseBody, Task.class);
            System.out.println(task);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

    @Test
    public void getTasks() {
        taskList = null;
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            type = new TypeToken<ArrayList<Task>>() {}.getType();
            taskList = gson.fromJson(response.body(), type);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getTasks().size(), taskList.size(), "Размеры списков не совпадают");

        for (int i = 0; i < manager.getTasks().size(); i++) {
            assertEquals(manager.getTasks().get(i), taskList.get(i), "Задачи с номером " + i + " не совпадают");
        }
    }

    @Test
    public void removeAllTasks() {
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertTrue(manager.getTasks().isEmpty(), "Список задач не пуст");
    }

    @Test
    public void findTaskById() {
        url = URI.create("http://localhost:8080/tasks/task/?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        Task task = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            task = gson.fromJson(response.body(), Task.class);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(task2, task,  "Задачи не совпадают");
    }

    @Test
    public void addTask() {
        Task task6 = new Task("t6", "1", Status.DONE);
        task6.setId(9);
        String json = gson.toJson(task6);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/task/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertTrue(manager.getTasks().contains(task6),  "Задачи не совпадают");
    }

    @Test
    public void updateTask() {
        Task task6 = new Task("t6", "1", Status.DONE);
        task6.setId(1);
        String json = gson.toJson(task6);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/task/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getTasks().get(0), task6,  "Задача некорректно обновлена");
    }

    @Test
    public void removeTask() {
        url = URI.create("http://localhost:8080/tasks/task/?id=" + task2.getId());
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertFalse(manager.getTasks().contains(task2), "Задача некорректно удалена");
    }

    @Test
    public void getTask() {
        Task task = null;
        url = URI.create("http://localhost:8080/tasks/task/?id=3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            task = gson.fromJson(response.body(), Task.class);
        } catch (IOException | InterruptedException e) {
        }

        System.out.println(manager.getTasks());

        assertEquals(task3, task, "Задачи не совпадают");
    }


    @AfterEach
    public void stopServer() {
        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubTasks();
        manager.resetTaskIds();
        httpServer.stop(0);
    }

}