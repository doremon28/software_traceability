package tp.software.traceability.ui.gui.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.software.traceability.services.UserService;
import tp.software.traceability.shared.dto.UserDto;
import tp.software.traceability.ui.gui.controllers.UserController;
import tp.software.traceability.ui.models.requests.UserDetailsRequestModel;
import tp.software.traceability.ui.models.responses.UserResponse;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class UserControllerImpl implements UserController {

    private final UserService userService;
    @Override
    public UserResponse createUser(UserDetailsRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userToCreate = modelMapper.map(userDetails, UserDto.class);
        return modelMapper.map(userService.createUser(userToCreate), UserResponse.class);
    }

    @Override
    public UserResponse getUser(String email) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userService.getUser(email), UserResponse.class);
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        return userService.authenticateUser(email, password);
    }
}
