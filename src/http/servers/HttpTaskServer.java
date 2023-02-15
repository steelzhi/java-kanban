package http.servers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.taskmanager.TaskManager;
import managers.taskmanager.TaskValidationException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class HttpTaskServer {
    private TaskManager fileBackedTasksManager;
    private Gson gson = new Gson();
    private HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks/task/", new TasksHandler());
        httpServer.createContext("/tasks/subtask/", new SubTasksHandler());
        httpServer.createContext("/tasks/epic/", new EpicsHandler());
        httpServer.createContext("/tasks/history/", new HistoryHandler());
        httpServer.createContext("/tasks/", new PrioritizedTasksHandler());
        httpServer.start();
        System.out.println("Сервер начал работу");
        fileBackedTasksManager = Managers.getFileBackedTasksManager();
    }

    class TasksHandler implements HttpHandler {
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

    class SubTasksHandler implements HttpHandler {
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

    class EpicsHandler implements HttpHandler {
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

    class HistoryHandler implements HttpHandler {
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

    class PrioritizedTasksHandler implements HttpHandler {
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
        String pathAndQuery = uri.getPath();
        if (uri.getQuery() != null) {
            pathAndQuery += "?" + uri.getQuery();
        }

        if (!pathAndQuery.contains("/tasks/")) {
            return true;
        }

        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (pathAndQuery.equals("/tasks/task/")
                    || pathAndQuery.contains("/tasks/task/?id=")
                    || pathAndQuery.equals("/tasks/subtask/")
                    || pathAndQuery.contains("/tasks/subtask/?id=")
                    || pathAndQuery.equals("/tasks/epic/")
                    || pathAndQuery.contains("/tasks/epic/?id=")
                    || pathAndQuery.contains("/tasks/subtask/epic/?id=")
                    || pathAndQuery.equals("/tasks/history/")
                    || pathAndQuery.equals("/tasks/")) {
                return false;
            }
        } else if (method.equals("POST")) {
            if (pathAndQuery.contains("/tasks/task/")
                    || pathAndQuery.contains("/tasks/subtask/")
                    || pathAndQuery.contains("/tasks/epic/")) {
                return false;
            }
        } else if (method.equals("DELETE")) {
            if (pathAndQuery.equals("/tasks/task/")
                    || pathAndQuery.contains("/tasks/task/?id=")
                    || pathAndQuery.equals("/tasks/subtask/")
                    || pathAndQuery.contains("/tasks/subtask/?id=")
                    || pathAndQuery.equals("/tasks/epic/")
                    || pathAndQuery.contains("/tasks/epic/?id=")) {
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

    private <T> String toJson(List<T> tasks) {
        return gson.toJson(tasks);
    }

    private String toJson(Task task) {
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
            os.write("Неверно составлен запрос".getBytes());
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

    private boolean taskAlreadyExists(Task task) {
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

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public TaskManager getFileBackedTasksManager() {
        return fileBackedTasksManager;
    }
}


