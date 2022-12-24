package tp.software.traceability.exceptions;

public class UserException extends RuntimeException {

    private static final long serialVersionUID = 2289989766795352324L;

    public UserException(String message) {
        super(message);
    }
}
