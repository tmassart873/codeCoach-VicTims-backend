package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    //using RANDOM_PORT ensures that you can run test + app at the same time
    @LocalServerPort
    private int port;

    private final String keycloakUrl =
            "https://keycloak.switchfully.com/auth/realms/java-oct-2021/protocol/openid-connect/token";
    final String email = "newvictim@email.com";
    final String password = "password";

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @AfterEach
    void cleanUp() {
        keycloakService.deleteUser(email);
        User toDelete = userRepository.getByEmail(email);
        userRepository.delete(toDelete);
    }

    @Test
    void givenUserDtoToCreate_whenRegisteringUser_thenTheNewlyCreatedUserIsSavedAndReturned() {
        UserDto userDtoToRegister = new UserDto(null,"Dries","Verreydt",
                password,email,"switchfully",null);

        UserDto registeredUserDto =
                RestAssured
                        .given()
                        .body(userDtoToRegister)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .post("/users")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(UserDto.class);

        assertThat(registeredUserDto.id()).isNotNull();
        assertThat(registeredUserDto.firstName()).isEqualTo("Dries");
        assertThat(registeredUserDto.lastName()).isEqualTo("Verreydt");
        assertThat(registeredUserDto.email()).isEqualTo(email);
        assertThat(registeredUserDto.company()).isEqualTo("switchfully");
        assertThat(registeredUserDto.userRole()).isEqualTo(UserRole.COACHEE);

        keycloakCheck(email, password);

    }

    @Nested
    @DisplayName("Story2 end Story3 get a user by email")
    class GetUserByEmail {

        @Test
        @DisplayName("End to end test/ Given email, return correct user")
        void whenGivenEmail_returnCorrectUser() {
            String email = "this.email@outlook.com";

            User expectedUser = new User.UserBuilder()
                    .withUserRole(UserRole.COACHEE)
                    .withEmail(email)
                    .withCompany("Colruyt")
                    .withFirstName("Bert")
                    .withLastName("Vermissen")
                    .build();

            if (!userRepository.findAll().contains(expectedUser)) {
                userRepository.save(expectedUser);
            }

            UserDto actualUser =
                    RestAssured
                            .given()
                            .body("")
                            .accept(JSON)
                            .contentType(JSON)
                            .when()
                            .port(port)
                            .get("/users/" + email)
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.OK.value())
                            .extract()
                            .as(UserDto.class);

            assertThat(actualUser).isEqualTo(userMapper.toDto(expectedUser));
        }
    }

     void keycloakCheck(String username, String password) {
        RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", username)
                .formParam("password", password)
                .formParam("client_id", "codeCoach-victims")
                .formParam("client_secret",clientSecret)
                .formParam("grant_type", "password")
                .when()
                .post(keycloakUrl)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
    /*    @Test
    void givenCoachee_whenBecomesCoach_thenHasCoachRole() {
        UserDto userDtoToRegister = new UserDto(null,"Timmy","Timster",
                password,email,"switchfully",null);

        UserDto registeredUserDto =
                RestAssured
                        .given()
                        .body(userDtoToRegister)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .post("/users")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(UserDto.class);

        String id = registeredUserDto.id();

        RestAssured
                .given()
                .when()
                .port(port)
                .put("/users"+ id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto.class);

        assertThat(registeredUserDto.userRole()).isEqualTo(UserRole.COACH);
    }*/
}