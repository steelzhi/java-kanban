package tasks;

import managers.taskmanager.memorymanager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {
    InMemoryTaskManager manager;
    Epic epic;
    SubTask subTask1;
    SubTask subTask2;

    @BeforeEach
    private void createManagerAndEpic() {
        manager = new InMemoryTaskManager();
        epic = new Epic("e1", "2");
        manager.addEpic(epic);
    }

    //Расчет статуса для эпика с пустым списком подзадач
    @Test
    public void epicWithEmptySubTaskList() {
        assertEquals(Status.NEW, epic.getStatus(), "Статус не соответствует NEW");
    }

    // Расчет статуса для эпика со списком подзадач, у которых статус равен Status.NEW
    @Test
    public void epicWithSubTaskWithStatusNEW() {
        createSubTasksWithStatusNEW();
        addSubTasks();
        assertEquals(Status.NEW, epic.getStatus(), "Статус не соответствует NEW");

    }

    // Расчет статуса для эпика со списком подзадач, у которых статус равен Status.DONE
    @Test
    public void epicWithSubTaskWithStatusDONE() {
        createSubTasksWithStatusNEW();
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        addSubTasks();
        assertEquals(Status.DONE, epic.getStatus(), "Статус не соответствует DONE");
    }

    // Расчет статуса для эпика со списком подзадач, у которых статусы равны Status.NEW и Status.DONE
    @Test
    public void epicWithSubTaskWithStatusNEWAndDONE() {
        createSubTasksWithStatusNEW();
        subTask2.setStatus(Status.DONE);
        addSubTasks();
        assertEquals(Status.NEW, epic.getStatus(), "Статус не соответствует NEW");
    }

    // Расчет статуса для эпика со списком подзадач, у которых статус равен Status.IN_PROGRESS
    @Test
    public void epicWithSubTaskWithStatusIN_PROGRESS() {
        createSubTasksWithStatusNEW();
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        addSubTasks();
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус не соответствует IN_PROGRESS");
    }

    @Test
    public void checkInstantAndDurationInEpicAndSubTasks() {
        LocalDateTime now = LocalDateTime.now();
        epic.setStartTime(now);
        epic.setEndTime(now.plusSeconds(6000));
        assertEquals(now, epic.getStartTime(), "Неверно рассчитывается время старта эпика");
        assertEquals(6000 / 60, (int) Duration.between(epic.getStartTime(), epic.getEndTime()).toMinutes(),
                "Неверно рассчитывается длительность эпика");

        SubTask subTask3 = new SubTask("st1", "3", Status.NEW, epic.getId());
        subTask3.setStartTime(now);
        subTask3.setDuration(120);
        manager.addSubTask(subTask3);

        assertEquals(now, subTask3.getStartTime(), "Неверно рассчитывается время старта подзадачи");
        assertEquals(120, subTask3.getDuration(),"Неверно рассчитывается длительность подзадачи");
        assertEquals(now.plusSeconds(120 * 60), epic.getEndTime(),
                "Неверно рассчитывается время окончания эпика с подзадачей");

        SubTask subTask4 = new SubTask("st1", "3", Status.NEW, epic.getId());
        subTask3.setStartTime(now.minusSeconds(3600));
        subTask3.setDuration(80);
        manager.addSubTask(subTask4);

        assertEquals(now.minusSeconds(3600), epic.getStartTime(),
                "Неверно рассчитывается время начала эпика с подзадачей");
    }

    private void createSubTasksWithStatusNEW() {
        subTask1 = new SubTask("st1", "3", Status.NEW, epic.getId());
        subTask2 = new SubTask("st2", "3", Status.NEW, epic.getId());
    }

    private void addSubTasks() {
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
    }
}