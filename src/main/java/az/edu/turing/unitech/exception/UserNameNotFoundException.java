package az.edu.turing.unitech.exception;

public class UserNameNotFoundException extends RuntimeException{

    public UserNameNotFoundException(String message) {
        super(message);
    }
}
