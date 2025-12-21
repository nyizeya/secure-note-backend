package com.secure.notes.common.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetUrl) throws MessagingException {
        log.info("Sending password reset mail to {}", to);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' enables multipart (for HTML content)
        helper.setTo(to);
        helper.setSubject("Password Reset Request");
        helper.setText("<p>Click the link below to reset your password:</p><p><a href=\"" + resetUrl + "\">Reset Password</a></p>", true);
        mailSender.send(message);
    }

}
