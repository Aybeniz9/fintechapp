package az.edu.turing.exception;

public class InvalidCartNumberException extends RuntimeException {
    public InvalidCartNumberException(String message) {
        super(message);
    }
}