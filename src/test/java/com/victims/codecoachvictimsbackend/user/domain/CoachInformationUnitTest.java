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
    private UserService userService;
    private UserMapper userMapper;
    private UserRepository mockUserRepository;
    private KeycloakService mockKeycloakService;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        mockUserRepository = Mockito.mock(UserRepository.class);
        mockKeycloakService = Mockito.mock(KeycloakService.class);
        userService = new UserService(userMapper, mockUserRepository, mockKeycloakService);
        userOne = User.UserBuilder.userBuilder()
                .withFirstName("Tim")
                .withLastName("Timster")
                .withEmail("hello@mail.com")
                .withUserRole(UserRole.COACHEE)
                .withCompany("hello")
                .withCoachInformation(null)
                .build();
    }

    @Test
    void givenACoachee_WhenRequestCoachInformationXp_ThenNullPointerExceptionThrown() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userOne.getCoachInformation().getCoachXp());
    }

    @Test
    void givenACoach_HasAllRequiredFields() {
        Mockito.when(mockUserRepository.getById(userOne.getId())).thenReturn(userOne);
        userService.updateToCoach(userOne.getId().toString());
        Assertions.assertThat(userOne.getCoachInformation()).isNotNull();
        Assertions.assertThat(userOne.getCoachInformation().getCoachXp()).isEqualTo(0);
        Assertions.assertThat(userOne.getCoachInformation().getAvailability()).isEqualTo("");
        Assertions.assertThat(userOne.getCoachInformation().getIntroduction()).isEqualTo("");
        Assertions.assertThat(userOne.getCoachInformation().getTopics()).isEmpty();
        Assertions.assertThat(userOne.getCoachInformation().getId()).isNotNull();
    }
}
