// Тесты findTaskById() и addTask() мешают друг другу: по отдельности они запускаются нормально. Если запустить их вместе,
// сбивается id и startTime у задач.

package http.servers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.taskmanager.TaskValidationException;
import managers.taskmanager.filemanager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static http.servers.HttpTaskServer.getJsonInUrlFormat;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    FileBackedTasksManager manager;
    Gson gson = new Gson();
    HttpClient client = HttpClient.newHttpClient();
    URI url;
    HttpRequest request;
    HttpResponse<String> response;
    Type type;
    List<Task> taskList;
    List<Epic> epicList;
    List<SubTask> subTaskList;
    HttpTaskServer httpTaskServer;
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
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void addValuesAndActionsForTest() throws IOException {
        httpTaskServer = new HttpTaskServer();
        manager = (FileBackedTasksManager) httpTaskServer.getFileBackedTasksManager();

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
        url = URI.create("http://localhost:8080/tasks/task/?id=" + task2.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        Task task = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            task = gson.fromJson(response.body(), Task.class);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(task2, task, "Найденная задача не совпадает с искомой");
    }

    @Test
    public void addTask() {
        Task task6 = new Task("t6", "1", Status.DONE);
        task6.setId(manager.getCurrentTaskId());
        String json = gson.toJson(task6);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/task/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertTrue(manager.getTasks().contains(task6), "Ошибка добавления задачи в список");
    }

    @Test
    public void updateTask() {
        Task task6 = new Task("t6", "1", Status.DONE);
        task6.setId(task1.getId());
        String json = gson.toJson(task6);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/task/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getTasks().get(0), task6, "Задача некорректно обновлена");
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
        url = URI.create("http://localhost:8080/tasks/task/?id=" + task3.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            task = gson.fromJson(response.body(), Task.class);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(task3, task, "Задачи не совпадают");
    }

    @Test
    public void getEpics() {
        epicList = null;
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            type = new TypeToken<ArrayList<Epic>>() {
            }.getType();
            epicList = gson.fromJson(response.body(), type);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getEpics().size(), epicList.size(), "Размеры списков не совпадают");

        for (int i = 0; i < manager.getEpics().size(); i++) {
            assertEquals(manager.getEpics().get(i), epicList.get(i), "Эпики с номером " + i + " не совпадают");
        }
    }

    @Test
    public void removeAllEpics() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertTrue(manager.getEpics().isEmpty(), "Список эпиков не пуст");
    }

    @Test
    public void findEpicById() {
        url = URI.create("http://localhost:8080/tasks/epic/?id=" + epic1.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        Epic epic = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            epic = gson.fromJson(response.body(), Epic.class);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(epic1, epic, "Найденный эпик не совпадает с искомым");
    }

    @Test
    public void addEpic() {
        Epic epic4 = new Epic("e4", "2");
        epic4.setId(manager.getCurrentTaskId());
        String json = gson.toJson(epic4);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/epic/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertTrue(manager.getEpics().contains(epic4), "Ошибка добавления эпика в список");
    }

    @Test
    public void updateEpic() {
        Epic epic4 = new Epic("e4", "2");
        epic4.setId(epic1.getId());
        String json = gson.toJson(epic4);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/epic/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getEpics().get(0), epic4, "Эпик некорректно обновлен");
    }

    @Test
    public void removeEpic() {
        url = URI.create("http://localhost:8080/tasks/epic/?id=" + epic2.getId());
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertFalse(manager.getEpics().contains(epic2), "Эпик некорректно удален");
    }

    @Test
    public void getSubTasksInEpic() {
        url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + epic3.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        List<SubTask> subtaskList = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            type = new TypeToken<ArrayList<SubTask>>() {
            }.getType();
            subtaskList = gson.fromJson(response.body(), type);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getSubTasksInEpic(epic3.getId()).size(), subtaskList.size(),
                "Размеры списков не совпадают");

        for (int i = 0; i < manager.getSubTasksInEpic(epic3.getId()).size(); i++) {
            assertEquals(manager.getSubTasksInEpic(epic3.getId()).get(i), subtaskList.get(i),
                    "Подзадачи с номером " + i + " из эпика " + epic3.getId() + " не совпадают");
        }
    }

    @Test
    public void getEpic() {
        Epic epic = null;
        url = URI.create("http://localhost:8080/tasks/epic/?id=" + epic2.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            epic = gson.fromJson(response.body(), Epic.class);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(epic2, epic, "Эпики не совпадают");
    }

    @Test
    public void getAllSubTasks() {
        subTaskList = null;
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            type = new TypeToken<ArrayList<SubTask>>() {
            }.getType();
            subTaskList = gson.fromJson(response.body(), type);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getAllSubTasks().size(), subTaskList.size(), "Размеры списков не совпадают");

        for (int i = 0; i < manager.getAllSubTasks().size(); i++) {
            assertEquals(manager.getAllSubTasks().get(i), subTaskList.get(i),
                    "Подзадачи с номером " + i + " не совпадают");
        }
    }

    @Test
    public void removeAllSubTasks() {
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertTrue(manager.getAllSubTasks().isEmpty(), "Список подзадач не пуст");
    }

    @Test
    public void findSubTaskById() {
        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subTask4.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        SubTask subTask = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            subTask = gson.fromJson(response.body(), SubTask.class);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(subTask4, subTask, "Найденная подзадача не совпадает с искомой");
    }

    @Test
    public void addSubTask() {
        SubTask subTask5 = new SubTask("st5", "3", Status.DONE, epic3.getId());
        subTask5.setId(manager.getCurrentTaskId());
        String json = gson.toJson(subTask5);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/subtask/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertTrue(manager.getAllSubTasks().contains(subTask5), "Ошибка добавления подзадачи в список");
    }

    @Test
    public void updateSubTask() {
        SubTask subTask5 = new SubTask("st5", "3", Status.DONE, epic3.getId());
        subTask5.setId(subTask4.getId());
        String json = gson.toJson(subTask5);
        String uri = getJsonInUrlFormat(json, "http://localhost:8080/tasks/subtask/");

        url = URI.create(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).POST(body).uri(url).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getAllSubTasks().get(0), subTask5, "Подзадача некорректно обновлена");
    }

    @Test
    public void removeSubTask() {
        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subTask4.getId());
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        }

        assertFalse(manager.getAllSubTasks().contains(subTask4), "Подзадача некорректно удалена");
    }

    @Test
    public void getSubTask() {
        SubTask subTask = null;
        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subTask4.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            subTask = gson.fromJson(response.body(), SubTask.class);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(subTask4, subTask, "Подзадачи не совпадают");
    }

    @Test
    public void getHistory() {
        List<Task> historyList = null;
        url = URI.create("http://localhost:8080/tasks/history/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            historyList = gson.fromJson(response.body(), type);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getHistory().size(), historyList.size(), "Размеры списков не совпадают");

        for (int i = 0; i < manager.getHistory().size(); i++) {
            assertEquals(manager.getHistory().get(i).getId(), historyList.get(i).getId(),
                    "Задачи с номером " + i + " в истории не совпадают");
        }
    }

    @Test
    public void getPrioritizedTasks() {
        List<Task> prioritizedTaskList = null;
        url = URI.create("http://localhost:8080/tasks/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            prioritizedTaskList = gson.fromJson(response.body(), type);
        } catch (IOException | InterruptedException e) {
        }

        assertEquals(manager.getPrioritizedTasks().size(), prioritizedTaskList.size(), "Размеры списков не совпадают");

        for (int i = 0; i < manager.getPrioritizedTasks().size(); i++) {
            assertEquals(manager.getPrioritizedTasks().get(i).getId(), prioritizedTaskList.get(i).getId(),
                    "Задачи с номером " + i + " в приоритизированном списке не совпадают");
        }
    }

    @AfterEach
    public void stopServer() {
        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubTasks();
        manager.resetTaskIds();
        httpTaskServer.getHttpServer().stop(0);
    }
}