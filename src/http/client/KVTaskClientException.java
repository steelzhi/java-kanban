package http.client;

public class KVTaskClientException extends RuntimeException{
    public KVTaskClientException(String message) {
        super(message);
    }

    public KVTaskClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
