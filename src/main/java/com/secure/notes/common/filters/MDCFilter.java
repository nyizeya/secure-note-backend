package com.secure.notes.common.filters;

import com.secure.notes.common.services.UserActivityLogAspectService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class MDCFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String USERNAME = "username";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String SESSION_ID = "sessionId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
            MDC.put(USERNAME, getUsername());
            MDC.put(IP_ADDRESS, UserActivityLogAspectService.getClientIpAddress(request));
            MDC.put(SESSION_ID, request.getSession().getId());
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID);
            MDC.remove(USERNAME);
            MDC.remove(IP_ADDRESS);
            MDC.remove(SESSION_ID);
        }

    }

    private String getUsername() {
        return getCurrentLoggedInUser().orElse("Anonymous");
    }

    public static Optional<String> getCurrentLoggedInUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(context.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) return null;

        if (authentication.getPrincipal() instanceof UserDetails userDetails) return userDetails.getUsername();

        if (authentication.getPrincipal() instanceof String username) return username;

        return null;
    }

}
