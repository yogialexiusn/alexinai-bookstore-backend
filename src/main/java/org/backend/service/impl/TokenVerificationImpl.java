package org.backend.service.impl;

import lombok.AllArgsConstructor;
import org.backend.entity.TokenVerification;
import org.backend.repository.TokenVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TokenVerificationImpl {

    private final TokenVerificationRepository tokenVerificationRepository;

    public void saveConfirmationToken(TokenVerification tokenVerification) {
        tokenVerificationRepository.save(tokenVerification);
    }

    public TokenVerification getToken(String tokenString) {
        return tokenVerificationRepository.findByTokenVerification(tokenString);
    }

    public int setConfirmedAt(String token) {
        return tokenVerificationRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
