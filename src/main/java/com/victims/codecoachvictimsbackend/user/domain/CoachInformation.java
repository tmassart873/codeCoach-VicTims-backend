package com.victims.codecoachvictimsbackend.user.domain;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "coach_information")
public class CoachInformation {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "coach_xp")
    private Integer coachXp;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "availability")
    private String availability;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "coach_information_topics",
            joinColumns = @JoinColumn(name = "coach_information_id"),
            inverseJoinColumns = @JoinColumn(name = "topics_id"))
    private Set<Topic> topics;

    protected CoachInformation() {
    }

    public CoachInformation(CoachInformationBuilder coachInfoBuilder) {
        this.id = UUID.randomUUID();
        this.coachXp = coachInfoBuilder.coachXp;
        this.introduction = coachInfoBuilder.introduction;
        this.availability = coachInfoBuilder.availability;
        this.topics = coachInfoBuilder.topics;
    }

    public UUID getId() {
        return id;
    }

    public Integer getCoachXp() {
        return coachXp;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getAvailability() {
        return availability;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public static final class CoachInformationBuilder {
        private Integer coachXp;
        private String introduction;
        private String availability;
        private Set<Topic> topics;

        private CoachInformationBuilder() {
        }

        public static CoachInformationBuilder aCoachInformation() {
            return new CoachInformationBuilder();
        }

        public CoachInformation build() {
            return new CoachInformation(this);
        }

        public CoachInformationBuilder withCoachXp(Integer coachXp) {
            this.coachXp = coachXp;
            return this;
        }

        public CoachInformationBuilder withIntroduction(String introduction) {
            this.introduction = introduction;
            return this;
        }

        public CoachInformationBuilder withAvailability(String availability) {
            this.availability = availability;
            return this;
        }

        public CoachInformationBuilder withTopics(Set<Topic> topics) {
            this.topics = topics;
            return this;
        }

    }
}
