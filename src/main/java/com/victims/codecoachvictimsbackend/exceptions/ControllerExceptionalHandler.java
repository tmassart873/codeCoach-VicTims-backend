package com.victims.codecoachvictimsbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionalHandler {

    /*@ResponseStatus(HttpStatus.BAD_REQUEST)
    protected void handleBadRequestException(Exception exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }*/

    @ExceptionHandler({NullPointerException.class, EntityNotFoundException.class, UserInformationException.class})
    protected ResponseEntity<String> handleBadRequestRE(Exception exception) {
        return new ResponseEntity<>("{\"message\":\""+exception.getMessage()+"\"}",null,HttpStatus.BAD_REQUEST.value());
    }
}
