package main;

import managers.Managers;
import managers.taskmanager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Instant;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

/*        Task task1 = new Task("t1", "1", Status.NEW);
        Task task2 = new Task("t2", "1", Status.IN_PROGRESS);
        task1.setDuration(1000);
        task1.setStartTime(Instant.now());
        task2.setDuration(10000);
        task2.setStartTime(Instant.now());
        System.out.println(task1.getEndTime());
        System.out.println(task2.getEndTime());

        Epic epic1 = new Epic("e1", "2");
        inMemoryTaskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        SubTask subTask3 = new SubTask("st3", "3", Status.NEW, epic1.getId());
        SubTask subTask4 = new SubTask("st4", "3", Status.DONE, epic1.getId());
        subTask1.setStartTime(Instant.now().minusSeconds(100000));
        subTask1.setDuration(1000);
        subTask2.setStartTime(Instant.now());
        subTask2.setDuration(2000000);
        subTask3.setStartTime(Instant.now());
        subTask3.setDuration(400000);
        inMemoryTaskManager.addSubTask(subTask1);
        inMemoryTaskManager.addSubTask(subTask2);
        inMemoryTaskManager.addSubTask(subTask3);
        inMemoryTaskManager.addSubTask(subTask4);

        System.out.println(epic1.getStartTime());
        System.out.println(epic1.getEndTime());*/

    }
}
