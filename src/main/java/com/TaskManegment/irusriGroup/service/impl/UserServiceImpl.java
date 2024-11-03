package com.TaskManegment.irusriGroup.service.impl;

import com.TaskManegment.irusriGroup.dto.RequestDto.LoginRequest;
import com.TaskManegment.irusriGroup.dto.UserRegistrationDto;
import com.TaskManegment.irusriGroup.entity.User;
import com.TaskManegment.irusriGroup.exception.EntryDuplicateException;
import com.TaskManegment.irusriGroup.repo.UserRepository;
import com.TaskManegment.irusriGroup.service.UserService;
import com.TaskManegment.irusriGroup.utill.JwtTokenUtil;
import com.TaskManegment.irusriGroup.utill.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtUtil;
    @Override

    /**
     * Registers a new user in the system.
     *
     * This method encodes the user's password and checks if a user with the same email already exists.
     * If not, it saves the user to the repository.
     *
     * @param userDto the user registration details.
     * @return the ID of the newly registered user.
     * @throws EntryDuplicateException if a user with the provided email already exists.
     */
    public String registerUser(UserRegistrationDto userDto) {
        log.info("Received registration request for user: {}", userDto);
        User user = UserMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Registration failed: User exists with the provided email: {}", user.getEmail());
            throw new EntryDuplicateException("User exists with the provided email.");
        }
        return userRepository.save(user).getId();
    }
    /**
     * Authenticates a user and generates a JWT token.
     *
     * This method attempts to authenticate the user with the provided email and password.
     * If successful, it generates and returns a JWT token for the user.
     *
     * @param loginRequest the user's login credentials.
     * @return a JWT token for the authenticated user.
     *
     */

    @Override
    public String login(LoginRequest loginRequest) {
        log.info("Received login request for email: {}", loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));


        String token = jwtUtil.generateToken(loginRequest.getEmail());
        log.info("Login successful for email: {}. Token generated.", loginRequest.getEmail());
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}

