package main;

import managers.Managers;
import managers.historymanager.InMemoryHistoryManager;
import managers.taskmanager.InMemoryTaskManager;
import status.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

/*
Никита, здравствуйте.
Спасибо за коррективы! Все поправил, проверьте, пожалуйста.
 */

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }
}
