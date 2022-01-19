package com.victims.codecoachvictimsbackend.user.service;

import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserDto registerUser(UserDto userDto, UserRole coachee) {
        validateUser(userDto.email());
        User userToRegister = userMapper.toEntity(userDto, coachee);
        User registeredUser = userRepository.save(userToRegister);
        return userMapper.toDto(registeredUser);
    }

    private void validateUser(String email){
        if(!isUserEmailUnique(email)){
            throw new IllegalArgumentException("User email has to be unqiue.");
        }
    }

    private boolean isUserEmailUnique(String email) {
       return userRepository.getCountEmail(email).intValue() == 0;
    }
}
