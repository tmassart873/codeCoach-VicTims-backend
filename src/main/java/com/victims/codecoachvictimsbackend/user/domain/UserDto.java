package com.victims.codecoachvictimsbackend.user.domain;

import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;

public record UserDto(
    String id,
    String firstName,
    String lastName,
    String password,
    String email,
    String company,
    UserRole userRole
) {}

