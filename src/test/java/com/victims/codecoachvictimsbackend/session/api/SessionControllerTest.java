package com.victims.codecoachvictimsbackend.session.api;
import com.victims.codecoachvictimsbackend.security.KeycloakService;
import com.victims.codecoachvictimsbackend.session.domain.SessionDto;
import com.victims.codecoachvictimsbackend.session.domain.SessionLocation;
import com.victims.codecoachvictimsbackend.session.mapper.SessionMapper;
import com.victims.codecoachvictimsbackend.session.service.SessionService;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import com.victims.codecoachvictimsbackend.user.mapper.UserMapper;
import com.victims.codecoachvictimsbackend.user.repository.UserRepository;
import com.victims.codecoachvictimsbackend.user.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionControllerTest {

    @LocalServerPort
    private int port;

    private final String keycloakUrl =
            "https://keycloak.switchfully.com/auth/realms/java-oct-2021/protocol/openid-connect/token";

    //one for the coachee, one for the coach
    final String email1 = "rud1victim@email.com";
    final String password1 = "password1";

    final String email2 = "rud2victim@email.com";
    final String password2 = "password2";

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private String coacheeId;
    private String coachId;

    @BeforeEach
    void setUp() {
        UserDto userOneDtoToRegister = new UserDto(null, "Dries", "Verreydt",
                password1, email1, "switchfully", null, null);
        UserDto userTwoDtoToRegister = new UserDto(null, "Timmy", "Timster",
                password2, email2, "switchfully", null, null);

        UserDto coachee = userService.registerUser(userOneDtoToRegister, UserRole.COACHEE);
        UserDto coachToUpdate = userService.registerUser(userTwoDtoToRegister, UserRole.COACHEE);

        System.out.println(coachToUpdate);
        System.out.println(coachToUpdate.id());
        UserDto coach = userService.updateToCoach(coachToUpdate.id());

        coacheeId = coachee.id();
        coachId = coach.id();

    }

    @Test
    void givenCoacheeAndCoach_whenRequestSession_SessionIsSaved() {
        SessionDto requestedSessionDto =
                new SessionDto(null, coacheeId, coachId, "Lolze", "2022-02-10", "10:02",
                        SessionLocation.ONLINE, "lol", null);

        //get authorized to request a session
        String accessTokenCoachee = getTokenFromKeycloak(email1, password1);
        System.out.println(coacheeId);
        System.out.println(coachId);

        SessionDto savedRequestedSession =
                RestAssured
                .given()
                    .auth()
                    .oauth2(accessTokenCoachee)
                    .body(requestedSessionDto)
                    .accept(ContentType.JSON)
                    .contentType(JSON)
                .when()
                    .port(port)
                    .post("/sessions")
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(SessionDto.class);

        assertThat(savedRequestedSession.id()).isNotNull();

        cleanUpUserInRepositoryAndKeycloak(email1, password1);
        cleanUpUserInRepositoryAndKeycloak(email2, password2);
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

    private void cleanUpUserInRepositoryAndKeycloak(String email, String password) {
        keycloakService.deleteUser(email);
        User toDelete = userRepository.getByEmail(email);
        userRepository.delete(toDelete);
    }
}
