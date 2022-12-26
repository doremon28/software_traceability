package tp.software.traceability.ui.models.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ProductRequest {
    private Long userId;
    private String name;
    private double price;
    private Date expirationDate;

}
