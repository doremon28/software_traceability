package tp.software.traceability.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.software.traceability.exceptions.UserServiceException;
import tp.software.traceability.io.entities.UserEntity;
import tp.software.traceability.io.repositories.UserRepository;
import tp.software.traceability.services.UserService;
import tp.software.traceability.shared.dto.UserDto;
import tp.software.traceability.shared.utils.GenerateUtils;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GenerateUtils generateUtils;

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserServiceException("Record already exists");
        }
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userToSave = modelMapper.map(user, UserEntity.class);
        userToSave.setId(generateUtils.generateNumericUserId(10));
        UserEntity savedUser = userRepository.save(userToSave);
        UserDto returnValue = modelMapper.map(savedUser, UserDto.class);
        if (returnValue == null) {
            throw new UserServiceException("User not saved");
        }
        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userFound = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new UserServiceException("User not found");
        });
        return modelMapper.map(userFound, UserDto.class);
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        UserEntity userToFound = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new UserServiceException("User not found");
        });
        if (userToFound.getPassword().equals(password)) {
            return true;
        } else {
            throw new UserServiceException("Authentication failed");
        }
    }
}
