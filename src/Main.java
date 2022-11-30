import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        while (true) {
            printMainMenu();
            int answer = scanner.nextInt();
            if (answer == 1) {
                while (true) {
                    System.out.println();
                    printTaskMenu();
                    answer = scanner.nextInt();
                    if (answer == 1) {
                        taskManager.getTasks();
                    } else if (answer == 2) {
                        taskManager.removeAllTasks();
                    } else if (answer == 3) {
                        taskManager.findTaskById();
                    } else if (answer == 4) {
                        Task task = createTaskByParams();
                        taskManager.addTask(task);
                    } else if (answer == 5) {
                        if (taskManager.isTasksMapEmpty()) {
                            taskManager.writeAboutEmptyMap();
                        } else {
                            System.out.println("Доступные задачи:");
                            taskManager.getTasks();
                            System.out.println("Введите id задачи:");
                            int id = getId();
                            if (taskManager.doesTaskManagerContainTask(id)) {
                                Task task = createTaskByParams();
                                taskManager.updateTask(id, task);
                            } else
                                taskManager.writeAboutMissingId();
                        }
                    } else if (answer == 6) {
                        taskManager.removeTask();
                    } else if (answer == 0) {
                        System.out.println();
                        break;
                    } else {
                        System.out.println("Выбранного пункта меню не существует! Выберите корректный пункт.\n");
                    }
                }
            } else if (answer == 2) {
                while (true) {
                    System.out.println();
                    printEpicMenu();
                    answer = scanner.nextInt();
                    if (answer == 1) {
                        taskManager.getEpics();
                    } else if (answer == 2) {
                        taskManager.removeAllEpics();
                    } else if (answer == 3) {
                        taskManager.findEpicById();
                    } else if (answer == 4) {
                        Epic epic = createEpicByParams();
                        taskManager.addEpic(epic);
                    } else if (answer == 5) {
                        if (taskManager.isEpicsMapEmpty()) {
                            taskManager.writeAboutEmptyMap();
                        } else {
                            System.out.println("Доступные эпики:");
                            taskManager.getEpics();
                            System.out.println("Введите id эпика:");
                            int id = getId();
                            if (taskManager.doesTaskManagerContainEpic(id)) {
                                Epic epic = createEpicByParams();
                                taskManager.updateEpic(id, epic);
                            } else
                                taskManager.writeAboutMissingId();
                        }
                    } else if (answer == 6) {
                        taskManager.removeEpic();
                    } else if (answer == 7) {
                        taskManager.getSubTasksInEpic();
                    } else if (answer == 0) {
                        System.out.println();
                        break;
                    } else {
                        System.out.println("Выбранного пункта меню не существует! Выберите корректный пункт.\n");
                    }
                }
            } else if (answer == 3) {
                while (true) {
                    System.out.println();
                    printSubTaskMenu();
                    answer = scanner.nextInt();
                    if (answer == 1) {
                        taskManager.getAllSubTasks();
                    } else if (answer == 2) {
                        taskManager.removeAllSubTasks();

                    } else if (answer == 3) {
                        taskManager.findSubTaskById();
                    } else if (answer == 4) {
                        if (taskManager.isEpicsMapEmpty()) {
                            System.out.println("Список эпиков пуст. Создайте сперва эпик.\n");
                            break;
                        } else {
                            SubTask subTask;
                            System.out.println("Введите id эпика, к которому относится подзадача.");
                            int epicId;
                            while (true) {
                                System.out.println("Доступные эпики:");
                                taskManager.getEpics();
                                epicId = scanner.nextInt();
                                if (!taskManager.doesEpicExist(epicId)) {
                                    System.out.println("Эпика с таким id не существует! Введите правильный id.");
                                } else {
                                    subTask = createSubTaskByParams();
                                    break;
                                }
                            }
                            taskManager.addSubTask(epicId, subTask);
                        }
                    } else if (answer == 5) {
                        if (taskManager.isSubTasksMapEmpty()) {
                            taskManager.writeAboutEmptyMap();
                        } else {
                            System.out.println("Доступные подзадачи:");
                            taskManager.getAllSubTasks();
                            System.out.println("Введите id подзадачи:");
                            int id = getId();
                            if (taskManager.doesTaskManagerContainSubTask(id)) {
                                SubTask subTask = createSubTaskByParams();
                                taskManager.updateSubTask(id, subTask);
                            } else
                                taskManager.writeAboutMissingId();
                        }
                    } else if (answer == 6) {
                        taskManager.removeSubTask();
                    } else if (answer == 0) {
                        System.out.println();
                        break;
                    } else {
                        System.out.println("Выбранного пункта меню не существует! Выберите корректный пункт.\n");
                    }
                }
            } else if (answer == 0) {
                return;
            } else {
                System.out.println("Выбранного пункта меню не существует! Выберите корректный пункт.\n");
            }
        }
    }

    public static void printMainMenu() {
        System.out.println("С каким типом задач Вы будете работать?");
        System.out.println("1 - Обычные задачи");
        System.out.println("2 - Эпики");
        System.out.println("3 - Подзадачи");
        System.out.println("0 - Выйти из приложения");
    }

    public static void printTaskMenu() {
        System.out.println("Что Вы хотите сделать?");
        System.out.println("1 - Получить список задач");
        System.out.println("2 - Удалить все задачи");
        System.out.println("3 - Найти задачу по id");
        System.out.println("4 - Добавить новую задачу");
        System.out.println("5 - Обновить задачу с заданным id");
        System.out.println("6 - Удалить задачу с заданным id");
        System.out.println("0 - Вернуться в главное меню");
    }

    public static void printEpicMenu() {
        System.out.println("Что Вы хотите сделать?");
        System.out.println("1 - Получить список эпиков");
        System.out.println("2 - Удалить все эпики");
        System.out.println("3 - Найти эпик по id");
        System.out.println("4 - Добавить новый эпик");
        System.out.println("5 - Обновить эпик с заданным id");
        System.out.println("6 - Удалить эпик с заданным id");
        System.out.println("7 - Получить список всех подзадач эпика");
        System.out.println("0 - Вернуться в главное меню");
    }

    public static void printSubTaskMenu() {
        System.out.println("Что Вы хотите сделать?");
        System.out.println("1 - Получить список подзадач");
        System.out.println("2 - Удалить все подзадачи");
        System.out.println("3 - Найти подзадачу по id");
        System.out.println("4 - Добавить новую подзадачу");
        System.out.println("5 - Обновить подзадачу с заданным id");
        System.out.println("6 - Удалить подзадачу с заданным id");
        System.out.println("0 - Вернуться в главное меню");
    }

    private static Task createTaskByParams() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название задачи:");
        String name = scanner.next();
        System.out.println("Введите описание задачи:");
        String description = scanner.next();
        String status = getCorrectStatus();
        return new Task(name, description, status);
    }

    private static Epic createEpicByParams() {
        System.out.println("Введите название эпика:");
        String name = scanner.next();
        System.out.println("Введите описание эпика:");
        String description = scanner.next();
        return new Epic(name, description, null);
    }

    private static SubTask createSubTaskByParams() {
        System.out.println("Введите название подзадачи:");
        String name = scanner.next();
        System.out.println("Введите описание подзадачи:");
        String description = scanner.next();
        String status = getCorrectStatus();

        return new SubTask(name, description, status);
    }

    private static String getCorrectStatus() {
        while (true) {
            System.out.println("Введите статус.");
            System.out.println("Подсказка: доступные статусы - \"NEW\", \"IN_PROGRESS\", \"DONE\".");
            String status = scanner.next();
            if (!status.equals("NEW") && !status.equals("IN_PROGRESS") && !status.equals("DONE")) {
                System.out.println("Введен неверный статус!");
            } else {
                System.out.println();
                return status;
            }
        }
    }

    private static int getId() {
        System.out.println("Введите id:");
        return scanner.nextInt();
    }
}
