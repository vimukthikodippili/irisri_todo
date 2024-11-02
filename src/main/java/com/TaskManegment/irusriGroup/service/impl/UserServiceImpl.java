package com.TaskManegment.irusriGroup.service.impl;

import com.TaskManegment.irusriGroup.dto.RequestDto.LoginRequest;
import com.TaskManegment.irusriGroup.dto.UserRegistrationDto;
import com.TaskManegment.irusriGroup.entity.User;
import com.TaskManegment.irusriGroup.exception.EntryDuplicateException;
import com.TaskManegment.irusriGroup.repo.UserRepository;
import com.TaskManegment.irusriGroup.service.UserService;
import com.TaskManegment.irusriGroup.utill.JwtTokenUtil;
import com.TaskManegment.irusriGroup.utill.mapper.UserMapper;
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
    public String registerUser(UserRegistrationDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // Encode password
        System.out.println(userDto);
        if (userRepository.existsByEmail(user.getEmail())) { // Check by email instead of ID
            throw new EntryDuplicateException("User exists with the provided email.");
        }
        return userRepository.save(user).getId();
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // If authentication is successful, generate a token
        String token = jwtUtil.generateToken(loginRequest.getEmail());
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}

