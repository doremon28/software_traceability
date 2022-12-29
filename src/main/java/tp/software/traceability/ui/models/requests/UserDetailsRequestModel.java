package tp.software.traceability.ui.models.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class UserDetailsRequestModel {

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, message = "Name must not be less than 2 characters")
    private String name;

    @NotEmpty(message = "Age cannot be empty")
    @Size(min = 1, message = "Age must not be less than 1 characters")
    private int age;
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password must be equal or greater than 8 characters and less than 16 characters")
    private String password;

}
