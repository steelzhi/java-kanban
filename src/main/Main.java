package main;

import managers.Managers;
import managers.taskmanager.InMemoryTaskManager;

/*
Никита, приветствую!
Спасибо за замечания и рекомендации! Я все поправил и реализовал.
 */

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }
}
