package com.secure.notes.security.services;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface TOTPService {

    GoogleAuthenticatorKey generateSecretKey();

    String getQRCodeURL(GoogleAuthenticatorKey secret, String username);

    boolean verifyCode(String secret, int code);
}
