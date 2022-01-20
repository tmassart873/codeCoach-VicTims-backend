package com.victims.codecoachvictimsbackend.user.service;

import com.victims.codecoachvictimsbackend.user.api.UserMapper;
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

class UserServiceUnitTest {

    private UserService userService;
    private UserRepository userRepositoryMock;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userRepositoryMock = Mockito.mock(UserRepository.class);
        this.userMapper = new UserMapper();
        this.userService = new UserService(userRepositoryMock, userMapper);
    }

    @Nested
    @DisplayName("Getting User by email Service")
    class gettingusersbyemailfromservice {
        @Test
        void mock_whenGettingUsersById_findAll_and_getUsers_isCalled() {
            userService.getUsers();
            Mockito.verify(userRepositoryMock).findAll();
        }
    }

    @Nested
    @DisplayName("Getting Users Service")
    class gettingusersfromservice {
        @Test
        @DisplayName("Mocking/ When getting users, test if method <findAll()> is called from repository")
        void mock_whenGettingUsers_findAll_isCalled() {
            try {
                userService.getUserByEmail("Test@Email.com");
            } catch (Exception ignored) {}

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
                    .map(user -> userMapper.toUserDto(user))
                    .collect(Collectors.toList());

            Mockito.when(userRepositoryMock.findAll()).thenReturn(testUsers);

            List<UserDto> actualUserDtos = userService.getUsers();

            Assertions.assertThat(actualUserDtos).isEqualTo(expectedUserDto);
        }
    }
}