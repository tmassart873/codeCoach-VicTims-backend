package com.victims.codecoachvictimsbackend.session.service;

import com.victims.codecoachvictimsbackend.session.domain.Session;
import com.victims.codecoachvictimsbackend.session.domain.SessionDto;
import com.victims.codecoachvictimsbackend.session.domain.SessionLocation;
import com.victims.codecoachvictimsbackend.session.mapper.SessionMapper;
import com.victims.codecoachvictimsbackend.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SessionService {
    private final SessionMapper sessionMapper;
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionMapper sessionMapper, SessionRepository sessionRepository) {
        this.sessionMapper = sessionMapper;
        this.sessionRepository = sessionRepository;
    }

    public SessionDto requestSession(SessionDto sessionDto) {
        return sessionMapper.toDto(sessionRepository.save(sessionMapper.toEntity(sessionDto)));
    }
}