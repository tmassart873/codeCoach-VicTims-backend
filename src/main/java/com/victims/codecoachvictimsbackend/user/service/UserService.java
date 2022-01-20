package com.victims.codecoachvictimsbackend.user.service;

import com.victims.codecoachvictimsbackend.user.api.UserMapper;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserByEmail(String email) {
        return this.getUsers().stream()
                .filter(userDto -> userDto.email().equals(email))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
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
