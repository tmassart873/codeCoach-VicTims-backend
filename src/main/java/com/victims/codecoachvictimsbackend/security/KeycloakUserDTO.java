package com.victims.codecoachvictimsbackend.security;

public record KeycloakUserDTO (String userName, String password, Role role){
}