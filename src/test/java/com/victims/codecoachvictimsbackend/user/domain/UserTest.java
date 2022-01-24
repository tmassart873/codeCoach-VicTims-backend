package com.victims.codecoachvictimsbackend.user.domain;

import com.victims.codecoachvictimsbackend.exceptions.UserInformationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class UserTest {

    @Test
    public void givenUserWithNoFirstName_whenCreatingUser_thenUserInformationExceptionIsThrownWithMessage() {
        User.UserBuilder userBuilder = User.UserBuilder.userBuilder()
                .withLastName("V")
                .withEmail("dv@mail.com")
                .withCompany("company");
        Assertions.assertThatExceptionOfType(UserInformationException.class)
                .isThrownBy(() -> new User(userBuilder))
                .withMessage("First Name of user can not be null.");
    }

    @Test
    public void givenUserWithNoLastName_whenCreatingUser_thenUserInformationExceptionIsThrownWithMessage() {
        User.UserBuilder userBuilder = User.UserBuilder.userBuilder()
                .withFirstName("Dries")
                .withEmail("dv@mail.com")
                .withCompany("company");
        Assertions.assertThatExceptionOfType(UserInformationException.class)
                .isThrownBy(() -> new User(userBuilder))
                .withMessage("Last Name of user can not be null.");
    }

    @Test
    public void givenUserWithNoEmail_whenCreatingUser_thenUserInformationExceptionIsThrownWithMessage() {
        User.UserBuilder userBuilder = User.UserBuilder.userBuilder()
                .withFirstName("Dries")
                .withLastName("V")
                .withCompany("company");
        Assertions.assertThatExceptionOfType(UserInformationException.class)
                .isThrownBy(() -> new User(userBuilder))
                .withMessage("Email of user can not be null.");
    }

    @Test
    public void givenUserWithInvalidEmailFormat_whenCreatingUser_thenUserInformationExceptionIsThrownWithMessage() {
        User.UserBuilder userBuilder = User.UserBuilder.userBuilder()
                .withFirstName("Dries")
                .withLastName("V")
                .withEmail("dvmail.com")
                .withCompany("company");
        Assertions.assertThatExceptionOfType(UserInformationException.class)
                .isThrownBy(() -> new User(userBuilder))
                .withMessage("Email of user requires an @ symbol.");
    }

    @Test
    public void givenUserWithNoCompany_whenCreatingUser_thenUserInformationExceptionIsThrownWithMessage() {
        User.UserBuilder userBuilder = User.UserBuilder.userBuilder()
                .withFirstName("Dries")
                .withLastName("V")
                .withEmail("dv@mail.com");
        Assertions.assertThatExceptionOfType(UserInformationException.class)
                .isThrownBy(() -> new User(userBuilder))
                .withMessage("Company of user can not be null.");
    }
}