package http.servers;

public class KVTaskServerCreationException extends RuntimeException{
    public KVTaskServerCreationException(String message) {
        super(message);
    }

    public KVTaskServerCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
