package az.edu.turing;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import az.edu.turing.controller.UserController;
import az.edu.turing.model.dto.user.UserDto;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserResponse;
import az.edu.turing.service.authorization.AuthorizationHelperService;
import az.edu.turing.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationHelperService helper;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private UserResponse userResponse;
    private UserRegisterRequest userRegisterRequest;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setFinCode("1234567");

        userResponse = new UserResponse();
        userResponse.setFinCode("1234567");

        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setFinCode("1234567");
        userRegisterRequest.setEmail("test@example.com");
        userRegisterRequest.setPhoneNumber("+1234567890");
    }

    @Test
    void testGetUsers() {
        // Given
        List<UserDto> userList = Arrays.asList(userDto);
        when(userService.getAllUsers()).thenReturn(userList);

        // When
        ResponseEntity<List<UserDto>> response = userController.getUsers();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userList, response.getBody());
        verify(userService).getAllUsers();
    }

    @Test
    void testGetUser() {
        // Given
        String auth = "Bearer token";
        String finCode = "1234567";
        when(helper.getFinCode(auth)).thenReturn(finCode);
        when(userService.getUser(finCode)).thenReturn(userResponse);

        // When
        ResponseEntity<UserResponse> response = userController.getUser(auth);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userResponse, response.getBody());
        verify(helper).getFinCode(auth);
        verify(userService).getUser(finCode);
    }

    @Test
    void testUpdateUser() {
        // Given
        String auth = "Bearer token";
        String finCode = "1234567";
        when(helper.getFinCode(auth)).thenReturn(finCode);
        when(userService.updateUser(finCode, userRegisterRequest)).thenReturn(userRegisterRequest);

        // When
        ResponseEntity<UserRegisterRequest> response = userController.updateUser(auth, userRegisterRequest);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userRegisterRequest, response.getBody());
        verify(helper).getFinCode(auth);
        verify(userService).updateUser(finCode, userRegisterRequest);
    }

    @Test
    void testDeleteUser() {
        // Given
        String auth = "Bearer token";
        String finCode = "1234567";
        when(helper.getFinCode(auth)).thenReturn(finCode);

        // When
        userController.deleteUser(auth);

        // Then
        verify(helper).getFinCode(auth);
        verify(userService).deleteUser(finCode);
    }
}