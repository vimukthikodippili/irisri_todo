package com.TaskManegment.irusriGroup.controller;


import com.TaskManegment.irusriGroup.dto.RequestDto.LoginRequest;
import com.TaskManegment.irusriGroup.dto.UserRegistrationDto;
import com.TaskManegment.irusriGroup.service.UserService;
import com.TaskManegment.irusriGroup.utill.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<StandardResponse> registerUser(@RequestBody UserRegistrationDto userDto) {
        String id = userService.registerUser(userDto);
        System.out.println(userDto);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201,"user saved",id),
                HttpStatus.CREATED);

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest);
            return ResponseEntity.ok(new StandardResponse(200, "Login successful", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StandardResponse(401, "Invalid email or password", null));
        }
    }
}
