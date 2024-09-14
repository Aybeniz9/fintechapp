package az.edu.turing.controller;

import az.edu.turing.model.dto.token.TokenResponse;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserLoginRequest;
import az.edu.turing.model.dto.user.UserRegisterResponse;
import az.edu.turing.service.authentication.AuthService;
import az.edu.turing.service.user.UserService;
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
    private final AuthService authService;
    private final UserService userService;

    //    private final AuthService authService;
//
//    @PostMapping("/login{finCode}{password}")
//    public ResponseEntity<String> login(@PathVariable String finCode, @PathVariable String password) {
//        authService.login(finCode, password);
//        return ResponseEntity.ok("Login successful");
//    }
//    @GetMapping("/login")
//    public ResponseEntity<String> login() {
//        return ResponseEntity.ok("Login successful");
//    }
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
//        return ResponseEntity.ok(authService.register(userDto));
//    }
//    @PostMapping("/authenticate")
//    public ResponseEntity<TokenResponse> generateToken(@RequestBody UserLoginRequest userLoginRequest) {
//        return ResponseEntity.ok(authService.authenticate(userLoginRequest));
//    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(authService.login(userLoginRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest userRequest) {
        return ResponseEntity.ok(authService.register(userRequest));
    }
}

