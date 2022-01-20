package com.victims.codecoachvictimsbackend.user.mapper;

import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto userDto, UserRole coachee) {
        return new User.UserBuilder()
                .withFirstName(userDto.firstName())
                .withLastName(userDto.lastName())
                .withPassword(userDto.password())
                .withEmail(userDto.email())
                .withCompany(userDto.company())
                .withUserRole(coachee)
                .build();
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail(),
                user.getCompany(),
                user.getUserRole()
        );
    }
}
