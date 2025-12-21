package com.secure.notes.web.resources;

import com.secure.notes.domain.entity.User;
import com.secure.notes.domain.service.UserService;
import com.secure.notes.security.services.TOTPService;
import com.secure.notes.security.services.impl.UserDetailsImpl;
import com.secure.notes.web.dtos.mappers.UserMapper;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.secure.notes.common.constants.UrlConstant.USER_URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(USER_URL)
public class UserResource {

    private final UserMapper userMapper;
    private final UserService userService;
    private final TOTPService totpService;

    @GetMapping
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetailsImpl userDetail = (UserDetailsImpl) userDetails;
        return ResponseEntity.ok(userMapper.toDTO(userDetail.getUser()));
    }

    @PostMapping("/enable-2fa")
    public ResponseEntity<String> enable2FA() {
        User user = userService.getUserFromAuthentication();
        GoogleAuthenticatorKey secret = userService.generate2FASecret(user);
        String qrCodeURL = totpService.getQRCodeURL(secret, user.getUsername());
        return ResponseEntity.ok(qrCodeURL);
    }

    @PostMapping("/disable-2fa")
    public ResponseEntity<String> disable2FA() {
        User user = userService.getUserFromAuthentication();
        userService.disable2FA(user.getId());
        return ResponseEntity.ok("2FA is disabled for user [%d]".formatted(user.getId()));
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<String> verifyCode(@RequestParam int code) {
        User user = userService.getUserFromAuthentication();
        boolean isValid = userService.validate2FACode(user.getId(), code);

        if (isValid) {
            userService.enable2FA(user.getId());
            return ResponseEntity.ok("2FA Verified");
        }

        return ResponseEntity.badRequest().body("Invalid 2FA code.");
    }

    @GetMapping("/2fa-status")
    public ResponseEntity<?> get2FAStatus() {
        User user = userService.getUserFromAuthentication();
        return ResponseEntity.ok(Map.of("is2FAEnabled", user.isTwoFactorEnabled()));
    }

}
