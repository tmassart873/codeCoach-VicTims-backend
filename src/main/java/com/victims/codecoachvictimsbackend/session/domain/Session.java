package com.victims.codecoachvictimsbackend.session.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "fk_coachee_id")
    private UUID coacheeId;
    @Column(name = "coach_id")
    private UUID coachId;
    @Column(name = "subject")
    private String subject;
    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "time")
    private LocalTime time;
    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private SessionLocation location;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "isvalid")
    private boolean isValid;

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    public Session(SessionBuilder sessionBuilder) {
        this.id = UUID.randomUUID();
        this.coacheeId = sessionBuilder.coacheeId;
        this.coachId = sessionBuilder.coachId;
        this.subject = sessionBuilder.subject;
        this.date = sessionBuilder.date;
        this.time = sessionBuilder.time;
        this.location = sessionBuilder.location;
        this.remarks = sessionBuilder.remarks;
        this.isValid = dateValidation(this.date);
    }

    public Session() {
    }

    public static final class SessionBuilder {
        private UUID coacheeId;
        private UUID coachId;
        private String subject;
        private LocalDate date;
        private LocalTime time;
        private SessionLocation location;
        private String remarks;


        public SessionBuilder() {

        }

        public static SessionBuilder sessionBuilder() {
            return new SessionBuilder();
        }

        public SessionBuilder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public SessionBuilder withDate(String date) {
            this.date = LocalDate.parse(date, dateFormatter);
            return this;
        }

        public SessionBuilder withTime(String time) {
            this.time = LocalTime.parse(time, timeFormatter);
            return this;
        }

        public SessionBuilder withLocation(SessionLocation location) {
            this.location = location;
            return this;
        }

        public SessionBuilder withRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public SessionBuilder withCoacheeId(String id) {
            this.coacheeId = UUID.fromString(id);
            return this;
        }

        public SessionBuilder withCoachId(String id) {
            this.coachId = UUID.fromString(id);
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

    public boolean dateValidation(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            return false;
        }

        return true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCoacheeId() {
        return coacheeId;
    }

    public void setCoacheeId(UUID coacheeId) {
        this.coacheeId = coacheeId;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {

        this.date = LocalDate.parse(date, dateFormatter);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = LocalTime.parse(time, timeFormatter);
    }

    public SessionLocation getLocation() {
        return location;
    }

    public void setLocation(SessionLocation location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean getIsValid() {
        return isValid;
    }
}
