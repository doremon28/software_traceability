package tp.software.traceability.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ProductDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8578362746153635789L;

    private Long id;
    private String name;
    private double price;
    private Date expirationDate;
}
