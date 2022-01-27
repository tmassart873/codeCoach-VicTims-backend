package com.victims.codecoachvictimsbackend.session.domain;

import com.victims.codecoachvictimsbackend.exceptions.SessionInformationException;
import com.victims.codecoachvictimsbackend.user.domain.CoachInformation;
import com.victims.codecoachvictimsbackend.user.domain.User;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

public class SessionUnitTest {

    private User coachee;
    private User coach;
    private CoachInformation coachInfo;

    @BeforeEach
    void setUp() {
        coachee = User.UserBuilder.userBuilder()
                .withFirstName("Blurb")
                .withLastName("Blurbor")
                .withEmail("blurb@mail.com")
                .withCompany("BlurbCo")
                .withUserRole(UserRole.COACHEE)
                .build();

        coachInfo = CoachInformation.CoachInformationBuilder.aCoachInformation()
                .withCoachXp(0)
                .withAvailability("always")
                .withIntroduction("here for ya")
                .withTopics(null)
                .build();

        coach = User.UserBuilder.userBuilder()
                .withFirstName("Slurp")
                .withLastName("Slurpor")
                .withEmail("slurb@mail.com")
                .withCompany("SlurpCo")
                .withUserRole(UserRole.COACH)
                .withCoachInformation(coachInfo)
                .build();
    }

    @Test
    void givenSessionWithNoSubject_ThenThrowsInvalidSessionInformationException() {
        Session.SessionBuilder sessionBuilder = Session.SessionBuilder.sessionBuilder()
                .withCoacheeId(coachee.getId().toString())
                .withCoachId(coach.getId().toString())
                .withDate("10/02/2022")
                .withTime("10:02")
                .withLocation(SessionLocation.FACE2FACE)
                .withRemarks("c'est la vie");
        Assertions.assertThatExceptionOfType(SessionInformationException.class)
                .isThrownBy(() -> new Session(sessionBuilder))
                .withMessage("A session requires a valid subject.");
    }

    @Test
    void givenSessionWithNoDate_ThenThrowsInvalidSessionInformationException() {
        Session.SessionBuilder sessionBuilder = Session.SessionBuilder.sessionBuilder()
                .withCoacheeId(coachee.getId().toString())
                .withCoachId(coach.getId().toString())
                .withSubject("la vie")
                .withTime("10:02")
                .withLocation(SessionLocation.FACE2FACE)
                .withRemarks("c'est la vie");
        Assertions.assertThatExceptionOfType(SessionInformationException.class)
                .isThrownBy(() -> new Session(sessionBuilder))
                .withMessage("A session requires a valid date.");
    }

    @Test
    void givenSessionWithInvalidDate_ThenThrowsDateTimeParseException() {
        Assertions.assertThatExceptionOfType(DateTimeParseException.class)
                .isThrownBy(() -> Session.SessionBuilder.sessionBuilder().withDate(""));
    }

    @Test
    void givenSessionWithNoTime_ThenThrowsInvalidSessionInformationException() {
        Session.SessionBuilder sessionBuilder = Session.SessionBuilder.sessionBuilder()
                .withCoacheeId(coachee.getId().toString())
                .withCoachId(coach.getId().toString())
                .withSubject("la vie")
                .withDate("10/02/2022")
                .withLocation(SessionLocation.ONLINE)
                .withRemarks("c'est la vie");
        Assertions.assertThatExceptionOfType(SessionInformationException.class)
                .isThrownBy(() -> new Session(sessionBuilder))
                .withMessage("A session requires a valid time.");
    }

    @Test
    void givenSessionWithInvalidTime_ThenThrowsDateTimeParseException() {
        Assertions.assertThatExceptionOfType(DateTimeParseException.class)
                .isThrownBy(() -> Session.SessionBuilder.sessionBuilder().withTime(""));
    }

    @Test
    void givenSessionWithNoLocation_ThenThrowsInvalidSessionInformationException() {
        Session.SessionBuilder sessionBuilder = Session.SessionBuilder.sessionBuilder()
                .withCoacheeId(coachee.getId().toString())
                .withCoachId(coach.getId().toString())
                .withSubject("la vie")
                .withDate("10/02/2022")
                .withTime("10:02")
                .withRemarks("c'est la vie");
        Assertions.assertThatExceptionOfType(SessionInformationException.class)
                .isThrownBy(() -> new Session(sessionBuilder))
                .withMessage("A session requires a valid location.");
    }


    @Test
    void givenSessionWithNoRemarks_ThenThrowsInvalidSessionInformationException() {
        Session.SessionBuilder sessionBuilder = Session.SessionBuilder.sessionBuilder()
                .withCoacheeId(coachee.getId().toString())
                .withCoachId(coach.getId().toString())
                .withSubject("la vie")
                .withDate("10/02/2022")
                .withTime("10:02")
                .withLocation(SessionLocation.FACE2FACE);
        Assertions.assertThatExceptionOfType(SessionInformationException.class)
                .isThrownBy(() -> new Session(sessionBuilder))
                .withMessage("A session requires a valid remark.");
    }

}
