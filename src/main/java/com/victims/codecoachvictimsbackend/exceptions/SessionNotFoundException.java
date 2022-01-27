package com.victims.codecoachvictimsbackend.exceptions;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String session) {
        super("The Session :" + session + " is not found in the database");
    }
}
