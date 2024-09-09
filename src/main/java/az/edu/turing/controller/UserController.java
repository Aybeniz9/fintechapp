package az.edu.turing.controller;

import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.service.UserService;
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

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/{finCode}")
    public ResponseEntity<UserEntity> getUser(@PathVariable String finCode) {
        return ResponseEntity.ok(userService.getUser(finCode));
    }
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.create(userDto));
    }
    @PatchMapping("/update/{finCode}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String finCode, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(finCode, userDto);
        return ResponseEntity.ok(updatedUser);
    }
//    @DeleteMapping("/delete/{finCode}")
//    public void deleteUser(@PathVariable String finCode) {
//        userService.deleteUser(finCode);
//    }
}
