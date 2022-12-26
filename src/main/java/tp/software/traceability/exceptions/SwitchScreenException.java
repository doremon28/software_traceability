package tp.software.traceability.exceptions;

import java.io.Serial;

public class SwitchScreenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4735482142807002900L;

    public SwitchScreenException(String message, Throwable cause) {
        super(message, cause);
    }
}

