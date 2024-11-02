package com.TaskManegment.irusriGroup.utill.mapper;

import com.TaskManegment.irusriGroup.dto.UserDto;
import com.TaskManegment.irusriGroup.dto.UserRegistrationDto;
import com.TaskManegment.irusriGroup.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public static User toUser(UserRegistrationDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // Consider encoding in service, if necessary
        return user;
    }


    UserDto toUserDto(User user);
}
