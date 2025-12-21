package com.secure.notes.domain.service.impl;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.common.exceptions.InvalidOperationException;
import com.secure.notes.common.services.EmailService;
import com.secure.notes.common.utils.CommonUtils;
import com.secure.notes.domain.entity.PasswordResetToken;
import com.secure.notes.domain.entity.Role;
import com.secure.notes.domain.entity.User;
import com.secure.notes.domain.repository.UserRepository;
import com.secure.notes.domain.service.PasswordResetTokenService;
import com.secure.notes.domain.service.RoleService;
import com.secure.notes.domain.service.UserService;
import com.secure.notes.security.services.TOTPService;
import com.secure.notes.web.dtos.UserDTO;
import com.secure.notes.web.dtos.mappers.UserMapper;
import com.secure.notes.web.dtos.request.ResetPasswordRequest;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${client.url}")
    private String CLIENT_URL;

    private final UserMapper userMapper;
    private final RoleService roleService;
    private final TOTPService totpService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordResetTokenService;

    @Transactional
    @Override
    public void updateUserRole(Long userId, AppRole appRole) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> CommonUtils.createEntityNotFoundException("User", "id", userId));
        Role role = roleService.findByRoleName(appRole).orElseGet(() -> roleService.createNewRole(appRole));
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserDTO createNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(userRepository.findById(id).orElseThrow(() -> CommonUtils.createEntityNotFoundException("User", "id", id)));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> CommonUtils.createEntityNotFoundException("User", "username", username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    @Transactional
    @Override
    public CompletableFuture<Boolean> generatePasswordResetToken(String email) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating a password reset token...");
            User user = userRepository.findUserByEmail(email).orElseThrow(() -> CommonUtils.createEntityNotFoundException("User", "email", email));
            String token = UUID.randomUUID().toString();
            Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
            PasswordResetToken passwordResetToken = new PasswordResetToken(user, token, expiryDate);
            passwordResetTokenService.savePasswordResetToken(passwordResetToken);

            String resetUrl = CLIENT_URL + "/reset-password?token=" + token;

            try {
                emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }).exceptionally((e) -> {
            e.printStackTrace();
            return false;
        });
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(request.getToken()).orElseThrow(() ->
                CommonUtils.createEntityNotFoundException("Password request token", "token", request.getToken()));

        if (passwordResetToken.isUsed())
            throw new InvalidOperationException("Password reset token has already been used.");

        if (passwordResetToken.getExpiryDate().isBefore(Instant.now()))
            throw new InvalidOperationException("Password reset token has expired.");

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        passwordResetToken.setUsed(true);
        passwordResetTokenService.savePasswordResetToken(passwordResetToken);
    }

    @Transactional
    @Override
    public void enable2FA(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setTwoFactorEnabled(true);
            userRepository.save(user);
            log.info("Enabled 2FA for user [{}]", userId);
        });
    }

    @Transactional
    @Override
    public void disable2FA(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setTwoFactorEnabled(false);
            userRepository.save(user);
            log.info("Disabled 2FA for user [{}]", userId);
        });
    }

    @Override
    public GoogleAuthenticatorKey generate2FASecret(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> CommonUtils.createEntityNotFoundException("User", "id", userId));
        return generate2FASecret(user);
    }

    @Transactional
    @Override
    public GoogleAuthenticatorKey generate2FASecret(User user) {
        GoogleAuthenticatorKey key = totpService.generateSecretKey();
        user.setTwoFactorSecret(key.getKey());
        userRepository.save(user);
        return key;
    }

    @Override
    public boolean validate2FACode(Long userId, int code) {
        User user = userRepository.findById(userId).orElseThrow(() -> CommonUtils.createEntityNotFoundException("User", "id", userId));
        return totpService.verifyCode(user.getTwoFactorSecret(), code);
    }

    @Override
    public User getUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return getUserByUsername(userDetails.getUsername());
    }
}
