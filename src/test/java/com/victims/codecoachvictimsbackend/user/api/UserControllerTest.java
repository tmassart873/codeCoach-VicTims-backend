package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Value("${server.port}")
    private int port;

    private final String keycloakUrl =
            "https://keycloak.switchfully.com/auth/realms/java-oct-2021/protocol/openid-connect/token";

    final String email = "oompalumpa@mail.com";
    final String password = "password";


    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

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

    void keycloakCheck(String username, String password) {
        RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .header("username", username)
                .header("password", password)
                .header("client_id", "codeCoach-victims")
                .header("client_secret",clientSecret)
                .header("grant_type", "password")
                .when()
                .post(keycloakUrl)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}