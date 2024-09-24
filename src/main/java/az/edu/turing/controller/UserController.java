package az.edu.turing.controller;

import az.edu.turing.model.dto.user.UserDto;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserResponse;
import az.edu.turing.service.authorization.AuthorizationHelperService;
import az.edu.turing.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final AuthorizationHelperService helper;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String auth) {
        String finCode = helper.getFinCode(auth);
        UserResponse user = userService.getUser(finCode);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update2")
    public ResponseEntity<UserRegisterRequest> updateUser(@RequestHeader("Authorization") String auth, @Valid @RequestBody UserRegisterRequest userDto) {
        String finCode = helper.getFinCode(auth);
        UserRegisterRequest updatedUser = userService.updateUser(finCode, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestHeader("Authorization") String auth) {
        String finCode = helper.getFinCode(auth);
        userService.deleteUser(finCode);
    }
}