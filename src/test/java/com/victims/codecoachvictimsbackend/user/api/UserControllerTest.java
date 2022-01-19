package com.victims.codecoachvictimsbackend.user.api;

import com.victims.codecoachvictimsbackend.user.domain.UserDto;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import io.restassured.RestAssured;
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

    @Test
    void givenUserDtoToCreate_whenRegisteringUser_thenTheNewlyCreatedUserIsSavedAndReturned() {
        UserDto userDtoToRegister = new UserDto(null,"Dries","Verreydt",
                "password","oompalumpa@mail.com","switchfully",null);

        UserDto userDtoToRegister2 = new UserDto(null,"Dries","Verreydt",
                "password","banana@mail.com","switchfully",null);

        UserDto userDtoToRegister3 = new UserDto(null,"Dries","Verreydt",
                "password","monkey@mail.com","switchfully",null);

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

                RestAssured
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

                RestAssured
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

        assertThat(registeredUserDto.id()).isNotNull();
        assertThat(registeredUserDto.firstName()).isEqualTo("Dries");
        assertThat(registeredUserDto.lastName()).isEqualTo("Verreydt");
        assertThat(registeredUserDto.password()).isEqualTo("password"); //FIXME kan na keycload geencodeerd terugkomen
        assertThat(registeredUserDto.email()).isEqualTo("dries@mail.com");
        assertThat(registeredUserDto.company()).isEqualTo("switchfully");
        assertThat(registeredUserDto.userRole()).isEqualTo(UserRole.COACHEE);
    }
}