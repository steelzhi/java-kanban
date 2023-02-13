package main;

import http.servers.KVServer;
import http.client.KVTaskClient;
import managers.Managers;
import managers.taskmanager.TaskValidationException;
import managers.taskmanager.httpmanager.HttpTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer server = new KVServer();
        server.start();
        HttpTaskManager httpTaskManager = (HttpTaskManager) Managers.getDefault();
        KVTaskClient taskClient = httpTaskManager.getTaskClient();

        taskClient.put("2", "This is value");
        System.out.println(taskClient.load("2"));
        taskClient.put("3", "fff!");
        System.out.println(taskClient.load("3"));
        taskClient.put("2", "This is new value");
        System.out.println(taskClient.load("2"));

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
            httpTaskManager.addTask(task1);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            httpTaskManager.addTask(task2);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            httpTaskManager.addTask(task3);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            httpTaskManager.addTask(task4);
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
        httpTaskManager.addEpic(epic1);
        httpTaskManager.addEpic(epic2);
        httpTaskManager.addEpic(epic3);

        SubTask subTask1 = new SubTask("st1", "3", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask2 = new SubTask("st2", "3", Status.DONE, epic1.getId());
        SubTask subTask3 = new SubTask("st3", "3", Status.NEW, epic2.getId());
        SubTask subTask4 = new SubTask("st4", "3", Status.DONE, epic3.getId());
        subTask1.setStartTime(LocalDateTime.now().minusSeconds(1000000));
        subTask1.setDuration(100000);
        subTask2.setStartTime(LocalDateTime.now().minusSeconds(10000000));
        subTask2.setDuration(200000);
        subTask3.setStartTime(LocalDateTime.now().minusSeconds(2000000));
        subTask3.setDuration(400000);
        try {
            httpTaskManager.addSubTask(subTask1);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            httpTaskManager.addSubTask(subTask2);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            httpTaskManager.addSubTask(subTask3);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
        try {
            httpTaskManager.addSubTask(subTask4);
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(taskClient.load("3"));
        System.out.println(taskClient.load("7"));

        // При тестировании через Insomnia этот метод нужно закомментировать:
        server.stop();
    }
}
