package az.edu.turing.controller;

import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.model.dto.user.UserDto;
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
    public ResponseEntity<UserResponse> getUser(@RequestHeader ("Authorization") String auth) {
        String finCode = helper.getFinCode(auth);
        UserResponse user = userService.getUser(finCode);
        return ResponseEntity.ok(user);
    }
//    @PostMapping("/create")
//    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
//        return ResponseEntity.ok(userService.create(userDto));
//    }
    @PatchMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestHeader ("Authorization") String auth, @Valid @RequestBody UserDto userDto) {
        String finCode = helper.getFinCode(auth);
        UserDto updatedUser = userService.updateUser(finCode, userDto);
        return ResponseEntity.ok(updatedUser);
    }
    @DeleteMapping("/delete")
    public void deleteUser(@RequestHeader ("Authorization") String auth) {
        String finCode = helper.getFinCode(auth);
        userService.deleteUser(finCode);
    }
}
