package az.edu.turing.service.authentication;

import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.dao.repository.UserRepository;
import az.edu.turing.exception.UserAlreadyExistException;
import az.edu.turing.exception.UserNotFoundException;
import az.edu.turing.jwt.JwtService;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.dto.token.TokenResponse;
import az.edu.turing.model.dto.user.UserLoginRequest;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenResponse login(UserLoginRequest loginRequest) {
        UserEntity user = userRepository.findByFinCode(loginRequest.getFinCode())
                .orElseThrow(() -> new UserNotFoundException(loginRequest.getFinCode()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password doesn't match");
        }

        return authenticateLogin(loginRequest);
    }

    public UserRegisterResponse register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByFinCode(userRegisterRequest.getFinCode())) {
            throw new UserAlreadyExistException("User Already Exists for this fin code");
        } else if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new UserAlreadyExistException("User Already Exists for this email");
        } else if (userRepository.existsByPhoneNumber(userRegisterRequest.getPhoneNumber())) {
            throw new UserAlreadyExistException("User Already Exists for this phone number");
        }
        userRegisterRequest.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        UserEntity userEntity = userMapper.dtoFromRegisterToEntity(userRegisterRequest);
        UserEntity save = userRepository.save(userEntity);
        return userMapper.entityToDtoFromResponse(save);
    }


    private TokenResponse authenticateLogin(UserLoginRequest userLoginRequest) {
        TokenResponse tokenResponse = new TokenResponse();
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginRequest.getFinCode(), userLoginRequest.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);
        tokenResponse.setToken(jwtService.generateToken(userLoginRequest.getFinCode()));
        return tokenResponse;
    }


}