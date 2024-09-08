package az.edu.turing.controller;

import az.edu.turing.model.dto.user.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {


    @PostMapping("/login/{finCode}{password}")
    public ResponseEntity<UserRequest> login(@PathVariable String finCode , @PathVariable String password) {

        return ResponseEntity.ok();
    }
}
