package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class UserControllerUnitTest {
    private UserService userServiceMock;
    private UserController userController;
    private UserMapper userMapper;


    @BeforeEach
    void setUp() {
        this.userServiceMock = Mockito.mock(UserService.class);
        this.userController = new UserController(userServiceMock);
        this.userMapper = new UserMapper();
    }

    @Nested
    @DisplayName("getting User by email from Controller")
    class GettingUsersByEmailFromController {
        @Test
        @DisplayName("Mocking/ When getting users by email, test if getUserByEmail() is called from userService")
        void mock_whenGettingUserById_getUserByEmail_isCalled() {
            try {
                userController.getUserByEmail("TestEmail");
            } catch (Exception e) {
                System.out.println("this does not work");
            }

            Mockito.verify(userServiceMock).getUserByEmail("TestEmail");
        }

        @Test
        void stub_whenGettingUsersByEmail_correctUserIsReturned() {
            User user1 = User.UserBuilder.userBuilder()
                    .withFirstName("Bert")
                    .withLastName("Vrijstand")
                    .withEmail("Bert@Vrijstand.com")
                    .withCompany("FOD")
                    .withUserRole(UserRole.COACHEE)
                    .withCoachInformation(null)
                    .build();

            UserDto expectedUserDto = userMapper.toDto(user1);

            Mockito.when(userServiceMock.getUserByEmail(user1.getEmail())).thenReturn(expectedUserDto);

            UserDto actualUserDto =  userController.getUserByEmail(user1.getEmail());

            Assertions.assertThat(actualUserDto).isEqualTo(expectedUserDto);
        }
    }
}
