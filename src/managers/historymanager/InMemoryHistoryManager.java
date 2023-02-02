package managers.historymanager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> viewedTasks = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        removeNode(viewedTasks.get(task.getId()));
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(viewedTasks.get(id));
    }

    public void linkLast(Task task) {
        if (task == null) {
            return;
        }

        Node<Task> newNode = new Node<>(task);

        if (head == null) {
            head = newNode;
        } else {
            newNode.setPrev(tail);
            tail.setNext(newNode);
        }
        tail = newNode;

        viewedTasks.put(task.getId(), newNode);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>();

        Node<Task> node = head;
        while (node != null) {
            taskArrayList.add(node.getData());
            node = node.getNext();
        }

        return taskArrayList;
    }

    public void removeNode(Node<Task> node) {
        if (head == null) {
            return;
        }

        if (viewedTasks.containsValue(node)) {
            if (node.getPrev() == null) {
                if (node.getNext() == null) {
                    head = null;
                    tail = null;
                } else {
                    head = node.getNext();
                    head.setPrev(null);
                }
            } else {
                if (node.getNext() == null) {
                    tail = node.getPrev();
                    tail.setNext(null);
                } else {
                    node.getPrev().setNext(node.getNext());
                    node.getNext().setPrev(node.getPrev());
                }
            }

            viewedTasks.remove(node.getData().getId());
        }
    }
}
