package com.TaskManegment.irusriGroup.controller;


import com.TaskManegment.irusriGroup.dto.RequestDto.LoginRequest;
import com.TaskManegment.irusriGroup.dto.UserRegistrationDto;
import com.TaskManegment.irusriGroup.service.UserService;
import com.TaskManegment.irusriGroup.utill.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }
    /**
     * Endpoint for user registration.
     *
     * This method accepts a UserRegistrationDto, processes it through the UserService,
     * and returns a response indicating the result of the registration attempt.
     *
     * @param userDto the user registration details.
     * @return ResponseEntity containing the registration status and user ID.
     */
    @PostMapping("/register")
    public ResponseEntity<StandardResponse> registerUser(@RequestBody UserRegistrationDto userDto) {
        log.info("Received registration request for user: {}", userDto);
        String id = userService.registerUser(userDto);
        log.info("User registered successfully with ID: {}", id);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201,"user saved",id),
                HttpStatus.CREATED);

    }
    /**
     * Endpoint for user login.
     *
     * This method accepts a LoginRequest containing the user's credentials,
     * validates them, and returns an authentication token if successful.
     *
     * @param loginRequest the user's login credentials.
     * @return ResponseEntity containing the login status and authentication token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Received login request for email: {}", loginRequest.getEmail());
        try {
            String token = userService.login(loginRequest);
            log.info("Login successful for email: {}", loginRequest.getEmail());
            return ResponseEntity.ok(new StandardResponse(200, "Login successful", token));
        } catch (AuthenticationException e) {
            log.warn("Login failed for email: {}. Reason: {}", loginRequest.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StandardResponse(401, "Invalid email or password", null));
        }
    }
}
