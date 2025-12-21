package com.secure.notes.config;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.entity.Role;
import com.secure.notes.domain.entity.User;
import com.secure.notes.domain.service.RoleService;
import com.secure.notes.domain.service.UserService;
import com.secure.notes.security.jwt.services.JwtService;
import com.secure.notes.security.services.impl.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserService userService;
    private final RoleService roleService;

    private String username;
    private String idAttributeKey;

    private static final String GITHUB = "github";
    private static final String GOOGLE = "google";

    @Value("${client.url}")
    private String FRONTEND_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        final String CLIENT_REG_ID = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        DefaultOAuth2User principal = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String email = attributes.getOrDefault("email", "").toString();
        String name = attributes.getOrDefault("name", "").toString();

        if (GITHUB.equals(CLIENT_REG_ID) || GOOGLE.equals(CLIENT_REG_ID))
        {
            if (GITHUB.equals(CLIENT_REG_ID)) {
                username = attributes.getOrDefault("login", "").toString();
                idAttributeKey = "id";
            } else if (GOOGLE.equals(CLIENT_REG_ID)) {
                username = email.split("@")[0];
                idAttributeKey = "sub";
            } else {
                username = "";
                idAttributeKey = "id";
            }

            log.info("Hello OAUTH : email [{}], name [{}], username [{}]", email, name, username);

            userService.getUserByEmail(email).ifPresentOrElse(
                    user -> {
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())), attributes, idAttributeKey
                        );
                        Authentication auth = new OAuth2AuthenticationToken(
                                oauthUser, oauthUser.getAuthorities(), CLIENT_REG_ID
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    },
                    () -> {
                        Role role = roleService.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new EntityNotFoundException("Role User not found"));
                        User user = new User(username, email);
                        user.setRole(role);
                        user.setSignUpMethod(CLIENT_REG_ID);
                        userService.registerUser(user);
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())), attributes, idAttributeKey
                        );
                        Authentication auth = new OAuth2AuthenticationToken(
                            oauthUser, oauthUser.getAuthorities(), CLIENT_REG_ID
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
            );
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        String targetUrl = UriComponentsBuilder.fromUriString(FRONTEND_URL + "/oauth2/redirect")
                .queryParam("token", createJwtTokenForOAuthUser(email))
                .build().toUriString();

        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }


    private String createJwtTokenForOAuthUser(String email) {
        UserDetailsImpl userDetails = new UserDetailsImpl(userService.getUserByEmail(email).get());
        return jwtService.generateTokenFromUsername(userDetails);
    }

}
