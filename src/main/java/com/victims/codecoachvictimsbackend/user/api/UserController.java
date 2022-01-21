package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.security.KeycloakUserDTO;
import com.victims.codecoachvictimsbackend.security.Role;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="users", produces = APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final KeycloakService keycloakService;

    @Autowired
    public UserController(UserService userService, KeycloakService keycloakService) {
        this.userService = userService;
        this.keycloakService = keycloakService;
    }

    @GetMapping(path ="/{email}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('GET_USER_PROFILE')")
    public UserDto getUserByEmail(@PathVariable String email)
    {
        return userService.getUserByEmail(email);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        UserDto user =  userService.registerUser(userDto, UserRole.COACHEE);
        keycloakService.addUser(new KeycloakUserDTO(user.email(),user.password(), Role.COACHEE));
        return user;
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDto becomeCoach(@PathVariable String id) {
        UserDto newCoach = userService.updateToCoach(id);
        return newCoach;
    }
}
