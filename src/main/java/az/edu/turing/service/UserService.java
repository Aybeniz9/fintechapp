package az.edu.turing.service;

import az.edu.turing.exception.UserNotFoundException;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.dao.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto create(@NotNull UserDto userDto) {
        UserEntity userEntity = userMapper.dtoToEntity(userDto);
        userRepository.save(userEntity);
        return userMapper.entityToDto(userEntity);
    }

    public UserDto getUser(@NotBlank String finCode) {
        UserEntity userEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.entityToDto(userEntity);
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> all = userRepository.findAll();
        return userMapper.entityListToDtoList(all);
    }

    public void deleteUser(@NotBlank String finCode) {
        UserEntity userEntity = userRepository.existsByFinCode(finCode).orElseThrow(()->new UserNotFoundException("User not found found for "));
        userRepository.delete(userEntity);
    }

    public UserDto updateUser(@NotBlank String finCode, @NotNull UserDto userDto) {
        UserEntity existingUserEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateEntityFromDto(userDto, existingUserEntity);
        UserEntity updatedUserEntity = userRepository.save(existingUserEntity);

        return userMapper.entityToDto(updatedUserEntity);
    }
}
