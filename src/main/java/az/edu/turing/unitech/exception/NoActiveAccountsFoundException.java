package az.edu.turing.unitech.exception;

public class NoActiveAccountsFoundException extends RuntimeException {

    public NoActiveAccountsFoundException(String message) {
           super(message);
    }

}
