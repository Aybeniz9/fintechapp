//package az.edu.turing.controller;
//
//import az.edu.turing.model.dto.UserDto;
//import az.edu.turing.service.AuthService;
//import jdk.jfr.Registered;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RestController
//public class AuthController {
//    private final AuthService authService;
//
////    @PostMapping("/login{finCode}{password}")
////    public ResponseEntity<String> login(@PathVariable String finCode, @PathVariable String password) {
////        authService.login(finCode, password);
////        return ResponseEntity.ok("Login successful");
////    }
////    @GetMapping("/login")
////    public ResponseEntity<String> login() {
////        return ResponseEntity.ok("Login successful");
////    }
////    @PostMapping("/register")
////    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
////        return ResponseEntity.ok(authService.register(userDto));
////    }
//}
