package main;

import managers.Managers;
import managers.historymanager.InMemoryHistoryManager;
import managers.taskmanager.InMemoryTaskManager;
import status.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

/*
Никита, приветствую!
С Новым годом, всех Вам благ!
Проверьте, пожалуйста, выполнение задания по 5-му спринту.
 */

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }
}
