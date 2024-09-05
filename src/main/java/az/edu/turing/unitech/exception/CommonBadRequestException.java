package az.edu.turing.unitech.exception;

public class CommonBadRequestException extends RuntimeException {
    public CommonBadRequestException(String message) {
        super(message);
    }
}
