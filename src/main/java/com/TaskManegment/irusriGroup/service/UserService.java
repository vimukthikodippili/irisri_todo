package com.TaskManegment.irusriGroup.service;

import com.TaskManegment.irusriGroup.dto.RequestDto.LoginRequest;
import com.TaskManegment.irusriGroup.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    String registerUser(UserRegistrationDto userDto);

    String login(LoginRequest loginRequest);
}
