package az.edu.turing.service.user;

import az.edu.turing.exception.CustomException;
import az.edu.turing.exception.UserNotFoundException;
import az.edu.turing.model.dto.user.UserDto;
import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.dao.repository.UserRepository;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserResponse getUser(@NotBlank String finCode) {
        UserEntity userEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponse userResponse = userMapper.entityToResponse(userEntity);
        return userResponse;
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> all = userRepository.findAll();
        return userMapper.entityListToDtoList(all);
    }

    public void deleteUser(@NotBlank String finCode) {
        UserEntity userEntity = userRepository.findByFinCode(finCode).orElseThrow(() -> new UserNotFoundException("Aybeniz tap;lmad;"));
        userRepository.delete(userEntity);
        log.info("User {} deleted", finCode);//todo
    }

    public UserRegisterRequest updateUser(@NotBlank String finCode, @NotNull UserRegisterRequest userRegisterRequest) {

        UserEntity existingUserEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRepository.existsByFinCode(userRegisterRequest.getFinCode()) &&
                !finCode.equals(userRegisterRequest.getFinCode())) {
            throw new CustomException("This FIN code already belongs to another user");
        }
        if (userRepository.existsByEmail(userRegisterRequest.getEmail()) &&
                !existingUserEntity.getEmail().equals(userRegisterRequest.getEmail())) {
            throw new CustomException("This email already belongs to another user");
        }

        if (userRepository.existsByPhoneNumber(userRegisterRequest.getPhoneNumber()) &&
                !existingUserEntity.getPhoneNumber().equals(userRegisterRequest.getPhoneNumber())) {
            throw new CustomException("This phone number already belongs to another user");
        }

        userMapper.updateEntityFromDto(userRegisterRequest, existingUserEntity);
        UserEntity updatedUserEntity = userRepository.save(existingUserEntity);

        UserRegisterRequest userRegister = userMapper.entityToRegisterRequest(updatedUserEntity);
        return userRegister;
    }
}
