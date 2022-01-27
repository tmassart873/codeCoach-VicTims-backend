package com.victims.codecoachvictimsbackend.session.repository;

import com.victims.codecoachvictimsbackend.session.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

}
