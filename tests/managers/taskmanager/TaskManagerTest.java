package managers.taskmanager;


import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @Test
    public void getTasksCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task("t2", "2", Status.NEW);
        manager.addTask(task2);
        Task task3 = new Task("t3", "3", Status.NEW);
        manager.addTask(task3);
        assertEquals(List.of(task1, task2, task3), manager.getTasks(), "Неверный список задач");
    }

    @Test
    public void getTasksCaseEmptyList() {
        assertTrue(manager.getTasks().isEmpty(), "Список задач - не пустой");
    }

    @Test
    public void removeAllTasksCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        Task task2 = new Task("t2", "2", Status.DONE);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.removeAllTasks();
        assertTrue(manager.getTasks().isEmpty(), "Не все задачи удалены из списка");
    }

    @Test
    public void removeAllTasksCaseEmptyList() {
        manager.removeAllTasks();
        assertTrue(manager.getTasks().isEmpty(),
                "После удаления задач из пустого списка в списке появились задачи");
    }

    @Test
    public void findTaskByIdCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        Task task2 = new Task("t2", "2", Status.DONE);
        manager.addTask(task1);
        manager.addTask(task2);
        int id = task2.getId();
        assertEquals(task2, manager.getTask(id), "Найденная по id задача не совпадает с искомой");
    }

    @Test
    public void findTaskByIdCaseEmptyList() {
        assertNull(manager.getTask(1), "В пустом списке найдена задача");
    }

    @Test
    public void findTaskByIdCaseIncorrectData() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        int taskDoesNotExistId = task1.getId() + 1;
        assertNull(manager.getTask(taskDoesNotExistId), "В списке найдена задача с несуществующим id");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getTasks().get(taskDoesNotExistId));

        assertEquals("Index " + taskDoesNotExistId + " out of bounds for length " + manager.getTasks().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void addTaskCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        assertEquals(List.of(task1), manager.getTasks(), "Задача не добавлена в список");
    }

    @Test
    public void updateTaskCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        int id = task1.getId();
        Task task2 = new Task("t3", "3", Status.IN_PROGRESS);
        manager.updateTask(id, task2);
        assertEquals(List.of(task2), manager.getTasks(), "Задача некорректно обновлена");
    }

    @Test
    public void updateTaskCaseEmptyList() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.updateTask(1, task1);
        assertNull(manager.getTask(1), "В пустом списке обнаружена задача");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getTasks().get(1));

        assertEquals("Index " + 1 + " out of bounds for length " + manager.getTasks().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void updateTaskCaseIncorrectData() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        int id = task1.getId() + 1;
        Task task2 = new Task("t3", "3", Status.IN_PROGRESS);
        manager.updateTask(id, task2);
        assertNull(manager.getTask(id), "В списке найдена задача с несуществующим id");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getTasks().get(id));

        assertEquals("Index " + id + " out of bounds for length " + manager.getTasks().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void removeTaskCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        manager.removeTask(task1.getId());
        assertTrue(manager.getTasks().isEmpty(), "Задача не удалена из списка");
    }

    @Test
    public void removeTaskCaseEmptyList() {
        manager.removeTask(1);
        assertTrue(manager.getTasks().isEmpty(), "В пустом списке после удаления обнаружена задача");
    }

    @Test
    public void removeTaskCaseIncorrectData() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        manager.removeTask(task1.getId() + 1);
        assertEquals(List.of(task1), manager.getTasks(),
                "После удаления несуществующей задачи список задач - неверный");
    }

    @Test
    public void getTaskCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        assertEquals(task1, manager.getTask(task1.getId()), "Найденная задача не совпадает с искомой");
    }

    @Test
    public void getTaskCaseEmptyList() {
        assertNull(manager.getTask(1), "В пустом списке найдена задача");
    }

    @Test
    public void getTaskCaseIncorrectData() {
        Task task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        int id = task1.getId() + 1;
        assertNull(manager.getTask(id), "В списке найдена задача с несуществующим id");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getTasks().get(id));

        assertEquals("Index " + id + " out of bounds for length " + manager.getTasks().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void getEpicsCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        Epic epic2 = new Epic("e2", "2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        boolean doesEpicMapContainsAllEpicsAdded = true;
        if (!manager.getEpics().contains(epic1) || !manager.getEpics().contains(epic2)) {
            doesEpicMapContainsAllEpicsAdded = false;
        }

        assertTrue(doesEpicMapContainsAllEpicsAdded, "Список эпиков не содержит все добавленные эпики");
    }

    @Test
    public void getEpicsCaseEmptyList() {
        assertTrue(manager.getEpics().isEmpty(), "Список эпиков - не пустой");
    }

    @Test
    public void removeAllEpicsCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        Epic epic2 = new Epic("e2", "2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.removeAllEpics();
        assertTrue(manager.getEpics().isEmpty(), "Не все эпики удалены из списка");
    }

    @Test
    public void removeAllEpicsCaseEmptyList() {
        manager.removeAllEpics();
        assertTrue(manager.getEpics().isEmpty(),
                "После удаления эпиков из пустого списка в списке появились эпики");
    }


    @Test
    public void findEpicByIdCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        Epic epic2 = new Epic("e2", "2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        assertEquals(epic2, manager.getEpic(epic2.getId()), "Найденный по id эпик не совпадает с искомым");
    }

    @Test
    public void findEpicByIdCaseEmptyList() {
        assertNull(manager.getEpic(1), "В пустом списке найден эпик");
    }

    @Test
    public void findEpicByIdCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        int id = epic1.getId() + 1;
        assertNull(manager.getEpic(id), "В списке найден эпик с несуществующим id");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getTasks().get(id));

        assertEquals("Index " + id + " out of bounds for length " + manager.getTasks().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void addEpicCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("e2", "22");
        manager.addEpic(epic2);
        List<Epic> epics = manager.getEpics();
        epics.remove(epic1);
        epics.remove(epic2);

        assertTrue(epics.isEmpty(), "Эпики были некорректно добавлены в список");
    }

    @Test
    public void addEpicCaseEmptyList() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        assertEquals(List.of(epic1), manager.getEpics(), "Эпик был некорректно добавлен в пустой список");
    }


    @Test
    public void updateEpicCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("e2", "4");
        manager.updateEpic(epic1.getId(), epic2);
        assertEquals(List.of(epic2), manager.getEpics(), "Эпик некорректно обновлен");
    }

    @Test
    public void updateEpicCaseEmptyList() {
        Epic epic1 = new Epic("e1", "2");
        Epic epic2 = new Epic("e2", "4");
        manager.updateEpic(epic1.getId(), epic2);

        assertNull(manager.getEpic(epic1.getId()), "В пустом списке обнаружен эпик");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getEpics().get(epic1.getId()));

        assertEquals("Index " + epic1.getId() + " out of bounds for length " + manager.getEpics().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void updateEpicCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("e2", "4");
        int id = epic1.getId() + 100;
        manager.updateEpic(id, epic2);
        assertNull(manager.getEpic(id), "В списке найден эпик с несуществующим id");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getEpics().get(id));

        assertEquals("Index " + id + " out of bounds for length " + manager.getEpics().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void removeEpicCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        manager.removeEpic(epic1.getId());
        assertTrue(manager.getEpics().isEmpty(), "Эпик не удален из списка");
    }

    @Test
    public void removeEpicCaseEmptyList() {
        manager.removeEpic(1);
        assertTrue(manager.getEpics().isEmpty(), "В пустом списке после удаления обнаружен эпик");
    }

    @Test
    public void getSubTasksInEpicCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(List.of(subTask1, subTask2), manager.getSubTasksInEpic(epic1.getId()),
                "Добавленные в список подзадачи не совпадают с реальными");
    }

    @Test
    public void getSubTasksInEpicCaseEmptyList() {
        assertNull(manager.getSubTasksInEpic(1), "В не созданном эпике найдены не созданные подзадачи");
    }

    @Test
    public void getEpicCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        assertEquals(epic1, manager.getEpic(epic1.getId()), "Найденный эпик не совпадает с искомым");
    }

    @Test
    public void getEpicCaseEmptyList() {
        assertNull(manager.getEpic(10), "В пустом списке найден эпик");
    }


    @Test
    public void countEpicStatusCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        assertEquals(Status.NEW, epic1.getStatus(), "Для нового эпика неверно рассчитан статус");

        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.addSubTask(subTask1);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(),
                "Для эпика, в который добавлена подзадача, неверно обновляется статус");

        manager.removeAllSubTasks();
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.addSubTask(subTask2);
        assertEquals(Status.DONE, epic1.getStatus(),
                "Для эпика, в который добавлена подзадача, неверно обновляется статус");

        manager.removeAllSubTasks();
        assertEquals(Status.NEW, epic1.getStatus(),
                "Для эпика, в котором удалены все подзадачи, неверно обновляется статус");
    }

    @Test
    public void countEpicStatusCaseEmptyList() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        assertEquals(Status.NEW, epic1.getStatus(), "Для эпика без подзадач неверно рассчитывается статус");

        manager.removeAllSubTasks();
        assertEquals(Status.NEW, epic1.getStatus(),
                "Для эпика, в котором удалены все подзадачи, неверно обновляется статус");
    }

    @Test
    public void getAllSubTasksCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(List.of(subTask1, subTask2), manager.getAllSubTasks(), "Неверный список подзадач");
    }

    @Test
    public void getAllSubTasksCaseEmptyList() {
        assertEquals(new ArrayList<>(), manager.getAllSubTasks(), "Список подзадач - не пустой");
    }

    @Test
    public void removeAllSubTasksCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.removeAllSubTasks();
        assertTrue(manager.getAllSubTasks().isEmpty(), "Не все подзадачи удалены из списка");
    }

    @Test
    public void removeAllSubTasksCaseEmptyList() {
        manager.removeAllSubTasks();
        assertTrue(manager.getAllSubTasks().isEmpty(),
                "После удаления подзадач из пустого списка в списке появились подзадачи");
    }

    @Test
    public void findSubTaskByIdCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(subTask2, manager.getSubTask(subTask2.getId()),
                "Найденная по id подзадача не совпадает с искомой");
    }

    @Test
    public void findSubTaskByIdCaseEmptyList() {
        assertNull(manager.getSubTask(1), "В пустом списке найдена подзадача");
    }

    @Test
    public void findSubTaskByIdCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.addSubTask(subTask1);
        int id = subTask1.getId() + 1;
        assertNull(manager.getSubTask(id), "В списке найдена подзадача с несуществующим id");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getAllSubTasks().get(id));

        assertEquals("Index " + id + " out of bounds for length " + manager.getAllSubTasks().size(),
                ex.getMessage(), "В списке найдена подзадача с несуществующим id");
    }

    @Test
    public void addSubTaskCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.addSubTask(subTask1);
        assertEquals(List.of(subTask1), manager.getAllSubTasks(), "Подзадача не добавлена в список");
    }


    @Test
    public void addSubTaskCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId() + 1);
        manager.addSubTask(subTask1);
        assertNull(manager.getSubTask(subTask1.getId()),
                "Не привязанная к эпику подзадача добавлена в список подзадач");
    }

    @Test
    public void updateSubTaskCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.updateSubTask(subTask1.getId(), subTask2);
        assertEquals(subTask2, manager.getSubTask(subTask1.getId()), "Подзадача некорректно обновлена");
    }

    @Test
    public void updateSubTaskCaseEmptyList() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.updateSubTask(10, subTask2);
        assertNull(manager.getSubTask(10), "В пустом списке обнаружена подзадача");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getAllSubTasks().get(10));

        assertEquals("Index " + 10 + " out of bounds for length " + manager.getAllSubTasks().size(),
                ex.getMessage(), "В списке найдена подзадача с несуществующим id");
    }

    @Test
    public void updateSubTaskCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        int id = subTask1.getId() + 10;
        manager.updateSubTask(id, subTask2);
        assertNull(manager.getSubTask(id), "В списке найдена задача с несуществующим id");

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> manager.getAllSubTasks().get(id));

        assertEquals("Index " + id + " out of bounds for length " + manager.getAllSubTasks().size(),
                ex.getMessage(), "В списке найдена задача с несуществующим id");
    }

    @Test
    public void removeSubTaskCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.removeSubTask(subTask1.getId());
        assertTrue(manager.getAllSubTasks().size() == 1, "Подзадача некорректно удалена из списка");
        manager.removeSubTask(subTask2.getId());
        assertTrue(manager.getAllSubTasks().isEmpty(), "Подзадачи некорректно удалены из списка");
    }

    @Test
    public void removeSubTaskCaseEmptyList() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.removeSubTask(subTask1.getId());
        assertTrue(manager.getAllSubTasks().isEmpty(), "В пустом списке после удаления обнаружена подзадача");
    }

    @Test
    public void removeSubTaskCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.addSubTask(subTask1);
        manager.removeSubTask(subTask1.getId() + 10);
        assertTrue(manager.getAllSubTasks().size() == 1,
                "После удаления несуществующей подзадачи список подзадач - неверный");
    }

    @Test
    public void doesEpicExistForSubTaskCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getEpicId());
        assertNotNull(subTask1.getEpicId(), "Подзадача не добавлена к эпику");
    }

    @Test
    public void doesEpicExistForSubTaskCaseEmptyList() {
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, 1);
        assertNull(manager.getEpic(1), "Подзадача добавлена к несуществующему эпику");
    }

    @Test
    public void doesEpicExistForSubTaskCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getEpicId() + 1);
        assertNull(manager.getSubTask(subTask1.getId()), "Подзадача добавлена к несуществующему эпику");
    }

    @Test
    public void getHistoryCaseStandard() {
        Task task1 = new Task("t1", "1", Status.NEW);
        Task task2 = new Task("t2", "1", Status.IN_PROGRESS);
        Task task3 = new Task("t3", "1", Status.DONE);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.getTask(task1.getId());
        manager.getTask(task3.getId());
        List<Task> history = manager.getHistory();
        assertEquals(List.of(task1, task3), history, "Просмотренные задачи не совпадают с задачами из истории");
    }

    @Test
    public void getHistoryCaseEmptyList() {
        Task task1 = new Task("t1", "1", Status.NEW);
        Task task2 = new Task("t2", "1", Status.IN_PROGRESS);
        Task task3 = new Task("t3", "1", Status.DONE);
        List<Task> history = manager.getHistory();
        assertEquals(new ArrayList<>(), history, "В истории отображаются не просмотренные задачи");
    }

    @Test
    public void getHistoryCaseIncorrectData() {
        Task task1 = new Task("t1", "1", Status.NEW);
        Task task2 = new Task("t2", "1", Status.IN_PROGRESS);
        Task task3 = new Task("t3", "1", Status.DONE);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.getTask(task1.getId() + 1000);
        manager.getTask(task3.getId() + 100);
        List<Task> history = manager.getHistory();
        assertEquals(new ArrayList<>(), history, "В истории отображаются не просмотренные задачи");
    }

    @Test
    public void getSubTaskCaseStandard() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(subTask1, manager.getSubTask(subTask1.getId()), "Не получена запрошенная подзадача");
    }

    @Test
    public void getSubTaskCaseEmptyList() {
        assertNull(manager.getSubTask(10), "Получена не существующая подзадача");
    }

    @Test
    public void getSubTaskCaseIncorrectData() {
        Epic epic1 = new Epic("e1", "2");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        manager.addSubTask(subTask1);
        assertNull(manager.getSubTask(subTask1.getId() + 10), "Получена не существующая подзадача");
    }
}

