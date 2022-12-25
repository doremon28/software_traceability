package tp.software.traceability.ui.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private double price;
    private Date expirationDate;
}
