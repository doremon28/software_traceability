package tp.software.traceability.services;

import tp.software.traceability.shared.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    boolean authenticateUser(String email, String password);
}
