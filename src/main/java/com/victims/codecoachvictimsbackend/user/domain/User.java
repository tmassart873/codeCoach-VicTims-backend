package com.victims.codecoachvictimsbackend.user.domain;

import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "users",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"email"})
)
public class User {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "company")
    private String company;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coach_id")
    private CoachInformation coachInformation;

    protected User() {
    }

    public User(UserBuilder userBuilder) {
        validateUser(userBuilder);
        this.id = UUID.randomUUID();
        this.firstName = userBuilder.firstName;
        this.lastName = userBuilder.lastName;
        this.password = userBuilder.password;
        this.email = userBuilder.email;
        this.company = userBuilder.company;
        this.userRole = userBuilder.userRole;
        this.coachInformation = userBuilder.coachInformation;
    }

    private void validateUser(UserBuilder userBuilder) {
        if(userBuilder.firstName == null){
            throw new IllegalArgumentException("First Name of user can not be null.");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public CoachInformation getCoachInformation() {
        return coachInformation;
    }

    public static final class UserBuilder {
        private String firstName;
        private String lastName;
        private String password;
        private String email;
        private String company;

        private UserRole userRole;
        private CoachInformation coachInformation;

        public UserBuilder() {
        }

        public static UserBuilder userBuilder() {
            return new UserBuilder();
        }

        public UserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withCompany(String company) {
            this.company = company;
            return this;
        }

        public UserBuilder withCoachInformation(CoachInformation coachInformation) {
            this.coachInformation = coachInformation;
            return this;
        }

        public UserBuilder withUserRole(UserRole userRole) {
            this.userRole = userRole;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
