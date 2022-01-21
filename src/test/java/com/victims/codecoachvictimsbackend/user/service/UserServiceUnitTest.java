package com.victims.codecoachvictimsbackend.user.service;

import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

class UserServiceUnitTest {

    private UserService userService;
    private UserRepository userRepositoryMock;
    private UserMapper userMapper;
    private KeycloakService keycloakService;

    @BeforeEach
    void setUp() {
        this.userRepositoryMock = Mockito.mock(UserRepository.class);
        this.userMapper = new UserMapper();
        this.userService = new UserService(userMapper,userRepositoryMock,keycloakService);
    }

    @Nested
    @DisplayName("Getting User by email Service")
    class gettingusersbyemailfromservice {
        @Test
        @DisplayName("Mocking/ When getting user by email, test if findAll() is called from Repository")
        void mock_whenGettingUsersById_findAll_and_getUsers_isCalled() {
            try {
                userService.getUserByEmail("testEmail@test.com");
            } catch (Exception ignored) {}
            Mockito.verify(userRepositoryMock).findAll();
        }

        @Test
        @DisplayName("Stubbing/ When given existing user email, return correct user.")
        void stub_whenGettingUserByEmail() {
            User user1 = User.UserBuilder.userBuilder()
                    .withFirstName("Bert")
                    .withLastName("Vrijstand")
                    .withEmail("Bert@Vrijstand.com")
                    .withCompany("FOD")
                    .withPassword("password123")
                    .withUserRole(UserRole.COACHEE)
                    .withCoachInformation(null)
                    .build();

            UserDto expectedUserDto = userMapper.toDto(user1);
            String workingEmail = "Bert@Vrijstand.com";

            Mockito.when(userRepositoryMock.findAll()).thenReturn(List.of(user1));

            UserDto actualUserDto = userService.getUserByEmail(workingEmail);

            Assertions.assertThat(actualUserDto).isEqualTo(expectedUserDto);
        }

        @Test
        void stub_whenGettingUserByEmailWhitWrongEmail_GiveException() {

            User user1 = User.UserBuilder.userBuilder()
                    .withFirstName("Bert")
                    .withLastName("Vrijstand")
                    .withEmail("Bert@Vrijstand.com")
                    .withCompany("FOD")
                    .withPassword("password123")
                    .withUserRole(UserRole.COACHEE)
                    .withCoachInformation(null)
                    .build();

            Mockito.when(userRepositoryMock.findAll()).thenReturn(List.of(user1));

//            try {
//                UserDto actualUserDto = userService.getUserByEmail("notExistingEmail");
//                fail();
//            } catch (Exception exception) {
//
//            } catch (Exception exception) {
//                fail();
//            }
        }
    }

    @Nested
    @DisplayName("Getting Users Service")
    class gettingusersfromservice {
        @Test
        @DisplayName("Mocking/ When getting users, test if method <findAll()> is called from repository")
        void mock_whenGettingUsers_findAll_isCalled() {
            userService.getUsers();
            Mockito.verify(userRepositoryMock).findAll();
        }

        @Test
        @DisplayName("Stubbing/ When getting users test if all users are returned")
        void stub_whenGettingUsers_AllUsers_areReturned() {
            User user1 = User.UserBuilder.userBuilder()
                    .withFirstName("Bert")
                    .withLastName("Vrijstand")
                    .withEmail("Bert@Vrijstand.com")
                    .withCompany("FOD")
                    .withPassword("password123")
                    .withUserRole(UserRole.COACHEE)
                    .withCoachInformation(null)
                    .build();

            User user2 = User.UserBuilder.userBuilder()
                    .withFirstName("Bert2")
                    .withLastName("Vrijstand")
                    .withEmail("Bert2@Vrijstand.com")
                    .withCompany("FOD")
                    .withPassword("password123")
                    .withUserRole(UserRole.COACHEE)
                    .withCoachInformation(null)
                    .build();

            List<User> testUsers = List.of(user1, user2);
            List<UserDto> expectedUserDto = testUsers.stream()
                    .map(user -> userMapper.toDto(user))
                    .collect(Collectors.toList());

            Mockito.when(userRepositoryMock.findAll()).thenReturn(testUsers);

            List<UserDto> actualUserDtos = userService.getUsers();

            Assertions.assertThat(actualUserDtos).isEqualTo(expectedUserDto);
        }
    }
}