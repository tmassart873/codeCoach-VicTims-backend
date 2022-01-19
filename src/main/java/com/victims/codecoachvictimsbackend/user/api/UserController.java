package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "helloworld", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String helloWorld(){
        return "Hello Tim =)";
    }
//
//    @GetMapping(path = "/{email}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public List<UserDto> getUsers(@PathVariable String email, @RequestBody String password)
//    {
//        return userService.signIn(email,password);
//    }

}
