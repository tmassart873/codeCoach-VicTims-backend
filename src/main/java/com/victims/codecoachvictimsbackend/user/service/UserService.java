package com.victims.codecoachvictimsbackend.user.service;

import com.victims.codecoachvictimsbackend.exceptions.UserInformationException;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.exceptions.UserNotFoundException;
import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.security.KeycloakUserDTO;
import com.victims.codecoachvictimsbackend.security.Role;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
                .orElseThrow(() -> new UserNotFoundException(email));
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
            throw new UserInformationException("User email has to be unqiue.");
        }
    }

    private boolean isUserEmailUnique(String email) {
        int i = userRepository.getCountEmail(email).intValue();
        return i == 0;
    }

    public UserDto updateToCoach(String id) {
        User userToUpdate = userRepository.getById(UUID.fromString(id));
        userToUpdate.setRole(UserRole.COACH);
        userToUpdate.setCoachInformation(0, "", "", new HashSet<>());
        UserDto updatedUserDto = userMapper.toDto(userToUpdate);
        System.out.println(userToUpdate.getEmail());
        keycloakService.addRole(userToUpdate.getEmail(), Role.COACH.getLabel());
        return updatedUserDto;
    }

    public UserDto updateUser(UserDto userDto, boolean byAdmin){
        User userToUpdate = userRepository.getById(UUID.fromString(userDto.id()));
        userToUpdate.setFirstName(userDto.firstName());
        userToUpdate.setLastName(userDto.lastName());
        if(byAdmin){
            userToUpdate.setRole(userDto.userRole());
            switch (userDto.userRole()){
                case COACHEE ->  keycloakService.addRole(userToUpdate.getEmail(),Role.COACHEE.getLabel());
                case COACH ->  keycloakService.addRole(userToUpdate.getEmail(),Role.COACH.getLabel());
                case ADMIN ->  keycloakService.addRole(userToUpdate.getEmail(),Role.ADMIN.getLabel());
            }
        }
        UserDto updatedUserDto = userMapper.toDto(userToUpdate);
        return updatedUserDto;
    }

    public List<UserDto> getAllCoaches() {
        List<User> allCoaches = userRepository.getAllCoaches();
        List<UserDto> allCoachesDtos = allCoaches.stream()
                .map(userMapper::toDto)
                .toList();
        return allCoachesDtos;
    }

    public UserDto getUserById(String id) {
        Optional<User> userOptional =userRepository.findById(UUID.fromString(id));
        User user = userOptional.orElseThrow();
        return userMapper.toDto(user);
    }
}
