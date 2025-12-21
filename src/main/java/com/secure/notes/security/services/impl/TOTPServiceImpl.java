package com.secure.notes.security.services.impl;

import com.secure.notes.security.services.TOTPService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TOTPServiceImpl implements TOTPService {

    private final GoogleAuthenticator gAuth;

    public TOTPServiceImpl() {
        this.gAuth = new GoogleAuthenticator();
    }

    @Override
    public GoogleAuthenticatorKey generateSecretKey() {
        return gAuth.createCredentials();
    }

    @Override
    public String getQRCodeURL(GoogleAuthenticatorKey secret, String username) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("Secured Notes Application", username, secret);
    }

    @Override
    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }

}
