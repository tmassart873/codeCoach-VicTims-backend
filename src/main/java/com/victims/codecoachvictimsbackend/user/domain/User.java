package com.victims.codecoachvictimsbackend.user.domain;

import com.victims.codecoachvictimsbackend.exceptions.UserInformationException;
import com.victims.codecoachvictimsbackend.user.domain.enums.UserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "app_user",
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) || email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public User(UserBuilder userBuilder) {
        validateUser(userBuilder);
        this.id = UUID.randomUUID();
        this.firstName = userBuilder.firstName;
        this.lastName = userBuilder.lastName;
        this.email = userBuilder.email;
        this.company = userBuilder.company;
        this.userRole = userBuilder.userRole;
        this.coachInformation = userBuilder.coachInformation;
    }

    private void validateUser(UserBuilder userBuilder) {
        userArgumentNotNull(userBuilder.firstName, "First Name");
        userArgumentNotNull(userBuilder.lastName, "Last Name");
        userArgumentNotNull(userBuilder.email, "Email");
        if(!userBuilder.email.contains("@")){
            throw new UserInformationException("Email of user requires an @ symbol.");
        }
        userArgumentNotNull(userBuilder.company, "Company");
    }

    private <T> void userArgumentNotNull(T userArgument, String userArgumentName) {
        if(userArgument == null){
            throw new UserInformationException(userArgumentName + " of user can not be null.");
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

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setRole(UserRole role) {
        this.userRole = role;
    }

    public void setCoachInformation(int coachXp, String introduction, String availability, Set<Topic> topics) {
        this.coachInformation = CoachInformation.CoachInformationBuilder.aCoachInformation()
                .withCoachXp(coachXp)
                .withIntroduction(introduction)
                .withAvailability(availability)
                .withTopics(topics).build();
    }

    public static final class UserBuilder {
        private String firstName;
        private String lastName;
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
