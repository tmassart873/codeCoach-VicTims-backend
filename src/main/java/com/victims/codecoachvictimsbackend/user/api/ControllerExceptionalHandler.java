package com.victims.codecoachvictimsbackend.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionalHandler {
    @ExceptionHandler({NullPointerException.class, EntityNotFoundException.class})
    protected void setNullPointerException(NullPointerException nullPointerException, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), nullPointerException.getMessage());
    }
}
