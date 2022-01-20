package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getId().toString(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail(), user.getCompany(), user.getUserRole());
    }

    public User toEntity(UserDto userDto, UserRole coachee) {
        return null;
    }
}
