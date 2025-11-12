package com.personalproject.llmmanager.users.application.service;

import com.personalproject.llmmanager.users.port.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenResetScheduler {
    private final UsersRepository usersRepository;

    /**
     * 매일 자정(00:00)에 모든 사용자의 토큰 사용량을 초기화합니다.
     * Cron 표현식: 초(0) 분(0) 시(0) 일(*) 월(*) 요일(*)
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetAllUserTokens() {
        log.info("토큰 리셋 시작: {}", LocalDateTime.now());
        int resetAllUsers = usersRepository.resetAllUserTokens();
        log.info("토큰 리셋 완료된 사용자 수: {}", resetAllUsers);
    }
}
