package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mockito;

class UserControllerUnitTest {
    private UserService userServiceMock;
    private UserController userController;
    private KeycloakService keycloakService;


    public UserControllerUnitTest(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @BeforeEach
    void setUp() {
        this.userServiceMock = Mockito.mock(UserService.class);
        this.userController = new UserController(userServiceMock,keycloakService);
    }

    @Nested
    @DisplayName("getting Users from Controller")
    class GettingUsersFromController {
//        @Test
//        @DisplayName("Mocking/ When getting users, test if getUsers() is called from userService")
//        void mock_whenGettingUsers_getUsers_isCalled() {
//            try {
//                userController.getUserByEmail("testEmail");
//            } catch (Exception e) {}
//
//            Mockito.verify(userServiceMock).getUsers();
//            Mockito.verify(userServiceMock).getUserByEmail("TestEmail");
//        }
//
//        @Test
//        @DisplayName("Stubbing/ When getting users, test if all users are returned")
//        void stub_whenGettingUsers_AllUsers_areReturned() {
//
//        }
    }
}
