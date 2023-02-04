package managers.taskmanager.filemanager;

import managers.historymanager.HistoryManager;
import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {

    private CSVTaskFormatter() {
    }

    public static String getTitle() {
        return "id,type,name,status,description,start,duration,epic\n";
    }

    public static String toString(Task task) {
        String taskParams = task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus().toString() + ","
                + task.getDescription() + ","
                + task.getStartTime() + ","
                + task.getDuration() + ",";

        if (task.getType() == TaskTypes.SUBTASK) {
            taskParams += task.getEpicId() + ",";
        }

        return taskParams + "\n";
    }

    public static Task fromString(String value) {
        String[] taskParams = value.split(",");

        final int id = Integer.valueOf(taskParams[0]);
        final TaskTypes type = TaskTypes.valueOf(taskParams[1]);
        final String name = taskParams[2];
        final Status status = Status.valueOf(taskParams[3]);
        final String description = taskParams[4];

        final LocalDateTime startTime;
        if (!taskParams[5].equals("null")) {
            startTime = LocalDateTime.parse(taskParams[5]);
        } else {
            startTime = null;
        }

        final int duration;
        if (!taskParams[6].equals(0)) {
            duration = Integer.parseInt(taskParams[6]);
        } else {
            duration = 0;
        }

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                task.setStartTime(startTime);
                task.setDuration(duration);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                return epic;
            case SUBTASK:
                int epicId = Integer.valueOf(taskParams[7]);
                SubTask subTask = new SubTask(name, description, status, epicId);
                subTask.setId(id);
                subTask.setStartTime(startTime);
                subTask.setDuration(duration);
                return subTask;
            default:
                return null;
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> viewedTasks = manager.getHistory();

        if (viewedTasks.isEmpty()) {
            return "";
        }

        StringBuilder viewedTasksIds = new StringBuilder();
        for (Task task : viewedTasks) {
            viewedTasksIds.append(task.getId() + ",");
        }
        viewedTasksIds.deleteCharAt(viewedTasksIds.length() - 1);

        return viewedTasksIds.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> viewedTasksIds = new ArrayList<>();

        String[] ids = value.split(",");
        for (String id : ids) {
            viewedTasksIds.add(Integer.parseInt(id));
        }

        return viewedTasksIds;
    }
}
