package com.victims.codecoachvictimsbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionalHandler {

    @ExceptionHandler(
            {AuthorizationServiceException.class,
            NullPointerException.class,
            EntityNotFoundException.class,
            UserInformationException.class,
            UserNotFoundException.class})

    protected ResponseEntity<String> handleBadRequestRE(Exception exception) {
        return new ResponseEntity<>("{\"message\":\""+exception.getMessage()+"\"}",
                null,HttpStatus.BAD_REQUEST.value());
    }
}
