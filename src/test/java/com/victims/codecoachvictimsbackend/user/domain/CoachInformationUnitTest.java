package com.victims.codecoachvictimsbackend.user.domain;

import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CoachInformationUnitTest {

    private User userOne;
    private UserDto registeredDto;
    private UserService userService;
    private UserMapper userMapper;
    private UserRepository mockUserRepository;
    private KeycloakService keycloakService;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        mockUserRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userMapper, mockUserRepository, keycloakService);
        userOne = User.UserBuilder.userBuilder()
                .withFirstName("Tim")
                .withLastName("Timster")
                .withEmail("hello@mail.com")
                .withUserRole(UserRole.COACHEE)
                .withCompany("hello")
                .withCoachInformation(null)
                .build();
        UserDto toRegister = userMapper.toDto(userOne);
        registeredDto = userService.registerUser(toRegister, UserRole.COACHEE);
    }

    @Test
    void givenACoachee_WhenRequestCoachInformation_ThenNullPointerExceptionThrown() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userOne.getCoachInformation());
    }

    @Test
    void givenACoach_HasAllRequiredFields() {

        Mockito.when(userService.getUserByEmail("hello@mail.com")).then();
    }


}
