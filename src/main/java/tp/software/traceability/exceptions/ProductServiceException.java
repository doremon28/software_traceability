package tp.software.traceability.exceptions;


public class ProductServiceException extends RuntimeException {

    private static final long serialVersionUID = 2289989766795352324L;

    public ProductServiceException(String message) {
        super(message);
    }
}
