package tp.software.traceability.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDto implements Serializable {


    private static final long serialVersionUID = 4524673998592433343L;
    private Long id;
    private String name;
    private int age;
    private String email;
    private String password;

    private List<ProductDto> products;
}
