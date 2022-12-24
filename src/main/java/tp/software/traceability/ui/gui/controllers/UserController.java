package tp.software.traceability.ui.gui.controllers;

import tp.software.traceability.ui.models.requests.UserDetailsRequestModel;
import tp.software.traceability.ui.models.responses.UserResponse;

public interface UserController {
    UserResponse createUser(UserDetailsRequestModel userDetails);
    UserResponse getUser(String email);
    boolean authenticateUser(String email, String password);

}
