package tp.software.traceability.exceptions;

import java.io.Serial;

public class ProductServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2289989766795352324L;

    public ProductServiceException(String message) {
        super(message);
    }
}
