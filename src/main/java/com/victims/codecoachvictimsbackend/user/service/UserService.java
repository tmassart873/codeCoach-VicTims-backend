package com.victims.codecoachvictimsbackend.user.service;

import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.security.KeycloakUserDTO;
import com.victims.codecoachvictimsbackend.security.Role;
import com.victims.codecoachvictimsbackend.user.domain.Topic;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, KeycloakService keycloakService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
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
        keycloakService.addUser(new KeycloakUserDTO(userDto.email(),userDto.password(), Role.COACHEE));
        return userMapper.toDto(registeredUser);
    }

    private void validateUser(String email) {
        if (!isUserEmailUnique(email)) {
            throw new IllegalArgumentException("User email has to be unqiue.");
        }
    }

    private boolean isUserEmailUnique(String email) {
        return userRepository.getCountEmail(email).intValue() == 0;
    }

    public UserDto updateToCoach(String id) {
        User userToUpdate = userRepository.getById(UUID.fromString(id));
        userToUpdate.setRole(UserRole.COACH);
        userToUpdate.setCoachInformation(0, "", "", new HashSet<Topic>());
        UserDto updatedUserDto = userMapper.toDto(userToUpdate);
        keycloakService.addRole(userToUpdate.getEmail(), Role.COACH.getLabel());
        return updatedUserDto;
    }
}
