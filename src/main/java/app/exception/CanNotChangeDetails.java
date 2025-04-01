package app.exception;

public class CanNotChangeDetails extends RuntimeException{
    public CanNotChangeDetails(String message) {
        super(message);
    }
}
