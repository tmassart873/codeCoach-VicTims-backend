package com.victims.codecoachvictimsbackend.session.repository;

import com.victims.codecoachvictimsbackend.session.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

}
