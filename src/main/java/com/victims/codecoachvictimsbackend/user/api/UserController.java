package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('GET_ALL_COACHES')")
    public List<UserDto> getAllCoaches(@RequestParam boolean isCoach) {
        if (!isCoach) {
            throw new AuthorizationServiceException("for coachees: /users?isCoach=true is needed. You need the correct authorization to view the coaches");
        }

        List<UserDto> allCoachesDtos = userService.getAllCoaches();
        return allCoachesDtos;
    }

    @GetMapping(path = "/{email}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('GET_USER_PROFILE')")
    public UserDto getUserByEmail(@PathVariable String email) {
        UserDto userDtoByEmail = userService.getUserByEmail(email);
        return userDtoByEmail;
    }

//    @GetMapping(produces = APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('GET_USER_PROFILE')")
//    public List<UserDto> getAllUsers() {
//        return userService.getUsers();
//    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        UserDto user = userService.registerUser(userDto, UserRole.COACHEE);
        return user;
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto becomeCoach(@PathVariable String id) {
        UserDto newCoach = userService.updateToCoach(id);
        return newCoach;
    }
}
