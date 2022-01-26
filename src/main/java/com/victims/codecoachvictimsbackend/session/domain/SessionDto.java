package com.victims.codecoachvictimsbackend.session.domain;

import java.time.LocalDate;

public record SessionDto(
     String id,
     String coacheeId,
     String coachId,
     String subject,
     String date,
     String time,
     SessionLocation location,
     String remarks,
     boolean isValid

) {

}
