package http.servers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.taskmanager.TaskManager;
import managers.taskmanager.TaskValidationException;
import managers.taskmanager.filemanager.FileBackedTasksManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static TaskManager fileBackedTasksManager = Managers.getFileBackedTasksManager();
    static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        System.out.println("Сервер готов к работе.");

        httpServer.createContext("/tasks/task/", new TasksHandler());
        httpServer.createContext("/tasks/subtask/", new SubTasksHandler());
        httpServer.createContext("/tasks/epic/", new EpicsHandler());
        httpServer.createContext("/tasks/history/", new HistoryHandler());
        httpServer.createContext("/tasks/", new PrioritizedTasksHandler());

        httpServer.start();
        //addValuesAndActionsForTest((FileBackedTasksManager) fileBackedTasksManager);

    }

    public static TaskManager getfileBackedTasksManager() {
        return fileBackedTasksManager;
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String path = uri.toString();
            String query = "/tasks/task/";

            if (isQueryIncorrect(exchange)) {
                writeError400Message(exchange, query);
                return;
            }

            if (path.length() > query.length()) {
                if (exchange.getRequestMethod().equals("GET")) {
                    if (path.contains("id=")) {
                        int taskId = getTaskId(path);

                        sendResponseHeaders("/tasks/task/?id=" + taskId, exchange, 200);
                        String task = toJson(fileBackedTasksManager.findTaskById(taskId));
                        writeResultToOutputStream(exchange, task);
                        fileBackedTasksManager.getTask(taskId);
                    }
                } else if (exchange.getRequestMethod().equals("POST")) {
                    String taskInJsonFormat = getTaskInJsonFormat(path, query);

                    Task task = gson.fromJson(taskInJsonFormat, Task.class);
                    sendResponseHeaders("/tasks/task/" + taskInJsonFormat, exchange, 200);

                    try {
                        if (taskAlreadyExists(task)) {
                            fileBackedTasksManager.updateTask(task.getId(), task);
                            writeResultToOutputStream(exchange, "Задача успешно обновлена");
                        } else {
                            fileBackedTasksManager.addTask(task);
                            writeResultToOutputStream(exchange, "Задача успешно добавлена");
                        }
                    } catch (TaskValidationException e) {
                        writeError406Message(exchange, query);
                    }
                } else if (exchange.getRequestMethod().equals("DELETE")) {
                    if (path.contains("id=")) {
                        int taskId = getTaskId(path);
                        sendResponseHeaders("/tasks/task/?id=" + taskId, exchange, 200);
                        fileBackedTasksManager.removeTask(taskId);
                        writeResultToOutputStream(exchange,
                                "Задача с id = " + taskId + " удалена либо ее не было в списке.");
                    }
                }
            } else if (exchange.getRequestMethod().equals("GET")) {
                sendResponseHeaders("/tasks/task/", exchange, 200);
                String tasks = toJson(fileBackedTasksManager.getTasks());
                writeResultToOutputStream(exchange, tasks);
            } else if (exchange.getRequestMethod().equals("DELETE")) {
                sendResponseHeaders("/tasks/task/", exchange, 200);
                fileBackedTasksManager.removeAllTasks();
                writeResultToOutputStream(exchange, "Все задачи удалены.");
            }
        }
    }

    static class SubTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String path = uri.toString();
            String query = "/tasks/subtask/";

            if (isQueryIncorrect(exchange)) {
                writeError400Message(exchange, query);
                return;
            }

            if (path.length() > query.length()) {
                if (exchange.getRequestMethod().equals("GET")) {
                    if (path.contains("epic") && path.contains("id=")) {
                        int epicId = getTaskId(path);

                        sendResponseHeaders("/tasks/subtask/epic/?id=" + epicId, exchange, 200);
                        List<SubTask> subTasksInEpic = fileBackedTasksManager.getSubTasksInEpic(epicId);
                        String subTasks = toJson(subTasksInEpic);
                        writeResultToOutputStream(exchange, subTasks);
                        for (SubTask subTask : subTasksInEpic) {
                            fileBackedTasksManager.getSubTask(subTask.getId());
                        }
                    } else if (path.contains("id=")) {
                        int subTaskId = getTaskId(path);

                        sendResponseHeaders("/tasks/subtask/?id=" + subTaskId, exchange, 200);
                        String subTask = toJson(fileBackedTasksManager.findSubTaskById(subTaskId));
                        writeResultToOutputStream(exchange, subTask);
                        fileBackedTasksManager.getSubTask(subTaskId);
                    }
                } else if (exchange.getRequestMethod().equals("POST")) {
                    String subTaskInJsonFormat = getTaskInJsonFormat(path, query);
                    SubTask subTask = gson.fromJson(subTaskInJsonFormat, SubTask.class);
                    sendResponseHeaders("/tasks/subtask/" + subTaskInJsonFormat, exchange, 200);

                    try {
                        if (taskAlreadyExists(subTask)) {
                            fileBackedTasksManager.updateSubTask(subTask.getId(), subTask);
                            writeResultToOutputStream(exchange, "Подзадача успешно обновлена");
                        } else {
                            fileBackedTasksManager.addSubTask(subTask);
                            writeResultToOutputStream(exchange, "Подзадача успешно добавлена");
                        }
                    } catch (TaskValidationException e) {
                        writeError406Message(exchange, query);
                    }
                } else if (exchange.getRequestMethod().equals("DELETE")) {
                    if (path.contains("id=")) {
                        int subTaskId = getTaskId(path);

                        sendResponseHeaders("/tasks/subtask/?id=" + subTaskId, exchange, 200);
                        fileBackedTasksManager.removeSubTask(subTaskId);
                        writeResultToOutputStream(exchange,
                                "Подзадача с id = " + subTaskId + " удалена либо ее не было в списке.");
                    }
                }
            } else if (exchange.getRequestMethod().equals("GET")) {
                sendResponseHeaders("/tasks/subtask/", exchange, 200);
                String subTasks = toJson(fileBackedTasksManager.getAllSubTasks());
                writeResultToOutputStream(exchange, subTasks);
            } else if (exchange.getRequestMethod().equals("DELETE")) {
                sendResponseHeaders("/tasks/subtask/", exchange, 200);
                fileBackedTasksManager.removeAllSubTasks();
                writeResultToOutputStream(exchange, "Все подзадачи удалены.");
            }
        }
    }

    static class EpicsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String path = uri.toString();
            String query = "/tasks/epic/";

            if (isQueryIncorrect(exchange)) {
                writeError400Message(exchange, query);
                return;
            }

            if (path.length() > query.length()) {
                if (exchange.getRequestMethod().equals("GET")) {
                    if (path.contains("id=")) {
                        int epicId = getTaskId(path);

                        sendResponseHeaders("/tasks/epic/?id=" + epicId, exchange, 200);
                        String epic = toJson(fileBackedTasksManager.findEpicById(epicId));
                        writeResultToOutputStream(exchange, epic);
                        fileBackedTasksManager.getEpic(epicId);
                    }
                } else if (exchange.getRequestMethod().equals("POST")) {
                    String epicInJsonFormat = getTaskInJsonFormat(path, query);

                    Epic epic = gson.fromJson(epicInJsonFormat, Epic.class);

                    sendResponseHeaders("/tasks/epic/" + epicInJsonFormat, exchange, 200);

                    try {
                        if (taskAlreadyExists(epic)) {
                            fileBackedTasksManager.updateEpic(epic.getId(), epic);
                            writeResultToOutputStream(exchange, "Эпик успешно обновлен");
                        } else {
                            fileBackedTasksManager.addEpic(epic);
                            writeResultToOutputStream(exchange, "Эпик успешно добавлен");
                        }
                    } catch (TaskValidationException e) {
                        writeError406Message(exchange, query);
                    }
                } else if (exchange.getRequestMethod().equals("DELETE")) {
                    if (path.contains("id=")) {
                        int epicId = getTaskId(path);

                        sendResponseHeaders("/tasks/epic/?id=" + epicId, exchange, 200);
                        fileBackedTasksManager.removeEpic(epicId);
                        writeResultToOutputStream(exchange,
                                "Эпик с id = " + epicId + " удален либо его не было в списке.");
                    }
                }
            } else if (exchange.getRequestMethod().equals("GET")) {
                sendResponseHeaders("/tasks/epic/", exchange, 200);
                String epics = toJson(fileBackedTasksManager.getEpics());
                writeResultToOutputStream(exchange, epics);
            } else if (exchange.getRequestMethod().equals("DELETE")) {
                sendResponseHeaders("/tasks/epic/", exchange, 200);
                fileBackedTasksManager.removeAllEpics();
                writeResultToOutputStream(exchange, "Все эпики удалены.");
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = "/tasks/history/";

            if (isQueryIncorrect(exchange)) {
                writeError400Message(exchange, query);
                return;
            }

            sendResponseHeaders(query, exchange, 200);
            String history = toJson(fileBackedTasksManager.getHistory());
            writeResultToOutputStream(exchange, history);
        }
    }

    static class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = "/tasks/";

            if (isQueryIncorrect(exchange)) {
                writeError400Message(exchange, query);
                return;
            }

            sendResponseHeaders(query, exchange, 200);
            String prioritizedTasks = toJson(fileBackedTasksManager.getPrioritizedTasks());
            writeResultToOutputStream(exchange, prioritizedTasks);
        }
    }

    private static boolean isQueryIncorrect(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        if (!path.contains("/tasks/")) {
            return true;
        }

        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (path.equals("/tasks/task/")
                    || path.contains("/tasks/task/?id=")
                    || path.equals("/tasks/subtask/")
                    || path.contains("/tasks/subtask/?id=")
                    || path.equals("/tasks/epic/")
                    || path.contains("/tasks/epic/?id=")
                    || path.contains("/tasks/subtask/epic/?id=")
                    || path.equals("/tasks/history/")
                    || path.equals("/tasks/")) {
                return false;
            }
        } else if (method.equals("POST")) {
            if (path.contains("/tasks/task/")
                    || path.contains("/tasks/subtask/")
                    || path.contains("/tasks/epic/")) {
                return false;
            }
        } else if (method.equals("DELETE")) {
            if (path.equals("/tasks/task/")
                    || path.contains("/tasks/task/?id=")
                    || path.equals("/tasks/subtask/")
                    || path.contains("/tasks/subtask/?id=")
                    || path.equals("/tasks/epic/")
                    || path.contains("/tasks/epic/?id=")) {
                return false;
            }
        }
        return true;
    }

    private static void sendResponseHeaders(String query, HttpExchange exchange, int code) throws IOException {
        System.out.println("Началась обработка " + query + " запроса от клиента.");
        if (code == 200) {
            exchange.sendResponseHeaders(200, 0);
        } else if (code == 400) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    public static String getTaskInJsonFormat(String path, String query) {
        String taskWithNormalSymbols = path.replace("%22", "")
                .replace("%7B", "{")
                .replace("%7D", "}")
                .replace("%5B", "[")
                .replace("%5D", "]");

        String taskInJsonFormat = taskWithNormalSymbols.substring(path.indexOf(query)
                + query.length());

        return taskInJsonFormat;
    }

    private static <T> String toJson(List<T> tasks) {
        return gson.toJson(tasks);
    }

    private static String toJson(Task task) {
        return gson.toJson(task);
    }

    private static void writeResultToOutputStream(HttpExchange exchange, String output) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            if (output.equals("null")) {
                os.write("Задач не найдено".getBytes());
                return;
            }
            os.write(output.getBytes());
        }
    }

    private static void writeError400Message(HttpExchange exchange, String query) throws IOException {
        sendResponseHeaders(query, exchange, 400);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Неверно составлен запрос.".getBytes());
        }
    }

    private static void writeError406Message(HttpExchange exchange, String query) throws IOException {
        sendResponseHeaders(query, exchange, 406);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Невозможно добавить/изменить задачу, т.к. она пересекается по времени с другими задачами"
                    .getBytes());
        }
    }

    private static int getTaskId(String path) {
        String idInURI = path.substring(path.indexOf("=") + 1);

        try {
            int id = Integer.parseInt(idInURI);
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Введен некорректный id");
            return -1;
        }
    }

    private static boolean taskAlreadyExists(Task task) {
        if (task instanceof Epic) {
            List<Epic> epics = fileBackedTasksManager.getEpics();
            for (Epic epic : epics) {
                if (epic.getId() == task.getId()) {
                    return true;
                }
            }
            return false;
        }

        if (task instanceof SubTask) {
            List<SubTask> subTasks = fileBackedTasksManager.getAllSubTasks();
            for (SubTask subTask : subTasks) {
                if (subTask.getId() == task.getId()) {
                    return true;
                }
            }
            return false;
        }

        List<Task> tasks = fileBackedTasksManager.getTasks();
        for (Task t : tasks) {
            if (t.getId() == task.getId()) {
                return true;
            }
        }
        return false;
    }

    public static String getJsonInUrlFormat(String json, String urlAndQuery) {
        String jsonWithUrlSymbols = json.replace("\"", "%22")
                .replace("{", "%7B")
                .replace("}", "%7D")
                .replace("[", "%5B")
                .replace("]", "%5D");

        String jsonInUrlFormat = urlAndQuery + jsonWithUrlSymbols;
        return jsonInUrlFormat;
    }

    /*private static void addValuesAndActionsForTest(FileBackedTasksManager manager) {
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

        Epic epic1 = new Epic("e1", "2");
        Epic epic2 = new Epic("e2", "2");
        Epic epic3 = new Epic("e3", "2");
        epic1.setStartTime(LocalDateTime.now());
        epic1.setDuration(10000);
        epic2.setStartTime(LocalDateTime.now());
        epic2.setDuration(10000);

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        String epic2ToJson = gson.toJson(epic2);
        System.out.println(epic2ToJson);

        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        SubTask subTask3 = new SubTask("st3", "3", Status.NEW, epic2.getId());
        SubTask subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());
        subTask1.setStartTime(LocalDateTime.now().minusSeconds(1000000));
        subTask1.setDuration(100000);

        String subTask1ToJson = gson.toJson(subTask1);
        System.out.println(subTask1ToJson);

        subTask2.setStartTime(LocalDateTime.now().minusSeconds(10000000));
        subTask2.setDuration(200000);
        subTask3.setStartTime(LocalDateTime.now().minusSeconds(2000000));
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
        System.out.println("S1: " + subTask1.getStartTime() + " " + subTask1.getEndTime());
        System.out.println("S2: " + subTask2.getStartTime() + " " + subTask2.getEndTime());
        System.out.println("S3: " + subTask3.getStartTime() + " " + subTask3.getEndTime());
        System.out.println("S4: " + subTask4.getStartTime() + " " + subTask4.getEndTime());

        System.out.println("Epic: " + epic1.getStartTime() + " " + epic1.getEndTime());

        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(5);
        manager.getTask(1);
        manager.getTask(3);
        manager.getSubTask(8);
        manager.getEpic(4);
        manager.getSubTask(10);
        manager.getEpic(6);

        manager.updateTask(1, new Task("t1", "fasdfas", Status.DONE));

        List<Task> prioritizedtasks = manager.getPrioritizedTasks();
        System.out.println("Задачи в порядке приоритета:");
        for (Task task : prioritizedtasks) {
            System.out.println(task);
        }

        //savingManager.removeAllSubTasks();

        try {
            manager.addSubTask(subTask3);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        manager.getSubTask(11);

        manager.removeEpic(4);

        HttpClient client = HttpClient.newHttpClient();
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
        }

    }*/

}
