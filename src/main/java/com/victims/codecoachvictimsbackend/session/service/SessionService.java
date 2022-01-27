package com.victims.codecoachvictimsbackend.session.service;

import com.victims.codecoachvictimsbackend.session.domain.Session;
import com.victims.codecoachvictimsbackend.session.domain.SessionDto;
import com.victims.codecoachvictimsbackend.session.mapper.SessionMapper;
import com.victims.codecoachvictimsbackend.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        Session requestedSession = sessionMapper.toEntity(sessionDto);
        Session savedRequestedSession = sessionRepository.save(requestedSession);
        return sessionMapper.toDto(savedRequestedSession);
    }
}