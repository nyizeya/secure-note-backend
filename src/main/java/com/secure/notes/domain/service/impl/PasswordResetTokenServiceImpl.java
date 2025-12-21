package com.secure.notes.domain.service.impl;

import com.secure.notes.domain.entity.PasswordResetToken;
import com.secure.notes.domain.repository.PasswordResetTokenRepository;
import com.secure.notes.domain.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository repository;

    /**
     * Since Transaction is opened in invoking methods, we don't need to create new Transaction here with @Transactional.
     */

    @Override
    public void savePasswordResetToken(PasswordResetToken passwordResetToken) {
        repository.save(passwordResetToken);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return repository.findByToken(token);
    }

}
