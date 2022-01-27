package com.victims.codecoachvictimsbackend.session.domain;

public record SessionDto(
        String id,
        String coacheeId,
        String coachId,
        String subject,
        String date,
        String time,
        SessionLocation location,
        String remarks,
        Boolean isValid

) {
}
