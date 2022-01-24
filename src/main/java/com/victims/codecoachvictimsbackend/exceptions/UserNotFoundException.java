package com.victims.codecoachvictimsbackend.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String user) {
        super("The user :" + user + " is not found in the database");
    }
}
