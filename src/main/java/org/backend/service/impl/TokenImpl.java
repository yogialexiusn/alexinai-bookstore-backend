package org.backend.service.impl;

import lombok.AllArgsConstructor;
import org.backend.entity.Token;
import org.backend.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenImpl {

    private final TokenRepository tokenRepository;

    public void saveConfirmationToken(Token token) {
        tokenRepository.save(token);
    }

    public Token getToken(String tokenString) {
        return tokenRepository.findByToken(tokenString);
    }

    public int setConfirmedAt(String token) {
        return tokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
