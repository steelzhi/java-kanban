package managers.taskmanager;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(String message){
        super(message);
    }
}
