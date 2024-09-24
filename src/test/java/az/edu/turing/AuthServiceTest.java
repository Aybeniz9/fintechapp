package az.edu.turing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.dao.repository.UserRepository;
import az.edu.turing.exception.UserAlreadyExistException;
import az.edu.turing.jwt.JwtService;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.dto.token.TokenResponse;
import az.edu.turing.model.dto.user.UserLoginRequest;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserRegisterResponse;
import az.edu.turing.service.authentication.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        // Given
        UserLoginRequest loginRequest = new UserLoginRequest("1234567", "password");
        UserEntity user = new UserEntity();
        user.setPassword("$2a$10$encodedpassword");
        when(userRepository.findByFinCode(loginRequest.getFinCode())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq(loginRequest.getPassword()), any())).thenReturn(true);
        when(jwtService.generateToken(loginRequest.getFinCode())).thenReturn("token");

        // When
        TokenResponse tokenResponse = authService.login(loginRequest);

        // Then
        assertNotNull(tokenResponse);
        assertEquals("token", tokenResponse.getToken());
        verify(userRepository).findByFinCode(loginRequest.getFinCode());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtService).generateToken(loginRequest.getFinCode());
    }

    @Test
    void testLoginInvalidPassword() {
        // Given
        UserLoginRequest loginRequest = new UserLoginRequest("1234567", "password");
        UserEntity user = new UserEntity();
        user.setPassword("$2a$10$encodedpassword");
        when(userRepository.findByFinCode(loginRequest.getFinCode())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq(loginRequest.getPassword()), any())).thenReturn(false);

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("Password doesn't match", thrown.getMessage());
    }

    @Test
    void testRegisterSuccess() {
        // Given
        UserRegisterRequest registerRequest = new UserRegisterRequest("John", "Doe", "1234567", 25, "email@example.com", "+0123456789", "123 Main St", "password");
        UserEntity userEntity = new UserEntity();
        UserEntity savedUser = new UserEntity();
        savedUser.setFinCode("1234567");

        // Mock the behavior of passwordEncoder.encode to return "encodedpassword"
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("password");

        // Mock the repository and mapper
        when(userRepository.existsByFinCode(registerRequest.getFinCode())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())).thenReturn(false);
        when(userMapper.dtoFromRegisterToEntity(registerRequest)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        when(userMapper.entityToDtoFromResponse(savedUser)).thenReturn(new UserRegisterResponse("1234567", "John", "Doe"));

        // When
        UserRegisterResponse response = authService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals("1234567", response.getFinCode());
        verify(userRepository).existsByFinCode(registerRequest.getFinCode());
        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(userRepository).existsByPhoneNumber(registerRequest.getPhoneNumber());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userMapper).dtoFromRegisterToEntity(registerRequest);
        verify(userRepository).save(userEntity);
        verify(userMapper).entityToDtoFromResponse(savedUser);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        // Given
        UserRegisterRequest registerRequest = new UserRegisterRequest("John", "Doe", "1234567", 25, "email@example.com", "+0123456789", "123 Main St", "password");
        when(userRepository.existsByFinCode(registerRequest.getFinCode())).thenReturn(true);

        // When & Then
        UserAlreadyExistException thrown = assertThrows(UserAlreadyExistException.class, () -> authService.register(registerRequest));
        assertEquals("User Already Exists for this fin code", thrown.getMessage());
    }

    @Test
    void testRegisterUserAlreadyExistsByEmail() {
        // Given
        UserRegisterRequest registerRequest = new UserRegisterRequest("John", "Doe", "1234567", 25, "email@example.com", "+0123456789", "123 Main St", "password");
        when(userRepository.existsByFinCode(registerRequest.getFinCode())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // When & Then
        UserAlreadyExistException thrown = assertThrows(UserAlreadyExistException.class, () -> authService.register(registerRequest));
        assertEquals("User Already Exists for this email", thrown.getMessage());
    }

    @Test
    void testRegisterUserAlreadyExistsByPhoneNumber() {
        // Given
        UserRegisterRequest registerRequest = new UserRegisterRequest("John", "Doe", "1234567", 25, "email@example.com", "+0123456789", "123 Main St", "password");
        when(userRepository.existsByFinCode(registerRequest.getFinCode())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())).thenReturn(true);

        // When & Then
        UserAlreadyExistException thrown = assertThrows(UserAlreadyExistException.class, () -> authService.register(registerRequest));
        assertEquals("User Already Exists for this phone number", thrown.getMessage());
    }
}
