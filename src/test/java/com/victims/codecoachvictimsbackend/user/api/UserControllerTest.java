package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.exceptions.UserAlreadyExistsException;
import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.security.KeycloakUserDTO;
import com.victims.codecoachvictimsbackend.security.Role;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
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

import java.util.List;

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
    final String email = "blurym@email.com";
    final String password = "password";

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserRepository userRepository;

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

        cleanUpUserInRepositoryAndKeycloak(email);
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
        cleanUpUserInRepositoryAndKeycloak(email);
    }

    @Test
    @DisplayName("End to end test/ Given email, return correct user")
    void whenGivenEmail_returnCorrectUser() {

        String url = "/users?email=" + email;

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
        cleanUpUserInRepositoryAndKeycloak(email);
    }

    @Test
    void givenTwoCoachesOneCoachee_whenGettingAllCoaches_thenReturnTwoCoaches() {
        String password1 = "password1";
        String password2 = "password2";
        String password3 = "password3";
        String email1 = "mail111@mail.com";
        String email2 = "mail222@mail.com";
        String email3 = "mail333@mail.com";

        UserDto userDtoToRegister1 = new UserDto(null, "FN1", "LN1",
                password1, email1, "switchfully1", null, null);

        UserDto userDtoToRegister2 = new UserDto(null, "FN2", "LN2",
                password2, email2, "switchfully2", null, null);

        UserDto userDtoToRegister3 = new UserDto(null, "FN3", "LN3",
                password3, email3, "switchfully3", null, null);

        //Add user 1,2 and 3
        UserDto registeredUserDto1 = RestAssured
                .given()
                .body(userDtoToRegister1)
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

        System.out.println("added user 1");
        UserDto registeredUserDto2 = RestAssured
                .given()
                .body(userDtoToRegister2)
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

        System.out.println("added user 2");
        UserDto registeredUserDto3 = RestAssured
                .given()
                .body(userDtoToRegister3)
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

        System.out.println("added user 3");
        //Make user 1 and 3 coach
        String url1 = "/users/" + registeredUserDto1.id();
        String accessToken1 = getTokenFromKeycloak(email1, password1);

        String url3 = "/users/" + registeredUserDto3.id();
        String accessToken3 = getTokenFromKeycloak(email3, password3);

        RestAssured
                .given()
                .auth()
                .oauth2(accessToken1)
                .when()
                .port(port)
                .put(url1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto.class);

        System.out.println("coach user 1");
        RestAssured
                .given()
                .auth()
                .oauth2(accessToken3)
                .when()
                .port(port)
                .put(url3)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto.class);

        System.out.println("coach user 3");
        //Get coaches
        List<UserDto> allCoaches = RestAssured
                .given()
                .auth()
                .oauth2(accessToken1)
                .when()
                .port(port)
                .get("/users?isCoach=true") //?coach=true
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserDto.class);

        assertThat(containsEmail(allCoaches,userDtoToRegister1.email())).isTrue();
        assertThat(containsEmail(allCoaches,userDtoToRegister2.email())).isFalse();
        assertThat(containsEmail(allCoaches,userDtoToRegister3.email())).isTrue();

        cleanUpUserInRepositoryAndKeycloak(email1);
        cleanUpUserInRepositoryAndKeycloak(email2);
        cleanUpUserInRepositoryAndKeycloak(email3);
    }

    @Test
    void givenAUser_WhenCreatingTheSameUserAgain_ThenThrowUserAlreadyExistsException() {

        String email1 = "mail111111111@mail.com";
        String email2 = "mail111111111@mail.com";

        KeycloakUserDTO keycloakUserDTOoToRegister1 = new KeycloakUserDTO(email1, "FN1", Role.COACHEE);

        KeycloakUserDTO keycloakUserDTOoToRegister2 = new KeycloakUserDTO(email2, "FN1", Role.COACHEE);


        keycloakService.addUser(keycloakUserDTOoToRegister1);

        Assertions.assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() ->  keycloakService.addUser(keycloakUserDTOoToRegister2));

        keycloakService.deleteUser(email1);


    }

    private boolean containsEmail(List<UserDto> allCoaches, String email) {
        return allCoaches.stream().anyMatch(coach -> coach.email().equals(email));
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

    private void cleanUpUserInRepositoryAndKeycloak(String email) {
        keycloakService.deleteUser(email);
        User toDelete = userRepository.getByEmail(email);
        userRepository.delete(toDelete);
    }

}
