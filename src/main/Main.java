package main;

import managers.Managers;
import managers.taskmanager.InMemoryTaskManager;

/*
Никита, добрый день!
По 3-му спринту все ваши заключительные комментарии (по улучшению работающего кода) я учел и реализовал в 4-м спринте.
Спасибо!
 */

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }
}
