package com.secure.notes.domain.service;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.entity.User;
import com.secure.notes.web.dtos.UserDTO;
import com.secure.notes.web.dtos.request.ResetPasswordRequest;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    void updateUserRole(Long userId, AppRole role);

    UserDTO createNewUser(User user);

    User registerUser(User user);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    User getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    List<UserDTO> findAllUsers();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    CompletableFuture<Boolean> generatePasswordResetToken(String email);

    void resetPassword(ResetPasswordRequest request);

    void enable2FA(Long userId);

    void disable2FA(Long userId);

    GoogleAuthenticatorKey generate2FASecret(Long userId);

    @Transactional
    GoogleAuthenticatorKey generate2FASecret(User user);

    boolean validate2FACode(Long userId, int code);

    User getUserFromAuthentication();
}
