package com.victims.codecoachvictimsbackend.session.api;

import com.victims.codecoachvictimsbackend.session.domain.SessionDto;
import com.victims.codecoachvictimsbackend.session.domain.SessionLocation;
import com.victims.codecoachvictimsbackend.session.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "sessions", produces = APPLICATION_JSON_VALUE)
@CrossOrigin
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(consumes=APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto requestSession(@RequestBody SessionDto sessionDto){
        SessionDto requestedSession = sessionService.requestSession(sessionDto);
        return requestedSession;
    }
}
