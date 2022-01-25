package com.victims.codecoachvictimsbackend.session.mapper;

import com.victims.codecoachvictimsbackend.session.domain.Session;
import com.victims.codecoachvictimsbackend.session.domain.SessionDto;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public Session toEntity(SessionDto sessionDto){
        return new Session.SessionBuilder()
                .withCoacheeId(sessionDto.coacheeId())
                .withCoachId(sessionDto.coachId())
                .withSubject(sessionDto.subject())
                .withDate(sessionDto.date())
                .withTime(sessionDto.time())
                .withLocation(sessionDto.location())
                .withRemarks(sessionDto.remarks())
                .build();
    }

    public SessionDto toDto(Session session){
        return new SessionDto(
                session.getId().toString(),
                session.getCoacheeId().toString(),
                session.getCoachId().toString(),
                session.getSubject(),
                session.getDate().toString(),
                session.getTime().toString(),
                session.getLocation(),
                session.getRemarks(),
                session.getIsValid()
        );
    }
}
