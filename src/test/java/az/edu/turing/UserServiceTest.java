package az.edu.turing;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.dao.repository.UserRepository;
import az.edu.turing.exception.CustomException;
import az.edu.turing.exception.UserNotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.dto.user.UserDto;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserResponse;
import az.edu.turing.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserSuccess() {
        // Given
        String finCode = "1234567";
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();

        when(userRepository.findByFinCode(finCode)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToResponse(userEntity)).thenReturn(userResponse);

        // When
        UserResponse result = userService.getUser(finCode);

        // Then
        assertNotNull(result);
        assertEquals(userResponse, result);
        verify(userRepository).findByFinCode(finCode);
        verify(userMapper).entityToResponse(userEntity);
    }

    @Test
    void testGetUserNotFound() {
        // Given
        String finCode = "1234567";
        when(userRepository.findByFinCode(finCode)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUser(finCode));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByFinCode(finCode);
    }
    @Test
    void testGetAllUsers() {
        // Given
        List<UserEntity> userEntities = Arrays.asList(new UserEntity(), new UserEntity());
        List<UserDto> userDtos = Arrays.asList(new UserDto(), new UserDto());

        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.entityListToDtoList(userEntities)).thenReturn(userDtos);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(userDtos, result);
        verify(userRepository).findAll();
        verify(userMapper).entityListToDtoList(userEntities);
    }
    @Test
    void testDeleteUserSuccess() {
        // Given
        String finCode = "1234567";
        UserEntity userEntity = new UserEntity();

        when(userRepository.findByFinCode(finCode)).thenReturn(Optional.of(userEntity));

        // When
        userService.deleteUser(finCode);

        // Then
        verify(userRepository).findByFinCode(finCode);
        verify(userRepository).delete(userEntity);
        // Optionally verify log output using a logging framework if necessary
    }

    @Test
    void testDeleteUserNotFound() {
        // Given
        String finCode = "1234567";
        when(userRepository.findByFinCode(finCode)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(finCode));
        assertEquals("Aybeniz tap;lmad;", exception.getMessage());
        verify(userRepository).findByFinCode(finCode);
        verify(userRepository, never()).delete(any(UserEntity.class));
    }
    @Test
    void testUpdateUserSuccess() {
        // Given
        String finCode = "1234567";
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setFinCode(finCode);
        userRegisterRequest.setEmail("new@example.com");
        userRegisterRequest.setPhoneNumber("+0987654321");

        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setEmail("old@example.com");
        existingUserEntity.setPhoneNumber("+0123456789");

        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setEmail("new@example.com");
        updatedUserEntity.setPhoneNumber("+0987654321");

        when(userRepository.findByFinCode(finCode)).thenReturn(Optional.of(existingUserEntity));
        when(userRepository.existsByFinCode(userRegisterRequest.getFinCode())).thenReturn(false);
        when(userRepository.existsByEmail(userRegisterRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(userRegisterRequest.getPhoneNumber())).thenReturn(false);
        when(userMapper.entityToRegisterRequest(updatedUserEntity)).thenReturn(userRegisterRequest);
        when(userRepository.save(existingUserEntity)).thenReturn(updatedUserEntity);

        // Use doAnswer to handle the updateEntityFromDto method
        doAnswer(invocation -> {
            UserRegisterRequest dto = invocation.getArgument(0);
            UserEntity entity = invocation.getArgument(1);
            entity.setEmail(dto.getEmail());
            entity.setPhoneNumber(dto.getPhoneNumber());
            return null; // Since this is a void method
        }).when(userMapper).updateEntityFromDto(any(UserRegisterRequest.class), any(UserEntity.class));

        // When
        UserRegisterRequest result = userService.updateUser(finCode, userRegisterRequest);

        // Then
        assertNotNull(result);
        assertEquals(userRegisterRequest.getEmail(), result.getEmail());
        assertEquals(userRegisterRequest.getPhoneNumber(), result.getPhoneNumber());

        verify(userRepository).findByFinCode(finCode);
        verify(userRepository).existsByFinCode(userRegisterRequest.getFinCode());
        verify(userRepository).existsByEmail(userRegisterRequest.getEmail());
        verify(userRepository).existsByPhoneNumber(userRegisterRequest.getPhoneNumber());
        verify(userMapper).updateEntityFromDto(userRegisterRequest, existingUserEntity);
        verify(userRepository).save(existingUserEntity);
    }


    @Test
    void testUpdateUserFinCodeConflict() {
        // Given
        String finCode = "1234567";
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setFinCode("7654321");

        UserEntity existingUserEntity = new UserEntity();

        when(userRepository.findByFinCode(finCode)).thenReturn(Optional.of(existingUserEntity));
        when(userRepository.existsByFinCode(userRegisterRequest.getFinCode())).thenReturn(true);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(finCode, userRegisterRequest));
        assertEquals("This FIN code already belongs to another user", exception.getMessage());
        verify(userRepository).findByFinCode(finCode);
        verify(userRepository).existsByFinCode(userRegisterRequest.getFinCode());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).existsByPhoneNumber(any());
        verify(userMapper, never()).updateEntityFromDto(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUserEmailConflict() {
        // Given
        String finCode = "1234567";
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("conflict@example.com");

        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setEmail("old@example.com");

        when(userRepository.findByFinCode(finCode)).thenReturn(Optional.of(existingUserEntity));
        when(userRepository.existsByEmail(userRegisterRequest.getEmail())).thenReturn(true);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(finCode, userRegisterRequest));
        assertEquals("This email already belongs to another user", exception.getMessage());
        verify(userRepository).findByFinCode(finCode);
        verify(userRepository).existsByEmail(userRegisterRequest.getEmail());
        verify(userRepository, never()).existsByPhoneNumber(any());
        verify(userMapper, never()).updateEntityFromDto(any(), any());
        verify(userRepository, never()).save(any());
    }
}