package tp.software.traceability.ui.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WrapperResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String status;
    private String code;
    private String message;
    private T data;

}
