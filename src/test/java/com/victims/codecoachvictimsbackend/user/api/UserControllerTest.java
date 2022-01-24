package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    final String email = "new2victim@email.com";
    final String password = "password";

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Test
    void givenUserDtoToCreate_whenRegisteringUser_thenTheNewlyCreatedUserIsSavedAndReturned() {
        UserDto userDtoToRegister = new UserDto(null, "Dries", "Verreydt",
                password, email, "switchfully", null, null);

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

        cleanUpUserInRepositoryAndKeycloak();
    }

    @Test
    void givenAnIncompleteUserDtoToCreate_whenRegisteringUser_thenBadRequestIsReturnedWithMessage() {
        UserDto userDtoToRegister = new UserDto(null, null, "Verreydt",
                password, email, "switchfully", null, null);

        String responseMessage = RestAssured
                .given()
                .body(userDtoToRegister)
                .accept(JSON)
                .contentType(JSON)
                .when()
                .port(port)
                .post("/users")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().path("message");


        Assertions.assertThat(responseMessage).isEqualTo("First Name of user can not be null.");
    }

    @Test
    void givenCoachee_whenBecomesCoach_thenHasCoachRole() {
        UserDto userDtoToRegister = new UserDto(null, "Timmy", "Timster",
                password, email, "switchfully", null, null);

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

        String url = "/users/" + registeredUserDto.id();

        String accessToken = getTokenFromKeycloak(email, password);

        UserDto coacheeToCoachDto = RestAssured
                .given()
                .auth()
                .oauth2(accessToken)
                .when()
                .port(port)
                .put(url)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto.class);

        assertThat(coacheeToCoachDto.userRole()).isEqualTo(UserRole.COACH);
        keycloakCheck(email, password);
        cleanUpUserInRepositoryAndKeycloak();
    }


    private String getTokenFromKeycloak(String username, String password) {
        String url = "https://keycloak.switchfully.com/auth/realms/java-oct-2021/protocol/openid-connect/token";

        String response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("grant_type", "password")
                .formParam("username", username)
                .formParam("password", password)
                .formParam("client_id", "codeCoach-victims")
                .formParam("client_secret", clientSecret)
                .when()
                .post(url)
                .then()
                .extract()
                .path("access_token")
                .toString();

        return response;
    }

    private void keycloakCheck(String username, String password) {
        RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", username)
                .formParam("password", password)
                .formParam("client_id", "codeCoach-victims")
                .formParam("client_secret", clientSecret)
                .formParam("grant_type", "password")
                .when()
                .post(keycloakUrl)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    private void cleanUpUserInRepositoryAndKeycloak() {
        keycloakService.deleteUser(email);
        User toDelete = userRepository.getByEmail(email);
        userRepository.delete(toDelete);
    }


    @Test
    @DisplayName("End to end test/ Given email, return correct user")
    void whenGivenEmail_returnCorrectUser() {

        String url = "/users/" + email;

        UserDto userDtoToRegister = new UserDto(null, "Dries", "Verreydt",
                password, email, "switchfully", null, null);

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

        String accessToken = getTokenFromKeycloak(email, password);


        UserDto actualUser =
                RestAssured
                        .given()
                        .auth()
                        .oauth2(accessToken)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .get(url)
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(UserDto.class);

        assertThat(actualUser).isEqualTo(registeredUserDto);
        keycloakCheck(email, password);
        cleanUpUserInRepositoryAndKeycloak();
    }
}
