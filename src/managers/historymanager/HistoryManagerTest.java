package managers.historymanager;

import managers.taskmanager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    InMemoryTaskManager manager;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Task task5;
    ArrayList<Task> viewedTasks;

    @BeforeEach
    private void initManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void addCaseEmptyList() {
        addTasks();
        manager.getTask(task1.getId());
        manager.getTask(task3.getId());
        manager.getTask(task2.getId());
        // Текущий порядок по id в истории: 1, 3, 2

        getHistory();
        assertEquals(List.of(task1, task3, task2), viewedTasks,
                "При добавлении задач в пустой список и их дальнейшем просмотре история отображается неверно");
    }

    @Test
    void getHistoryEmptyList() {
        assertEquals(new ArrayList<>(), manager.getHistory());
    }

    @Test
    void getHistoryDuplication() {
        addTasks();
        manager.getTask(task1.getId());
        manager.getTask(task3.getId());
        manager.getTask(task1.getId());
        manager.getTask(task3.getId());
        manager.getTask(task2.getId());
        manager.getTask(task2.getId());
        // Текущий порядок по id в истории: 1, 3, 2

        getHistory();
        assertEquals(List.of(task1, task3, task2), viewedTasks,
                "При повторном просмотре задач история отображается неверно");
    }

    @Test
    void removeEmptyList() {
        task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        manager.removeTask(1);
        assertEquals(new ArrayList<>(), manager.getHistory(),
                "При очистке истории не все задачи удаляются из истории");
    }

    @Test
    void removeFromStart() {
        addTasks();
        get5DifferentTasks();

        manager.removeTask(task5.getId());

        getHistory();
        assertEquals(List.of(task1, task3, task4, task2), viewedTasks,
                "Некорректное удаление задачи из начала истории");
    }

    @Test
    void removeFromMiddle() {
        addTasks();
        get5DifferentTasks();

        manager.removeTask(task3.getId());

        getHistory();
        assertEquals(List.of(task5, task1, task4, task2), viewedTasks,
                "Некорректное удаление задачи из середины истории");
    }

    @Test
    void removeFromEnd() {
        addTasks();
        get5DifferentTasks();

        manager.removeTask(task2.getId());

        getHistory();
        assertEquals(List.of(task5, task1, task3, task4), viewedTasks, "Некорректное удаление задачи из конца истории");
    }

    private void addTasks() {
        task1 = new Task("t1", "1", Status.NEW);
        manager.addTask(task1);
        task2 = new Task("t2", "2", Status.NEW);
        manager.addTask(task2);
        task3 = new Task("t3", "3", Status.NEW);
        manager.addTask(task3);
        task4 = new Task("t4", "4", Status.NEW);
        manager.addTask(task4);
        task5 = new Task("t5", "5", Status.NEW);
        manager.addTask(task5);
    }

    private void get5DifferentTasks() {
        manager.getTask(task1.getId());
        manager.getTask(task3.getId());
        manager.getTask(task5.getId());
        manager.getTask(task1.getId());
        manager.getTask(task3.getId());
        manager.getTask(task4.getId());
        manager.getTask(task2.getId());
        // Текущий порядок по id в истории: 5, 1, 3, 4, 2
    }

    private void getHistory() {
        viewedTasks = (ArrayList<Task>) manager.getHistory();
    }
}