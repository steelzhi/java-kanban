package managers.historymanager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private final Map<Integer, Node<Task>> viewedTasksMap = new HashMap<>();
    private final CustomLinkedList<Task> viewedTasksList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        viewedTasksList.removeNode(viewedTasksMap.get(task.getId()));
        viewedTasksList.linkLast(task);

        viewedTasksMap.put(task.getId(), new Node<>(task));
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasksList.getTasks();
    }

    @Override
    public void remove(int id) {
        viewedTasksList.removeNode(viewedTasksMap.get(id));
        viewedTasksMap.remove(id);
    }

    public static class CustomLinkedList<T> extends LinkedList<T> {
        public Node<T> head;
        public Node<T> tail;

        public void linkLast(T task) {
            Node<T> newNode = new Node<>(task);

            if (head == null) {
                head = newNode;
            } else if (tail == null) {
                tail = newNode;
                head.next = tail;
                tail.prev = head;
            } else {
                newNode.prev = tail;
                tail.next = newNode;
                tail = newNode;
            }
        }

        public ArrayList<T> getTasks() {
            ArrayList<T> taskArrayList = new ArrayList<>();

            if (head != null) {
                Node<T> node = head;
                while (node != null) {
                    taskArrayList.add(node.data);
                    node = node.next;
                }
            }

            return taskArrayList;
        }

        public void removeNode(Node<Task> node) {
            if (head == null) {
                return;
            }

            Node<T> currentNode = head;
            while (currentNode != null) {
                if (currentNode.equals(node)) {
                    if (currentNode.next == null) {
                        if (currentNode.prev == null) {
                            head = null;
                            tail = null;
                        } else {
                            tail = currentNode.prev;
                            tail.next = null;
                        }
                    } else {
                        if (currentNode.prev == null) {
                            head = currentNode.next;
                            head.prev = null;
                        } else {
                            currentNode.prev.next = currentNode.next;
                            currentNode.next.prev = currentNode.prev;
                        }
                    }
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }
}
