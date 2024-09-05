package az.edu.turing.unitech.exception;

public class CannotDeleteActiveAccountException extends RuntimeException {
    public CannotDeleteActiveAccountException(String message) {
        super(message);
    }
}
