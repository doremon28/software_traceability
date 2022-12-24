package tp.software.traceability.exceptions;

public class UserServiceException extends RuntimeException {
    private static final long serialVersionUID = 9025453784649279609L;

    public UserServiceException(String message) {
        super(message);
    }
}
