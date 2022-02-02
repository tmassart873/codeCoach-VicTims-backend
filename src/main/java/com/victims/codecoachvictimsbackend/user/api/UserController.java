package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.security.Role;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "users", produces = APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(params = "isCoach")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('GET_ALL_COACHES')")
    public List<UserDto> getAllCoaches(@RequestParam(name = "isCoach") boolean isCoach) {
        if (!isCoach) {
            throw new AuthorizationServiceException("for coachees: /users?isCoach=true is needed. You need the correct authorization to view the coaches");
        }
        List<UserDto> allCoachesDtos = userService.getAllCoaches();
        return allCoachesDtos;
    }

    @GetMapping(params = "email")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('GET_USER_PROFILE')")
    public UserDto getUserByEmail(@RequestParam(name = "email") String email) {
        UserDto userDtoByEmail = userService.getUserByEmail(email);
        return userDtoByEmail;
    }

    @GetMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable String id) {
        UserDto userDtoById = userService.getUserById(id);
        return userDtoById;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        UserDto user = userService.registerUser(userDto, UserRole.COACHEE);
        return user;
    }

    @PostMapping(path = "/{id}/becomeCoach")
    @ResponseStatus(HttpStatus.OK)
    public UserDto becomeCoach(@PathVariable String id) {
        UserDto updatedUser = userService.updateToCoach(id);
        return updatedUser;
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable String id,
                              Authentication authentication) {
        boolean isAdmin = ((SimpleKeycloakAccount) authentication.getDetails()).getRoles()
                .stream().map(String::toLowerCase)
                .toList().contains(Role.ADMIN.name().toLowerCase());
        String loggedInUser = id;
        if(!isAdmin){
            loggedInUser = userService.getUserByEmail(authentication.getName()).id();
        }
        if (!isAdmin && !loggedInUser.equals(id)) {
            throw new AuthorizationServiceException(
                    "Can not update different user unless you are an administrator");
        }
        UserDto updatedUser = userService.updateUser(userDto, isAdmin);
        return updatedUser;
    }

}
