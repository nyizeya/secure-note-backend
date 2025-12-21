package com.secure.notes.domain.service;

import com.secure.notes.domain.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenService {
    void savePasswordResetToken(PasswordResetToken passwordResetToken);

    Optional<PasswordResetToken> findByToken(String token);
}
