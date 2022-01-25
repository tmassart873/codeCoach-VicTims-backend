package com.victims.codecoachvictimsbackend.session.api;

import com.victims.codecoachvictimsbackend.session.service.SessionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "sessions", produces = APPLICATION_JSON_VALUE)
@CrossOrigin
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }
}
