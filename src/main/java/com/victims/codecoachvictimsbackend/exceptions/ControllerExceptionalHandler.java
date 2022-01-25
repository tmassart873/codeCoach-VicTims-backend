package com.victims.codecoachvictimsbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionalHandler {
    /*@ExceptionHandler({NullPointerException.class, EntityNotFoundException.class, UserNotFoundException.class})
    protected void setNullPointerException(NullPointerException nullPointerException, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), nullPointerException.getMessage());
    }*/
    @ExceptionHandler({
            NullPointerException.class,
            EntityNotFoundException.class,
            UserInformationException.class,
            UserNotFoundException.class,
            SessionNotFoundException.class
    })
    protected ResponseEntity<String> handleBadRequestRE(Exception exception) {
        return new ResponseEntity<>("{\"message\":\""+exception.getMessage()+"\"}",null,HttpStatus.BAD_REQUEST.value());
    }
}
