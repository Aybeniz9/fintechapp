package az.edu.turing.controller;

import az.edu.turing.model.dto.token.TokenResponse;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserLoginRequest;
import az.edu.turing.model.dto.user.UserRegisterResponse;
import az.edu.turing.service.authentication.AuthService;
import az.edu.turing.service.notification.NotificationService;
import az.edu.turing.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final NotificationService notificationService;

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequest userLoginRequest) {

        return ResponseEntity.ok(authService.login(userLoginRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest userRequest) {
        return ResponseEntity.ok(authService.register(userRequest));
    }
}

