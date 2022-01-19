package com.victims.codecoachvictimsbackend.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="users")
public class UserController {

    @GetMapping(path = "helloworld", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String helloWorld(){
        return "Hello Tim =)";
    }

}
